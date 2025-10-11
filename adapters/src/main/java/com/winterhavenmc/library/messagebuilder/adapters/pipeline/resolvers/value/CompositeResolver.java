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

package com.winterhavenmc.library.messagebuilder.adapters.pipeline.resolvers.value;

import com.winterhavenmc.library.messagebuilder.adapters.pipeline.accessors.MacroFieldAccessor;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.Accessor;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.AccessorRegistry;

import com.winterhavenmc.library.messagebuilder.adapters.pipeline.accessors.FieldAccessorRegistry;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.resolvers.macro.ValueResolver;
import com.winterhavenmc.library.messagebuilder.models.keys.ValidMacroKey;
import com.winterhavenmc.library.messagebuilder.core.maps.MacroObjectMap;
import com.winterhavenmc.library.messagebuilder.core.maps.MacroStringMap;


/**
 * A {@link ValueResolver} implementation that handles complex or structured objects
 * by applying one or more matching {@link Accessor} instances.
 * <p>
 * The {@code CompositeResolver} delegates the resolution process to registered
 * adapters that know how to transform an object into a set of string values,
 * each of which is mapped to a derived {@link ValidMacroKey}. The resolved entries
 * typically include sub-keys that extend the base string in a dot-notated form.
 * <p>
 * This resolver does not overwrite existing entries in the base string when used
 * in conjunction with a {@link MacroValueResolver}; it merely returns all extracted
 * string-value pairs. The calling resolver is responsible for deciding merge behavior.
 *
 * <p>Example flow:
 * <ol>
 *   <li>Retrieve the object from the {@link MacroObjectMap} using the given {@code macroKey}.</li>
 *   <li>Find all matching adapters for the object via the {@link FieldAccessorRegistry}.</li>
 *   <li>For each adapter, adapt the object and extract string-value mappings via the {@link MacroFieldAccessor}.</li>
 *   <li>Aggregate all mappings into a {@link MacroStringMap}.</li>
 * </ol>
 *
 * @see ValueResolver
 * @see FieldAccessorRegistry
 * @see Accessor
 * @see MacroFieldAccessor
 */
public class CompositeResolver implements ValueResolver
{
	private final AccessorRegistry accessorRegistry;
	private final MacroFieldAccessor macroFieldAccessor;


	/**
	 * Constructs a {@code CompositeResolver} with the given adapter registry
	 * and field extractor.
	 *
	 * @param accessorRegistry an instance that manages and matches {@link Accessor} objects
	 * @param macroFieldAccessor an instance used to extract field values from adapted objects
	 */
	public CompositeResolver(final AccessorRegistry accessorRegistry,
							 final MacroFieldAccessor macroFieldAccessor)
	{
		this.accessorRegistry = accessorRegistry;
		this.macroFieldAccessor = macroFieldAccessor;
	}


	/**
	 * Resolves a {@link ValidMacroKey} by attempting to adapt the associated object
	 * from the {@link MacroObjectMap} using applicable {@link Accessor} instances.
	 * <p>
	 * For each adapter that supports the object, this method invokes the adapter’s
	 * {@code adapt} method, then extracts sub-string mappings via the {@link MacroFieldAccessor}.
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

		macroObjectMap.get(macroKey).ifPresent(object -> accessorRegistry
				.getMatchingAdapters(object)
				.forEach(adapter -> adapter
						.adapt(object)
						.ifPresent(adapted -> macroStringMap
								.putAll(macroFieldAccessor.extract(macroKey, adapter, adapted)))));

		return macroStringMap;
	}

}
