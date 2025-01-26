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

import com.winterhavenmc.util.messagebuilder.macro.processor.ResultMap;
import com.winterhavenmc.util.messagebuilder.util.LocalizedException;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class MacroReplacerTest {

	@Mock Player playerMock;
	MacroReplacer macroReplacer;


	@BeforeEach
	public void setUp() {
		macroReplacer = new MacroReplacer();
	}

	@AfterEach
	public void tearDown() {
		playerMock = null;
		macroReplacer = null;
	}


	@Nested
	class ReplaceMacrosTests {

		@Test
		void testReplaceMacros() {
			ContextMap contextMap = new ContextMap(playerMock);
			MacroReplacer macroReplacer = new MacroReplacer();
			String key = "ITEM_NAME";
			contextMap.put(key, "TEST_STRING");

			String resultString = macroReplacer.replaceMacros(playerMock, contextMap, "Replace this: {ITEM_NAME}");
			assertEquals("Replace this: TEST_STRING", resultString);
		}

		@Test
		void testReplaceMacros_parameter_null_recipient() {
			// Arrange
			ContextMap contextMap = new ContextMap(playerMock);

			// Act
			LocalizedException exception = assertThrows(LocalizedException.class,
					() -> macroReplacer.replaceMacros(null, contextMap, "Some message string."));

			// Assert
			assertEquals("The parameter 'recipient' cannot be null.", exception.getMessage());
		}


		@Test
		void testReplaceMacros_parameter_null_context_map() {
			// Arrange & Act
			LocalizedException exception = assertThrows(LocalizedException.class,
					() -> macroReplacer.replaceMacros(playerMock, null, "Some message string."));

			// Assert
			assertEquals("The parameter 'contextMap' cannot be null.", exception.getMessage());
		}


		@Test
		void testReplaceMacros_parameter_null_messageString() {
			// Arrange
			ContextMap contextMap = new ContextMap(playerMock);

			// Act
			LocalizedException exception = assertThrows(LocalizedException.class,
					() -> macroReplacer.replaceMacros(playerMock, contextMap, null));

			// Assert
			assertEquals("The parameter 'messageString' cannot be null.", exception.getMessage());
		}
	}


	@Nested
	class AddRecipientContextTests {

		@Test
		void testAddRecipientContext() {
			// Arrange
			ContextMap contextMap = new ContextMap(playerMock);

			// Act
			macroReplacer.addRecipientContext(playerMock, contextMap);

			// Assert
			assertTrue(contextMap.containsKey("RECIPIENT.LOCATION"));
		}

		@Test
		void testAddRecipientContext_parameter_null_recipient() {
			// Arrange
			ContextMap contextMap = new ContextMap(playerMock);

			// Act
			LocalizedException exception = assertThrows(LocalizedException.class,
					() -> macroReplacer.addRecipientContext(null, contextMap));

			// Assert
			assertEquals("The parameter 'recipient' cannot be null.", exception.getMessage());
		}

		@Test
		void testAddRecipientContext_parameter_null_context_map() {
			// Arrange & Act
			LocalizedException exception = assertThrows(LocalizedException.class,
					() -> macroReplacer.addRecipientContext(playerMock, null));

			// Assert
			assertEquals("The parameter 'contextMap' cannot be null.", exception.getMessage());
		}
	}

	@Nested
	class ConvertValuesToStringsTests {

		@Test
		void testConvertValuesToStrings() {
			// Arrange
			ItemStack itemStack = new ItemStack(Material.STONE);
			ContextMap contextMap = new ContextMap(playerMock);
			contextMap.put("NUMBER", 42);
			contextMap.put("ITEM_STACK", itemStack);

			// Act
			ResultMap resultMap = macroReplacer.convertValuesToStrings(contextMap);

			// Assert
			assertTrue(resultMap.containsKey("NUMBER"));
			assertEquals("42", resultMap.get("NUMBER"));
			assertTrue(resultMap.containsKey("ITEM_STACK"));
			assertEquals("STONE", resultMap.get("ITEM_STACK"));
		}

		@Test
		void testConvertValuesToStrings_parameter_null_context_map() {
			LocalizedException exception = assertThrows(LocalizedException.class,
					() -> macroReplacer.convertValuesToStrings(null));

			assertEquals("The parameter 'contextMap' cannot be null.", exception.getMessage());
		}
	}


	@Nested
	class PerformReplacementsTests {
		@Test
		void testPerformReplacements() {
			// Arrange
			MacroReplacer localMacroReplacer = new MacroReplacer();

			ResultMap resultMap = new ResultMap();
			resultMap.put("KEY", "value");
			String messageString = "this is a macro replacement string {KEY}.";

			// Act
			String resultString = localMacroReplacer.performReplacements(resultMap, messageString);

			// Assert
			assertEquals("this is a macro replacement string value.", resultString);
			System.out.println("Output: " + resultString);
		}

		@Test
		void testPerformReplacements_parameter_nul_replacement_map() {
			LocalizedException exception = assertThrows(LocalizedException.class,
					() -> macroReplacer.performReplacements(null, "some string"));

			assertEquals("The parameter 'replacementMap' cannot be null.", exception.getMessage());
		}

		@Test
		void testPerformReplacements_parameter_nul_message_string() {
			ResultMap resultMap = new ResultMap();
			resultMap.put("KEY1", "value1");
			resultMap.put("KEY2", "value2");
			resultMap.put("KEY3", "value3");

			LocalizedException exception = assertThrows(LocalizedException.class,
					() -> macroReplacer.performReplacements(resultMap, null));

			assertEquals("The parameter 'messageString' cannot be null.", exception.getMessage());
		}
	}

}
