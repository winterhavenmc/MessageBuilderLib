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

package com.winterhavenmc.library.messagebuilder.query;

import com.winterhavenmc.library.messagebuilder.keys.ConstantKey;
import com.winterhavenmc.library.messagebuilder.keys.InvalidConstantKey;
import com.winterhavenmc.library.messagebuilder.keys.InvalidKeyReason;
import com.winterhavenmc.library.messagebuilder.keys.ValidConstantKey;
import com.winterhavenmc.library.messagebuilder.model.language.ConstantRecord;
import com.winterhavenmc.library.messagebuilder.model.language.InvalidConstantRecord;
import com.winterhavenmc.library.messagebuilder.model.language.InvalidRecordReason;
import com.winterhavenmc.library.messagebuilder.model.language.ValidConstantRecord;
import com.winterhavenmc.library.messagebuilder.resources.language.SectionProvider;
import com.winterhavenmc.library.messagebuilder.validation.ValidationException;

import org.bukkit.configuration.ConfigurationSection;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ConstantQueryHandlerTest
{
	@Mock ConfigurationSection constantSectionMock;


	@Test
	void constructor_with_null_parameter_throws_validation_exception()
	{
		// Arrange & Act
		ValidationException exception = assertThrows(ValidationException.class,
				() -> new ConstantQueryHandler(null));

		// Assert
		assertEquals("The parameter 'sectionSupplier' cannot be null.", exception.getMessage());
	}


	@Test
	void getRecord_with_valid_key_returns_validRecord()
	{
		// Arrange
		ValidConstantKey recordKey = ConstantKey.of("SPAWN.DISPLAY_NAME").isValid().orElseThrow();

		when(constantSectionMock.get(recordKey.toString())).thenReturn("Spawn Display Name");

		SectionProvider mockProvider = () -> constantSectionMock;
		ConstantQueryHandler handler = new ConstantQueryHandler(mockProvider);

		// Act
		ValidConstantRecord result = (ValidConstantRecord) handler.getRecord(recordKey);

		// Assert
		assertInstanceOf(ValidConstantRecord.class, result);
		assertEquals("Spawn Display Name", result.value());

		// Verify
		verify(constantSectionMock, atLeastOnce()).get("SPAWN.DISPLAY_NAME");
	}


	@Test
	void getRecord_with_invalid_key_returns_InvalidConstantRecord()
	{
		// Arrange
		InvalidConstantKey recordKey = new InvalidConstantKey("Invalid", InvalidKeyReason.KEY_INVALID);

		when(constantSectionMock.get(recordKey.toString())).thenReturn("Spawn Display Name");

		SectionProvider mockProvider = () -> constantSectionMock;
		ConstantQueryHandler handler = new ConstantQueryHandler(mockProvider);

		// Act
		ConstantRecord result = handler.getRecord(recordKey);

		// Assert
		assertInstanceOf(InvalidConstantRecord.class, result);
		assertEquals(recordKey, result.key());
		assertEquals(InvalidRecordReason.CONSTANT_KEY_INVALID, ((InvalidConstantRecord) result).reason());

		// Verify
		verify(constantSectionMock, atLeastOnce()).get(recordKey.toString());
	}


	@Test
	void getString_with_valid_key_returns_valid_string()
	{
		// Arrange
		ValidConstantKey recordKey = ConstantKey.of("SPAWN.DISPLAY_NAME").isValid().orElseThrow();

		when(constantSectionMock.getString(recordKey.toString())).thenReturn("Spawn Display Name");

		SectionProvider mockProvider = () -> constantSectionMock;
		ConstantQueryHandler handler = new ConstantQueryHandler(mockProvider);

		// Act
		Optional<String> result = handler.getString(recordKey);

		// Assert
		assertEquals(Optional.of("Spawn Display Name"), result);

		// Verify
		verify(constantSectionMock, atLeastOnce()).getString("SPAWN.DISPLAY_NAME");
	}


	@Test
	void getRecord_with_invalid_key_returns_invalidRecord()
	{
		// Arrange
		ValidConstantKey recordKey = ConstantKey.of("INVALID_PATH").isValid().orElseThrow();

		SectionProvider mockProvider = () -> constantSectionMock;
		ConstantQueryHandler handler = new ConstantQueryHandler(mockProvider);

		// Act
		ConstantRecord constantRecord = handler.getRecord(recordKey);

		// Assert
		assertInstanceOf(InvalidConstantRecord.class, constantRecord);

		// Verify
		verify(constantSectionMock, atLeastOnce()).get(any());
	}


	@Test
	void getStringList_with_valid_key_returns_valid_stringList()
	{
		// Arrange
		ValidConstantKey recordKey = ConstantKey.of("TEST_LIST").isValid().orElseThrow();

		when(constantSectionMock.getStringList(recordKey.toString())).thenReturn(List.of("string1", "string2"));

		SectionProvider mockProvider = () -> constantSectionMock;
		ConstantQueryHandler handler = new ConstantQueryHandler(mockProvider);

		// Act
		List<String> result = handler.getStringList(recordKey);

		// Assert
		assertEquals(List.of("string1", "string2"), result);

		// Verify
		verify(constantSectionMock, atLeastOnce()).getStringList(anyString());
	}


	@Test
	void getInt_with_valid_key_returns_valid_int()
	{
		// Arrange
		ValidConstantKey recordKey = ConstantKey.of("TEST_INT").isValid().orElseThrow();

		when(constantSectionMock.getInt(recordKey.toString())).thenReturn(42);

		SectionProvider mockProvider = () -> constantSectionMock;
		ConstantQueryHandler handler = new ConstantQueryHandler(mockProvider);

		// Act
		int result = handler.getInt(recordKey);

		// Assert
		assertEquals(42, result);

		// Verify
		verify(constantSectionMock, atLeastOnce()).getInt("TEST_INT");
	}

}
