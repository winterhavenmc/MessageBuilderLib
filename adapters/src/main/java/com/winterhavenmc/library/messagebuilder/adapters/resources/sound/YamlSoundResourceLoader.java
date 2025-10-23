package com.winterhavenmc.library.messagebuilder.adapters.resources.sound;

import com.winterhavenmc.library.messagebuilder.adapters.resources.language.LanguageResourceMessage;
import com.winterhavenmc.library.messagebuilder.core.ports.resources.ResourceLoader;
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

import static com.winterhavenmc.library.messagebuilder.adapters.resources.sound.SoundResourceConstant.RESOURCE_NAME;


public final class YamlSoundResourceLoader implements ResourceLoader
{
	private final Plugin plugin;
	private final Supplier<YamlConfiguration> configurationSupplier;
	private final LocaleProvider localeProvider;


	public YamlSoundResourceLoader(final Plugin plugin, final LocaleProvider localeProvider)
	{
		this(plugin, YamlConfiguration::new, localeProvider);
	}


	public YamlSoundResourceLoader(final Plugin plugin,
								   final Supplier<YamlConfiguration> configurationSupplier,
								   final LocaleProvider localeProvider)
	{
		this.plugin = plugin;
		this.configurationSupplier = configurationSupplier;
		this.localeProvider = localeProvider;
	}


	@Override
	public FileConfiguration load()
	{
		YamlConfiguration configuration = configurationSupplier.get();

		try
		{
			File soundConfigFile = new File(plugin.getDataFolder(), RESOURCE_NAME.toString());
			if (soundConfigFile.exists())
			{
				configuration.load(soundConfigFile);
				plugin.getLogger().info("Sound file '" + RESOURCE_NAME + "' successfully loaded.");
			}
			else
			{
				//TODO: provide translations for error messages in this class
				plugin.getLogger().warning("The file '" + RESOURCE_NAME + "' does not exist in the plugin data folder.");
			}
		}
		catch (IOException | InvalidConfigurationException e)
		{
			plugin.getLogger().warning("The file '" + RESOURCE_NAME + "' in the plugin data folder could not be read.");
		}

		return configuration;
	}


	/**
	 * Loads a language YAML file directly from the JAR resource as a last resort.
	 */
	public Configuration loadFromResource(LanguageTag fallback)
	{
		try (InputStream stream = plugin.getResource(RESOURCE_NAME.toString()))
		{
			if (stream != null)
			{
				YamlConfiguration config = configurationSupplier.get();
				config.load(new InputStreamReader(stream, StandardCharsets.UTF_8));
				plugin.getLogger().info(LanguageResourceMessage.LANGUAGE_RESOURCE_FALLBACK_SUCCESS
						.getLocalizedMessage(localeProvider.getLocale(), RESOURCE_NAME));
				return config;
			}
			else
			{
				plugin.getLogger().severe(LanguageResourceMessage.LANGUAGE_RESOURCE_FALLBACK_MISSING
						.getLocalizedMessage(localeProvider.getLocale(), RESOURCE_NAME));
			}
		}
		catch (IOException | InvalidConfigurationException exception)
		{
			plugin.getLogger().severe(LanguageResourceMessage.LANGUAGE_RESOURCE_FALLBACK_FAILED
					.getLocalizedMessage(localeProvider.getLocale(), RESOURCE_NAME));
		}

		return new YamlConfiguration(); // always return a safe config
	}
}
