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

package com.winterhavenmc.util.messagebuilder;

import com.winterhavenmc.util.messagebuilder.messages.Macro;

import org.bukkit.inventory.ItemStack;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.bukkit.Material.GOLDEN_PICKAXE;


@ExtendWith(MockitoExtension.class)
class InvalidMessageTest
{
	InvalidMessage invalidMessage;

	@BeforeEach
	void setUp()
	{
		invalidMessage = new InvalidMessage("fail");
	}

	@Test
	void testSetMacro()
	{
		Message newMessage = invalidMessage.setMacro(Macro.TOOL, new ItemStack(GOLDEN_PICKAXE));
		assertNotNull(newMessage);
	}

	@Test
	void testSetMacro2()
	{
		Message newMessage = invalidMessage.setMacro(2, Macro.TOOL, new ItemStack(GOLDEN_PICKAXE));
		assertNotNull(newMessage);
	}

	@Test
	void send()
	{
		assertDoesNotThrow(() -> invalidMessage.send());
	}

	@Test
	void getMessageKey()
	{
		assertNull(invalidMessage.getMessageKey());
	}

	@Test
	void getRecipient()
	{
		assertNull(invalidMessage.getRecipient());
	}

	@Test
	void getContextMap()
	{
		assertNull(invalidMessage.getContextMap());
	}

	@Test
	void reason()
	{
		assertEquals("fail", invalidMessage.reason());
	}

}
