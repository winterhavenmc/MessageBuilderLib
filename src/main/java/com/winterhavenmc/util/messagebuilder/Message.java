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

package com.winterhavenmc.util.messagebuilder;

import com.winterhavenmc.util.messagebuilder.recipient.ValidRecipient;
import com.winterhavenmc.util.messagebuilder.resources.RecordKey;
import com.winterhavenmc.util.messagebuilder.pipeline.context.ContextMap;
import com.winterhavenmc.util.messagebuilder.pipeline.processor.MessageProcessor;
import com.winterhavenmc.util.messagebuilder.validation.LogLevel;
import com.winterhavenmc.util.messagebuilder.validation.Parameter;
import com.winterhavenmc.util.messagebuilder.validation.ValidationException;

import java.util.Objects;
import java.util.Optional;

import static com.winterhavenmc.util.messagebuilder.validation.ErrorMessageKey.PARAMETER_INVALID;
import static com.winterhavenmc.util.messagebuilder.validation.ErrorMessageKey.PARAMETER_NULL;
import static com.winterhavenmc.util.messagebuilder.validation.Parameter.*;
import static com.winterhavenmc.util.messagebuilder.validation.ValidationHandler.logging;
import static com.winterhavenmc.util.messagebuilder.validation.Validator.validate;


/**
 * The message object being built with builder pattern
 */
public class Message
{
	private final ValidRecipient recipient;
	private final RecordKey messageKey;
	private final MessageProcessor messageProcessor;
	private final ContextMap contextMap;


	/**
	 * Class constructor
	 *
	 * @param recipient message recipient
	 * @param messageKey message identifier
	 * @param messageProcessor the message processor that will receive the message when the send method is called
	 */
	public Message(final ValidRecipient recipient, final RecordKey messageKey, final MessageProcessor messageProcessor)
	{
		this.recipient = recipient;
		this.messageKey = messageKey;
		this.messageProcessor = messageProcessor;
		this.contextMap = ContextMap.of(this.recipient, this.messageKey)
				.orElseThrow(() -> new ValidationException(PARAMETER_INVALID, CONTEXT_MAP));
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
	public <K extends Enum<K>, V> Message setMacro(final K macro, final V value)
	{
		// throw exception on passed in null enum constant
		RecordKey macroKey = RecordKey.of(macro).orElseThrow(() -> new ValidationException(PARAMETER_NULL, MACRO));

		// allow null value to be inserted into context map. uncomment below line to throw on null value
		//Validator.validate(value, Objects::isNull, throwing(PARAMETER_NULL, VALUE));

		// insert value into context map using validated macro key
		this.contextMap.put(macroKey, value);

		// return this instance of Message class to the builder chain
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
	public <K extends Enum<K>, V> Message setMacro(int quantity, final K macro, final V value)
	{
		// throw exception on passed in null enum constant
		RecordKey macroKey = RecordKey.of(macro).orElseThrow(() -> new ValidationException(PARAMETER_NULL, MACRO));

		// unwrap quantityKey or throw exception if InvalidRecipient
		RecordKey quantityKey = RecordKey.of(macroKey + ".QUANTITY")
				.orElseThrow(() -> new ValidationException(PARAMETER_INVALID, Parameter.QUANTITY));

		// allow null 'value' parameter to be inserted into context map. uncomment below line to throw on null value
		//Validator.validate(value, Objects::isNull, throwing(PARAMETER_NULL, VALUE));

		// put value into context map using macro enum constant name for key
		this.contextMap.put(macroKey, value);
		this.contextMap.put(quantityKey, quantity);

		// return this instance of Message class to the builder chain
		return this;
	}


	/**
	 * Final step of message builder, performs replacements and sends message to recipient
	 */
	public void send()
	{
		messageProcessor.process(this);
	}


	/**
	 * Getter for recipient
	 *
	 * @return {@code ValidRecipientRecipient} the message recipient
	 */
	public ValidRecipient getRecipient()
	{
		return recipient;
	}


	/**
	 * Getter for key
	 *
	 * @return {@code String} the unique message identifier
	 */
	public String getMessageId()
	{
		return messageKey.toString();
	}


	public RecordKey getMessageKey()
	{
		return messageKey;
	}


	/**
	 * Getter for contextMap that contains macro key/value pairs for the message
	 *
	 * @return the context map for the message
	 */
	public ContextMap getContextMap()
	{
		return contextMap;
	}


	public boolean isEmpty() {
		return false;
	}


	/**
	 * Examine the contents of the context map for testing purposes
	 *
	 * @param macro the key to retrieve from the context map
	 * @param <K> type parameter for key
	 * @return {@code Object} the value stored in the map, or {@code null} if no value is present for key
	 */
	<K extends Enum<K>> Optional<Object> peek(K macro)
	{
		RecordKey macroKey = RecordKey.of(macro).orElseThrow(() -> new ValidationException(PARAMETER_INVALID, MACRO));

		return contextMap.get(macroKey);
	}


	/**
	 * Static method that returns a no-op stubbed message. This method is called when an InvalidRecipient parameter
	 * is passed in to the compose method.
	 *
	 * @return a no-op stubbed Message object
	 */
	public static Message empty()
	{
		return new Message(null, null, null)
		{
			@Override
			public <K extends Enum<K>, V> Message setMacro(K macro, V value)
			{
				return this; // no-op
			}

			@Override
			public void send()
			{
				// no-op method, log error message
				validate(null, Objects::isNull, logging(LogLevel.WARN, PARAMETER_NULL, RECIPIENT));
			}

			@Override
			public boolean isEmpty() {
				return true;
			}
		};
	}

}
