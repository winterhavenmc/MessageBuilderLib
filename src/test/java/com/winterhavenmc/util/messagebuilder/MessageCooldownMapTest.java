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

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class MessageCooldownMapTest {

	private final static UUID player1Uid = new UUID(1,1);

	@Mock private Plugin pluginMock;
	@Mock private Server serverMock;
	@Mock private PluginManager pluginManagerMock;
	@Mock private Player playerMock;

	// get real instance of MessageCooldownMap
	private MessageCooldownMap<MessageId> messageCooldownMap;

	@BeforeEach
	public void setUp() {
		// return serverMock for plugin.getServer()
		when(pluginMock.getServer()).thenReturn(serverMock);

		// return pluginManagerMock for server.getPluginManager()
		when(serverMock.getPluginManager()).thenReturn(pluginManagerMock);

		// instantiate real MessageCooldownMap
		messageCooldownMap = MessageCooldownMap.getInstance(pluginMock);
	}

	@AfterEach
	public void tearDown() {
		pluginMock = null;
		serverMock = null;
		pluginManagerMock = null;
		playerMock = null;
		messageCooldownMap = null;
	}


	@Test
	void getInstance() {
		assertNotNull(MessageCooldownMap.getInstance(pluginMock));
	}

	@Test
	void getMessageCooldownMap() {
		assertNotNull(messageCooldownMap.getMessageCooldownMap());
	}

	@Test
	void put() {
		// Arrange
		when(playerMock.getUniqueId()).thenReturn(player1Uid);

		// Act
		messageCooldownMap.put(MessageId.ENABLED_MESSAGE, playerMock);

		// Assert
		assertTrue(messageCooldownMap.isCooling(playerMock, MessageId.ENABLED_MESSAGE, 10));

		// Verify
		verify(playerMock, atLeastOnce()).getUniqueId();
	}

	@Test
	void get() {
		// Arrange
		when(playerMock.getUniqueId()).thenReturn(player1Uid);

		// Act
		messageCooldownMap.put(MessageId.ENABLED_MESSAGE, playerMock);

		// Assert
		assertTrue(messageCooldownMap.get(MessageId.ENABLED_MESSAGE, playerMock) > 0);

		// Verify
		verify(playerMock, atLeastOnce()).getUniqueId();
	}

	@Test
	public void remove() {
		// Arrange
		when(playerMock.getUniqueId()).thenReturn(player1Uid);

		// Act
		messageCooldownMap.put(MessageId.ENABLED_MESSAGE, playerMock);
		assertTrue(messageCooldownMap.isCooling(playerMock, MessageId.ENABLED_MESSAGE, 10));
		messageCooldownMap.remove(playerMock);

		// Assert
		assertFalse(messageCooldownMap.isCooling(playerMock, MessageId.ENABLED_MESSAGE, 10));

		// Verify
		verify(playerMock, atLeastOnce()).getUniqueId();
	}


	@Test
	public void remove_null_entity() {
		// Act & Assert
		assertFalse(messageCooldownMap.remove(null) > 0);
	}


	@Test
	void isCooling() {
		// Arrange
		when(playerMock.getUniqueId()).thenReturn(player1Uid);

		// Act
		messageCooldownMap.put(MessageId.ENABLED_MESSAGE, playerMock);

		// assert
		assertTrue(messageCooldownMap.isCooling(playerMock, MessageId.ENABLED_MESSAGE, 10));

		// Verify
		verify(playerMock, atLeastOnce()).getUniqueId();
	}


	@Test
	void singletonTest() {
		// Arrange & Act
		MessageCooldownMap<MessageId> messageCooldownMap1 = MessageCooldownMap.getInstance(pluginMock);
		MessageCooldownMap<MessageId> messageCooldownMap2 = MessageCooldownMap.getInstance(pluginMock);

		// Assert
		assertEquals(messageCooldownMap1, messageCooldownMap2);
	}

}
