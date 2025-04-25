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

package com.winterhavenmc.util.messagebuilder.model.recipient;

import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import org.bukkit.entity.ZombieVillager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class RecipientTest
{
	@Mock Player playerMock;
	@Mock ConsoleCommandSender consoleCommandSender;
	@Mock ZombieVillager zombieVillager;

	@BeforeEach
	void setUp()
	{
	}

	@Test
	void testOf_null()
	{
		// Act
		Recipient recipient = Recipient.of(null);

		// Assert
		assertInstanceOf(Recipient.Invalid.class, recipient);
	}

	@Test
	void testOf_player()
	{
		// Act
		Recipient recipient = Recipient.of(playerMock);

		// Assert
		assertInstanceOf(Recipient.Valid.class, recipient);
	}

	@Test
	void testOf_console()
	{
		// Act
		Recipient recipient = Recipient.of(consoleCommandSender);

		// Assert
		assertInstanceOf(Recipient.Valid.class, recipient);
	}

	@Test
	void testOf_other()
	{
		// Act
		Recipient recipient = Recipient.of(zombieVillager);

		// Assert
		assertInstanceOf(Recipient.Invalid.class, recipient);
	}

}
