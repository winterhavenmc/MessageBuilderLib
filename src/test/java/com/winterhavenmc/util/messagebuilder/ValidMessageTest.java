/*
 * Copyright (c) 2022-2025 Tim Savage.
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

import com.winterhavenmc.util.messagebuilder.recipient.InvalidRecipient;
import com.winterhavenmc.util.messagebuilder.recipient.RecipientResult;
import com.winterhavenmc.util.messagebuilder.recipient.ValidRecipient;
import com.winterhavenmc.util.messagebuilder.recordkey.RecordKey;
import com.winterhavenmc.util.messagebuilder.messages.Macro;
import com.winterhavenmc.util.messagebuilder.pipeline.context.ContextMap;
import com.winterhavenmc.util.messagebuilder.pipeline.processor.MessageProcessor;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.MessageRecord;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.ValidMessageRecord;
import com.winterhavenmc.util.messagebuilder.validation.ValidationException;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.winterhavenmc.util.messagebuilder.messages.MessageId.ENABLED_MESSAGE;
import static com.winterhavenmc.util.messagebuilder.validation.ErrorMessageKey.PARAMETER_INVALID;
import static com.winterhavenmc.util.messagebuilder.validation.Parameter.RECIPIENT;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class ValidMessageTest
{
	// declare mocks
	@Mock Player playerMock;
	@Mock MessageProcessor messageProcessorMock;

	// declare real objects
	FileConfiguration pluginConfiguration;
	Message message;
	ItemStack itemStack;
	ValidMessageRecord validMessageRecord;
	ValidRecipient recipient;
	RecordKey messageKey;
	ConfigurationSection section;


	@BeforeEach
	public void setUp()
	{
		pluginConfiguration = new YamlConfiguration();
		pluginConfiguration.set("language", "en-US");
		pluginConfiguration.set("locale", "en-US");
		pluginConfiguration.set("titles-enabled", true);

		itemStack = new ItemStack(Material.DIAMOND_SWORD);

		recipient = switch (RecipientResult.from(playerMock)) {
			case ValidRecipient validRecipient -> validRecipient;
			case InvalidRecipient ignored -> throw new ValidationException(PARAMETER_INVALID, RECIPIENT);
		};

		messageKey = RecordKey.of(ENABLED_MESSAGE).orElseThrow();

		message = new ValidMessage(recipient, messageKey, messageProcessorMock);

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
	}


	@Test @DisplayName("test recipient accessor")
	void testGetRecipient()
	{
		assertEquals(recipient, message.getRecipient());
	}


	@Test @DisplayName("test messageKey accessor")
	void testGetMessageKey()
	{
		// Act
		RecordKey messageKey = message.getMessageKey();

		// Assert
		assertEquals(ENABLED_MESSAGE.name(), messageKey.toString());
	}


	@Test @DisplayName("test contextMap accessor")
	void testGetContextMap()
	{
		assertNotNull(message.getContextMap());
	}


	@Nested
	class SetMacroTests
	{
		@Test @DisplayName("test setMacro (two parameter) method with valid parameters")
		void testSetMacro()
		{
			// Arrange
			Message newMessage = message.setMacro(Macro.TOOL, itemStack);
			RecordKey macroKey = RecordKey.of(Macro.TOOL).orElseThrow();
			ContextMap contextMap = newMessage.getContextMap();

			// Act
			Object object = contextMap.get(macroKey).orElseThrow();

			// Assert
			assertInstanceOf(ItemStack.class, object);
			assertEquals(itemStack, object);

		}


		@Test  @DisplayName("test setMacro method with null macro")
		// Act
		void testSetMacro_parameter_null_macro()
		{
			ValidationException exception = assertThrows(ValidationException.class,
					() -> message.setMacro(null, new ItemStack(Material.DIAMOND_SWORD)));

			// Assert
			assertEquals("The parameter 'macro' cannot be null.", exception.getMessage());
		}


		@Test @DisplayName("test setMacro method with null value")
		@Disabled("null values allowed")
		void testSetMacro_parameter_null_value()
		{
			ValidationException exception = assertThrows(ValidationException.class,
					() -> message.setMacro(Macro.OWNER, null));

			assertEquals("The parameter 'value' cannot be null.", exception.getMessage());
		}
	}


	@Nested
	class SetMacro2Tests
	{
		@Test @DisplayName("test setMacro (three parameter) method with valid parameters")
		void testSetMacro2()
		{
			// Arrange
			Message newMessage = message.setMacro(10, Macro.TOOL, itemStack);
			RecordKey macroKey = RecordKey.of(Macro.TOOL).orElseThrow();
			ContextMap contextMap = newMessage.getContextMap();

			// Act
			Object object = contextMap.get(macroKey).orElseThrow();

			// Assert
			assertInstanceOf(ItemStack.class, object);
			assertEquals(itemStack, object);
		}


		@Test @DisplayName("test setMacro (three parameter) method with null macro parameter")
		void testSetMacro2_parameter_null_macro()
		{
			ValidationException exception = assertThrows(ValidationException.class,
					() -> message.setMacro(5, null, new ItemStack(Material.DIAMOND_SWORD)));

			assertEquals("The parameter 'macro' cannot be null.", exception.getMessage());
		}


		@Test
		@Disabled("null values allowed")
		void testSetMacro2_parameter_null_object()
		{
			ValidationException exception = assertThrows(ValidationException.class,
					() -> message.setMacro(6, Macro.OWNER, null));

			assertEquals("The parameter 'value' cannot be null.", exception.getMessage());
		}
	}


	@Test
	void testSend()
	{
		assertDoesNotThrow(() -> message.send());
	}


	@Test
	void emptyMessage_shouldNotBeNull()
	{
		Message empty = Message.empty();
		assertNotNull(empty, "empty() should not return null");
	}

	@Test
	void emptyMessage_shouldBeInstanceOfMessage()
	{
		Message empty = Message.empty();
		assertInstanceOf(Message.class, empty, "empty() should return a Message instance");
	}

	@Test
	void emptyMessage_setMacroShouldReturnSameInstance()
	{
		Message empty = Message.empty();
		Message result = empty.setMacro(DummyMacro.TEST, "value");
		assertSame(empty, result, "setMacro() on empty message should return the same instance");
	}

	@Test
	void testEmptyMessage_send()
	{
		// Arrange
		Message emptyMessage = Message.empty();

		// Act & Assert
		assertDoesNotThrow(emptyMessage::send);
	}

	/**
	 * Dummy enum for macro test – replace with your actual Macro enum if available.
	 */
	private enum DummyMacro
	{
		TEST
	}

}
