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

import com.winterhavenmc.library.messagebuilder.keys.MacroKey;

import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Stream;


/**
 * Creates a stream of String containing placeholders from a message String using a regex pattern.
 * Placeholders are enclosed in delimiters, which are removed from the resulting placeholder string,
 * and may contain periods, denoting compound dot-separated keys made of a base key and any number of subkeys.
 */
public class PlaceholderMatcher implements Matcher
{
	@Override
	public Stream<MacroKey> match(final String input, final Pattern pattern)
	{
		return pattern.matcher(input).results()
				.map(matchResult -> MacroKey.of(matchResult.group(1)))
				.flatMap(Optional::stream);
	}

}
