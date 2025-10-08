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

package com.winterhavenmc.library.messagebuilder.core.ports.pipeline.resolvers.macro;

import com.winterhavenmc.library.messagebuilder.core.pipeline.adapters.Adapter;

import com.winterhavenmc.library.messagebuilder.models.keys.ValidMacroKey;
import com.winterhavenmc.library.messagebuilder.core.maps.MacroObjectMap;
import com.winterhavenmc.library.messagebuilder.core.maps.MacroStringMap;


/**
 * Defines a contract for resolving values from a {@link MacroObjectMap} into a
 * set of string-replaceable entries mapped by {@link ValidMacroKey}s.
 * <p>
 * A {@code ValueResolver} converts one or more input objects associated with a given
 * macro string into a {@link MacroStringMap}, where each entry corresponds to a
 * placeholder that may be used in a templated message or text.
 * <p>
 * Implementations may vary in complexity:
 * <ul>
 *   <li>{@code AtomicResolver} handles single, directly formattable values.</li>
 *   <li>{@code CompositeResolver} handles structured or composite values using
 *       {@link Adapter}s to produce multiple sub-entries.</li>
 *   <li>{@code FieldResolver} combines multiple resolvers in a prioritized chain.</li>
 * </ul>
 * This interface supports functional-style composition and may be used in lambda
 * form where appropriate.
 *
 * @see ValidMacroKey
 * @see MacroObjectMap
 * @see MacroStringMap
 */
@FunctionalInterface
public interface ValueResolver
{
	/**
	 * Resolves a set of string values from the given {@link MacroObjectMap}
	 * for the provided {@link ValidMacroKey}. Each resolved value corresponds to
	 * a macro placeholder that may appear in a templated message.
	 * <p>
	 * The resulting {@link MacroStringMap} may contain one or more entries.
	 * Implementations may also return an empty map if no resolution was possible.
	 *
	 * @param key the base macro string used to look up and resolve objects
	 * @param macroObjectMap the map of objects available for macro resolution
	 * @return a {@link MacroStringMap} containing resolved macro string-value pairs
	 */
	MacroStringMap resolve(ValidMacroKey key, MacroObjectMap macroObjectMap);
}
