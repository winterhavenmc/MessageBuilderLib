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

import org.bukkit.World;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;

import org.bukkit.plugin.Plugin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class ConfigurationQueryHandlerTest {

	private final static String TEST_ITEM_KEY = "DEFAULT";

	Plugin plugin;
	FileConfiguration pluginConfig;
	QueryHandler queryHandler;
	World world;

	@BeforeEach
	void setUp() {
		plugin = mock(Plugin.class, "MockPlugin");
		pluginConfig = mock(FileConfiguration.class, "MockPluginConfig");

		when(plugin.getLogger()).thenReturn(Logger.getLogger("ConfigurationQueryHandlerTest"));
		when(plugin.getConfig()).thenReturn(pluginConfig);
		when(pluginConfig.getString("language")).thenReturn("en-US");

		Configuration mockConfiguration = mock(Configuration.class, "MockConfiguration");

		queryHandler = new ConfigurationQueryHandler(plugin, mockConfiguration);
		world = mock(World.class);
		when(world.getName()).thenReturn("world");
	}

	@Test
	void getItemRecord() {
	}

	@Test
	void getMessageRecord() {
	}

	@Test
	void getWorldName() {
	}

	@Test
	void getWorldAlias() {
	}

	@Test
	void getTimeString() {
	}

	@Test
	void testGetTimeString() {
	}

//	@Nested
//	class GetMessageRecordTests {
//		@Test
//		void getMessageRecordTest() {
//			assertNotNull(languageHandler.getMessageRecord_opt(MessageId.ENABLED_MESSAGE));
//		}
//
//		@Test
//		void getMessageRecordTest_null_key() {
//			IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
//					() -> languageHandler.getMessageRecord_opt(null));
//			assertEquals("the messageId parameter was null.", exception.getMessage());
//		}
//
//		@Test
//		void getMessageRecordTest_nonexistent_entry() {
//			assertTrue(languageHandler.getMessageRecord_opt(MessageId.NONEXISTENT_ENTRY).isEmpty());
//		}
//	}

//	@Nested
//	class GetOptMessageRecordTests {
//		@Test
//		void getOptMessageRecordTest() {
//			assertTrue(languageHandler.getMessageRecord_opt(MessageId.ENABLED_MESSAGE).isPresent());
//		}
//
//		@Test
//		void getOptMessageRecordTest_null_key() {
//			IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
//					() -> languageHandler.getMessageRecord_opt(null));
//			assertEquals("the messageId parameter was null.", exception.getMessage());
//		}
//
//		@Test
//		void getOptMessageRecordTest_nonexistent_entry() {
//			assertTrue(languageHandler.getMessageRecord_opt(MessageId.NONEXISTENT_ENTRY).isEmpty());
//		}
//	}


//	@Nested
//	@Disabled
//	class IsEnabledTests {
//		@Test
//		void isEnabled() {
//			assertTrue(languageHandler.getMessageRecord_opt(MessageId.ENABLED_MESSAGE).isPresent());
//			assertTrue(languageHandler.getMessageRecord_opt(MessageId.ENABLED_MESSAGE).get().enabled());
//		}
//		@Test
//		void isEnabled_disabled() {
//			assertTrue(languageHandler.getMessageRecord_opt(MessageId.DISABLED_MESSAGE).isPresent());
//			assertFalse(languageHandler.getMessageRecord_opt(MessageId.DISABLED_MESSAGE).get().enabled());
//		}
//
//		@Test
//		void isEnabled_null() {
//			IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
//					() -> languageHandler.getMessageRecord_opt(null).orElseThrow().enabled());
//			assertEquals("the messageId parameter was null.", exception.getMessage());
//		}
//
//		@Test
//		void isEnabled_nonexistent_entry() {
//			// if entry for messageId does not exist, the message record will be an empty optional
//			assertTrue(languageHandler.getMessageRecord_opt(MessageId.NONEXISTENT_ENTRY).isEmpty());
//		}
//	}


	@Nested
	class ItemRecordTests {

		@Test
		void getItemRecordTest() {
			assertTrue(queryHandler.getItemRecord(TEST_ITEM_KEY).isPresent());
		}

		@Test
		void getItemRecordTest_null_key() {
			IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
					() -> queryHandler.getItemRecord(null));
			assertEquals("The itemKey parameter was null.", exception.getMessage());
		}

		@Test
		void getItemRecordTest_nonexistent_entry() {
			assertTrue(queryHandler.getItemRecord("NONEXISTENT_ENTRY").isEmpty());
		}
	}

	@Nested
	class WorldNameTests {
		@Disabled
		@Test
		void getWorldNameTest() {
			Optional<String> worldName = queryHandler.getWorldName(world);
			assertNotNull(world, "The mock world is null.");
			assertTrue(worldName.isPresent());
			assertEquals("world", worldName.get());
			verify(world, atLeastOnce()).getName();
		}

		@Test
		void getWorldName_null() {
			Optional<String> optionalWorldName = queryHandler.getWorldName(null);
			assertTrue(optionalWorldName.isEmpty());
		}
	}

	@Nested
	class WorldAliasTests {

		@Disabled
		@Test
		void getWorldAliasTest() {
			assertTrue(queryHandler.getWorldAlias(world).isEmpty());
		}

		@Test
		void getWorldAliasTest_null() {
			assertTrue(queryHandler.getWorldAlias(null).isEmpty());
		}
	}

}
