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

package com.winterhavenmc.util.messagebuilder.macro.processor;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
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

import javax.lang.model.type.NullType;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class ProcessorTypeTest {

	@Mock CommandSender commandSenderMock;
	@Mock Entity entityMock;
	@Mock OfflinePlayer offlinePlayer;
	@Mock World worldMock;
	@Mock Location locationMock;

	static Map<String, ProcessorType> nameMap = new HashMap<>();


	@BeforeEach
	void setUp() {
		nameMap.put("COMMAND_SENDER", ProcessorType.COMMAND_SENDER);
		nameMap.put("DURATION", ProcessorType.DURATION);
		nameMap.put("ENTITY", ProcessorType.ENTITY);
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
		entityMock = null;
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
	void matchType_entity() {
		assertEquals(ProcessorType.ENTITY, ProcessorType.matchType(entityMock));
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

	@Test
	void getExpectedType() {
		assertEquals(CommandSender.class, ProcessorType.COMMAND_SENDER.getExpectedType());
		assertEquals(Duration.class, ProcessorType.DURATION.getExpectedType());
		assertEquals(Entity.class, ProcessorType.ENTITY.getExpectedType());
		assertEquals(ItemStack.class, ProcessorType.ITEM_STACK.getExpectedType());
		assertEquals(Location.class, ProcessorType.LOCATION.getExpectedType());
		assertEquals(NullType.class, ProcessorType.NULL.getExpectedType());
		assertEquals(Number.class, ProcessorType.NUMBER.getExpectedType());
		assertEquals(Object.class, ProcessorType.OBJECT.getExpectedType());
		assertEquals(OfflinePlayer.class, ProcessorType.OFFLINE_PLAYER.getExpectedType());
		assertEquals(String.class, ProcessorType.STRING.getExpectedType());
		assertEquals(World.class, ProcessorType.WORLD.getExpectedType());
	}


	/**
	 * Test that ProcessorType.of() method returns an object that conforms to the
	 * MacroProcessor interface fore each constant.
	 */
	@ParameterizedTest
	@EnumSource
	void testCreate(ProcessorType processorType) {
		assertInstanceOf(MacroProcessor.class, ProcessorType.create(processorType));
	}


	@ParameterizedTest
	@EnumSource
	void testValues(ProcessorType processorType) {
		assertTrue(Arrays.stream(ProcessorType.values()).toList().contains(processorType));
	}

	@ParameterizedTest
	@ValueSource(strings = { "COMMAND_SENDER", "DURATION", "ENTITY", "ITEM_STACK", "LOCATION", "NULL", "NUMBER", "OBJECT", "OFFLINE_PLAYER", "STRING", "WORLD" } )
	void testValueCreate(String name) {
		assertEquals(nameMap.get(name), ProcessorType.valueOf(name));
	}

}
