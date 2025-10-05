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

package com.winterhavenmc.library.messagebuilder.core.pipeline.adapters.displayname;

import com.winterhavenmc.library.messagebuilder.core.context.AdapterCtx;
import com.winterhavenmc.library.messagebuilder.core.maps.MacroStringMap;
import com.winterhavenmc.library.messagebuilder.models.keys.MacroKey;
import com.winterhavenmc.library.messagebuilder.models.keys.ValidMacroKey;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class DisplayNameableTest
{
	@Mock AdapterCtx ctxMock;

	static class TestObject implements DisplayNameable
	{
		@Override
		public String getDisplayName()
		{
			return "Display Name";
		}
	}


	@Test
	void object_is_instance_of_DisplayNameable()
	{
		// Arrange & Act
		TestObject testObject = new TestObject();

		// Assert
		assertInstanceOf(DisplayNameable.class, testObject);
	}


	@Test
	void getDisplayName_returns_string()
	{
		// Arrange
		TestObject testObject = new TestObject();

		// Act
		String result = testObject.getDisplayName();

		// Assert
		assertEquals("Display Name", result);
	}


	@Test
	void extractDisplayName_returns_populated_map()
	{
		// Arrange
		ValidMacroKey baseKey = MacroKey.of("TEST").isValid().orElseThrow();
		ValidMacroKey subKey = baseKey.append("DISPLAY_NAME").isValid().orElseThrow();
		TestObject testObject = new TestObject();

		// Act
		MacroStringMap result = testObject.extractDisplayName(baseKey, ctxMock);

		// Assert
		assertEquals("Display Name", result.get(subKey));
	}


	@Test
	void formatDisplayName_returns_optional_string()
	{
		// Arrange
		ValidMacroKey macroKey = MacroKey.of("TEST").isValid().orElseThrow();
		TestObject testObject = new TestObject();

		// Act
		Optional<String> result = DisplayNameable.formatDisplayName(testObject.getDisplayName());

		// Assert
		assertEquals(Optional.of("Display Name"), result);
	}


	@Test
	void formatDisplayName_with_null_displayName_returns_empty_optional()
	{
		// Arrange
		ValidMacroKey macroKey = MacroKey.of("TEST").isValid().orElseThrow();
		TestObject testObject = new TestObject();

		// Act
		Optional<String> result = DisplayNameable.formatDisplayName(null);

		// Assert
		assertEquals(Optional.empty(), result);
	}


	@Test
	void formatDisplayName_with_blank_displayName_returns_empty_optional()
	{
		// Arrange
		ValidMacroKey macroKey = MacroKey.of("TEST").isValid().orElseThrow();
		TestObject testObject = new TestObject();

		// Act
		Optional<String> result = DisplayNameable.formatDisplayName("");

		// Assert
		assertEquals(Optional.empty(), result);
	}

}
