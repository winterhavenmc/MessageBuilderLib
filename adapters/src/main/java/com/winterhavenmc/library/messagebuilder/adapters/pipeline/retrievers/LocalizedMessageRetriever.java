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

package com.winterhavenmc.library.messagebuilder.adapters.pipeline.retrievers;

import com.winterhavenmc.library.messagebuilder.core.ports.resourcemanagers.language.MessageRepository;
import com.winterhavenmc.library.messagebuilder.core.ports.retrievers.MessageRetriever;
import com.winterhavenmc.library.messagebuilder.core.pipeline.MessagePipeline;

import com.winterhavenmc.library.messagebuilder.models.keys.ValidMessageKey;
import com.winterhavenmc.library.messagebuilder.models.language.InvalidRecordReason;
import com.winterhavenmc.library.messagebuilder.models.language.MessageRecord;
import com.winterhavenmc.library.messagebuilder.models.language.ValidMessageRecord;


/**
 * Default implementation of the {@link MessageRetriever} interface that retrieves
 * a {@link com.winterhavenmc.library.messagebuilder.models.language.MessageRecord}
 *
 * <p>This class ensures safety and consistency by always returning a non-null record.
 * If the underlying query handler fails to provide a valid message, a fallback
 * {@code MessageRecord#empty(com.winterhavenmc.library.messagebuilder.models.keys.RecordKey, InvalidRecordReason) empty record}
 * is returned instead.
 *
 * <p>This class is typically used as the entry point in a
 * {@link MessagePipeline MessagePipeline}.
 *
 * @see MessageRetriever
 * @see com.winterhavenmc.library.messagebuilder.models.language.MessageRecord MessageRecord
 * @see com.winterhavenmc.library.messagebuilder.models.language.ValidMessageRecord ValidMessageRecord
 * @see com.winterhavenmc.library.messagebuilder.models.language.InvalidMessageRecord InvalidMessageRecord
 */
public final class LocalizedMessageRetriever implements MessageRetriever
{
	private final MessageRepository messageRepository;


	/**
	 * Constructs a {@code LocalizedMessageRetriever} using the specified query handler.
	 */
	public LocalizedMessageRetriever(final MessageRepository messageRepository)
	{
		this.messageRepository = messageRepository;
	}


	/**
	 * Retrieves a {@link com.winterhavenmc.library.messagebuilder.models.language.MessageRecord}
	 *
	 * <p>If the result is not an instance of {@link com.winterhavenmc.library.messagebuilder.models.language.ValidMessageRecord},
	 * this method returns an {@code MessageRecord#empty(com.winterhavenmc.library.messagebuilder.models.keys.RecordKey, InvalidRecordReason)
	 * empty record} as a safe fallback.
	 *
	 * @param messageKey the string used to locate the message record
	 * @return a valid or empty message record; never {@code null}
	 */
	@Override
	public MessageRecord getRecord(final ValidMessageKey messageKey)
	{
		return (messageRepository.getMessageRecord(messageKey) instanceof ValidMessageRecord validMessageRecord)
				? validMessageRecord
				: MessageRecord.empty(messageKey, InvalidRecordReason.MESSAGE_ENTRY_MISSING);
	}

}
