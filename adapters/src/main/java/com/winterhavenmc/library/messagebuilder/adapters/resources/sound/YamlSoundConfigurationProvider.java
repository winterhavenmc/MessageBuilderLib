package com.winterhavenmc.library.messagebuilder.adapters.resources.sound;

import com.winterhavenmc.library.messagebuilder.core.ports.resources.ConfigurationProvider;
import org.bukkit.configuration.Configuration;

import java.util.function.Supplier;


public final class YamlSoundConfigurationProvider implements ConfigurationProvider
{
	private final Supplier<Configuration> configurationSupplier;


	public YamlSoundConfigurationProvider(final Supplier<Configuration> configurationSupplier)
	{
		this.configurationSupplier = configurationSupplier;
	}


	@Override
	public Configuration getConfiguration()
	{
		return configurationSupplier.get();
	}

}
