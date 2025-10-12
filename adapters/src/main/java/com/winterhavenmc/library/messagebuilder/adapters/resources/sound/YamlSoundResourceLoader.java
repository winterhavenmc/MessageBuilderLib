package com.winterhavenmc.library.messagebuilder.adapters.resources.sound;

import com.winterhavenmc.library.messagebuilder.core.ports.resources.language.LanguageResourceMessage;
import com.winterhavenmc.library.messagebuilder.models.configuration.LanguageTag;
import com.winterhavenmc.library.messagebuilder.models.configuration.LocaleProvider;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.nio.charset.StandardCharsets;
import java.util.function.Supplier;

import static com.winterhavenmc.library.messagebuilder.adapters.resources.sound.YamlSoundResourceInstaller.FILE_NAME;


public class YamlSoundResourceLoader
{
	private final Plugin plugin;
	private final Supplier<YamlConfiguration> yamlFactory;
	private final LocaleProvider localeProvider;


	public YamlSoundResourceLoader(final Plugin plugin, final LocaleProvider localeProvider)
	{
		this(plugin, YamlConfiguration::new, localeProvider);
	}


	public YamlSoundResourceLoader(final Plugin plugin,
								   final Supplier<YamlConfiguration> yamlFactory,
								   final LocaleProvider localeProvider)
	{
		this.plugin = plugin;
		this.yamlFactory = yamlFactory;
		this.localeProvider = localeProvider;
	}


	public FileConfiguration load()
	{
		YamlConfiguration configuration = yamlFactory.get();

		try
		{
			File soundConfigFile = new File(plugin.getDataFolder(), FILE_NAME);
			if (soundConfigFile.exists())
			{
				configuration.load(soundConfigFile);
			}
		}
		catch (IOException | InvalidConfigurationException e)
		{
			plugin.getLogger().warning("The file '" + FILE_NAME + "' in the plugin data folder could not be read.");
		}

		return configuration;
	}


	/**
	 * Loads a language YAML file directly from the JAR resource as a last resort.
	 */
	public Configuration loadFromResource(LanguageTag fallback)
	{
		String resourcePath = YamlSoundResourceManager.getResourceName(fallback);
		try (InputStream stream = plugin.getResource(resourcePath))
		{
			if (stream != null)
			{
				YamlConfiguration config = yamlFactory.get();
				config.load(new InputStreamReader(stream, StandardCharsets.UTF_8));
				plugin.getLogger().info(LanguageResourceMessage.LANGUAGE_RESOURCE_FALLBACK_SUCCESS
						.getLocalizedMessage(localeProvider.getLocale(), resourcePath));
				return config;
			}
			else
			{
				plugin.getLogger().severe(LanguageResourceMessage.LANGUAGE_RESOURCE_FALLBACK_MISSING
						.getLocalizedMessage(localeProvider.getLocale(), resourcePath));
			}
		}
		catch (IOException | InvalidConfigurationException exception)
		{
			plugin.getLogger().severe(LanguageResourceMessage.LANGUAGE_RESOURCE_FALLBACK_FAILED
					.getLocalizedMessage(localeProvider.getLocale(), resourcePath));
		}

		return new YamlConfiguration(); // always return a safe config
	}
}
