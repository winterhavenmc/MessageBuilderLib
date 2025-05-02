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

package com.winterhavenmc.library.messagebuilder.pipeline.replacer;

import com.winterhavenmc.library.messagebuilder.pipeline.containers.MacroObjectMap;
import com.winterhavenmc.library.messagebuilder.pipeline.matcher.Matcher;
import com.winterhavenmc.library.messagebuilder.pipeline.resolvers.Resolver;
import com.winterhavenmc.library.messagebuilder.model.language.FinalMessageRecord;
import com.winterhavenmc.library.messagebuilder.model.language.ValidMessageRecord;


/**
 * This class provides handling of the Macro Processors and their Registry
 */
public class MacroReplacer implements Replacer
{
	private final StringReplacer stringReplacer;


	public MacroReplacer(final Resolver resolver, final Matcher matcher)
	{
		this.stringReplacer = new StringReplacer(resolver, matcher);
	}


	/**
	 * Replace macros in a message to be sent
	 *
	 * @param messageRecord the message record to have macro placeholders replaced in message and title strings
	 * @return a new {@code FinalMessageRecord} with all macro replacements performed and placed into the final string fields
	 */
	@Override
	public FinalMessageRecord replaceMacros(final ValidMessageRecord messageRecord, final MacroObjectMap macroObjectMap)
	{
		// return new message record with final string fields added with macro replacements performed
		return messageRecord.withFinalStrings(
				stringReplacer.replaceStrings(macroObjectMap, messageRecord.message()),
				stringReplacer.replaceStrings(macroObjectMap, messageRecord.title()),
				stringReplacer.replaceStrings(macroObjectMap, messageRecord.subtitle()));
	}

}
