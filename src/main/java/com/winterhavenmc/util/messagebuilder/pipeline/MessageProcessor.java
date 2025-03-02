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

package com.winterhavenmc.util.messagebuilder.pipeline;

import com.winterhavenmc.util.messagebuilder.Message;
import com.winterhavenmc.util.messagebuilder.validation.ValidationException;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import static com.winterhavenmc.util.messagebuilder.validation.MessageKey.PARAMETER_NULL;
import static com.winterhavenmc.util.messagebuilder.validation.Parameter.MESSAGE;
import static com.winterhavenmc.util.messagebuilder.validation.Validate.validate;


public final class MessageProcessor implements Processor
{
	private final MessageRetriever messageRetriever;
	private final MacroReplacer macroReplacer;
	private final MessageSender messageSender;
	private final TitleSender titleSender;
	private final Predicate<CooldownKey> notCooling;


	public MessageProcessor(final MessageRetriever messageRetriever,
	                        final MacroReplacer macroReplacer,
	                        final CooldownMap cooldownMap,
	                        final MessageSender messageSender,
	                        final TitleSender titleSender)
	{
		this.messageRetriever = messageRetriever;
		this.macroReplacer = macroReplacer;
		this.messageSender = messageSender;
		this.titleSender = titleSender;
		this.notCooling = cooldownMap::notCooling;
	}


	@Override
	public void process(final Message message)
	{
		validate(message, Objects::isNull, () -> new ValidationException(PARAMETER_NULL, MESSAGE));

		List<Sender> senders = List.of(messageSender, titleSender);

		CooldownKey.optional(message.getRecipient(), message.getMessageId())
				.filter(notCooling)
				.flatMap(key ->
						messageRetriever.getRecord(key.getMessageId()))
				.flatMap(messageRecord ->
						macroReplacer.replaceMacros(messageRecord, message))
				.ifPresent(processedMessage ->
						senders.forEach(sender ->
								sender.send(message.getRecipient(), processedMessage)));
	}

}
