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
import com.winterhavenmc.library.messagebuilder.model.language.InvalidConstantRecord;
import com.winterhavenmc.library.messagebuilder.model.language.Section;
import com.winterhavenmc.library.messagebuilder.query.QueryHandler;
import com.winterhavenmc.library.messagebuilder.query.QueryHandlerFactory;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class ConstantResolverTest
{
	@Mock QueryHandlerFactory queryHandlerFactoryMock;
	@Mock QueryHandler<ConstantRecord> constantQueryHandlerMock;


	@Nested
	class GetStringTests
	{
		@Test
		void getString_returns_optional_String()
		{
			// Arrange
			RecordKey recordKey = RecordKey.of("KEY").orElseThrow();
			ConstantRecord constantRecord = ConstantRecord.from(recordKey, "result string");

			doReturn(constantQueryHandlerMock).when(queryHandlerFactoryMock).getQueryHandler(Section.CONSTANTS);
			when(constantQueryHandlerMock.getRecord(recordKey)).thenReturn(constantRecord);

			ConstantResolver constantResolver = new ConstantResolver(queryHandlerFactoryMock);

			// Act
			Optional<String> result = constantResolver.getString("KEY");

			// Assert
			assertEquals(Optional.of("result string"), result);
		}


		@Test
		void getString_returns_empty_optional()
		{
			// Arrange
			RecordKey recordKey = RecordKey.of("KEY").orElseThrow();
			ConstantRecord constantRecord = ConstantRecord.from(recordKey, Optional.empty());

			doReturn(constantQueryHandlerMock).when(queryHandlerFactoryMock).getQueryHandler(Section.CONSTANTS);
			when(constantQueryHandlerMock.getRecord(recordKey)).thenReturn(constantRecord);

			ConstantResolver constantResolver = new ConstantResolver(queryHandlerFactoryMock);

			// Act
			Optional<String> result = constantResolver.getString("KEY");

			// Assert
			assertEquals(Optional.empty(), result);
		}


		@Test
		void getString_returns_empty_optional_when_non_string_value_record()
		{
			// Arrange
			RecordKey recordKey = RecordKey.of("KEY").orElseThrow();
			ConstantRecord constantRecord = ConstantRecord.from(recordKey, 42);

			doReturn(constantQueryHandlerMock).when(queryHandlerFactoryMock).getQueryHandler(Section.CONSTANTS);
			when(constantQueryHandlerMock.getRecord(recordKey)).thenReturn(constantRecord);

			ConstantResolver constantResolver = new ConstantResolver(queryHandlerFactoryMock);

			// Act
			Optional<String> result = constantResolver.getString("KEY");

			// Assert
			assertEquals(Optional.empty(), result);
		}
	}


	@Nested
	class GetIntegerTests
	{
		@Test
		void getInteger_returns_optional_Integer()
		{
			// Arrange
			RecordKey recordKey = RecordKey.of("KEY").orElseThrow();
			ConstantRecord constantRecord = ConstantRecord.from(recordKey, 42);

			doReturn(constantQueryHandlerMock).when(queryHandlerFactoryMock).getQueryHandler(Section.CONSTANTS);
			when(constantQueryHandlerMock.getRecord(recordKey)).thenReturn(constantRecord);

			ConstantResolver constantResolver = new ConstantResolver(queryHandlerFactoryMock);

			// Act
			Optional<Integer> result = constantResolver.getInteger("KEY");

			// Assert
			assertEquals(Optional.of(42), result);
		}


		@Test
		void getInteger_returns_empty_optional()
		{
			// Arrange
			RecordKey recordKey = RecordKey.of("KEY").orElseThrow();
			ConstantRecord constantRecord = ConstantRecord.from(recordKey, Optional.empty());

			doReturn(constantQueryHandlerMock).when(queryHandlerFactoryMock).getQueryHandler(Section.CONSTANTS);
			when(constantQueryHandlerMock.getRecord(recordKey)).thenReturn(constantRecord);

			ConstantResolver constantResolver = new ConstantResolver(queryHandlerFactoryMock);

			// Act
			Optional<Integer> result = constantResolver.getInteger("KEY");

			// Assert
			assertEquals(Optional.empty(), result);
		}
	}


	@Nested
	class GetBooleanTests
	{
		@Test
		void getBoolean_returns_optional_Boolean()
		{
			// Arrange
			RecordKey recordKey = RecordKey.of("KEY").orElseThrow();
			ConstantRecord constantRecord = ConstantRecord.from(recordKey, true);

			doReturn(constantQueryHandlerMock).when(queryHandlerFactoryMock).getQueryHandler(Section.CONSTANTS);
			when(constantQueryHandlerMock.getRecord(recordKey)).thenReturn(constantRecord);

			ConstantResolver constantResolver = new ConstantResolver(queryHandlerFactoryMock);

			// Act
			Optional<Boolean> result = constantResolver.getBoolean("KEY");

			// Assert
			assertEquals(Optional.of(true), result);
		}


		@Test
		void getBoolean_returns_empty_optional()
		{
			// Arrange
			RecordKey recordKey = RecordKey.of("KEY").orElseThrow();
			ConstantRecord constantRecord = ConstantRecord.from(recordKey, Optional.empty());

			doReturn(constantQueryHandlerMock).when(queryHandlerFactoryMock).getQueryHandler(Section.CONSTANTS);
			when(constantQueryHandlerMock.getRecord(recordKey)).thenReturn(constantRecord);

			ConstantResolver constantResolver = new ConstantResolver(queryHandlerFactoryMock);

			// Act
			Optional<Boolean> result = constantResolver.getBoolean("KEY");

			// Assert
			assertEquals(Optional.empty(), result);
		}
	}


	@Nested
	class ExtractStringTests
	{
		@Test
		void extractString_returns_optional_string_when_given_valid_record()
		{
			// Arrange
			RecordKey recordKey = RecordKey.of("KEY").orElseThrow();
			ConstantRecord constantRecord = ConstantRecord.from(recordKey, "result string");

			ConstantResolver constantResolver = new ConstantResolver(queryHandlerFactoryMock);

			// Act
			Optional<String> result = constantResolver.fetchStringValue(constantRecord);

			// Assert
			assertEquals(Optional.of("result string"), result);
		}


		@Test
		void extractString_returns_empty_optional_when_given_invalid_constant_record()
		{
			// Arrange
			RecordKey recordKey = RecordKey.of("KEY").orElseThrow();
			InvalidConstantRecord constantRecord = new InvalidConstantRecord(recordKey, "reason");

			ConstantResolver constantResolver = new ConstantResolver(queryHandlerFactoryMock);

			// Act
			Optional<String> result = constantResolver.fetchStringValue(constantRecord);

			// Assert
			assertEquals(Optional.empty(), result);
		}
	}


	@Nested
	class ExtractIntegerTests
	{
		@Test
		void extractInteger_returns_optional_integer_when_given_valid_record()
		{
			// Arrange
			RecordKey recordKey = RecordKey.of("KEY").orElseThrow();
			ConstantRecord constantRecord = ConstantRecord.from(recordKey, 42);

			ConstantResolver constantResolver = new ConstantResolver(queryHandlerFactoryMock);

			// Act
			Optional<Integer> result = constantResolver.fetchIntegerValue(constantRecord);

			// Assert
			assertEquals(Optional.of(42), result);
		}


		@Test
		void extractInteger_returns_empty_optional_when_given_invalid_constant_record()
		{
			// Arrange
			RecordKey recordKey = RecordKey.of("KEY").orElseThrow();
			InvalidConstantRecord constantRecord = new InvalidConstantRecord(recordKey, "reason");

			ConstantResolver constantResolver = new ConstantResolver(queryHandlerFactoryMock);

			// Act
			Optional<Integer> result = constantResolver.fetchIntegerValue(constantRecord);

			// Assert
			assertEquals(Optional.empty(), result);
		}
	}


	@Nested
	class ExtractBooleanTests
	{
		@Test
		void extractBoolean_returns_optional_boolean_when_given_valid_record()
		{
			// Arrange
			RecordKey recordKey = RecordKey.of("KEY").orElseThrow();
			ConstantRecord constantRecord = ConstantRecord.from(recordKey, true);

			ConstantResolver constantResolver = new ConstantResolver(queryHandlerFactoryMock);

			// Act
			Optional<Boolean> result = constantResolver.fetchBooleanValue(constantRecord);

			// Assert
			assertEquals(Optional.of(true), result);
		}


		@Test
		void extractBoolean_returns_empty_optional_when_given_invalid_constant_record()
		{
			// Arrange
			RecordKey recordKey = RecordKey.of("KEY").orElseThrow();
			InvalidConstantRecord constantRecord = new InvalidConstantRecord(recordKey, "reason");

			ConstantResolver constantResolver = new ConstantResolver(queryHandlerFactoryMock);

			// Act
			Optional<Boolean> result = constantResolver.fetchBooleanValue(constantRecord);

			// Assert
			assertEquals(Optional.empty(), result);
		}
	}

}
