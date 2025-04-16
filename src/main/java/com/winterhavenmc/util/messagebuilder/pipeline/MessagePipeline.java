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

import com.winterhavenmc.util.messagebuilder.message.ValidMessage;
import com.winterhavenmc.util.messagebuilder.pipeline.cooldown.CooldownKey;
import com.winterhavenmc.util.messagebuilder.pipeline.cooldown.CooldownMap;
import com.winterhavenmc.util.messagebuilder.pipeline.replacer.MacroReplacer;
import com.winterhavenmc.util.messagebuilder.pipeline.retriever.MessageRetriever;
import com.winterhavenmc.util.messagebuilder.pipeline.sender.Sender;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.FinalMessageRecord;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.MessageRecord;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.ValidMessageRecord;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;


public final class MessagePipeline implements Pipeline
{
	private final MessageRetriever messageRetriever;
	private final MacroReplacer macroReplacer;
	private final CooldownMap cooldownMap;
	private final List<Sender> senders;


	public MessagePipeline(final MessageRetriever messageRetriever,
						   final MacroReplacer macroReplacer,
						   final CooldownMap cooldownMap,
						   final List<Sender> senders)
	{
		this.messageRetriever = messageRetriever;
		this.macroReplacer = macroReplacer;
		this.cooldownMap = cooldownMap;
		this.senders = senders;
	}


	@Override
	public void process(final ValidMessage message)
	{
		Function<CooldownKey, Optional<ValidMessageRecord>> retrieveMessage =key ->
				{
					MessageRecord messageRecord = messageRetriever.getRecord(message.getMessageKey());
					return (messageRecord instanceof ValidMessageRecord valid)
							? Optional.of(valid)
							: Optional.empty();
				};

		Function<ValidMessageRecord, FinalMessageRecord> resolveMacros = messageRecord -> macroReplacer
				.replaceMacros(messageRecord, message.getContextMap());

		Consumer<FinalMessageRecord> sendMessage = processed -> senders
				.forEach(sender -> sender.send(message.getRecipient(), processed));


		CooldownKey.of(message.getRecipient(), message.getMessageKey())
				.filter(cooldownMap::notCooling)
				.flatMap(retrieveMessage)
				.map(resolveMacros)
				.ifPresent(sendMessage);
	}

}
