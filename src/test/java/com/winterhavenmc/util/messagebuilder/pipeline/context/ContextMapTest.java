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

package com.winterhavenmc.util.messagebuilder.pipeline.context;

import com.winterhavenmc.util.messagebuilder.recipient.InvalidRecipient;
import com.winterhavenmc.util.messagebuilder.recipient.RecipientResult;
import com.winterhavenmc.util.messagebuilder.recipient.ValidRecipient;
import com.winterhavenmc.util.messagebuilder.recordkey.ValidRecordKey;
import com.winterhavenmc.util.messagebuilder.messages.Macro;
import com.winterhavenmc.util.messagebuilder.messages.MessageId;
import com.winterhavenmc.util.messagebuilder.validation.ValidationException;

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

import static com.winterhavenmc.util.messagebuilder.validation.ErrorMessageKey.PARAMETER_INVALID;
import static com.winterhavenmc.util.messagebuilder.validation.Parameter.RECIPIENT;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;


@ExtendWith(MockitoExtension.class)
class ContextMapTest
{
	@Mock CommandSender commandSenderMock;
	@Mock World worldMock;

	ValidRecipient recipient;
	ValidRecordKey messageKey;
	ValidRecordKey macroKey;
	ContextMap contextMap;


	@BeforeEach
	void setUp()
	{
		messageKey = ValidRecordKey.of(MessageId.ENABLED_MESSAGE).orElseThrow();
		macroKey = ValidRecordKey.of(Macro.TOOL).orElseThrow();
		recipient = switch (RecipientResult.from(commandSenderMock)) {
			case ValidRecipient vr -> vr;
			case InvalidRecipient ignored -> throw new ValidationException(PARAMETER_INVALID, RECIPIENT);
		};
		contextMap = ContextMap.of(recipient, messageKey).orElseThrow();
	}


	@Test
	void testPutAndGet()
	{
		// Arrange
		Integer number = 42;
		macroKey = ValidRecordKey.of(Macro.PAGE_NUMBER).orElseThrow();

		// Act
		contextMap.put(macroKey, number);

		// Assert
		assertEquals(Optional.of(42), contextMap.get(macroKey), "Retrieved value should match the original");
	}


	@Test
	void testPut_parameter_null_value()
	{
		// Arrange
		macroKey = ValidRecordKey.of("NUMBER").orElseThrow();
		contextMap.put(macroKey, null);

		// Act
		Optional<Object> retrievedValue = contextMap.get(macroKey);

		// Assert
		assertEquals(Optional.of("NULL"), retrievedValue);
	}


	@Test
	void testGetValueWithCorrectType()
	{
		// Arrange
		macroKey = ValidRecordKey.of("PLAYER.LOCATION").orElseThrow();
		Location location = new Location(worldMock, 10, 20, 30);
		contextMap.put(macroKey, location);

		// Act
		Optional<Object> retrievedValue = contextMap.get(macroKey);

		// Assert
		assertNotNull(retrievedValue, "Value should be non-null");
		assertEquals(Optional.of(location), retrievedValue, "Retrieved value should match the original");
	}


	@Test
	void testGetValueWithIncorrectType()
	{
		// Arrange
		macroKey = ValidRecordKey.of("SWORD").orElseThrow();
		ItemStack itemStack = new ItemStack(Material.DIAMOND_SWORD);
		contextMap.put(macroKey, itemStack);

		// Act
		Optional<Object> retrievedValue = contextMap.get(macroKey);

		// Assert
		assertTrue(retrievedValue.isPresent());
		assertInstanceOf(ItemStack.class, retrievedValue.get(), "Value should not be present for mismatched type");
	}


	@Test
	void testContains()
	{
		// Arrange
		macroKey = ValidRecordKey.of("LOCATION").orElseThrow();
		ValidRecordKey nonExistentKey = ValidRecordKey.of("NON_EXISTENT_KEY").orElseThrow();
		World world = mock(World.class, "MockWorld");
		Location location = new Location(world, 10, 20, 30);
		contextMap.put(macroKey, location);

		// Act & Assert
		assertTrue(contextMap.contains(macroKey), "Key should be present in the map");
		assertFalse(contextMap.contains(nonExistentKey), "Key should not be present in the map");
	}


	@Test
	void testEmptyMap()
	{
		// Act & Assert
		assertFalse(contextMap.contains(macroKey), "Empty map should not contain any keys");
		assertTrue(contextMap.isEmpty());
	}



	@Test
	void testEntrySet()
	{
		// Arrange
		ValidRecordKey key1 = ValidRecordKey.of("NUMBER1").orElseThrow();
		Integer value1 = 41;
		contextMap.put(key1, value1);

		ValidRecordKey key2 = ValidRecordKey.of("NUMBER2").orElseThrow();
		Integer value2 = 42;
		contextMap.put(key2, value2);

		// Act
		Set<Map.Entry<ValidRecordKey, Object>> entrySet = contextMap.entrySet();

		// Assert
		assertEquals(2, entrySet.size());
	}


	@Test
	void testRemove()
	{
		// Arrange
		ValidRecordKey key1 = ValidRecordKey.of("NUMBER1").orElseThrow();
		Integer value1 = 41;
		contextMap.put(key1, value1);

		ValidRecordKey key2 = ValidRecordKey.of("NUMBER2").orElseThrow();
		Integer value2 = 42;
		contextMap.put(key2, value2);

		assertFalse(contextMap.isEmpty());
		assertEquals(2, contextMap.size());

		// Act
		Object removedObject = contextMap.remove(key1);

		// Assert
		assertEquals(1, contextMap.size());
		assertFalse(contextMap.contains(key1));
		assertTrue(contextMap.contains(key2));
		assertNotNull(removedObject);
	}

//	@Test
//	void testRemove_nonexistent() {
//		// Arrange
//		ValidRecordKey key = ValidRecordKey.create("NUMBER1").orElseThrow();
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
		// Arrange
		ValidRecordKey macroKey1 = ValidRecordKey.of("NUMBER1").orElseThrow();
		Integer value1 = 41;
		contextMap.put(macroKey1, value1);

		ValidRecordKey macroKey2 = ValidRecordKey.of("NUMBER2").orElseThrow();
		Integer value2 = 42;

		contextMap.put(macroKey2, value2);
		assertFalse(contextMap.isEmpty());
		assertEquals(2, contextMap.size());

		// Act
		contextMap.clear();

		// Assert
		assertTrue(contextMap.isEmpty());
	}

	@Test
	void testSize_empty() {
		assertEquals(0, contextMap.size());
		contextMap.put(macroKey, 42);
		assertNotEquals(0, contextMap.size());
	}

	@Test
	void testSize_not_empty() {
		// Arrange
		ValidRecordKey macroKey = ValidRecordKey.of("NUMBER").orElseThrow();
		Integer value = 42;

		// Act
		contextMap.put(macroKey, value);

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
		ValidRecordKey macroKey = ValidRecordKey.of("NUMBER").orElseThrow();
		Integer value = 42;

		// Act
		contextMap.put(macroKey, value);

		// Assert
		assertFalse(contextMap.isEmpty());
	}

	@Test
	void getRecipient() {
		// Arrange & Act
		ValidRecipient recipient = contextMap.getRecipient();

		// Assert
		assertNotNull(recipient);
	}

	@Test
	void testGetMessageKey() {
		// Arrange & Act
		ValidRecordKey result = contextMap.getMessageKey();

		// Assert
		assertEquals(MessageId.ENABLED_MESSAGE.name(), result.toString());
	}

}
