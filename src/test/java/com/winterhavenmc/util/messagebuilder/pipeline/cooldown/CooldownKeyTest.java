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

package com.winterhavenmc.util.messagebuilder.pipeline.cooldown;

import com.winterhavenmc.util.messagebuilder.resources.RecordKey;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static com.winterhavenmc.util.messagebuilder.messages.MessageId.DISABLED_MESSAGE;
import static com.winterhavenmc.util.messagebuilder.messages.MessageId.ENABLED_MESSAGE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class CooldownKeyTest {

	@Mock Player playerMock;


	@Test
	void testConstructor() {
		// Arrange
		RecordKey recordKey = RecordKey.of(ENABLED_MESSAGE).orElseThrow();

		// Act
		CooldownKey cooldownKey = CooldownKey.of(playerMock, recordKey).orElseThrow();

		// Assert
		assertNotNull(cooldownKey);
	}


	@Test
	void testConstructor_parameter_null_recipient() {
		// Arrange
		RecordKey recordKey = RecordKey.of(ENABLED_MESSAGE).orElseThrow();

		// Act
		Optional<CooldownKey> cooldownKey = CooldownKey.of(null, recordKey);

		// Assert
		assertTrue(cooldownKey.isEmpty());
	}


	@Test
	void testConstructor_non_entity()
	{
		// Arrange
		ConsoleCommandSender console = mock(ConsoleCommandSender.class);
		RecordKey recordKey = RecordKey.of(ENABLED_MESSAGE).orElseThrow();

		//  Act
		CooldownKey cooldownKey = CooldownKey.of(console, recordKey).orElseThrow();

		// Assert
		assertNotNull(cooldownKey);
		assertEquals("ENABLED_MESSAGE|00000000-0000-0000-0000-000000000000", cooldownKey.toString());
	}


	@Test
	void testToString()
	{
		// Arrange
		UUID randomUUID = UUID.randomUUID();
		when(playerMock.getUniqueId()).thenReturn(randomUUID);
		RecordKey recordKey = RecordKey.of(ENABLED_MESSAGE).orElseThrow();

		// Act
		CooldownKey cooldownKey = CooldownKey.of(playerMock, recordKey).orElseThrow();

		// Assert
		assertEquals("ENABLED_MESSAGE|" + randomUUID, cooldownKey.toString());

		// Verify
		verify(playerMock, atLeastOnce()).getUniqueId();
	}


	@Test
	void testEquals_SameObject()
	{
		// Arrange
		when(playerMock.getUniqueId()).thenReturn(UUID.randomUUID());
		RecordKey recordKey = RecordKey.of(ENABLED_MESSAGE).orElseThrow();

		// Act
		CooldownKey cooldownKey = CooldownKey.of(playerMock, recordKey).orElseThrow();

		// Assert
		assertEquals(cooldownKey, cooldownKey); // An object should be equal to itself

		// Verify
		verify(playerMock, atLeastOnce()).getUniqueId();
	}


	@Test
	void testEquals_EqualObjects()
	{
		// Arrange
		when(playerMock.getUniqueId()).thenReturn(UUID.randomUUID());
		RecordKey recordKey = RecordKey.of(ENABLED_MESSAGE).orElseThrow();

		// Act
		CooldownKey key1 = CooldownKey.of(playerMock, recordKey).orElseThrow();
		CooldownKey key2 = CooldownKey.of(playerMock, recordKey).orElseThrow();

		// Assert
		assertEquals(key1, key2); // Objects with the same uuid and messageIdString should be equal

		// Verify
		verify(playerMock, atLeastOnce()).getUniqueId();
	}


	@Test @DisplayName("Different UUIDs should result in inequality")
	void testEquals_DifferentUUIDs()
	{
		// Arrange
		Player player2Mock = mock(Player.class, "Player Two");
		when(playerMock.getUniqueId()).thenReturn(UUID.randomUUID());
		when(player2Mock.getUniqueId()).thenReturn(UUID.randomUUID());
		RecordKey recordKey = RecordKey.of(ENABLED_MESSAGE).orElseThrow();

		// Act
		CooldownKey key1 = CooldownKey.of(playerMock, recordKey).orElseThrow();
		CooldownKey key2 = CooldownKey.of(player2Mock, recordKey).orElseThrow();

		// Assert
		assertNotEquals(key1, key2);

		// Verify
		verify(playerMock, atLeastOnce()).getUniqueId();
		verify(player2Mock, atLeastOnce()).getUniqueId();
	}


	@Test @DisplayName("Different messageIdStrings should result in inequality")
	void testEquals_DifferentMessageIdStrings()
	{
		// Arrange
		when(playerMock.getUniqueId()).thenReturn(UUID.randomUUID());
		RecordKey recordKey1 = RecordKey.of(ENABLED_MESSAGE).orElseThrow();
		RecordKey recordKey2 = RecordKey.of(DISABLED_MESSAGE).orElseThrow();

		// Act
		CooldownKey key1 = CooldownKey.of(playerMock, recordKey1).orElseThrow();
		CooldownKey key2 = CooldownKey.of(playerMock, recordKey2).orElseThrow();

		// Assert
		assertNotEquals(key1, key2);

		// Verify
		verify(playerMock, atLeastOnce()).getUniqueId();
	}


	@Test @DisplayName("An object should not be equal to null")
	void testEquals_Null()
	{
		// Arrange
		RecordKey recordKey = RecordKey.of(ENABLED_MESSAGE).orElseThrow();

		// Act
		CooldownKey key = CooldownKey.of(playerMock, recordKey).orElseThrow();

		// Assert
		assertNotEquals(null, key);

		// Verify
		verify(playerMock, atLeastOnce()).getUniqueId();
	}


	@Test @DisplayName("An object should not be equal to an instance of another class")
	void testEquals_DifferentClass()
	{
		// Arrange
		RecordKey recordKey = RecordKey.of(ENABLED_MESSAGE).orElseThrow();

		// Act
		CooldownKey key = CooldownKey.of(playerMock, recordKey).orElseThrow();

		// Assert
		assertNotEquals("some string", key);

		// Verify
		verify(playerMock, atLeastOnce()).getUniqueId();
	}


	@Test @DisplayName("Equal objects must have the same hash code")
	void testHashCode_EqualObjects()
	{
		// Arrange
		when(playerMock.getUniqueId()).thenReturn(UUID.randomUUID());
		RecordKey recordKey = RecordKey.of(ENABLED_MESSAGE).orElseThrow();

		// Act
		CooldownKey key1 = CooldownKey.of(playerMock, recordKey).orElseThrow();
		CooldownKey key2 = CooldownKey.of(playerMock, recordKey).orElseThrow();

		// Assert
		assertEquals(key1.hashCode(), key2.hashCode());

		// Verify
		verify(playerMock, atLeastOnce()).getUniqueId();
	}


	@Test @DisplayName("Different UUIDs likely result in different hash codes")
	void testHashCode_DifferentUUIDs()
	{
		// Arrange
		Player player2Mock = mock(Player.class, "Player Two");
		when(playerMock.getUniqueId()).thenReturn(UUID.randomUUID());
		when(player2Mock.getUniqueId()).thenReturn(UUID.randomUUID());
		RecordKey recordKey = RecordKey.of(ENABLED_MESSAGE).orElseThrow();

		// Act
		CooldownKey key1 = CooldownKey.of(playerMock, recordKey).orElseThrow();
		CooldownKey key2 = CooldownKey.of(player2Mock, recordKey).orElseThrow();

		// Assert
		assertNotEquals(key1.hashCode(), key2.hashCode());

		// Verify
		verify(playerMock, atLeastOnce()).getUniqueId();
	}


	@Test @DisplayName("Different RecordKeys should result in different hash codes")
	void testHashCode_DifferentMessageIdStrings()
	{
		// Arrange
		when(playerMock.getUniqueId()).thenReturn(UUID.randomUUID());
		RecordKey recordKey1 = RecordKey.of(ENABLED_MESSAGE).orElseThrow();
		RecordKey recordKey2 = RecordKey.of(DISABLED_MESSAGE).orElseThrow();

		// Act
		CooldownKey key1 = CooldownKey.of(playerMock, recordKey1).orElseThrow();
		CooldownKey key2 = CooldownKey.of(playerMock, recordKey2).orElseThrow();

		// Assert
		assertNotEquals(key1.hashCode(), key2.hashCode());

		// Verify
		verify(playerMock, atLeastOnce()).getUniqueId();
	}


	@Test
	void testGetMessageId()
	{
		// Arrange
		RecordKey recordKey = RecordKey.of(ENABLED_MESSAGE).orElseThrow();

		// Act
		RecordKey result = CooldownKey.of(playerMock, recordKey).orElseThrow().getRecordKey();

		// Assert
		assertEquals("ENABLED_MESSAGE", result.toString());

		// Verify
		verify(playerMock, atLeastOnce()).getUniqueId();
	}


	@Test
	void testOf()
	{
		// Arrange
		RecordKey recordKey = RecordKey.of(ENABLED_MESSAGE).orElseThrow();

		// Act
		Optional<CooldownKey> cooldownKey = CooldownKey.of(playerMock, recordKey);

		// Assert
		assertTrue(cooldownKey.isPresent());
		assertEquals("ENABLED_MESSAGE", cooldownKey.get().getRecordKey().toString());

		// Verify
		verify(playerMock, atLeastOnce()).getUniqueId();
	}


	@Test
	void testEquals()
	{
		// Arrange
		RecordKey recordKey = RecordKey.of(ENABLED_MESSAGE).orElseThrow();

		// Act
		CooldownKey cooldownKey = CooldownKey.of(playerMock, recordKey).orElseThrow();

		// Assert
        assertNotEquals("not a cooldown key", cooldownKey.getRecordKey().toString());

		// Verify
		verify(playerMock, atLeastOnce()).getUniqueId();
	}

}
