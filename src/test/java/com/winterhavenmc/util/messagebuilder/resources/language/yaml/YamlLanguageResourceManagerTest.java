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
	@Mock YamlLanguageResourceInstaller languageResourceInstallerMock;
	@Mock YamlLanguageResourceLoader languageResourceLoaderMock;

	// real language handler
	YamlLanguageResourceManager resourceManager;
	Configuration languageConfiguration;
	FileConfiguration pluginConfiguration;


	@BeforeEach
	void setUp() {
		// create real plugin config
		pluginConfiguration = new YamlConfiguration();
		pluginConfiguration.set("language", "en-US");
		pluginConfiguration.set("locale", "en-US");

		// create real language configuration
		languageConfiguration = MockUtility.loadConfigurationFromResource(Option.RESOURCE_LANGUAGE_EN_US_YML.toString());

		// instantiate real language handler with mocked parameters
		resourceManager = YamlLanguageResourceManager.getInstance(languageResourceInstallerMock, languageResourceLoaderMock);

	}


	@AfterEach
	void tearDown() {
		pluginConfiguration = null;
		pluginMock = null;
		languageResourceLoaderMock = null;
		resourceManager = null;
		languageConfiguration = null;
	}


	@Test
	void testLanguageConfiguration_not_null() {
		assertNotNull(this.languageConfiguration);
	}


	@Test
	void testGetConfigurationSupplier() {
		// Arrange
		when(languageResourceLoaderMock.loadConfiguration()).thenReturn(languageConfiguration);

		//  Act
		resourceManager.reload();
		YamlConfigurationSupplier configurationSupplier = resourceManager.getConfigurationSupplier();

		// Assert
		assertNotNull(configurationSupplier);

		// Verify
		verify(languageResourceLoaderMock, atLeastOnce()).loadConfiguration();
	}


	@Nested
	class ReloadTests {
		@Test
		void testReload() {
			// Arrange & Act
			boolean success = resourceManager.reload();

			// Assert
			assertTrue(success);
			assertNotNull(resourceManager.getConfigurationSupplier());
		}


		@Test
		void testReload_new_config() {
			// Arrange
			FileConfiguration newLanguageConfiguration = new YamlConfiguration();
			newLanguageConfiguration.set("test_key", "test_value");

			// Act
			boolean success = resourceManager.reload();

			// Assert
			assertTrue(success);
			assertNotNull(resourceManager.getConfigurationSupplier());
		}
	}

}
