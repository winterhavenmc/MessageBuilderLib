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

package com.winterhavenmc.util.messagebuilder.pipeline.retriever;

import com.winterhavenmc.util.messagebuilder.resources.QueryHandler;
import com.winterhavenmc.util.messagebuilder.resources.RecordKey;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.*;


public final class MessageRetriever implements Retriever
{
	private final QueryHandler<MessageRecord> queryHandler;


	/**
	 * Class constructor
	 *
	 * @param queryHandler the query handler to be used to retrieve a message record
	 */
	public MessageRetriever(final QueryHandler<MessageRecord> queryHandler)
	{
		this.queryHandler = queryHandler;
	}


	@Override
	public MessageRecord getRecord(final RecordKey key)
	{
		MessageRecord result = queryHandler.getRecord(key);

		return switch (result)
		{
			case ValidMessageRecord validMessageRecord -> validMessageRecord;
			case InvalidMessageRecord ignored -> MessageRecord.empty();
			case FinalMessageRecord ignored -> MessageRecord.empty();
		};

	}

}
