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
import org.bukkit.plugin.Plugin;

import org.junit.jupiter.api.*;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import static com.winterhavenmc.util.messagebuilder.mocks.MockPlugin.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class YamlLanguageFileLoaderTests {

	private Plugin plugin;
	private YamlLanguageFileLoader yamlLanguageFileLoader;


//	@BeforeAll
//	public static void preSetUp() {
//		MockPlugin.verifyTempDir();
//	}

	@BeforeEach
	public void setUp() {

		// create new mock plugin
		plugin = mock(Plugin.class, "MockPlugin");
		when(plugin.getLogger()).thenReturn(Logger.getLogger(this.getClass().getName()));

		// mock configuration
		FileConfiguration configuration = mock(FileConfiguration.class);
		when(plugin.getConfig()).thenReturn(configuration);
		when(configuration.getString("language")).thenReturn("en-US");

		// return real file input streams for mock plugin resources
		doAnswer(invocation -> getResourceStream(invocation.getArgument(0)))
				.when(plugin).getResource(anyString());

		// copy resource to temporary data directory when saveResource method is called
		doAnswer(invocation -> installResource(invocation.getArgument(0)))
				.when(plugin).saveResource(anyString(), eq(false));

		// create new real file loader
		yamlLanguageFileLoader = new YamlLanguageFileLoader(plugin);
	}

	@AfterEach
	public void tearDown() {
		plugin = null;
		yamlLanguageFileLoader = null;
	}

	@Test
	void FileLoaderNotNull() {
		assertNotNull(yamlLanguageFileLoader);
	}

	@Test
	void verifyDataDirectoryExistsTest() {
		assertTrue(getDataFolder().isDirectory());
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
		assertEquals("en-US", yamlLanguageFileLoader.getValidLanguageTag("en-US"),
				"language file 'en-US.yml' does not exist.");
	}

	@Test
	void languageFileExistsTest_nonexistent_tag() {
		assertNotEquals("bs-ES", yamlLanguageFileLoader.getValidLanguageTag("bs-ES"),
				"wrong language tag returned.");
		assertEquals("en-US", yamlLanguageFileLoader.getValidLanguageTag("bs-ES"),
				"wrong language tag returned.");
	}

	@Test
	void getMessageTest_enabled_message() {
		Configuration messageConfiguration = yamlLanguageFileLoader.getConfiguration();
		assertNotNull(messageConfiguration);
		assertEquals("This is an enabled message", messageConfiguration.getString("MESSAGES.ENABLED_MESSAGE.message"));
		assertTrue(messageConfiguration.getBoolean("MESSAGES.ENABLED_MESSAGE.enabled"));
	}

	@Test
	void getMessageTest_disabled_message() {
		Configuration messageConfiguration = yamlLanguageFileLoader.getConfiguration();
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

	@Test
	@DisplayName("languageFileExists test")
	void languageFileExistsTests_nonexistent() {
		assertNotNull(yamlLanguageFileLoader.getValidLanguageTag("not-a-valid-tag"));
		assertEquals("en-US", yamlLanguageFileLoader.getValidLanguageTag("not-a-valid-tag"));
	}

	@Test
	@DisplayName("getResourceName test")
	void getResourceNameTest() {
		assertNotNull(yamlLanguageFileLoader.getResourceName("en-US"));
		assertEquals(LANGUAGE_EN_US_YML, yamlLanguageFileLoader.getResourceName("en-US"));
	}

	@Test
	@DisplayName("getResourceName test 2")
	void getResourceNameTest_nonexistent() {
		assertNotNull(yamlLanguageFileLoader.getResourceName("not-a-valid-tag"));
		assertEquals(LANGUAGE_EN_US_YML, yamlLanguageFileLoader.getResourceName("not-a-valid-tag"));
	}


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

}
