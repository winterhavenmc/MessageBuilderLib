/*
 * Copyright (c) 2022 Tim Savage.
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

import com.winterhavenmc.util.messagebuilder.messages.MessageId;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class MessageCooldownTest
{
	@Mock Plugin plugin;
	@Mock Player playerMock;

	MessageCooldown<MessageId> messageCooldown;


	@BeforeEach
	public void setUp()
	{
		// instantiate MessageCooldown
		messageCooldown = new MessageCooldown<>(plugin);
	}


	@Test
	void getInstance() {
		assertNotNull(MessageCooldown.getInstance(plugin));
	}

	@Test
	void getMessageCooldownMap() {
		assertNotNull(messageCooldown.getMessageCooldownMap());
	}

	@Disabled
	@Test
	void put() {
		MessageCooldown<MessageId> messageCooldown = new MessageCooldown<>(plugin);
		when(playerMock.getName()).thenReturn("testy");
		messageCooldown.put(MessageId.ENABLED_MESSAGE, playerMock);
		assertTrue(messageCooldown.isCooling(playerMock, MessageId.ENABLED_MESSAGE, 10));
	}

	@Disabled
	@Test
	void get() {
		MessageCooldown<MessageId> messageCooldown = new MessageCooldown<>(plugin);
		messageCooldown.put(MessageId.ENABLED_MESSAGE, playerMock);
		assertTrue(messageCooldown.get(MessageId.ENABLED_MESSAGE, playerMock) > 0);
	}

	@Disabled
	@Test
	public void remove() {
		MessageCooldown<MessageId> messageCooldown = new MessageCooldown<>(plugin);
		messageCooldown.put(MessageId.ENABLED_MESSAGE, playerMock);
		assertTrue(messageCooldown.isCooling(playerMock, MessageId.ENABLED_MESSAGE, 10));
		messageCooldown.remove(playerMock);
		assertFalse(messageCooldown.isCooling(playerMock, MessageId.ENABLED_MESSAGE, 10));
	}

	@Disabled
	@Test
	void isCooling() {
		MessageCooldown<MessageId> messageCooldown = new MessageCooldown<>(plugin);
		messageCooldown.put(MessageId.ENABLED_MESSAGE, playerMock);
		assertTrue(messageCooldown.isCooling(playerMock, MessageId.ENABLED_MESSAGE, 10));
	}

}
