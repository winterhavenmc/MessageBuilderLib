/*
 * Copyright (c) 2022-2025 Tim Savage.
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

package com.winterhavenmc.library.messagebuilder.adapters.pipeline.processors;

import com.winterhavenmc.library.messagebuilder.adapters.pipeline.replacers.RegexMacroReplacer;

import com.winterhavenmc.library.messagebuilder.core.ports.matchers.PlaceholderMatcher;
import com.winterhavenmc.library.messagebuilder.core.ports.processors.Processor;
import com.winterhavenmc.library.messagebuilder.core.ports.replacers.MacroReplacer;
import com.winterhavenmc.library.messagebuilder.core.ports.resolvers.macro.ValueResolver;

import com.winterhavenmc.library.messagebuilder.models.language.FinalMessageRecord;
import com.winterhavenmc.library.messagebuilder.models.language.ValidMessageRecord;
import com.winterhavenmc.library.messagebuilder.core.maps.MacroObjectMap;


/**
 * Default implementation of the {@link Processor} interface responsible for performing
 * macro replacement on message fields.
 *
 * <p>This class uses a {@link MacroReplacer}
 * to apply all relevant placeholder substitutions on a
 * {@link com.winterhavenmc.library.messagebuilder.models.language.ValidMessageRecord},
 * producing a new {@link com.winterhavenmc.library.messagebuilder.models.language.FinalMessageRecord}
 * with all final strings populated.
 *
 * <p>It serves as the final step in the message pipeline before rendering or dispatching.
 *
 * @see Processor
 * @see MacroReplacer
 * @see com.winterhavenmc.library.messagebuilder.models.language.ValidMessageRecord
 * @see com.winterhavenmc.library.messagebuilder.models.language.FinalMessageRecord
 */
public class MessageProcessor implements Processor
{
	private final MacroReplacer macroReplacer;


	/**
	 * Constructs a {@code MessageProcessor} using the provided {@link ValueResolver} and {@link PlaceholderMatcher}
	 * to configure the underlying {@link MacroReplacer}.
	 *
	 * @param resolver the macro resolver used to extract string representations from context objects
	 * @param placeholderMatcher the placeholder matcher used to detect macro keys in strings
	 */
	public MessageProcessor(final ValueResolver resolver, final PlaceholderMatcher placeholderMatcher)
	{
		this.macroReplacer = new RegexMacroReplacer(resolver, placeholderMatcher);
	}


	/**
	 * Processes a {@link com.winterhavenmc.library.messagebuilder.models.language.ValidMessageRecord}
	 * by replacing all macros in its fields using the provided {@link MacroObjectMap}.
	 *
	 * <p>This includes the {@code message}, {@code title}, and {@code subtitle} fields,
	 * which are resolved and returned as part of a new {@link FinalMessageRecord}.
	 *
	 * @param messageRecord the valid message record to process
	 * @param macroObjectMap the macro context object map used to resolve placeholder values
	 * @return a {@code FinalMessageRecord} with all macros resolved and final strings populated
	 */
	@Override
	public FinalMessageRecord process(final ValidMessageRecord messageRecord, final MacroObjectMap macroObjectMap)
	{
		// return new message record with final string fields added with macro replacements performed
		return messageRecord.withFinalStrings(
				macroReplacer.replace(macroObjectMap, messageRecord.message()),
				macroReplacer.replace(macroObjectMap, messageRecord.title()),
				macroReplacer.replace(macroObjectMap, messageRecord.subtitle()));
	}

}
