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

import com.winterhavenmc.util.messagebuilder.model.recipient.InvalidRecipient;
import com.winterhavenmc.util.messagebuilder.model.recipient.Recipient;
import com.winterhavenmc.util.messagebuilder.model.recipient.ValidRecipient;
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
	void testFrom_null()
	{
		// Act
		Recipient recipient = Recipient.from(null);

		// Assert
		assertInstanceOf(InvalidRecipient.class, recipient);
	}

	@Test
	void testFrom_player()
	{
		// Act
		Recipient recipient = Recipient.from(playerMock);

		// Assert
		assertInstanceOf(ValidRecipient.class, recipient);
	}

	@Test
	void testFrom_console()
	{
		// Act
		Recipient recipient = Recipient.from(consoleCommandSender);

		// Assert
		assertInstanceOf(ValidRecipient.class, recipient);
	}

	@Test
	void testFrom_other()
	{
		// Act
		Recipient recipient = Recipient.from(zombieVillager);

		// Assert
		assertInstanceOf(InvalidRecipient.class, recipient);
	}

}
