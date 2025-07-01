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

package com.winterhavenmc.library.messagebuilder.macro;

import com.winterhavenmc.library.messagebuilder.macro.processor.ProcessorType;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class ProcessorTypeTest
{
	@Mock Player playerMock;
	@Mock World worldMock;
	@Mock OfflinePlayer offlinePlayerMock;
	@Mock ItemStack itemStackMock;
	@Mock ConsoleCommandSender consoleCommandSenderMock;


	@Test
	void matchTypeString() {
		String string = "string";
		assertEquals(ProcessorType.STRING, ProcessorType.matchType(string), "String does not match processor type STRING");
	}

	@Disabled
	@Test
	void matchTypeEntity() {
		when(playerMock.getName()).thenReturn("player");
		assertEquals(ProcessorType.ENTITY, ProcessorType.matchType(playerMock), "Mock player does not match processor type ENTITY");
	}

	@Test
	void matchTypeCommandSender() {
		assertEquals(ProcessorType.COMMAND_SENDER, ProcessorType.matchType(consoleCommandSenderMock));
	}

	@Test
	void matchTypeNumber() {
		assertEquals(ProcessorType.NUMBER, ProcessorType.matchType(42));
		assertEquals(ProcessorType.NUMBER, ProcessorType.matchType(420L));
	}

	@Disabled
	@Test
	void matchTypeWorld() {
		assertEquals(ProcessorType.WORLD, ProcessorType.matchType(worldMock));
	}

	@Test
	void matchTypeOfflinePlayer() {
		assertEquals(ProcessorType.OFFLINE_PLAYER, ProcessorType.matchType(offlinePlayerMock));
	}

	@Disabled
	@Test
	void matchTypeItemStack() {
		assertEquals(ProcessorType.ITEM_STACK, ProcessorType.matchType(itemStackMock));
	}

	@Disabled
	@Test
	void matchTypeLocation() {
		Location location = new Location(worldMock,10,20,30);
		assertEquals(ProcessorType.LOCATION, ProcessorType.matchType(location));
	}

	@Test
	void matchTypeObject() {
		assertEquals(ProcessorType.OBJECT, ProcessorType.matchType(new Object()));
	}

}
