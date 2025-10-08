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

package com.winterhavenmc.library.messagebuilder.adapters.pipeline.replacers;

import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.matchers.PlaceholderMatcher;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.replacers.MacroReplacer;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.resolvers.macro.ValueResolver;
import com.winterhavenmc.library.messagebuilder.models.Delimiter;

import com.winterhavenmc.library.messagebuilder.core.maps.MacroObjectMap;
import com.winterhavenmc.library.messagebuilder.core.maps.MacroStringMap;

import java.util.Optional;
import java.util.regex.Pattern;


/**
 * Default implementation of the {@link MacroReplacer} interface that performs macro substitution
 * on input strings using values derived from a {@link MacroObjectMap}.
 *
 * <p>This replacer extracts macros from the message using configurable regular expressions,
 * then delegates resolution of each macro to a {@link ValueResolver}.
 * Once resolved, macros are replaced in the string using their corresponding values.
 *
 * <p>The class supports two levels of macro detection:
 * <ul>
 *     <li>{@linkplain #BASE_KEY_PATTERN} — for determining candidate base keys used in resolution</li>
 *     <li>{@linkplain #FULL_KEY_PATTERN} — for actual replacement of placeholders in the final string</li>
 * </ul>
 *
 * <p>Unresolvable macros are left intact in the output string, ensuring safe and
 * fail-tolerant rendering behavior.
 *
 * @see MacroReplacer
 * @see ValueResolver
 * @see PlaceholderMatcher
 * @see MacroStringMap
 */
public class RegexMacroReplacer implements MacroReplacer
{
	private final ValueResolver resolver;
	private final PlaceholderMatcher placeholderMatcher;

	/**
	 * Pattern used to match fully qualified macro placeholders (e.g., {@code %ITEM.NAME_SINGULAR%})
	 * for the purpose of final string replacement.
	 */
	public static final Pattern FULL_KEY_PATTERN = Pattern.compile(
			Pattern.quote(Delimiter.OPEN.toString())
					+ "(\\p{Lu}[\\p{Alnum}_.]+)"
					+ Pattern.quote(Delimiter.CLOSE.toString()));


	/**
	 * Pattern used to match base macro keys (e.g., {@code [ITEM}}) for macro resolution via the {@link ValueResolver}.
	 * <p>
	 * This pattern determines which objects should be passed to the resolver to populate
	 * values for all matching derived keys.
	 */
	public static final Pattern BASE_KEY_PATTERN = Pattern.compile(
			Pattern.quote(Delimiter.OPEN.toString())
					+ "(\\p{Lu}[\\p{Alnum}_]+)[\\p{Alnum}_.]*"
					+ Pattern.quote(Delimiter.CLOSE.toString()));


	/**
	 * Constructs a {@code RegexMacroReplacer} with the given resolver and placeholder matcher.
	 *
	 * @param resolver the macro resolver responsible for producing string values for each macro
	 * @param placeholderMatcher the matcher used to identify macro patterns in message strings
	 */
	public RegexMacroReplacer(final ValueResolver resolver, final PlaceholderMatcher placeholderMatcher)
	{
		this.resolver = resolver;
		this.placeholderMatcher = placeholderMatcher;
	}


	/**
	 * Replaces all macro placeholders found in the given message string using the provided
	 * {@link MacroObjectMap}.
	 *
	 * <p>The replacement process is performed in two stages:
	 * <ol>
	 *   <li>Extract base macro keys from the string using {@linkplain #BASE_KEY_PATTERN}</li>
	 *   <li>Resolve and populate a {@link MacroStringMap} with string values</li>
	 *   <li>Substitute each matching placeholder using {@linkplain #FULL_KEY_PATTERN}</li>
	 * </ol>
	 *
	 * <p>If a macro cannot be resolved, it is left in-place as its original placeholder form.
	 *
	 * @param macroObjectMap a map of macro keys to context objects used for resolution
	 * @param messageString the input string containing macro placeholders (may be {@code null})
	 * @return the final formatted message with macros replaced
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
