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

import com.winterhavenmc.library.messagebuilder.configuration.ConfigProvider;
import com.winterhavenmc.library.messagebuilder.configuration.LanguageProvider;
import com.winterhavenmc.library.messagebuilder.models.configuration.LanguageSetting;

import org.bukkit.plugin.Plugin;

import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Stream;


/**
 * Provides a dynamic {@link LanguageSetting} derived from the plugin's {@code config.yml}.
 * <p>
 * This class implements {@link ConfigProvider} to supply a language setting,
 * allowing server operators to specify a localized language file containing plugin messages
 * and other localized strings. It checks for the presence of the {@code language} or
 * {@code locale} keys in the configuration and uses the first valid match. If neither setting
 * is found, a default of {@code en-US} is used. While an IETF language tag is recommended for
 * language file naming, it is not required. This allows server operators to use a custom
 * language file of their own creation, without interfering with existing or provided files.
 * </p>
 *
 * <p>
 * The configuration value is accessed via a {@link Supplier}, allowing dynamic reloading
 * of the setting without requiring object reinitialization. This enables consistent
 * behavior across classes that rely on the selected language configuration.
 * </p>
 *
 * <p>
 * Note that the {@code .yml} suffix is appended to the setting, and therefore should not
 * be included in the language file name setting of the {@code config.yml} file.
 * </p>
 */
public class BukkitLanguageProvider implements LanguageProvider
{
	private final static String FALLBACK_NAME = "en-US";
	private final Supplier<LanguageSetting> languageSettingSupplier;


	/**
	 * Private constructor. Use {@link #create(Plugin)} to instantiate.
	 *
	 * @param supplier a supplier that yields the most current {@link LanguageSetting}
	 */
	private BukkitLanguageProvider(final Supplier<LanguageSetting> supplier)
	{
		this.languageSettingSupplier = supplier;
	}


	/**
	 * Creates a {@code LanguageProvider} by reading the configuration from the given plugin.
	 * <p>
	 * Searches for {@code language} or {@code locale} keys in the plugin’s configuration,
	 * and constructs a {@link LanguageSetting} accordingly. Defaults to {@code en-US}
	 * if no valid string is found.
	 * </p>
	 *
	 * @param plugin the plugin providing the configuration
	 * @return a new {@code LanguageProvider} instance
	 */
	public static LanguageProvider create(Plugin plugin)
	{
		return new BukkitLanguageProvider(() -> {
			var config = plugin.getConfig();

			return Stream.of(LanguageField.values())
					.map(LanguageField::toString)
					.map(config::getString)
					.filter(Objects::nonNull)
					.map(LanguageSetting::new)
					.findFirst()
					.orElse(new LanguageSetting(FALLBACK_NAME));
		});
	}

	/**
	 * Returns the current {@link LanguageSetting}.
	 *
	 * @return the current language setting
	 */
	@Override
	public LanguageSetting get()
	{
		return languageSettingSupplier.get();
	}


	/**
	 * Returns the configured language file name string (e.g., {@code en-US}).
	 *
	 * @return the string representation of the current language file setting
	 */
	@Override
	public String getName()
	{
		return languageSettingSupplier.get().name();
	}

	/**
	 * Enum representing the recognized configuration keys for language settings.
	 */
	public enum LanguageField
	{
		LANGUAGE("language"),
		LOCALE("locale");

		private final String key;

		LanguageField(final String key)
		{
			this.key = key;
		}

		@Override
		public String toString()
		{
			return key;
		}
	}
}
