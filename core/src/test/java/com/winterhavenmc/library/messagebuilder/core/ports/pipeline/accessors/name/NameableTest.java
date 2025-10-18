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

package com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.name;

import com.winterhavenmc.library.messagebuilder.core.context.AccessorCtx;
import com.winterhavenmc.library.messagebuilder.core.maps.MacroStringMap;

import com.winterhavenmc.library.messagebuilder.models.keys.MacroKey;
import com.winterhavenmc.library.messagebuilder.models.keys.ValidMacroKey;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;


@ExtendWith(MockitoExtension.class)
class NameableTest
{
	@Mock AccessorCtx ctxMock;


	static class TestObject implements Nameable
	{
		@Override
		public String getName()
		{
			return "Name";
		}
	}


	@Test
	void object_is_instance_of_Nameable()
	{
		// Arrange & Act
		TestObject testObject = new TestObject();

		// Assert
		assertInstanceOf(Nameable.class, testObject);
	}


	@Test
	void getName_returns_string()
	{
		// Arrange
		TestObject testObject = new TestObject();

		// Act
		String result = testObject.getName();

		// Assert
		assertEquals("Name", result);
	}


	@Test
	void extractName_returns_populated_map()
	{
		// Arrange
		ValidMacroKey baseKey = MacroKey.of("TEST").isValid().orElseThrow();
		ValidMacroKey subKey = baseKey.append("NAME").isValid().orElseThrow();
		TestObject testObject = new TestObject();

		// Act
		MacroStringMap result = testObject.extractName(baseKey, ctxMock);

		// Assert
		assertEquals("Name", result.get(subKey));
	}


	@Test
	void formatName_returns_optional_string()
	{
		// Arrange
		ValidMacroKey macroKey = MacroKey.of("TEST").isValid().orElseThrow();
		TestObject testObject = new TestObject();

		// Act
		Optional<String> result = Nameable.formatName(testObject.getName());

		// Assert
		assertEquals(Optional.of("Name"), result);
	}


	@Test
	void formatName_with_null_name_returns_empty_optional()
	{
		// Arrange
		ValidMacroKey macroKey = MacroKey.of("TEST").isValid().orElseThrow();
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
		ValidMacroKey macroKey = MacroKey.of("TEST").isValid().orElseThrow();
		TestObject testObject = new TestObject();

		// Act
		Optional<String> result = Nameable.formatName("");

		// Assert
		assertEquals(Optional.empty(), result);
	}

}
