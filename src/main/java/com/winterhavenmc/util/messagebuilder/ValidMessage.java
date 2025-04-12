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

package com.winterhavenmc.util.messagebuilder;

import com.winterhavenmc.util.messagebuilder.pipeline.context.ContextMap;
import com.winterhavenmc.util.messagebuilder.pipeline.processor.MessageProcessor;
import com.winterhavenmc.util.messagebuilder.recipient.ValidRecipient;
import com.winterhavenmc.util.messagebuilder.recordkey.ValidRecordKey;
import com.winterhavenmc.util.messagebuilder.validation.Parameter;
import com.winterhavenmc.util.messagebuilder.validation.ValidationException;

import static com.winterhavenmc.util.messagebuilder.validation.ErrorMessageKey.PARAMETER_INVALID;
import static com.winterhavenmc.util.messagebuilder.validation.ErrorMessageKey.PARAMETER_NULL;


public final class ValidMessage implements Message
{
	private final ValidRecipient recipient;
	private final ValidRecordKey messageKey;
	private final MessageProcessor messageProcessor;
	private final ContextMap contextMap;


	/**
	 * Class constructor
	 *
	 * @param recipient message recipient
	 * @param messageKey message identifier
	 * @param messageProcessor the message processor that will receive the message when the send method is called
	 */
	public ValidMessage(final ValidRecipient recipient,
						final ValidRecordKey messageKey,
						final MessageProcessor messageProcessor)
	{
		this.recipient = recipient;
		this.messageKey = messageKey;
		this.messageProcessor = messageProcessor;
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
	public <K extends Enum<K>, V> Message setMacro(K macro, V value)
	{
		ValidRecordKey macroKey = ValidRecordKey.of(macro).orElseThrow(() ->
				new ValidationException(PARAMETER_NULL, Parameter.MACRO));

		contextMap.put(macroKey, value);
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
	public <K extends Enum<K>, V> Message setMacro(int quantity, K macro, V value)
	{
		ValidRecordKey macroKey = ValidRecordKey.of(macro)
				.orElseThrow(() -> new ValidationException(PARAMETER_NULL, Parameter.MACRO));

		ValidRecordKey quantityKey = ValidRecordKey.of(macroKey + ".QUANTITY")
				.orElseThrow(() -> new ValidationException(PARAMETER_INVALID, Parameter.QUANTITY));

		contextMap.put(macroKey, value);
		contextMap.put(quantityKey, quantity);
		return this;
	}


	/**
	 * Final step of message builder, performs replacements and sends message to recipient
	 */
	@Override
	public void send()
	{
		messageProcessor.process(this);
	}


	/**
	 * Accessor method for message key
	 *
	 * @return {@code ValidRecordKey} the unique message identifier
	 */
	@Override
	public ValidRecordKey getMessageKey()
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
