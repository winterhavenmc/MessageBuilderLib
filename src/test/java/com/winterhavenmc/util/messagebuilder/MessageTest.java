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

import com.winterhavenmc.util.messagebuilder.messages.Macro;
import com.winterhavenmc.util.messagebuilder.pipeline.ContextMap;
import com.winterhavenmc.util.messagebuilder.pipeline.processor.MessageProcessor;
import com.winterhavenmc.util.messagebuilder.resources.RecordKey;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.MessageRecord;

import com.winterhavenmc.util.messagebuilder.validation.ValidationException;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.time.Duration;
import java.util.Optional;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.winterhavenmc.util.messagebuilder.messages.MessageId.ENABLED_MESSAGE;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class MessageTest
{
	// declare mocks
	@Mock Player playerMock;
	@Mock MessageProcessor messageProcessorMock;

	// declare real objects
	FileConfiguration pluginConfiguration;
	Message message;
	ItemStack itemStack;
	MessageRecord messageRecord;


	@BeforeEach
	public void setUp()
	{
		pluginConfiguration = new YamlConfiguration();
		pluginConfiguration.set("language", "en-US");
		pluginConfiguration.set("locale", "en-US");
		pluginConfiguration.set("titles-enabled", true);

		itemStack = new ItemStack(Material.DIAMOND_SWORD);

		RecordKey recordKey = RecordKey.of(ENABLED_MESSAGE).orElseThrow();

		message = new Message(
				playerMock, recordKey, messageProcessorMock);

		messageRecord = new MessageRecord(
				recordKey,
				true,
				"this is a test message",
				Duration.ofSeconds(11),
				"this is a test title",
				22,
				33,
				44,
				"this is a test subtitle", "", "", "");
	}


	@AfterEach
	public void tearDown()
	{
		playerMock = null;
		pluginConfiguration = null;
	}


	@Test
	void getRecipient()
	{
		assertEquals(playerMock, message.getRecipient());
	}


	@Test
	void getMessageId()
	{
		assertEquals(ENABLED_MESSAGE.name(), message.getMessageId());
	}


	@Test
	void getRecordKey()
	{
		// Act
		RecordKey recordKey = message.getMessageKey();

		// Assert
		assertEquals(ENABLED_MESSAGE.name(), recordKey.toString());
	}


	@Test
	void getContextMap()
	{
		assertNotNull(message.getContextMap());
	}


	@Nested
	class SetMacroTests
	{
		@Test
		void testSetMacro()
		{
			Message newMessage = message.setMacro(Macro.TOOL, itemStack);
			assertEquals(Optional.of(itemStack), newMessage.peek(Macro.TOOL));
		}


		@Test
		void testSetMacro_parameter_null_macro() {
			ValidationException exception = assertThrows(ValidationException.class,
					() -> message.setMacro(null, new ItemStack(Material.DIAMOND_SWORD)));

			assertEquals("The parameter 'macro' cannot be null.", exception.getMessage());
		}


		@Test
		@Disabled("currently not validating against null macro")
		void testSetMacro_parameter_null_object() {
			ValidationException exception = assertThrows(ValidationException.class,
					() -> message.setMacro(Macro.OWNER, null));

			assertEquals("The parameter 'value' cannot be null.", exception.getMessage());
		}
	}


	@Nested
	class SetMacro2Tests {
		@Test
		void testSetMacro2() {
			// Arrange
			Message newMessage = message.setMacro(10, Macro.TOOL, itemStack);
			RecordKey recordKey = RecordKey.of(Macro.TOOL).orElseThrow();
			ContextMap contextMap = newMessage.getContextMap();

			// Act
			Object object = contextMap.get(recordKey).orElseThrow();

			// Assert
			assertInstanceOf(ItemStack.class, object);
			assertEquals(itemStack, object);
		}


		@Test
		void testSetMacro2_parameter_null_macro() {
			ValidationException exception = assertThrows(ValidationException.class,
					() -> message.setMacro(5, null, new ItemStack(Material.DIAMOND_SWORD)));

			assertEquals("The parameter 'macro' cannot be null.", exception.getMessage());
		}


		@Test
		@Disabled("currently not validating against null macro")
		void testSetMacro2_parameter_null_object() {
			ValidationException exception = assertThrows(ValidationException.class,
					() -> message.setMacro(6, Macro.OWNER, null));

			assertEquals("The parameter 'value' cannot be null.", exception.getMessage());
		}
	}


	@Test
	void testSend() {
		message.send();
	}

}
