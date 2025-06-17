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

package com.winterhavenmc.library.messagebuilder.pipeline.processor;

import com.winterhavenmc.library.messagebuilder.pipeline.containers.MacroObjectMap;
import com.winterhavenmc.library.messagebuilder.pipeline.matcher.Matcher;
import com.winterhavenmc.library.messagebuilder.pipeline.replacer.MacroReplacer;
import com.winterhavenmc.library.messagebuilder.pipeline.resolvers.Resolver;
import com.winterhavenmc.library.messagebuilder.model.language.FinalMessageRecord;
import com.winterhavenmc.library.messagebuilder.model.language.ValidMessageRecord;


/**
 * Default implementation of the {@link Processor} interface responsible for performing
 * macro replacement on message fields.
 *
 * <p>This class uses a {@link com.winterhavenmc.library.messagebuilder.pipeline.replacer.MacroReplacer}
 * to apply all relevant placeholder substitutions on a
 * {@link com.winterhavenmc.library.messagebuilder.model.language.ValidMessageRecord},
 * producing a new {@link com.winterhavenmc.library.messagebuilder.model.language.FinalMessageRecord}
 * with all final strings populated.
 *
 * <p>It serves as the final step in the message pipeline before rendering or dispatching.
 *
 * @see Processor
 * @see com.winterhavenmc.library.messagebuilder.pipeline.replacer.MacroReplacer
 * @see com.winterhavenmc.library.messagebuilder.model.language.ValidMessageRecord
 * @see com.winterhavenmc.library.messagebuilder.model.language.FinalMessageRecord
 */
public class MessageProcessor implements Processor
{
	private final MacroReplacer macroReplacer;


	/**
	 * Constructs a {@code MessageProcessor} using the provided {@link Resolver} and {@link Matcher}
	 * to configure the underlying {@link MacroReplacer}.
	 *
	 * @param resolver the macro resolver used to extract string representations from context objects
	 * @param placeholderMatcher the placeholder matcher used to detect macro keys in strings
	 */
	public MessageProcessor(final Resolver resolver, final Matcher placeholderMatcher)
	{
		this.macroReplacer = new MacroReplacer(resolver, placeholderMatcher);
	}


	/**
	 * Processes a {@link com.winterhavenmc.library.messagebuilder.model.language.ValidMessageRecord}
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
