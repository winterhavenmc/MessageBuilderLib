package com.winterhavenmc.library.messagebuilder.adapters.resources.sound;

import com.winterhavenmc.library.messagebuilder.core.ports.resources.*;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;


public final class YamlSoundResourceManager implements ResourceManager
{
	private final ResourceLoader resourceLoader;
	private final ResourceInstaller resourceInstaller;
	private Configuration soundConfiguration;


	public YamlSoundResourceManager(final ResourceInstaller resourceInstaller, final ResourceLoader resourceLoader)
	{
		this.resourceInstaller = resourceInstaller;
		this.resourceLoader = resourceLoader;

		reload();
	}


	YamlSoundResourceManager(final ResourceLoader resourceLoader,
							 final ResourceInstaller resourceInstaller,
							 final FileConfiguration configuration)
	{
		this.resourceInstaller = resourceInstaller;
		this.resourceLoader = resourceLoader;
		this.soundConfiguration = configuration;
	}


	/**
	 * reload sound configuration from yaml file in plugin data folder
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


	@Override
	public ConfigurationProvider getConfigurationProvider()
	{
		return new YamlSoundConfigurationProvider(() -> soundConfiguration);
	}


	@Override
	public SectionProvider getSectionProvider(String sectionName)
	{
		return new YamlSoundSectionProvider(() -> soundConfiguration, sectionName);
	}


	/**
	 * Installs sound resource files if not already present in the plugin data folder
	 */
	@Override
	public void installResources()
	{
		resourceInstaller.install();
	}

}
