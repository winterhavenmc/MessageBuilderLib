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

package com.winterhavenmc.util.messagebuilder.message;

import com.winterhavenmc.util.messagebuilder.keys.MacroKey;
import com.winterhavenmc.util.messagebuilder.pipeline.context.ContextMap;
import com.winterhavenmc.util.messagebuilder.pipeline.MessagePipeline;
import com.winterhavenmc.util.messagebuilder.recipient.ValidRecipient;
import com.winterhavenmc.util.messagebuilder.keys.RecordKey;
import com.winterhavenmc.util.messagebuilder.validation.Parameter;
import com.winterhavenmc.util.messagebuilder.validation.ValidationException;
import com.winterhavenmc.util.time.BoundedDuration;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import static com.winterhavenmc.util.messagebuilder.validation.ErrorMessageKey.PARAMETER_INVALID;
import static com.winterhavenmc.util.messagebuilder.validation.ErrorMessageKey.PARAMETER_NULL;


public final class ValidMessage implements Message
{
	private final ValidRecipient recipient;
	private final RecordKey messageKey;
	private final MessagePipeline messagePipeline;
	private final ContextMap contextMap;


	/**
	 * Class constructor
	 *
	 * @param recipient message recipient
	 * @param messageKey message identifier
	 * @param messagePipeline the message processor that will receive the message when the send method is called
	 */
	public ValidMessage(final ValidRecipient recipient,
						final RecordKey messageKey,
						final MessagePipeline messagePipeline)
	{
		this.recipient = recipient;
		this.messageKey = messageKey;
		this.messagePipeline = messagePipeline;
		this.contextMap = ContextMap.of(this.recipient, this.messageKey)
				.orElseThrow(() -> new ValidationException(PARAMETER_INVALID, Parameter.CONTEXT_MAP));
	}


	/**
	 * set macro for message replacements
	 *
	 * @param macro token for placeholder
	 * @param value object that contains value that will be substituted in message
	 * @param <K> type parameter for key
	 * @param <V> type parameter for value
	 * @return this message object with macro value set in map
	 */
	@Override
	public <K extends Enum<K>, V> Message setMacro(final K macro,
												   final V value)
	{
		MacroKey macroKey = MacroKey.of(macro).orElseThrow(() ->
				new ValidationException(PARAMETER_NULL, Parameter.MACRO));

		contextMap.putIfAbsent(macroKey, value);
		return this;
	}


	/**
	 * set macro for message replacements, with a corresponding quantity
	 *
	 * @param quantity an integer representing a quantity associated with the macro value
	 * @param macro token for placeholder
	 * @param value object that contains value that will be substituted in message
	 * @param <K> type parameter for key
	 * @param <V> type parameter for value
	 * @return this message object with macro value set in map
	 */
	@Override
	public <K extends Enum<K>, V> Message setMacro(final int quantity,
												   final K macro,
												   final V value)
	{
		MacroKey macroKey = MacroKey.of(macro)
				.orElseThrow(() -> new ValidationException(PARAMETER_NULL, Parameter.MACRO));

		MacroKey quantityKey = MacroKey.of(macroKey + ".QUANTITY")
				.orElseThrow(() -> new ValidationException(PARAMETER_INVALID, Parameter.QUANTITY));

		contextMap.putIfAbsent(macroKey, value);
		contextMap.putIfAbsent(quantityKey, quantity);
		return this;
	}


	public <K extends Enum<K>> Message setMacro(final K macro,
												final Duration duration,
												final ChronoUnit precision)
	{
		return setMacro(macro, new BoundedDuration(duration, precision));
	}


	/**
	 * Final step of message builder, performs replacements and sends message to recipient
	 */
	@Override
	public void send()
	{
		messagePipeline.process(this);
	}


	/**
	 * Accessor method for message key
	 *
	 * @return {@code RecordKey} the unique message identifier
	 */
	@Override
	public RecordKey getMessageKey()
	{
		return messageKey;
	}


	/**
	 * Accessor method for recipient
	 *
	 * @return {@code ValidRecipient} the message recipient
	 */
	@Override
	public ValidRecipient getRecipient()
	{
		return recipient;
	}


	/**
	 * Accessor method for contextMap that contains macro key/value pairs for the message
	 *
	 * @return the context map for the message
	 */
	@Override
	public ContextMap getContextMap()
	{
		return contextMap;
	}

}
