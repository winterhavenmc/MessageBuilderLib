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

package com.winterhavenmc.library.messagebuilder.pipeline;

import com.winterhavenmc.library.messagebuilder.model.message.ValidMessage;
import com.winterhavenmc.library.messagebuilder.pipeline.cooldown.CooldownKey;
import com.winterhavenmc.library.messagebuilder.pipeline.cooldown.CooldownMap;
import com.winterhavenmc.library.messagebuilder.pipeline.processor.MessageProcessor;
import com.winterhavenmc.library.messagebuilder.pipeline.retriever.MessageRetriever;
import com.winterhavenmc.library.messagebuilder.pipeline.sender.Sender;
import com.winterhavenmc.library.messagebuilder.model.language.FinalMessageRecord;
import com.winterhavenmc.library.messagebuilder.model.language.ValidMessageRecord;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;


public final class MessagePipeline implements Pipeline
{
	private final MessageRetriever messageRetriever;
	private final MessageProcessor messageProcessor;
	private final CooldownMap cooldownMap;
	private final List<Sender> senders;


	public MessagePipeline(final MessageRetriever messageRetriever,
						   final MessageProcessor messageProcessor,
						   final CooldownMap cooldownMap,
						   final List<Sender> senders)
	{
		this.messageRetriever = messageRetriever;
		this.messageProcessor = messageProcessor;
		this.cooldownMap = cooldownMap;
		this.senders = senders;
	}


	@Override
	public void initiate(final ValidMessage message)
	{
		// queries CooldownMap, returns ValidMessageRecord
		Function<CooldownKey, Optional<ValidMessageRecord>> retrieveMessageRecord = key ->
				(messageRetriever.getRecord(message.getMessageKey()) instanceof ValidMessageRecord validMessageRecord)
						? Optional.of(validMessageRecord)
						: Optional.empty();

		// transforms ValidMessageRecord into FinalMessageRecord
		Function<ValidMessageRecord, FinalMessageRecord> processMessageRecord = messageRecord -> messageProcessor
				.process(messageRecord, message.getObjectMap());

		// consumes FinalMessageRecord
		Consumer<FinalMessageRecord> sendMessageRecord = processed -> senders
				.forEach(sender -> sender.send(message.getRecipient(), processed));


		// process message through pipeline
		CooldownKey.of(message.getRecipient(), message.getMessageKey())
				.filter(cooldownMap::notCooling)
				.flatMap(retrieveMessageRecord)
				.map(processMessageRecord)
				.ifPresent(sendMessageRecord);
	}

}
