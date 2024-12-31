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

import com.winterhavenmc.util.messagebuilder.util.MockUtility;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Collection;
import java.util.Comparator;

import static com.winterhavenmc.util.messagebuilder.util.MockUtility.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

//TODO: Hit that one last branch/line with test coverage to score 100!
public class YamlLanguageFileInstallerTests {

	private Plugin mockPlugin;
	private final static File dataDir = MockUtility.getDataDir();

	private YamlLanguageFileInstaller yamlLanguageFileInstaller;
	private Collection<String> filenames;


	@BeforeEach
	public void setUp() throws IOException {

		// create new mock plugin
		mockPlugin = MockUtility.createMockPlugin();

		// create real instance of installer
		yamlLanguageFileInstaller = new YamlLanguageFileInstaller(mockPlugin);
		yamlLanguageFileInstaller.install();

		// get filenames from installer
		filenames = yamlLanguageFileInstaller.getAutoInstallFilenames();
	}

	@AfterEach
	public void tearDown() {
		mockPlugin = null;
		yamlLanguageFileInstaller = null;
		filenames = null;
	}


	@Nested
	class MockingSetupTests {
		@Test
		void testResourceStream() {
			System.out.println("testResourceStream test starting...");
			InputStream resourceStream = MockUtility.getResourceStream("language/en-US.yml");
			assertNotNull(resourceStream, "Resource stream should not be null");
		}


		@Test
		void testSaveResource_MocksCorrectly() throws Exception {
			// Arrange
			String resourceName = "language/en-US.yml";
			Path targetFilePath = MockUtility.getDataDir().toPath().resolve(resourceName);

			// Ensure parent directories exist
			Files.createDirectories(targetFilePath.getParent());

			// Simulate saving the resource by copying it from the test resources
			try (InputStream resourceStream = MockUtility.getResourceStream(resourceName)) {
				if (resourceStream == null) {
					throw new IOException("Resource '" + resourceName + "' not found in the classpath.");
				}
				Files.copy(resourceStream, targetFilePath, StandardCopyOption.REPLACE_EXISTING);
			}

			// Act
			mockPlugin.saveResource("language/en-US.yml", false);

			// Assert
			File savedFile = new File(MockUtility.getDataDir(), "language/en-US.yml");
			assertTrue(savedFile.exists(), "The resource file should be saved to the data directory.");
		}

	}
	@Test
	void testInstallResourceToTempDir() throws IOException {
		// Act: Install a resource into the temporary data directory
		boolean result = MockUtility.installResource("language/en-US.yml");

		// Assert: Verify the file exists
		assertTrue(result);
		File installedFile = new File(MockUtility.getDataDir(), "language/en-US.yml");
		assertTrue(installedFile.exists());
	}

	@Test
	void testInstallResourceToCustomDir() throws IOException {
		// Arrange: Create a custom temporary directory
		Path customDir = Files.createTempDirectory("custom-test-dir");

		// Act: Install a resource into the custom directory
		boolean result = MockUtility.installResource("language/en-US.yml", customDir);

		// Assert: Verify the file exists
		assertTrue(result);
		assertTrue(Files.exists(customDir.resolve("language/en-US.yml")));

		// Cleanup
		Files.walk(customDir).sorted(Comparator.reverseOrder()).forEach(path -> path.toFile().delete());
	}


	@Test
	void verifyLanguageDirectoryTest_exists() {
		assertTrue(new File(getDataDir(), "language").isDirectory(),
				"the language directory should exist but it does not.");
	}

	@Test
	void testInstallResource_CreatesFileSuccessfully() throws Exception {
		// Arrange: Create a temporary directory for the test
		Path tempDir = Files.createTempDirectory("installer-test");

		// Act: Install a resource into the temporary directory
		boolean result = MockUtility.installResource("language/en-US.yml", tempDir);

		// Assert: Verify the file exists and was successfully copied
		assertTrue(result, "Resource should have been installed successfully.");
		assertTrue(Files.exists(tempDir.resolve("language/en-US.yml")));

		// Cleanup: Delete the temporary directory and its contents
		Files.walk(tempDir)
				.sorted(Comparator.reverseOrder())
				.forEach(path -> path.toFile().delete());
	}

	@Nested
	class AutoInstallResourceTests {
		@Test
		void getAutoInstallFilename() {
			assertEquals("language/auto_install.txt", yamlLanguageFileInstaller.getAutoInstallFilename());
		}

		@Test
		public void autoInstallResourceExistsTest() {
			assertTrue(yamlLanguageFileInstaller.resourceExists(AUTO_INSTALL_TXT));
			verify(mockPlugin, atLeastOnce()).getResource(AUTO_INSTALL_TXT);
		}

		@Test
		public void autoInstallResourceExistsTest_null_parameter() {
			assertFalse(yamlLanguageFileInstaller.resourceExists(null));
			verify(mockPlugin, atLeastOnce()).getResource(anyString());
		}

		@Test
		public void autoInstallResourceExistsTest_null_return() {
			// return a null File object when language/auto_install.txt resource is fetched, simulating a missing resource
			when(mockPlugin.getResource(AUTO_INSTALL_TXT)).thenReturn(null);
			assertFalse(yamlLanguageFileInstaller.resourceExists(AUTO_INSTALL_TXT));
			verify(mockPlugin, atLeastOnce()).getResource(AUTO_INSTALL_TXT);
		}
	}

	@Test
	public void languageResourceExistsTest_null() {
		// return a null File object when language/en-US.yml resource is fetched, simulating a missing resource
		when(mockPlugin.getResource(LANGUAGE_EN_US_YML)).thenReturn(null);
		assertFalse(yamlLanguageFileInstaller.resourceExists(LANGUAGE_EN_US_YML));
		verify(mockPlugin, atLeastOnce()).getResource(LANGUAGE_EN_US_YML);
	}

	@Test
	public void getDataFolderTest_not_null() {
		assertNotNull(mockPlugin.getDataFolder());
		verify(mockPlugin, atLeastOnce()).getDataFolder();
	}

	@Test
	public void getDataFolderTest_is_directory() {
		assertTrue(mockPlugin.getDataFolder().isDirectory());
		verify(mockPlugin, atLeastOnce()).getDataFolder();
	}

	@Test
	public void getAutoInstallFilenamesTest_not_null() {
		assertNotNull(filenames);
	}

	@Test
	public void getAutoInstallFilenamesTest_not_empty() {
		assertFalse(filenames.isEmpty());
	}

	@Test
	public void getAutoInstallFilenamesTest_valid_entries() {
		assertTrue(filenames.contains("language/en-US.yml"));
		assertTrue(filenames.contains("language/fr-FR.yml"));
	}

	@Test
	public void getAutoInstallFilenamesTest_invalid_entries() {
		assertFalse(filenames.contains("nonexistent.yml"));
		assertFalse(filenames.contains("this_line_is_intended_to_fail"));
	}

	@Test
	void getAutoInstallFilenamesTest() {
		assertTrue(yamlLanguageFileInstaller.getAutoInstallFilenames().contains("language/en-US.yml"));
		verify(mockPlugin, atLeastOnce()).getResource(AUTO_INSTALL_TXT);
	}

	@Test
	void getAutoInstallFilenamesTest_no_auto_install_txt() {
		when(mockPlugin.getResource(AUTO_INSTALL_TXT)).thenReturn(null);
		assertTrue(yamlLanguageFileInstaller.getAutoInstallFilenames().isEmpty());
		verify(mockPlugin, atLeastOnce()).getResource(AUTO_INSTALL_TXT);
	}

	@Nested
	class VerifyResourceExistsTests {
		@Test
		void verifyResourceExistsTest() {
			assertTrue(yamlLanguageFileInstaller.resourceExists(LANGUAGE_EN_US_YML));
			verify(mockPlugin, atLeastOnce()).getResource(AUTO_INSTALL_TXT);
		}

		@Test
		void verifyResourceExistsTest_nonexistent() {
			assertFalse(yamlLanguageFileInstaller.resourceExists("nonexistent_resource"));
			verify(mockPlugin, atLeastOnce()).getResource("nonexistent_resource");
		}
	}

	@Nested
	class VerifyResourceInstalledTests {
		@Test
		void verifyResourceInstalledTest() {
			assertTrue(yamlLanguageFileInstaller.verifyResourceInstalled(LANGUAGE_EN_US_YML));
		}

		@Test
		void verifyResourceInstalledTest_nonexistent() {
			assertFalse(yamlLanguageFileInstaller.verifyResourceInstalled("nonexistent_file"));
		}
	}

}
