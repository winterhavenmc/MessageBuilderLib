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
import com.winterhavenmc.library.messagebuilder.pipeline.maps.MacroObjectMap;
import com.winterhavenmc.library.messagebuilder.pipeline.MessagePipeline;
import com.winterhavenmc.library.messagebuilder.keys.RecordKey;
import com.winterhavenmc.library.messagebuilder.validation.LogLevel;
import com.winterhavenmc.library.messagebuilder.validation.Parameter;
import com.winterhavenmc.library.messagebuilder.pipeline.formatters.duration.BoundedDuration;
import com.winterhavenmc.library.messagebuilder.validation.ValidationException;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import static com.winterhavenmc.library.messagebuilder.validation.ErrorMessageKey.PARAMETER_INVALID;
import static com.winterhavenmc.library.messagebuilder.validation.ErrorMessageKey.PARAMETER_NULL;
import static com.winterhavenmc.library.messagebuilder.validation.Parameter.*;
import static com.winterhavenmc.library.messagebuilder.validation.Validator.logging;
import static com.winterhavenmc.library.messagebuilder.validation.Validator.throwing;
import static com.winterhavenmc.library.messagebuilder.validation.Validator.validate;


/**
 * A concrete implementation of {@link Message} representing a fully constructed,
 * resolvable, and sendable message.
 * <p>
 * This class encapsulates all components required to render and dispatch a message:
 * <ul>
 *   <li>A {@link com.winterhavenmc.library.messagebuilder.model.recipient.Recipient.Sendable recipient}</li>
 *   <li>A {@link com.winterhavenmc.library.messagebuilder.keys.RecordKey messageKey} identifying the template</li>
 *   <li>A {@link com.winterhavenmc.library.messagebuilder.pipeline.MessagePipeline} to handle rendering and delivery</li>
 *   <li>A {@link com.winterhavenmc.library.messagebuilder.pipeline.maps.MacroObjectMap macro object map}
 *       holding values to be substituted into the message</li>
 * </ul>
 * The recipient is also automatically added as a macro object under the key {@code %RECIPIENT%}.
 *
 * @see Message
 * @see com.winterhavenmc.library.messagebuilder.MessageBuilder MessageBuilder
 */
public final class ValidMessage implements Message
{
	private final static String RECIPIENT_KEY = "RECIPIENT";
	private final Recipient.Sendable recipient;
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
	public ValidMessage(final Recipient.Sendable recipient,
						final RecordKey messageKey,
						final MessagePipeline messagePipeline)
	{
		this.recipient = recipient;
		this.messageKey = messageKey;
		this.messagePipeline = messagePipeline;

		MacroKey recipientKey = MacroKey.of(RECIPIENT_KEY).orElseThrow();
		this.macroObjectMap = new MacroObjectMap();
		this.macroObjectMap.put(recipientKey, recipient);
	}


	@Override
	public <K extends Enum<K>, V> Message setMacro(final K macro,
												   final V value)
	{
		MacroKey macroKey = MacroKey.of(macro).orElseThrow(() -> new ValidationException(PARAMETER_INVALID, MACRO_KEY));

		macroObjectMap.putIfAbsent(macroKey, value);
		return this;
	}


	@Override
	public <K extends Enum<K>, V> Message setMacro(final int quantity,
												   final K macro,
												   final V value)
	{
		MacroKey macroKey = MacroKey.of(macro).orElseThrow(() -> new ValidationException(PARAMETER_INVALID, MACRO_KEY));
		MacroKey quantityKey = MacroKey.of(macroKey + ".QUANTITY").orElseThrow();

		macroObjectMap.putIfAbsent(macroKey, value);
		macroObjectMap.putIfAbsent(quantityKey, quantity);
		return this;
	}


	@Override
	public <K extends Enum<K>> Message setMacro(final K macro,
												final Duration duration,
												final ChronoUnit lowerBound)
	{
		validate(macro, Objects::isNull, throwing(PARAMETER_NULL, MACRO));
		Duration validDuration = validate(duration, Objects::isNull, logging(LogLevel.WARN, PARAMETER_NULL, DURATION)).orElse(Duration.ZERO);
		ChronoUnit validLowerBound = validate(lowerBound, Objects::isNull, logging(LogLevel.WARN, PARAMETER_NULL, Parameter.LOWER_BOUND)).orElse(ChronoUnit.MINUTES);

		MacroKey macroKey = MacroKey.of(macro).orElseThrow(() -> new ValidationException(PARAMETER_INVALID, MACRO_KEY));
		BoundedDuration boundedDuration = new BoundedDuration(validDuration, validLowerBound);

		macroObjectMap.putIfAbsent(macroKey, boundedDuration);
		return this;
	}


	@Override
	public void send()
	{
		messagePipeline.initiate(this);
	}


	@Override
	public RecordKey getMessageKey()
	{
		return messageKey;
	}


	@Override
	public Recipient.Sendable getRecipient()
	{
		return recipient;
	}


	@Override
	public MacroObjectMap getObjectMap()
	{
		return macroObjectMap;
	}

}
