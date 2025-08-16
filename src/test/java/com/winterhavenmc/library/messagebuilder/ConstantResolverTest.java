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
import com.winterhavenmc.library.messagebuilder.query.QueryHandler;
import com.winterhavenmc.library.messagebuilder.query.QueryHandlerFactory;

import org.junit.jupiter.api.DisplayName;
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
		@Test @DisplayName("getString returns optional String value when present.")
		void getString_returns_optional_String()
		{
			// Arrange
			ValidConstantKey recordKey = ConstantKey.of("KEY").isValid().orElseThrow();
			ConstantRecord constantRecord = ConstantRecord.from(recordKey, "result string");

			doReturn(constantQueryHandlerMock).when(queryHandlerFactoryMock).getQueryHandler(Section.CONSTANTS);
			when(constantQueryHandlerMock.getRecord(recordKey)).thenReturn(constantRecord);

			ConstantResolver constantResolver = new ConstantResolver(queryHandlerFactoryMock);

			// Act
			Optional<String> result = constantResolver.getString("KEY");

			// Assert
			assertEquals(Optional.of("result string"), result);
		}


		@Test @DisplayName("getString returns empty optional when not present.")
		void getString_returns_empty_optional()
		{
			// Arrange
			ValidConstantKey recordKey = ConstantKey.of("KEY").isValid().orElseThrow();
			ConstantRecord constantRecord = ConstantRecord.from(recordKey, Optional.empty());

			doReturn(constantQueryHandlerMock).when(queryHandlerFactoryMock).getQueryHandler(Section.CONSTANTS);
			when(constantQueryHandlerMock.getRecord(recordKey)).thenReturn(constantRecord);

			ConstantResolver constantResolver = new ConstantResolver(queryHandlerFactoryMock);

			// Act
			Optional<String> result = constantResolver.getString("KEY");

			// Assert
			assertEquals(Optional.empty(), result);
		}


		@Test @DisplayName("getString returns empty optional when fetched value type is not String.")
		void getString_returns_empty_optional_when_non_string_value_record()
		{
			// Arrange
			ValidConstantKey recordKey = ConstantKey.of("KEY").isValid().orElseThrow();
			ConstantRecord constantRecord = ConstantRecord.from(recordKey, 42);

			doReturn(constantQueryHandlerMock).when(queryHandlerFactoryMock).getQueryHandler(Section.CONSTANTS);
			when(constantQueryHandlerMock.getRecord(recordKey)).thenReturn(constantRecord);

			ConstantResolver constantResolver = new ConstantResolver(queryHandlerFactoryMock);

			// Act
			Optional<String> result = constantResolver.getString("KEY");

			// Assert
			assertEquals(Optional.empty(), result);
		}


		@Test @DisplayName("getString returns empty optional when fetched value type is null.")
		void getString_returns_empty_optional_when_null_value_record()
		{
			// Arrange
			ValidConstantKey recordKey = ConstantKey.of("KEY").isValid().orElseThrow();
			ConstantRecord constantRecord = ConstantRecord.from(recordKey, null);

			doReturn(constantQueryHandlerMock).when(queryHandlerFactoryMock).getQueryHandler(Section.CONSTANTS);
			when(constantQueryHandlerMock.getRecord(recordKey)).thenReturn(constantRecord);

			ConstantResolver constantResolver = new ConstantResolver(queryHandlerFactoryMock);

			// Act
			Optional<String> result = constantResolver.getString("KEY");

			// Assert
			assertEquals(Optional.empty(), result);
		}


		@Test @DisplayName("getString returns empty optional when fetched value type is empty Optional.")
		void getString_returns_empty_optional_when_empty_optional_value_record()
		{
			// Arrange
			ValidConstantKey recordKey = ConstantKey.of("KEY").isValid().orElseThrow();
			ConstantRecord constantRecord = ConstantRecord.from(recordKey, Optional.empty());

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
		@Test @DisplayName("getInteger() returns optional Integer value when present.")
		void getInteger_returns_optional_Integer()
		{
			// Arrange
			ValidConstantKey recordKey = ConstantKey.of("KEY").isValid().orElseThrow();
			ConstantRecord constantRecord = ConstantRecord.from(recordKey, 42);

			doReturn(constantQueryHandlerMock).when(queryHandlerFactoryMock).getQueryHandler(Section.CONSTANTS);
			when(constantQueryHandlerMock.getRecord(recordKey)).thenReturn(constantRecord);

			ConstantResolver constantResolver = new ConstantResolver(queryHandlerFactoryMock);

			// Act
			Optional<Integer> result = constantResolver.getInteger("KEY");

			// Assert
			assertEquals(Optional.of(42), result);
		}


		@Test @DisplayName("getInteger() returns empty optional when not present.")
		void getInteger_returns_empty_optional()
		{
			// Arrange
			ValidConstantKey recordKey = ConstantKey.of("KEY").isValid().orElseThrow();
			ConstantRecord constantRecord = ConstantRecord.from(recordKey, Optional.empty());

			doReturn(constantQueryHandlerMock).when(queryHandlerFactoryMock).getQueryHandler(Section.CONSTANTS);
			when(constantQueryHandlerMock.getRecord(recordKey)).thenReturn(constantRecord);

			ConstantResolver constantResolver = new ConstantResolver(queryHandlerFactoryMock);

			// Act
			Optional<Integer> result = constantResolver.getInteger("KEY");

			// Assert
			assertEquals(Optional.empty(), result);
		}


		@Test @DisplayName("getInteger() returns empty optional when fetched value type is not Integer.")
		void getInteger_returns_empty_optional_when_non_Integer_value_record()
		{
			// Arrange
			ValidConstantKey recordKey = ConstantKey.of("KEY").isValid().orElseThrow();
			ConstantRecord constantRecord = ConstantRecord.from(recordKey, "string");

			doReturn(constantQueryHandlerMock).when(queryHandlerFactoryMock).getQueryHandler(Section.CONSTANTS);
			when(constantQueryHandlerMock.getRecord(recordKey)).thenReturn(constantRecord);

			ConstantResolver constantResolver = new ConstantResolver(queryHandlerFactoryMock);

			// Act
			Optional<Integer> result = constantResolver.getInteger("KEY");

			// Assert
			assertEquals(Optional.empty(), result);
		}


		@Test @DisplayName("getInteger() returns empty optional when fetched value type is null.")
		void getInteger_returns_empty_optional_when_null_value_record()
		{
			// Arrange
			ValidConstantKey recordKey = ConstantKey.of("KEY").isValid().orElseThrow();
			ConstantRecord constantRecord = ConstantRecord.from(recordKey, null);

			doReturn(constantQueryHandlerMock).when(queryHandlerFactoryMock).getQueryHandler(Section.CONSTANTS);
			when(constantQueryHandlerMock.getRecord(recordKey)).thenReturn(constantRecord);

			ConstantResolver constantResolver = new ConstantResolver(queryHandlerFactoryMock);

			// Act
			Optional<Integer> result = constantResolver.getInteger("KEY");

			// Assert
			assertEquals(Optional.empty(), result);
		}


		@Test @DisplayName("getInteger() returns empty optional when fetched value type is empty Optional.")
		void getInteger_returns_empty_optional_when_empty_optional_value_record()
		{
			// Arrange
			ValidConstantKey recordKey = ConstantKey.of("KEY").isValid().orElseThrow();
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
		@Test @DisplayName("getBoolean() returns optional boolean value when present.")
		void getBoolean_returns_optional_Boolean()
		{
			// Arrange
			ValidConstantKey recordKey = ConstantKey.of("KEY").isValid().orElseThrow();
			ConstantRecord constantRecord = ConstantRecord.from(recordKey, true);

			doReturn(constantQueryHandlerMock).when(queryHandlerFactoryMock).getQueryHandler(Section.CONSTANTS);
			when(constantQueryHandlerMock.getRecord(recordKey)).thenReturn(constantRecord);

			ConstantResolver constantResolver = new ConstantResolver(queryHandlerFactoryMock);

			// Act
			Optional<Boolean> result = constantResolver.getBoolean("KEY");

			// Assert
			assertEquals(Optional.of(true), result);
		}


		@Test @DisplayName("getBoolean returns empty optional when not present.")
		void getBoolean_returns_empty_optional()
		{
			// Arrange
			ValidConstantKey recordKey = ConstantKey.of("KEY").isValid().orElseThrow();
			ConstantRecord constantRecord = ConstantRecord.from(recordKey, Optional.empty());

			doReturn(constantQueryHandlerMock).when(queryHandlerFactoryMock).getQueryHandler(Section.CONSTANTS);
			when(constantQueryHandlerMock.getRecord(recordKey)).thenReturn(constantRecord);

			ConstantResolver constantResolver = new ConstantResolver(queryHandlerFactoryMock);

			// Act
			Optional<Boolean> result = constantResolver.getBoolean("KEY");

			// Assert
			assertEquals(Optional.empty(), result);
		}


		@Test @DisplayName("getBoolean returns empty optional when fetched value type is not Boolean.")
		void getBoolean_returns_empty_optional_when_non_Boolean_value_record()
		{
			// Arrange
			ValidConstantKey recordKey = ConstantKey.of("KEY").isValid().orElseThrow();
			ConstantRecord constantRecord = ConstantRecord.from(recordKey, 42);

			doReturn(constantQueryHandlerMock).when(queryHandlerFactoryMock).getQueryHandler(Section.CONSTANTS);
			when(constantQueryHandlerMock.getRecord(recordKey)).thenReturn(constantRecord);

			ConstantResolver constantResolver = new ConstantResolver(queryHandlerFactoryMock);

			// Act
			Optional<Boolean> result = constantResolver.getBoolean("KEY");

			// Assert
			assertEquals(Optional.empty(), result);
		}


		@Test @DisplayName("getBoolean() returns empty optional when fetched value type is null.")
		void getBoolean_returns_empty_optional_when_null_value_record()
		{
			// Arrange
			ValidConstantKey recordKey = ConstantKey.of("KEY").isValid().orElseThrow();
			ConstantRecord constantRecord = ConstantRecord.from(recordKey, null);

			doReturn(constantQueryHandlerMock).when(queryHandlerFactoryMock).getQueryHandler(Section.CONSTANTS);
			when(constantQueryHandlerMock.getRecord(recordKey)).thenReturn(constantRecord);

			ConstantResolver constantResolver = new ConstantResolver(queryHandlerFactoryMock);

			// Act
			Optional<Boolean> result = constantResolver.getBoolean("KEY");

			// Assert
			assertEquals(Optional.empty(), result);
		}


		@Test @DisplayName("getBoolean() returns empty optional when fetched value type is empty Optional.")
		void getBoolean_returns_empty_optional_when_empty_optional_value_record()
		{
			// Arrange
			ValidConstantKey recordKey = ConstantKey.of("KEY").isValid().orElseThrow();
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

}
