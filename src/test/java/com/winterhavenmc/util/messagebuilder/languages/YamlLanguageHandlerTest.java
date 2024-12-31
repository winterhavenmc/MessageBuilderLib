/*
 * Copyright (c) 2022-2024 Tim Savage.
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

package com.winterhavenmc.util.messagebuilder.languages;

import com.winterhavenmc.util.messagebuilder.util.MockUtility;

import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.util.Locale;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class YamlLanguageHandlerTest {

	private Plugin mockPlugin;
	private YamlLanguageFileLoader mockLanguageFileLoader;

	private YamlLanguageHandler languageHandler;


	@BeforeEach
	public void setUp() throws IOException {
		mockPlugin = MockUtility.createMockPlugin();
		mockLanguageFileLoader = MockUtility.createMockLanguageFileLoader();

		// create a real language handler
		languageHandler = new YamlLanguageHandler(mockPlugin, mockLanguageFileLoader);
	}


	@AfterEach
	public void tearDown() {
		mockPlugin = null;
		mockLanguageFileLoader = null;
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
//		@Test
//		void dataDirectoryTest_exists_static() {
//			assertTrue(MockUtility.getDataFolder().isDirectory());
//		}

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
			assertFalse(languageHandler.isFileLoaderSet());
		}

		@Test
		void constructorTest_three_parameter() {
			YamlLanguageHandler languageHandler = new YamlLanguageHandler(mockPlugin, mockLanguageFileLoader);
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

		@Test
		void setterTest_fileLoader() {
			YamlLanguageHandler yamlLanguageHandler = new YamlLanguageHandler();
			assertFalse(yamlLanguageHandler.isFileLoaderSet(), "the fileLoader field is not null.");
			yamlLanguageHandler.setFileLoader(mockLanguageFileLoader);
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
		verify(mockPlugin, atLeastOnce()).getConfig();
	}

	@Test
	void reloadTest() {
		languageHandler.reload();
		assertNotNull(languageHandler.getConfiguration());
		verify(mockLanguageFileLoader, atLeastOnce()).reload();
	}

	@Test
	void reloadTest_fail() {
		when(mockLanguageFileLoader.getConfiguration()).thenReturn(null);
		languageHandler.reload();
		//TODO: test for logger output here
		// match string "WARNING: The configuration could not be reloaded. Keeping existing configuration."
		assertNotNull(languageHandler.getConfiguration());
		verify(mockLanguageFileLoader, atLeastOnce()).getConfiguration();
	}

}
