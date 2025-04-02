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
import com.winterhavenmc.util.messagebuilder.pipeline.ContextMap;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.RecordKey;

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
class ContextMapTest
{
	@Mock CommandSender commandSenderMock;
	@Mock World worldMock;
	ContextMap contextMap;
	RecordKey recordKey = RecordKey.create(MessageId.ENABLED_MESSAGE).orElseThrow();

	@BeforeEach
	void setUp() {
		contextMap = new ContextMap(commandSenderMock, RecordKey.create(MessageId.ENABLED_MESSAGE).orElseThrow());
	}


	@Test
	void testPutAndGet()
	{
		// Arrange
		Integer number = 42;

		// Act
		contextMap.put(recordKey, number);

		// Assert
		assertEquals(Optional.of(42), contextMap.getOpt(recordKey), "Retrieved value should match the original");
	}


	@Test
	void testPut_parameter_null_value()
	{
		// Arrange
		RecordKey key = RecordKey.create("NUMBER").orElseThrow();

		// Act
		contextMap.put(key, null);

		// Assert
		assertEquals(Optional.of("NULL"), contextMap.getOpt(key));
	}


	@Test
	void testGetValueWithCorrectType()
	{
		// Arrange
		RecordKey key = RecordKey.create("PLAYER.LOCATION").orElseThrow();
		Location location = new Location(worldMock, 10, 20, 30);
		contextMap.put(key, location);

		// Act
		Optional<Object> retrievedValue = contextMap.getOpt(key);

		// Assert
		assertNotNull(retrievedValue, "Value should be non-null");
		assertEquals(Optional.of(location), retrievedValue, "Retrieved value should match the original");
	}


	@Test
	void testGetValueWithIncorrectType()
	{
		// Arrange
		RecordKey key = RecordKey.create("SWORD").orElseThrow();
		ItemStack itemStack = new ItemStack(Material.DIAMOND_SWORD);
		contextMap.put(key, itemStack);

		// Act
		Optional<Object> retrievedValue = contextMap.getOpt(key);

		// Assert
		assertTrue(retrievedValue.isPresent());
		assertInstanceOf(ItemStack.class, retrievedValue.get(), "Value should not be present for mismatched type");
	}


	@Test
	void testContains()
	{
		// Arrange
		RecordKey key = RecordKey.create("LOCATION").orElseThrow();
		RecordKey nonExistentKey = RecordKey.create("NON_EXISTENT_KEY").orElseThrow();
		World world = mock(World.class, "MockWorld");
		Location location = new Location(world, 10, 20, 30);
		contextMap.put(key, location);

		// Act & Assert
		assertTrue(contextMap.contains(key), "Key should be present in the map");
		assertFalse(contextMap.contains(nonExistentKey), "Key should not be present in the map");
	}


	@Test
	void testEmptyMap()
	{
		// Act & Assert
		assertFalse(contextMap.contains(recordKey), "Empty map should not contain any keys");
		assertTrue(contextMap.isEmpty());
	}



	@Test
	void testEntrySet()
	{
		// Arrange
		RecordKey key1 = RecordKey.create("NUMBER1").orElseThrow();
		Integer value1 = 41;
		contextMap.put(key1, value1);

		RecordKey key2 = RecordKey.create("NUMBER2").orElseThrow();
		Integer value2 = 42;
		contextMap.put(key2, value2);

		// Act
		Set<Map.Entry<RecordKey, Object>> entrySet = contextMap.entrySet();

		// Assert
		assertEquals(2, entrySet.size());
	}


	@Test
	void testRemove()
	{
		// Arrange
		RecordKey key1 = RecordKey.create("NUMBER1").orElseThrow();
		Integer value1 = 41;
		contextMap.put(key1, value1);

		RecordKey key2 = RecordKey.create("NUMBER2").orElseThrow();
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

//	@Test
//	void testRemove_nonexistent() {
//		// Arrange
//		RecordKey key = RecordKey.create("NUMBER1").orElseThrow();
//		Integer value = 41;
//		contextMap.put(key, value);
//
//		// Act
//		Object removedObject = contextMap.remove(key);
//
//		// Assert
//		assertNull(removedObject);
//	}

	@Test
	void testClear()
	{
		RecordKey key1 = RecordKey.create("NUMBER1").orElseThrow();
		Integer value1 = 41;
		contextMap.put(key1, value1);

		RecordKey key2 = RecordKey.create("NUMBER2").orElseThrow();
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
		RecordKey key = RecordKey.create("NUMBER").orElseThrow();
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
		RecordKey key = RecordKey.create("NUMBER").orElseThrow();
		Integer value = 42;

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
	void testGetMessageKey() {
		RecordKey result = contextMap.getMessageKey();

		assertEquals(MessageId.ENABLED_MESSAGE.name(), result.toString());
	}

}
