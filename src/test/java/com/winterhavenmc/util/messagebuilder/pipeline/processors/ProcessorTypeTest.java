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

package com.winterhavenmc.util.messagebuilder.pipeline.processors;

import com.winterhavenmc.util.messagebuilder.pipeline.processors.DependencyContext;
import com.winterhavenmc.util.messagebuilder.pipeline.processors.MacroProcessor;
import com.winterhavenmc.util.messagebuilder.pipeline.processors.ProcessorType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;


@ExtendWith(MockitoExtension.class)
class ProcessorTypeTest {

	@Mock CommandSender commandSenderMock;
	@Mock OfflinePlayer offlinePlayer;
	@Mock World worldMock;
	@Mock Location locationMock;

	static Map<String, ProcessorType> nameMap = new HashMap<>();


	@BeforeEach
	void setUp() {
		nameMap.put("COMMAND_SENDER", ProcessorType.COMMAND_SENDER);
		nameMap.put("DURATION", ProcessorType.DURATION);
		nameMap.put("ITEM_STACK", ProcessorType.ITEM_STACK);
		nameMap.put("LOCATION", ProcessorType.LOCATION);
		nameMap.put("NULL", ProcessorType.NULL);
		nameMap.put("NUMBER", ProcessorType.NUMBER);
		nameMap.put("OBJECT", ProcessorType.OBJECT);
		nameMap.put("OFFLINE_PLAYER", ProcessorType.OFFLINE_PLAYER);
		nameMap.put("STRING", ProcessorType.STRING);
		nameMap.put("WORLD",ProcessorType.WORLD);
	}


	@AfterEach
	void tearDown() {
		commandSenderMock = null;
		offlinePlayer = null;
		worldMock = null;
		locationMock = null;
	}


	@Test
	void matchType_command_sender() {
		assertEquals(ProcessorType.COMMAND_SENDER, ProcessorType.matchType(commandSenderMock));
	}

	@Test
	void matchType_duration() {
		assertEquals(ProcessorType.DURATION, ProcessorType.matchType(Duration.ofMillis(2000)));
	}

	@Test
	void matchType_item_stack() {
		assertEquals(ProcessorType.ITEM_STACK, ProcessorType.matchType(new ItemStack(Material.STONE)));
	}

	@Test
	void matchType_location() {
		assertEquals(ProcessorType.LOCATION, ProcessorType.matchType(locationMock));
	}

	@Test
	void matchType_null() {
		assertEquals(ProcessorType.NULL, ProcessorType.matchType(null));
	}

	@Test
	void matchType_number() {
		assertEquals(ProcessorType.NUMBER, ProcessorType.matchType(42));
	}

	@Test
	void matchType_object() {
		assertEquals(ProcessorType.OBJECT, ProcessorType.matchType(new Object()));
	}

	@Test
	void matchType_offline_player() {
		assertEquals(ProcessorType.OFFLINE_PLAYER, ProcessorType.matchType(offlinePlayer));
	}

	@Test
	void matchType_string() {
		assertEquals(ProcessorType.STRING, ProcessorType.matchType("a string"));
	}

	@Test
	void matchType_world() {
		assertEquals(ProcessorType.WORLD, ProcessorType.matchType(worldMock));
	}

	@ParameterizedTest
	@EnumSource
	void testValues(ProcessorType processorType) {
		assertTrue(Arrays.stream(ProcessorType.values()).toList().contains(processorType));
	}

	@ParameterizedTest
	@ValueSource(strings = { "COMMAND_SENDER", "DURATION", "ITEM_STACK", "LOCATION", "NULL", "NUMBER", "OBJECT", "OFFLINE_PLAYER", "STRING", "WORLD" } )
	void testValueCreate(String name) {
		assertEquals(nameMap.get(name), ProcessorType.valueOf(name));
	}


/* test below were added from another test class */

	@Test
	void matchTypeCommandSender() {
		CommandSender consoleSender = mock(ConsoleCommandSender.class);
		assertNotNull(consoleSender, "Console sender is null.");
		assertEquals(ProcessorType.COMMAND_SENDER, ProcessorType.matchType(consoleSender));
	}

	@Test
	void matchTypeDuration() {
		Duration duration = Duration.ofMillis(2000);
		assertEquals(ProcessorType.DURATION, ProcessorType.matchType(duration));
	}

	@Test
	void matchTypeItemStack() {
		ItemStack itemStack = mock(ItemStack.class);
		assertNotNull(itemStack, "ItemStack is null.");
		assertEquals(ProcessorType.ITEM_STACK, ProcessorType.matchType(itemStack));
	}

	@Test
	void matchTypeLocation() {
		World world = mock(World.class);
		assertNotNull(world);
		Location location = new Location(world,10,20,30);
		assertNotNull(location);
		assertEquals(ProcessorType.LOCATION, ProcessorType.matchType(location));
	}

	@Test
	void matchTypeNull() {
		assertEquals(ProcessorType.NULL, ProcessorType.matchType(null));
	}

	@Test
	void matchTypeNumber() {
		assertEquals(ProcessorType.NUMBER, ProcessorType.matchType(42));
		assertEquals(ProcessorType.NUMBER, ProcessorType.matchType(420L));
		assertEquals(ProcessorType.NUMBER, ProcessorType.matchType(3.14));
	}

	@Test
	void matchTypeObject() {
		assertEquals(ProcessorType.OBJECT, ProcessorType.matchType(new Object()));
	}

	@Test
	void matchTypeOfflinePlayer() {
		OfflinePlayer offlinePlayer = mock(OfflinePlayer.class);
		assertNotNull(offlinePlayer);
		assertEquals(ProcessorType.OFFLINE_PLAYER, ProcessorType.matchType(offlinePlayer));
	}

	@Test
	void matchTypeString() {
		String string = "string";
		assertEquals(ProcessorType.STRING, ProcessorType.matchType(string), "String does not match processor type STRING");
	}

	@Test
	void matchTypeWorld() {
		World world = mock(World.class);
		assertNotNull(world);
		assertEquals(ProcessorType.WORLD, ProcessorType.matchType(world));
	}

	@Test
	void testCreate() {
		DependencyContext ctx = new DependencyContext();
		assertInstanceOf(MacroProcessor.class, ProcessorType.WORLD.create(ctx));

	}

}
