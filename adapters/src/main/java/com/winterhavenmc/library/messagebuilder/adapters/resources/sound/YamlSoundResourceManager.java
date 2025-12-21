package com.winterhavenmc.library.messagebuilder.adapters.resources.sound;

import com.winterhavenmc.library.messagebuilder.core.ports.resources.*;
import com.winterhavenmc.library.messagebuilder.core.ports.resources.sound.SoundResourceManager;

import com.winterhavenmc.library.messagebuilder.models.configuration.ConfigRepository;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.Plugin;


public final class YamlSoundResourceManager implements SoundResourceManager
{
	private final ResourceLoader resourceLoader;
	private final ResourceInstaller resourceInstaller;
	private Configuration soundConfiguration;


	private YamlSoundResourceManager(final ResourceInstaller resourceInstaller, final ResourceLoader resourceLoader)
	{
		this.resourceInstaller = resourceInstaller;
		this.resourceLoader = resourceLoader;

		reload();
	}


	/**
	 * Static factory method returns instance of YamlSoundResourceManager
	 */
	public static YamlSoundResourceManager create(final Plugin plugin,
												  final ConfigRepository configRepository)
	{
		final YamlSoundResourceInstaller resourceInstaller = new YamlSoundResourceInstaller(plugin);
		final YamlSoundResourceLoader resourceLoader = new YamlSoundResourceLoader(plugin, configRepository);

		return new YamlSoundResourceManager(resourceInstaller, resourceLoader);
	}


	@Override
	public ConfigurationProvider getConfigurationProvider()
	{
		return new YamlSoundConfigurationProvider(() -> soundConfiguration);
	}


	/**
	 * Installs sound resource files if not already present in the plugin data folder
	 */
	@Override
	public void installResources()
	{
		resourceInstaller.install();
	}


	/**
	 * reload sound configuration from YAML file in plugin data folder
	 */
	@Override
	public boolean reload()
	{
		resourceInstaller.install();
		Configuration soundConfiguration = resourceLoader.load();

		if (soundConfiguration != null)
		{
			this.soundConfiguration = soundConfiguration;
			return true;
		}

		return false;
	}

}
