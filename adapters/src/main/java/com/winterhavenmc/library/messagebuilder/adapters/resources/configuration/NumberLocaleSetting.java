package com.winterhavenmc.library.messagebuilder.adapters.resources.configuration;

import com.winterhavenmc.library.messagebuilder.models.configuration.LanguageTag;
import com.winterhavenmc.library.messagebuilder.models.configuration.LocaleSetting;
import org.bukkit.plugin.Plugin;

import java.util.function.Supplier;


public class NumberLocaleSetting implements Supplier<LocaleSetting>
{
	private final Plugin plugin;


	NumberLocaleSetting(final Plugin plugin)
	{
		this.plugin = plugin;
	}


	public LocaleSetting get()
	{
		return (plugin.getConfig().isString(BukkitConfigRepository.ConfigKey.LOCALE_NUMBER.key()))
				? new LocaleSetting(LanguageTag.of(plugin.getConfig().getString(BukkitConfigRepository.ConfigKey.LOCALE_NUMBER.key()))
				.orElseGet(LanguageTag::getSystemDefault))
				: new LocaleSetting(LanguageTag.of(plugin.getConfig().getString(BukkitConfigRepository.ConfigKey.LOCALE.key()))
				.orElseGet(LanguageTag::getSystemDefault));
	}

}
