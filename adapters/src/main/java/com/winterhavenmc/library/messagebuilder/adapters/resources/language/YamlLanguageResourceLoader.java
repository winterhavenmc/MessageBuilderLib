/*
 * Copyright (c) 2022-2025 Tim Savage.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.winterhavenmc.library.messagebuilder.adapters.resources.language;

import com.winterhavenmc.library.messagebuilder.adapters.resources.ResourceMessage;
import com.winterhavenmc.library.messagebuilder.core.ports.resources.ResourceLoader;

import com.winterhavenmc.library.messagebuilder.models.configuration.ConfigRepository;
import com.winterhavenmc.library.messagebuilder.models.configuration.LanguageTag;

import org.bukkit.plugin.Plugin;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Supplier;

import static com.winterhavenmc.library.messagebuilder.adapters.resources.language.LanguageConfigConstant.DEFAULT_LANGUAGE_TAG;


/**
 * This class is responsible for the loading of the language file from the plugin data directory into
 * a configuration object. The configuration object is loaded from file whenever the getConfiguration method
 * is called. The class does not store the configuration; each invocation of the getConfiguration method will
 * result in a new configuration object loaded from the currently configured language file, or the en-US language
 * file if a file for the currently configured language cannot be found in the plugin data directory.
 */
public class YamlLanguageResourceLoader implements ResourceLoader
{
	private final Plugin plugin;
	private final Supplier<YamlConfiguration> yamlFactory;
	private final ConfigRepository configRepository;
	final LanguageTag defaultLanguageTag = LanguageTag.of(DEFAULT_LANGUAGE_TAG.toString()).orElseThrow();


	/**
	 * Class constructor
	 *
	 * @param plugin an instance of the plugin main class
	 */
	public YamlLanguageResourceLoader(final Plugin plugin, final ConfigRepository configRepository)
	{
		this(plugin, YamlConfiguration::new, configRepository);
	}


	/**
	 * Testable constructor allowing custom YamlConfiguration supplier.
	 *
	 * @param plugin      an instance of the plugin main class
	 * @param yamlFactory factory for creating YamlConfiguration instances
	 */
	public YamlLanguageResourceLoader(final Plugin plugin,
									  final Supplier<YamlConfiguration> yamlFactory,
									  final ConfigRepository configRepository)
	{
		this.plugin = plugin;
		this.yamlFactory = yamlFactory;
		this.configRepository = configRepository;
	}


	/**
	 * Gets language tag specified in config.yml.
	 *
	 * @return Optional {@code LanguageTag} or an empty Optional if config setting is null or empty
	 */
	public Optional<LanguageTag> getConfiguredLanguageTag()
	{
		String configLanguageTag = plugin.getConfig().getString(LanguageConfigConstant.CONFIG_LANGUAGE_KEY.toString());
		return (configLanguageTag != null && !configLanguageTag.isBlank())
				? LanguageTag.of(Locale.forLanguageTag(configLanguageTag))
				: LanguageTag.of(DEFAULT_LANGUAGE_TAG.toString());
	}


	public Locale getConfiguredLocale()
	{
		return getConfiguredLanguageTag()
				.map(tag -> Locale.forLanguageTag(tag.toString()))
				.orElse(Locale.getDefault());
	}


	/**
	 * Load the language configuration object for the configured language from file and return it.
	 * The returned configuration object contains no default values loaded, by design.
	 *
	 * @return Configuration - message configuration object
	 */
	@Override
	public Configuration load()
	{
		return getConfiguredLanguageTag()
				.map(tag -> loadWithFallback(tag, defaultLanguageTag))
				.orElseThrow(() -> new IllegalStateException(ResourceMessage.RESOURCE_TAG_MISSING
						.getLocalizedMessage(configRepository.locale())));
	}


	/**
	 * Attempts to load the preferred language file from disk.
	 * If unavailable or invalid, falls back to loading the fallback language directly from the plugin resource.
	 */
	public Configuration loadWithFallback(LanguageTag preferred, LanguageTag fallback)
	{
		File languageFile = new File(plugin.getDataFolder(), YamlLanguageResourceManager.getFileName(preferred));
		YamlConfiguration config = yamlFactory.get();
		boolean success = false;

		try
		{
			config.load(languageFile);
			success = true;
		}
		catch (FileNotFoundException exception)
		{
			plugin.getLogger().warning(ResourceMessage.RESOURCE_NOT_FOUND
					.getLocalizedMessage(configRepository.locale(), languageFile.getName()));
		}
		catch (IOException ioException)
		{
			plugin.getLogger().warning(ResourceMessage.RESOURCE_UNREADABLE
					.getLocalizedMessage(configRepository.locale(), languageFile.getName()));
		}
		catch (InvalidConfigurationException configurationException)
		{
			plugin.getLogger().warning(ResourceMessage.RESOURCE_INVALID_YAML
					.getLocalizedMessage(configRepository.locale(), languageFile.getName()));
		}
		catch (IllegalArgumentException argumentException)
		{
			plugin.getLogger().warning(ResourceMessage.RESOURCE_INVALID_FILE
					.getLocalizedMessage(configRepository.locale(), languageFile.getName(), argumentException.getLocalizedMessage()));
		}
		catch (Exception exception)
		{
			plugin.getLogger().severe(ResourceMessage.RESOURCE_LOAD_EXCEPTION
					.getLocalizedMessage(configRepository.locale(), languageFile.getName()));
		}

		if (success)
		{
			plugin.getLogger().info(ResourceMessage.RESOURCE_LOAD_SUCCESS
					.getLocalizedMessage(configRepository.locale(), languageFile.getName()));
			return config;
		}
		else
		{
			return loadFromResource(fallback);
		}
	}


	/**
	 * Loads a language YAML file directly from the JAR resource as a last resort.
	 */
	public Configuration loadFromResource(LanguageTag fallback)
	{
		String resourcePath = YamlLanguageResourceManager.getResourceName(fallback);
		try (InputStream stream = plugin.getResource(resourcePath))
		{
			if (stream != null)
			{
				YamlConfiguration config = yamlFactory.get();
				config.load(new InputStreamReader(stream, StandardCharsets.UTF_8));
				plugin.getLogger().info(ResourceMessage.RESOURCE_FALLBACK_SUCCESS
						.getLocalizedMessage(configRepository.locale(), resourcePath));
				return config;
			}
			else
			{
				plugin.getLogger().severe(ResourceMessage.RESOURCE_FALLBACK_MISSING
						.getLocalizedMessage(configRepository.locale(), resourcePath));
			}
		}
		catch (IOException | InvalidConfigurationException exception)
		{
			plugin.getLogger().severe(ResourceMessage.RESOURCE_FALLBACK_FAILED
					.getLocalizedMessage(configRepository.locale(), resourcePath));
		}

		return new YamlConfiguration(); // always return a safe config
	}

}
