package com.winterhavenmc.library.messagebuilder.adapters.resources.sound;

import com.winterhavenmc.library.messagebuilder.core.ports.resources.ResourceInstaller;
import com.winterhavenmc.library.messagebuilder.core.ports.resources.ResourceLoader;
import com.winterhavenmc.library.messagebuilder.core.ports.resources.ResourceManager;

import com.winterhavenmc.library.messagebuilder.models.configuration.LanguageTag;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

import static com.winterhavenmc.library.messagebuilder.adapters.resources.sound.YamlSoundResourceInstaller.FILE_NAME;


public class YamlSoundResourceManager implements ResourceManager
{
	private final Plugin plugin;
	private final SoundResourceLoader resourceLoader;
	private final SoundResourceInstaller resourceInstaller;
	private FileConfiguration soundConfiguration;


	YamlSoundResourceManager(final Plugin plugin,
							 final SoundResourceLoader resourceLoader,
							 final SoundResourceInstaller resourceInstaller)
	{
		this.plugin = plugin;
		this.resourceInstaller = resourceInstaller;
		this.resourceLoader = resourceLoader;

		installResources();
		this.soundConfiguration = resourceLoader.load();
	}


	YamlSoundResourceManager(final Plugin plugin,
							 final SoundResourceLoader resourceLoader,
							 final SoundResourceInstaller resourceInstaller,
							 final FileConfiguration configuration)
	{
		this.plugin = plugin;
		this.resourceInstaller = resourceInstaller;
		this.resourceLoader = resourceLoader;
		this.soundConfiguration = configuration;
	}


	public static String getResourceName(LanguageTag fallback)
	{

	}


	private void installResources()
	{
		resourceInstaller.install();
	}


	public FileConfiguration load()
	{
		FileConfiguration newConfig = resourceLoader.load();

		if (newConfig != null)
		{
			soundConfiguration = newConfig;
		}

		return soundConfiguration;
	}


	/**
	 * Load sound configuration from yaml file in plugin data folder
	 */
	public boolean reload()
	{
		// get File object for sound file
		File soundFile = new File(plugin.getDataFolder().getPath(), FILE_NAME);

		// copy resource to plugin data directory if it does not already exist there
		plugin.saveResource(FILE_NAME, false);

		try
		{
			soundConfiguration.load(soundFile);
			return true;
		}
		catch (IOException ioException)
		{
			plugin.getLogger().severe(ioException.getLocalizedMessage());
		}
		catch (InvalidConfigurationException invalidConfigurationException)
		{
			throw new RuntimeException(invalidConfigurationException);
		}

		return false;
	}

}
