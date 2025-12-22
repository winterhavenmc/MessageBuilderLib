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

import com.winterhavenmc.library.messagebuilder.adapters.pipeline.cooldown.MessageCooldownMap;
import com.winterhavenmc.library.messagebuilder.adapters.pipeline.processors.MessageProcessor;
import com.winterhavenmc.library.messagebuilder.adapters.pipeline.retrievers.LocalizedMessageRetriever;
import com.winterhavenmc.library.messagebuilder.adapters.pipeline.senders.SenderFactory;
import com.winterhavenmc.library.messagebuilder.core.context.AccessorCtx;
import com.winterhavenmc.library.messagebuilder.core.context.FormatterCtx;
import com.winterhavenmc.library.messagebuilder.core.context.MessagePipelineCtx;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.Pipeline;

import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.senders.Sender;
import com.winterhavenmc.library.messagebuilder.core.ports.resources.language.MessageRepository;
import com.winterhavenmc.library.messagebuilder.core.ports.resources.sound.SoundRepository;
import com.winterhavenmc.library.messagebuilder.models.keys.CooldownKey;
import com.winterhavenmc.library.messagebuilder.models.language.message.FinalMessageRecord;
import com.winterhavenmc.library.messagebuilder.models.language.message.ValidMessageRecord;

import com.winterhavenmc.library.messagebuilder.core.message.ValidMessage;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;


public final class MessagePipeline implements Pipeline
{
	private final MessagePipelineCtx ctx;
	private final MiniMessage miniMessage;


	public MessagePipeline(final MessagePipelineCtx ctx)
	{
		this.ctx = ctx;
		this.miniMessage = ctx.miniMessage();
	}

	/**
	 * A static factory method to create the message processing pipeline
	 *
	 * @param formatterCtx a context container which contains instances of string formatters for specific types
	 * @param accessorCtx a context container for injecting dependencies into adapters
	 * @return an instance of the message pipeline
	 */
	public static @NotNull MessagePipeline createMessagePipeline(final Plugin plugin,
																 final MessageRepository messages,
																 final SoundRepository sounds,
																 final FormatterCtx formatterCtx,
																 final AccessorCtx accessorCtx)
	{
		final LocalizedMessageRetriever localizedMessageRetriever = new LocalizedMessageRetriever(messages);
		final MessageProcessor messageProcessor = MessageProcessor.create(formatterCtx, accessorCtx);
		final MessageCooldownMap messageCooldownMap = new MessageCooldownMap();
		final List<Sender> messageSenders = SenderFactory.createSenders(plugin, messageCooldownMap, sounds);

		final MessagePipelineCtx pipelineCtx = new MessagePipelineCtx(localizedMessageRetriever, messageProcessor,
				messageCooldownMap, formatterCtx.miniMessage(), messageSenders);
		return new MessagePipeline(pipelineCtx);
	}


	/**
	 * A static factory method to create the message processing pipeline
	 *
	 * @param formatterCtx a context container which contains instances of string formatters for specific types
	 * @param accessorCtx a context container for injecting dependencies into adapters
	 * @return an instance of the message pipeline
	 */
	static @NotNull MessagePipeline createComponentPipeline(final Plugin plugin,
															final MessageRepository messages,
															final SoundRepository sounds,
															final FormatterCtx formatterCtx,
															final AccessorCtx accessorCtx)
	{
		final LocalizedMessageRetriever localizedMessageRetriever = new LocalizedMessageRetriever(messages);
		final MessageProcessor messageProcessor = MessageProcessor.create(formatterCtx, accessorCtx);
		final MessageCooldownMap messageCooldownMap = new MessageCooldownMap();
		final List<Sender> messageSenders = SenderFactory.createSenders(plugin, messageCooldownMap, sounds);

		final MessagePipelineCtx pipelineCtx = new MessagePipelineCtx(localizedMessageRetriever, messageProcessor,
				messageCooldownMap, formatterCtx.miniMessage(), messageSenders);
		return new MessagePipeline(pipelineCtx);
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


	public Optional<Component> retrieve(final ValidMessage message)
	{
		// queries CooldownMap, returns ValidMessageRecord
		Function<CooldownKey, Optional<ValidMessageRecord>> retrieveMessageRecord = key ->
				(ctx.messageRetriever().getRecord(message.getMessageKey()) instanceof ValidMessageRecord validMessageRecord)
						? Optional.of(validMessageRecord)
						: Optional.empty();

		// transforms ValidMessageRecord into FinalMessageRecord
		Function<ValidMessageRecord, FinalMessageRecord> processMessageRecord =messageRecord -> ctx.messageProcessor()
				.process(messageRecord, message.getObjectMap());

		// process message through pipeline
		return CooldownKey.of(message.getRecipient(), message.getMessageKey())
				.filter(ctx.cooldownMap()::notCooling)
				.flatMap(retrieveMessageRecord)
				.map(processMessageRecord)
				.map(this::toComponent);
	}


	private Component toComponent(final FinalMessageRecord finalMessageRecord)
	{
		return (finalMessageRecord.finalMessageString().isPresent())
				? miniMessage.deserialize(finalMessageRecord.finalMessageString().get())
				: Component.empty();
	}

}
