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

package com.winterhavenmc.library.messagebuilder.adapters.resources.configuration;

import com.winterhavenmc.library.messagebuilder.models.configuration.ConfigRepository;
import com.winterhavenmc.library.messagebuilder.models.configuration.*;
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
 * The {@code LocaleProvider} resolves configuration values from the plugin {@code config.yml}
 * in the following order:
 * </p>
 * <ul>
 *   <li>{@code locale} string (preferred)</li>
 *   <li>{@code language} string (fallback)</li>
 *   <li>{@link Locale#getDefault()} (system fallback)</li>
 * </ul>
 *
 * <p>
 * The {@code timezone} configuration value, if present and valid, is resolved into a
 * {@link ZoneId}. If the value is invalid or missing, the system default time zone is used.
 * </p>
 *
 * @see ConfigProvider
 * @see LocaleSetting
 * @see Locale
 * @see ZoneId
 */
public final class BukkitConfigRepository implements ConfigRepository
{
	private final Supplier<LanguageSetting> languageSettingSupplier;
	private final Supplier<LocaleSetting> localeSettingSupplier;
	private final Supplier<LocaleSetting> numberLocaleSettingSupplier;
	private final Supplier<LocaleSetting> dateLocaleSettingSupplier;
	private final Supplier<LocaleSetting> timeLocaleSettingSupplier;
	private final Supplier<LocaleSetting> logLocaleSettingSupplier;
	private final Supplier<ZoneId> zoneIdSupplier;
	final static String DEFAULT_LANGUAGE_SETTING = "en-US";


	/**
	 * Enum representing the recognized configuration keys for locale setting, in order of preference.
	 */
	enum ConfigKey
	{
		LOCALE("locale"),
		LOCALE_NUMBER("locale.number"),
		LOCALE_DATE("locale.date"),
		LOCALE_TIME("locale.time"),
		LOCALE_LOG("locale.log"),
		LANGUAGE("language"),
		TIME_ZONE("timezone");

		private final String key;
		ConfigKey(String key) { this.key = key; }
		public String key() { return this.key; }
		@Override public String toString() { return this.key; }
	}


	/**
	 * Constructs a new {@code LocaleProvider} with injected suppliers.
	 *
	 * @param localeSettingSupplier supplies the current {@link LocaleSetting}
	 * @param zoneIdSupplier supplies the current {@link ZoneId}
	 */
	private BukkitConfigRepository(final Supplier<LanguageSetting> languageSettingSupplier,
								   final Supplier<LocaleSetting> localeSettingSupplier,
								   final Supplier<LocaleSetting> numberLocaleSettingSupplier,
								   final Supplier<LocaleSetting> dateLocaleSettingSupplier,
								   final Supplier<LocaleSetting> timeLocaleSettingSupplier,
								   final Supplier<LocaleSetting> logLocaleSettingSupplier,
								   final Supplier<ZoneId> zoneIdSupplier)
	{
		this.languageSettingSupplier = languageSettingSupplier;
		this.localeSettingSupplier = localeSettingSupplier;
		this.numberLocaleSettingSupplier = numberLocaleSettingSupplier;
		this.dateLocaleSettingSupplier = dateLocaleSettingSupplier;
		this.timeLocaleSettingSupplier = timeLocaleSettingSupplier;
		this.logLocaleSettingSupplier = logLocaleSettingSupplier;
		this.zoneIdSupplier = zoneIdSupplier;
	}


	/**
	 * Factory method to construct a {@code LocaleProvider} using a plugin's configuration.
	 * <p>
	 * This method attempts to extract valid configuration values in the following order:
	 * </p>
	 * <ol>
	 *   <li>{@code locale} setting (must be a valid BCP-47 language tag)</li>
	 *   <li>{@code language} setting (fallback, same format or else ignored)</li>
	 *   <li>System default locale and timezone as final fallback</li>
	 * </ol>
	 *
	 * @param plugin the plugin whose configuration will be consulted
	 * @return a new {@code LocaleProvider} with dynamic access to locale and time zone settings
	 */
	public static ConfigRepository create(final Plugin plugin)
	{
		return new BukkitConfigRepository(
				() -> new LocaleLanguageSetting(plugin).get(),
				() -> new GlobalLocaleSetting(plugin).get(),
				() -> new NumberLocaleSetting(plugin).get(),
				() -> new DateLocaleSetting(plugin).get(),
				() -> new TimeLocaleSetting(plugin).get(),
				() -> new LoggingLocaleSetting(plugin).get(),
				() -> new ZoneIdSetting(plugin).get());
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
	 * Returns the resolved {@link Locale} object derived from the configuration.
	 *
	 * @return a Java {@code Locale}
	 */
	@Override
	public Locale locale()
	{
		return localeSettingSupplier.get().locale();
	}

	@Override
	public Locale numberLocale()
	{
		return numberLocaleSettingSupplier.get().locale();
	}

	@Override
	public Locale dateLocale()
	{
		return dateLocaleSettingSupplier.get().locale();
	}

	@Override
	public Locale timeLocale()
	{
		return timeLocaleSettingSupplier.get().locale();
	}

	@Override
	public Locale logLocale()
	{
		return logLocaleSettingSupplier.get().locale();
	}


	/**
	 * Returns the configured {@link ZoneId} if valid, or the system default otherwise.
	 *
	 * @return the applicable time zone as represented by a {@code ZoneId} object.
	 */
	@Override
	public ZoneId zoneId()
	{
		return zoneIdSupplier.get();
	}


	@Override
	public String language()
	{
		//TODO: sanitize file name?
		return languageSettingSupplier.get().name();
	}

}
