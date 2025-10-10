/*
 * Copyright (c) 2025 Tim Savage.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.winterhavenmc.library.messagebuilder.models.configuration;

import org.bukkit.plugin.Plugin;

import java.time.ZoneId;
import java.util.Locale;


public interface LocaleProvider extends ConfigProvider<LocaleSetting>
{
	String TIME_ZONE_SETTING_KEY = "timezone";


	/**
	 * Resolves the configured {@code timezone} string into a valid {@link ZoneId}.
	 * <p>
	 * If the {@code timezone} value is present in the configuration and matches
	 * one of the available {@link ZoneId} recoginized by the JVM, it is used.
	 * Otherwise, this method falls back to the system default time zone.
	 * </p>
	 *
	 * @param plugin the plugin whose configuration is queried for the timezone
	 * @return a valid {@code ZoneId}, or the system default if no valid setting is found
	 */
	static ZoneId getValidZoneId(Plugin plugin)
	{
		String timezone = plugin.getConfig().getString(TIME_ZONE_SETTING_KEY);

		return (timezone != null && ZoneId.getAvailableZoneIds().contains(timezone))
				? ZoneId.of(timezone)
				: ZoneId.systemDefault();
	}

	/**
	 * Returns the resolved {@link LocaleSetting} based on the plugin configuration.
	 *
	 * @return a locale setting encapsulating a {@link LanguageTag}
	 */
	@Override
	LocaleSetting get();

	/**
	 * Returns the resolved {@link LanguageTag} representing the locale tag.
	 *
	 * @return a valid LanguageTag
	 */
	LanguageTag getLanguageTag();

	/**
	 * Returns the {@link Locale} object derived from the configuration.
	 *
	 * @return a Java {@code Locale}
	 */
	Locale getLocale();

	/**
	 * Returns the configured {@link ZoneId} if valid, or the system default otherwise.
	 *
	 * @return the applicable time zone as represented by a {@code ZoneId} object.
	 */
	ZoneId getZoneId();
}
