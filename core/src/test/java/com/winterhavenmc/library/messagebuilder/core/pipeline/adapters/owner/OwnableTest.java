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

package com.winterhavenmc.library.messagebuilder.core.pipeline.adapters.owner;

import com.winterhavenmc.library.messagebuilder.core.context.AdapterCtx;
import com.winterhavenmc.library.messagebuilder.core.maps.MacroStringMap;
import com.winterhavenmc.library.messagebuilder.models.keys.MacroKey;
import com.winterhavenmc.library.messagebuilder.models.keys.ValidMacroKey;

import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Player;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class OwnableTest
{
	@Mock AdapterCtx ctxMock;
	@Mock Player playerMock;

	class TestObject implements Ownable
	{
		@Override
		public AnimalTamer getOwner()
		{
			return playerMock;
		}
	}


	@Test
	void object_is_instance_of_Ownable()
	{
		// Arrange & Act
		TestObject testObject = new TestObject();

		// Assert
		assertInstanceOf(Ownable.class, testObject);
	}


	@Test
	void getOwner_returns_Entity()
	{
		// Arrange
		TestObject testObject = new TestObject();

		// Act
		AnimalTamer result = testObject.getOwner();

		// Assert
		assertEquals(playerMock, result);
	}


	@Test
	void extractOwner_returns_populated_map()
	{
		// Arrange
		ValidMacroKey baseKey = MacroKey.of("TEST").isValid().orElseThrow();
		ValidMacroKey subKey = baseKey.append("OWNER").isValid().orElseThrow();
		TestObject testObject = new TestObject();
		when(playerMock.getName()).thenReturn("Owner");

		// Act
		MacroStringMap result = testObject.extractOwner(baseKey, ctxMock);

		// Assert
		assertEquals("Owner", result.get(subKey));
	}


	@Test
	void formatOwner_returns_optional_string()
	{
		// Arrange
		ValidMacroKey macroKey = MacroKey.of("TEST").isValid().orElseThrow();
		TestObject testObject = new TestObject();
		when(playerMock.getName()).thenReturn("Owner");

		// Act
		Optional<String> result = Ownable.formatOwner(testObject.getOwner());

		// Assert
		assertEquals(Optional.of("Owner"), result);
	}


	@Test
	void formatOwner_with_null_name_returns_empty_optional()
	{
		// Arrange
		ValidMacroKey macroKey = MacroKey.of("TEST").isValid().orElseThrow();
		TestObject testObject = new TestObject();
		when(playerMock.getName()).thenReturn(null);

		// Act
		Optional<String> result = Ownable.formatOwner(playerMock);

		// Assert
		assertEquals(Optional.empty(), result);
	}


	@Test
	void formatOwner_with_blank_name_returns_empty_optional()
	{
		// Arrange
		ValidMacroKey macroKey = MacroKey.of("TEST").isValid().orElseThrow();
		TestObject testObject = new TestObject();
		when(playerMock.getName()).thenReturn("");

		// Act
		Optional<String> result = Ownable.formatOwner(playerMock);

		// Assert
		assertEquals(Optional.empty(), result);
	}


	@Test
	void formatOwner_with_null_owner_returns_empty_optional()
	{
		// Arrange
		ValidMacroKey macroKey = MacroKey.of("TEST").isValid().orElseThrow();
		TestObject testObject = new TestObject();

		// Act
		Optional<String> result = Ownable.formatOwner(null);

		// Assert
		assertEquals(Optional.empty(), result);
	}

}
