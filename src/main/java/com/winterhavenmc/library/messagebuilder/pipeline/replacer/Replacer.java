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

package com.winterhavenmc.library.messagebuilder.pipeline.replacer;

import com.winterhavenmc.library.messagebuilder.pipeline.containers.MacroObjectMap;


/**
 * A functional interface representing a macro replacement engine that substitutes
 * placeholder values within a message string using data from a
 * {@link com.winterhavenmc.library.messagebuilder.pipeline.containers.MacroObjectMap}.
 *
 * <p>This interface abstracts the final stage in the message-building pipeline, where
 * macros (e.g., {@code %PLAYER_NAME%}, {@code %LOCATION_X%}) are resolved to actual
 * string values prior to display.
 *
 * <p>Implementations must ensure that all macros present in the input string are
 * replaced using context objects available in the provided {@code MacroObjectMap}.
 *
 * @see com.winterhavenmc.library.messagebuilder.pipeline.containers.MacroObjectMap
 * @see MacroReplacer
 */
public interface Replacer
{
	/**
	 * Replaces all macro placeholders in the provided message string using values
	 * from the given macro object map.
	 *
	 * @param macroObjectMap a map of macro keys to context objects
	 * @param messageString the input message string containing one or more macros
	 * @return the fully formatted string with all resolvable macros replaced
	 */
	String replace(MacroObjectMap macroObjectMap, String messageString);
}
