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

package com.winterhavenmc.library.messagebuilder.resources.configuration;

import org.bukkit.plugin.Plugin;

import java.time.ZoneId;
import java.util.Locale;
import java.util.function.Supplier;


/**
 * A configuration-backed provider for both {@link Locale} and {@link ZoneId} settings.
 * <p>
 * This class wraps a {@link LocaleSetting} and provides a centralized access point
 * for retrieving the user's desired locale and timezone, both of which are commonly
 * required for time/date formatting and localization operations throughout the plugin.
 * </p>
 *
 * <p>
 * The {@code LocaleProvider} resolves configuration values from {@code config.yml}
 * in the following order:
 * </p>
 * <ul>
 *   <li>{@code locale} key (preferred)</li>
 *   <li>{@code language} key (fallback)</li>
 *   <li>{@link Locale#getDefault()} (system fallback)</li>
 * </ul>
 *
 * <p>
 * The {@code timezone} configuration value, if present and valid, is resolved into a
 * {@link ZoneId}. If the value is invalid or missing, the system default time zone is used.
 * </p>
 *
 * @see com.winterhavenmc.library.messagebuilder.resources.configuration.ConfigProvider
 * @see com.winterhavenmc.library.messagebuilder.resources.configuration.LocaleSetting
 * @see java.util.Locale
 * @see java.time.ZoneId
 */
public class LocaleProvider implements ConfigProvider<LocaleSetting>
{
	private final static String timeZoneSetting = "timezone";
	private final Supplier<LocaleSetting> localeSettingSupplier;
	private final Supplier<ZoneId> zoneIdSupplier;


	/**
	 * Enum representing the recognized configuration keys for language settings.
	 */
	enum LocaleField
	{
		LOCALE("locale"),
		LANGUAGE("language");

		private final String string;
		LocaleField(String string) { this.string = string; }
		@Override public String toString() { return this.string; }
	}


	/**
	 * Constructs a new {@code LocaleProvider} with injected suppliers.
	 *
	 * @param localeSettingSupplier supplies the current {@link LocaleSetting}
	 * @param zoneIdSupplier supplies the current {@link ZoneId}
	 */
	private LocaleProvider(final Supplier<LocaleSetting> localeSettingSupplier, final Supplier<ZoneId> zoneIdSupplier)
	{
		this.localeSettingSupplier = localeSettingSupplier;
		this.zoneIdSupplier = zoneIdSupplier;
	}


	/**
	 * Factory method to construct a {@code LocaleProvider} using a plugin's configuration.
	 * <p>
	 * This method attempts to extract valid configuration values in the following order:
	 * </p>
	 * <ol>
	 *   <li>{@code locale} field (must be a valid BCP-47 language tag)</li>
	 *   <li>{@code language} field (fallback, same format)</li>
	 *   <li>System default locale and timezone as final fallback</li>
	 * </ol>
	 *
	 * @param plugin the plugin whose configuration will be consulted
	 * @return a new {@code LocaleProvider} with dynamic access to locale and time zone settings
	 */
	public static LocaleProvider create(final Plugin plugin)
	{
		return new LocaleProvider(
				() -> new LocaleSetting(LanguageTag.of(plugin.getConfig().getString(LocaleField.LOCALE.toString()))
						.orElse(LanguageTag.of(plugin.getConfig().getString(LocaleField.LANGUAGE.toString()))
						.orElse(LanguageTag.getSystemDefault()))),
				() -> getValidZoneId(plugin));
	}


	/**
	 * Returns the resolved {@link LocaleSetting} based on the plugin configuration.
	 *
	 * @return a locale setting encapsulating a {@link LanguageTag}
	 */
	@Override
	public LocaleSetting get()
	{
		return localeSettingSupplier.get();
	}


	/**
	 * Returns the resolved {@link LanguageTag} representing the locale tag.
	 *
	 * @return a valid LanguageTag
	 */
	public LanguageTag getLanguageTag()
	{
		return localeSettingSupplier.get().languageTag();
	}


	/**
	 * Returns the {@link Locale} object derived from the configuration.
	 *
	 * @return a Java {@code Locale}
	 */
	public Locale getLocale()
	{
		return localeSettingSupplier.get().languageTag().getLocale();
	}


	/**
	 * Returns the configured {@link ZoneId} if valid, or the system default otherwise.
	 *
	 * @return the applicable {@code ZoneId}
	 */
	public ZoneId getZoneId()
	{
		return zoneIdSupplier.get();
	}


	/**
	 * Resolves the configured {@code timezone} string into a valid {@link ZoneId}.
	 * <p>
	 * If the {@code timezone} value is present in the configuration and matches
	 * one of the available {@link ZoneId} IDs, it is used. Otherwise, this method
	 * falls back to the system default time zone.
	 * </p>
	 *
	 * @param plugin the plugin whose configuration is queried for the timezone
	 * @return a valid {@code ZoneId}, or the system default if no valid setting is found
	 */
	static ZoneId getValidZoneId(final Plugin plugin)
	{
		String timezone = plugin.getConfig().getString(timeZoneSetting);

		return (timezone != null && ZoneId.getAvailableZoneIds().contains(timezone))
				? ZoneId.of(timezone)
				: ZoneId.systemDefault();
	}

}
