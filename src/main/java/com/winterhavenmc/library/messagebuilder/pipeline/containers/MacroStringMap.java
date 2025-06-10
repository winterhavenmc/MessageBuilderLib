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
	 * Static factory method to create new MacroStringMap from existing MacroStringMap
	 *
	 * @param map an existing MacroStringMap
	 * @return a new MacroStringMap with the contents of the given map
	 */
	public static MacroStringMap of(MacroStringMap map)
	{
		MacroStringMap resultMap = new MacroStringMap();
		resultMap.putAll(map);
		return resultMap;
	}


	public void put(final MacroKey macroKey, final String value)
	{
		validate(value, INVALID, logging(LogLevel.INFO, PARAMETER_INVALID, VALUE));
		INTERNAL_MAP.put(macroKey, value);
	}


	public void putIfAbsent(final MacroKey macroKey, final String value)
	{
		validate(value, INVALID, logging(LogLevel.INFO, PARAMETER_INVALID, VALUE));
		INTERNAL_MAP.putIfAbsent(macroKey, value);
	}


	public String get(final MacroKey key)
	{
		return INTERNAL_MAP.get(key);
	}


	public String getValueOrDefault(final MacroKey key, final String defaultString)
	{
		return INTERNAL_MAP.getOrDefault(key, defaultString);
	}


	public void putAll(final MacroStringMap insertionMap)
	{
		for (Map.Entry<MacroKey, String> entry : insertionMap.entrySet())
		{
			INTERNAL_MAP.put(entry.getKey(), entry.getValue());
		}
	}


	public boolean containsKey(final MacroKey key)
	{
		return INTERNAL_MAP.containsKey(key);
	}


	public Iterable<? extends Map.Entry<MacroKey, String>> entrySet()
	{
		return INTERNAL_MAP.entrySet();
	}


	public Set<MacroKey> keySet()
	{
		return INTERNAL_MAP.keySet();
	}


	public boolean isEmpty()
	{
		return INTERNAL_MAP.isEmpty();
	}


	public int size()
	{
		return INTERNAL_MAP.size();
	}


	static public MacroStringMap empty()
	{
		return new MacroStringMap();
	}


	public MacroStringMap with(MacroKey key, String value)
	{
		this.put(key, value);
		return this;
	}

}
