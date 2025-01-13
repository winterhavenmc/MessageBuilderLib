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

package com.winterhavenmc.util.messagebuilder.language.yaml;

import com.winterhavenmc.util.messagebuilder.util.MockUtility;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;
import java.util.Locale;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class YamlLanguageResourceHandlerTest {

	@Mock private YamlLanguageResourceLoader languageFileLoaderMock;

	// real language handler
	private YamlLanguageResourceHandler languageHandler;
	private Configuration languageConfiguration;
	private FileConfiguration pluginConfig;


	@BeforeEach
	public void setUp() throws IOException {

		// create real plugin config
		pluginConfig = new YamlConfiguration();
		pluginConfig.set("locale", "en-US");
		pluginConfig.set("language", "en-US");

		// create real language configuration
		languageConfiguration = MockUtility.loadConfigurationFromResource("language/en-US.yml");


		// instantiate real language handler with mocked parameters
		languageHandler = new YamlLanguageResourceHandler(pluginConfig, languageFileLoaderMock);
	}


	@AfterEach
	public void tearDown() {
		pluginConfig = null;
		languageFileLoaderMock = null;
		languageHandler = null;
	}


	@Test
	void testLanguageHandler_not_null() {
		assertNotNull(languageHandler);
	}


	@Nested
	class LocaleTests {
		@Test
		void testGetLocale() {
			assertEquals(Locale.US, languageHandler.getLocale());
		}

		@Test
		void TestSetLocale() {
			assertEquals(Locale.US, languageHandler.getLocale());
			languageHandler.setLocale(Locale.UK);
			assertEquals(Locale.UK, languageHandler.getLocale());
		}
	}

	@Nested
	class LanguageTests {
		@Test
		void testGetLanguage() {
			assertEquals(Locale.US.toLanguageTag(), languageHandler.getConfiguredLanguage());
		}
	}


	@Nested
	class constructorTests {
		@Test
		void constructorTest_no_parameter() {
			YamlLanguageResourceHandler languageHandler = new YamlLanguageResourceHandler();
			assertNotNull(languageHandler);
			assertFalse(languageHandler.isFileLoaderSet());
		}

		@Test
		void constructorTest_three_parameter() {
			YamlLanguageResourceHandler languageHandler = new YamlLanguageResourceHandler(pluginConfig, languageFileLoaderMock);
			assertNotNull(languageHandler);
			assertTrue(languageHandler.isFileLoaderSet());
		}
	}


	@Nested
	class setterTests {

		@Test
		void setterTest_fileLoader() {
			// Arrange
			YamlLanguageResourceHandler yamlLanguageResourceHandler = new YamlLanguageResourceHandler();
			assertFalse(yamlLanguageResourceHandler.isFileLoaderSet(), "the fileLoader field is not null.");

			// Act
			yamlLanguageResourceHandler.setFileLoader(languageFileLoaderMock);

			// Assert
			assertTrue(yamlLanguageResourceHandler.isFileLoaderSet(), "the fileLoader field is null.");
		}
	}

	//TODO: getConfiguration may go away, replaced by getConfigurationSuppler. refactor tests accordingly
	@Test
	void getConfigurationTest() {
		// Arrange
		when(languageFileLoaderMock.getConfiguration()).thenReturn(languageConfiguration);

		// Act
		languageHandler.reload(); //TODO: check if it's normal that languageHandler.getConfiguration() returned null before being reloaded

		// Assert
		assertNotNull(languageHandler.getPluginConfiguration());

		// Verify
		verify(languageFileLoaderMock, atLeastOnce()).getConfiguration();
	}

	@Test
	void testGetConfigurationSupplier() {
		// Arrange & Act
		YamlConfigurationSupplier yamlConfigurationSupplier = new YamlConfigurationSupplier(languageConfiguration);

		// Assert
		assertNotNull(yamlConfigurationSupplier);
		assertNotNull(yamlConfigurationSupplier.get());

		// Verify
		verify(languageFileLoaderMock, atLeastOnce()).getConfiguration();
	}

	@Test
	void getConfiguredLanguageTest() {
		// Act & Assert
		assertEquals("en-US", languageHandler.getConfiguredLanguage());
	}

	@Nested
	class ReloadTests {
		@Test
		void reloadTest() {
			// Arrange
			when(languageFileLoaderMock.getConfiguration()).thenReturn(languageConfiguration);

			// Act
			boolean success = languageHandler.reload();

			// Assert
			assertTrue(success);
			assertNotNull(languageHandler.getPluginConfiguration());

			// Verify
			verify(languageFileLoaderMock, atLeastOnce()).reload();
		}

		@Test
		void reloadTest_fail() {
			// Arrange
			when(languageFileLoaderMock.getConfiguration()).thenReturn(null);

			// Act
			boolean success = languageHandler.reload();

			// Assert
			assertFalse(success);
			assertNull(languageHandler.getPluginConfiguration());

			// Verify
			verify(languageFileLoaderMock, atLeastOnce()).getConfiguration();
		}
	}

}
