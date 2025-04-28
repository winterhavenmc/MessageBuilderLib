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

import com.winterhavenmc.library.messagebuilder.keys.RecordKey;
import com.winterhavenmc.library.messagebuilder.model.language.constant.ConstantRecord;
import com.winterhavenmc.library.messagebuilder.model.language.constant.InvalidConstantRecord;
import com.winterhavenmc.library.messagebuilder.model.language.constant.ValidConstantRecord;
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
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class ConstantQueryHandlerTest
{
	@Mock ConfigurationSection constantSectionMock;


	@Test
	void testConstructor_parameter_null()
	{
		// Arrange & Act
		ValidationException exception = assertThrows(ValidationException.class,
				() -> new ConstantQueryHandler(null));

		// Assert
		assertEquals("The parameter 'sectionSupplier' cannot be null.", exception.getMessage());
	}


	@Test
	void getRecord_keyPath_valid()
	{
		// Arrange
		RecordKey recordKey = RecordKey.of("SPAWN.DISPLAY_NAME").orElseThrow();

		when(constantSectionMock.get(recordKey.toString())).thenReturn("Spawn Display Name");

		SectionProvider mockProvider = () -> constantSectionMock;
		ConstantQueryHandler handler = new ConstantQueryHandler(mockProvider);

		// Act
		ValidConstantRecord result = (ValidConstantRecord) handler.getRecord(recordKey);

		// Assert
		assertInstanceOf(ValidConstantRecord.class, result);
		assertEquals("Spawn Display Name", result.value());
	}


	@Test
	void getString_keyPath_valid()
	{
		// Arrange
		RecordKey recordKey = RecordKey.of("SPAWN.DISPLAY_NAME").orElseThrow();

		when(constantSectionMock.getString(recordKey.toString())).thenReturn("Spawn Display Name");

		SectionProvider mockProvider = () -> constantSectionMock;
		ConstantQueryHandler handler = new ConstantQueryHandler(mockProvider);

		// Act
		Optional<String> result = handler.getString(recordKey);

		// Assert
		assertEquals(Optional.of("Spawn Display Name"), result);
	}


	@Test
	void getString_keyPath_invalid()
	{
		// Arrange
		RecordKey recordKey = RecordKey.of("INVALID_PATH").orElseThrow();

		SectionProvider mockProvider = () -> constantSectionMock;
		ConstantQueryHandler handler = new ConstantQueryHandler(mockProvider);

		// Act
		ConstantRecord constantRecord = handler.getRecord(recordKey);

		// Assert
		assertInstanceOf(InvalidConstantRecord.class, constantRecord);
	}


	@Test
	void getStringList_keyPath_valid()
	{
		// Arrange
		RecordKey recordKey = RecordKey.of("TEST_LIST").orElseThrow();

		when(constantSectionMock.getStringList(recordKey.toString())).thenReturn(List.of("string1", "string2"));

		SectionProvider mockProvider = () -> constantSectionMock;
		ConstantQueryHandler handler = new ConstantQueryHandler(mockProvider);


		// Act
		List<String> result = handler.getStringList(recordKey);

		// Assert
		assertEquals(List.of("string1", "string2"), result);
	}


	@Test
	void getInt_keyPath_valid()
	{
		// Arrange
		RecordKey recordKey = RecordKey.of("TEST_INT").orElseThrow();

		when(constantSectionMock.getInt(recordKey.toString())).thenReturn(42);

		SectionProvider mockProvider = () -> constantSectionMock;
		ConstantQueryHandler handler = new ConstantQueryHandler(mockProvider);

		// Act
		int result = handler.getInt(recordKey);

		// Assert
		assertEquals(42, result);
	}

}
