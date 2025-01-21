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

import com.winterhavenmc.util.messagebuilder.macro.processor.ProcessorType;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class ProcessorTypeTest {

	@Mock Plugin pluginMock;

	@BeforeEach
	public void setUp() {

	}

	@AfterEach
	public void tearDown() {
		pluginMock = null;
	}


	@Test
	void matchTypeString() {
		String string = "string";
		assertEquals(ProcessorType.STRING, ProcessorType.matchType(string), "String does not match processor type STRING");
	}

	@Disabled
	@Test
	void matchTypeEntity() {
		Player player = mock(Player.class);
		assertNotNull(player, "Mock player is null.");
		assertEquals(ProcessorType.ENTITY, ProcessorType.matchType(player), "Mock player does not match processor type ENTITY");
	}

	@Test
	void matchTypeCommandSender() {
		CommandSender consoleSender = mock(ConsoleCommandSender.class);
		assertNotNull(consoleSender, "Console sender is null.");
		assertEquals(ProcessorType.COMMAND_SENDER, ProcessorType.matchType(consoleSender));
	}

	@Test
	void matchTypeNumber() {
		assertEquals(ProcessorType.NUMBER, ProcessorType.matchType(42));
		assertEquals(ProcessorType.NUMBER, ProcessorType.matchType(420L));
		assertEquals(ProcessorType.NUMBER, ProcessorType.matchType(3.14));
	}

	@Disabled
	@Test
	void matchTypeWorld() {
		World world = mock(World.class);
		assertNotNull(world);
		assertEquals(ProcessorType.WORLD, ProcessorType.matchType(world));
	}

	@Test
	void matchTypeOfflinePlayer() {
		OfflinePlayer offlinePlayer = mock(OfflinePlayer.class);
		assertNotNull(offlinePlayer);
		assertEquals(ProcessorType.OFFLINE_PLAYER, ProcessorType.matchType(offlinePlayer));
	}

	@Disabled
	@Test
	void matchTypeItemStack() {
		ItemStack itemStack = mock(ItemStack.class);
		assertNotNull(itemStack, "ItemStack is null.");
		assertEquals(ProcessorType.ITEM_STACK, ProcessorType.matchType(itemStack));
	}

	@Disabled
	@Test
	void matchTypeLocation() {
		World world = mock(World.class);
		when(world.getName()).thenReturn("test_world");
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
//		MacroProcessor macroProcessor = macroProcessorType.create(plugin, languageHandler);
//	}

}
