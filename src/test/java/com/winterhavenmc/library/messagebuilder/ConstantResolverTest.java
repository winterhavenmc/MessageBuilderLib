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
import com.winterhavenmc.library.messagebuilder.ports.language_resource.ConstantRepository;

import com.winterhavenmc.library.messagebuilder.resources.language.LanguageResourceManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ConstantResolverTest
{
	@Mock LanguageResourceManager languageResourceManagerMock;
	@Mock ConstantRepository constantRepositoryMock;

	ValidConstantKey recordKey;


	@BeforeEach
	void setUp()
	{
		recordKey = ConstantKey.of("KEY").isValid().orElseThrow();
	}


	@Nested
	class GetStringTests
	{
		@Test @DisplayName("getString returns optional String value when present.")
		void getString_returns_optional_String()
		{
			// Arrange
			when(languageResourceManagerMock.constants()).thenReturn(constantRepositoryMock);
			when(constantRepositoryMock.getString(recordKey)).thenReturn(Optional.of("result string"));

			// Act
			ConstantResolver constantResolver = new ConstantResolver(languageResourceManagerMock);
			Optional<String> result = constantResolver.getString("KEY");

			// Assert
			assertEquals(Optional.of("result string"), result);

			// Verify
			verify(languageResourceManagerMock, atLeastOnce()).constants();
			verify(constantRepositoryMock, atLeastOnce()).getString(recordKey);
		}


		@Test @DisplayName("getString returns empty optional when given invalid key.")
		void getString_returns_empty_optional_when_given_invalid_key()
		{
			// Arrange
			when(languageResourceManagerMock.constants()).thenReturn(constantRepositoryMock);

			// Act
			ConstantResolver constantResolver = new ConstantResolver(languageResourceManagerMock);
			Optional<String> result = constantResolver.getString("invalid-key");

			// Assert
			assertEquals(Optional.empty(), result);
			// Verify
			verify(languageResourceManagerMock, atLeastOnce()).constants();
		}


		@Test @DisplayName("getString returns empty optional when not present.")
		void getString_returns_empty_optional()
		{
			// Arrange
			when(languageResourceManagerMock.constants()).thenReturn(constantRepositoryMock);
			when(constantRepositoryMock.getString(recordKey)).thenReturn(Optional.empty());

			// Act
			ConstantResolver constantResolver = new ConstantResolver(languageResourceManagerMock);
			Optional<String> result = constantResolver.getString("KEY");

			// Assert
			assertEquals(Optional.empty(), result);

			// Verify
			verify(languageResourceManagerMock, atLeastOnce()).constants();
			verify(constantRepositoryMock, atLeastOnce()).getString(recordKey);
		}


		@Test @DisplayName("getString returns empty optional when fetched value type is not String.")
		void getString_returns_empty_optional_when_non_string_value_record()
		{
			// Arrange
			when(languageResourceManagerMock.constants()).thenReturn(constantRepositoryMock);
			when(constantRepositoryMock.getString(recordKey)).thenReturn(Optional.empty());

			// Act
			ConstantResolver constantResolver = new ConstantResolver(languageResourceManagerMock);
			Optional<String> result = constantResolver.getString("KEY");

			// Assert
			assertEquals(Optional.empty(), result);

			// Verify
			verify(languageResourceManagerMock, atLeastOnce()).constants();
			verify(constantRepositoryMock, atLeastOnce()).getString(recordKey);
		}


		@Test @DisplayName("getString returns empty optional when fetched value type is null.")
		void getString_returns_empty_optional_when_null_value_record()
		{
			// Arrange
			when(languageResourceManagerMock.constants()).thenReturn(constantRepositoryMock);
			when(constantRepositoryMock.getString(recordKey)).thenReturn(Optional.empty());

			// Act
			ConstantResolver constantResolver = new ConstantResolver(languageResourceManagerMock);
			Optional<String> result = constantResolver.getString("KEY");

			// Assert
			assertEquals(Optional.empty(), result);

			// Verify
			verify(languageResourceManagerMock, atLeastOnce()).constants();
			verify(constantRepositoryMock, atLeastOnce()).getString(recordKey);
		}


		@Test @DisplayName("getString returns empty optional when fetched value type is empty Optional.")
		void getString_returns_empty_optional_when_empty_optional_value_record()
		{
			// Arrange
			when(languageResourceManagerMock.constants()).thenReturn(constantRepositoryMock);
			when(constantRepositoryMock.getString(recordKey)).thenReturn(Optional.empty());

			// Act
			ConstantResolver constantResolver = new ConstantResolver(languageResourceManagerMock);
			Optional<String> result = constantResolver.getString("KEY");

			// Assert
			assertEquals(Optional.empty(), result);

			// Verify
			verify(languageResourceManagerMock, atLeastOnce()).constants();
			verify(constantRepositoryMock, atLeastOnce()).getString(recordKey);
		}
	}


	@Nested
	class GetIntegerTests
	{
		@Test @DisplayName("getInteger() returns optional Integer value when present.")
		void getInteger_returns_optional_Integer()
		{
			// Arrange
			when(languageResourceManagerMock.constants()).thenReturn(constantRepositoryMock);
			when(constantRepositoryMock.getInteger(recordKey)).thenReturn(Optional.of(42));

			// Act
			ConstantResolver constantResolver = new ConstantResolver(languageResourceManagerMock);
			Optional<Integer> result = constantResolver.getInteger("KEY");

			// Assert
			assertEquals(Optional.of(42), result);

			// Verify
			verify(languageResourceManagerMock, atLeastOnce()).constants();
			verify(constantRepositoryMock, atLeastOnce()).getInteger(recordKey);
		}


		@Test @DisplayName("getInteger returns empty optional when given invalid key.")
		void getInteger_returns_empty_optional_when_given_invalid_key()
		{
			// Arrange
			when(languageResourceManagerMock.constants()).thenReturn(constantRepositoryMock);

			// Act
			ConstantResolver constantResolver = new ConstantResolver(languageResourceManagerMock);
			Optional<Integer> result = constantResolver.getInteger("invalid-key");

			// Assert
			assertEquals(Optional.empty(), result);

			// Verify
			verify(languageResourceManagerMock, atLeastOnce()).constants();
		}


		@Test @DisplayName("getInteger() returns empty optional when not present.")
		void getInteger_returns_empty_optional()
		{
			// Arrange
			when(languageResourceManagerMock.constants()).thenReturn(constantRepositoryMock);

			// Act
			ConstantResolver constantResolver = new ConstantResolver(languageResourceManagerMock);
			Optional<Integer> result = constantResolver.getInteger("KEY");

			// Assert
			assertEquals(Optional.empty(), result);

			// Verify
			verify(languageResourceManagerMock, atLeastOnce()).constants();
		}


		@Test @DisplayName("getInteger() returns empty optional when fetched value type is not Integer.")
		void getInteger_returns_empty_optional_when_non_Integer_value_record()
		{
			// Arrange
			when(languageResourceManagerMock.constants()).thenReturn(constantRepositoryMock);

			// Act
			ConstantResolver constantResolver = new ConstantResolver(languageResourceManagerMock);
			Optional<Integer> result = constantResolver.getInteger("KEY");

			// Assert
			assertEquals(Optional.empty(), result);

			// Verify
			verify(languageResourceManagerMock, atLeastOnce()).constants();
		}


		@Test @DisplayName("getInteger() returns empty optional when fetched value type is null.")
		void getInteger_returns_empty_optional_when_null_value_record()
		{
			// Arrange
			when(languageResourceManagerMock.constants()).thenReturn(constantRepositoryMock);
			when(constantRepositoryMock.getInteger(recordKey)).thenReturn(Optional.empty());

			// Act
			ConstantResolver constantResolver = new ConstantResolver(languageResourceManagerMock);
			Optional<Integer> result = constantResolver.getInteger("KEY");

			// Assert
			assertEquals(Optional.empty(), result);

			// Verify
			verify(languageResourceManagerMock, atLeastOnce()).constants();
			verify(constantRepositoryMock, atLeastOnce()).getInteger(recordKey);
		}


		@Test @DisplayName("getInteger() returns empty optional when fetched value type is empty Optional.")
		void getInteger_returns_empty_optional_when_empty_optional_value_record()
		{
			// Arrange
			when(languageResourceManagerMock.constants()).thenReturn(constantRepositoryMock);
			when(constantRepositoryMock.getInteger(recordKey)).thenReturn(Optional.empty());

			// Act
			ConstantResolver constantResolver = new ConstantResolver(languageResourceManagerMock);
			Optional<Integer> result = constantResolver.getInteger("KEY");

			// Assert
			assertEquals(Optional.empty(), result);

			// Verify
			verify(languageResourceManagerMock, atLeastOnce()).constants();
		}
	}


	@Nested
	class GetBooleanTests
	{
		@Test @DisplayName("getBoolean() returns optional boolean value when present.")
		void getBoolean_returns_optional_Boolean()
		{
			// Arrange
			when(languageResourceManagerMock.constants()).thenReturn(constantRepositoryMock);
			when(constantRepositoryMock.getBoolean(recordKey)).thenReturn(Optional.of(true));

			// Act
			ConstantResolver constantResolver = new ConstantResolver(languageResourceManagerMock);
			Optional<Boolean> result = constantResolver.getBoolean("KEY");

			// Assert
			assertEquals(Optional.of(true), result);

			// Verify
			verify(languageResourceManagerMock, atLeastOnce()).constants();
		}


		@Test @DisplayName("getBoolean returns empty optional when given invalid key.")
		void getBoolean_returns_empty_optional_when_given_invalid_key()
		{
			// Arrange
			ValidConstantKey recordKey = ConstantKey.of("KEY").isValid().orElseThrow();
			when(languageResourceManagerMock.constants()).thenReturn(constantRepositoryMock);


			// Act
			ConstantResolver constantResolver = new ConstantResolver(languageResourceManagerMock);
			Optional<Boolean> result = constantResolver.getBoolean("invalid-key");

			// Assert
			assertEquals(Optional.empty(), result);

			// Verify
			verify(languageResourceManagerMock, atLeastOnce()).constants();
		}


		@Test @DisplayName("getBoolean returns empty optional when not present.")
		void getBoolean_returns_empty_optional()
		{
			// Arrange
			ValidConstantKey recordKey = ConstantKey.of("KEY").isValid().orElseThrow();
			when(languageResourceManagerMock.constants()).thenReturn(constantRepositoryMock);
			when(constantRepositoryMock.getBoolean(recordKey)).thenReturn(Optional.empty());

			// Act
			ConstantResolver constantResolver = new ConstantResolver(languageResourceManagerMock);
			Optional<Boolean> result = constantResolver.getBoolean("KEY");

			// Assert
			assertEquals(Optional.empty(), result);

			// Verify
			verify(languageResourceManagerMock, atLeastOnce()).constants();
		}


		@Test @DisplayName("getBoolean returns empty optional when fetched value type is not Boolean.")
		void getBoolean_returns_empty_optional_when_non_Boolean_value_record()
		{
			// Arrange
			ValidConstantKey recordKey = ConstantKey.of("KEY").isValid().orElseThrow();
			when(languageResourceManagerMock.constants()).thenReturn(constantRepositoryMock);
			when(constantRepositoryMock.getBoolean(recordKey)).thenReturn(Optional.empty());

			// Act
			ConstantResolver constantResolver = new ConstantResolver(languageResourceManagerMock);
			Optional<Boolean> result = constantResolver.getBoolean("KEY");

			// Assert
			assertEquals(Optional.empty(), result);

			// Verify
			verify(languageResourceManagerMock, atLeastOnce()).constants();
			verify(constantRepositoryMock, atLeastOnce()).getBoolean(recordKey);
		}


		@Test @DisplayName("getBoolean() returns empty optional when fetched value type is null.")
		void getBoolean_returns_empty_optional_when_null_value_record()
		{
			// Arrange
			when(languageResourceManagerMock.constants()).thenReturn(constantRepositoryMock);
			when(constantRepositoryMock.getBoolean(recordKey)).thenReturn(Optional.empty());

			// Act
			ConstantResolver constantResolver = new ConstantResolver(languageResourceManagerMock);
			Optional<Boolean> result = constantResolver.getBoolean("KEY");

			// Assert
			assertEquals(Optional.empty(), result);

			// Verify
			verify(languageResourceManagerMock, atLeastOnce()).constants();
			verify(constantRepositoryMock, atLeastOnce()).getBoolean(recordKey);
		}


		@Test @DisplayName("getBoolean() returns empty optional when fetched value type is empty Optional.")
		void getBoolean_returns_empty_optional_when_empty_optional_value_record()
		{
			// Arrange
			ValidConstantKey recordKey = ConstantKey.of("KEY").isValid().orElseThrow();
			when(languageResourceManagerMock.constants()).thenReturn(constantRepositoryMock);
			when(constantRepositoryMock.getBoolean(recordKey)).thenReturn(Optional.empty());

			// Act
			ConstantResolver constantResolver = new ConstantResolver(languageResourceManagerMock);
			Optional<Boolean> result = constantResolver.getBoolean("KEY");

			// Assert
			assertEquals(Optional.empty(), result);

			// Verify
			verify(languageResourceManagerMock, atLeastOnce()).constants();
			verify(constantRepositoryMock, atLeastOnce()).getBoolean(recordKey);
		}
	}

}
