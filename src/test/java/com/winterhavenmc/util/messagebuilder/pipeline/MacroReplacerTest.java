/*
 * Copyright (c) 2024-2025 Tim Savage.
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

package com.winterhavenmc.util.messagebuilder.pipeline;

import com.winterhavenmc.util.messagebuilder.Message;

import com.winterhavenmc.util.messagebuilder.messages.MessageId;
import com.winterhavenmc.util.messagebuilder.pipeline.processors.ResultMap;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.MessageRecord;
import com.winterhavenmc.util.messagebuilder.validation.ValidationException;

import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.Optional;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;


@ExtendWith(MockitoExtension.class)
class MacroReplacerTest
{
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


	@Nested
	class ReplaceMacrosTests
	{
		@Test
		@DisplayName("Test replaceMacros method with valid parameter")
		void testReplaceMacros_valid_parameters()
		{
			// Arrange
			MessageRecord messageRecord = new MessageRecord(
					MessageId.ENABLED_MESSAGE.name(),
					true,
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
		@DisplayName("Test replaceMacros method with null messageRecord parameter")
		void testReplaceMacros_parameter_null_messageRecord()
		{
			ValidationException exception = assertThrows(ValidationException.class,
					() -> macroReplacer.replaceMacros(null, message));

			assertEquals("The parameter 'messageRecord' cannot be null.", exception.getMessage());
		}


		@Test
		@DisplayName("Test replaceMacros method with null contextMap parameter")
		void testReplaceMacros_parameter_null_contextMap()
		{
			// Arrange
			MessageRecord messageRecord = new MessageRecord(
					MessageId.ENABLED_MESSAGE.name(),
					true,
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
			ValidationException exception = assertThrows(ValidationException.class,
					() -> macroReplacer.replaceMacros(messageRecord, null));

			// Assert
			assertEquals("The parameter 'message' cannot be null.", exception.getMessage());
		}
	}


	@Nested
	class ReplaceMacrosInStringTests
	{
		@Test @DisplayName("Test replaceMacrosInString method with valid parameters")
		void testReplaceMacrosInString()
		{
			ContextMap contextMap = new ContextMap(playerMock, MessageId.ENABLED_MESSAGE.name());
			MacroReplacer macroReplacer = new MacroReplacer();
			String key = "ITEM_NAME";
			contextMap.put(key, "TEST_STRING");

			String resultString = macroReplacer.replaceMacrosInString(contextMap, "Replace this: {ITEM_NAME}");
			assertEquals("Replace this: TEST_STRING", resultString);
		}


		@Test @DisplayName("Test replaceMacrosInString method with null contextMap parameter")
		void testReplaceMacrosInString_parameter_null_contextMap()
		{
			// Arrange & Act
			ValidationException exception = assertThrows(ValidationException.class,
					() -> macroReplacer.replaceMacrosInString(null, "Some message string."));

			// Assert
			assertEquals("The parameter 'contextMap' cannot be null.", exception.getMessage());
		}


		@Test @DisplayName("Test replaceMacrosInString method with null messageString parameter")
		void testReplaceMacrosInString_parameter_null_messageString()
		{
			// Arrange
			ContextMap contextMap = new ContextMap(playerMock, MessageId.ENABLED_MESSAGE.name());

			// Act
			ValidationException exception = assertThrows(ValidationException.class,
					() -> macroReplacer.replaceMacrosInString(contextMap, null));

			// Assert
			assertEquals("The parameter 'messageString' cannot be null.", exception.getMessage());
		}
	}


	@Test
	void testAddRecipientContext() {
		// Arrange
		ContextMap contextMap = new ContextMap(playerMock, MessageId.ENABLED_MESSAGE.name());

		// Act
		macroReplacer.addRecipientContext(contextMap);

		// Assert
		assertTrue(contextMap.contains("RECIPIENT.LOCATION"));
	}


	@Test
	void testAddRecipientContext_parameter_null_context_map() {
		// Arrange & Act
		ValidationException exception = assertThrows(ValidationException.class,
				() -> macroReplacer.addRecipientContext(null));

		// Assert
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
		ValidationException exception = assertThrows(ValidationException.class,
				() -> macroReplacer.performReplacements(null, "some string"));

		assertEquals("The parameter 'replacementMap' cannot be null.", exception.getMessage());
	}

	@Test
	void testPerformReplacements_parameter_nul_message_string() {
		ResultMap resultMap = new ResultMap();
		resultMap.put("KEY1", "value1");
		resultMap.put("KEY2", "value2");
		resultMap.put("KEY3", "value3");

		ValidationException exception = assertThrows(ValidationException.class,
				() -> macroReplacer.performReplacements(resultMap, null));

		assertEquals("The parameter 'messageString' cannot be null.", exception.getMessage());
	}


	@Test
	void addRecipientContext() {
		ConsoleCommandSender console = mock(ConsoleCommandSender.class);
		ContextMap contextMap = new ContextMap(console, MessageId.ENABLED_MESSAGE.name());

		macroReplacer.addRecipientContext(contextMap);
		assertTrue(contextMap.contains("RECIPIENT"));
	}

}
