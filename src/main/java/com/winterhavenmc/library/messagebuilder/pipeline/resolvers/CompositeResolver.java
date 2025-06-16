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


/**
 * Resolves compound object values that are mapped to subkeys of the base key. Values may also be duplicated
 * in the base key entry, depending on the object adapter type. For instance, an adapted Name value for an object
 * will exist under the BASEKEY.NAME entry, as well as the BASEKEY entry for an object in the result map.
 * Priority is given by the order of registration of the adapters in the {@link AdapterRegistry}.
 */
public class CompositeResolver implements Resolver
{
	private final AdapterRegistry adapterRegistry;
	private final FieldExtractor fieldExtractor;


	/**
	 * Class constructor
	 *
	 * @param adapterRegistry an instance of an adapter registry
	 * @param fieldExtractor an instance of a field extractor
	 */
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

		macroObjectMap.get(macroKey).ifPresent(object -> adapterRegistry
				.getMatchingAdapters(object)
				.forEach(adapter -> adapter
						.adapt(object)
						.ifPresent(adapted -> macroStringMap
								.putAll(fieldExtractor.extract(macroKey, adapter, adapted)))));

		return macroStringMap;
	}

}
