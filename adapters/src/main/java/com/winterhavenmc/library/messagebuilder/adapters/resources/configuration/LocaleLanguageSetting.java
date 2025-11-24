package com.winterhavenmc.library.messagebuilder.adapters.resources.configuration;

import com.winterhavenmc.library.messagebuilder.models.configuration.LanguageSetting;
import org.bukkit.plugin.Plugin;

import java.util.function.Supplier;

public class LocaleLanguageSetting implements Supplier<LanguageSetting>
{
	private final Plugin plugin;


	LocaleLanguageSetting(final Plugin plugin)
	{
		this.plugin = plugin;
	}


	/**
	 * Returns the language setting from the plugin config.yml file. Returns DEFAULT_LANGUAGE_SETTING
	 * declared in this class if language configuration setting is not present in the plugin config.yml file.
	 *
	 * @return a LocaleLanguageSetting object encapsulating the configured or default language setting string from
	 * the plugin config.yml file
	 */
	public LanguageSetting get()
	{
		return (plugin.getConfig().contains(BukkitConfigRepository.ConfigKey.LANGUAGE.key()))
				? new LanguageSetting(plugin.getConfig().getString(BukkitConfigRepository.ConfigKey.LANGUAGE.key()))
				: new LanguageSetting(BukkitConfigRepository.DEFAULT_LANGUAGE_SETTING);
	}

}
