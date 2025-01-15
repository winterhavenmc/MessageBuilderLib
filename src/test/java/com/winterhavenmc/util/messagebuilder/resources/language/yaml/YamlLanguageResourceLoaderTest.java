/*
 * Copyright (c) 2024-2025 Tim Savage.
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
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.InputStream;
import java.util.logging.Logger;

import static com.winterhavenmc.util.messagebuilder.util.MockUtility.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class YamlLanguageResourceLoaderTest {

	@Mock Plugin pluginMock;
	@Mock YamlLanguageResourceInstaller resourceInstallerMock;

	FileConfiguration pluginConfiguration;
	Configuration languageConfiguration;

	private YamlLanguageResourceLoader yamlLanguageResourceLoader;


	@BeforeEach
	public void setUp() {
		// create real plugin config
		pluginConfiguration = new YamlConfiguration();
		pluginConfiguration.set("locale", "en-US");
		pluginConfiguration.set("language", "en-US");

		// create real language configuration
		languageConfiguration = MockUtility.loadConfigurationFromResource("language/en-US.yml");

		// create new real file loader
		yamlLanguageResourceLoader = new YamlLanguageResourceLoader(pluginMock, resourceInstallerMock);
		yamlLanguageResourceLoader.setup();
	}

	@AfterEach
	public void tearDown() {
		pluginMock = null;
		pluginConfiguration = null;
		languageConfiguration = null;
		yamlLanguageResourceLoader = null;
	}


	@Test
	void FileLoaderNotNull() {
		assertNotNull(yamlLanguageResourceLoader);
	}

	@Test
	void getLanguageFilenameTest() {
		assertEquals("language/en-US.yml", YamlLanguageResourceLoader.getLanguageFilename("en-US"),
				"an incorrect filename was returned.");
		assertEquals("language/es-ES.yml", YamlLanguageResourceLoader.getLanguageFilename("es-ES"),
				"an incorrect filename was returned.");
		assertEquals("language/invalid_tag.yml", YamlLanguageResourceLoader.getLanguageFilename("invalid_tag"),
				"an incorrect filename was returned.");
	}


	@Test
	void testLoadConfiguration() {
		when(pluginMock.getConfig()).thenReturn(pluginConfiguration);
		when(pluginMock.getLogger()).thenReturn(Logger.getLogger(this.getClass().getName()));

		// Arrange & Act
		Configuration configuration = yamlLanguageResourceLoader.loadConfiguration();

		// Assert
		assertNotNull(configuration);
	}


	@Test
	void languageFileExistsTest_valid_tag() {
		// Arrange
		when(pluginMock.getLogger()).thenReturn(Logger.getLogger(this.getClass().getName()));

		// Act
		String resultString = yamlLanguageResourceLoader.getValidLanguageTag("en-US");

		// Assert
		assertEquals("en-US", resultString,"language file 'en-US.yml' does not exist.");
	}

	@Test
	void languageFileExistsTest_nonexistent_tag() {
		// Act
		when(pluginMock.getLogger()).thenReturn(Logger.getLogger(this.getClass().getName()));

		// Assert
		assertNotEquals("bs-ES", yamlLanguageResourceLoader.getValidLanguageTag("bs-ES"),
				"wrong language tag returned.");
		assertEquals("en-US", yamlLanguageResourceLoader.getValidLanguageTag("bs-ES"),
				"wrong language tag returned.");
	}

	@Test
	@DisplayName("file loader get current filename not null.")
	void GetLanguageFilenameTest() {
		assertEquals("language" + File.separator + "en-US.yml",
				YamlLanguageResourceLoader.getLanguageFilename("en-US"));
	}

	@Test
	void GetLanguageFilenameTest_nonexistent() {
		assertEquals("language" + File.separator + "not-a-valid-tag.yml",
				YamlLanguageResourceLoader.getLanguageFilename("not-a-valid-tag"));
	}

	@Test
	void languageFileExistsTests_nonexistent() {
		// Arrange
		when(pluginMock.getLogger()).thenReturn(Logger.getLogger(this.getClass().getName()));

		// Act & Assert
		assertNotNull(yamlLanguageResourceLoader.getValidLanguageTag("not-a-valid-tag"));
		assertEquals("en-US", yamlLanguageResourceLoader.getValidLanguageTag("not-a-valid-tag"));
	}

	@Test
	void testLoadConfigurationDefaults() {
		// Arrange
		Configuration testConfiguration = new MemoryConfiguration();
		testConfiguration.set("TEST_KEY_!", true);
		testConfiguration.set("TEST_KEY_2", "a string value");

		InputStream resourceStream = MockUtility.getResourceStream("language/en-US.yml");

		// Act
		yamlLanguageResourceLoader.getConfigurationDefaults(resourceStream, testConfiguration);

		// Assert
		assertTrue(testConfiguration.contains("CONSTANTS.SPAWN.DISPLAY_NAME"));
	}

	@Test
	void fileAbsent() {
		assertTrue(yamlLanguageResourceLoader.fileAbsent("en-US"));
//		when(resourceInstallerMock.verifyResourceInstalled(Paths.get("language","en-US.yml").toString())).thenReturn(false);
	}


	@Nested
	class ValidateKeysTests {
		@Test
		void validateKeys_valid() {
			// Arrange
			Configuration testConfiguration = new MemoryConfiguration();
			testConfiguration.set("VALID_KEYS_ONLY", true);
			testConfiguration.set("ANOTHER_VALID_KEY", "a string value");

			// Act
			boolean result = yamlLanguageResourceLoader.validateKeys(testConfiguration);

			// Assert
			assertTrue(result);
		}

		@Test
		void validateKeys_invalid() {
			// Arrange
			Configuration testConfiguration = new MemoryConfiguration();
			testConfiguration.set("invalid-keys-only", false);
			testConfiguration.set("A_VALID_KEY", "a string value");

			// Act
			boolean result = yamlLanguageResourceLoader.validateKeys(testConfiguration);

			// Assert
			assertFalse(result);
		}
	}



	@Nested
	class GetValidResourceTypeNameTests {
		@Test
		void testGetValidResourceName() {
			assertNotNull(yamlLanguageResourceLoader.getValidResourceName("en-US"));
			assertEquals(LANGUAGE_EN_US_YML, yamlLanguageResourceLoader.getValidResourceName("en-US"));
		}

		@Test
		void testGetValidResourceName_nonexistent() {
			assertNotNull(yamlLanguageResourceLoader.getValidResourceName("not-a-valid-tag"));
			assertEquals(LANGUAGE_EN_US_YML, yamlLanguageResourceLoader.getValidResourceName("not-a-valid-tag"));
		}

		@Test
		void testGetValidResourceName_parameter_null() {
			IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
					() -> yamlLanguageResourceLoader.getValidResourceName(null));

			// Assert
			assertEquals("ResourceType name cannot be null.", exception.getMessage());
		}
	}


	@Nested
	class GetResourceTypeNameTests {
		@Test
		void TestGetResourceName_valid_language_tag() {
			assertEquals("language/en-US.yml", yamlLanguageResourceLoader.getValidResourceName("en-US"));
		}

		@Test
		void TestGetResourceName_valid_language_tag_no_file() {
			assertEquals("language/en-US.yml", yamlLanguageResourceLoader.getValidResourceName("en-UK"));
		}

		@Test
		void TestGetResourceName_invalid_language_tag() {
			assertEquals("language/en-US.yml", yamlLanguageResourceLoader.getValidResourceName("invalid-tag"));
		}
	}

}
