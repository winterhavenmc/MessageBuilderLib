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

package com.winterhavenmc.library.messagebuilder.pipeline.adapters.killer;

import com.winterhavenmc.library.messagebuilder.keys.MacroKey;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.AdapterContextContainer;
import com.winterhavenmc.library.messagebuilder.pipeline.maps.MacroStringMap;

import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Player;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.winterhavenmc.library.messagebuilder.pipeline.adapters.Adapter.BuiltIn.KILLER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class KillableTest
{
	@Mock AdapterContextContainer ctxMock;
	@Mock Player playerMock;

	class TestObject implements Killable
	{
		@Override
		public AnimalTamer getKiller()
		{
			return playerMock;
		}
	}


	@Test
	void object_is_instance_of_Killable()
	{
		// Arrange & Act
		TestObject testObject = new TestObject();

		// Assert
		assertInstanceOf(Killable.class, testObject);
	}


	@Test
	void getKiller_returns_Entity()
	{
		// Arrange
		TestObject testObject = new TestObject();

		// Act
		AnimalTamer result = testObject.getKiller();

		// Assert
		assertEquals(playerMock, result);
	}


	@Test
	void extractKiller_returns_populated_map()
	{
		// Arrange
		MacroKey baseKey = MacroKey.of("TEST").orElseThrow();
		MacroKey subKey = baseKey.append(KILLER).orElseThrow();
		TestObject testObject = new TestObject();
		when(playerMock.getName()).thenReturn("Killer");

		// Act
		MacroStringMap result = testObject.extractKiller(baseKey, ctxMock);

		// Assert
		assertEquals("Killer", result.get(subKey));
	}


	@Test
	void formatKiller_returns_optional_string()
	{
		// Arrange
		MacroKey macroKey = MacroKey.of("TEST").orElseThrow();
		TestObject testObject = new TestObject();
		when(playerMock.getName()).thenReturn("Killer");

		// Act
		Optional<String> result = Killable.formatKiller(testObject.getKiller());

		// Assert
		assertEquals(Optional.of("Killer"), result);
	}


	@Test
	void formatKiller_with_blank_name_returns_empty_optional()
	{
		// Arrange
		MacroKey macroKey = MacroKey.of("TEST").orElseThrow();
		TestObject testObject = new TestObject();
		when(playerMock.getName()).thenReturn("");

		// Act
		Optional<String> result = Killable.formatKiller(playerMock);

		// Assert
		assertEquals(Optional.empty(), result);
	}


	@Test
	void formatKiller_with_null_owner_returns_empty_optional()
	{
		// Arrange
		MacroKey macroKey = MacroKey.of("TEST").orElseThrow();
		TestObject testObject = new TestObject();

		// Act
		Optional<String> result = Killable.formatKiller(null);

		// Assert
		assertEquals(Optional.empty(), result);
	}

}
