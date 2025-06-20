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

package com.winterhavenmc.library.messagebuilder.model.message;

import com.winterhavenmc.library.messagebuilder.keys.MacroKey;
import com.winterhavenmc.library.messagebuilder.pipeline.formatters.duration.BoundedDuration;
import com.winterhavenmc.library.messagebuilder.model.recipient.Recipient;
import com.winterhavenmc.library.messagebuilder.keys.RecordKey;
import com.winterhavenmc.library.messagebuilder.messages.Macro;
import com.winterhavenmc.library.messagebuilder.pipeline.maps.MacroObjectMap;
import com.winterhavenmc.library.messagebuilder.pipeline.MessagePipeline;
import com.winterhavenmc.library.messagebuilder.model.language.MessageRecord;
import com.winterhavenmc.library.messagebuilder.model.language.ValidMessageRecord;
import com.winterhavenmc.library.messagebuilder.validation.ValidationException;

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

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import static com.winterhavenmc.library.messagebuilder.messages.MessageId.ENABLED_MESSAGE;
import static com.winterhavenmc.library.messagebuilder.validation.ErrorMessageKey.PARAMETER_INVALID;
import static com.winterhavenmc.library.messagebuilder.validation.Parameter.RECIPIENT;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class ValidMessageTest
{
	// declare mocks
	@Mock Player playerMock;
	@Mock MessagePipeline messagePipelineMock;

	// declare real objects
	FileConfiguration pluginConfiguration;
	Message message;
	ItemStack itemStack;
	ValidMessageRecord validMessageRecord;
	Recipient.Valid recipient;
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

		recipient = switch (Recipient.of(playerMock))
		{
			case Recipient.Valid valid -> valid;
			case Recipient.Proxied ignored -> throw new ValidationException(PARAMETER_INVALID, RECIPIENT);
			case Recipient.Invalid ignored -> throw new ValidationException(PARAMETER_INVALID, RECIPIENT);
		};

		messageKey = RecordKey.of(ENABLED_MESSAGE).orElseThrow();

		message = new ValidMessage(recipient, messageKey, messagePipelineMock);

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
	void testGetObjectMap()
	{
		assertNotNull(message.getObjectMap());
	}


	@Nested @DisplayName("Set Macro Tests (2 parameter)")
	class SetMacroTests
	{
		@Test @DisplayName("test setMacro (two parameter) method with valid parameters")
		void testSetMacro()
		{
			// Arrange
			Message newMessage = message.setMacro(Macro.TOOL, itemStack);
			MacroKey macroKey = MacroKey.of(Macro.TOOL).orElseThrow();
			MacroObjectMap macroObjectMap = newMessage.getObjectMap();

			// Act
			Object object = macroObjectMap.get(macroKey).orElseThrow();

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
			assertEquals("The parameter 'macroKey' was invalid.", exception.getMessage());
		}
	}


	@Nested @DisplayName("Set Macro Tests (3 parameter)")
	class SetMacro2Tests
	{
		@Test @DisplayName("test setMacro (three parameter) method with valid parameters")
		void testSetMacro2()
		{
			// Arrange
			Message newMessage = message.setMacro(10, Macro.TOOL, itemStack);
			MacroKey macroKey = MacroKey.of(Macro.TOOL).orElseThrow();
			MacroObjectMap macroObjectMap = newMessage.getObjectMap();

			// Act
			Object object = macroObjectMap.get(macroKey).orElseThrow();

			// Assert
			assertInstanceOf(ItemStack.class, object);
			assertEquals(itemStack, object);
		}


		@Test @DisplayName("test setMacro (quantity) method with null macro parameter")
		void testSetMacro2_parameter_null_macro()
		{
			ValidationException exception = assertThrows(ValidationException.class,
					() -> message.setMacro(5, null, new ItemStack(Material.DIAMOND_SWORD)));

			assertEquals("The parameter 'macroKey' was invalid.", exception.getMessage());
		}
	}


	@Test @DisplayName("test setMacro (duration) method with valid parameters")
	void testSetMacro3()
	{
		// Arrange
		Message newMessage = message.setMacro(Macro.DURATION, Duration.ofMinutes(5), ChronoUnit.MINUTES);
		MacroKey macroKey = MacroKey.of(Macro.DURATION).orElseThrow();
		MacroObjectMap macroObjectMap = newMessage.getObjectMap();

		// Act
		BoundedDuration boundedDuration = (BoundedDuration) macroObjectMap.get(macroKey).orElseThrow();

		// Assert
		assertInstanceOf(BoundedDuration.class, boundedDuration);
		assertEquals(Duration.ofMinutes(5), boundedDuration.duration());
		assertEquals(ChronoUnit.MINUTES, boundedDuration.precision());
	}


	@Test
	void testSetMacro4_null_duration()
	{
		// Arrange
		Duration duration = Duration.ofSeconds(15);
		MacroKey macroKey = MacroKey.of(Macro.DURATION).orElseThrow();

		// Act
		Message newMessage = message.setMacro(Macro.DURATION, null, ChronoUnit.MINUTES);
		MacroObjectMap macroObjectMap = newMessage.getObjectMap();
		BoundedDuration boundedDuration = (BoundedDuration) macroObjectMap.get(macroKey).orElseThrow();

		// Assert
		assertEquals(Duration.ZERO, boundedDuration.duration());
	}


	@Test
	void testSetMacro4_null_lower_bound()
	{
		// Arrange
		Duration duration = Duration.ofSeconds(15);
		MacroKey macroKey = MacroKey.of(Macro.DURATION).orElseThrow();

		// Act
		Message newMessage = message.setMacro(Macro.DURATION, duration, null);
		MacroObjectMap macroObjectMap = newMessage.getObjectMap();
		BoundedDuration boundedDuration = (BoundedDuration) macroObjectMap.get(macroKey).orElseThrow();

		// Assert
		assertEquals(ChronoUnit.MINUTES, boundedDuration.precision());
	}


	@Test
	void testSetMacro4_invalid_key()
	{
		Duration duration = Duration.ofSeconds(15);
		ValidationException exception = assertThrows(ValidationException.class,
				() ->  message.setMacro(Macro.invalid, duration, ChronoUnit.MINUTES));

		assertEquals("The parameter 'macroKey' was invalid.", exception.getMessage());
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
