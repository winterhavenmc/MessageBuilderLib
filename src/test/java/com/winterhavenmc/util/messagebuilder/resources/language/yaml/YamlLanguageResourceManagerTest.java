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
	YamlLanguageResourceManager resourceManager;
	Configuration languageConfiguration;
	FileConfiguration pluginConfiguration;


	@BeforeEach
	void setUp() {

		// create real plugin config
		pluginConfiguration = new YamlConfiguration();
		pluginConfiguration.set("locale", "en-US");
		pluginConfiguration.set("language", "en-US");

		when(pluginMock.getConfig()).thenReturn(pluginConfiguration);

		when(pluginMock.getLogger()).thenReturn(Logger.getLogger(this.getClass().getName()));

		// create real language configuration
		languageConfiguration = MockUtility.loadConfigurationFromResource("language/en-US.yml");

		// instantiate real language handler with mocked parameters
		resourceManager = YamlLanguageResourceManager.getInstance(pluginMock, languageResourceLoaderMock);
	}


	@AfterEach
	void tearDown() {
		pluginMock = null;
		languageResourceLoaderMock = null;
		resourceManager = null;
		languageConfiguration = null;
		pluginConfiguration = null;
	}


	@Test
	void testLanguageHandler_not_null() {
		assertNotNull(languageResourceLoaderMock);
	}


	@Test
	void testGetConfiguredLanguage() {
		// Act & Assert
		assertEquals("en-US", resourceManager.getConfiguredLanguage());
	}

	@Test
	void testGetConfigurationSupplier() {
		// Arrange & Act
		YamlConfigurationSupplier yamlConfigurationSupplier = resourceManager.getConfigurationSupplier();

		// Assert
		assertNotNull(yamlConfigurationSupplier);
	}


	@Nested
	@Disabled
	class ReloadTests {
		//TODO: confirm this test will always receive an identical configuration object from the loader
		@Test
		void testReload_same_config() {
			// Arrange
			when(languageResourceLoaderMock.loadConfiguration()).thenReturn(languageConfiguration);

			// Act
			boolean success = resourceManager.reload();

			// Assert
			assertFalse(success);

			// Verify
			verify(languageResourceLoaderMock, atLeastOnce()).loadConfiguration();
		}

		//TODO: test the reload method when a different configuration is returned from the loader
		@Test
		void testReload_different_config() {
			languageConfiguration = new YamlConfiguration();
			languageConfiguration.set("test_key", "test_value");

			// Arrange
			when(languageResourceLoaderMock.loadConfiguration()).thenReturn(languageConfiguration);

			// Act
			boolean success = resourceManager.reload();

			// Assert
			assertTrue(success);
		}

		//TODO: test when the new configuration from the loader is null
		@Disabled
		@Test
		void testReload_fail() {
			// Arrange
			lenient().when(languageResourceLoaderMock.reload()).thenReturn(null);

			// Act
			boolean success = resourceManager.reload();

			// Assert
			assertTrue(success);
		}
	}

}
