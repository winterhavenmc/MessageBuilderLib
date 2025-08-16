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
import com.winterhavenmc.library.messagebuilder.keys.ValidMacroKey;

import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Stream;


/**
 * Default implementation of the {@link Matcher} interface that uses Java’s built-in
 * {@link java.util.regex.Pattern} and {@link java.util.regex.MatchResult} APIs to extract
 * macro keys from an input string.
 *
 * <p>This class transforms matching groups into {@link ValidMacroKey}
 * instances using {@code LegacyMacroKey.of(...)}, and silently skips any invalid results.
 *
 * <p>It is designed for use in both the resolution and replacement stages of the pipeline,
 * depending on whether base keys or full macro keys are being matched.
 *
 * @see Matcher
 * @see ValidMacroKey
 */
public class PlaceholderMatcher implements Matcher
{
	@Override
	public Stream<ValidMacroKey> match(final String input, final Pattern pattern)
	{
		return pattern.matcher(input).results()
				.map(matchResult -> MacroKey.of(matchResult.group(1)).isValid())
				.flatMap(Optional::stream);
	}

}
