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

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import org.bukkit.World;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Optional;

import static com.winterhavenmc.util.TimeUnit.*;
import static org.junit.jupiter.api.Assertions.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class YamlLanguageHandlerTest {

	private ServerMock server;
	private PluginMain plugin;
	private LanguageHandler languageHandler;

	@BeforeEach
	public void setUp() {
		// Start the mock server
		server = MockBukkit.mock();

		// start the mock plugin
		plugin = MockBukkit.load(PluginMain.class);

		// create a language handler instance
		languageHandler = new YamlLanguageHandler(plugin);

	}

	@AfterEach
	public void tearDown() {
		// Stop the mock server
		MockBukkit.unmock();
	}

	@Test
	void getMessageKeys() {
		assertFalse(languageHandler.getMessageKeys().isEmpty());
		assertTrue(languageHandler.getMessageKeys().contains("ENABLED_MESSAGE"));
	}

	@Nested
	class IsEnabledTests {
		@Test
		void isEnabled() {
			assertTrue(languageHandler.isEnabled(MessageId.ENABLED_MESSAGE));
			assertFalse(languageHandler.isEnabled(MessageId.DISABLED_MESSAGE));
		}

		@Test
		void isEnabled_null() {
			assertFalse(languageHandler.isEnabled(null));
		}

		@Test
		void isEnabled_unconfigured_message() {
			assertFalse(languageHandler.isEnabled(MessageId.UNCONFIGURED_MESSAGE));
		}


		@Test
		void isEnabled_legacy_message() {
			assertTrue(languageHandler.isEnabled(MessageId.LEGACY_MESSAGE));
		}
	}

	@Nested
	class RepeatDelayTests {
		@Test
		void getRepeatDelay() {
			assertEquals(10, languageHandler.getRepeatDelay(MessageId.REPEAT_DELAYED_MESSAGE));
		}

		@Test
		void getRepeatDelay_null_parameter() {
			assertEquals(0, languageHandler.getRepeatDelay(null));
		}
	}

	@Nested
	class GetMessageTests {
		@Test
		void getMessage() {
			assertEquals("This is an enabled message", languageHandler.getMessage(MessageId.ENABLED_MESSAGE));
		}

		@Test
		void getMessage_null_parameter() {
			assertEquals("", languageHandler.getMessage(null));
		}

		@Test
		void getMessage_no_message_string() {
			assertEquals("", languageHandler.getMessage(MessageId.ENABLED_TITLE));
		}
	}

	@Nested
	class TitleTests {
		@Test
		void getTitle() {
			assertEquals("This is an enabled title", languageHandler.getTitle(MessageId.ENABLED_TITLE));
		}

		@Test
		void getTitle_null() {
			assertEquals("", languageHandler.getTitle(null));
		}

		@Test
		void getTitle_no_title_configured() {
			assertEquals("", languageHandler.getTitle(MessageId.ENABLED_MESSAGE));
		}

		@Test
		void getTitle_unconfigured_message() {
			assertEquals("", languageHandler.getTitle(MessageId.UNCONFIGURED_MESSAGE));
		}
	}


	@Nested
	class SubtitleTests {
		@Test
		void getSubtitle() {
			assertEquals("This is an enabled subtitle", languageHandler.getSubtitle(MessageId.ENABLED_SUBTITLE));
		}

		@Test
		void getSubtitle_null_parameter() {
			assertEquals("", languageHandler.getSubtitle(null));
		}
	}

	@Nested
	class TitleFadeInTests {

		private final int defaultFadeIn = 10;

		@Test
		void getTitleFadeIn_default() {
			assertEquals(defaultFadeIn, languageHandler.getTitleFadeIn(MessageId.ENABLED_TITLE));
		}

		@Test
		void getTitleFadeIn_custom() {
			assertEquals(20, languageHandler.getTitleFadeIn(MessageId.CUSTOM_FADE_TITLE));
		}

		@Test
		void getTitleFadeIn_unconfigured_message() {
			assertEquals(defaultFadeIn, languageHandler.getTitleFadeIn(MessageId.UNCONFIGURED_MESSAGE));
		}

		@Test
		void getTitleFadeIn_non_integer_values() {
			assertEquals(defaultFadeIn, languageHandler.getTitleFadeIn(MessageId.NON_INT_TITLE_FADE_VALUES));
		}

		@Test
		void getTitleFadeIn_null() {
			assertEquals(defaultFadeIn, languageHandler.getTitleFadeIn(null));
		}
	}

	@Nested
	class TitleStayTests {

		private final int defaultStay = 70;

		@Test
		void getTitleStay_default() {
			assertEquals(defaultStay, languageHandler.getTitleStay(MessageId.ENABLED_TITLE));
		}

		@Test
		void getTitleStay_custom() {
			assertEquals(140, languageHandler.getTitleStay(MessageId.CUSTOM_FADE_TITLE));
		}

		@Test
		void getTitleFadeIn_unconfigured_message() {
			assertEquals(defaultStay, languageHandler.getTitleStay(MessageId.UNCONFIGURED_MESSAGE));
		}

		@Test
		void getTitleFadeIn_non_integer_values() {
			assertEquals(defaultStay, languageHandler.getTitleStay(MessageId.NON_INT_TITLE_FADE_VALUES));
		}

		@Test
		void getTitleStay_null() {
			assertEquals(defaultStay, languageHandler.getTitleStay(null));
		}
	}

	@Nested
	class TitleFadeOutTests {

		private final int defaultFadeOut = 20;

		@Test
		void getTitleFadeOut_default() {
			assertEquals(defaultFadeOut, languageHandler.getTitleFadeOut(MessageId.ENABLED_TITLE));
		}

		@Test
		void getTitleFadeOut_custom() {
			assertEquals(40, languageHandler.getTitleFadeOut(MessageId.CUSTOM_FADE_TITLE));
		}

		@Test
		void getTitleFadeIn_unconfigured_message() {
			assertEquals(defaultFadeOut, languageHandler.getTitleFadeOut(MessageId.UNCONFIGURED_MESSAGE));
		}

		@Test
		void getTitleFadeIn_non_integer_values() {
			assertEquals(defaultFadeOut, languageHandler.getTitleFadeOut(MessageId.NON_INT_TITLE_FADE_VALUES));
		}

		@Test
		void getTitleFadeOut_null() {
			assertEquals(defaultFadeOut, languageHandler.getTitleFadeOut(null));
		}
	}

	@Nested
	class ItemMetadataTests {
		@Test
		void getItemName() {
			assertEquals("§aTest Item", languageHandler.getItemName().orElse("fail"));
		}

		@Test
		void getItemName_null() {
			plugin.getConfig().set("item-name", null);
			assertNull(plugin.getConfig().getString("item-name"));
			assertEquals("§aTest Item", languageHandler.getItemName(null).orElse("fail"));
			// this test still passes because the null config entry is supplanted by the default config
		}

		@Test
		void getItemNamePlural() {
			assertEquals("§aTest Items", languageHandler.getItemNamePlural().orElse("fail"));
		}

		@Test
		void getInventoryItemName() {
			assertEquals("§aInventory Item", languageHandler.getInventoryItemName().orElse("fail"));
		}

		@Test
		void getItemLore() {
			assertEquals(List.of("§elore line 1", "§elore line 2"), languageHandler.getItemLore());
		}
	}

	@Nested
	class LocationNameTests {
		@Test
		void getSpawnDisplayName() {
			assertEquals("§aSpawn", languageHandler.getSpawnDisplayName().orElse("fail"));
		}

		@Test
		void getHomeDisplayName() {
			assertEquals("§aHome", languageHandler.getHomeDisplayName().orElse("fail"));
		}
	}

	@Nested
	class TimeStringTests {
		@Test
		void getTimeString_with_singular_units() {
			// test duration
			long duration = DAYS.toMillis(1) + HOURS.toMillis(1) + MINUTES.toMillis(1) + SECONDS.toMillis(1);
			assertEquals("1 day 1 hour 1 minute 1 second", languageHandler.getTimeString(duration));
		}

		@Test
		void getTimeString_with_plural_units() {
			long duration = DAYS.toMillis(2) + HOURS.toMillis(2) + MINUTES.toMillis(2) + SECONDS.toMillis(2);
			assertEquals("2 days 2 hours 2 minutes 2 seconds", languageHandler.getTimeString(duration));
		}

		@Test
		void getTimeString_with_unlimited_time() {
			assertEquals("unlimited time", languageHandler.getTimeString(-1));
		}

		@Test
		void getTimeString_with_null_timeUnit() {
			long duration = DAYS.toMillis(1) + HOURS.toMillis(1) + MINUTES.toMillis(1) + SECONDS.toMillis(1);
			assertEquals("1 day 1 hour 1 minute 1 second", languageHandler.getTimeString(duration, null));
		}

		@Test
		void getTimeString_with_less_than_second() {
			assertEquals("less than one second", languageHandler.getTimeString(999));
		}
	}

	@Test
	void getString() {
		assertEquals("an arbitrary string", languageHandler.getString("ARBITRARY_STRING").orElse("fail"));
	}

	@Test
	void getStringList() {
		assertTrue(languageHandler.getStringList("ARBITRARY_STRING_LIST").containsAll(List.of("item 1", "item 2", "item 3")));
	}

	@Disabled
	@Nested
	class mockServerWorldTests {
		@Test
		void getWorlds_test_for_empty() {
			assertFalse(server.getWorlds().isEmpty());
		}
		@Test
		void addSimpleWorld_test_for_null() {
			assertNotNull(server.addSimpleWorld("test_world"));
		}
		@Test
		void addPlayerTest() {
			assertNotNull(server.addPlayer("player1"));
		}
	}


	@Nested
	class WorldNameTests {
		@Disabled
		@Test
		void getWorldNameTest() {
			World world = server.getWorld("world");
			Optional<String> optionalWorldName = languageHandler.getWorldName(world);
			assertNotNull(world, "The default mock world is null.");
			assertTrue(optionalWorldName.isPresent());
			assertEquals("world", optionalWorldName.get());
		}

		@Test
		void getWorldName_null() {
			Optional<String> optionalWorldName = languageHandler.getWorldName(null);
			assertTrue(optionalWorldName.isEmpty());
		}
	}

	@Nested
	class WorldAliasTests {

		@Disabled
		@Test
		void getWorldAliasTest() {
			World world = server.getWorld("world");
			assertTrue(languageHandler.getWorldAlias(world).isEmpty());
		}

		@Test
		void getWorldAliasTest_null() {
			assertTrue(languageHandler.getWorldAlias(null).isEmpty());
		}
	}

	@Test
	void reload() {
		languageHandler.reload();
		// test that at least one message key exists after reload
		assertFalse(languageHandler.getMessageKeys().isEmpty());
		// test that a specific message key exists after reload
		assertTrue(languageHandler.getMessageKeys().contains("ENABLED_MESSAGE"));
	}
}
