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
import org.junit.jupiter.api.*;

import java.util.List;

import static com.winterhavenmc.util.TimeUnit.*;
import static org.junit.jupiter.api.Assertions.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class YamlLanguageHandlerTest {

	@SuppressWarnings({"FieldCanBeLocal", "unused"})
	private ServerMock server;
	private PluginMain plugin;

	@BeforeAll
	public void setUp() {
		// Start the mock server
		server = MockBukkit.mock();

		// start the mock plugin
		plugin = MockBukkit.load(PluginMain.class);
	}

	@AfterAll
	public void tearDown() {
		// Stop the mock server
		MockBukkit.unmock();
	}

	@Test
	void getMessageKeys() {
		LanguageHandler languageHandler = new YamlLanguageHandler(plugin);
		assertFalse(languageHandler.getMessageKeys().isEmpty());
		assertTrue(languageHandler.getMessageKeys().contains("ENABLED_MESSAGE"));
	}

	@Test
	void isEnabled() {
		LanguageHandler languageHandler = new YamlLanguageHandler(plugin);
		assertTrue(languageHandler.isEnabled(MessageId.ENABLED_MESSAGE));
		assertFalse(languageHandler.isEnabled(MessageId.DISABLED_MESSAGE));
	}

	@Test
	void getRepeatDelay() {
		LanguageHandler languageHandler = new YamlLanguageHandler(plugin);
		assertEquals(10, languageHandler.getRepeatDelay(MessageId.REPEAT_DELAYED_MESSAGE));
	}

	@Test
	void getMessage() {
		LanguageHandler languageHandler = new YamlLanguageHandler(plugin);
		assertEquals("This is an enabled message", languageHandler.getMessage(MessageId.ENABLED_MESSAGE));
	}

	@Test
	void getTitle() {
		LanguageHandler languageHandler = new YamlLanguageHandler(plugin);
		assertEquals("This is an enabled title", languageHandler.getTitle(MessageId.ENABLED_TITLE));
	}

	@Test
	void getSubtitle() {
		LanguageHandler languageHandler = new YamlLanguageHandler(plugin);
		assertEquals("This is an enabled subtitle", languageHandler.getSubtitle(MessageId.ENABLED_SUBTITLE));
	}

	@Test
	void getTitleFadeIn() {
		LanguageHandler languageHandler = new YamlLanguageHandler(plugin);
		assertEquals(10, languageHandler.getTitleFadeIn(MessageId.ENABLED_TITLE));
		assertEquals(20, languageHandler.getTitleFadeIn(MessageId.CUSTOM_FADE_TITLE));
	}

	@Test
	void getTitleStay() {
		LanguageHandler languageHandler = new YamlLanguageHandler(plugin);
		assertEquals(70, languageHandler.getTitleStay(MessageId.ENABLED_TITLE));
		assertEquals(140, languageHandler.getTitleStay(MessageId.CUSTOM_FADE_TITLE));
	}

	@Test
	void getTitleFadeOut() {
		LanguageHandler languageHandler = new YamlLanguageHandler(plugin);
		assertEquals(20, languageHandler.getTitleFadeOut(MessageId.ENABLED_TITLE));
		assertEquals(40, languageHandler.getTitleFadeOut(MessageId.CUSTOM_FADE_TITLE));
	}

	@Test
	void getItemName() {
		LanguageHandler languageHandler = new YamlLanguageHandler(plugin);
		assertEquals("§aTest Item", languageHandler.getItemName().orElse("fail"));
	}

	@Test
	void getItemNamePlural() {
		LanguageHandler languageHandler = new YamlLanguageHandler(plugin);
		assertEquals("§aTest Items", languageHandler.getItemNamePlural().orElse("fail"));
	}

	@Test
	void getInventoryItemName() {
		LanguageHandler languageHandler = new YamlLanguageHandler(plugin);
		assertEquals("§aInventory Item", languageHandler.getInventoryItemName().orElse("fail"));
	}

	@Test
	void getItemLore() {
		LanguageHandler languageHandler = new YamlLanguageHandler(plugin);
		assertEquals(List.of("§elore line 1", "§elore line 2"), languageHandler.getItemLore());
	}

	@Test
	void getSpawnDisplayName() {
		LanguageHandler languageHandler = new YamlLanguageHandler(plugin);
		assertEquals("§aSpawn", languageHandler.getSpawnDisplayName().orElse("fail"));
	}

	@Test
	void getHomeDisplayName() {
		LanguageHandler languageHandler = new YamlLanguageHandler(plugin);
		assertEquals("§aHome", languageHandler.getHomeDisplayName().orElse("fail"));
	}

	@Test
	void getTimeString() {
		LanguageHandler languageHandler = new YamlLanguageHandler(plugin);

		long duration;

		// singular time units
		duration = DAYS.toMillis(1) + HOURS.toMillis(1) + MINUTES.toMillis(1) + SECONDS.toMillis(1);
		assertEquals("1 day 1 hour 1 minute 1 second", languageHandler.getTimeString(duration));

		// plural time units
		duration = DAYS.toMillis(2) + HOURS.toMillis(2) + MINUTES.toMillis(2) + SECONDS.toMillis(2);
		assertEquals("2 days 2 hours 2 minutes 2 seconds", languageHandler.getTimeString(duration));

		// unlimited time
		assertEquals("unlimited time", languageHandler.getTimeString(-1));
	}

	@Test
	void getString() {
		LanguageHandler languageHandler = new YamlLanguageHandler(plugin);
		assertEquals("an arbitrary string", languageHandler.getString("ARBITRARY_STRING").orElse("fail"));
	}

	@Test
	void getStringList() {
		LanguageHandler languageHandler = new YamlLanguageHandler(plugin);
		assertTrue(languageHandler.getStringList("ARBITRARY_STRING_LIST").containsAll(List.of("item 1", "item 2", "item 3")));
	}

	@Test
	void reload() {
	}

}
