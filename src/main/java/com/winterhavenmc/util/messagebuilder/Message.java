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

import com.winterhavenmc.util.messagebuilder.pipeline.ContextMap;
import com.winterhavenmc.util.messagebuilder.pipeline.*;

import com.winterhavenmc.util.messagebuilder.validation.ValidationException;
import org.bukkit.command.CommandSender;

import java.util.Objects;

import static com.winterhavenmc.util.messagebuilder.validation.MessageKey.PARAMETER_NULL;
import static com.winterhavenmc.util.messagebuilder.validation.Parameter.MACRO;
import static com.winterhavenmc.util.messagebuilder.validation.Parameter.VALUE;
import static com.winterhavenmc.util.messagebuilder.validation.Validate.validate;
import static com.winterhavenmc.util.messagebuilder.validation.Validator.staticValidate;


/**
 * The message object being built with builder pattern
 */
public final class Message
{
	private final CommandSender recipient;
	private final String messageId;
	private final MessageProcessor messageProcessor;
	private final ContextMap contextMap;


	/**
	 * Class constructor
	 *
	 * @param recipient message recipient
	 * @param messageId message identifier
	 * @param messageProcessor the message processor that will receive the message when the send method is called
	 */
	public Message(final CommandSender recipient, final String messageId, final MessageProcessor messageProcessor)
	{
		this.recipient = recipient;
		this.messageId = messageId;
		this.messageProcessor = messageProcessor;
		this.contextMap = new ContextMap(recipient, messageId);
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
		validate(macro, Objects::isNull, () -> new ValidationException(PARAMETER_NULL, MACRO));
		staticValidate(value, Objects::isNull, () -> new ValidationException(PARAMETER_NULL, VALUE));
		// allow null 'value' parameter to be inserted into context map

		// put value into context map using macro enum constant name as key
		this.contextMap.put(macro.name(), value);

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
		validate(macro, Objects::isNull, () -> new ValidationException(PARAMETER_NULL, MACRO));
		staticValidate(value, Objects::isNull, () -> new ValidationException(PARAMETER_NULL, VALUE));
		// allow null 'value' parameter to be inserted into context map

		// put value into context map using macro enum constant name for key
		this.contextMap.put(macro.name(), value);
		this.contextMap.put(macro.name() + ".QUANTITY", quantity);

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
	 * @return {@code CommandSender} the message recipient
	 */
	public CommandSender getRecipient()
	{
		return recipient;
	}


	/**
	 * Getter for messageId
	 *
	 * @return {@code String} the unique message identifier
	 */
	public String getMessageId()
	{
		return messageId;
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


	/**
	 * Examine the contents of the context map for testing purposes
	 *
	 * @param macro the key to retrieve from the context map
	 * @param <K> type parameter for key
	 * @return {@code Object} the value stored in the map, or {@code null} if no value is present for key
	 */
	<K extends Enum<K>> Object peek(K macro)
	{
		return contextMap.get(macro.name());
	}

}
