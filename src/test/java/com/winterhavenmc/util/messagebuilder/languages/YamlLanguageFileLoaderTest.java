/*
 * Copyright (c) 2024 Tim Savage.
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

import com.winterhavenmc.util.messagebuilder.messages.MessageId;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import static com.winterhavenmc.util.messagebuilder.util.MockUtility.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class YamlLanguageFileLoaderTest {

	@Mock private Plugin pluginMock;

	private YamlLanguageFileLoader yamlLanguageFileLoader;


	@BeforeEach
	public void setUp() {
		// create new real file loader
		yamlLanguageFileLoader = new YamlLanguageFileLoader(pluginMock);
	}

	@AfterEach
	public void tearDown() {
		pluginMock = null;
		yamlLanguageFileLoader = null;
	}


	@Test
	void FileLoaderNotNull() {
		assertNotNull(yamlLanguageFileLoader);
	}

	@Test
	void getLanguageFilenameTest() {
		assertEquals("language/en-US.yml", YamlLanguageFileLoader.getLanguageFilename("en-US"),
				"an incorrect filename was returned.");
		assertEquals("language/es-ES.yml", YamlLanguageFileLoader.getLanguageFilename("es-ES"),
				"an incorrect filename was returned.");
		assertEquals("language/invalid_tag.yml", YamlLanguageFileLoader.getLanguageFilename("invalid_tag"),
				"an incorrect filename was returned.");
	}

	@Test
	void languageFileExistsTest_valid_tag() {
		// Arrange
		when(pluginMock.getLogger()).thenReturn(Logger.getLogger(this.getClass().getName()));

		// Act
		String resultString = yamlLanguageFileLoader.getValidLanguageTag("en-US");

		// Assert
		assertEquals("en-US", resultString,"language file 'en-US.yml' does not exist.");

		// Verify
		verify(pluginMock, atLeastOnce()).getLogger();
	}


	@Test
	void languageFileExistsTest_nonexistent_tag() {
		// Arrange
		when(pluginMock.getLogger()).thenReturn(Logger.getLogger(this.getClass().getName()));

		// Act & Assert
		assertNotEquals("bs-ES", yamlLanguageFileLoader.getValidLanguageTag("bs-ES"),
				"wrong language tag returned.");
		assertEquals("en-US", yamlLanguageFileLoader.getValidLanguageTag("bs-ES"),
				"wrong language tag returned.");

		// Verify
		verify(pluginMock, atLeastOnce()).getLogger();
	}

	@Disabled
	@Test
	void getMessageTest_enabled_message() {
		// Arrange
		when(pluginMock.getConfig()).thenReturn(getConfig());
		when(pluginMock.getLogger()).thenReturn(Logger.getLogger(this.getClass().getName()));

		// Act
		Configuration messageConfiguration = yamlLanguageFileLoader.getConfiguration();

		// Assert
		assertNotNull(messageConfiguration);
		assertEquals("This is an enabled message", messageConfiguration.getString("MESSAGES.ENABLED_MESSAGE.message"));
		assertTrue(messageConfiguration.getBoolean("MESSAGES.ENABLED_MESSAGE.enabled"));
	}

	@Disabled
	@Test
	void getMessageTest_disabled_message() {
		// Arrange
		when(pluginMock.getConfig()).thenReturn(getConfig());
		when(pluginMock.getLogger()).thenReturn(Logger.getLogger(this.getClass().getName()));

		// Act
		Configuration messageConfiguration = yamlLanguageFileLoader.getConfiguration();

		// Assert
		assertNotNull(messageConfiguration);
		assertEquals("This is a disabled message", messageConfiguration.getString("MESSAGES.DISABLED_MESSAGE.message"));
		assertFalse(messageConfiguration.getBoolean("MESSAGES.DISABLED_MESSAGE.enabled"));
	}

	@Test
	@DisplayName("file loader get current filename not null.")
	void GetLanguageFilenameTest() {
		assertEquals("language" + File.separator + "en-US.yml",
				YamlLanguageFileLoader.getLanguageFilename("en-US"));
	}

	@Test
	@DisplayName("file loader get current filename non-existent.")
	void GetLanguageFilenameTest_nonexistent() {
		assertEquals("language" + File.separator + "not-a-valid-tag.yml",
				YamlLanguageFileLoader.getLanguageFilename("not-a-valid-tag"));
	}

	@Disabled
	@Test
	@DisplayName("languageFileExists test")
	void languageFileExistsTests_nonexistent() {
		assertNotNull(yamlLanguageFileLoader.getValidLanguageTag("not-a-valid-tag"));
		assertEquals("en-US", yamlLanguageFileLoader.getValidLanguageTag("not-a-valid-tag"));
	}

	@Test
	@DisplayName("getResourceName test")
	void getValidResourceNameTest() {
		assertNotNull(yamlLanguageFileLoader.getValidResourceName("en-US"));
		assertEquals(LANGUAGE_EN_US_YML, yamlLanguageFileLoader.getValidResourceName("en-US"));
	}

	@Test
	@DisplayName("getResourceName test 2")
	void getValidResourceNameTest_nonexistent() {
		assertNotNull(yamlLanguageFileLoader.getValidResourceName("not-a-valid-tag"));
		assertEquals(LANGUAGE_EN_US_YML, yamlLanguageFileLoader.getValidResourceName("not-a-valid-tag"));
	}


	//TODO: I think these are old tests that are no longer valid. check them out before deleting them
	@Disabled
	@Nested
	class MatchMessageKeysTest {

		private Set<String> getConfigMessageNames() {
			return yamlLanguageFileLoader.getConfiguration().getKeys(false);
		}

		private Set<String> getEnumMessageNames() {
			Set<String> enumMessageNames = new HashSet<>();
			for (MessageId messageId : MessageId.values()) {
				enumMessageNames.add(messageId.toString());
			}
			return enumMessageNames;
		}


		@Test
		@DisplayName("Match all MessageId enum members names against config keys.")
		void EnumContainsAllConfigMessages() {

			// check each enum member has corresponding key in message config file
			for (String messageName : getConfigMessageNames()) {
				assertTrue(getEnumMessageNames().contains(messageName),
						messageName + " not is contained in MessageId enum.");
			}
		}


		@Test
		@DisplayName("Match all message config keys against MessageId enum member names.")
		void ConfigContainsAllEnumMessages() {

			// check each message config key has corresponding MessageId enum member
			for (String messageName : getConfigMessageNames()) {
				assertTrue(getConfigMessageNames().contains(messageName),
						messageName + " is contained in messages config file.");
			}
		}
	}

	@Nested
	class GetResourceNameTests {
		@Test
		void TestGetResourceName_valid_language_tag() {
			assertEquals("language/en-US.yml", yamlLanguageFileLoader.getValidResourceName("en-US"));
		}

		@Test
		void TestGetResourceName_valid_language_tag_no_file() {
			assertEquals("language/en-US.yml", yamlLanguageFileLoader.getValidResourceName("en-UK"));
		}

		@Test
		void TestGetResourceName_invalid_language_tag() {
			assertEquals("language/en-US.yml", yamlLanguageFileLoader.getValidResourceName("invalid-tag"));
		}
	}


	private static FileConfiguration getConfig() {
		FileConfiguration configuration = new YamlConfiguration();
		configuration.set("locale", "en-US");
		configuration.set("language", "en-US");
		return configuration;
	}

}
