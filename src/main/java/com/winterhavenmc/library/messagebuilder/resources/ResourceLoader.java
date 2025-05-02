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

package com.winterhavenmc.library.messagebuilder.resources;

import com.winterhavenmc.library.messagebuilder.resources.configuration.LanguageTag;
import org.bukkit.configuration.Configuration;

import java.util.Locale;
import java.util.Optional;

public interface ResourceLoader
{
	/**
	 * Gets language tag specified in config.yml.
	 * <p>
	 * it is recommended, but not required, that languages should be specified by their ISO-639 codes,
	 * with two letter lowercase language code and two letter uppercase country code separated by a hyphen.
	 * <p>
	 * <i>example:</i> en-US
	 * <p>
	 * The language yaml file must match the specified tag, with a .yml extension appended.
	 *
	 * @return Optional {@code LanguageTag} or an empty Optional if config setting is null or empty
	 */
	Optional<LanguageTag> getConfiguredLanguageTag();

	Locale getConfiguredLocale();

	/**
	 * Load the language configuration object for the configured language from file and return it. The returned
	 * configuration object contains no default values loaded, by design
	 *
	 * @return Configuration - message configuration object
	 */
	Configuration load();

	/**
	 * Load the language configuration object for the configured language from file and return it. The returned
	 * configuration object contains no default values loaded, by design
	 *
	 * @return {@link Configuration} containing the configuration loaded from the language file
	 */
	Configuration load(LanguageTag languageTag);
}
