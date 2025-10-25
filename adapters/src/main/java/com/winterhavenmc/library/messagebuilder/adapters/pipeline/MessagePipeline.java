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

package com.winterhavenmc.library.messagebuilder.adapters.pipeline;

import com.winterhavenmc.library.messagebuilder.core.context.MessagePipelineCtx;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.Pipeline;

import com.winterhavenmc.library.messagebuilder.models.keys.CooldownKey;
import com.winterhavenmc.library.messagebuilder.models.language.FinalMessageRecord;
import com.winterhavenmc.library.messagebuilder.models.language.ValidMessageRecord;

import com.winterhavenmc.library.messagebuilder.core.message.ValidMessage;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;


public final class MessagePipeline implements Pipeline
{
	private final MessagePipelineCtx ctx;


	public MessagePipeline(final MessagePipelineCtx ctx)
	{
		this.ctx = ctx;
	}


	@Override
	public void initiate(final ValidMessage message)
	{
		// queries CooldownMap, returns ValidMessageRecord
		Function<CooldownKey, Optional<ValidMessageRecord>> retrieveMessageRecord = key ->
				(ctx.messageRetriever().getRecord(message.getMessageKey()) instanceof ValidMessageRecord validMessageRecord)
						? Optional.of(validMessageRecord)
						: Optional.empty();

		// transforms ValidMessageRecord into FinalMessageRecord
		Function<ValidMessageRecord, FinalMessageRecord> processMessageRecord =messageRecord -> ctx.messageProcessor()
				.process(messageRecord, message.getObjectMap());

		// consumes FinalMessageRecord
		Consumer<FinalMessageRecord> sendMessageRecord = processed -> ctx.senders()
				.forEach(sender -> sender.send(message.getRecipient(), processed));


		// process message through pipeline
		CooldownKey.of(message.getRecipient(), message.getMessageKey())
				.filter(ctx.cooldownMap()::notCooling)
				.flatMap(retrieveMessageRecord)
				.map(processMessageRecord)
				.ifPresent(sendMessageRecord);
	}

}
