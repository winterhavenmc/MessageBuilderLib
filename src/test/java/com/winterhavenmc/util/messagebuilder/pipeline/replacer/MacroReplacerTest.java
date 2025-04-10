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

package com.winterhavenmc.util.messagebuilder.pipeline.replacer;

import com.winterhavenmc.util.messagebuilder.Message;
import com.winterhavenmc.util.messagebuilder.ValidMessage;
import com.winterhavenmc.util.messagebuilder.messages.Macro;
import com.winterhavenmc.util.messagebuilder.messages.MessageId;
import com.winterhavenmc.util.messagebuilder.pipeline.context.ContextMap;
import com.winterhavenmc.util.messagebuilder.pipeline.processor.MessageProcessor;
import com.winterhavenmc.util.messagebuilder.pipeline.processors.ResultMap;
import com.winterhavenmc.util.messagebuilder.recipient.InvalidRecipient;
import com.winterhavenmc.util.messagebuilder.recipient.RecipientResult;
import com.winterhavenmc.util.messagebuilder.recipient.ValidRecipient;
import com.winterhavenmc.util.messagebuilder.resources.RecordKey;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.FinalMessageRecord;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.MessageRecord;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.ValidMessageRecord;
import com.winterhavenmc.util.messagebuilder.validation.ValidationException;

import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.entity.Player;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.winterhavenmc.util.messagebuilder.messages.MessageId.ENABLED_MESSAGE;
import static com.winterhavenmc.util.messagebuilder.validation.ErrorMessageKey.PARAMETER_INVALID;
import static com.winterhavenmc.util.messagebuilder.validation.Parameter.RECIPIENT;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;


@ExtendWith(MockitoExtension.class)
class MacroReplacerTest
{
	@Mock Player playerMock;
	@Mock MessageProcessor messageProcessorMock;

	ValidRecipient recipient;
	RecordKey messageKey;
	RecordKey macroKey;
	MacroReplacer macroReplacer;
	Message message;
	ConfigurationSection section;
	ValidMessageRecord validMessageRecord;
	FinalMessageRecord finalMessageRecord;


	@BeforeEach
	public void setUp()
	{
		recipient = switch (RecipientResult.from(playerMock)) {
			case ValidRecipient validRecipient -> validRecipient;
			case InvalidRecipient ignored -> throw new ValidationException(PARAMETER_INVALID, RECIPIENT);
		};
		messageKey = RecordKey.of(MessageId.ENABLED_MESSAGE).orElseThrow();
		macroKey = RecordKey.of(Macro.OWNER).orElseThrow();

		message = new ValidMessage(recipient, messageKey, messageProcessorMock);
		macroReplacer = new MacroReplacer();

		messageKey = RecordKey.of(ENABLED_MESSAGE).orElseThrow();

		section = new MemoryConfiguration();
		section.set(MessageRecord.Field.ENABLED.toKey(), true);
		section.set(MessageRecord.Field.MESSAGE_TEXT.toKey(), "this is a test message");
		section.set(MessageRecord.Field.REPEAT_DELAY.toKey(), 11);
		section.set(MessageRecord.Field.TITLE_TEXT.toKey(), "this is a test title");
		section.set(MessageRecord.Field.TITLE_FADE_IN.toKey(), 22);
		section.set(MessageRecord.Field.TITLE_STAY.toKey(), 33);
		section.set(MessageRecord.Field.TITLE_FADE_OUT.toKey(), 44);
		section.set(MessageRecord.Field.SUBTITLE_TEXT.toKey(), "this is a test subtitle");

		validMessageRecord = ValidMessageRecord.create(messageKey, section);

		finalMessageRecord = validMessageRecord.withFinalStrings(
				"this is a final message",
				"this is a final title",
				"this is a final subtitle");
	}


	@Test @DisplayName("Test replaceMacros method with Valid parameter")
	void testReplaceMacros_valid_parameters()
	{
		// Act
		FinalMessageRecord result = macroReplacer.replaceMacros(validMessageRecord, message.getContextMap());

		// Assert
		assertNotNull(result);
	}


	@Nested
	class ReplaceMacrosInStringTests
	{
		@Test @DisplayName("Test replaceMacrosInString method with Valid parameters")
		void testReplaceMacrosInString()
		{
			ContextMap contextMap = ContextMap.of(recipient, messageKey).orElseThrow();
			MacroReplacer macroReplacer = new MacroReplacer();
			RecordKey key = RecordKey.of("ITEM_NAME").orElseThrow();
			contextMap.put(key, "TEST_STRING");

			String resultString = macroReplacer.replaceMacrosInString(contextMap, "Replace this: {ITEM_NAME}");
			assertEquals("Replace this: TEST_STRING", resultString);
		}


		@Test @DisplayName("Test replaceMacrosInString method with null messageString parameter")
		void testReplaceMacrosInString_parameter_null_messageString()
		{
			// Arrange
			ContextMap contextMap = ContextMap.of(recipient, messageKey).orElseThrow();

			// Act
			ValidationException exception = assertThrows(ValidationException.class,
					() -> macroReplacer.replaceMacrosInString(contextMap, null));

			// Assert
			assertEquals("The parameter 'messageString' cannot be null.", exception.getMessage());
		}
	}


	@Test
	void testAddRecipientContext()
	{
		// Arrange
		ContextMap contextMap = ContextMap.of(recipient, messageKey).orElseThrow();
		RecordKey recipientKey = RecordKey.of("RECIPIENT").orElseThrow();

		// Act
		macroReplacer.addRecipientContext(contextMap);

		// Assert
		assertTrue(contextMap.contains(recipientKey));
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
	void addRecipientContext()
	{
		ConsoleCommandSender console = mock(ConsoleCommandSender.class);
		recipient = switch (RecipientResult.from(console)) {
			case ValidRecipient validRecipient -> validRecipient;
			case InvalidRecipient ignored -> throw new ValidationException(PARAMETER_INVALID, RECIPIENT);
		};
		RecordKey key = RecordKey.of("RECIPIENT").orElseThrow();
		ContextMap contextMap = ContextMap.of(recipient, key).orElseThrow();

		macroReplacer.addRecipientContext(contextMap);
		assertTrue(contextMap.contains(key));
	}

}
