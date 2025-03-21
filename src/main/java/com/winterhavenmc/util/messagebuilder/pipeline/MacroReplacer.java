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

package com.winterhavenmc.util.messagebuilder.pipeline;

import com.winterhavenmc.util.messagebuilder.Message;
import com.winterhavenmc.util.messagebuilder.pipeline.processors.*;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.messages.MessageRecord;

import com.winterhavenmc.util.messagebuilder.validation.ValidationException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static com.winterhavenmc.util.messagebuilder.validation.MessageKey.PARAMETER_EMPTY;
import static com.winterhavenmc.util.messagebuilder.validation.MessageKey.PARAMETER_NULL;
import static com.winterhavenmc.util.messagebuilder.validation.Parameter.*;
import static com.winterhavenmc.util.messagebuilder.validation.Validator.validate;


/**
 * This class provides handling of the Macro Processors and their Registry
 */
public class MacroReplacer implements Replacer, Resolver
{
	private final static String DELIMITER_OPEN = "{";
	private final static String DELIMITER_CLOSE = "}";

	private final ProcessorRegistry processorRegistry;


	/**
	 * Class constructor
	 */
	public MacroReplacer()
	{
		this.processorRegistry = new ProcessorRegistry(new DependencyContext());
	}


	/**
	 * Replace macros in a message to be sent
	 *
	 * @param messageRecord the message record to have macro placeholders replaced in message and title strings
	 * @return a new {@code MessageRecord} with all macro replacements performed and placed into the final string fields
	 */
	@Override
	public Optional<MessageRecord> replaceMacros(final MessageRecord messageRecord, final Message message)
	{
		validate(messageRecord, Objects::isNull, () -> new ValidationException(PARAMETER_NULL, MESSAGE_RECORD));
		validate(message, Objects::isNull, () -> new ValidationException(PARAMETER_NULL, MESSAGE));

		// return new message record with final string fields added with macro replacements performed
		return messageRecord.withFinalStrings(
				replaceMacros(message.getContextMap(), messageRecord.message()),
				replaceMacros(message.getContextMap(), messageRecord.title()),
				replaceMacros(message.getContextMap(), messageRecord.subtitle())
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
		return getMatcher(message).find();
	}


	/**
	 * Create a {@link Stream} of placeholder strings in a message string
	 *
	 * @param input the message string with placeholders
	 * @return a {@code Stream} of placeholder strings
	 */
	public Stream<String> getPlaceholderStream(final String input)
	{
		return getMatcher(input).results().map(matchResult -> matchResult.group(1));
	}


	/**
	 * Replace macros in a message to be sent
	 *
	 * @param contextMap    the context map containing other objects whose values may be retrieved
	 * @param messageString the message with placeholders to be replaced by macro values
	 * @return the string with all macro replacements performed
	 */
	String replaceMacros(final ContextMap contextMap, final String messageString)
	{
		validate(contextMap, Objects::isNull, () -> new ValidationException(PARAMETER_NULL, CONTEXT_MAP));
		validate(messageString, Objects::isNull, () -> new ValidationException(PARAMETER_NULL, MESSAGE_STRING));
		validate(messageString, String::isBlank, () -> new ValidationException(PARAMETER_EMPTY, MESSAGE_STRING));

		// copy message string to local variable
		String modifiedMessageString = messageString;

		// only process macro tokens if message string contains a pair of macro delimiters
		if (containsMacros(modifiedMessageString))
		{
			// add recipient fields to context map
			addRecipientContext(contextMap);

			// final result map of keys and processed string values
			ResultMap replacementStringMap = new ResultMap();
			replacementStringMap.putAll(resolveContext(contextMap));

			// do macro replacements on message string
			modifiedMessageString = performReplacements(replacementStringMap, modifiedMessageString);
		}

		return modifiedMessageString;
	}


	/**
	 * Add the recipient fields to the context map, including location fields if the recipient is a player
	 *
	 * @param contextMap a map containing key/value pairs of placeholder names and the value objects from which
	 *                   their replacement strings will be derived
	 */
	void addRecipientContext(ContextMap contextMap)
	{
		validate(contextMap, Objects::isNull, () -> new ValidationException(PARAMETER_NULL, CONTEXT_MAP));

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


	/**
	 * Convert the value objects contained in the context map into their string representations in a
	 * new result map.
	 *
	 * @param contextMap a map containing key/value pairs of placeholder strings and their corresponding value object
	 * @return {@code ResultMap} a map containing the placeholder strings and the string representations of the values
	 */
	@Override
	public ResultMap resolveContext(ContextMap contextMap)
	{
		validate(contextMap, Objects::isNull, () -> new ValidationException(PARAMETER_NULL, CONTEXT_MAP));

		ResultMap resultMap = new ResultMap();
		for (Map.Entry<String, Object> entry : contextMap.entrySet())
		{
			ProcessorType processorType = ProcessorType.matchType(entry.getValue());
			MacroProcessor macroProcessor = processorRegistry.get(processorType);
			resultMap.putAll(macroProcessor.resolveContext(entry.getKey(), contextMap));
		}

		return resultMap;
	}


	/**
	 * Replace values in the message string with macro string values in replacementMap
	 *
	 * @param replacementMap a collection of key/value pairs representing the placeholders and their replacement values
	 * @param messageString the message string containing placeholders to be replaced
	 * @return {@code String} the final string with all replacements performed
	 */
	String performReplacements(final ResultMap replacementMap, final String messageString)
	{
		validate(replacementMap, Objects::isNull, () -> new ValidationException(PARAMETER_NULL, REPLACEMENT_MAP));
		validate(messageString, Objects::isNull, () -> new ValidationException(PARAMETER_NULL, MESSAGE_STRING));
		validate(messageString, String::isBlank, () -> new ValidationException(PARAMETER_EMPTY, MESSAGE_STRING));

		String modifiedMessageString = messageString;

		for (Map.Entry<String, String> entry : replacementMap.entrySet())
		{
			String macroToken = DELIMITER_OPEN + entry.getKey() + DELIMITER_CLOSE;
			modifiedMessageString = modifiedMessageString.replace(macroToken, entry.getValue());
		}

		return modifiedMessageString;
	}


	/**
	 * Static method to provide a compiled regex pattern matcher, matching message placeholders with delimiters
	 *
	 * @return a compiled regex pattern matcher
	 */
	static Matcher getMatcher(final String input)
	{
		String regex = "\\" + DELIMITER_OPEN + "([\\p{Lu}0-9_]+)" + DELIMITER_CLOSE;
		Pattern pattern = Pattern.compile(regex);
		return pattern.matcher(input);
	}

}
