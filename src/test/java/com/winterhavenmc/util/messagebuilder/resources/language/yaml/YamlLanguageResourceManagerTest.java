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

package com.winterhavenmc.util.messagebuilder.resources.language.yaml;

import com.winterhavenmc.util.messagebuilder.util.MockUtility;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.Locale;
import java.util.logging.Logger;

import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class YamlLanguageResourceManagerTest {

	@Mock Plugin pluginMock;
	@Mock YamlLanguageResourceLoader languageResourceLoaderMock;

	// real language handler
	YamlLanguageResourceManager languageHandler;
	Configuration languageConfiguration;
	FileConfiguration pluginConfiguration;


	@BeforeEach
	void setUp() {

		// create real plugin config
		pluginConfiguration = new YamlConfiguration();
		pluginConfiguration.set("locale", "en-US");
		pluginConfiguration.set("language", "en-US");

		when(pluginMock.getConfig()).thenReturn(pluginConfiguration);
		assertNotNull(pluginMock.getConfig());

		when(pluginMock.getLogger()).thenReturn(Logger.getLogger(this.getClass().getName()));
		assertNotNull(pluginMock.getLogger());

		// create real language configuration
		languageConfiguration = MockUtility.loadConfigurationFromResource("language/en-US.yml");

		// instantiate real language handler with mocked parameters
		languageHandler = YamlLanguageResourceManager.getInstance(pluginMock);
		assertNotNull(languageHandler);
	}


	@AfterEach
	void tearDown() {
		pluginMock = null;
		languageResourceLoaderMock = null;
		languageHandler = null;
		languageConfiguration = null;
		pluginConfiguration = null;
	}


	@Test
	void testLanguageHandler_not_null() {
		assertNotNull(languageResourceLoaderMock);
	}


	@Test
	void getConfigurationSupplier() {
		// Act
		YamlConfigurationSupplier configurationSupplier = languageHandler.getConfigurationSupplier();

		// Assert
		assertNotNull(configurationSupplier);
	}


	@Nested
	class LanguageTests {
		@Test
		void testGetLanguage() {
			assertEquals(Locale.US.toLanguageTag(), languageHandler.getConfiguredLanguage());
		}
	}


	@Test
	void testGetConfigurationSupplier() {
		// Arrange & Act
		YamlConfigurationSupplier yamlConfigurationSupplier = new YamlConfigurationSupplier(languageConfiguration);

		// Assert
		assertNotNull(yamlConfigurationSupplier);
		assertNotNull(yamlConfigurationSupplier.get());
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
			// Act
			boolean success = languageHandler.reload();

			// Assert
			assertTrue(success);
		}


		@Test
		void reloadTest_fail() {
			// Act
			boolean success = languageHandler.reload();

			// Assert
			assertTrue(success);
		}
	}

}
