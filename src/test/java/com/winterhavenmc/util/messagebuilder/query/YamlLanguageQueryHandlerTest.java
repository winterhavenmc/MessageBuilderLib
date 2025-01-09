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
import com.winterhavenmc.util.messagebuilder.namespace.Namespace;
import com.winterhavenmc.util.messagebuilder.query.domain.DomainQueryHandler;
import com.winterhavenmc.util.messagebuilder.query.domain.item.ItemQueryHandler;
import com.winterhavenmc.util.messagebuilder.query.domain.item.ItemRecord;
import com.winterhavenmc.util.messagebuilder.query.domain.message.MessageRecord;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import org.bukkit.plugin.PluginManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.winterhavenmc.util.messagebuilder.util.MockUtility.loadConfigurationFromResource;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class YamlLanguageQueryHandlerTest {

	@Mock Plugin pluginMock;
	LanguageQueryHandler queryHandler;


	@BeforeEach
	void setUp() {
		Configuration languageConfig = loadConfigurationFromResource("language/en-US.yml");
		queryHandler = new YamlLanguageQueryHandler(pluginMock, languageConfig);
	}

	@Test
	void getItemRecordTest() {
		DomainQueryHandler<?> handler = queryHandler.getQueryHandler(Namespace.Domain.ITEMS);
		Optional<ItemRecord> itemRecord;
		if (handler instanceof ItemQueryHandler itemQueryHandler) {
			itemRecord = itemQueryHandler.getItemRecord("TEST_ITEM_1");
			assertTrue(itemRecord.isPresent());
		}
	}

	@Test
	void getItemRecordTest_null() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> queryHandler.getItemRecord(null)
		);
		assertEquals("The itemKey parameter was null.", exception.getMessage());
	}

	@Test
	void getMessageRecordTest() {
		Optional<MessageRecord> messageRecord = queryHandler.getMessageRecord(MessageId.ENABLED_MESSAGE);
		assertTrue(messageRecord.isPresent());
	}

	@Test
	void getIMessageRecordTest_null() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> queryHandler.getMessageRecord(null));
		assertEquals("The messageId parameter cannot null.", exception.getMessage());
	}

	@Test
	void getConstantTest() {
		Optional<String> constantString = queryHandler.getString("HOME.DISPLAY_NAME");
		assertTrue(constantString.isPresent());
	}

	@Test
	void getConstantTest_null() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> queryHandler.getString(null));
		assertEquals("The itemKey parameter was null.", exception.getMessage());
	}

	@Disabled
	@Test
	void isMultiverseInstalled() {
		// mock plugin manager to return isEnabled("Multiverse-Core") = false
		PluginManager mockPluginManager = mock(PluginManager.class, "MockPluginManager");
		when(mockPluginManager.isPluginEnabled("Multiverse-Core")).thenReturn(false);
	}

}
