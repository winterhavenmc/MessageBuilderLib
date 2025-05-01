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

import com.winterhavenmc.library.messagebuilder.keys.MacroKey;
import com.winterhavenmc.library.messagebuilder.pipeline.containers.MacroObjectMap;
import com.winterhavenmc.library.messagebuilder.pipeline.containers.MacroStringMap;
import com.winterhavenmc.library.messagebuilder.pipeline.matcher.Matcher;
import com.winterhavenmc.library.messagebuilder.pipeline.matcher.PlaceholderMatcher;
import com.winterhavenmc.library.messagebuilder.pipeline.resolvers.Resolver;
import com.winterhavenmc.library.messagebuilder.util.Delimiter;

import java.util.Objects;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.function.Function;

import static com.winterhavenmc.library.messagebuilder.validation.ErrorMessageKey.PARAMETER_NULL;
import static com.winterhavenmc.library.messagebuilder.validation.Parameter.MESSAGE_STRING;
import static com.winterhavenmc.library.messagebuilder.validation.Parameter.REPLACEMENT_MAP;
import static com.winterhavenmc.library.messagebuilder.validation.ValidationHandler.throwing;
import static com.winterhavenmc.library.messagebuilder.validation.Validator.validate;

public class StringReplacer
{
	private final static MacroKey DEFAULT_KEY = MacroKey.of("KEY").orElseThrow();
	private final Resolver resolver;
	private final Matcher matcher;


	StringReplacer(Resolver resolver, Matcher matcher)
	{
		this.resolver = resolver;
		this.matcher = matcher;
	}


	/**
	 * Replace macros in a message to be sent
	 *
	 * @param macroObjectMap    the context map containing other objects whose values may be retrieved
	 * @param messageString the message with placeholders to be replaced by macro values
	 * @return the string with all macro replacements performed
	 */
	public String replaceStrings(final MacroObjectMap macroObjectMap, final String messageString)
	{
		validate(messageString, Objects::isNull, throwing(PARAMETER_NULL, MESSAGE_STRING));

		Function<String, Optional<MacroKey>> toMacroKey = MacroKey::of;
		Function<MacroKey, MacroStringMap> resolveKey = key -> resolver.resolve(key, macroObjectMap);
		BinaryOperator<MacroStringMap> mergeMaps = (r1, r2) -> {
			r1.putAll(r2);
			return r1;
		};

		return doReplacements(matcher.match(messageString)
				.map(toMacroKey)
				.flatMap(Optional::stream)
				.map(resolveKey)
				.reduce(new MacroStringMap(), mergeMaps), messageString);
	}


	/**
	 * Replace values in the message string with macro string values in replacementMap
	 *
	 * @param replacementMap a collection of key/value pairs representing the placeholders and their replacement values
	 * @param messageString  the message string containing placeholders to be replaced
	 * @return {@code String} the final string with all replacements performed
	 */
	String doReplacements(final MacroStringMap replacementMap, final String messageString)
	{
		validate(replacementMap, Objects::isNull, throwing(PARAMETER_NULL, REPLACEMENT_MAP));
		validate(messageString, Objects::isNull, throwing(PARAMETER_NULL, MESSAGE_STRING));

		return new PlaceholderMatcher().match(messageString)
				.reduce(messageString, (msg, placeholder) ->
						msg.replace(Delimiter.OPEN + placeholder + Delimiter.CLOSE,
								replacementMap.getValueOrKey(MacroKey.of(placeholder).orElse(DEFAULT_KEY))));
	}

}
