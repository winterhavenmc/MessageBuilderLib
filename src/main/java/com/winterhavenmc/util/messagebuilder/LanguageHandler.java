/*
 * Copyright (c) 2022 Tim Savage.
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

package com.winterhavenmc.util.messagebuilder;

import org.bukkit.configuration.Configuration;

import java.util.List;
import java.util.Optional;


public interface LanguageHandler {

	/**
	 * Get Configuration object for current language file
	 *
	 * @return {@code Configuration} the configuration object for the current language file
	 */
	Configuration getConfiguration();

	/**
	 * Get setting for language from plugin config file
	 *
	 * @return a string containing the IETF language tag set in the plugin config file
	 */
	//TODO: change this to (or add) getLocale
	String getConfigLanguage();


	/**
	 * Get spawn display name from language file
	 *
	 * @return the formatted display name for the world spawn, or empty string if key not found
	 */
	Optional<String> getSpawnDisplayName();


	/**
	 * Get home display name from language file
	 *
	 * @return the formatted display name for home, or empty string if key not found
	 */
	Optional<String> getHomeDisplayName();


	/**
	 * Get string by path in message file
	 * @param path the message path for the string being retrieved
	 * @return String - the string retrieved by path from message file
	 */
	Optional<String> getString(final String path);


	/**
	 * Get List of String by path in message file
	 * @param path the message path for the string list being retrieved
	 * @return List of String - the string list retrieved by path from message file
	 */
	List<String> getStringList(final String path);


	/**
	 * Reload messages into Configuration object
	 */
	void reload();

}
