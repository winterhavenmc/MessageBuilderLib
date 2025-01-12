/*
 * Copyright (c) 2022-2024 Tim Savage.
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

package com.winterhavenmc.util.messagebuilder.language;

import com.winterhavenmc.util.messagebuilder.language.yaml.YamlConfigurationSupplier;
import org.bukkit.configuration.Configuration;


public interface LanguageResourceHandler {

	YamlConfigurationSupplier getSupplier();

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
	 * Reload messages into Configuration object
	 *
	 * @return true if successful, false if not
	 */
	boolean reload();

}
