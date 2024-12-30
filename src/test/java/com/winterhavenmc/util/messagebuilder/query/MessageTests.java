/*
 * Copyright (c) 2022-2024 Tim Savage.
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

package com.winterhavenmc.util.messagebuilder.query;

import com.winterhavenmc.util.messagebuilder.Message;
import com.winterhavenmc.util.messagebuilder.MessageBuilder;
import com.winterhavenmc.util.messagebuilder.messages.Macro;
import com.winterhavenmc.util.messagebuilder.messages.MessageId;
import com.winterhavenmc.util.messagebuilder.mocks.MockPlugin;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.UUID;
import java.util.logging.Logger;

import static com.winterhavenmc.util.messagebuilder.mocks.MockPlugin.getDataFolder;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MessageTests {

	// declare mocks
	Plugin plugin;
	Player player;
	World world;
	FileConfiguration configuration;

	// real uuid for player
	private final static UUID playerUid = new UUID(0, 1);

//	// real location
//	Location location = new Location(world, 0.0, 0.0, 0.0);
//
//	// real uuid for world
//	private final static UUID worldUid = new UUID(1, 1);

	MessageBuilder<MessageId, Macro> messageBuilder;


	@BeforeAll
	public static void preSetUp() {
		System.out.println("Temporary data directory exists: " + getDataFolder().isDirectory());
	}

	@BeforeEach
	public void setUp() throws IOException {

		// create mocks
		plugin = mock(Plugin.class, "mockPlugin");
		player = mock(Player.class, "mockPlayer");
		world = mock(World.class, "mockWorld");
		configuration = mock(FileConfiguration.class, "MockConfiguration");

		// return real logger for mock plugin
		when(plugin.getLogger()).thenReturn(Logger.getLogger(this.getClass().getName()));

		// return temporary directory for mock plugin data directory
		when(plugin.getDataFolder()).thenReturn(MockPlugin.getDataFolder());
		when(plugin.getConfig()).thenReturn(configuration);


		when(player.getName()).thenReturn("player1");
		when(player.getUniqueId()).thenReturn(playerUid);
		when(player.getWorld()).thenReturn(world);
		when(player.getLocation()).thenReturn(new Location(world, 3.0, 4.0, 5.0));

		messageBuilder = new MessageBuilder<>(plugin);
	}

	@AfterEach
	public void tearDown() {
		plugin = null;
		player = null;
		world = null;
		messageBuilder = null;
	}

	@Disabled
	@Test
	void setMacroTest() {
//		PlayerMock testPlayer = server.addPlayer("test_player");
		Message<MessageId, Macro> message = messageBuilder.compose(player, MessageId.DURATION_MESSAGE);
		message.setMacro(Macro.DURATION, 1200L);
		assertEquals("Duration is 1 second", message.toString());
	}

//	@Test
//	void send() {
//	}
//
//	@Test
//	void testToString() {
//	}
//
//	@Test
//	void setAltMessage() {
//	}
//
//	@Test
//	void setAltTitle() {
//	}
//
//	@Test
//	void setAltSubtitle() {
//	}

}