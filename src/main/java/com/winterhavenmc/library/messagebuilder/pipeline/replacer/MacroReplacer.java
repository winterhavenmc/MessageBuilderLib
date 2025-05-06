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
import com.winterhavenmc.library.messagebuilder.pipeline.containers.MacroStringMap;
import com.winterhavenmc.library.messagebuilder.pipeline.matcher.Matcher;
import com.winterhavenmc.library.messagebuilder.pipeline.resolvers.Resolver;
import com.winterhavenmc.library.messagebuilder.util.Delimiter;

import java.util.Optional;
import java.util.regex.Pattern;


public class MacroReplacer implements Replacer
{
	private final Resolver resolver;
	private final Matcher placeholderMatcher;

	public static final Pattern FULL_KEY_PATTERN = Pattern.compile(
			Pattern.quote(Delimiter.OPEN.toString())
					+ "(\\p{Lu}[\\p{Alnum}_.]+)"
					+ Pattern.quote(Delimiter.CLOSE.toString()));

	public static final Pattern BASE_KEY_PATTERN = Pattern.compile(
			Pattern.quote(Delimiter.OPEN.toString())
					+ "(\\p{Lu}[\\p{Alnum}_]+)[\\p{Alnum}_.]*"
					+ Pattern.quote(Delimiter.CLOSE.toString()));



	/**
	 * Class constructor
	 *
	 * @param resolver resolver instance
	 * @param placeholderMatcher placeholder matcher instance
	 */
	public MacroReplacer(final Resolver resolver, final Matcher placeholderMatcher)
	{
		this.resolver = resolver;
		this.placeholderMatcher = placeholderMatcher;
	}


	/**
	 * Replace macros in a message to be sent
	 *
	 * @param macroObjectMap the context map containing other objects whose values may be retrieved
	 * @param messageString the message with placeholders to be replaced by macro values
	 * @return the string with all macro replacements performed
	 */
	@Override
	public String replace(final MacroObjectMap macroObjectMap, final String messageString)
	{
		String validMessageString = (messageString != null)
				? messageString
				: "";

		MacroStringMap stringMap = placeholderMatcher.match(validMessageString, BASE_KEY_PATTERN)
				.collect(MacroStringMap::new, (map, key) -> map
				.putAll(resolver.resolve(key, macroObjectMap)), MacroStringMap::putAll);

		return placeholderMatcher.match(validMessageString, FULL_KEY_PATTERN)
				.distinct()
				.reduce(validMessageString,
						(result, key) -> result.replace(
								key.asPlaceholder(),
								Optional.ofNullable(stringMap.get(key)).orElse(key.asPlaceholder())), (s1, s2) -> s2);
	}

}
