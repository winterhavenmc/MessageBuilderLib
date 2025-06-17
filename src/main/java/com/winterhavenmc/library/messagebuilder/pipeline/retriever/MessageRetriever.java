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

package com.winterhavenmc.library.messagebuilder.pipeline.retriever;

import com.winterhavenmc.library.messagebuilder.keys.RecordKey;
import com.winterhavenmc.library.messagebuilder.query.QueryHandler;
import com.winterhavenmc.library.messagebuilder.model.language.MessageRecord;
import com.winterhavenmc.library.messagebuilder.model.language.ValidMessageRecord;


/**
 * Default implementation of the {@link Retriever} interface that retrieves
 * a {@link com.winterhavenmc.library.messagebuilder.model.language.MessageRecord}
 * using a {@link com.winterhavenmc.library.messagebuilder.query.QueryHandler}.
 *
 * <p>This class ensures safety and consistency by always returning a non-null record.
 * If the underlying query handler fails to provide a valid message, a fallback
 * {@linkplain com.winterhavenmc.library.messagebuilder.model.language.MessageRecord#empty(RecordKey) empty record}
 * is returned instead.
 *
 * <p>This class is typically used as the entry point in a
 * {@link com.winterhavenmc.library.messagebuilder.pipeline.MessagePipeline MessagePipeline}.
 *
 * @see Retriever
 * @see com.winterhavenmc.library.messagebuilder.query.QueryHandler QueryHandler
 * @see com.winterhavenmc.library.messagebuilder.model.language.MessageRecord MessageRecord
 * @see com.winterhavenmc.library.messagebuilder.model.language.ValidMessageRecord ValidMessageRecord
 * @see com.winterhavenmc.library.messagebuilder.model.language.InvalidMessageRecord InvalidMessageRecord
 */
public final class MessageRetriever implements Retriever
{
	private final QueryHandler<MessageRecord> queryHandler;


	/**
	 * Constructs a {@code MessageRetriever} using the specified query handler.
	 *
	 * @param queryHandler a handler responsible for resolving message records from a configuration source
	 */
	public MessageRetriever(final QueryHandler<MessageRecord> queryHandler)
	{
		this.queryHandler = queryHandler;
	}


	/**
	 * Retrieves a {@link com.winterhavenmc.library.messagebuilder.model.language.MessageRecord}
	 * for the given key using the underlying {@link QueryHandler}.
	 *
	 * <p>If the result is not an instance of {@link com.winterhavenmc.library.messagebuilder.model.language.ValidMessageRecord},
	 * this method returns an {@linkplain com.winterhavenmc.library.messagebuilder.model.language.MessageRecord#empty(RecordKey)
	 * empty record} as a safe fallback.
	 *
	 * @param messageKey the key used to locate the message record
	 * @return a valid or empty message record; never {@code null}
	 */
	@Override
	public MessageRecord getRecord(final RecordKey messageKey)
	{
		return (queryHandler.getRecord(messageKey) instanceof ValidMessageRecord validMessageRecord)
				? validMessageRecord
				: MessageRecord.empty(messageKey);
	}

}
