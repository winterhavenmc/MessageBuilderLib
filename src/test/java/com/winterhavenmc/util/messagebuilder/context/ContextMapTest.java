/*
 * Copyright (c) 2024-2025 Tim Savage.
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

package com.winterhavenmc.util.messagebuilder.context;

import com.winterhavenmc.util.messagebuilder.messages.MessageId;
import com.winterhavenmc.util.messagebuilder.util.LocalizedException;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;


@ExtendWith(MockitoExtension.class)
class ContextMapTest {

	@Mock CommandSender commandSenderMock;
	@Mock World worldMock;
	ContextMap contextMap;

	@BeforeEach
	void setUp() {
		contextMap = new ContextMap(commandSenderMock, MessageId.ENABLED_MESSAGE.name());
	}

	@Test
	void testPutAndGet() {
		// Arrange
		String key = "NUMBER";
		Integer number = 42;

		// Act
		contextMap.put(key, number);

		// Assert
		assertEquals(42, contextMap.get(key), "Retrieved value should match the original");
	}

	@Test
	void testPut_parameter_null_key() {
		// Arrange
		Integer number = 42;

		// Act
		LocalizedException exception = assertThrows(LocalizedException.class,
				() -> contextMap.put(null, number));

		// Assert
		assertEquals("The parameter 'key' cannot be null.", exception.getMessage());
	}

	@Test
	void testPut_parameter_null_value() {
		// Arrange
		String key = "NUMBER";

		// Act
		contextMap.put(key, null);

		// Assert
		assertEquals("NULL", contextMap.get(key));
	}

	@Test
	void testGet_parameter_null_key() {
		// Arrange & Act
		LocalizedException exception = assertThrows(LocalizedException.class,
				() -> contextMap.get(null));

		// Assert
		assertEquals("The parameter 'key' cannot be null.", exception.getMessage());
	}

	@Test
	void testGetValueWithCorrectType() {
		// Arrange
		String key = "PLAYER.LOCATION";
		Location location = new Location(worldMock, 10, 20, 30);
		contextMap.put(key, location);

		// Act
		Optional<Object> retrievedValue = contextMap.getOpt(key);

		// Assert
		assertNotNull(retrievedValue, "Value should be non-null");
		assertEquals(Optional.of(location), retrievedValue, "Retrieved value should match the original");
	}

	@Test
	void testGetValueWithIncorrectType() {
		// Arrange
		String key = "SWORD";
		ItemStack itemStack = new ItemStack(Material.DIAMOND_SWORD);
		contextMap.put(key, itemStack);

		// Act
		Optional<Object> retrievedValue = contextMap.getOpt(key);

		// Assert
		assertTrue(retrievedValue.isPresent());
		assertInstanceOf(ItemStack.class, retrievedValue.get(), "Value should not be present for mismatched type");
	}

	@Test
	void testContains() {
		// Arrange
		String key = "MACRO|LOCATION";
		World world = mock(World.class, "MockWorld");
		Location location = new Location(world, 10, 20, 30);
		contextMap.put(key, location);

		// Act & Assert
		assertTrue(contextMap.contains(key), "Key should be present in the map");
		assertFalse(contextMap.contains("NonExistentKey"), "Key should not be present in the map");
	}

	@Test
	void testContains_parameter_null_key() {
		// Arrange & Act
		LocalizedException exception = assertThrows(LocalizedException.class,
				() -> contextMap.contains(null));

		// Assert
		assertEquals("The parameter 'key' cannot be null.", exception.getMessage());
	}

	@Test
	void testEmptyMap() {
		// Act & Assert
		assertFalse(contextMap.contains("SomeKey"), "Empty map should not contain any keys");
	}



	@Test
	void testEntrySet() {
		// Arrange
		String key1 = "NUMBER1";
		Integer value1 = 41;
		contextMap.put(key1, value1);

		String key2 = "NUMBER2";
		Integer value2 = 42;
		contextMap.put(key2, value2);

		// Act
		Set<Map.Entry<String, Object>> entrySet = contextMap.entrySet();

		// Assert
		assertEquals(2, entrySet.size());
	}

	@Test
	void testRemove() {
		// Arrange
		String key1 = "MACRO|NUMBER1";
		Integer value1 = 41;
		contextMap.put(key1, value1);

		String key2 = "MACRO|NUMBER2";
		Integer value2 = 42;
		contextMap.put(key2, value2);

		assertFalse(contextMap.isEmpty());
		assertEquals(2, contextMap.size());

		// Act
		contextMap.remove(key1);

		// Assert
		assertEquals(1, contextMap.size());
		assertFalse(contextMap.contains(key1));
		assertTrue(contextMap.contains(key2));
	}

	@Test
	void testRemove_parameter_null_key() {
		// Arrange & Act
		LocalizedException exception = assertThrows(LocalizedException.class,
				() -> contextMap.remove(null));

		// Assert
		assertEquals("The parameter 'key' cannot be null.", exception.getMessage());
	}

	@Test
	void testRemove_nonexistent() {
		// Arrange
		String key = "NUMBER1";
		Integer value = 41;
		contextMap.put(key, value);

		// Act
		Object removedObject = contextMap.remove("NONEXISTENT_KEY");

		// Assert
		assertNull(removedObject);
	}

	@Test
	void testClear() {
		String key1 = "MACRO|NUMBER1";
		Integer value1 = 41;
		contextMap.put(key1, value1);

		String key2 = "MACRO|NUMBER2";
		Integer value2 = 42;
		contextMap.put(key2, value2);

		assertFalse(contextMap.isEmpty());
		assertEquals(2, contextMap.size());
		contextMap.clear();

		assertTrue(contextMap.isEmpty());
	}

	@Test
	void testSize_empty() {
		assertEquals(0, contextMap.size());
	}

	@Test
	void testSize_not_empty() {
		String key = "MACRO|NUMBER";
		Integer value =42;

		// Act
		contextMap.put(key, value);

		// Assert
		assertEquals(1, contextMap.size());
	}

	@Test
	void testIsEmpty() {
		assertTrue(contextMap.isEmpty());
	}

	@Test
	void testIsEmpty_not_empty() {
		// Arrange
		String key = "NUMBER";
		Integer value =42;

		// Act
		contextMap.put(key, value);

		// Assert
		assertFalse(contextMap.isEmpty());
	}

	@Test
	void getRecipient() {
		CommandSender sender = contextMap.getRecipient();

		assertNotNull(sender);
	}

	@Test
	void getMessageId() {
		String result = contextMap.getMessageId();

		assertEquals(MessageId.ENABLED_MESSAGE.name(), result);
	}

}
