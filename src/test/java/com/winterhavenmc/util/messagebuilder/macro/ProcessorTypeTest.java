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

package com.winterhavenmc.util.messagebuilder.macro;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.command.ConsoleCommandSenderMock;
import be.seeseemelk.mockbukkit.entity.OfflinePlayerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import be.seeseemelk.mockbukkit.inventory.ItemStackMock;

import com.winterhavenmc.util.messagebuilder.PluginMain;
import com.winterhavenmc.util.messagebuilder.macro.processor.ProcessorType;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

import org.junit.jupiter.api.*;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProcessorTypeTest {

	ServerMock server;
	PluginMain plugin;

	@BeforeEach
	public void setUp() {
		// Start the mock server
		server = MockBukkit.mock();

		// start the mock plugin
		plugin = MockBukkit.load(PluginMain.class);
	}

	@AfterEach
	public void tearDown() {
		// Stop the mock server
		MockBukkit.unmock();
	}


	@Test
	void matchTypeString() {
		String string = "string";
		assertEquals(ProcessorType.STRING, ProcessorType.matchType(string), "String does not match processor type STRING");
	}

	@Disabled
	@Test
	void matchTypeEntity() {
		PlayerMock player = server.addPlayer("player");
		assertNotNull(player, "Mock player is null.");
		assertEquals(ProcessorType.ENTITY, ProcessorType.matchType(player), "Mock player does not match processor type ENTITY");
	}

	@Test
	void matchTypeCommandSender() {
		CommandSender consoleSender = new ConsoleCommandSenderMock();
		assertNotNull(consoleSender, "Console sender is null.");
		assertEquals(ProcessorType.COMMAND_SENDER, ProcessorType.matchType(consoleSender));
	}

	@Test
	void matchTypeNumber() {
		assertEquals(ProcessorType.NUMBER, ProcessorType.matchType(42));
		assertEquals(ProcessorType.NUMBER, ProcessorType.matchType(420L));
	}

	@Disabled
	@Test
	void matchTypeWorld() {
		World world = server.getWorld("world");
		assertNotNull(world);
		assertEquals(ProcessorType.WORLD, ProcessorType.matchType(world));
	}

	@Test
	void matchTypeOfflinePlayer() {
		OfflinePlayer offlinePlayer = new OfflinePlayerMock(UUID.randomUUID(), "offline guy");
		assertNotNull(offlinePlayer);
		assertEquals(ProcessorType.OFFLINE_PLAYER, ProcessorType.matchType(offlinePlayer));
	}

	@Disabled
	@Test
	void matchTypeItemStack() {
		ItemStack itemStack = new ItemStackMock(Material.STONE);
		assertNotNull(itemStack, "ItemStack is null.");
		assertEquals(ProcessorType.ITEM_STACK, ProcessorType.matchType(itemStack));
	}

	@Disabled
	@Test
	void matchTypeLocation() {
		World world = server.getWorld("world");
		assertNotNull(world);
		Location location = new Location(world,10,20,30);
		assertNotNull(location);
		assertEquals(ProcessorType.LOCATION, ProcessorType.matchType(location));
	}

	@Test
	void matchTypeObject() {
		assertEquals(ProcessorType.OBJECT, ProcessorType.matchType(new Object()));
	}


//	@ParameterizedTest
//	@EnumSource(MacroProcessorType.class)
//	void createProcessor(MacroProcessorType macroProcessorType) {
//		Processor macroProcessor = macroProcessorType.create(plugin, languageHandler);
//	}

}
