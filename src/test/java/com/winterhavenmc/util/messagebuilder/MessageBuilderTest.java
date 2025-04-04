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

package com.winterhavenmc.util.messagebuilder;

import com.winterhavenmc.util.messagebuilder.pipeline.replacer.MacroReplacer;
import com.winterhavenmc.util.messagebuilder.messages.MessageId;
import com.winterhavenmc.util.messagebuilder.pipeline.processor.MessageProcessor;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.YamlLanguageResourceManager;
import com.winterhavenmc.util.messagebuilder.validation.ValidationException;
import com.winterhavenmc.util.messagebuilder.util.MockUtility;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.util.logging.Logger;

import static com.winterhavenmc.util.messagebuilder.MessageBuilder.TICKS;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class MessageBuilderTest
{
	@Mock Plugin pluginMock;
	@Mock Player playerMock;

	@Mock YamlLanguageResourceManager languageResourceManagerMock;
	@Mock MacroReplacer macroReplacerMock;
	@Mock MessageProcessor messageProcessorMock;

	FileConfiguration pluginConfiguration;
	Configuration languageConfiguration;

	MessageBuilder messageBuilder;


	@BeforeEach
	void setUp()
	{
		// load configuration from resource
		pluginConfiguration = new YamlConfiguration();
		pluginConfiguration.set("language", "en-US");
		pluginConfiguration.set("locale", "en-US");

		languageConfiguration = MockUtility.loadConfigurationFromResource("language/en-US.yml");
		messageBuilder = MessageBuilder.test(pluginMock,
				languageResourceManagerMock,
				messageProcessorMock);
	}

	@AfterEach
	public void tearDown()
	{
		playerMock = null;
		pluginMock = null;
		pluginConfiguration = null;
		languageConfiguration = null;
		languageResourceManagerMock = null;
		macroReplacerMock = null;
		messageProcessorMock = null;
	}


	@Test
	void testTickUnit()
	{
		assertEquals(Duration.ofMillis(50), TICKS.getDuration());
	}


	@Test
	void compose()
	{
		// Arrange & Act
		Message message = messageBuilder.compose(playerMock, MessageId.ENABLED_MESSAGE);

		// Assert
		assertNotNull(message);
	}


	@Test
	void compose_parameter_null_player()
	{
		// Arrange & Act
		ValidationException exception = assertThrows(ValidationException.class,
				() -> messageBuilder.compose(null, MessageId.ENABLED_MESSAGE));

		// Assert
		assertEquals("The parameter 'recipient' cannot be null.", exception.getMessage());
	}


	@Test
	void compose_parameter_null_message_id()
	{
		// Arrange & Act
		ValidationException exception = assertThrows(ValidationException.class,
				() -> messageBuilder.compose(playerMock, null));

		// Assert
		assertEquals("The parameter 'key' cannot be null.", exception.getMessage());
	}


	@Test
	void reload()
	{
		when(languageResourceManagerMock.reload()).thenReturn(true);

		messageBuilder.reload();

		verify(languageResourceManagerMock, atLeastOnce()).reload();
	}


	@Test
	void reload_failed()
	{
		when(pluginMock.getLogger()).thenReturn(Logger.getLogger(this.getClass().getName()));

		messageBuilder.reload();

		verify(languageResourceManagerMock, atLeastOnce()).reload();
	}


	@Test
	void testCreate()
	{
		// Arrange
		when(pluginMock.getConfig()).thenReturn(pluginConfiguration);
		when(pluginMock.getLogger()).thenReturn(Logger.getLogger(this.getClass().getName()));

		// Act
		MessageBuilder messageBuilder1 = MessageBuilder.create(pluginMock);

		// Assert
		assertNotNull(messageBuilder1);
	}


	@Test
	void testCreate_parameter_null_plugin()
	{
		// Arrange & Act
		ValidationException exception = assertThrows(ValidationException.class,
				() -> MessageBuilder.create(null));

		// Assert
		assertEquals("The parameter 'plugin' cannot be null.", exception.getMessage());
	}


	@Test
	void test_parameter_null_plugin()
	{
		// Arrange & Act
		ValidationException exception = assertThrows(ValidationException.class,
				() -> MessageBuilder.test(null,
						languageResourceManagerMock,
						messageProcessorMock));

		// Assert
		assertEquals("The parameter 'plugin' cannot be null.", exception.getMessage());
	}


	@Test
	void test_parameter_null_languageResourceManager()
	{
		// Arrange & Act
		ValidationException exception = assertThrows(ValidationException.class,
				() -> MessageBuilder.test(pluginMock,
						null,
						messageProcessorMock));

		// Assert
		assertEquals("The parameter 'languageResourceManager' cannot be null.", exception.getMessage());
	}


	@Test
	void test_parameter_null_messageProcessor()
	{
		// Arrange & Act
		ValidationException exception = assertThrows(ValidationException.class,
				() -> MessageBuilder.test(pluginMock,
						languageResourceManagerMock,
						null));

		// Assert
		assertEquals("The parameter 'messageProcessor' cannot be null.", exception.getMessage());
	}

}
