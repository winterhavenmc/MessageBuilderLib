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

import com.winterhavenmc.library.messagebuilder.keys.ValidMessageKey;
import com.winterhavenmc.library.messagebuilder.model.language.MessageRecord;


/**
 * A functional interface representing the first stage of the message pipeline:
 * retrieving a {@link com.winterhavenmc.library.messagebuilder.model.language.MessageRecord}
 * associated with a given {@link ValidMessageKey}.
 *
 * <p>Implementations of this interface typically fetch records from a pre-parsed
 * data source such as a YAML-backed {@code ConfigurationSection}, returning either
 * a valid or invalid message record.
 *
 * <p>This interface abstracts retrieval behavior for flexibility and testability,
 * and is typically used by {@link com.winterhavenmc.library.messagebuilder.pipeline.MessagePipeline}.
 *
 * @see com.winterhavenmc.library.messagebuilder.model.language.MessageRecord MessageRecord
 * @see com.winterhavenmc.library.messagebuilder.pipeline.retriever.MessageRetriever MessageRetriever
 * @see com.winterhavenmc.library.messagebuilder.query.QueryHandler QueryHandler
 */
@FunctionalInterface
public interface Retriever
{
	MessageRecord getRecord(ValidMessageKey key);
}
