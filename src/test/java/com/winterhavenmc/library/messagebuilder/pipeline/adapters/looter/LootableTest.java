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

package com.winterhavenmc.library.messagebuilder.pipeline.adapters.looter;

import com.winterhavenmc.library.messagebuilder.keys.MacroKey;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.AdapterContextContainer;
import com.winterhavenmc.library.messagebuilder.pipeline.maps.MacroStringMap;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.winterhavenmc.library.messagebuilder.pipeline.adapters.Adapter.BuiltIn.LOOTER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class LootableTest
{
	@Mock AdapterContextContainer ctxMock;
	@Mock Player playerMock;

	class TestObject implements Lootable
	{
		@Override
		public Entity getLooter()
		{
			return playerMock;
		}
	}


	@Test
	void object_is_instance_of_Lootable()
	{
		// Arrange & Act
		TestObject testObject = new TestObject();

		// Assert
		assertInstanceOf(Lootable.class, testObject);
	}


	@Test
	void getLooter_returns_Entity()
	{
		// Arrange
		TestObject testObject = new TestObject();

		// Act
		Entity result = testObject.getLooter();

		// Assert
		assertEquals(playerMock, result);
	}


	@Test
	void extractLooter_returns_populated_map()
	{
		// Arrange
		MacroKey baseKey = MacroKey.of("TEST").orElseThrow();
		MacroKey subKey = baseKey.append(LOOTER).orElseThrow();
		TestObject testObject = new TestObject();
		when(playerMock.getName()).thenReturn("Looter");

		// Act
		MacroStringMap result = testObject.extractLooter(baseKey, ctxMock);

		// Assert
		assertEquals("Looter", result.get(subKey));
	}


	@Test
	void formatLooter_returns_optional_string()
	{
		// Arrange
		MacroKey macroKey = MacroKey.of("TEST").orElseThrow();
		TestObject testObject = new TestObject();
		when(playerMock.getName()).thenReturn("Looter");

		// Act
		Optional<String> result = Lootable.formatLooter(testObject.getLooter());

		// Assert
		assertEquals(Optional.of("Looter"), result);
	}


	@Test
	void formatLooter_with_blank_name_returns_optional_UNKNOWN_VALUE()
	{
		// Arrange
		MacroKey macroKey = MacroKey.of("TEST").orElseThrow();
		TestObject testObject = new TestObject();
		when(playerMock.getName()).thenReturn("");

		// Act
		Optional<String> result = Lootable.formatLooter(playerMock);

		// Assert
		assertEquals(Optional.empty(), result);
	}


	@Test
	void formatLooter_with_null_value_returns_optional_UNKNOWN_VALUE()
	{
		// Arrange
		MacroKey macroKey = MacroKey.of("TEST").orElseThrow();

		// Act
		Optional<String> result = Lootable.formatLooter(null);

		// Assert
		assertEquals(Optional.empty(), result);
	}

}
