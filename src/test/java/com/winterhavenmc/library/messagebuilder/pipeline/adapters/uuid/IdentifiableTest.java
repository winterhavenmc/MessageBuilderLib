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

package com.winterhavenmc.library.messagebuilder.pipeline.adapters.uuid;

import com.winterhavenmc.library.messagebuilder.keys.MacroKey;
import com.winterhavenmc.library.messagebuilder.keys.ValidMacroKey;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.Adapter;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.AdapterContextContainer;
import com.winterhavenmc.library.messagebuilder.pipeline.maps.MacroStringMap;
import com.winterhavenmc.library.messagebuilder.pipeline.formatters.number.LocaleNumberFormatter;
import com.winterhavenmc.library.messagebuilder.pipeline.formatters.FormatterContainer;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;


@ExtendWith(MockitoExtension.class)
class IdentifiableTest
{
	@Mock AdapterContextContainer ctxMock;
	@Mock FormatterContainer formatterContainerMock;
	@Mock LocaleNumberFormatter numberFormatterMock;

	static class TestObject implements Identifiable
	{
		@Override
		public UUID getUniqueId()
		{
			return new UUID(42, 42);
		}
	}


	@Test
	void object_is_instance_of_Identifiable()
	{
		// Arrange & Act
		TestObject testObject = new TestObject();

		// Assert
		assertInstanceOf(Identifiable.class, testObject);
	}


	@Test
	void getUniqueId_returns_UUID()
	{
		// Arrange
		TestObject testObject = new TestObject();

		// Act
		UUID result = testObject.getUniqueId();

		// Assert
		assertEquals(new UUID(42, 42), result);
	}


	@Test
	void extractUniqueId_returns_populated_map()
	{
		// Arrange
		ValidMacroKey baseKey = MacroKey.of("TEST").isValid().orElseThrow();
		ValidMacroKey subKey = baseKey.append(Adapter.BuiltIn.UUID).isValid().orElseThrow();
		TestObject testObject = new TestObject();

		// Act
		MacroStringMap result = testObject.extractUid(baseKey, ctxMock);

		// Assert
		assertEquals(new UUID(42, 42).toString(), result.get(subKey));
	}


	@Test
	void formatUid_returns_optional_string()
	{
		// Arrange
		ValidMacroKey macroKey = MacroKey.of("TEST").isValid().orElseThrow();
		TestObject testObject = new TestObject();

		// Act
		Optional<String> result = Identifiable.formatUid(new UUID(42, 42));

		// Assert
		assertEquals(Optional.of(new UUID(42,42).toString()), result);
	}


	@Test
	void formatUid_with_null_UUID_returns_empty_optional()
	{
		// Arrange
		ValidMacroKey macroKey = MacroKey.of("TEST").isValid().orElseThrow();
		TestObject testObject = new TestObject();

		// Act
		Optional<String> result = Identifiable.formatUid(null);

		// Assert
		assertEquals(Optional.empty(), result);
	}

}
