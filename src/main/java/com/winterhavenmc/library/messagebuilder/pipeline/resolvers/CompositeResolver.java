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

import com.winterhavenmc.library.messagebuilder.keys.ValidMacroKey;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.Adapter;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.AdapterRegistry;
import com.winterhavenmc.library.messagebuilder.pipeline.maps.MacroObjectMap;
import com.winterhavenmc.library.messagebuilder.pipeline.extractor.FieldExtractor;
import com.winterhavenmc.library.messagebuilder.pipeline.maps.MacroStringMap;


/**
 * A {@link Resolver} implementation that handles complex or structured objects
 * by applying one or more matching {@link Adapter} instances.
 * <p>
 * The {@code CompositeResolver} delegates the resolution process to registered
 * adapters that know how to transform an object into a set of string values,
 * each of which is mapped to a derived {@link ValidMacroKey}. The resolved entries
 * typically include sub-keys that extend the base string in a dot-notated form.
 * <p>
 * This resolver does not overwrite existing entries in the base string when used
 * in conjunction with a {@link FieldResolver}; it merely returns all extracted
 * string-value pairs. The calling resolver is responsible for deciding merge behavior.
 *
 * <p>Example flow:
 * <ol>
 *   <li>Retrieve the object from the {@link MacroObjectMap} using the given {@code macroKey}.</li>
 *   <li>Find all matching adapters for the object via the {@link AdapterRegistry}.</li>
 *   <li>For each adapter, adapt the object and extract string-value mappings via the {@link FieldExtractor}.</li>
 *   <li>Aggregate all mappings into a {@link MacroStringMap}.</li>
 * </ol>
 *
 * @see Resolver
 * @see AdapterRegistry
 * @see Adapter
 * @see FieldExtractor
 */
public class CompositeResolver implements Resolver
{
	private final AdapterRegistry adapterRegistry;
	private final FieldExtractor fieldExtractor;


	/**
	 * Constructs a {@code CompositeResolver} with the given adapter registry
	 * and field extractor.
	 *
	 * @param adapterRegistry an instance that manages and matches {@link Adapter} objects
	 * @param fieldExtractor an instance used to extract field values from adapted objects
	 */
	public CompositeResolver(final AdapterRegistry adapterRegistry,
							 final FieldExtractor fieldExtractor)
	{
		this.adapterRegistry = adapterRegistry;
		this.fieldExtractor = fieldExtractor;
	}


	/**
	 * Resolves a {@link ValidMacroKey} by attempting to adapt the associated object
	 * from the {@link MacroObjectMap} using applicable {@link Adapter} instances.
	 * <p>
	 * For each adapter that supports the object, this method invokes the adapter’s
	 * {@code adapt} method, then extracts sub-string mappings via the {@link FieldExtractor}.
	 * <p>
	 * The resulting {@link MacroStringMap} contains mappings for the derived sub-keys.
	 * The base string may also be included if provided by the adapter and extractor.
	 *
	 * @param macroKey the string used to retrieve the source object from the macro object map
	 * @param macroObjectMap the object map containing input values to be resolved
	 * @return a {@link MacroStringMap} containing resolved sub-string mappings for the adapted object
	 */
	@Override
	public MacroStringMap resolve(final ValidMacroKey macroKey, final MacroObjectMap macroObjectMap)
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
