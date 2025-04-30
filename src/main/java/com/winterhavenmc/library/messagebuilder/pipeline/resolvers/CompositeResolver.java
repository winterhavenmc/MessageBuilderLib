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
	 * @param macroObjectMap a map containing key/value pairs of placeholder strings and their corresponding value object
	 * @return {@code MacroStringMap} a map containing the placeholder strings and the string representations of the values
	 */
	@Override
	public MacroStringMap resolve(final MacroKey macroKey, final MacroObjectMap macroObjectMap)
	{
		MacroStringMap macroStringMap = new MacroStringMap();

		macroObjectMap.get(macroKey).ifPresent(value ->
				resolveSubkeysInto(macroStringMap, value, macroKey, macroObjectMap));

		return macroStringMap;
	}


	/**
	 * Resolver static helper method
	 */
	private void resolveSubkeysInto(MacroStringMap macroStringMap, Object value, MacroKey macroKey, MacroObjectMap macroObjectMap)
	{
		adapterRegistry.getMatchingAdapters(value).forEach(adapter ->
				adapter.adapt(value).ifPresent(adapted ->
						fieldExtractor.extract(adapter, adapted, macroKey).keySet()
								.forEach(subKey -> macroStringMap.putAll(resolve(subKey, macroObjectMap)))
				)
		);
	}
}
