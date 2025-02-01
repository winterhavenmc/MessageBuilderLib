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

import com.winterhavenmc.util.messagebuilder.context.ContextMap;
import com.winterhavenmc.util.messagebuilder.pipeline.*;

import com.winterhavenmc.util.messagebuilder.util.LocalizedException;
import org.bukkit.command.CommandSender;

import static com.winterhavenmc.util.messagebuilder.util.LocalizedException.MessageKey.PARAMETER_NULL;
import static com.winterhavenmc.util.messagebuilder.util.LocalizedException.Parameter.*;


/**
 * The message object being built with builder pattern
 *
 * @param <Macro> a macro placeholder value to be added to the context map
 */
public final class Message<Macro extends Enum<Macro>> {

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
	 * @return this message object with macro value set in map
	 */
	public <T> Message<Macro> setMacro(final Macro macro, final T value)
	{
		if (macro == null) { throw new LocalizedException(PARAMETER_NULL, MACRO); }
		if (value == null) { throw new LocalizedException(PARAMETER_NULL, VALUE); }

		// use macro enum constant name as key
		String key = macro.name();

		// put value into context map
		this.contextMap.put(key, value);

		// return this instance of Message class to the builder chain
		return this;
	}


	/**
	 * set macro for message replacements
	 *
	 * @param macro token for placeholder
	 * @param value object that contains value that will be substituted in message
	 * @return this message object with macro value set in map
	 */
	public <T> Message<Macro> setMacro(int quantity, final Macro macro, final T value)
	{
		if (macro == null) { throw new LocalizedException(PARAMETER_NULL, MACRO); }
		if (value == null) { throw new LocalizedException(PARAMETER_NULL, VALUE); }

		// use macro enum constant name as key
		String key = macro.name();

		// put value into context map
		this.contextMap.put(key, value);
		this.contextMap.put(key + ".QUANTITY", quantity);

		// return this instance of Message class to the builder chain
		return this;
	}


	/**
	 * Final step of message builder, performs replacements and sends message to recipient
	 */
	public void send() {
		messageProcessor.process(this);
	}


	/**
	 * Examine the contents of the context map for testing purposes
	 *
	 * @param macro the key to retrieve from the context map
	 * @return {@code Object} the value stored in the map, or {@code null} if no value is present for key
	 */
	Object peek(Macro macro) {
		return contextMap.get(macro.name());
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

}
