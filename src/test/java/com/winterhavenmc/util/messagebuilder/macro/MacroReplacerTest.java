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

import com.winterhavenmc.util.messagebuilder.Message;
import com.winterhavenmc.util.messagebuilder.context.ContextMap;

import com.winterhavenmc.util.messagebuilder.macro.processor.ResultMap;
import com.winterhavenmc.util.messagebuilder.messages.Macro;
import com.winterhavenmc.util.messagebuilder.messages.MessageId;
import com.winterhavenmc.util.messagebuilder.pipeline.MessageProcessor;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.messages.MessageRecord;
import com.winterhavenmc.util.messagebuilder.util.LocalizedException;

import org.bukkit.Material;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;


@ExtendWith(MockitoExtension.class)
class MacroReplacerTest {

	@Mock Player playerMock;
	@Mock MessageProcessor messageProcessorMock;
	MacroReplacer macroReplacer;

	Message message;

	@BeforeEach
	public void setUp() {
		message = new Message(playerMock, MessageId.ENABLED_MESSAGE.name(), messageProcessorMock);
		macroReplacer = new MacroReplacer();
	}

	@AfterEach
	public void tearDown() {
		playerMock = null;
		macroReplacer = null;
	}


	@Test
	void testReplaceMacros1_valid_parameters() {
		// Arrange
		MessageRecord messageRecord = new MessageRecord(
				MessageId.ENABLED_MESSAGE.name(),
				true,
				false,
				"key",
				List.of("arg1", "arg2"),
				"this is a message.",
				Duration.ofSeconds(3),
				"this is a title.",
				20,
				40,
				30,
				"this is a subtitle.",
				"this is a final message string",
				"this is a final title string",
				"this is a final subtitle string"
		);

		// Act
		Optional<MessageRecord> result = macroReplacer.replaceMacros(messageRecord, message);

		// Assert
		assertNotNull(result);
	}

	@Test
	void testReplaceMacros1_parameter_null_messageRecord() {
		LocalizedException exception = assertThrows(LocalizedException.class,
				() -> macroReplacer.replaceMacros(null, message));

		assertEquals("The parameter 'messageRecord' cannot be null.", exception.getMessage());
	}

	@Test
	void testReplaceMacros1_parameter_null_contextMap() {
		// Arrange
		MessageRecord messageRecord = new MessageRecord(
				MessageId.ENABLED_MESSAGE.name(),
				true,
				false,
				"key",
				List.of("arg1", "arg2"),
				"this is a message.",
				Duration.ofSeconds(3),
				"this is a title.",
				20,
				40,
				30,
				"this is a subtitle.",
				"this is a final message string",
				"this is a final title string",
				"this is a final subtitle string"
		);

		// Act
		LocalizedException exception = assertThrows(LocalizedException.class,
				() -> macroReplacer.replaceMacros(messageRecord, null));

		// Assert
		assertEquals("The parameter 'contextMap' cannot be null.", exception.getMessage());
	}


	@Test
	void testReplaceMacros2() {
		ContextMap contextMap = new ContextMap(playerMock, MessageId.ENABLED_MESSAGE.name());
		MacroReplacer macroReplacer = new MacroReplacer();
		String key = "ITEM_NAME";
		contextMap.put(key, "TEST_STRING");

		String resultString = macroReplacer.replaceMacros(contextMap, "Replace this: {ITEM_NAME}");
		assertEquals("Replace this: TEST_STRING", resultString);
	}

	@Test
	void testReplaceMacros2_parameter_null_contextMap() {
		// Arrange & Act
		LocalizedException exception = assertThrows(LocalizedException.class,
				() -> macroReplacer.replaceMacros(null, "Some message string."));

		// Assert
		assertEquals("The parameter 'contextMap' cannot be null.", exception.getMessage());
	}


	@Test
	void testReplaceMacros2_parameter_null_messageString() {
		// Arrange
		ContextMap contextMap = new ContextMap(playerMock, MessageId.ENABLED_MESSAGE.name());

		// Act
		LocalizedException exception = assertThrows(LocalizedException.class,
				() -> macroReplacer.replaceMacros(contextMap, null));

		// Assert
		assertEquals("The parameter 'messageString' cannot be null.", exception.getMessage());
	}


	@Test
	void testAddRecipientContext() {
		// Arrange
		ContextMap contextMap = new ContextMap(playerMock, MessageId.ENABLED_MESSAGE.name());

		// Act
		macroReplacer.addRecipientContext(contextMap);

		// Assert
		assertTrue(contextMap.containsKey("RECIPIENT.LOCATION"));
	}

	@Test
	void testAddRecipientContext_parameter_null_context_map() {
		// Arrange & Act
		LocalizedException exception = assertThrows(LocalizedException.class,
				() -> macroReplacer.addRecipientContext(null));

		// Assert
		assertEquals("The parameter 'contextMap' cannot be null.", exception.getMessage());
	}




	@Test
	void testResolveContext() {
		// Arrange
		ItemStack itemStack = new ItemStack(Material.STONE);
		ContextMap contextMap = new ContextMap(playerMock, MessageId.ENABLED_MESSAGE.name());
		contextMap.put("NUMBER", 42);
		contextMap.put("ITEM_STACK", itemStack);

		// Act
		ResultMap resultMap = macroReplacer.resolveContext(contextMap);

		// Assert
		assertTrue(resultMap.containsKey("NUMBER"));
		assertEquals("42", resultMap.get("NUMBER"));
		assertTrue(resultMap.containsKey("ITEM_STACK"));
		assertEquals("STONE", resultMap.get("ITEM_STACK"));
	}

	@Test
	void testResolveContext_parameter_null_context_map() {
		LocalizedException exception = assertThrows(LocalizedException.class,
				() -> macroReplacer.resolveContext(null));

		assertEquals("The parameter 'contextMap' cannot be null.", exception.getMessage());
	}



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

	@Test
	void testContainsMacros() {
		assertTrue(macroReplacer.containsMacros("This is a string that {CONTAINS} a macro."));
		assertFalse(macroReplacer.containsMacros("This is a string that does not contain a macro."));
	}

	@Test
	void addRecipientContext() {
		ConsoleCommandSender console = mock(ConsoleCommandSender.class);
		ContextMap contextMap = new ContextMap(console, MessageId.ENABLED_MESSAGE.name());

		macroReplacer.addRecipientContext(contextMap);
		assertTrue(contextMap.containsKey("RECIPIENT"));
		//TODO: fix ths:
		// assertEquals("console", contextMap.get("RECIPIENT"));
	}

	@Test
	void testGetPlaceholderStream() {
		// Arrange
		String messageString = "This is a {MESSAGE} with {SEVERAL} {PLACE_HOLDERS}.";
		Stream<String> placeholderStream = macroReplacer.getPlaceholderStream(messageString);
		Set<String> placeholderSet = placeholderStream.collect(Collectors.toSet());

		assertFalse(placeholderSet.isEmpty());
		assertTrue(placeholderSet.contains("SEVERAL"));
		assertTrue(placeholderSet.contains("MESSAGE"));
		assertTrue(placeholderSet.contains("PLACE_HOLDERS"));
		assertFalse(placeholderSet.contains("This"));
	}

	@Test
	void getMatcher() {
		// Arrange
		String messageString = "This is a {MESSAGE} with {SEVERAL} {PLACE_HOLDERS}.";

		// Act
		Matcher matcher = MacroReplacer.getMatcher(messageString);

		// Assert
		assertTrue(matcher.find());
	}
}
