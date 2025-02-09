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

package com.winterhavenmc.util.messagebuilder.pipeline;

import com.winterhavenmc.util.messagebuilder.messages.MessageId;

import com.winterhavenmc.util.messagebuilder.util.LocalizedException;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class CooldownKeyTest {

	@Mock Player playerMock;


	@Test
	void testConstructor() {
		// Arrange & Act
		CooldownKey cooldownKey = new CooldownKey(playerMock, MessageId.ENABLED_MESSAGE.name());

		// Assert
		assertNotNull(cooldownKey);
	}

	@Test
	void testConstructor_parameter_null_recipient() {
		// Arrange & Act
		LocalizedException exception = assertThrows(LocalizedException.class,
				() -> new CooldownKey(null, MessageId.ENABLED_MESSAGE.name()));

		// Assert
		assertEquals("The parameter 'recipient' cannot be null.", exception.getMessage());
	}

	@Test
	void testConstructor_parameter_null_messageId() {
		// Arrange & Act
		LocalizedException exception = assertThrows(LocalizedException.class,
				() -> new CooldownKey(playerMock, null));

		// Assert
		assertEquals("The parameter 'messageId' cannot be null.", exception.getMessage());
	}

	@Test
	void testConstructor_non_entity() {
		// Arrange
		ConsoleCommandSender console = mock(ConsoleCommandSender.class);

		//  Act
		CooldownKey cooldownKey = new CooldownKey(console, MessageId.ENABLED_MESSAGE.name());

		// Assert
		assertNotNull(cooldownKey);
	}

	@Test
	void testToString() {
		// Arrange
		UUID randomUUID = UUID.randomUUID();
		when(playerMock.getUniqueId()).thenReturn(randomUUID);

		// Act
		CooldownKey cooldownKey = new CooldownKey(playerMock, MessageId.ENABLED_MESSAGE.name());

		// Assert
		assertEquals("ENABLED_MESSAGE|" + randomUUID, cooldownKey.toString());
	}

	@Test
	void testEquals_SameObject() {
		when(playerMock.getUniqueId()).thenReturn(UUID.randomUUID());
		CooldownKey key = new CooldownKey(playerMock, MessageId.ENABLED_MESSAGE.name());

		assertEquals(key, key); // An object should be equal to itself
	}

	@Test
	void testEquals_EqualObjects() {
		when(playerMock.getUniqueId()).thenReturn(UUID.randomUUID());
		CooldownKey key1 = new CooldownKey(playerMock, MessageId.ENABLED_MESSAGE.name());
		CooldownKey key2 = new CooldownKey(playerMock, MessageId.ENABLED_MESSAGE.name());

		assertEquals(key1, key2); // Objects with the same uuid and messageIdString should be equal
	}

	@Test
	void testEquals_DifferentUUIDs() {
		Player player2Mock = mock(Player.class, "Player Two");
		when(playerMock.getUniqueId()).thenReturn(UUID.randomUUID());
		when(player2Mock.getUniqueId()).thenReturn(UUID.randomUUID());
		CooldownKey key1 = new CooldownKey(playerMock, MessageId.ENABLED_MESSAGE.name());
		CooldownKey key2 = new CooldownKey(player2Mock, MessageId.ENABLED_MESSAGE.name());

		assertNotEquals(key1, key2); // Different UUIDs should result in inequality
	}

	@Test
	void testEquals_DifferentMessageIdStrings() {
		when(playerMock.getUniqueId()).thenReturn(UUID.randomUUID());
		CooldownKey key1 = new CooldownKey(playerMock, MessageId.ENABLED_MESSAGE.name());
		CooldownKey key2 = new CooldownKey(playerMock, MessageId.DISABLED_MESSAGE.name());

		assertNotEquals(key1, key2); // Different messageIdStrings should result in inequality
	}

	@Test
	void testEquals_Null() {
		CooldownKey key = new CooldownKey(playerMock, MessageId.ENABLED_MESSAGE.name());

		assertNotEquals(null, key); // An object should not be equal to null
	}

	@Test
	void testEquals_DifferentClass() {
		CooldownKey key = new CooldownKey(playerMock, MessageId.ENABLED_MESSAGE.name());

		assertNotEquals("some string", key); // An object should not be equal to an instance of another class
	}

	@Test
	void testHashCode_EqualObjects() {
		when(playerMock.getUniqueId()).thenReturn(UUID.randomUUID());
		CooldownKey key1 = new CooldownKey(playerMock, MessageId.ENABLED_MESSAGE.name());
		CooldownKey key2 = new CooldownKey(playerMock, MessageId.ENABLED_MESSAGE.name());

		assertEquals(key1.hashCode(), key2.hashCode()); // Equal objects must have the same hash code
	}

	@Test
	void testHashCode_DifferentUUIDs() {
		Player player2Mock = mock(Player.class, "Player Two");
		when(playerMock.getUniqueId()).thenReturn(UUID.randomUUID());
		when(player2Mock.getUniqueId()).thenReturn(UUID.randomUUID());
		CooldownKey key1 = new CooldownKey(playerMock, MessageId.ENABLED_MESSAGE.name());
		CooldownKey key2 = new CooldownKey(player2Mock, MessageId.ENABLED_MESSAGE.name());

		assertNotEquals(key1.hashCode(), key2.hashCode()); // Different UUIDs likely result in different hash codes
	}

	@Test
	void testHashCode_DifferentMessageIdStrings() {
		when(playerMock.getUniqueId()).thenReturn(UUID.randomUUID());
		CooldownKey key1 = new CooldownKey(playerMock, MessageId.ENABLED_MESSAGE.name());
		CooldownKey key2 = new CooldownKey(playerMock, MessageId.DISABLED_MESSAGE.name());

		assertNotEquals(key1.hashCode(), key2.hashCode()); // Different messageIdStrings should result in different hash codes
	}

	@Test
	void testGetMessageId() {
		// Arrange
		CooldownKey cooldownKey = new CooldownKey(playerMock, MessageId.ENABLED_MESSAGE.name());

		// Act
		String messageId = cooldownKey.getMessageId();

		// Assert
		assertEquals("ENABLED_MESSAGE", messageId);
	}

	@Test
	void testOptional() {
		// Arrange & Act
		Optional<CooldownKey> cooldownKey = CooldownKey.optional(playerMock, MessageId.ENABLED_MESSAGE.name());

		// Assert
		assertTrue(cooldownKey.isPresent());
		assertEquals("ENABLED_MESSAGE", cooldownKey.get().getMessageId());
	}

	@Test
	void testEquals() {
		CooldownKey cooldownKey = new CooldownKey(playerMock, MessageId.ENABLED_MESSAGE.name());

		assertFalse(cooldownKey.equals("not a cooldown key"));
	}
}
