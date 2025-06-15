/*
 * Copyright (c) 2022-2025 Tim Savage.
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

package com.winterhavenmc.library.messagebuilder.pipeline.containers;

import com.winterhavenmc.library.messagebuilder.keys.MacroKey;
import com.winterhavenmc.library.messagebuilder.validation.LogLevel;

import java.util.*;
import java.util.function.Predicate;

import static com.winterhavenmc.library.messagebuilder.validation.ErrorMessageKey.*;
import static com.winterhavenmc.library.messagebuilder.validation.Parameter.VALUE;
import static com.winterhavenmc.library.messagebuilder.validation.ValidationHandler.logging;
import static com.winterhavenmc.library.messagebuilder.validation.Validator.validate;


public class MacroStringMap
{
	private final Map<MacroKey, String> INTERNAL_MAP;
	private static final Predicate<String> STRING_IS_NULL = Objects::isNull;
	private static final Predicate<String> STRING_IS_EMPTY = String::isBlank;
	private static final Predicate<String> INVALID = STRING_IS_NULL.or(STRING_IS_EMPTY);


	/**
	 * Class constructor
	 */
	public MacroStringMap()
	{
		this.INTERNAL_MAP = new HashMap<>();
	}


	/**
	 * Insert a key/value pair into the map
	 *
	 * @param macroKey the key for the entry
	 * @param value the value for the entry
	 */
	public void put(final MacroKey macroKey, final String value)
	{
		validate(value, INVALID, logging(LogLevel.INFO, PARAMETER_INVALID, VALUE));
		INTERNAL_MAP.put(macroKey, value);
	}


	/**
	 * Insert a key/value pair into the map only if an entry for the key is not already present
	 *
	 * @param macroKey the key for the entry
	 * @param value the value for the entry
	 */
	public void putIfAbsent(final MacroKey macroKey, final String value)
	{
		validate(value, INVALID, logging(LogLevel.INFO, PARAMETER_INVALID, VALUE));
		INTERNAL_MAP.putIfAbsent(macroKey, value);
	}


	/**
	 * Retrieve a {@code String} value from the map for the key, or {@code null} if no entry is present for the key
	 *
	 * @param key the key for which to retrieve a value
	 * @return the {@code String} value for the key in the map, or {@code null} if no entry is present for the key
	 */
	public String get(final MacroKey key)
	{
		return INTERNAL_MAP.get(key);
	}


	/**
	 * Insert all entries from the passed in map to an existing map
	 *
	 * @param insertionMap the map whose entries are to be inserted into the existing map
	 */
	public void putAll(final MacroStringMap insertionMap)
	{
		for (Map.Entry<MacroKey, String> entry : insertionMap.entrySet())
		{
			INTERNAL_MAP.put(entry.getKey(), entry.getValue());
		}
	}


	/**
	 * Return a {@code boolean} representing whether a key is contained in the map
	 *
	 * @param key the MacroKey to test for existence in the map
	 * @return {@code true} if an entry with the key exists in the map, or {@code false} if not
	 */
	public boolean containsKey(final MacroKey key)
	{
		return INTERNAL_MAP.containsKey(key);
	}


	/**
	 * Return an Iterable Set view of the entries of the map
	 *
	 * @return Iterable Set of map entries
	 */
	public Iterable<? extends Map.Entry<MacroKey, String>> entrySet()
	{
		return INTERNAL_MAP.entrySet();
	}


	/**
	 * Return a {@code Set} view of the keys of the map
	 *
	 * @return {@code Set} of map keys
	 */
	public Set<MacroKey> keySet()
	{
		return INTERNAL_MAP.keySet();
	}


	/**
	 * Return {@code boolean} representing whether the map contains any entries
	 *
	 * @return {@code true} if the map contains no entries, or {@code false} if it contains one or more entries
	 */
	public boolean isEmpty()
	{
		return INTERNAL_MAP.isEmpty();
	}


	/**
	 * Return the number of entries in the MacroStringMap as int
	 *
	 * @return {@code int} number of entries in the map
	 */
	public int size()
	{
		return INTERNAL_MAP.size();
	}


	/**
	 * Return a new, unpopulated instance of a MacroStringMap
	 *
	 * @return an empty MacroStringMap
	 */
	static public MacroStringMap empty()
	{
		return new MacroStringMap();
	}


	/**
	 * Return the instance of MacroStringMap with the key/value pair added
	 *
	 * @param key the key to be added to the map
	 * @param value the value to be added to the map
	 * @return the map instance with the key/value pair added
	 */
	public MacroStringMap with(MacroKey key, String value)
	{
		this.put(key, value);
		return this;
	}

}
