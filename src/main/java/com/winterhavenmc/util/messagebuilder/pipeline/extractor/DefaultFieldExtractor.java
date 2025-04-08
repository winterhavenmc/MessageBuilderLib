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

import com.winterhavenmc.util.messagebuilder.resources.RecordKey;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


public class DefaultFieldExtractor<T> implements FieldExtractor<T>
{
	private final Function<T, Map<String, String>> extractionLogic;


	public DefaultFieldExtractor(final Function<T, Map<String, String>> extractionLogic)
	{
		this.extractionLogic = extractionLogic;
	}


	@Override
	public Map<String, String> extract(final T source, final RecordKey macroKey)
	{
		return extractionLogic.apply(source)
				.entrySet().stream()
				.collect(Collectors.toMap(
						entry -> macroKey + "." + entry.getKey(),
						Map.Entry::getValue
				));
	}

}
