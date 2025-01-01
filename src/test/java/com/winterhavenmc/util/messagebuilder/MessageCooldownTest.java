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
import com.winterhavenmc.util.messagebuilder.util.MockUtility;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.junit.jupiter.api.*;

import java.util.UUID;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MessageCooldownTest {

	private final static UUID player1Uid = new UUID(1,1);

	private Plugin plugin;
	private Server server;
	private PluginManager pluginManager;
	private Player player1;

	private MessageCooldown<MessageId> messageCooldown;

	@BeforeEach
	public void setUp() {

		plugin = MockUtility.createMockPlugin();

		server = mock(Server.class, "MockServer");
		when(plugin.getServer()).thenReturn(server);

		pluginManager = mock(PluginManager.class, "MockPluginManager");
		when(server.getPluginManager()).thenReturn(pluginManager);

		player1 = mock(Player.class, "MockPlayer1");
		when(player1.getName()).thenReturn("Player One");
		when(player1.getUniqueId()).thenReturn(player1Uid);
		when(server.getPlayer("player1")).thenReturn(player1);

		// instantiate real MessageCooldown
		messageCooldown = MessageCooldown.getInstance(plugin);
	}

	@AfterEach
	public void tearDown() {
		plugin = null;
		server = null;
		pluginManager = null;
		player1 = null;
		messageCooldown = null;
	}


	@Test
	void getInstance() {
		assertNotNull(MessageCooldown.getInstance(plugin));
	}

	@Test
	void getMessageCooldownMap() {
		assertNotNull(messageCooldown.getMessageCooldownMap());
	}

	@Test
	void put() {
		messageCooldown.put(MessageId.ENABLED_MESSAGE, player1);
		assertTrue(messageCooldown.isCooling(player1, MessageId.ENABLED_MESSAGE, 10));
	}

	@Test
	void get() {
		messageCooldown.put(MessageId.ENABLED_MESSAGE, player1);
		assertTrue(messageCooldown.get(MessageId.ENABLED_MESSAGE, player1) > 0);
	}

	@Test
	public void remove() {
		messageCooldown.put(MessageId.ENABLED_MESSAGE, player1);
		assertTrue(messageCooldown.isCooling(player1, MessageId.ENABLED_MESSAGE, 10));
		messageCooldown.remove(player1);
		assertFalse(messageCooldown.isCooling(player1, MessageId.ENABLED_MESSAGE, 10));
	}

	@Test
	public void remove_null_entity() {
		assertFalse(messageCooldown.remove(null) > 0);
	}

	@Test
	void isCooling() {
		messageCooldown.put(MessageId.ENABLED_MESSAGE, player1);
		assertTrue(messageCooldown.isCooling(player1, MessageId.ENABLED_MESSAGE, 10));
	}

	@Test
	void singletonTest() {
		MessageCooldown<MessageId> messageCooldown1 = MessageCooldown.getInstance(plugin);
		MessageCooldown<MessageId> messageCooldown2 = MessageCooldown.getInstance(plugin);
		assertEquals(messageCooldown1, messageCooldown2);
	}

}
