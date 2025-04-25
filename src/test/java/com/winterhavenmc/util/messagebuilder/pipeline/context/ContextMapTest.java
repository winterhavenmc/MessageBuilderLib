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

import com.winterhavenmc.util.messagebuilder.keys.MacroKey;
import com.winterhavenmc.util.messagebuilder.model.recipient.Recipient;
import com.winterhavenmc.util.messagebuilder.keys.RecordKey;
import com.winterhavenmc.util.messagebuilder.messages.Macro;
import com.winterhavenmc.util.messagebuilder.messages.MessageId;
import com.winterhavenmc.util.messagebuilder.validation.ValidationException;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
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
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class ContextMapTest
{
	@Mock Player playerMock;
	@Mock ConsoleCommandSender consoleCommandSenderMock;
	@Mock World worldMock;

	Recipient.Valid consoleRecipient;
	Recipient.Valid playerRecipient;
	RecordKey messageKey;
	MacroKey macroKey;
	ContextMap contextMap;
	Location location;
	MacroKey recipientMacroKey;
	MacroKey locationMacroKey;


	@BeforeEach
	void setUp()
	{
		messageKey = RecordKey.of(MessageId.ENABLED_MESSAGE).orElseThrow();
		macroKey = MacroKey.of(Macro.TOOL).orElseThrow();

		consoleRecipient = switch (Recipient.of(consoleCommandSenderMock))
		{
			case Recipient.Valid vr -> vr;
			case Recipient.Invalid ignored -> throw new ValidationException(PARAMETER_INVALID, RECIPIENT);
		};

		playerRecipient = switch (Recipient.of(playerMock))
		{
			case Recipient.Valid vr -> vr;
			case Recipient.Invalid ignored -> throw new ValidationException(PARAMETER_INVALID, RECIPIENT);
		};

		contextMap = ContextMap.of(playerRecipient, messageKey).orElseThrow();
		location = new Location(worldMock, 11, 12, 13);

		recipientMacroKey = MacroKey.of(TestField.RECIPIENT).orElseThrow();
		locationMacroKey = recipientMacroKey.append(TestField.LOCATION).orElseThrow();
	}


	@Test
	void testAddRecipientContext()
	{
		// Arrange
		ContextMap contextMap = ContextMap.of(consoleRecipient, messageKey).orElseThrow();
		MacroKey recipientMacroKey = MacroKey.of("RECIPIENT").orElseThrow();
		//TODO: Add recipient location to context map

		// Act
		contextMap.addRecipientContext();

		// Assert
		assertTrue(contextMap.contains(recipientMacroKey));
	}

	enum TestField
	{
		RECIPIENT, LOCATION
	}

	@Test
	void testAddRecipientContext_player()
	{
		// Arrange
		when(playerMock.getLocation()).thenReturn(location);
		ContextMap contextMap = ContextMap.of(playerRecipient, messageKey).orElseThrow();

		// Act
		contextMap.addRecipientContext();

		// Assert
		assertTrue(contextMap.contains(recipientMacroKey));
		assertTrue(contextMap.contains(locationMacroKey));
	}


	@Test
	void testAddRecipientContext_non_entity()
	{
		// Arrange
		ContextMap contextMap = ContextMap.of(consoleRecipient, messageKey).orElseThrow();
		MacroKey recipientMacroKey = MacroKey.of("RECIPIENT").orElseThrow();
		MacroKey locationMacroKey = MacroKey.of("LOCATION").orElseThrow();

		// Act
		contextMap.addRecipientContext();

		// Assert
		assertTrue(contextMap.contains(recipientMacroKey));
		assertFalse(contextMap.contains(locationMacroKey));
	}


	@Test
	void testPutAndGet()
	{
		// Arrange
		Integer number = 42;
		macroKey = MacroKey.of(Macro.PAGE_NUMBER).orElseThrow();

		// Act
		contextMap.put(macroKey, number);

		// Assert
		assertEquals(Optional.of(42), contextMap.get(macroKey), "Retrieved value should match the original");
	}


	@Test
	void testPutIfAbsent()
	{
		// Arrange
		Integer number = 42;
		macroKey = MacroKey.of(Macro.PAGE_NUMBER).orElseThrow();

		// Act
		contextMap.putIfAbsent(macroKey, number);

		// Assert
		assertEquals(Optional.of(42), contextMap.get(macroKey), "Retrieved value should match the original");
	}


	@Test
	void testPut_parameter_null_value()
	{
		// Arrange
		macroKey = MacroKey.of("NUMBER").orElseThrow();
		contextMap.putIfAbsent(macroKey, null);

		// Act
		Optional<Object> retrievedValue = contextMap.get(macroKey);

		// Assert
		assertEquals(Optional.of("NULL"), retrievedValue);
	}


	@Test
	void testGetValueWithCorrectType()
	{
		// Arrange
		macroKey = MacroKey.of("PLAYER.LOCATION").orElseThrow();
		Location location = new Location(worldMock, 10, 20, 30);
		contextMap.putIfAbsent(macroKey, location);

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
		macroKey = MacroKey.of("SWORD").orElseThrow();
		ItemStack itemStack = new ItemStack(Material.DIAMOND_SWORD);
		contextMap.putIfAbsent(macroKey, itemStack);

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
		macroKey = MacroKey.of("LOCATION").orElseThrow();
		MacroKey nonExistentKey = MacroKey.of("NON_EXISTENT_KEY").orElseThrow();
		World world = mock(World.class, "MockWorld");
		Location location = new Location(world, 10, 20, 30);
		contextMap.putIfAbsent(macroKey, location);

		// Act & Assert
		assertTrue(contextMap.contains(macroKey), "Key should be present in the map");
		assertFalse(contextMap.contains(nonExistentKey), "Key should not be present in the map");
	}


	@Test
	void testEmptyMap()
	{
		// Act & Assert
		assertFalse(contextMap.contains(macroKey), "Empty map should not contain macro key");
	}


	@Test
	void testEntrySet()
	{
		// Arrange
		MacroKey key1 = MacroKey.of("NUMBER1").orElseThrow();
		Integer value1 = 41;
		contextMap.putIfAbsent(key1, value1);

		MacroKey key2 = MacroKey.of("NUMBER2").orElseThrow();
		Integer value2 = 42;
		contextMap.putIfAbsent(key2, value2);

		// Act
		Set<Map.Entry<MacroKey, Object>> entrySet = contextMap.entrySet();

		// Assert
		assertTrue(entrySet.size() >= 2);
	}


	@Test
	void getRecipient() {
		// Arrange & Act
		Recipient.Valid recipient = contextMap.getRecipient();

		// Assert
		assertNotNull(recipient);
	}


	@Test
	void testGetMessageKey() {
		// Arrange & Act
		RecordKey result = contextMap.getMessageKey();

		// Assert
		assertEquals(MessageId.ENABLED_MESSAGE.name(), result.toString());
	}

}
