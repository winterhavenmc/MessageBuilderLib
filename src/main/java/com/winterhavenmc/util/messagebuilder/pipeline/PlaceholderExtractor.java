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

package com.winterhavenmc.util.messagebuilder.pipeline;

import java.util.regex.Pattern;
import java.util.stream.Stream;


public class PlaceholderExtractor implements Extractor
{
	private static final Pattern pattern = Pattern.compile(
            Pattern.quote(Delimiter.OPEN.toString()) +
                    "([\\p{Lu}0-9_]+)" +
                    Pattern.quote(Delimiter.CLOSE.toString()));


	@Override
	public Stream<String> extract(final String input)
	{
		return pattern.matcher(input).results().map(matchResult -> matchResult.group(1));
	}

}
