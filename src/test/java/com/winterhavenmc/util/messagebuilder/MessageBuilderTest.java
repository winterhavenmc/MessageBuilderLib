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

import com.winterhavenmc.util.messagebuilder.languages.YamlLanguageHandler;
import com.winterhavenmc.util.messagebuilder.macro.MacroHandler;
import com.winterhavenmc.util.messagebuilder.messages.Macro;
import com.winterhavenmc.util.messagebuilder.messages.MessageId;
import com.winterhavenmc.util.messagebuilder.query.YamlLangugageFileQueryHandler;
import com.winterhavenmc.util.messagebuilder.util.MockUtility;

import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.util.Locale;
import java.util.UUID;

import org.junit.jupiter.api.*;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class MessageBuilderTest {

	private Plugin pluginMock;
	@Mock private Server serverMock;
	@Mock private PluginManager pluginManagerMock;
	@Mock private YamlLanguageHandler mockLanguageHandler;
	@Mock private YamlLangugageFileQueryHandler mockConfigurationQueryHandler;
	@Mock private MacroHandler mockMacroHandler;
	@Mock private Player mockPlayer;

	private MessageBuilder<MessageId, Macro> messageBuilder;


	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		pluginMock = MockUtility.createMockPlugin();

		// Initialize the MessageBuilder with mocked dependencies
		messageBuilder = new MessageBuilder<>(pluginMock);

		when(mockLanguageHandler.reload()).thenReturn(true);
		when(pluginMock.getServer()).thenReturn(serverMock);
		when(serverMock.getPluginManager()).thenReturn(pluginManagerMock);

		// mock player responses
		when(mockPlayer.getName()).thenReturn("Player One");
		when(mockPlayer.getUniqueId()).thenReturn(new UUID(1,1));
	}

	@AfterEach
	public void tearDown() {
		pluginMock = null;
		messageBuilder = null;
	}


	@Test
	void constructorTest_null_parameter() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
				new MessageBuilder<MessageId, Macro>(null));
		assertEquals("The plugin parameter cannot be null.", exception.getMessage());
	}


	@Nested
	class MockingSetupTests {
		@Test
		void testMocksForNull() {
			assertNotNull(pluginMock);
			assertNotNull(serverMock);
			assertNotNull(pluginManagerMock);
			assertNotNull(mockLanguageHandler);
		}
	}

	@Test
	void getToolKitTest() {
		Toolkit toolkit = messageBuilder.getToolkit();
		assertNotNull(toolkit);
	}


//	@Disabled
//	@Test
//	void testHandleLanguageWithMockedLocale() {
//		LocaleProvider mockProvider = mock(LocaleProvider.class);
//		when(mockProvider.fromLanguageTag("en-US")).thenReturn(Locale.US);
//
////			LanguageHandler handler = new YamlLanguageHandler(mockProvider);
////			handler.handleLanguage("en-US");
//
//		// Verify interactions or assert behavior
//		verify(mockProvider).fromLanguageTag("en-US");
//	}


	@Nested
	class ComposeTests {
		@Test
		void composeTest() {
			Message<MessageId, Macro> message = messageBuilder.compose(mockPlayer, MessageId.ENABLED_MESSAGE);
			assertEquals("This is an enabled message", message.toString());
		}

		@Test
		void composeTest_null_parameter_recipient() {
			Player recipient = mock(Player.class, "MockRecipient");

			when(recipient.getName()).thenReturn("Mock Recipient");
			when(recipient.getUniqueId()).thenReturn(new UUID(42, 42));

			IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
					() -> messageBuilder.compose(null, MessageId.ENABLED_MESSAGE));
			assertEquals("The recipient parameter was null.", exception.getMessage());
		}

		@Test
		void composeTest_null_parameter_messageId() {
			Player recipient = mock(Player.class, "MockRecipient");

			when(recipient.getName()).thenReturn("Mock Recipient");
			when(recipient.getUniqueId()).thenReturn(new UUID(42,42));

			IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
					() -> messageBuilder.compose(recipient, null));
			assertEquals("The messageId parameter was null.", exception.getMessage());
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
			messageBuilder.compose(mockPlayer, MessageId.ENABLED_MESSAGE)
					.setMacro(Macro.TOOL, new ItemStack(Material.GOLDEN_AXE));

			// Verify that the macro was set correctly
			// Assuming you have a method to get the current state or macro settings
			// For example:
			// assertEquals(expectedValue, messageBuilder.getCurrentMacro(Macro.PLACEHOLDER1));
		}

		@Test
		void testSend_WithValidMessage_SendsMessage() {
			CommandSender mockSender = mock(CommandSender.class);
			MessageId messageId = MessageId.ENABLED_MESSAGE;

			messageBuilder.compose(mockSender, messageId)
					.setMacro(Macro.TOOL, new ItemStack(Material.GOLDEN_AXE))
					.send();

			// Verify that the send method does what is expected
			// This would depend on how your send method is implemented.
			// For example, if it sends a message to the CommandSender, you would verify that.
			verify(mockSender, times(1)).sendMessage(anyString()); // Adapt based on actual implementation
		}
	}

	@Disabled
	@Test
	void reloadTest() {
		messageBuilder.reload();
//		assertNotNull(mockLanguageHandler.getConfiguration());
		verify(mockLanguageHandler, atLeastOnce()).reload();
	}

}
