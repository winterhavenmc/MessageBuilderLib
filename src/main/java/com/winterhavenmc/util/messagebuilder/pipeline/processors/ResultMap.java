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

package com.winterhavenmc.util.messagebuilder.pipeline.processors;

import com.winterhavenmc.util.messagebuilder.util.RecordKey;
import com.winterhavenmc.util.messagebuilder.validation.LogLevel;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

import static com.winterhavenmc.util.messagebuilder.validation.ErrorMessageKey.*;
import static com.winterhavenmc.util.messagebuilder.validation.Parameter.VALUE;
import static com.winterhavenmc.util.messagebuilder.validation.ValidationHandler.logging;
import static com.winterhavenmc.util.messagebuilder.validation.Validator.validate;


public class ResultMap
{
	private final Map<RecordKey, String> internalResultMap;
	private static final Predicate<String> STRING_IS_NULL = Objects::isNull;
	private static final Predicate<String> STRING_IS_EMPTY = String::isBlank;
	private static final Predicate<String> INVALID = STRING_IS_NULL.or(STRING_IS_EMPTY);


	/**
	 * Class constructor
	 */
	public ResultMap()
	{
		this.internalResultMap = new HashMap<>();
	}

	public void put(final RecordKey key, final String value)
	{
		validate(value, INVALID, logging(LogLevel.INFO, PARAMETER_INVALID, VALUE))
				.ifPresent((string -> internalResultMap.put(key, string)));
	}


	public String get(final RecordKey key)
	{
		return internalResultMap.get(key);
	}


	public String getValueOrKey(final RecordKey key) {
		return internalResultMap.getOrDefault(key, key.toString()); // Return key itself if not found
	}


	public void putAll(final @NotNull ResultMap insertionMap)
	{
		for (Map.Entry<RecordKey, String> entry : insertionMap.entrySet()) {
			String value = entry.getValue();
			internalResultMap.put(entry.getKey(), value);
		}
	}


	public boolean containsKey(final RecordKey key)
	{
		return internalResultMap.containsKey(key);
	}


	public Iterable<? extends Map.Entry<RecordKey, String>> entrySet()
	{
		return internalResultMap.entrySet();
	}


	public boolean isEmpty()
	{
		return internalResultMap.isEmpty();
	}

}
