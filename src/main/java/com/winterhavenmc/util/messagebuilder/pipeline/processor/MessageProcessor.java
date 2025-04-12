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

import com.winterhavenmc.util.messagebuilder.message.ValidMessage;
import com.winterhavenmc.util.messagebuilder.pipeline.cooldown.CooldownKey;
import com.winterhavenmc.util.messagebuilder.pipeline.cooldown.CooldownMap;
import com.winterhavenmc.util.messagebuilder.pipeline.replacer.MacroReplacer;
import com.winterhavenmc.util.messagebuilder.pipeline.retriever.MessageRetriever;
import com.winterhavenmc.util.messagebuilder.pipeline.sender.Sender;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.ValidMessageRecord;

import java.util.List;
import java.util.function.Predicate;


public final class MessageProcessor implements Processor
{
	private final MessageRetriever messageRetriever;
	private final MacroReplacer macroReplacer;
	private final Predicate<CooldownKey> notCooling;
	private final List<Sender> senders;


	public MessageProcessor(final MessageRetriever messageRetriever,
	                        final MacroReplacer macroReplacer,
	                        final CooldownMap cooldownMap,
	                        final List<Sender> senders)
	{
		this.messageRetriever = messageRetriever;
		this.macroReplacer = macroReplacer;
		this.senders = senders;
		this.notCooling = cooldownMap::notCooling;
	}


	@Override
	public void process(final ValidMessage message)
	{
		CooldownKey.of(message.getRecipient(), message.getMessageKey())
				.filter(notCooling)
				.map(cooldownKey -> messageRetriever.getRecord(message.getMessageKey()))
				.map(validMessageRecord -> macroReplacer.replaceMacros((ValidMessageRecord) validMessageRecord, message.getContextMap()))
				.ifPresent(processedMessage -> senders
						.forEach(sender -> sender
								.send(message.getRecipient(), processedMessage)));
	}

}
