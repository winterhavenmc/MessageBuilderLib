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
import com.winterhavenmc.util.messagebuilder.resources.RecordKey;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.MessageRecord;
import com.winterhavenmc.util.messagebuilder.validation.ValidationException;

import org.bukkit.entity.Entity;

import java.util.Objects;
import java.util.Optional;

import static com.winterhavenmc.util.messagebuilder.validation.ExceptionMessageKey.PARAMETER_EMPTY;
import static com.winterhavenmc.util.messagebuilder.validation.ExceptionMessageKey.PARAMETER_NULL;
import static com.winterhavenmc.util.messagebuilder.validation.Parameter.*;
import static com.winterhavenmc.util.messagebuilder.validation.Validator.validate;


/**
 * This class provides handling of the Macro Processors and their Registry
 */
public class MacroReplacer implements Replacer
{
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
				replaceMacrosInString(message.getContextMap(), messageRecord.message()),
				replaceMacrosInString(message.getContextMap(), messageRecord.title()),
				replaceMacrosInString(message.getContextMap(), messageRecord.subtitle())
		);
	}


	/**
	 * Replace macros in a message to be sent
	 *
	 * @param contextMap    the context map containing other objects whose values may be retrieved
	 * @param messageString the message with placeholders to be replaced by macro values
	 * @return the string with all macro replacements performed
	 */
	String replaceMacrosInString(final ContextMap contextMap, final String messageString)
	{
		validate(contextMap, Objects::isNull, () -> new ValidationException(PARAMETER_NULL, CONTEXT_MAP));
		validate(messageString, Objects::isNull, () -> new ValidationException(PARAMETER_NULL, MESSAGE_STRING));
		validate(messageString, String::isBlank, () -> new ValidationException(PARAMETER_EMPTY, MESSAGE_STRING));

		return Optional.of(messageString)
				.map(msg -> performReplacements(new ContextResolver().resolve(addRecipientContext(contextMap)), msg))
				.orElse(messageString);
	}


	/**
	 * Add the recipient fields to the context map, including location fields if the recipient is a player
	 *
	 * @param contextMap a map containing key/value pairs of placeholder names and the value objects from which
	 *                   their replacement strings will be derived
	 * @return the updated context map, to allow use in functional pipelines
	 */
	ContextMap addRecipientContext(final ContextMap contextMap)
	{
		validate(contextMap, Objects::isNull, () -> new ValidationException(PARAMETER_NULL, CONTEXT_MAP));

		RecordKey recordKey = RecordKey.of("RECIPIENT").orElseThrow();
		RecordKey locationRecordKey = RecordKey.of("RECIPIENT.LOCATION").orElseThrow();

		contextMap.getRecipient().ifPresent(r -> contextMap.put(recordKey, r.getName()));
		contextMap.getRecipient()
				.filter(Entity.class::isInstance)
				.map(Entity.class::cast)
				.ifPresent(entity -> contextMap.put(locationRecordKey, entity.getLocation()));

		return contextMap;
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

		return new PlaceholderExtractor().extract(messageString)
				.reduce(messageString, (msg, placeholder) ->
						msg.replace( Delimiter.OPEN + placeholder + Delimiter.CLOSE,
								replacementMap.getValueOrKey(placeholder))
				);
	}

}
