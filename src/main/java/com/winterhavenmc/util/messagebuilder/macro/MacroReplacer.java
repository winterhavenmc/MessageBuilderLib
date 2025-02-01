/*
 * Copyright (c) 2022 Tim Savage.
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

package com.winterhavenmc.util.messagebuilder.macro;

import com.winterhavenmc.util.messagebuilder.context.ContextMap;
import com.winterhavenmc.util.messagebuilder.macro.processor.*;
import com.winterhavenmc.util.messagebuilder.pipeline.Replacer;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.messages.MessageRecord;
import com.winterhavenmc.util.messagebuilder.util.LocalizedException;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;

import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

import static com.winterhavenmc.util.messagebuilder.util.LocalizedException.MessageKey.PARAMETER_NULL;
import static com.winterhavenmc.util.messagebuilder.util.LocalizedException.Parameter.*;


/**
 * This class provides handling of the Macro Processors and their Registry
 */
public class MacroReplacer implements Replacer {

	private final static String DELIMITER_OPEN = "{";
	private final static String DELIMITER_CLOSE = "}";

	// Regex pattern to match placeholders with delimiters
	private final Pattern MACRO_PATTERN;

	private final ProcessorRegistry processorRegistry;


	/**
	 * Class constructor
	 */
	public MacroReplacer()
	{
		this.processorRegistry = new ProcessorRegistry(new DependencyContext());
		this.MACRO_PATTERN = getRegex();
	}


	/**
	 * Replace macros in a message to be sent
	 *
	 * @param messageRecord the message record to have macro placeholders replaced in message and title strings
	 * @param contextMap the context map containing other objects whose values may be retrieved
	 * @return a new {@code MessageRecord} with all macro replacements performed and placed into the final string fields
	 */
	@Override
	public Optional<MessageRecord> replaceMacros(MessageRecord messageRecord, ContextMap contextMap)
	{
		if (contextMap == null) { throw new LocalizedException(PARAMETER_NULL, CONTEXT_MAP); }
		if (messageRecord == null) { throw new LocalizedException(PARAMETER_NULL, MESSAGE_RECORD); }

		// return new message record with final string fields added with macro replacements performed
		return messageRecord.withFinalStrings(
				replaceMacros(contextMap, messageRecord.message()),
				replaceMacros(contextMap, messageRecord.title()),
				replaceMacros(contextMap, messageRecord.subtitle())
		);
	}


	/**
	 * Check if a message string contains any macro placeholders by matching delimiters
	 * containing a valid placeholder string
	 *
	 * @param message the message to check for macro placeholders
	 * @return {@code true} if the message string contains any macro placeholders
	 */
	public boolean containsMacros(String message)
	{
		return MACRO_PATTERN.matcher(message).find();
	}


	/**
	 * Replace macros in a message to be sent
	 *
	 * @param contextMap    the context map containing other objects whose values may be retrieved
	 * @param messageString the message with placeholders to be replaced by macro values
	 * @return the string with all macro replacements performed
	 */
	public String replaceMacros(final ContextMap contextMap, final String messageString)
	{
		if (contextMap == null) { throw new LocalizedException(PARAMETER_NULL, CONTEXT_MAP); }
		if (messageString == null) { throw new LocalizedException(PARAMETER_NULL, MESSAGE_STRING); }

		// copy message string to local variable
		String modifiedMessageString = messageString;

		// only process macro tokens if message string contains a pair of macro delimiters
		if (containsMacros(modifiedMessageString))
		{
			// add recipient fields to context map
			addRecipientContext(contextMap);

			// final result map of keys and processed string values
			ResultMap replacementStringMap = new ResultMap();
			replacementStringMap.putAll(convertValuesToStrings(contextMap));

			// do macro replacements on message string
			modifiedMessageString = performReplacements(replacementStringMap, modifiedMessageString);
		}

		return modifiedMessageString;
	}


	void addRecipientContext(ContextMap contextMap)
	{
		if (contextMap == null) { throw new LocalizedException(PARAMETER_NULL, CONTEXT_MAP); }

		// get recipient from context map
		CommandSender recipient = contextMap.getRecipient();

		// put recipient name in context map
		String key = "RECIPIENT";
		contextMap.put(key, recipient.getName());

		// if recipient is an entity, put recipient location in macro object map
		if (recipient instanceof Entity entity)
		{
			String locationKey = key.concat(".LOCATION");
			contextMap.put(locationKey, entity.getLocation());
		}
	}


	ResultMap convertValuesToStrings(ContextMap contextMap)
	{
		if (contextMap == null) { throw new LocalizedException(PARAMETER_NULL, CONTEXT_MAP); }

		ResultMap resultMap = new ResultMap();
		for (Map.Entry<String, Object> entry : contextMap.entrySet())
		{
			ProcessorType processorType = ProcessorType.matchType(entry.getValue());
			MacroProcessor macroProcessor = processorRegistry.get(processorType);
			resultMap.putAll(macroProcessor.resolveContext(entry.getKey(), contextMap));
		}

		return resultMap;
	}


	String performReplacements(final ResultMap replacementMap, final String messageString)
	{
		if (replacementMap == null) { throw new LocalizedException(PARAMETER_NULL, REPLACEMENT_MAP); }
		if (messageString == null) { throw new LocalizedException(PARAMETER_NULL, MESSAGE_STRING); }

		String modifiedMessageString = messageString;

		for (Map.Entry<String, String> entry : replacementMap.entrySet())
		{
			String macroToken = DELIMITER_OPEN + entry.getKey() + DELIMITER_CLOSE;
			modifiedMessageString = modifiedMessageString.replace(macroToken, entry.getValue());
		}

		return modifiedMessageString;
	}


	public static Pattern getRegex()
	{
		return Pattern.compile("\\" + DELIMITER_OPEN + "[\\p{Lu}0-9_]+" + DELIMITER_CLOSE);
	}

}
