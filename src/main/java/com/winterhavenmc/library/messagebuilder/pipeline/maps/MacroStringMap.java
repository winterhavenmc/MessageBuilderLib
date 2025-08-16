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

package com.winterhavenmc.library.messagebuilder.pipeline.maps;

import com.winterhavenmc.library.messagebuilder.keys.ValidMacroKey;
import com.winterhavenmc.library.messagebuilder.validation.LogLevel;

import java.util.*;
import java.util.function.Predicate;

import static com.winterhavenmc.library.messagebuilder.validation.ErrorMessageKey.*;
import static com.winterhavenmc.library.messagebuilder.validation.Parameter.VALUE;
import static com.winterhavenmc.library.messagebuilder.validation.Validator.logging;
import static com.winterhavenmc.library.messagebuilder.validation.Validator.validate;


/**
 * A specialized map for storing string macro values keyed by {@link ValidMacroKey}.
 * <p>
 * This map is used during the macro resolution phase of the message pipeline
 * to provide string substitution values for placeholders.
 *
 * <p>All insertions are validated: null and blank strings are considered invalid,
 * and will trigger a validation warning (but are still inserted).</p>
 *
 * <p>This class is mutable and not thread-safe. It is expected to be used in a
 * single-threaded message construction context.</p>
 */
public class MacroStringMap
{
	private final Map<ValidMacroKey, String> INTERNAL_MAP;
	private static final Predicate<String> STRING_IS_NULL = Objects::isNull;
	private static final Predicate<String> STRING_IS_EMPTY = String::isBlank;
	private static final Predicate<String> INVALID = STRING_IS_NULL.or(STRING_IS_EMPTY);


	/**
	 * Constructs an empty {@code MacroStringMap}.
	 */
	public MacroStringMap()
	{
		this.INTERNAL_MAP = new HashMap<>();
	}


	/**
	 * Inserts a string-value pair into the map.
	 * If the value is {@code null} or blank, a validation warning is logged.
	 *
	 * @param macroKey the macro string
	 * @param value    the string value to associate
	 */
	public void put(final ValidMacroKey macroKey, final String value)
	{
		validate(value, INVALID, logging(LogLevel.INFO, PARAMETER_INVALID, VALUE));
		INTERNAL_MAP.put(macroKey, value);
	}


	/**
	 * Inserts a string-value pair into the map only if the string is not already present.
	 * If the value is {@code null} or blank, a validation warning is logged.
	 *
	 * @param macroKey the macro string
	 * @param value    the string value to associate
	 */
	public void putIfAbsent(final ValidMacroKey macroKey, final String value)
	{
		validate(value, INVALID, logging(LogLevel.INFO, PARAMETER_INVALID, VALUE));
		INTERNAL_MAP.putIfAbsent(macroKey, value);
	}


	/**
	 * Returns the value associated with the given string, or {@code null} if not found.
	 *
	 * @param key the macro string
	 * @return the associated value, or {@code null} if not present
	 */
	public String get(final ValidMacroKey key)
	{
		return INTERNAL_MAP.get(key);
	}


	/**
	 * Inserts all entries from another {@code MacroStringMap} into this map.
	 * Existing keys will be overwritten.
	 *
	 * @param insertionMap the map whose entries should be copied
	 */
	public void putAll(final MacroStringMap insertionMap)
	{
		for (Map.Entry<ValidMacroKey, String> entry : insertionMap.entrySet())
		{
			INTERNAL_MAP.put(entry.getKey(), entry.getValue());
		}
	}


	/**
	 * Returns {@code true} if this map contains the specified string.
	 *
	 * @param key the string to check
	 * @return {@code true} if the string is present
	 */
	public boolean containsKey(final ValidMacroKey key)
	{
		return INTERNAL_MAP.containsKey(key);
	}


	/**
	 * Returns the set of string-value entries in this map.
	 *
	 * @return an iterable view of the map's entries
	 */
	public Iterable<? extends Map.Entry<ValidMacroKey, String>> entrySet()
	{
		return INTERNAL_MAP.entrySet();
	}


	/**
	 * Returns the set of keys contained in this map.
	 *
	 * @return a set of macro keys
	 */
	public Set<ValidMacroKey> keySet()
	{
		return INTERNAL_MAP.keySet();
	}


	/**
	 * Returns {@code true} if this map is empty.
	 *
	 * @return {@code true} if there are no entries
	 */
	public boolean isEmpty()
	{
		return INTERNAL_MAP.isEmpty();
	}


	/**
	 * Returns the number of entries in this map.
	 *
	 * @return the map size
	 */
	public int size()
	{
		return INTERNAL_MAP.size();
	}


	/**
	 * Returns a new, empty {@code MacroStringMap}.
	 *
	 * @return an empty instance
	 */
	static public MacroStringMap empty()
	{
		return new MacroStringMap();
	}


	/**
	 * Adds a string-value pair to this map and returns the map itself.
	 * This method is useful for fluent building patterns.
	 *
	 * @param key   the macro string
	 * @param value the string value to associate
	 * @return this map, with the new entry included
	 */
	public MacroStringMap with(ValidMacroKey key, String value)
	{
		this.put(key, value);
		return this;
	}

}
