package com.winterhavenmc.library.messagebuilder.adapters.resources.configuration;

import com.winterhavenmc.library.messagebuilder.models.configuration.LocaleSetting;
import org.bukkit.plugin.Plugin;

import java.util.function.Supplier;


public class GlobalLocaleSetting implements Supplier<LocaleSetting>
{
	private final Plugin plugin;


	GlobalLocaleSetting(final Plugin plugin)
	{
		this.plugin = plugin;
	}


	/**
	 * Returns the locale setting from the plugin config.yml file. Returns system default locale if locale configuration
	 * setting is not present in the plugin config.yml file.
	 *
	 * @return a LocaleSetting object encapsulating the configured or system default locale setting
	 */
	public LocaleSetting get()
	{
		return new LoggingLocaleSetting(plugin).get();
	}

}
