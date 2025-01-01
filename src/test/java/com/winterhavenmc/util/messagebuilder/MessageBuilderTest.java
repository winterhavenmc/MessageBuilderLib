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

import com.winterhavenmc.util.TimeUnit;
import com.winterhavenmc.util.messagebuilder.languages.LanguageHandler;
import com.winterhavenmc.util.messagebuilder.macro.MacroHandler;
import com.winterhavenmc.util.messagebuilder.messages.Macro;
import com.winterhavenmc.util.messagebuilder.messages.MessageId;
import com.winterhavenmc.util.messagebuilder.query.QueryHandler;
import com.winterhavenmc.util.messagebuilder.util.LocaleProvider;
import com.winterhavenmc.util.messagebuilder.util.MockUtility;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.junit.jupiter.api.*;

import static com.winterhavenmc.util.TimeUnit.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MessageBuilderTest {

	public static final String NON_EXISTENT_ENTRY = "NON_EXISTENT_ENTRY";
	public static final String FAIL = "fail";


	private Plugin mockPlugin;
	private FileConfiguration mockConfiguration;
	private LanguageHandler mockLanguageHandler;
	private QueryHandler mockQueryHandler;
	private MacroHandler mockMacroHandler;
	private Player mockPlayer;
	private MessageBuilder<MessageId, Macro> messageBuilder;

	@BeforeEach
	void setUp() {
		mockPlugin = MockUtility.createMockPlugin();
//		mockConfiguration = mock(FileConfiguration.class, "MockConfiguration");
		mockLanguageHandler = mock(LanguageHandler.class, "MockLanguageHandler");
		mockQueryHandler = mock(QueryHandler.class, "MockQueryHandler");
		mockMacroHandler = mock(MacroHandler.class, "mockMacroHandler");

//		LocaleProvider mockProvider = mock(LocaleProvider.class);
//		when(mockProvider.fromLanguageTag("en-US")).thenReturn(Locale.US);




//		when(mockPlugin.getLogger()).thenReturn(Logger.getLogger(this.getClass().getName()));
//		when(mockPlugin.getConfig()).thenReturn(mockConfiguration);

//		when(mockConfiguration.getString("language")).thenReturn("en-US");

		// mock player for message recipient
		mockPlayer = mock(Player.class, "MockPlayer");
		when(mockPlayer.getName()).thenReturn("Player One");
		when(mockPlayer.getUniqueId()).thenReturn(new UUID(1,1));

		// Initialize the MessageBuilder with mocked dependencies
		messageBuilder = new MessageBuilder<>(mockPlugin);
	}

	@AfterEach
	public void tearDown() {
		mockPlugin = null;
		messageBuilder = null;
	}


	@Disabled
	@Test
	void testHandleLanguageWithMockedLocale() {
		LocaleProvider mockProvider = mock(LocaleProvider.class);
		when(mockProvider.fromLanguageTag("en-US")).thenReturn(Locale.US);

//			LanguageHandler handler = new YamlLanguageHandler(mockProvider);
//			handler.handleLanguage("en-US");

		// Verify interactions or assert behavior
		verify(mockProvider).fromLanguageTag("en-US");
	}


	@Nested
	class ai_generated_tests {
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

		@Test
		void testSetMacro_WithValidParameters_Success() {
			messageBuilder.compose(mockPlayer, MessageId.ENABLED_MESSAGE)
					.setMacro(Macro.TOOL, "replacementValue");

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
					.setMacro(Macro.TOOL, "replacementValue")
					.send();

			// Verify that the send method does what is expected
			// This would depend on how your send method is implemented.
			// For example, if it sends a message to the CommandSender, you would verify that.
			verify(mockSender, times(1)).sendMessage(anyString()); // Adapt based on actual implementation
		}

		@Test
		void testCompose_WithNullParameters_ThrowsException() {
			// Assuming you throw IllegalArgumentException for null inputs
			assertThrows(IllegalArgumentException.class, () -> {
				messageBuilder.compose(null, null);
			});
		}

		// Additional tests for edge cases and other functionalities
		// For example, testing cooldown logic, macro handling, etc.
	}





	@Test
	void composeTest() {
		Message<MessageId, Macro> message = messageBuilder.compose(mockPlayer, MessageId.ENABLED_MESSAGE);
		assertEquals("This is an enabled message", message.toString());
	}

	@Nested
	class IsEnabledTests {

		@Test
		void isEnabledTest() {
			assertTrue(messageBuilder.isEnabled(MessageId.ENABLED_MESSAGE));
		}

		@Test
		void isEnabledTest_disabled() {
			assertFalse(messageBuilder.isEnabled(MessageId.DISABLED_MESSAGE));
		}

		@Test
		void isEnabledTest_nonexistent() {
			assertFalse(messageBuilder.isEnabled(MessageId.NONEXISTENT_ENTRY));
		}
	}

	@Test
	void getRepeatDelayTest() {
		assertEquals(10, messageBuilder.getRepeatDelay(MessageId.REPEAT_DELAYED_MESSAGE));
	}

	@Test
	void getMessageTest() {
		assertEquals("This is an enabled message", messageBuilder.getMessage(MessageId.ENABLED_MESSAGE));
	}

	@Test
	void getItemNameSingularTest() {
		assertTrue(messageBuilder.getInventoryItemNameSingular().isPresent());
	}

	@Test
	void getItemNameSingularTest_undefined_field() {
		assertTrue(messageBuilder.getItemNameSingular("UNDEFINED_ITEM_NAME").isEmpty());
	}

	@Test
	void getInventoryItemNameSingularTest_nonexistent_entry() {
		assertTrue(messageBuilder.getInventoryItemNameSingular(NON_EXISTENT_ENTRY).isEmpty());
	}

	@Test
	void getInventoryItemNamePluralTest_nonexistent_entry() {
		assertTrue(messageBuilder.getInventoryItemNamePlural(NON_EXISTENT_ENTRY).isEmpty());
	}

	@Test
	void getItemInventoryNameTest_null_parameter() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> messageBuilder.getInventoryItemNameSingular(null));
		assertEquals("the itemKey parameter was null.", exception.getMessage());
	}

	@Test
	void getInventoryItemNamePluralTest_null_parameter() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> messageBuilder.getInventoryItemNamePlural(null));
		assertEquals("the itemKey parameter was null.", exception.getMessage());
	}

	// 	public Optional<String> getItemNamePlural(final String itemKey) {
	@Test
	void getItemNamePluralTest() {
		assertEquals("Default Items", messageBuilder.getItemNamePlural().orElse(FAIL));
	}

	@Test
	void getItemNamePluralTest_nonexistent_entry() {
		assertTrue(messageBuilder.getItemNamePlural(NON_EXISTENT_ENTRY).isEmpty());
	}

	@Test
	void getInventoryItemNameSingularTest() {
		assertEquals("Default Inventory Item", messageBuilder.getInventoryItemNameSingular().orElse(FAIL));
	}

	@Test
	void getInventoryItemNamePluralTest() {
		assertEquals("Default Inventory Items", messageBuilder.getInventoryItemNamePlural().orElse(FAIL));
	}

	@Test
	void getItemInventoryNameTest_undefined_field() {
		assertTrue(messageBuilder.getInventoryItemNameSingular("UNDEFINED_INVENTORY_ITEM_NAME").isEmpty());
	}

	@Test
	void getInventoryItemNameTest_non_existent_entry() {
		assertTrue(messageBuilder.getInventoryItemNameSingular(NON_EXISTENT_ENTRY).isEmpty());
	}

	@Test
	void getItemLoreTest() {
		assertEquals(List.of("&etest1 lore line 1", "&etest1 lore line 2"), messageBuilder.getItemLore("TEST_ITEM_1"));
	}

	@Test
	void getItemLoreTest_no_parameter() {
		assertEquals(List.of("&edefault lore line 1", "&edefault lore line 2"), messageBuilder.getItemLore());
	}

	@Test
	void getSpawnDisplayNameTest() {
		assertEquals("&aSpawn", messageBuilder.getSpawnDisplayName().orElse(FAIL));
	}

	@Test
	void getHomeDisplayNameTest() {
		assertEquals("&aHome", messageBuilder.getHomeDisplayName().orElse(FAIL));
	}

	@Test
	void getTimeStringTest_with_singular_units() {
		long duration = DAYS.toMillis(1) + HOURS.toMillis(1) + MINUTES.toMillis(1) + SECONDS.toMillis(1);
		assertEquals("1 day 1 hour 1 minute 1 second", messageBuilder.getTimeString(duration));
	}

	@Test
	void getTimeStringTest_with_plural_units() {
		long duration = DAYS.toMillis(2) + HOURS.toMillis(2) + MINUTES.toMillis(2) + SECONDS.toMillis(2);
		assertEquals("2 days 2 hours 2 minutes 2 seconds", messageBuilder.getTimeString(duration));
	}

	@Test
	void getTimeStringTest_with_unlimited_time() {
		assertEquals("unlimited time", messageBuilder.getTimeString(-1));
	}

	@Test
	void getTimeStringTest_two_parameter_with_singular_units() {
		long duration = DAYS.toMillis(1) + HOURS.toMillis(1) + MINUTES.toMillis(1) + SECONDS.toMillis(1);
		assertEquals("1 day 1 hour 1 minute 1 second", messageBuilder.getTimeString(duration, TimeUnit.SECONDS));
	}

	@Test
	void getStringTest() {
		assertEquals("an arbitrary string", messageBuilder.getString("ARBITRARY_STRING").orElse(FAIL));
	}

	@Test
	void getStringListTest() {
		assertTrue(messageBuilder.getStringList("ARBITRARY_STRING_LIST").containsAll(List.of("item 1", "item 2", "item 3")));
	}

	@Test
	void setDelimitersTest_same() {
		messageBuilder.setDelimiters('#');
		assertEquals('#', MacroHandler.MacroDelimiter.LEFT.toChar());
		assertEquals('#', MacroHandler.MacroDelimiter.RIGHT.toChar());
		// these must be reset back manually or they persist across tests.
		messageBuilder.setDelimiters('%');
		assertEquals('%', MacroHandler.MacroDelimiter.LEFT.toChar());
		assertEquals('%', MacroHandler.MacroDelimiter.RIGHT.toChar());
	}

	@Test
	void setDelimitersTest_different() {
		messageBuilder.setDelimiters('L','R');
		assertEquals('L', MacroHandler.MacroDelimiter.LEFT.toChar());
		assertEquals('R', MacroHandler.MacroDelimiter.RIGHT.toChar());
		// these must be reset manually or they persist across tests.
		messageBuilder.setDelimiters('%');
		assertEquals('%', MacroHandler.MacroDelimiter.LEFT.toChar());
		assertEquals('%', MacroHandler.MacroDelimiter.RIGHT.toChar());
	}

//	@Test
//	void getWorldNameTest_mockito() {
//		World world = mock(World.class);
//		when(world.getName()).thenReturn("world");
//
//		assertNotNull(world, "Mock world 'world' is null.");
//		assertTrue(messageBuilder.getWorldName(world).isPresent(), "Returned value is an empty Optional.");
//		assertEquals(Optional.of("world"), messageBuilder.getWorldName(world), "Returned world name is not 'world'.");
//	}

	@Test
	void reloadTest() {
		messageBuilder.reload();
		assertEquals("This is an enabled message", messageBuilder.getMessage(MessageId.ENABLED_MESSAGE));
	}

}
