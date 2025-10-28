package com.winterhavenmc.library.messagebuilder.adapters.resources.sound;

import com.winterhavenmc.library.messagebuilder.adapters.resources.language.LanguageResourceMessage;
import com.winterhavenmc.library.messagebuilder.core.ports.resources.ResourceLoader;
import com.winterhavenmc.library.messagebuilder.models.configuration.ConfigRepository;
import com.winterhavenmc.library.messagebuilder.models.configuration.LanguageTag;

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
import static com.winterhavenmc.library.messagebuilder.adapters.resources.sound.SoundResourceMessage.*;


public final class YamlSoundResourceLoader implements ResourceLoader
{
	private final Plugin plugin;
	private final Supplier<YamlConfiguration> configurationSupplier;
	private final ConfigRepository configRepository;


	public YamlSoundResourceLoader(final Plugin plugin, final ConfigRepository configRepository)
	{
		this(plugin, YamlConfiguration::new, configRepository);
	}


	public YamlSoundResourceLoader(final Plugin plugin,
								   final Supplier<YamlConfiguration> configurationSupplier,
								   final ConfigRepository configRepository)
	{
		this.plugin = plugin;
		this.configurationSupplier = configurationSupplier;
		this.configRepository = configRepository;
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
				plugin.getLogger().info(SOUND_RESOURCE_LOAD_SUCCESS.getLocalizedMessage(configRepository.locale(), RESOURCE_NAME.toString()));
			}
			else
			{
				plugin.getLogger().warning(SOUND_RESOURCE_LOAD_MISSING.getLocalizedMessage(configRepository.locale(), RESOURCE_NAME.toString()));
			}
		}
		catch (IOException | InvalidConfigurationException e)
		{
			plugin.getLogger().warning(SOUND_RESOURCE_UNREADABLE.getLocalizedMessage(configRepository.locale(), RESOURCE_NAME.toString()));
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
						.getLocalizedMessage(configRepository.locale(), RESOURCE_NAME));
				return config;
			}
			else
			{
				plugin.getLogger().severe(LanguageResourceMessage.LANGUAGE_RESOURCE_FALLBACK_MISSING
						.getLocalizedMessage(configRepository.locale(), RESOURCE_NAME));
			}
		}
		catch (IOException | InvalidConfigurationException exception)
		{
			plugin.getLogger().severe(LanguageResourceMessage.LANGUAGE_RESOURCE_FALLBACK_FAILED
					.getLocalizedMessage(configRepository.locale(), RESOURCE_NAME));
		}

		return new YamlConfiguration(); // always return a safe config
	}
}
