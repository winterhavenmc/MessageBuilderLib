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

package com.winterhavenmc.library.messagebuilder.pipeline.adapters.quantity;

import com.winterhavenmc.library.messagebuilder.keys.MacroKey;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.AdapterContextContainer;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.name.Nameable;
import com.winterhavenmc.library.messagebuilder.pipeline.maps.MacroStringMap;
import com.winterhavenmc.library.messagebuilder.pipeline.formatters.number.LocaleNumberFormatter;
import com.winterhavenmc.library.messagebuilder.pipeline.formatters.FormatterContainer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class QuantifiableTest
{
	@Mock AdapterContextContainer ctxMock;
	@Mock FormatterContainer formatterContainerMock;
	@Mock
	LocaleNumberFormatter numberFormatterMock;

	static class TestObject implements Quantifiable
	{
		@Override
		public int getQuantity()
		{
			return 42;
		}
	}


	@Test
	void object_is_instance_of_Quantifiable()
	{
		// Arrange & Act
		TestObject testObject = new TestObject();

		// Assert
		assertInstanceOf(Quantifiable.class, testObject);
	}


	@Test
	void getQuantity_returns_int()
	{
		// Arrange
		TestObject testObject = new TestObject();

		// Act
		int result = testObject.getQuantity();

		// Assert
		assertEquals(42, result);
	}


	@Test
	void extractQuantity_returns_populated_map()
	{
		// Arrange
		MacroKey baseKey = MacroKey.of("TEST").orElseThrow();
		MacroKey subKey = baseKey.append("QUANTITY").orElseThrow();
		TestObject testObject = new TestObject();
		when(ctxMock.formatterContainer()).thenReturn(formatterContainerMock);
		when(formatterContainerMock.localeNumberFormatter()).thenReturn(numberFormatterMock);
		when(numberFormatterMock.getFormatted(42)).thenReturn("42");

		// Act
		MacroStringMap result = testObject.extractQuantity(baseKey, ctxMock);

		// Assert
		assertEquals("42", result.get(subKey));
	}


	@Test
	void formatQuantity_returns_optional_string()
	{
		// Arrange
		MacroKey macroKey = MacroKey.of("TEST").orElseThrow();
		TestObject testObject = new TestObject();
		when(numberFormatterMock.getFormatted(42)).thenReturn("42");

		// Act
		Optional<String> result = Quantifiable.formatQuantity(42, numberFormatterMock);

		// Assert
		assertEquals(Optional.of("42"), result);
	}


	@Test
	void formatName_with_null_name_returns_empty_optional()
	{
		// Arrange
		MacroKey macroKey = MacroKey.of("TEST").orElseThrow();
		TestObject testObject = new TestObject();

		// Act
		Optional<String> result = Nameable.formatName(null);

		// Assert
		assertEquals(Optional.empty(), result);
	}


	@Test
	void formatName_with_blank_name_returns_empty_optional()
	{
		// Arrange
		MacroKey macroKey = MacroKey.of("TEST").orElseThrow();
		TestObject testObject = new TestObject();

		// Act
		Optional<String> result = Nameable.formatName("");

		// Assert
		assertEquals(Optional.empty(), result);
	}

}
