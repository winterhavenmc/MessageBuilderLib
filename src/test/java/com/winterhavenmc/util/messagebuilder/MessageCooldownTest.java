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

import org.junit.jupiter.api.*;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;
import org.mockbukkit.mockbukkit.entity.PlayerMock;

import static org.junit.jupiter.api.Assertions.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MessageCooldownTest {

	private ServerMock server;
	private PluginMain plugin;

	@BeforeAll
	public void setUp() {
		// Start the mock server
		server = MockBukkit.mock();

		// start the mock plugin
		plugin = MockBukkit.load(PluginMain.class);
	}

	@AfterAll
	public void tearDown() {
		// Stop the mock server
		MockBukkit.unmock();
	}


	@Test
	void getInstance() {
		new MessageCooldown<>(plugin);
		assertNotNull(MessageCooldown.getInstance(plugin));
	}

	@Test
	void getMessageCooldownMap() {
		MessageCooldown<MessageId> messageCooldown = new MessageCooldown<>(plugin);
		assertNotNull(messageCooldown.getMessageCooldownMap());
	}

	@Disabled
	@Test
	void put() {
		MessageCooldown<MessageId> messageCooldown = new MessageCooldown<>(plugin);
		PlayerMock player = server.addPlayer("testy");
		messageCooldown.put(MessageId.ENABLED_MESSAGE, player);
		assertTrue(messageCooldown.isCooling(player, MessageId.ENABLED_MESSAGE, 10));
	}

	@Disabled
	@Test
	void get() {
		MessageCooldown<MessageId> messageCooldown = new MessageCooldown<>(plugin);
		PlayerMock player = server.addPlayer("player1");
		messageCooldown.put(MessageId.ENABLED_MESSAGE, player);
		assertTrue(messageCooldown.get(MessageId.ENABLED_MESSAGE, player) > 0);
	}

	@Disabled
	@Test
	public void remove() {
		MessageCooldown<MessageId> messageCooldown = new MessageCooldown<>(plugin);
		PlayerMock player = server.addPlayer("player1");
		messageCooldown.put(MessageId.ENABLED_MESSAGE, player);
		assertTrue(messageCooldown.isCooling(player, MessageId.ENABLED_MESSAGE, 10));
		messageCooldown.remove(player);
		assertFalse(messageCooldown.isCooling(player, MessageId.ENABLED_MESSAGE, 10));
	}

	@Disabled
	@Test
	void isCooling() {
		MessageCooldown<MessageId> messageCooldown = new MessageCooldown<>(plugin);
		PlayerMock player1 = server.addPlayer("player1");
		PlayerMock player2 = server.addPlayer("player2");
		messageCooldown.put(MessageId.ENABLED_MESSAGE, player1);
		assertTrue(messageCooldown.isCooling(player1, MessageId.ENABLED_MESSAGE, 10));
		assertFalse(messageCooldown.isCooling(player2, MessageId.ENABLED_MESSAGE, 10));
	}

}
