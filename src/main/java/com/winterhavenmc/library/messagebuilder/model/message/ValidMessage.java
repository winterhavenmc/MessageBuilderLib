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

package com.winterhavenmc.library.messagebuilder.model.message;

import com.winterhavenmc.library.messagebuilder.keys.MacroKey;
import com.winterhavenmc.library.messagebuilder.model.recipient.Recipient;
import com.winterhavenmc.library.messagebuilder.pipeline.containers.MacroObjectMap;
import com.winterhavenmc.library.messagebuilder.pipeline.MessagePipeline;
import com.winterhavenmc.library.messagebuilder.keys.RecordKey;
import com.winterhavenmc.library.messagebuilder.validation.Parameter;
import com.winterhavenmc.library.messagebuilder.pipeline.formatters.duration.BoundedDuration;
import com.winterhavenmc.library.messagebuilder.validation.ValidationException;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import static com.winterhavenmc.library.messagebuilder.validation.ErrorMessageKey.PARAMETER_INVALID;
import static com.winterhavenmc.library.messagebuilder.validation.ErrorMessageKey.PARAMETER_NULL;
import static com.winterhavenmc.library.messagebuilder.validation.Parameter.*;
import static com.winterhavenmc.library.messagebuilder.validation.ValidationHandler.throwing;
import static com.winterhavenmc.library.messagebuilder.validation.Validator.validate;


public final class ValidMessage implements Message
{
	private final static String RECIPIENT_KEY = "RECIPIENT";
	private final Recipient.Valid recipient;
	private final RecordKey messageKey;
	private final MessagePipeline messagePipeline;
	private final MacroObjectMap macroObjectMap;


	/**
	 * Class constructor
	 *
	 * @param recipient message recipient
	 * @param messageKey message identifier
	 * @param messagePipeline the message processor that will receive the message when the send method is called
	 */
	public ValidMessage(final Recipient.Valid recipient,
						final RecordKey messageKey,
						final MessagePipeline messagePipeline)
	{
		this.recipient = recipient;
		this.messageKey = messageKey;
		this.messagePipeline = messagePipeline;

		// create macro object map and add recipient field
		this.macroObjectMap = new MacroObjectMap();
		MacroKey recipientKey = MacroKey.of(RECIPIENT_KEY).orElseThrow(() -> new ValidationException(PARAMETER_INVALID, MACRO_KEY));
		this.macroObjectMap.put(recipientKey, recipient);
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
		MacroKey macroKey = MacroKey.of(macro).orElseThrow(() -> new ValidationException(PARAMETER_INVALID, MACRO_KEY));

		macroObjectMap.putIfAbsent(macroKey, value);
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
	 * @return this message object with macro value set in object map
	 */
	@Override
	public <K extends Enum<K>, V> Message setMacro(final int quantity,
												   final K macro,
												   final V value)
	{
		MacroKey macroKey = MacroKey.of(macro).orElseThrow(() -> new ValidationException(PARAMETER_INVALID, MACRO_KEY));
		MacroKey quantityKey = MacroKey.of(macroKey + ".QUANTITY").orElseThrow(() -> new ValidationException(PARAMETER_INVALID, QUANTITY));

		macroObjectMap.putIfAbsent(macroKey, value);
		macroObjectMap.putIfAbsent(quantityKey, quantity);
		return this;
	}


	/**
	 * set macro for message replacements, with a duration and lower bound
	 *
	 * @param macro token for placeholder
	 * @param duration a duration
	 * @param lowerBound a lower bound, represented by a ChronoUnit
	 * @param <K> type parameter for enum derived macro key
	 * @return this message object with macro value set in object map
	 */
	@Override
	public <K extends Enum<K>> Message setMacro(final K macro,
												final Duration duration,
												final ChronoUnit lowerBound)
	{
		validate(macro, Objects::isNull, throwing(PARAMETER_NULL, MACRO));
		validate(duration, Objects::isNull, throwing(PARAMETER_NULL, DURATION));
		validate(lowerBound, Objects::isNull, throwing(PARAMETER_NULL, Parameter.LOWER_BOUND));

		return setMacro(macro, new BoundedDuration(duration, lowerBound));
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
	 * @return {@code Valid} the message recipient
	 */
	@Override
	public Recipient.Valid getRecipient()
	{
		return recipient;
	}


	/**
	 * Accessor method for macroObjectMap that contains macro key/value pairs for the message
	 *
	 * @return the context map for the message
	 */
	@Override
	public MacroObjectMap getObjectMap()
	{
		return macroObjectMap;
	}

}
