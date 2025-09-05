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

package com.winterhavenmc.library.messagebuilder;

import com.winterhavenmc.library.messagebuilder.keys.ConstantKey;
import com.winterhavenmc.library.messagebuilder.keys.ValidConstantKey;
import com.winterhavenmc.library.messagebuilder.ports.language_resource.ConstantRepository;
import com.winterhavenmc.library.messagebuilder.resources.language.LanguageResourceManager;

import java.util.Optional;


/**
 * A class that allows clients of the library to access values in the constants section of the language file
 */
public class ConstantResolver
{
	private final ConstantRepository constants;


	/**
	 * Class constructor
	 */
	ConstantResolver(final LanguageResourceManager languageResourceManager)
	{
		this.constants = languageResourceManager.constants();
	}


	/**
	 * Retrieves an Optional String from the constants section of the language file
	 *
	 * @param key a string to be used as the string for the String to be retrieved
	 * @return an Optional String, or an empty Optional if no String record could be retrieved
	 */
	public Optional<String> getString(final String key)
	{
		return (ConstantKey.of(key) instanceof ValidConstantKey validConstantKey)
				? constants.getString(validConstantKey)
				: Optional.empty();
	}


	/**
	 * Retrieves an Optional Integer from the constants section of the language file
	 *
	 * @param key a string to be used as the string for the Integer to be retrieved
	 * @return an Optional Integer, or an empty Optional if no Integer record could be retrieved
	 */
	public Optional<Integer> getInteger(final String key)
	{
		return (ConstantKey.of(key) instanceof ValidConstantKey validConstantKey)
				? constants.getInteger(validConstantKey)
				: Optional.empty();
	}


	/**
	 * Retrieves an Optional Boolean from the constants section of the language file
	 *
	 * @param key a string to be used as the string for the Boolean to be retrieved
	 * @return an Optional Boolean, or an empty Optional if no Boolean record could be retrieved
	 */
	public Optional<Boolean> getBoolean(final String key)
	{
		return (ConstantKey.of(key) instanceof ValidConstantKey validConstantKey)
				? constants.getBoolean(validConstantKey)
				: Optional.empty();
	}

}
