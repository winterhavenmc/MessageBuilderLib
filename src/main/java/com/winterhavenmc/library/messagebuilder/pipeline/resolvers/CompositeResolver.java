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

package com.winterhavenmc.library.messagebuilder.pipeline.resolvers;

import com.winterhavenmc.library.messagebuilder.pipeline.adapters.AdapterRegistry;
import com.winterhavenmc.library.messagebuilder.keys.MacroKey;
import com.winterhavenmc.library.messagebuilder.pipeline.context.ContextMap;
import com.winterhavenmc.library.messagebuilder.pipeline.extractor.FieldExtractor;
import com.winterhavenmc.library.messagebuilder.pipeline.result.ResultMap;


public class CompositeResolver implements Resolver
{
	private final AdapterRegistry adapterRegistry;
	private final FieldExtractor fieldExtractor;


	/**
	 * Class constructor
	 */
	public CompositeResolver(final AdapterRegistry adapterRegistry,
							 final FieldExtractor fieldExtractor)
	{
		this.adapterRegistry = adapterRegistry;
		this.fieldExtractor = fieldExtractor;
	}


	/**
	 * Convert the value objects contained in the context map into their string representations in a
	 * new result map.
	 *
	 * @param contextMap a map containing key/value pairs of placeholder strings and their corresponding value object
	 * @return {@code ResultMap} a map containing the placeholder strings and the string representations of the values
	 */
	@Override
	public ResultMap resolve(final MacroKey macroKey, final ContextMap contextMap)
	{
		ResultMap resultMap = new ResultMap();

		contextMap.get(macroKey).ifPresent(value ->
				resolveSubkeysInto(resultMap, value, macroKey, contextMap));

		return resultMap;
	}


	/**
	 * Resolver static helper method
	 */
	private void resolveSubkeysInto(ResultMap resultMap, Object value, MacroKey macroKey, ContextMap contextMap)
	{
		adapterRegistry.getMatchingAdapters(value).forEach(adapter ->
				adapter.adapt(value).ifPresent(adapted ->
						fieldExtractor.extract(adapter, adapted, macroKey).keySet()
								.forEach(subKey -> resultMap.putAll(resolve(subKey, contextMap)))
				)
		);
	}
}
