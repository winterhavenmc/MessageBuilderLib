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

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import com.winterhavenmc.util.messagebuilder.messages.MessageId;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MessageCooldownTest {

	private ServerMock server;
	private PluginMain plugin;
	private MessageCooldown<MessageId> messageCooldown;

	@BeforeEach
	public void setUp() {
		// Start the mock server
		server = MockBukkit.mock();

		// start the mock plugin
		plugin = MockBukkit.load(PluginMain.class);

		// instantiate MessageCooldown
		messageCooldown = new MessageCooldown<>(plugin);
	}

	@AfterEach
	public void tearDown() {
		// Stop the mock server
		MockBukkit.unmock();
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
