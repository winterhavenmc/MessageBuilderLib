/*
 * Copyright (c) 2024 Tim Savage.
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

import com.winterhavenmc.util.messagebuilder.context.ContextMap;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class MacroHandlerTest {

	@Mock Player playerMock;
	MacroHandler macroHandler;


	@BeforeEach
	public void setUp() {
		// real objects
		macroHandler = new MacroHandler();
	}

	@AfterEach
	public void tearDown() {
		playerMock = null;
		macroHandler = null;
	}


	@Test
	void testConstructor_parameter_valid() {
		// Arrange & Act
		macroHandler = new MacroHandler();

		// Assert
		assertNotNull(macroHandler);
	}


	@Disabled
	@Test
	void replaceMacrosTest() {
		ContextMap contextMap = new ContextMap(playerMock);
		String key = "ITEM_NAME";
		contextMap.put(key, "TEST_STRING");

		String resultString = macroHandler.replaceMacros(playerMock, contextMap, "Replace this: {ITEM_NAME}");
		assertEquals("Replace this: §aTest Item", resultString);
	}

	@Disabled
	@Test
	void replaceMacrosTest_item_already_in_map() {
		ContextMap contextMap = new ContextMap(playerMock);
		String key = "MACRO:My_Item";
		contextMap.put(key, "TEST_STRING");

		String resultString = macroHandler.replaceMacros(playerMock, contextMap, "Replace this: %ITEM_NAME%");
		assertEquals("Replace this: §aTest Item", resultString);
	}

	@Test
	void replaceMacrosTest_item_no_delimiter() {
		ContextMap contextMap = new ContextMap(playerMock);
		String resultString = macroHandler.replaceMacros(playerMock, contextMap, "Replace this: ITEM_NAME");
		assertEquals("Replace this: ITEM_NAME", resultString);
	}

	@Disabled
	@Test
	void replaceMacrosTest_recipient_is_entity() {
		Entity entityMock = mock(Entity.class);
		when(entityMock.getUniqueId()).thenReturn(new UUID(123,123));
		when(entityMock.getName()).thenReturn("entity one");

		ContextMap contextMap = new ContextMap(playerMock);
		contextMap.put("ENTITY", entityMock);

		String resultString = macroHandler.replaceMacros(playerMock, contextMap, "Replace this: {ENTITY}");

		assertEquals("Replace this: player1", resultString);
	}



}
