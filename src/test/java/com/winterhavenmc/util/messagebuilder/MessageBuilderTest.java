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

import com.winterhavenmc.util.messagebuilder.messages.Macro;
import com.winterhavenmc.util.messagebuilder.messages.MessageId;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Logger;

import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.winterhavenmc.util.messagebuilder.util.MockUtility.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class MessageBuilderTest {

	@TempDir
	File tempDataDir;

	@Mock Plugin pluginMock;

	FileConfiguration pluginConfiguration;
	Configuration languageConfiguration;


	@BeforeEach
	void setUp() {

		// load configuration from resource
		pluginConfiguration = new YamlConfiguration();
		pluginConfiguration.set("language", "en-US");
		pluginConfiguration.set("locale", "en-US");

		languageConfiguration = loadConfigurationFromResource("language/en-US.yml");
	}

	@AfterEach
	public void tearDown() {
	}


	@Nested
	class MockingSetupTests {
		@Test
		void testTempDataDir_is_directory() {
			assertTrue(tempDataDir.isDirectory());
		}
	}

	@Test
	void testConstructor_null_parameter() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
				new MessageBuilder<MessageId, Macro>(null));
		assertEquals("The plugin parameter cannot be null.", exception.getMessage());
	}

	@Test
	void testInstallResource() {
		assertDoesNotThrow(() -> installResource("language/en-US.yml", tempDataDir.toPath()));
		assertTrue(Files.exists(Paths.get(tempDataDir.getAbsolutePath(), "language", "en-US.yml")));
	}

	@Test
	void testConstructor_valid() {
		assertDoesNotThrow(() -> installResource("language/en-US.yml", tempDataDir.toPath()));
		when(pluginMock.getDataFolder()).thenReturn(tempDataDir);
		when(pluginMock.getConfig()).thenReturn(pluginConfiguration);
		when(pluginMock.getLogger()).thenReturn(Logger.getLogger(this.getClass().getName()));

		when(pluginMock.getResource(AUTO_INSTALL_TXT)).thenReturn(getClass().getClassLoader().getResourceAsStream(AUTO_INSTALL_TXT));
		when(pluginMock.getResource("language/en-US.yml")).thenReturn(getClass().getClassLoader().getResourceAsStream("language/en-US.yml"));

		// install resource when saveResource is called
		doAnswer(invocation -> installResource(invocation.getArgument(0), tempDataDir.toPath()))
				.when(pluginMock).saveResource(anyString(), eq(false));

		new MessageBuilder<MessageId, Macro>(pluginMock);
	}


	//	@Test
//	void getToolKitTest() {
//		Toolkit toolkit = messageBuilder.getToolkit();
//		assertNotNull(toolkit);
//	}


//	@Nested
//	class ComposeTests {
//		@Test
//		void composeTest() {
//			// Arrange
//			when(playerMock.getUniqueId()).thenReturn(new UUID(0,1));
//
//			// Arrange & Act
//			String resultString = messageBuilder.compose(playerMock, MessageId.ENABLED_MESSAGE).toString();
//
//			// Assert
//			assertEquals("This is an enabled message", resultString);
//		}
//
//		@Test
//		void composeTest_null_parameter_recipient() {
//			// Arrange & Act
//			IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
//					() -> messageBuilder.compose(null, MessageId.ENABLED_MESSAGE));
//
//			// Assert
//			assertEquals("The recipient parameter was null.", exception.getMessage());
//		}
//
//		@Test
//		void composeTest_null_parameter_messageId() {
//			// Arrange
//			Player recipient = mock(Player.class, "MockRecipient");
//
//			// Act
//			IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
//					() -> messageBuilder.compose(recipient, null));
//
//			// Assert
//			assertEquals("The messageId parameter cannot null.", exception.getMessage());
//		}
//
//		@Test
//		void testCompose_WithValidParameters_Success() {
//			CommandSender mockSender = mock(CommandSender.class);
//
//			// Assuming MessageId.ENABLED_MESSAGE is a valid enum constant in your MessageId enum
//			MessageId messageId = MessageId.ENABLED_MESSAGE;
//
//			messageBuilder.compose(mockSender, messageId);
//
//			// Add assertions to verify the behavior
//			assertNotNull(messageBuilder); // Verify that the builder was created
//			// Further checks can be done based on the expected state after compose
//		}
//	}
//
//
//	@Nested
//	class ai_generated_tests {
//
//		@Test
//		void testSetMacro_WithValidParameters_Success() {
//			messageBuilder.compose(playerMock, MessageId.ENABLED_MESSAGE)
//					.setMacro(Macro.TOOL, new ItemStack(Material.GOLDEN_AXE));
//
//			// Verify that the macro was set correctly
//			// Assuming you have a method to get the current state or macro settings
//			// For example:
//			// assertEquals(expectedValue, messageBuilder.getCurrentMacro(Macro.PLACEHOLDER1));
//		}
//
//		@Disabled
//		@Test //TODO: move this test to the Message class, where the send method lives
//		void testSend_WithValidMessage_SendsMessage() {
//			CommandSender commandSenderMock = mock(CommandSender.class);
//			MessageId messageId = MessageId.ENABLED_MESSAGE;
//
//			messageBuilder.compose(commandSenderMock, messageId)
//					.setMacro(Macro.TOOL, new ItemStack(Material.GOLDEN_AXE))
//					.send();
//
//			// Verify that the send method does what is expected
//			// This would depend on how your send method is implemented.
//			// For example, if it sends a message to the CommandSender, you would verify that.
//			//verify(commandSenderMock, times(1)).sendMessage(anyString()); // Adapt based on actual implementation
//		}
//	}
//
//	@Disabled
//	@Test
//	void reloadTest() {
//		messageBuilder.reload();
////		assertNotNull(mockLanguageHandler.getConfiguration());
//		verify(languageHandlerMock, atLeastOnce()).reload();
//	}

}
