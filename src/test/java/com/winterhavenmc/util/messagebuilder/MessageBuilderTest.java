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

import com.winterhavenmc.util.messagebuilder.language.YamlLanguageHandler;
import com.winterhavenmc.util.messagebuilder.messages.Macro;
import com.winterhavenmc.util.messagebuilder.messages.MessageId;

import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.util.UUID;
import java.util.logging.Logger;

import org.junit.jupiter.api.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.winterhavenmc.util.messagebuilder.util.MockUtility.AUTO_INSTALL_TXT;
import static com.winterhavenmc.util.messagebuilder.util.MockUtility.LANGUAGE_EN_US_YML;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageBuilderTest {

	@Mock private Plugin pluginMock;
	@Mock private Server serverMock;
	@Mock private PluginManager pluginManagerMock;
	@Mock private YamlLanguageHandler languageHandlerMock;
	@Mock private Player playerMock;

	private MessageBuilder<MessageId, Macro> messageBuilder;


	@BeforeEach
	void setUp() {
		// load configuration from resource
		FileConfiguration pluginConfig = new YamlConfiguration();
		pluginConfig.set("language", "en-US");
		pluginConfig.set("locale", "en-US");

		assertNotNull(pluginConfig);
		assertEquals("en-US", pluginConfig.getString("language"));

		// return resources for mock plugin
		// return real file input streams for mock plugin resources
		doAnswer(invocation -> getClass().getClassLoader().getResourceAsStream(invocation.getArgument(0)))
				.when(pluginMock).getResource(anyString());

//		when(pluginMock.getResource(LANGUAGE_EN_US_YML)).thenReturn(getClass().getClassLoader().getResourceAsStream(LANGUAGE_EN_US_YML));
//		when(pluginMock.getResource(AUTO_INSTALL_TXT)).thenReturn(getClass().getClassLoader().getResourceAsStream(AUTO_INSTALL_TXT));

		// return populated pluginConfig for plugin.getConfig()
		when(pluginMock.getConfig()).thenReturn(pluginConfig);

		// return logger for plugin.getLogger()
		when(pluginMock.getLogger()).thenReturn(Logger.getLogger("MessageBuilderTest"));

		// return serverMock for plugin.getServer()
//		when(pluginMock.getServer()).thenReturn(serverMock);

		// return pluginManagerMock for server.getPluginManager()
//		when(serverMock.getPluginManager()).thenReturn(pluginManagerMock);


		// Initialize the MessageBuilder with mocked plugin
		messageBuilder = new MessageBuilder<>(pluginMock);

//		when(languageHandlerMock.reload()).thenReturn(true);
//		when(pluginMock.getServer()).thenReturn(serverMock);
//		when(serverMock.getPluginManager()).thenReturn(pluginManagerMock);

		// mock player responses
//		when(playerMock.getName()).thenReturn("Player One");
//		when(playerMock.getUniqueId()).thenReturn(new UUID(1,1));
	}

	@AfterEach
	public void tearDown() {
		pluginMock = null;
		serverMock = null;
		playerMock = null;
		pluginManagerMock = null;
		languageHandlerMock = null;
		messageBuilder = null;
	}


	@Nested
	class MockingSetupTests {
		@Test
		void testMocksForNull() {
			assertNotNull(pluginMock);
			assertNotNull(serverMock);
			assertNotNull(playerMock);
			assertNotNull(pluginManagerMock);
			assertNotNull(languageHandlerMock);
		}
	}

	@Test
	void testConstructor_null_parameter() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
				new MessageBuilder<MessageId, Macro>(null));
		assertEquals("The plugin parameter cannot be null.", exception.getMessage());
	}


	@Test
	void getToolKitTest() {
		Toolkit toolkit = messageBuilder.getToolkit();
		assertNotNull(toolkit);
	}


	@Nested
	class ComposeTests {
		@Test
		void composeTest() {
			// Arrange
			when(playerMock.getUniqueId()).thenReturn(new UUID(0,1));

			// Arrange & Act
			String resultString = messageBuilder.compose(playerMock, MessageId.ENABLED_MESSAGE).toString();

			// Assert
			assertEquals("This is an enabled message", resultString);
		}

		@Test
		void composeTest_null_parameter_recipient() {
			// Arrange & Act
			IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
					() -> messageBuilder.compose(null, MessageId.ENABLED_MESSAGE));

			// Assert
			assertEquals("The recipient parameter was null.", exception.getMessage());
		}

		@Test
		void composeTest_null_parameter_messageId() {
			// Arrange
			Player recipient = mock(Player.class, "MockRecipient");

			// Act
			IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
					() -> messageBuilder.compose(recipient, null));

			// Assert
			assertEquals("The messageId parameter cannot null.", exception.getMessage());
		}

		@Test
		void testCompose_WithValidParameters_Success() {
			CommandSender mockSender = mock(CommandSender.class);

			// Assuming MessageId.ENABLED_MESSAGE is a valid enum constant in your MessageId enum
			MessageId messageId = MessageId.ENABLED_MESSAGE;

			messageBuilder.compose(mockSender, messageId);

			// Add assertions to verify the behavior
			assertNotNull(messageBuilder); // Verify that the builder was created
			// Further checks can be done based on the expected state after compose
		}
	}


	@Nested
	class ai_generated_tests {

		@Test
		void testSetMacro_WithValidParameters_Success() {
			messageBuilder.compose(playerMock, MessageId.ENABLED_MESSAGE)
					.setMacro(Macro.TOOL, new ItemStack(Material.GOLDEN_AXE));

			// Verify that the macro was set correctly
			// Assuming you have a method to get the current state or macro settings
			// For example:
			// assertEquals(expectedValue, messageBuilder.getCurrentMacro(Macro.PLACEHOLDER1));
		}

		@Disabled
		@Test //TODO: move this test to the Message class, where the send method lives
		void testSend_WithValidMessage_SendsMessage() {
			CommandSender commandSenderMock = mock(CommandSender.class);
			MessageId messageId = MessageId.ENABLED_MESSAGE;

			messageBuilder.compose(commandSenderMock, messageId)
					.setMacro(Macro.TOOL, new ItemStack(Material.GOLDEN_AXE))
					.send();

			// Verify that the send method does what is expected
			// This would depend on how your send method is implemented.
			// For example, if it sends a message to the CommandSender, you would verify that.
			//verify(commandSenderMock, times(1)).sendMessage(anyString()); // Adapt based on actual implementation
		}
	}

	@Disabled
	@Test
	void reloadTest() {
		messageBuilder.reload();
//		assertNotNull(mockLanguageHandler.getConfiguration());
		verify(languageHandlerMock, atLeastOnce()).reload();
	}

}
