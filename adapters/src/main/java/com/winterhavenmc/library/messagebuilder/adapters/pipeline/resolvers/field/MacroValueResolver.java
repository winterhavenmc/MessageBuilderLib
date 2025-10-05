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

package com.winterhavenmc.library.messagebuilder.adapters.pipeline.resolvers.field;

import com.winterhavenmc.library.messagebuilder.core.ports.resolvers.macro.ValueResolver;
import com.winterhavenmc.library.messagebuilder.models.keys.ValidMacroKey;
import com.winterhavenmc.library.messagebuilder.core.maps.MacroObjectMap;
import com.winterhavenmc.library.messagebuilder.core.maps.MacroStringMap;

import java.util.List;


/**
 * A {@link ValueResolver} implementation that delegates resolution to a chain of
 * other {@link ValueResolver} instances in order.
 * <p>
 * This class attempts to resolve macro values by invoking each resolver in the
 * order provided during construction. Results from earlier resolvers in the
 * chain take precedence—if a macro string has already been mapped, subsequent
 * resolvers will not overwrite it.
 * <p>
 * Typical usage involves registering a {@code CompositeResolver} first, followed by
 * an {@code AtomicResolver}, allowing composite (adapted) values to take priority
 * over simpler atomic ones only if the base string has not already been defined.
 * {@snippet lang = "java":
 * ValueResolver resolver = new MacroValueResolver(List.of(
 *     new CompositeResolver(),
 *     new AtomicResolver()
 * ));
 *}
 *
 * @see ValueResolver
 * @see CompositeResolver
 * @see AtomicResolver
 */
public class MacroValueResolver implements ValueResolver
{
	private final List<ValueResolver> resolvers;


	/**
	 * Constructs a {@code MacroValueResolver} with the given list of delegate resolvers.
	 * The resolvers will be invoked in the order provided.
	 *
	 * @param resolvers the ordered list of {@link ValueResolver} instances to delegate to
	 */
	public MacroValueResolver(List<ValueResolver> resolvers)
	{
		this.resolvers = resolvers;
	}


	/**
	 * Resolves a {@link ValidMacroKey} using the available macro objects in the given
	 * {@link MacroObjectMap}, by delegating to each registered resolver in sequence.
	 * <p>
	 * Values resolved earlier take precedence—existing keys in the result map will
	 * not be overwritten by later resolvers.
	 *
	 * @param macroKey the base string to resolve values for
	 * @param macroObjectMap the object map containing macro values to be resolved
	 * @return a {@link MacroStringMap} containing resolved string-value pairs
	 */
	@Override
	public MacroStringMap resolve(final ValidMacroKey macroKey, final MacroObjectMap macroObjectMap)
	{
		return resolvers.stream()
				.map(resolver -> resolver.resolve(macroKey, macroObjectMap))
				.collect(
						MacroStringMap::new,
						(map1, map2) -> map2.entrySet()
								.forEach(entry -> map1.putIfAbsent(entry.getKey(), entry.getValue())),
						(map3, map4) -> map4.entrySet()
								.forEach(entry -> map3.putIfAbsent(entry.getKey(), entry.getValue()))
				);
	}

}
