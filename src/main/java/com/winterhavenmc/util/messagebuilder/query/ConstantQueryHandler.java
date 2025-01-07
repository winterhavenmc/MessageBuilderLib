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

package com.winterhavenmc.util.messagebuilder.query;

import com.winterhavenmc.util.messagebuilder.util.Error;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;
import java.util.Optional;

public class ConstantQueryHandler {

	private final ConfigurationSection constantSection;


	/**
	 * Class constructor
	 *
	 * @param constantSection the 'CONSTANTS' section of the language file
	 */
	public ConstantQueryHandler(final ConfigurationSection constantSection) {
		if (constantSection == null) { throw new IllegalArgumentException(Error.Parameter.NULL_CONSTANT_SECTION.getMessage()); }
		this.constantSection = constantSection;
	}


	/**
	 * Query the constants section of the language file for a String with keyPath
	 *
	 * @param keyPath the keyPath of the String to be retrieved
	 * @return an {@code Optional} String containing the String retrieved with keyPath, or an empty Optional if no
	 * value was found for the keyPath
	 */
	public Optional<String> getString(final String keyPath) {
		if (keyPath == null) { throw new IllegalArgumentException(Error.Parameter.NULL_KEY_PATH.getMessage()); }
		return Optional.ofNullable(constantSection.getString(keyPath));
	}


	/**
	 * Query the constants section of the language file for a List of String with the keyPath
	 *
	 * @param keyPath the keyPath of the List to be retrieved
	 * @return a {@code List} of String containing the values retrieved using keyPath, or an empty List if no
	 * value was found for the keyPath
	 */
	public List<String> getStringList(final String keyPath) {
		if (keyPath == null) { throw new IllegalArgumentException(Error.Parameter.NULL_KEY_PATH.getMessage()); }
		return constantSection.getStringList(keyPath);
	}


	/**
	 * Query the constants section of the language file for an {@code int} with the keyPath
	 *
	 * @param keyPath the keyPath of the {@code int} to be retrieved
	 * @return {@code int} containing the values retrieved using keyPath, or zero (0) if no
	 * value was found for the keyPath
	 */
	public int getInt(final String keyPath) {
		if (keyPath == null) { throw new IllegalArgumentException(Error.Parameter.NULL_KEY_PATH.getMessage()); }
		return constantSection.getInt(keyPath);
	}

}
