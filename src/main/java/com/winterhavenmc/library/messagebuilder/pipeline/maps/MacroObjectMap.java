/*
 * Copyright (c) 2024-2025 Tim Savage.
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

import com.winterhavenmc.library.messagebuilder.keys.MacroKey;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;


/**
 * A thread-safe mapping of {@link MacroKey} to arbitrary {@link Object} values,
 * used during macro substitution to store raw objects before formatting.
 * <p>
 * This class allows both {@code put} and {@code putIfAbsent} insertion behavior.
 * Null values are not permitted; any {@code null} value is automatically replaced
 * with the string literal {@code "NULL"}.
 *
 * <p>Primarily intended for internal use within the message pipeline where macro
 * resolution requires intermediate object capture before string conversion.</p>
 */
public class MacroObjectMap
{
	private final Map<MacroKey, Object> INTERNAL_MAP = new ConcurrentHashMap<>();


	/**
	 * Inserts a key-value pair into the map. If the value is {@code null}, the string {@code "NULL"}
	 * is used instead.
	 *
	 * @param macroKey the key under which the value should be stored
	 * @param value    the value to store, or {@code "NULL"} if null
	 * @param <T>      the type of the value
	 */
	public <T> void put(final MacroKey macroKey, final T value)
	{
		// insert value into map with key, replacing null values with string "NULL"
		INTERNAL_MAP.put(macroKey, Objects.requireNonNullElse(value, "NULL"));
	}


	/**
	 * Inserts a key-value pair only if the key is not already present in the map.
	 * If the value is {@code null}, the string {@code "NULL"} is used instead.
	 *
	 * @param macroKey the key to insert
	 * @param value    the value to associate, or {@code "NULL"} if null
	 * @param <T>      the type of the value
	 */
	public <T> void putIfAbsent(final MacroKey macroKey, final T value)
	{
        // insert value into map with key, replacing null values with string "NULL"
		INTERNAL_MAP.putIfAbsent(macroKey, Objects.requireNonNullElse(value, "NULL"));
	}


	/**
	 * Retrieves the value associated with the specified {@link MacroKey}, if present.
	 *
	 * @param macroKey the key whose value to retrieve
	 * @return an {@link Optional} containing the associated value, or empty if not found
	 */
	public Optional<Object> get(final MacroKey macroKey)
	{
		return Optional.ofNullable(INTERNAL_MAP.get(macroKey));
	}

}
