package com.winterhavenmc.library.messagebuilder.adapters.resources.sound;

import com.winterhavenmc.library.messagebuilder.core.ports.resources.ConfigurationProvider;
import com.winterhavenmc.library.messagebuilder.core.ports.resources.ResourceInstaller;
import com.winterhavenmc.library.messagebuilder.core.ports.resources.ResourceLoader;
import com.winterhavenmc.library.messagebuilder.core.ports.resources.ResourceManager;
import com.winterhavenmc.library.messagebuilder.core.ports.resources.SectionProvider;

import org.bukkit.plugin.Plugin;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;


public final class YamlSoundResourceManager implements ResourceManager
{
	private final Plugin plugin;
	private final ResourceLoader resourceLoader;
	private final ResourceInstaller resourceInstaller;
	private Configuration soundConfiguration;


	YamlSoundResourceManager(final Plugin plugin,
							 final ResourceLoader resourceLoader,
							 final ResourceInstaller resourceInstaller)
	{
		this.plugin = plugin;
		this.resourceInstaller = resourceInstaller;
		this.resourceLoader = resourceLoader;

		reload();
	}


	YamlSoundResourceManager(final Plugin plugin,
							 final ResourceLoader resourceLoader,
							 final ResourceInstaller resourceInstaller,
							 final FileConfiguration configuration)
	{
		this.plugin = plugin;
		this.resourceInstaller = resourceInstaller;
		this.resourceLoader = resourceLoader;
		this.soundConfiguration = configuration;
	}


	/**
	 * reload sound configuration from yaml file in plugin data folder
	 */
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
		return () -> soundConfiguration;
	}


	@Override
	public SectionProvider getSectionProvider(String stringName)
	{
		return () -> soundConfiguration.getConfigurationSection(stringName);
	}

}
