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
import com.winterhavenmc.library.messagebuilder.pipeline.containers.MacroObjectMap;
import com.winterhavenmc.library.messagebuilder.pipeline.extractor.FieldExtractor;
import com.winterhavenmc.library.messagebuilder.pipeline.containers.MacroStringMap;


public class CompositeResolver implements Resolver
{
	private final AdapterRegistry adapterRegistry;
	private final FieldExtractor fieldExtractor;


	public CompositeResolver(final AdapterRegistry adapterRegistry,
							 final FieldExtractor fieldExtractor)
	{
		this.adapterRegistry = adapterRegistry;
		this.fieldExtractor = fieldExtractor;
	}


	@Override
	public MacroStringMap resolve(final MacroKey macroKey, final MacroObjectMap macroObjectMap)
	{
		MacroStringMap macroStringMap = new MacroStringMap();

		macroObjectMap.get(macroKey).ifPresent(value -> adapterRegistry
				.getMatchingAdapters(value)
				.forEach(adapter -> adapter
				.adapt(value)
				.ifPresent(adapted -> fieldExtractor
				.extract(adapter, adapted, macroKey)
				.putAll(macroStringMap))));

		return macroStringMap;
	}


//	@Override
//	public MacroStringMap resolve(final MacroKey macroKey, final MacroObjectMap macroObjectMap)
//	{
//		return macroObjectMap.get(macroKey)
//				.map(value -> adapterRegistry.getMatchingAdapters(value)
//						.flatMap(adapter -> adapter
//								.adapt(value)
//								.map(adapted -> fieldExtractor.extract(adapter, adapted, macroKey))
//								.stream()) // flatten Optionals
//						.reduce(new MacroStringMap(), (acc, next) -> { acc.putAll(next); return acc; }))
//				.orElseGet(MacroStringMap::new);
//	}


//	public MacroStringMap resolveAll(final MacroObjectMap macroObjectMap)
//	{
//		MacroStringMap result = new MacroStringMap();
//
//		for (Map.Entry<MacroKey, Object> entry : macroObjectMap.entrySet())
//		{
//			for (Adapter adapter : adapterRegistry.getMatchingAdapters(entry.getValue()).toList())
//			{
//				adapter.adapt(entry.getValue()).ifPresent(adapted ->
//				{
//					MacroStringMap extracted = fieldExtractor.extract(adapter, adapted, entry.getKey());
//					result.putAll(extracted); // Key priority defined by map order
//				});
//			}
//		}
//
//		return result;
//	}

}
