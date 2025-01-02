/*
 * Copyright (c) 2024 Tim Savage.
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

package com.winterhavenmc.util.messagebuilder.query;

import com.winterhavenmc.util.messagebuilder.messages.MessageId;
import com.winterhavenmc.util.messagebuilder.util.MockUtility;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.Plugin;

import org.bukkit.plugin.PluginManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.winterhavenmc.util.messagebuilder.util.MockUtility.loadConfigurationFromResource;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class YamlLanguageFileQueryHandlerTest {

	Plugin plugin;
	LanguageFileQueryHandler languageFileQueryHandler;


	@BeforeEach
	void setUp() {
		Map<String, Object> configValues = new HashMap<>();
		configValues.put("language", "en-US");
		configValues.put("locale", "en-US");

		plugin = MockUtility.createMockPlugin(configValues);
		Configuration languageConfiguration = loadConfigurationFromResource("language/en-US.yml");
		languageFileQueryHandler = new YamlLangugageFileQueryHandler(plugin, languageConfiguration);
	}


	@Nested
	class MockSetupTests {
		@Test
		void PluginNotNullTest() {
			assertNotNull(plugin);
		}

		@Test
		void PluginConfigNotNullTest() {
			assertNotNull(plugin.getConfig());
		}

		@Test
		void PluginLoggerNotNullTest() {
			assertNotNull(plugin.getLogger());
		}
	}


	@Test
	void getItemRecordTest() {
		Optional<ItemRecord> itemRecord = languageFileQueryHandler.getItemRecord("TEST_ITEM_1");
		assertTrue(itemRecord.isPresent());
	}

	@Test
	void getItemRecordTest_null() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> languageFileQueryHandler.getItemRecord(null)
		);
		assertEquals("The itemKey parameter was null.", exception.getMessage());
	}

	@Test
	void getMessageRecordTest() {
		Optional<MessageRecord> messageRecord = languageFileQueryHandler.getMessageRecord(MessageId.ENABLED_MESSAGE);
		assertTrue(messageRecord.isPresent());
	}

	@Test
	void getIMessageRecordTest_null() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> languageFileQueryHandler.getMessageRecord(null));
		assertEquals("The messageId parameter was null.", exception.getMessage());
	}

	@Test
	void getConstantTest() {
		Optional<String> constantString = languageFileQueryHandler.getString("HOME.DISPLAY_NAME");
		assertTrue(constantString.isPresent());
	}

	@Test
	void getConstantTest_null() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> languageFileQueryHandler.getString(null));
		assertEquals("The itemKey parameter was null.", exception.getMessage());
	}

	@Test
	void isMultiverseInstalled() {
		// mock plugin manager to return isEnabled("Multiverse-Core") = false
		PluginManager mockPluginManager = mock(PluginManager.class, "MockPluginManager");
		when(mockPluginManager.isPluginEnabled("Multiverse-Core")).thenReturn(false);
	}

}
