/*
 * Copyright (c) 2022 Tim Savage.
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

package com.winterhavenmc.util.messagebuilder.macro.processor;

import com.winterhavenmc.util.messagebuilder.context.ContextMap;


/**
 * An interface for processing macros in the MessageBuilder library.
 * <p>
 * MacroProcessors are responsible for resolving contextual information from
 * input values and populating a {@link ContextMap} with placeholder
 * replacements. Each processor focuses on a specific type of input,
 * such as locations, entities, or custom types, and provides the necessary
 * mappings to integrate dynamic data into messages.
 * <p>
 * Implementations of this interface must define how placeholders are
 * generated and resolved based on the input value and its context.
 *
 * <br>
 * <b>Examples:</b>
 * <pre>
 * {@code
 * // A LocationProcessor might resolve placeholders for a player's position:
 * // %HOME_LOCATION_WORLD% -> "world"
 * // %HOME_LOCATION_X% -> "123"
 * // %HOME_LOCATION_Y% -> "64"
 * // %HOME_LOCATION_Z% -> "-789"
 * // %HOME_LOCATION% -> "world:123,64,-789"
 * }
 * </pre>
 *
 * <b>Example 2:</b> Resolving a single placeholder for a number
 * <pre>
 * {@code
 * // A NumberProcessor might resolve a single placeholder for any Java Number:
 * // Input: 42
 * // Placeholder: %SCORE%
 * // Resolved: %SCORE% -> "42"
 *
 * public class NumberProcessor implements MacroProcessor {
 *     @Override
 *     public <T> ResultMap resolveContext(String key, ContextMap contextMap, T value) {
 *         if (value instanceof Number number) {
 *             contextMap.put(key, number.toString());
 *             return ResultMap.success(key);
 *         }
 *         return ResultMap.failure(key, "Value is not a number.");
 *     }
 * }
 * }
 * </pre>


 * @see ContextMap
 */
@FunctionalInterface
public interface MacroProcessor<T> {

	/**
	 * Resolves contextual information from the given value and populates the context map.
	 * <p>
	 * Implementations should map relevant data from the input value to placeholders,
	 * using the provided {@code key} as a namespace to prevent collisions. The resolved
	 * context entries are stored in the {@link ContextMap}, and the method returns
	 * a {@link ResultMap} containing the value or values resulting from the processing,
	 * and any new fields generated for them with a namespaced key which is a child key of
	 * the original object's namespaced key.
	 * 
	 *
	 * @param key         the unique key or namespace for this macro entry
	 * @param contextMap  the {@link ContextMap} to populate with resolved placeholders
	 * @param value       the input value to resolve into context
	 * @return a {@link ResultMap} containing resolved macros and their replacements
	 */
	ResultMap resolveContext(String key, ContextMap contextMap, T value);

}
