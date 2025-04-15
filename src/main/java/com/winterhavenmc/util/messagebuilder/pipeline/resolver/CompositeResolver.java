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

package com.winterhavenmc.util.messagebuilder.pipeline.resolver;

import com.winterhavenmc.util.messagebuilder.adapters.AdapterRegistry;
import com.winterhavenmc.util.messagebuilder.pipeline.context.ContextMap;
import com.winterhavenmc.util.messagebuilder.pipeline.extractor.FieldExtractor;
import com.winterhavenmc.util.messagebuilder.pipeline.result.ResultMap;
import com.winterhavenmc.util.messagebuilder.recordkey.RecordKey;

import java.util.*;


public class CompositeResolver implements Resolver
{
	private final AdapterRegistry adapterRegistry;


	/**
	 * Class constructor
	 */
	public CompositeResolver()
	{
		this.adapterRegistry = new AdapterRegistry();
	}


	/**
	 * Convert the value objects contained in the context map into their string representations in a
	 * new result map.
	 *
	 * @param contextMap a map containing key/value pairs of placeholder strings and their corresponding value object
	 * @return {@code ResultMap} a map containing the placeholder strings and the string representations of the values
	 */
	@Override
	public ResultMap resolve(final RecordKey key, final ContextMap contextMap)
	{
		ResultMap resultMap = new ResultMap();

		// get the value from the context map for the given key
		contextMap.get(key).ifPresent(value ->
				adapterRegistry.getMatchingAdapters(value)
						.forEach(adapter -> adapter.adapt(value)
								.ifPresent(adapted -> {
									// extract subfields from this adapter (e.g. NAME, UUID, LOCATION)
									Map<RecordKey, Object> extracted = new FieldExtractor().extract(adapter, adapted, key);

									// recursively resolve each subfield
									extracted.forEach((subKey, subValue) -> {
										ResultMap subResult = resolve(subKey, contextMap);
										resultMap.putAll(subResult);
									});
								})));

		return resultMap;
	}

}
