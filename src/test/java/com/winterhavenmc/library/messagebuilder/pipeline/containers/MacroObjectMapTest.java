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

package com.winterhavenmc.library.messagebuilder.pipeline.containers;

import com.winterhavenmc.library.messagebuilder.keys.MacroKey;
import com.winterhavenmc.library.messagebuilder.model.recipient.Recipient;
import com.winterhavenmc.library.messagebuilder.keys.RecordKey;
import com.winterhavenmc.library.messagebuilder.messages.Macro;
import com.winterhavenmc.library.messagebuilder.messages.MessageId;
import com.winterhavenmc.library.messagebuilder.validation.ValidationException;

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

import static com.winterhavenmc.library.messagebuilder.validation.ErrorMessageKey.PARAMETER_INVALID;
import static com.winterhavenmc.library.messagebuilder.validation.Parameter.RECIPIENT;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;


@ExtendWith(MockitoExtension.class)
class MacroObjectMapTest
{
	@Mock Player playerMock;
	@Mock ConsoleCommandSender consoleCommandSenderMock;
	@Mock World worldMock;

	Recipient.Valid consoleRecipient;
	Recipient.Valid playerRecipient;
	RecordKey messageKey;
	MacroKey macroKey;
	MacroObjectMap macroObjectMap;
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
			case Recipient.Proxied ignored -> throw new ValidationException(PARAMETER_INVALID, RECIPIENT);
			case Recipient.Invalid ignored -> throw new ValidationException(PARAMETER_INVALID, RECIPIENT);
		};

		playerRecipient = switch (Recipient.of(playerMock))
		{
			case Recipient.Valid vr -> vr;
			case Recipient.Proxied ignored -> throw new ValidationException(PARAMETER_INVALID, RECIPIENT);
			case Recipient.Invalid ignored -> throw new ValidationException(PARAMETER_INVALID, RECIPIENT);
		};

		macroObjectMap = new MacroObjectMap();
		location = new Location(worldMock, 11, 12, 13);

		recipientMacroKey = MacroKey.of("RECIPIENT").orElseThrow();
		locationMacroKey = recipientMacroKey.append("LOCATION").orElseThrow();
	}


	@Test
	void testPutAndGet()
	{
		// Arrange
		Integer number = 42;
		macroKey = MacroKey.of(Macro.PAGE_NUMBER).orElseThrow();

		// Act
		macroObjectMap.put(macroKey, number);

		// Assert
		assertEquals(Optional.of(42), macroObjectMap.get(macroKey), "Retrieved value should match the original");
	}


	@Test
	void testPutIfAbsent()
	{
		// Arrange
		Integer number = 42;
		macroKey = MacroKey.of(Macro.PAGE_NUMBER).orElseThrow();

		// Act
		macroObjectMap.putIfAbsent(macroKey, number);

		// Assert
		assertEquals(Optional.of(42), macroObjectMap.get(macroKey), "Retrieved value should match the original");
	}


	@Test
	void testPut_parameter_null_value()
	{
		// Arrange
		macroKey = MacroKey.of("NUMBER").orElseThrow();
		macroObjectMap.putIfAbsent(macroKey, null);

		// Act
		Optional<Object> retrievedValue = macroObjectMap.get(macroKey);

		// Assert
		assertEquals(Optional.of("NULL"), retrievedValue);
	}


	@Test
	void testGetValueWithCorrectType()
	{
		// Arrange
		macroKey = MacroKey.of("PLAYER.LOCATION").orElseThrow();
		Location location = new Location(worldMock, 10, 20, 30);
		macroObjectMap.putIfAbsent(macroKey, location);

		// Act
		Optional<Object> retrievedValue = macroObjectMap.get(macroKey);

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
		macroObjectMap.putIfAbsent(macroKey, itemStack);

		// Act
		Optional<Object> retrievedValue = macroObjectMap.get(macroKey);

		// Assert
		assertTrue(retrievedValue.isPresent());
		assertInstanceOf(ItemStack.class, retrievedValue.get(), "Value should not be present for mismatched type");
	}


	@Test
	void testEntrySet()
	{
		// Arrange
		MacroKey key1 = MacroKey.of("NUMBER1").orElseThrow();
		Integer value1 = 41;
		macroObjectMap.putIfAbsent(key1, value1);

		MacroKey key2 = MacroKey.of("NUMBER2").orElseThrow();
		Integer value2 = 42;
		macroObjectMap.putIfAbsent(key2, value2);

		// Act
		Set<Map.Entry<MacroKey, Object>> entrySet = macroObjectMap.entrySet();

		// Assert
		assertTrue(entrySet.size() >= 2);
	}


	@Test
	void size()
	{
		// Arrange
		MacroKey key1 = MacroKey.of("NUMBER1").orElseThrow();
		Integer value1 = 41;
		macroObjectMap.putIfAbsent(key1, value1);

		MacroKey key2 = MacroKey.of("NUMBER2").orElseThrow();
		Integer value2 = 42;
		macroObjectMap.putIfAbsent(key2, value2);

		// Act
		var result = macroObjectMap.size();

		// Assert
		assertEquals(2, result);
	}


	@Test
	void isEmpty()
	{
		assertTrue(macroObjectMap.isEmpty());
	}

}
