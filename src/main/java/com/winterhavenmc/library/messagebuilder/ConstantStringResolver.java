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

package com.winterhavenmc.library.messagebuilder;

import com.winterhavenmc.library.messagebuilder.keys.RecordKey;
import com.winterhavenmc.library.messagebuilder.model.language.ConstantRecord;
import com.winterhavenmc.library.messagebuilder.model.language.ValidConstantRecord;
import com.winterhavenmc.library.messagebuilder.query.QueryHandler;

import java.util.Optional;

public class ConstantStringResolver
{
	private final QueryHandler<ConstantRecord> constantQueryHandler;


	ConstantStringResolver(QueryHandler<ConstantRecord> constantQueryHandler)
	{
		this.constantQueryHandler = constantQueryHandler;
	}


	public Optional<String> getConstantString(final String key)
	{
		return RecordKey.of(key)
				.flatMap(this::getConstantRecord)
				.flatMap(this::extractStringValue);
	}


	private Optional<ConstantRecord> getConstantRecord(RecordKey key)
	{
		return Optional.ofNullable(constantQueryHandler.getRecord(key));
	}


	private Optional<String> extractStringValue(ConstantRecord record)
	{
		return (record instanceof ValidConstantRecord validRecord && validRecord.value() instanceof String string)
				? Optional.of(string)
				: Optional.empty();
	}

}
