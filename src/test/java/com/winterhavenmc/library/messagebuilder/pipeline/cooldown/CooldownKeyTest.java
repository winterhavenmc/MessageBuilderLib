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

package com.winterhavenmc.library.messagebuilder.pipeline.cooldown;

import com.winterhavenmc.library.messagebuilder.model.recipient.Recipient;
import com.winterhavenmc.library.messagebuilder.keys.RecordKey;
import com.winterhavenmc.library.messagebuilder.validation.ValidationException;

import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static com.winterhavenmc.library.messagebuilder.messages.MessageId.DISABLED_MESSAGE;
import static com.winterhavenmc.library.messagebuilder.messages.MessageId.ENABLED_MESSAGE;
import static com.winterhavenmc.library.messagebuilder.validation.ErrorMessageKey.PARAMETER_INVALID;
import static com.winterhavenmc.library.messagebuilder.validation.Parameter.RECIPIENT;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class CooldownKeyTest
{
	@Mock Player playerMock;
	@Mock Player player2Mock;
	@Mock ConsoleCommandSender consoleMock;

	Recipient.Valid recipient;
	Recipient.Valid recipient2;
	Recipient.Valid consoleRecipient;
	RecordKey messageKey;


	@BeforeEach
	void setUp()
	{
		recipient = switch (Recipient.of(playerMock)) {
			case Recipient.Valid valid -> valid;
			case Recipient.Proxied ignored -> throw new ValidationException(PARAMETER_INVALID, RECIPIENT);
			case Recipient.Invalid ignored -> throw new ValidationException(PARAMETER_INVALID, RECIPIENT);
		};

		recipient2 = switch (Recipient.of(player2Mock)) {
			case Recipient.Valid valid -> valid;
			case Recipient.Proxied ignored -> throw new ValidationException(PARAMETER_INVALID, RECIPIENT);
			case Recipient.Invalid ignored -> throw new ValidationException(PARAMETER_INVALID, RECIPIENT);
		};

		messageKey = RecordKey.of(ENABLED_MESSAGE).orElseThrow();
	}


	@Test
	void testStaticFactory()
	{
		// Arrange
		// Act
		CooldownKey cooldownKey = CooldownKey.of(recipient, messageKey).orElseThrow();

		// Assert
		assertNotNull(cooldownKey);
	}


	@Test
	void testConstructor_non_entity()
	{
		// Arrange
		consoleRecipient = switch (Recipient.of(consoleMock))
		{
			case Recipient.Valid valid -> valid;
			case Recipient.Proxied ignored -> throw new ValidationException(PARAMETER_INVALID, RECIPIENT);
			case Recipient.Invalid ignored -> throw new ValidationException(PARAMETER_INVALID, RECIPIENT);
		};

		//  Act
		CooldownKey cooldownKey = CooldownKey.of(consoleRecipient, messageKey).orElseThrow();

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

		// Act
		CooldownKey cooldownKey = CooldownKey.of(recipient, messageKey).orElseThrow();

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

		// Act
		CooldownKey cooldownKey = CooldownKey.of(recipient, messageKey).orElseThrow();

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

		// Act
		CooldownKey key1 = CooldownKey.of(recipient, messageKey).orElseThrow();
		CooldownKey key2 = CooldownKey.of(recipient, messageKey).orElseThrow();

		// Assert
		assertEquals(key1, key2); // Objects with the same uuid and messageIdString should be equal

		// Verify
		verify(playerMock, atLeastOnce()).getUniqueId();
	}


	@Test @DisplayName("Different UUIDs should result in inequality")
	void testEquals_DifferentUUIDs()
	{
		// Arrange
		when(playerMock.getUniqueId()).thenReturn(new UUID(1, 1));
		when(player2Mock.getUniqueId()).thenReturn(new UUID(2, 2));

		// Act
		CooldownKey key1 = CooldownKey.of(recipient, messageKey).orElseThrow();
		CooldownKey key2 = CooldownKey.of(recipient2, messageKey).orElseThrow();

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
		RecordKey messageKey1 = RecordKey.of(ENABLED_MESSAGE).orElseThrow();
		RecordKey messageKey2 = RecordKey.of(DISABLED_MESSAGE).orElseThrow();

		// Act
		CooldownKey key1 = CooldownKey.of(recipient, messageKey1).orElseThrow();
		CooldownKey key2 = CooldownKey.of(recipient, messageKey2).orElseThrow();

		// Assert
		assertNotEquals(key1, key2);

		// Verify
		verify(playerMock, atLeastOnce()).getUniqueId();
	}


	@Test @DisplayName("An object should not be equal to null")
	void testEquals_Null()
	{
		// Arrange
		// Act
		CooldownKey key = CooldownKey.of(recipient, messageKey).orElseThrow();

		// Assert
		assertNotEquals(null, key);

		// Verify
		verify(playerMock, atLeastOnce()).getUniqueId();
	}


	@Test @DisplayName("An object should not be equal to an instance of another class")
	void testEquals_DifferentClass()
	{
		// Arrange
		// Act
		CooldownKey key = CooldownKey.of(recipient, messageKey).orElseThrow();

		// Assert
		assertNotEquals("some string", key.toString());

		// Verify
		verify(playerMock, atLeastOnce()).getUniqueId();
	}


	@Test @DisplayName("Equal objects must have the same hash code")
	void testHashCode_EqualObjects()
	{
		// Arrange
		when(playerMock.getUniqueId()).thenReturn(UUID.randomUUID());

		// Act
		CooldownKey key1 = CooldownKey.of(recipient, messageKey).orElseThrow();
		CooldownKey key2 = CooldownKey.of(recipient, messageKey).orElseThrow();

		// Assert
		assertEquals(key1.hashCode(), key2.hashCode());

		// Verify
		verify(playerMock, atLeastOnce()).getUniqueId();
	}


	@Test @DisplayName("Different UUIDs likely result in different hash codes")
	void testHashCode_DifferentUUIDs()
	{
		// Arrange
		when(playerMock.getUniqueId()).thenReturn(UUID.randomUUID());
		when(player2Mock.getUniqueId()).thenReturn(UUID.randomUUID());

		// Act
		CooldownKey key1 = CooldownKey.of(recipient, messageKey).orElseThrow();
		CooldownKey key2 = CooldownKey.of(recipient2, messageKey).orElseThrow();

		// Assert
		assertNotEquals(key1.hashCode(), key2.hashCode());

		// Verify
		verify(playerMock, atLeastOnce()).getUniqueId();
		verify(player2Mock, atLeastOnce()).getUniqueId();
	}


	@Test @DisplayName("Different RecordKeys should result in different hash codes")
	void testHashCode_DifferentMessageIdStrings()
	{
		// Arrange
		when(playerMock.getUniqueId()).thenReturn(UUID.randomUUID());
		RecordKey messageKey1 = RecordKey.of(ENABLED_MESSAGE).orElseThrow();
		RecordKey messageKey2 = RecordKey.of(DISABLED_MESSAGE).orElseThrow();

		// Act
		CooldownKey key1 = CooldownKey.of(recipient, messageKey1).orElseThrow();
		CooldownKey key2 = CooldownKey.of(recipient, messageKey2).orElseThrow();

		// Assert
		assertNotEquals(key1.hashCode(), key2.hashCode());

		// Verify
		verify(playerMock, atLeastOnce()).getUniqueId();
	}


	@Test
	void testGetMessageId()
	{
		// Arrange
		// Act
		RecordKey result = CooldownKey.of(recipient, messageKey).orElseThrow().getMessageKey();

		// Assert
		assertEquals("ENABLED_MESSAGE", result.toString());

		// Verify
		verify(playerMock, atLeastOnce()).getUniqueId();
	}


	@Test
	void testOf()
	{
		// Arrange
		// Act
		Optional<CooldownKey> cooldownKey = CooldownKey.of(recipient, messageKey);

		// Assert
		assertTrue(cooldownKey.isPresent());
		assertEquals("ENABLED_MESSAGE", cooldownKey.get().getMessageKey().toString());

		// Verify
		verify(playerMock, atLeastOnce()).getUniqueId();
	}


	@Test
	void testEquals()
	{
		// Arrange
		// Act
		CooldownKey cooldownKey = CooldownKey.of(recipient, messageKey).orElseThrow();

		// Assert
        assertNotEquals("not a cooldown key", cooldownKey.getMessageKey().toString());

		// Verify
		verify(playerMock, atLeastOnce()).getUniqueId();
	}

}
