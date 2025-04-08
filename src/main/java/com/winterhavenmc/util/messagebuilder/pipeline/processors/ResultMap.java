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

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.winterhavenmc.util.messagebuilder.validation.ErrorMessageKey.PARAMETER_EMPTY;
import static com.winterhavenmc.util.messagebuilder.validation.ErrorMessageKey.PARAMETER_NULL;
import static com.winterhavenmc.util.messagebuilder.validation.Parameter.KEY;
import static com.winterhavenmc.util.messagebuilder.validation.Parameter.VALUE;
import static com.winterhavenmc.util.messagebuilder.validation.ValidationHandler.throwing;
import static com.winterhavenmc.util.messagebuilder.validation.Validator.validate;


public class ResultMap
{
	private final Map<String, String> internalResultMap;


	/**
	 * Class constructor
	 */
	public ResultMap()
	{
		this.internalResultMap = new HashMap<>();
	}


	public void put(final String key, final String value)
	{
		validate(key, Objects::isNull, throwing(PARAMETER_NULL, KEY));
		validate(key, String::isBlank, throwing(PARAMETER_EMPTY, KEY));
		validate(value, Objects::isNull, throwing(PARAMETER_NULL, VALUE));
		// allow blank string value to be passed in. Uncomment line below to throw exception on blank string value
		//staticValidate(value, String::isBlank, () -> new ValidationException(PARAMETER_EMPTY, VALUE));

		internalResultMap.put(key, value);
	}


	public String get(final String key)
	{
		validate(key, Objects::isNull, throwing(PARAMETER_NULL, KEY));
		validate(key, String::isBlank, throwing(PARAMETER_EMPTY, KEY));

		return internalResultMap.get(key);
	}


	public String getValueOrKey(final String key) {
		return internalResultMap.getOrDefault(key, key); // Return key itself if not found
	}


	public void putAll(final @NotNull ResultMap insertionMap)
	{
		for (Map.Entry<String, String> entry : insertionMap.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			internalResultMap.put(key, value);
		}
	}


	public boolean containsKey(final String key)
	{
		validate(key, Objects::isNull, throwing(PARAMETER_NULL, KEY));
		validate(key, String::isBlank, throwing(PARAMETER_EMPTY, KEY));

		return internalResultMap.containsKey(key);
	}


	public Iterable<? extends Map.Entry<String, String>> entrySet()
	{
		return internalResultMap.entrySet();
	}


	public boolean isEmpty()
	{
		return internalResultMap.isEmpty();
	}

}
