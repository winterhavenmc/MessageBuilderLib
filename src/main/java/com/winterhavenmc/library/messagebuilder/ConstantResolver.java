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

import com.winterhavenmc.library.messagebuilder.keys.ConstantKey;
import com.winterhavenmc.library.messagebuilder.keys.ValidConstantKey;
import com.winterhavenmc.library.messagebuilder.model.language.ConstantRecord;
import com.winterhavenmc.library.messagebuilder.model.language.Section;
import com.winterhavenmc.library.messagebuilder.model.language.ValidConstantRecord;
import com.winterhavenmc.library.messagebuilder.query.QueryHandler;
import com.winterhavenmc.library.messagebuilder.query.QueryHandlerFactory;

import java.util.Optional;


/**
 * A class that allows clients of the library to access values in the constants section of the language file
 */
public class ConstantResolver
{
	private final QueryHandler<ConstantRecord> constantQueryHandler;


	/**
	 * Class constructor
	 *
	 * @param queryHandlerFactory an instance of the query handler factory
	 */
	ConstantResolver(final QueryHandlerFactory queryHandlerFactory)
	{
		this.constantQueryHandler = queryHandlerFactory.getQueryHandler(Section.CONSTANTS);
	}


	/**
	 * Retrieves a constant record using the constant query handler
	 *
	 * @param key a string to be used as the string for the record to be retrieved
	 * @return an Optional constant record, or an empty Optional if no record could be retrieved
	 */
	private Optional<ConstantRecord> getConstantRecord(final ValidConstantKey key)
	{
		return Optional.ofNullable(constantQueryHandler.getRecord(key));
	}


	/**
	 * Retrieves an Optional String from the constants section of the language file
	 *
	 * @param key a string to be used as the string for the String to be retrieved
	 * @return an Optional String, or an empty Optional if no String record could be retrieved
	 */
	public Optional<String> getString(final String key)
	{
		return (ConstantKey.of(key) instanceof ValidConstantKey validConstantKey)
				? this.getConstantRecord(validConstantKey).flatMap(this::extractStringValue)
				: Optional.empty();
	}


	/**
	 * Retrieves an Optional Integer from the constants section of the language file
	 *
	 * @param key a string to be used as the string for the Integer to be retrieved
	 * @return an Optional Integer, or an empty Optional if no Integer record could be retrieved
	 */
	public Optional<Integer> getInteger(final String key)
	{
		return (ConstantKey.of(key) instanceof ValidConstantKey validConstantKey)
				? this.getConstantRecord(validConstantKey).flatMap(this::extractIntegerValue)
				: Optional.empty();
	}


	/**
	 * Retrieves an Optional Boolean from the constants section of the language file
	 *
	 * @param key a string to be used as the string for the Boolean to be retrieved
	 * @return an Optional Boolean, or an empty Optional if no Boolean record could be retrieved
	 */
	public Optional<Boolean> getBoolean(final String key)
	{
		return (ConstantKey.of(key) instanceof ValidConstantKey validConstantKey)
				? this.getConstantRecord(validConstantKey).flatMap(this::extractBooleanValue)
				: Optional.empty();
	}


	/**
	 * Private helper method to extract a String value from a ConstantRecord
	 *
	 * @param record the ConstantRecord to extract a value
	 * @return an Optional String, or an empty Optional if a String value could not be extracted from the ConstantRecord
	 */
	private Optional<String> extractStringValue(final ConstantRecord record)
	{
		return (record instanceof ValidConstantRecord validRecord && validRecord.value() instanceof String string)
				? Optional.of(string)
				: Optional.empty();
	}


	/**
	 * Private helper method to extract an Integer value from a ConstantRecord
	 *
	 * @param record the ConstantRecord to extract a value
	 * @return an Optional Integer, or an empty Optional if an Integer value could not be extracted from the ConstantRecord
	 */
	private Optional<Integer> extractIntegerValue(final ConstantRecord record)
	{
		return (record instanceof ValidConstantRecord validRecord && validRecord.value() instanceof Integer integer)
				? Optional.of(integer)
				: Optional.empty();
	}


	/**
	 * Private helper method to extract a Boolean value from a ConstantRecord
	 *
	 * @param record the ConstantRecord to extract a value
	 * @return an Optional Boolean, or an empty Optional if a Boolean value could not be extracted from the ConstantRecord
	 */
	private Optional<Boolean> extractBooleanValue(final ConstantRecord record)
	{
		return (record instanceof ValidConstantRecord validRecord && validRecord.value() instanceof Boolean bool)
				? Optional.of(bool)
				: Optional.empty();
	}

}
