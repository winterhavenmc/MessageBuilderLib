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

package com.winterhavenmc.library.messagebuilder.core.pipeline.cooldown;

import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.cooldown.CooldownKey;
import com.winterhavenmc.library.messagebuilder.models.keys.MessageKey;
import com.winterhavenmc.library.messagebuilder.models.keys.ValidMessageKey;
import com.winterhavenmc.library.messagebuilder.models.recipient.Recipient;
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

import static com.winterhavenmc.library.messagebuilder.core.util.MessageId.DISABLED_MESSAGE;
import static com.winterhavenmc.library.messagebuilder.core.util.MessageId.ENABLED_MESSAGE;
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
	ValidMessageKey messageKey;


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

		messageKey = MessageKey.of(ENABLED_MESSAGE).isValid().orElseThrow();
	}


	@Test @DisplayName("of() method with valid parameters returns valid string.")
	void static_factory_method_with_valid_parameters_returns_valid_key()
	{
		// Arrange
		// Act
		CooldownKey cooldownKey = CooldownKey.of(recipient, messageKey).orElseThrow();

		// Assert
		assertNotNull(cooldownKey);
	}


	@Test @DisplayName("of() method with non-entity inserts zero uuid.")
	void static_factory_method_with_non_entity_inerts_zero_uuid()
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


	@Test @DisplayName("toString() returns String representation of string.")
	void toString_returns_string_representation_of_key()
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


	@Test @DisplayName("Key is equal to self.")
	void equals_SameObject()
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


	@Test @DisplayName("keys created with same values are equal.")
	void keys_with_same_values_are_equal()
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
	void keys_with_different_uuids_are_not_equal()
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


	@Test @DisplayName("Different messageIds result in inequality")
	void keys_with_different_messageIds_are_not_equal()
	{
		// Arrange
		when(playerMock.getUniqueId()).thenReturn(UUID.randomUUID());
		ValidMessageKey messageKey1 = MessageKey.of(ENABLED_MESSAGE).isValid().orElseThrow();
		ValidMessageKey messageKey2 = MessageKey.of(DISABLED_MESSAGE).isValid().orElseThrow();

		// Act
		CooldownKey key1 = CooldownKey.of(recipient, messageKey1).orElseThrow();
		CooldownKey key2 = CooldownKey.of(recipient, messageKey2).orElseThrow();

		// Assert
		assertNotEquals(key1, key2);

		// Verify
		verify(playerMock, atLeastOnce()).getUniqueId();
	}


	@Test @DisplayName("Key is not equal to null")
	void key_not_equal_to_null()
	{
		// Arrange
		// Act
		CooldownKey key = CooldownKey.of(recipient, messageKey).orElseThrow();

		// Assert
		assertNotEquals(null, key);

		// Verify
		verify(playerMock, atLeastOnce()).getUniqueId();
	}


	@Test @DisplayName("An string is not equal to an instance of another class")
	void objects_of_different_class_are_not_equal()
	{
		// Arrange & Act
		CooldownKey key = CooldownKey.of(recipient, messageKey).orElseThrow();

		// Assert
		assertNotEquals("some string", key.toString());

		// Verify
		verify(playerMock, atLeastOnce()).getUniqueId();
	}


	@Test @DisplayName("Equal objects have the same hash code")
	void equal_objects_have_same_hashcode()
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


	@Test @DisplayName("Different UUIDs result in different hash codes")
	void unequal_uuids_result_in_different_hash_codes()
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


	@Test @DisplayName("Different MessageIds result in different hash codes")
	void unequal_messageIds_result_in_different_hash_codes()
	{
		// Arrange
		when(playerMock.getUniqueId()).thenReturn(UUID.randomUUID());
		ValidMessageKey messageKey1 = MessageKey.of(ENABLED_MESSAGE).isValid().orElseThrow();
		ValidMessageKey messageKey2 = MessageKey.of(DISABLED_MESSAGE).isValid().orElseThrow();

		// Act
		CooldownKey key1 = CooldownKey.of(recipient, messageKey1).orElseThrow();
		CooldownKey key2 = CooldownKey.of(recipient, messageKey2).orElseThrow();

		// Assert
		assertNotEquals(key1.hashCode(), key2.hashCode());

		// Verify
		verify(playerMock, atLeastOnce()).getUniqueId();
	}


	@Test @DisplayName("getMessageId() returns valid messageId.")
	void getMessageId_returns_valid_messageId()
	{
		// Arrange
		// Act
		ValidMessageKey result = CooldownKey.of(recipient, messageKey).orElseThrow().getMessageKey();

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
        assertNotEquals("not a cooldown string", cooldownKey.getMessageKey().toString());

		// Verify
		verify(playerMock, atLeastOnce()).getUniqueId();
	}

}
