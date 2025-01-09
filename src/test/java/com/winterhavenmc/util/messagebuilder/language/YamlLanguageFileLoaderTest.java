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

package com.winterhavenmc.util.messagebuilder.language;

import org.bukkit.plugin.Plugin;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;

import static com.winterhavenmc.util.messagebuilder.util.MockUtility.*;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
public class YamlLanguageFileLoaderTest {

	@Mock Plugin pluginMock;

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
		// Act
		String resultString = yamlLanguageFileLoader.getValidLanguageTag("en-US");

		// Assert
		assertEquals("en-US", resultString,"language file 'en-US.yml' does not exist.");
	}

	@Test
	void languageFileExistsTest_nonexistent_tag() {
		// Act & Assert
		assertNotEquals("bs-ES", yamlLanguageFileLoader.getValidLanguageTag("bs-ES"),
				"wrong language tag returned.");
		assertEquals("en-US", yamlLanguageFileLoader.getValidLanguageTag("bs-ES"),
				"wrong language tag returned.");
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

}
