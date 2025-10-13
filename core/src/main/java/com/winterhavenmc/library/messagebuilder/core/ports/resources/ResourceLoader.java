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

package com.winterhavenmc.library.messagebuilder.core.ports.resources;

import org.bukkit.configuration.Configuration;


/**
 * An interface that represents classes responsible for loading an installed resource file from
 * the plugin data directory.
 */
public interface ResourceLoader
{
	/**
	 * Load the language configuration object for the configured language from file and return it. The returned
	 * configuration object contains no default values loaded, by design
	 *
	 * @return {@link Configuration} containing the configuration loaded from the language file
	 */
	Configuration load();
}
