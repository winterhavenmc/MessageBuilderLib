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

package com.winterhavenmc.library.messagebuilder;

import com.winterhavenmc.library.messagebuilder.model.message.Message;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.AdapterContextContainer;
import com.winterhavenmc.library.messagebuilder.pipeline.formatters.FormatterContainer;
import com.winterhavenmc.library.messagebuilder.messages.MessageId;
import com.winterhavenmc.library.messagebuilder.pipeline.MessagePipeline;
//import com.winterhavenmc.library.messagebuilder.query.QueryHandlerFactory;
import com.winterhavenmc.library.messagebuilder.resources.language.LanguageResourceManager;
import com.winterhavenmc.library.messagebuilder.validation.ValidationException;
import com.winterhavenmc.library.messagebuilder.util.MockUtility;

import org.bukkit.command.ProxiedCommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.util.logging.Logger;

import static com.winterhavenmc.library.messagebuilder.MessageBuilder.TICKS;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class MessageBuilderTest
{
	@Mock Plugin pluginMock;
	@Mock Player playerMock;
	@Mock ProxiedCommandSender proxiedCommandSenderMock;
	@Mock LanguageResourceManager languageResourceManagerMock;
	@Mock MessagePipeline messagePipelineMock;
	@Mock ItemForge itemForgeMock;
	@Mock FormatterContainer formatterContainerMock;
	@Mock AdapterContextContainer adapterContextContainerMock;

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
		lenient().when(pluginMock.getConfig()).thenReturn(pluginConfiguration);

		languageConfiguration = MockUtility.loadConfigurationFromResource("language/en-US.yml");
		messageBuilder = MessageBuilder.test(pluginMock,
				languageResourceManagerMock,
				itemForgeMock,
				messagePipelineMock);
	}


	@Test @DisplayName("Tick TimeUnit is valid.")
	void Tick_timeUnit_is_valid()
	{
		assertEquals(Duration.ofMillis(50), TICKS.getDuration());
	}


	@Test @DisplayName("compose method returns valid Message object.")
	void compose_method_returns_valid_Message_object()
	{
		// Arrange & Act
		Message result = messageBuilder.compose(playerMock, MessageId.ENABLED_MESSAGE);

		// Assert
		assertNotNull(result);
	}


	@Test @DisplayName("compose method does not throw exception when recipient parameter is null.")
	void compose_parameter_accepts_null_recipient_parameter()
	{
		// Act & Assert
		assertDoesNotThrow(() -> messageBuilder.compose(null, MessageId.ENABLED_MESSAGE));
	}


	@Test @DisplayName("compose method accepts ProxiedCommandSender for sender parameter.")
	void compose_parameter_accepts_proxied_command_sender()
	{
		// Act & Assert
		Message result = messageBuilder.compose(proxiedCommandSenderMock, MessageId.ENABLED_MESSAGE);

		assertNotNull(result);
	}


	@Test @DisplayName("compose method throws exception when messageId parameter is null.")
	void compose_throws_exception_when_parameter_messageId_is_null()
	{
		// Act
		ValidationException exception = assertThrows(ValidationException.class,
				() -> messageBuilder.compose(playerMock, null));

		// Assert
		assertEquals("The parameter 'messageId' cannot be null.", exception.getMessage());
	}


	@Test @DisplayName("Exception is not thrown when reload succeeds.")
	void reload_success_does_not_throw_exception()
	{
		// Arrange
		when(languageResourceManagerMock.reload()).thenReturn(true);

		// Act
		// Assert
		assertDoesNotThrow(() -> messageBuilder.reload());

		// Verify
		verify(languageResourceManagerMock, atLeastOnce()).reload();
	}


	@Test @DisplayName("Exception is not thrown when reload fails.")
	void reload_fail_does_not_throw_exception()
	{
		// Act & Assert
		assertDoesNotThrow(() -> messageBuilder.reload());

		// Verify
		verify(languageResourceManagerMock, atLeastOnce()).reload();
	}


	@Test @DisplayName("Static factory method (create) returns valid MessageBuilder.")
	void static_factory_create_returns_valid_MessageBuilder()
	{
		// Arrange
		lenient().when(pluginMock.getConfig()).thenReturn(pluginConfiguration);
		lenient().when(pluginMock.getLogger()).thenReturn(Logger.getLogger(this.getClass().getName()));

		try (MockedStatic<Bootstrap> bootstrapMockedStatic = Mockito.mockStatic(Bootstrap.class))
		{
			bootstrapMockedStatic.when(() -> Bootstrap
					.createMessagePipeline(pluginMock, languageResourceManagerMock, formatterContainerMock, adapterContextContainerMock))
					.thenReturn(messagePipelineMock);

			// Act
			MessageBuilder messageBuilder = MessageBuilder.create(pluginMock);

			// Assert
			assertNotNull(messageBuilder);
		}
	}


	@Test @DisplayName("ValidationException is thrown when plugin parameter is null (create method).")
	void static_factory_create_throws_exception_when_plugin_parameter_is_null()
	{
		// Arrange & Act
		ValidationException exception = assertThrows(ValidationException.class,
				() -> MessageBuilder.create(null));

		// Assert
		assertEquals("The parameter 'plugin' cannot be null.", exception.getMessage());
	}


	@Test @DisplayName("ValidationException is thrown when plugin parameter is null (test method).")
	void static_factory_test_throws_exception_when_plugin_parameter_is_null()
	{
		// Arrange & Act
		ValidationException exception = assertThrows(ValidationException.class,
				() -> MessageBuilder.test(null,
						languageResourceManagerMock,
						itemForgeMock,
						messagePipelineMock));

		// Assert
		assertEquals("The parameter 'plugin' cannot be null.", exception.getMessage());
	}


	@Test @DisplayName("ValidationException is thrown when languageResourceManager parameter is null (test method).")
	void static_factory_test_throws_exception_when_languageResourceManager_parameter_is_null()
	{
		// Arrange & Act
		ValidationException exception = assertThrows(ValidationException.class,
				() -> MessageBuilder.test(pluginMock,
						null,
						itemForgeMock,
						messagePipelineMock));

		// Assert
		assertEquals("The parameter 'languageResourceManager' cannot be null.", exception.getMessage());
	}


	@Test @DisplayName("ValidationException is thrown when messageProcessor parameter is null (test method).")
	void static_factory_test_throws_exception_when_messageProcessor_parameter_is_null()
	{
		// Arrange & Act
		ValidationException exception = assertThrows(ValidationException.class,
				() -> MessageBuilder.test(pluginMock,
						languageResourceManagerMock,
						itemForgeMock,
						null));

		// Assert
		assertEquals("The parameter 'messageProcessor' cannot be null.", exception.getMessage());
	}


	@Test
	void getItemForge_returns_ItemForge()
	{
		ItemForge itemForge = messageBuilder.itemForge();

		assertInstanceOf(ItemForge.class, itemForge);
	}
}
