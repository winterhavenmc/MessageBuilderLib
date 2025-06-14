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
 * Provides a resolved {@link LocaleSetting} from plugin configuration.
 * <p>
 * This class reads from the configuration fields {@code locale} and {@code language}
 * (in that order of priority) to attempt to resolve a {@link LanguageTag}.
 * If neither setting is valid or present, it falls back to {@link Locale#getDefault()}.
 *
 * <p>
 * This is primarily used for formatting numbers, dates, and times using the JVM's {@link Locale}.
 */
public class LocaleProvider implements ConfigProvider<LocaleSetting>
{
	private final static String timeZoneSetting = "timezone";
	private final Supplier<LocaleSetting> localeSettingSupplier;
	private final Supplier<ZoneId> zoneIdSupplier;


	/**
	 * The configuration keys supported for resolving the locale.
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
	 * Private constructor
	 *
	 * @param localeSettingSupplier a provider for the plugin config language setting
	 */
	private LocaleProvider(final Supplier<LocaleSetting> localeSettingSupplier, final Supplier<ZoneId> zoneIdSupplier)
	{
		this.localeSettingSupplier = localeSettingSupplier;
		this.zoneIdSupplier = zoneIdSupplier;
	}


	/**
	 * Creates a new {@code LocaleProvider} for the specified plugin.
	 * <p>
	 * Resolution order:
	 * <ol>
	 *   <li>{@code locale} setting (must be a valid and available JVM locale)</li>
	 *   <li>{@code language} setting (must be valid and available)</li>
	 *   <li>{@link Locale#getDefault()} if both are invalid or missing</li>
	 * </ol>
	 *
	 * @param plugin the plugin whose configuration should be used
	 * @return a new LocaleProvider
	 */
	public static LocaleProvider create(final Plugin plugin)
	{
		return new LocaleProvider(
				() -> new LocaleSetting(LanguageTag.of(plugin.getConfig().getString(LocaleField.LOCALE.toString()))
						.orElse(LanguageTag.of(plugin.getConfig().getString(LocaleField.LANGUAGE.toString()))
						.orElse(LanguageTag.getSystemDefault()))),
				() -> getValidZoneId(plugin));
	}


	private static ZoneId getValidZoneId(final Plugin plugin)
	{
		String timezone = plugin.getConfig().getString(timeZoneSetting);

		return (timezone != null && ZoneId.getAvailableZoneIds().contains(timezone))
				? ZoneId.of(timezone)
				: ZoneId.systemDefault();
	}


	/**
	 * Returns the resolved {@link LocaleSetting}.
	 *
	 * @return the current LocaleSetting
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
	 * Returns the actual {@link Locale} derived from the configuration.
	 *
	 * @return the Java Locale object
	 */
	public Locale getLocale()
	{
		return localeSettingSupplier.get().languageTag().getLocale();
	}


	public ZoneId getZoneId()
	{
		return zoneIdSupplier.get();
	}

}
