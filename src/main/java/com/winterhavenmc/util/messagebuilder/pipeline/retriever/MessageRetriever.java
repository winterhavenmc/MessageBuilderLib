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
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.MessageRecord;

import java.util.Objects;
import java.util.Optional;

import static com.winterhavenmc.util.messagebuilder.validation.ExceptionMessageKey.PARAMETER_NULL;
import static com.winterhavenmc.util.messagebuilder.validation.Parameter.QUERY_HANDLER;
import static com.winterhavenmc.util.messagebuilder.validation.ValidationHandler.throwing;
import static com.winterhavenmc.util.messagebuilder.validation.Validator.validate;


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
		validate(queryHandler, Objects::isNull, throwing(PARAMETER_NULL, QUERY_HANDLER));

		this.queryHandler = queryHandler;
	}


	@Override
	public Optional<MessageRecord> getRecord(final RecordKey key)
	{
		return queryHandler.getRecord(key);
	}

}
