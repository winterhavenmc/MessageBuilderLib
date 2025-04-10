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

package com.winterhavenmc.util.messagebuilder.pipeline.processor;

import com.winterhavenmc.util.messagebuilder.ValidMessage;
import com.winterhavenmc.util.messagebuilder.pipeline.cooldown.CooldownKey;
import com.winterhavenmc.util.messagebuilder.pipeline.cooldown.CooldownMap;
import com.winterhavenmc.util.messagebuilder.pipeline.replacer.MacroReplacer;
import com.winterhavenmc.util.messagebuilder.pipeline.retriever.MessageRetriever;
import com.winterhavenmc.util.messagebuilder.pipeline.sender.MessageSender;
import com.winterhavenmc.util.messagebuilder.pipeline.sender.TitleSender;

import java.util.List;
import java.util.function.Predicate;


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
	public void process(final ValidMessage message)
	{
		CooldownKey.of(message.getRecipient(), message.getMessageKey())
				.filter(notCooling)
				.map(cooldownKey -> messageRetriever.getRecord(message.getMessageKey()))
				.map(messageRecord -> macroReplacer.replaceMacros(messageRecord, message.getContextMap()))
				.ifPresent(processedMessage -> List.of(messageSender, titleSender)
						.forEach(sender -> sender
								.send(message.getRecipient(), processedMessage.get())));
	}

}
