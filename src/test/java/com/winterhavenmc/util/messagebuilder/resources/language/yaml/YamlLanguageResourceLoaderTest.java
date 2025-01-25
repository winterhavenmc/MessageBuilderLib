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

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.regex.Matcher;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.winterhavenmc.util.messagebuilder.resources.language.yaml.Option.DEFAULT_LANGUAGE_TAG;
import static com.winterhavenmc.util.messagebuilder.resources.language.yaml.Option.RESOURCE_LANGUAGE_EN_US_YML;
import static com.winterhavenmc.util.messagebuilder.resources.language.yaml.YamlLanguageResourceLoader.UPPER_SNAKE_CASE;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class YamlLanguageResourceLoaderTest {

	@TempDir File tempDataDir;
	@Mock Plugin pluginMock;

	FileConfiguration pluginConfiguration;
	Configuration languageConfiguration;

	YamlLanguageResourceLoader yamlLanguageResourceLoader;


	@BeforeEach
	public void setUp() throws IOException {
		// create real plugin config
		pluginConfiguration = new YamlConfiguration();
		pluginConfiguration.set("locale", DEFAULT_LANGUAGE_TAG);
		pluginConfiguration.set("language", DEFAULT_LANGUAGE_TAG);

		// create real language configuration
		languageConfiguration = MockUtility.loadConfigurationFromResource("language/en-US.yml");

		// create new real file loader
		yamlLanguageResourceLoader = new YamlLanguageResourceLoader(pluginMock);

		// install resource to temp directory
		MockUtility.installResource(RESOURCE_LANGUAGE_EN_US_YML.toString(), tempDataDir.toPath());
	}

	@AfterEach
	public void tearDown() {
		pluginMock = null;
		pluginConfiguration = null;
		languageConfiguration = null;
		yamlLanguageResourceLoader = null;
	}


	@Test
	public void FileLoaderNotNull() {
		assertNotNull(yamlLanguageResourceLoader);
	}


	@Test
	public void testLoadConfiguration() {
		when(pluginMock.getConfig()).thenReturn(pluginConfiguration);
		when(pluginMock.getLogger()).thenReturn(Logger.getLogger(this.getClass().getName()));

		// Arrange & Act
		Configuration configuration = yamlLanguageResourceLoader.loadConfiguration();

		// Assert
		assertNotNull(configuration);
	}


	@Test
	public void GetLanguageFilenameTest() {
		assertEquals("language" + File.separator + "en-US.yml",
				new LanguageTag(DEFAULT_LANGUAGE_TAG.toString()).getFileName());
	}

	@Test
	public void GetLanguageFilenameTest_nonexistent() {
		assertEquals("language" + File.separator + "not-a-valid-tag.yml",
				new LanguageTag("not-a-valid-tag").getFileName());
	}


	@Nested
	public class ValidateKeysTests {
		@Test
		public void validateKeys_valid() {
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
		public void validateKeys_invalid() {
			// Arrange
			when(pluginMock.getLogger()).thenReturn(Logger.getLogger(this.getClass().getName()));

			Configuration testConfiguration = new MemoryConfiguration();
			testConfiguration.set("invalid-keys-only", false);
			testConfiguration.set("A_VALID_KEY", "a string value");

			// Act
			boolean result = yamlLanguageResourceLoader.validateKeys(testConfiguration);

			// Assert
			assertFalse(result);
		}
	}

	@Test
	public void testPattern_match() {
		// Arrange
		String initialString = "THE_QUICK_BROWN_FOX";

		// Act
		Matcher matcher = UPPER_SNAKE_CASE.matcher(initialString);

		// Assert
		assertTrue(matcher.matches());
	}

	@Test
	public void testPattern_no_match() {
		// Arrange
		String initialString = "the quick brown fox.";

		// Act
		Matcher matcher = UPPER_SNAKE_CASE.matcher(initialString);

		// Assert
		assertFalse(matcher.matches());
	}

}
