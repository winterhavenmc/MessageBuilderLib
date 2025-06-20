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

import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Stream;


/**
 * Provides a dynamic {@link LanguageSetting} derived from the plugin's {@code config.yml}.
 * <p>
 * This class implements {@link ConfigProvider} to supply a locale-based language setting,
 * allowing server operators to specify preferred language or locale values. It checks
 * for the presence of the {@code language} or {@code locale} keys in the configuration
 * and uses the first valid match. If neither is found, a default of {@code en-US} is used.
 * </p>
 *
 * <p>
 * The configuration value is accessed via a {@link Supplier}, allowing dynamic reloading
 * of the setting without requiring object reinitialization. This enables consistent
 * behavior across classes that rely on the selected language configuration.
 * </p>
 */
public class LanguageProvider implements ConfigProvider<LanguageSetting>
{
	private final Supplier<LanguageSetting> languageSettingSupplier;

	private static final String FALLBACK_NAME = "en-US";


	/**
	 * Enum representing the recognized configuration keys for language settings.
	 */
	private enum LanguageField
	{
		LANGUAGE("language"),
		LOCALE("locale");

		private final String key;
		LanguageField(final String key) { this.key = key; }
		@Override public String toString() { return key; }
	}


	/**
	 * Private constructor. Use {@link #create(Plugin)} to instantiate.
	 *
	 * @param supplier a supplier that yields the most current {@link LanguageSetting}
	 */
	private LanguageProvider(final Supplier<LanguageSetting> supplier)
	{
		this.languageSettingSupplier = supplier;
	}


	/**
	 * Creates a {@code LanguageProvider} by reading the configuration from the given plugin.
	 * <p>
	 * Searches for {@code language} or {@code locale} keys in the plugin’s configuration,
	 * and constructs a {@link LanguageSetting} accordingly. Defaults to {@code en-US}
	 * if no valid key is found.
	 * </p>
	 *
	 * @param plugin the plugin providing the configuration
	 * @return a new {@code LanguageProvider} instance
	 */
	public static LanguageProvider create(final Plugin plugin)
	{
		return new LanguageProvider(() -> {
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
	 * Returns the configured language name string (e.g., {@code en-US}).
	 *
	 * @return the string representation of the current language setting
	 */
	public String getName()
	{
		return languageSettingSupplier.get().name();
	}

}
