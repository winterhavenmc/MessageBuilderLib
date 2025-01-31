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

import com.winterhavenmc.util.messagebuilder.cooldown.CooldownMap;
import com.winterhavenmc.util.messagebuilder.macro.MacroReplacer;
import com.winterhavenmc.util.messagebuilder.messages.Macro;
import com.winterhavenmc.util.messagebuilder.messages.MessageId;
import com.winterhavenmc.util.messagebuilder.resources.language.LanguageQueryHandler;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.messages.MessageRecord;
import com.winterhavenmc.util.messagebuilder.util.LocalizedException;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.util.List;

import static com.winterhavenmc.util.messagebuilder.messages.MessageId.ENABLED_MESSAGE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class MessageTest {


	// declare mocks
	@Mock Plugin pluginMock;
	@Mock Player playerMock;
	@Mock World worldMock;
	@Mock LanguageQueryHandler languageQueryHandlerMock;
	@Mock MacroReplacer macroReplacerMock;
	@Mock CooldownMap cooldownMapMock;

	// declare real objects
	FileConfiguration pluginConfiguration;
	Message<MessageId, Macro> message;
	ItemStack itemStack;
	MessageRecord testRecord;


	@BeforeEach
	public void setUp() {
		pluginConfiguration = new YamlConfiguration();
		pluginConfiguration.set("language", "en-US");
		pluginConfiguration.set("locale", "en-US");
		pluginConfiguration.set("titles-enabled", true);

		itemStack = new ItemStack(Material.DIAMOND_SWORD);

		message = new Message<>(
				languageQueryHandlerMock,
				macroReplacerMock,
				playerMock,
				MessageId.ENABLED_MESSAGE.name(),
				cooldownMapMock);

		testRecord = new MessageRecord(
				ENABLED_MESSAGE.name(),
				true,
				true,
				"this-is-a_string-key",
				List.of("list", "of", "arguments"),
				"this is a test message",
				Duration.ofSeconds(11),
				"this is a test title",
				22,
				33,
				44,
				"this is a test subtitle", "", "", "");
	}

	@AfterEach
	public void tearDown() {
		pluginMock = null;
		playerMock = null;
		worldMock = null;
		languageQueryHandlerMock = null;
		macroReplacerMock = null;
		cooldownMapMock = null;
		pluginConfiguration = null;
	}


	@Nested
	class SetMacroTests {
		@Test
		void testSetMacro() {
			message.setMacro(Macro.TOOL, itemStack);
			assertEquals(itemStack, message.peek(Macro.TOOL));
		}

		@Test
		void testSetMacro_parameter_null_macro() {
			LocalizedException exception = assertThrows(LocalizedException.class,
					() -> message.setMacro(null, new ItemStack(Material.DIAMOND_SWORD)));

			assertEquals("The parameter 'macro' cannot be null.", exception.getMessage());
		}

		@Test
		void testSetMacro_parameter_null_object() {
			LocalizedException exception = assertThrows(LocalizedException.class,
					() -> message.setMacro(Macro.OWNER, null));

			assertEquals("The parameter 'value' cannot be null.", exception.getMessage());
		}
	}


	@Nested
	class SetMacro2Tests {
		@Test
		void testSetMacro2() {
			message.setMacro(10, Macro.TOOL, itemStack);
			assertEquals(itemStack, message.peek(Macro.TOOL));
		}

		@Test
		void testSetMacro2_parameter_null_macro() {
			LocalizedException exception = assertThrows(LocalizedException.class,
					() -> message.setMacro(5, null, new ItemStack(Material.DIAMOND_SWORD)));

			assertEquals("The parameter 'macro' cannot be null.", exception.getMessage());
		}

		@Test
		void testSetMacro2_parameter_null_object() {
			LocalizedException exception = assertThrows(LocalizedException.class,
					() -> message.setMacro(6, Macro.OWNER, null));

			assertEquals("The parameter 'value' cannot be null.", exception.getMessage());
		}
	}

	@Nested
	class IsSendableTests {
		@Test
		void testIsSendable() {
			// Arrange
			when(playerMock.isOnline()).thenReturn(true);

			// Act & Assert
			assertTrue(message.isSendable(playerMock, testRecord));

			// Verify
			verify(playerMock, atLeastOnce()).isOnline();
		}

		@Test
		void testIsSendable_parameter_null_recipient() {
			LocalizedException exception = assertThrows(LocalizedException.class,
					() -> message.isSendable(null, testRecord));

			assertEquals("The parameter 'recipient' cannot be null.", exception.getMessage());
		}

		@Test
		void testIsSendable_parameter_null_messageRecord() {
			LocalizedException exception = assertThrows(LocalizedException.class,
					() -> message.isSendable(playerMock, null));

			assertEquals("The parameter 'messageRecord' cannot be null.", exception.getMessage());
		}
	}


	@Test
	void testSend() {
		message.send();
	}

}
