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

package com.winterhavenmc.util.messagebuilder;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class RecipientTest
{
	@Mock Player playerMock;


	@Test
	void testStaticFactory()
	{
		// Act
		Optional<Recipient> validRecipient = Recipient.of(playerMock);

		// Assert
		assertTrue(validRecipient.isPresent());
	}


	@Test
	void testStaticFactory_parameter_null()
	{
		// Act
		Optional<Recipient> validRecipient = Recipient.of(null);

		// Assert
		assertTrue(validRecipient.isEmpty());
	}


	@Test
	void asCommandSender()
	{
        // Arrange
		Recipient recipient = Recipient.of(playerMock).orElseThrow();

        // Act
		CommandSender sender = recipient.asCommandSender();

        // Assert
        assertNotNull(sender);
		assertInstanceOf(CommandSender.class, sender);
	}


	@Test
	void testToString()
	{
		// Arrange
		when(playerMock.getName()).thenReturn("Player One");
		Recipient recipient = Recipient.of(playerMock).orElseThrow();

		// Act
		String result = recipient.toString();

		// Assert
		assertEquals("Player One", result);
	}

}
