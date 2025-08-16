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

package com.winterhavenmc.library.messagebuilder.pipeline.matcher;

//import com.winterhavenmc.library.messagebuilder.keys.LegacyMacroKey;
import com.winterhavenmc.library.messagebuilder.keys.ValidMacroKey;

import java.util.regex.Pattern;
import java.util.stream.Stream;


/**
 * A functional interface representing a strategy for extracting {@link ValidMacroKey}
 * instances from an input string using a specified regular expression pattern.
 *
 * <p>This interface abstracts the placeholder matching logic used throughout the message
 * processing pipeline, allowing different implementations or syntaxes if desired.
 *
 * <p>Typically used to:
 * <ul>
 *   <li>Find base macro keys that indicate which objects should be resolved</li>
 *   <li>Locate full placeholder keys to be substituted during rendering</li>
 * </ul>
 *
 * <p>Implementations must return a stream of distinct {@code LegacyMacroKey} instances derived
 * from all matches found in the input string.
 *
 * @see com.winterhavenmc.library.messagebuilder.pipeline.matcher.PlaceholderMatcher
 * @see ValidMacroKey
 */
@FunctionalInterface
public interface Matcher
{
	/**
	 * Extracts macro keys from the given input string using the specified regular expression pattern.
	 *
	 * <p>Each successful match is converted into a {@link ValidMacroKey}.
	 * Implementations may choose to skip invalid or malformed macro segments.
	 *
	 * @param input   the raw string to search for macros (e.g., a message from the YAML file)
	 * @param pattern the regular expression pattern used to locate macro placeholders
	 * @return a stream of matched {@code LegacyMacroKey} instances (may be empty but never {@code null})
	 */
	Stream<ValidMacroKey> match(String input, Pattern pattern);
}
