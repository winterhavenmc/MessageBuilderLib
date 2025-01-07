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

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.util.Locale;
import java.util.logging.Logger;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class YamlLanguageHandlerTest {

	@Mock private Plugin pluginMock;
	@Mock private YamlLanguageFileLoader languageFileLoaderMock;

	// real language handler
	private YamlLanguageHandler languageHandler;
	private FileConfiguration languageConfig;
	private FileConfiguration pluginConfig;


	@BeforeEach
	public void setUp() throws IOException {

		// create real plugin config
		pluginConfig = new YamlConfiguration();
		pluginConfig.set("locale", "en-US");
		pluginConfig.set("language", "en-US");

		// create real language configuration
		languageConfig = new YamlConfiguration();
		languageConfig.set("ITEMS.DEFAULT.NAME.SINGULAR", "default_item");
		languageConfig.set("ITEMS.DEFAULT.NAME.PLURAL", "default_items");

//		when(pluginMock.getLogger()).thenReturn(Logger.getLogger(getClass().getName()));

//		when(pluginMock.getConfig()).thenReturn(pluginConfig);
//		when(languageFileLoaderMock.getConfiguration()).thenReturn(languageConfig);

		// instantiate real language handler with mocked parameters
		languageHandler = new YamlLanguageHandler(pluginMock, languageFileLoaderMock);
	}


	@AfterEach
	public void tearDown() {
		pluginMock = null;
		languageFileLoaderMock = null;
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
			// Arrange
			when(pluginMock.getConfig()).thenReturn(pluginConfig);

			// Act & Assert
			assertEquals("en-US", pluginMock.getConfig().getString("locale"));

			// Verify
			verify(pluginMock, atLeastOnce()).getConfig();
		}

		@Test
		void getLanguageSetting_test() {
			// Arrange
			when(pluginMock.getConfig()).thenReturn(pluginConfig);

			// Act & Assert
			assertEquals("en-US", pluginMock.getConfig().getString("language"));

			// Verify
			verify(pluginMock, atLeastOnce()).getConfig();
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
			YamlLanguageHandler languageHandler = new YamlLanguageHandler(pluginMock, languageFileLoaderMock);
			assertNotNull(languageHandler);
			assertTrue(languageHandler.isPluginSet());
			assertTrue(languageHandler.isFileLoaderSet());
		}
	}

	@Nested
	class setterTests {
		@Test
		void setterTest_plugin() {
			// Arrange
			YamlLanguageHandler yamlLanguageHandler = new YamlLanguageHandler();
			assertFalse(yamlLanguageHandler.isPluginSet(), "the plugin field is not null and it should be.");

			// Act
			yamlLanguageHandler.setPlugin(pluginMock);

			// Assert
			assertTrue(yamlLanguageHandler.isPluginSet(), "the plugin field is null and it should not be.");
		}

		@Test
		void setterTest_fileLoader() {
			// Arrange
			YamlLanguageHandler yamlLanguageHandler = new YamlLanguageHandler();
			assertFalse(yamlLanguageHandler.isFileLoaderSet(), "the fileLoader field is not null.");

			// Act
			yamlLanguageHandler.setFileLoader(languageFileLoaderMock);

			// Assert
			assertTrue(yamlLanguageHandler.isFileLoaderSet(), "the fileLoader field is null.");
		}
	}

	@Test
	void getConfigurationTest() {
		// Arrange
		when(languageFileLoaderMock.getConfiguration()).thenReturn(languageConfig);

		// Act
		languageHandler.reload(); //TODO: check if it's normal that languageHandler.getConfiguration() returned null before being reloaded

		// Assert
		assertNotNull(languageHandler.getConfiguration());

		// Verify
		verify(languageFileLoaderMock, atLeastOnce()).getConfiguration();
	}

	@Test
	void getConfigLanguageTest() {
		// Arrange
		when(pluginMock.getConfig()).thenReturn(pluginConfig);

		// Act & Assert
		assertEquals("en-US", languageHandler.getConfigLanguage());

		// Verify
		verify(pluginMock, atLeastOnce()).getConfig();
	}

	@Test
	void reloadTest() {
		// Arrange
		when(languageFileLoaderMock.getConfiguration()).thenReturn(languageConfig);

		// Act
		languageHandler.reload();

		// Assert
		assertNotNull(languageHandler.getConfiguration());

		// Verify
		verify(languageFileLoaderMock, atLeastOnce()).reload();
	}

	@Disabled
	@Test
	void reloadTest_fail() {
		when(languageFileLoaderMock.getConfiguration()).thenReturn(null);
		when(pluginMock.getLogger()).thenReturn(Logger.getLogger(getClass().getName()));
		languageHandler.reload();
		//TODO: test for logger output here; update: it's throwing an npe now after updating mocks
		// match string "WARNING: The configuration could not be reloaded. Keeping existing configuration."
		assertNotNull(languageHandler.getConfiguration());
		verify(languageFileLoaderMock, atLeastOnce()).getConfiguration();
	}

}
