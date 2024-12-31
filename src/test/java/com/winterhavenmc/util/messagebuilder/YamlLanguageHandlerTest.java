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

import com.winterhavenmc.util.messagebuilder.languages.LanguageFileLoader;
import com.winterhavenmc.util.messagebuilder.util.MockingUtilities;

import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.util.Locale;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class YamlLanguageHandlerTest {

	private Plugin mockPlugin;
	private LanguageFileLoader mocklanguageFileLoader;

	private YamlLanguageHandler languageHandler;


	@BeforeAll
	public static void preSetUp() {
		MockingUtilities.verifyTempDir();
	}

	@BeforeEach
	public void setUp() throws IOException {

		// create mock plugin
		mockPlugin = MockingUtilities.createMockPlugin();

		// create mock file loader
		mocklanguageFileLoader = mock(LanguageFileLoader.class, "MockLanguageFileLoader");
		when(mocklanguageFileLoader.getConfiguration()).thenReturn(MockingUtilities.loadConfigurationFromResource("language/en-US.yml"));

		// create a real language handler
		languageHandler = new YamlLanguageHandler(mockPlugin, mocklanguageFileLoader);
	}


	@AfterEach
	public void tearDown() {
		mockPlugin = null;
		mocklanguageFileLoader = null;
		languageHandler = null;
	}

	@Nested
	class LocaleTests {
		@Test
		void getLocaleTest() {
			assertEquals(Locale.US, languageHandler.getLocale());
		}

		@Test
		void setLocaleTest() {
			assertEquals(Locale.US, languageHandler.getLocale());
			languageHandler.setLocale(Locale.UK);
			assertEquals(Locale.UK, languageHandler.getLocale());
		}
	}

	@Nested
	class testingEnvironmentTests {
		@Test
		void dataDirectoryTest_exists_static() {
			assertTrue(MockingUtilities.getDataFolder().isDirectory());
		}

		@Test
		void languageHandlerTest() {
			assertNotNull(languageHandler);
		}
	}

	@Nested
	class mockPluginConfigTests {
		@Test
		void getLocaleSetting_test() {
			assertEquals("en-US", mockPlugin.getConfig().getString("locale"));
		}
		@Test
		void getLanguageSetting_test() {
			assertEquals("en-US", mockPlugin.getConfig().getString("language"));
		}
	}


	@Nested
	class constructorTests {
		@Test
		void constructorTest_no_parameter() {
			YamlLanguageHandler languageHandler = new YamlLanguageHandler();
			assertNotNull(languageHandler);
			assertFalse(languageHandler.isPluginSet());
//			assertFalse(languageHandler.isFileInstallerSet());
			assertFalse(languageHandler.isFileLoaderSet());
		}

		@Test
		void constructorTest_three_parameter() {
			YamlLanguageHandler languageHandler = new YamlLanguageHandler(mockPlugin, mocklanguageFileLoader);
			assertNotNull(languageHandler);
			assertTrue(languageHandler.isPluginSet());
			assertTrue(languageHandler.isFileLoaderSet());
		}
	}

	@Nested
	class setterTests {
		@Test
		void setterTest_plugin() {
			YamlLanguageHandler yamlLanguageHandler = new YamlLanguageHandler();
			assertFalse(yamlLanguageHandler.isPluginSet(), "the plugin field is not null and it should be.");
			yamlLanguageHandler.setPlugin(mockPlugin);
			assertTrue(yamlLanguageHandler.isPluginSet(), "the plugin field is null and it should not be.");
		}

//		@Test
//		void setterTest_fileInstaller() {
//			YamlLanguageHandler yamlLanguageHandler = new YamlLanguageHandler();
//			assertFalse(yamlLanguageHandler.isFileInstallerSet(), "the fileInstaller field is not null and it should be.");
//			YamlLanguageFileInstaller mockLanguageFileInstaller = mock(YamlLanguageFileInstaller.class, "MockInstaller");
//			yamlLanguageHandler.setFileInstaller(mockLanguageFileInstaller);
//			assertTrue(yamlLanguageHandler.isFileInstallerSet(), "the fileLoader field is null and it should not be.");
//		}

		@Test
		void setterTest_fileLoader() {
			YamlLanguageHandler yamlLanguageHandler = new YamlLanguageHandler();
			assertFalse(yamlLanguageHandler.isFileLoaderSet(), "the fileLoader field is not null.");
			yamlLanguageHandler.setFileLoader(mocklanguageFileLoader);
			assertTrue(yamlLanguageHandler.isFileLoaderSet(), "the fileLoader field is null.");
		}
	}

	@Test
	void getConfigurationTest() {
		assertNotNull(languageHandler.getConfiguration());
	}

	@Test
	void getConfigLanguageTest() {
		assertEquals("en-US", languageHandler.getConfigLanguage());
	}

	@Test
	void reloadTest() {
		Configuration configuration = languageHandler.getConfiguration();
		assertNotNull(configuration);
		configuration = null;
		assertNull(configuration);
		languageHandler.reload();
		configuration = languageHandler.getConfiguration();
		assertNotNull(configuration);
	}

//	@Test
//	void reload() {
//		// test that at least one message key exists before reload
//		assertFalse(languageHandler.getMessageKeys().isEmpty());
//		// test that a specific message key exists before reload
//		assertTrue(languageHandler.getMessageKeys().contains("ENABLED_MESSAGE"));
//		languageHandler.reload();
//		// test that at least one message key exists after reload
//		assertFalse(languageHandler.getMessageKeys().isEmpty());
//		// test that a specific message key exists after reload
//		assertTrue(languageHandler.getMessageKeys().contains("ENABLED_MESSAGE"));
//	}

//	static Configuration getLanguageConfiguration() {
//		FileConfiguration configuration = new YamlConfiguration();
//		try {
//			configuration.loadFromString(getStringFile());
//		} catch (InvalidConfigurationException e) {
//			throw new RuntimeException(e);
//		}
//		return configuration;
//	}

//	static String getStringFile() {
//		return
//"""
//# Language configuration file for MessageBuilderLib v1.21.0
//
//##########
//# Settings
//##########
//SETTINGS:
//  DELIMITERS:
//    LEFT: '%'
//    RIGHT: '%'
//
//
//########################
//# Location Display Names
//########################
//LOCATIONS:
//  SPAWN:
//    DISPLAY_NAME: '&aSpawn'
//  HOME:
//    DISPLAY_NAME: '&aHome'
//
//
//##############
//# Item Strings
//##############
//ITEMS:
//  DEFAULT:
//    NAME:
//      SINGULAR: 'Default Item'
//      PLURAL: 'Default Items'
//    INVENTORY_NAME:
//      SINGULAR: 'Default Inventory Item'
//      PLURAL:  'Default Inventory ItemS'
//    ITEM_LORE:
//      - '&edefault lore line 1'
//      - '&edefault lore line 2'
//
//  TEST_ITEM_1:
//    NAME:
//      SINGULAR: '&aTest Item'
//      PLURAL: '&aTest Items'
//    INVENTORY_NAME:
//      SINGULAR: '&aInventory Item'
//      PLURAL: '&aInventory Item'
//    ITEM_LORE:
//      - '&etest1 lore line 1'
//      - '&etest1 lore line 2'
//
//  TEST_ITEM_2:
//    NAME:
//      SINGULAR: '&aTest Item 2'
//      PLURAL: '&aTest Items 2'
//    INVENTORY_NAME:
//      SINGULAR: '&aInventory Item 2'
//      PLURAL: '&aInventory Items 2'
//    LORE:
//      - '&etest2 lore line 1'
//      - '&etest2 lore line 2'
//
//  UNDEFINED_ITEM_NAME:
//    NAME:
//      PLURAL: '&aTest Items'
//    INVENTORY_NAME:
//      SINGULAR: '&aInventory Item'
//      PLURAL: '&aInventory Items'
//    LORE:
//      - '&elore line 1'
//      - '&elore line 2'
//
//  UNDEFINED_NAME_PLURAL:
//    NAME:
//      SINGULAR: '&aTest Item'
//      PLURAL: '&aTest Items'
//    INVENTORY_NAME:
//      SINGULAR: '&aInventory Item'
//      PLURAL: '&aInventory ItemS'
//    LORE:
//      - '&elore line 1'
//      - '&elore line 2'
//
//  UNDEFINED_INVENTORY_ITEM_NAME:
//    NAME:
//      SINGULAR: '&aTest Item'
//      PLURAL: '&aTest Items'
//    INVENTORY_NAME:
//      PLURAL: '&aInventory ItemS'
//    ITEM_LORE:
//      - '&elore line 1'
//      - '&elore line 2'
//
//  UNDEFINED_INVENTORY_ITEM_NAME_PLURAL:
//    NAME:
//      SINGULAR: '&aTest Item'
//      PLURAL: '&aTest Items'
//    INVENTORY_NAME:
//      SINGULAR: '&aInventory Item'
//    ITEM_LORE:
//      - '&elore line 1'
//      - '&elore line 2'
//
//  UNDEFINED_ITEM_LORE:
//    NAME:
//      SINGULAR: '&aTest Item'
//      PLURAL: '&aTest Items'
//    INVENTORY_NAME:
//      SINGULAR: '&aInventory Item'
//      PLURAL: '&aInventory Item'
//
//
//##############
//# Time strings
//##############
//TIME_STRINGS:
//  DAY: 'day'
//  DAY_PLURAL: 'days'
//  HOUR: 'hour'
//  HOUR_PLURAL: 'hours'
//  MINUTE: 'minute'
//  MINUTE_PLURAL: 'minutes'
//  SECOND: 'second'
//  SECOND_PLURAL: 'seconds'
//  UNLIMITED: 'unlimited time'
//  LESS_THAN_ONE: 'less than one'
//
//
//###################
//# Arbitrary Strings
//###################
//ARBITRARY_STRING: 'an arbitrary string'
//
//ARBITRARY_STRING_LIST:
//  - "item 1"
//  - "item 2"
//  - "item 3"
//
//
//##########
//# Messages
//##########
//MESSAGES:
//  ENABLED_MESSAGE:
//    enabled: true
//    message: "This is an enabled message"
//
//  DISABLED_MESSAGE:
//    enabled: false
//    message: "This is a disabled message"
//
//  REPEAT_DELAYED_MESSAGE:
//    enabled: true
//    message: "this is a repeat delayed message"
//    repeat-delay: 10
//
//  MACRO_MESSAGE:
//    enabled: true
//    message: "This is a message with a macro replacement %ITEM_NAME%."
//
//  MACRO_MESSAGE_CURLY_BRACE_DELIMITERS:
//    enabled: true
//    message: "This is a message with a macro replacement {ITEM_NAME}."
//
//  ENABLED_TITLE:
//    enabled: true
//    title: "This is an enabled title"
//
//  DISABLED_TITLE:
//    enabled: false
//    title: "This is a disabled title"
//
//  ENABLED_SUBTITLE:
//    enabled: true
//    subtitle: "This is an enabled subtitle"
//
//  DISABLED_SUBTITLE:
//    enabled: false
//    subtitle: "This is a disabled subtitle"
//
//  CUSTOM_FADE_TITLE:
//    enabled: true
//    title: "This is a title with custom fade values"
//    title-fade-in: 20
//    title-stay: 140
//    title-fade-out: 40
//
//  NON_INT_TITLE_FADE_VALUES:
//    enabled: true
//    title: "This is a title with non-integer fade values"
//    title-fade-in: "STRING"
//    title-stay: "STRING"
//    title-fade-out: "STRING"
//
//  DURATION_MESSAGE:
//    enabled: true
//    message: "Duration is %DURATION%"
//
//
//  # all number fields are unique from each other and defaults(20, 70, 10)
//  ALL_FIELDS_PRESENT:
//    message: "This entry has all fields present - message"
//    repeat-delay: 14
//    title: "This entry has all fields present - title"
//    title_fade_in: 24
//    title_stay: 34
//    title_fade_out: 44
//    subtitle: "This entry has all fields present - subtitle"
//
//
//  UNDEFINED_FIELD_ENABLED:
//    message: "This entry has no enabled field"
//
//  UNDEFINED_FIELD_MESSAGE:
//    enabled: true
//    title: "This entry has no message field"
//
//  UNDEFINED_FIELD_REPEAT_DELAY:
//    enabled: true
//    message: "This entry has no repeat-delay field"
//
//  UNDEFINED_FIELD_TITLE:
//    enabled: true
//    message: "This entry has no title field"
//
//  UNDEFINED_FIELD_TITLE_FADE_IN:
//    enabled: true
//    message: "This entry has no title-fade-in field"
//
//  UNDEFINED_FIELD_TITLE_STAY:
//    enabled: true
//    message: "This entry has no title-stay field"
//
//  UNDEFINED_FIELD_TITLE_FADE_OUT:
//    enabled: true
//    message: "This entry has no title-fade-out field"
//
//  UNDEFINED_FIELD_SUBTITLE:
//    enabled: true
//    message: "This entry has no subtitle field"
//		""";
//	}

}
