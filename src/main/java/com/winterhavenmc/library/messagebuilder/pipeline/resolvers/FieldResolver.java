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

import com.winterhavenmc.library.messagebuilder.keys.MacroKey;
import com.winterhavenmc.library.messagebuilder.pipeline.maps.MacroObjectMap;
import com.winterhavenmc.library.messagebuilder.pipeline.maps.MacroStringMap;

import java.util.List;


/**
 * A {@link Resolver} implementation that delegates resolution to a chain of
 * other {@link Resolver} instances in order.
 * <p>
 * This class attempts to resolve macro values by invoking each resolver in the
 * order provided during construction. Results from earlier resolvers in the
 * chain take precedence—if a macro key has already been mapped, subsequent
 * resolvers will not overwrite it.
 * <p>
 * Typical usage involves registering a {@code CompositeResolver} first, followed by
 * an {@code AtomicResolver}, allowing composite (adapted) values to take priority
 * over simpler atomic ones only if the base key has not already been defined.
 * {@snippet lang="java":
 * Resolver resolver = new FieldResolver(List.of(
 *     new CompositeResolver(),
 *     new AtomicResolver()
 * ));
 * }
 *
 * @see Resolver
 * @see CompositeResolver
 * @see AtomicResolver
 */
public class FieldResolver implements Resolver
{
	private final List<Resolver> resolvers;


	/**
	 * Constructs a {@code FieldResolver} with the given list of delegate resolvers.
	 * The resolvers will be invoked in the order provided.
	 *
	 * @param resolvers the ordered list of {@link Resolver} instances to delegate to
	 */
	public FieldResolver(List<Resolver> resolvers)
	{
		this.resolvers = resolvers;
	}


	/**
	 * Resolves a {@link MacroKey} using the available macro objects in the given
	 * {@link MacroObjectMap}, by delegating to each registered resolver in sequence.
	 * <p>
	 * Values resolved earlier take precedence—existing keys in the result map will
	 * not be overwritten by later resolvers.
	 *
	 * @param macroKey the base key to resolve values for
	 * @param macroObjectMap the object map containing macro values to be resolved
	 * @return a {@link MacroStringMap} containing resolved key-value pairs
	 */
	@Override
	public MacroStringMap resolve(final MacroKey macroKey, final MacroObjectMap macroObjectMap)
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
