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

package com.winterhavenmc.util.messagebuilder.pipeline.extractor;

import java.util.*;
import java.util.function.Function;


public class FieldExtractorRegistry
{
	private final Map<Class<?>, Function<Object, Map<String, Object>>> EXTRACTOR_MAP = new HashMap<>();


	/**
	 * Registers a field extractor for a specific type.
	 * @param type The class type for which the extractor applies.
	 * @param extractor A function that extracts fields from an object of the given type.
	 */
	public <T> void registerExtractor(final Class<T> type, final Function<T, Map<String, Object>> extractor)
	{
		EXTRACTOR_MAP.put(type, obj -> extractor.apply(type.cast(obj)));
	}


	/**
	 * Extracts fields from an object using a registered extractor, if available.
	 * @param value The object to extract fields from.
	 * @return A map of extracted field names and values.
	 */
	public Optional<Map<String, Object>> extractFields(final Object value)
	{
		return value == null
				? Optional.empty()
				: Optional.ofNullable(EXTRACTOR_MAP.get(value.getClass()))
                        .map(extractor -> extractor.apply(value));
	}

}
