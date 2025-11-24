package com.winterhavenmc.library.messagebuilder.adapters.resources.configuration;

import org.bukkit.plugin.Plugin;

import java.time.ZoneId;
import java.util.function.Supplier;


public class ZoneIdSetting implements Supplier<ZoneId>
{
	private final Plugin plugin;


	ZoneIdSetting(final Plugin plugin)
	{
		this.plugin = plugin;
	}


	/**
	 * Resolves the configured {@code timezone} string into a valid {@link ZoneId}.
	 * <p>
	 * If the {@code timezone} value is present in the configuration and matches
	 * one of the available {@link ZoneId} recognized by the JVM, it is used.
	 * Otherwise, this method falls back to the system default time zone.
	 *
	 * @return a valid {@code ZoneId}, or the system default if no valid setting is found
	 */
	public ZoneId get()
	{
		String timezone = plugin.getConfig().getString(BukkitConfigRepository.ConfigKey.TIME_ZONE.key());

		return (timezone != null && ZoneId.getAvailableZoneIds().contains(timezone))
				? ZoneId.of(timezone)
				: ZoneId.systemDefault();
	}

}
