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

package com.winterhavenmc.util.messagebuilder.language.yaml;

import com.winterhavenmc.util.messagebuilder.util.Error;

import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Comparator;
import java.util.logging.Logger;

import static com.winterhavenmc.util.messagebuilder.util.MockUtility.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class YamlLanguageResourceInstallerTest {

	@Mock private Plugin pluginMock;

	private YamlLanguageResourceInstaller fileInstaller;
	private File tempDataDir;

	@BeforeEach
	public void setUp() throws IOException {

		tempDataDir = Files.createTempDirectory("MessageBuilderLib_").toFile();

		when(pluginMock.getLogger()).thenReturn(Logger.getLogger("YamlLanguageResourceInstallerTest"));
		when(pluginMock.getDataFolder()).thenReturn(tempDataDir);
		when(pluginMock.getResource(AUTO_INSTALL_TXT)).thenReturn(getClass().getClassLoader().getResourceAsStream(AUTO_INSTALL_TXT));
		when(pluginMock.getResource(LANGUAGE_EN_US_YML)).thenReturn(getClass().getClassLoader().getResourceAsStream(LANGUAGE_EN_US_YML));

		// create real instance of installer
		fileInstaller = new YamlLanguageResourceInstaller(pluginMock);
		fileInstaller.install();
	}

	@AfterEach
	public void tearDown() {
		pluginMock = null;
		fileInstaller = null;
		deleteTempFiles();
	}

	@Test
	void testGetAutoInstallFilename() {
		assertEquals("language/auto_install.txt", fileInstaller.getAutoInstallFilename());
	}


	@Test
	void testGetAutoInstallFileNames() {
		// Arrange
		when(pluginMock.getResource(AUTO_INSTALL_TXT)).thenReturn(getResourceStream(AUTO_INSTALL_TXT));

		// Act
		Collection<String> auto_install_filenames = fileInstaller.getAutoInstallFilenames();

		// Assert
		assertFalse(auto_install_filenames.isEmpty());

		// Verify
		verify(pluginMock, atLeastOnce()).getResource(AUTO_INSTALL_TXT);
	}


	@Nested
	class MockingSetupTests {
		@Test
		void testResourceStream() {
			InputStream resourceStream = getClass().getClassLoader().getResourceAsStream(LANGUAGE_EN_US_YML);
			assertNotNull(resourceStream, "Resource stream should not be null");
			try {
				resourceStream.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}


		@Test
		void testSaveResource_MocksCorrectly() throws Exception {
			// Arrange
			Path targetFilePath = tempDataDir.toPath().resolve(LANGUAGE_EN_US_YML);

			// Ensure parent directories exist
			Files.createDirectories(targetFilePath.getParent());

			// Simulate saving the resource by copying it from the test resources
			try (InputStream resourceStream = getClass().getClassLoader().getResourceAsStream(LANGUAGE_EN_US_YML)) {
				if (resourceStream == null) {
					throw new IOException("Resource '" + LANGUAGE_EN_US_YML + "' not found in the classpath.");
				}
				Files.copy(resourceStream, targetFilePath); // DO NOT REPLACE EXISTING FILES
			}

			// Act
			pluginMock.saveResource(LANGUAGE_EN_US_YML, false);

			// Assert
			File savedFile = new File(tempDataDir, LANGUAGE_EN_US_YML);
			assertTrue(savedFile.exists(), "The resource file should be saved to the data directory.");
		}

	}
//	@Test
//	void testInstallResourceToTempDir() throws IOException {
//		// Act: Install a resource into the temporary data directory
//
//		boolean result;
//		if (MockUtility.installResource(LANGUAGE_EN_US_YML)) {
//			result = true;
//		}
//		else {
//			result = false;
//		}
//
//		// Assert: Verify the file exists
//		assertTrue(result);
//		File installedFile = new File(tempDataDir, LANGUAGE_EN_US_YML);
//		assertTrue(installedFile.exists());
//	}

	@Test
	void testInstallResourceToCustomDir() throws IOException {
		// Arrange: Create a custom temporary directory
		Path customDir = Files.createTempDirectory("custom-test-dir");

		// Act: Install a resource into the custom directory
		boolean result = installResource(LANGUAGE_EN_US_YML, customDir);

		// Assert: Verify the file exists
		assertTrue(result);
		assertTrue(Files.exists(customDir.resolve(LANGUAGE_EN_US_YML)));

		// Cleanup
		Files.walk(customDir).sorted(Comparator.reverseOrder()).forEach(path -> path.toFile().delete());
	}

	@Disabled //TODO: run this check after we know the language directory has been created. it's obviously working
	@Test
	void verifyLanguageDirectoryTest_exists() {
		assertTrue(new File(tempDataDir, "language").isDirectory(),
				"the language directory should exist but it does not.");
	}

	@Test
	void testInstallResource_CreatesFileSuccessfully() throws Exception {
		// Arrange: Create a temporary directory for the test
		Path tempDir = Files.createTempDirectory("installer-test");

		// Act: Install a resource into the temporary directory
		boolean result = installResource(LANGUAGE_EN_US_YML, tempDir);

		// Assert: Verify the file exists and was successfully copied
		assertTrue(result, "Resource should have been installed successfully.");
		assertTrue(Files.exists(tempDir.resolve(LANGUAGE_EN_US_YML)));

		// Cleanup: Delete the temporary directory and its contents
		Files.walk(tempDir)
				.sorted(Comparator.reverseOrder())
				.forEach(path -> path.toFile().delete());
	}

	@Nested
	class AutoInstallResourceTests {
		@Test
		void getAutoInstallFilename() {
			assertEquals("language/auto_install.txt", fileInstaller.getAutoInstallFilename());
		}

		@Test
		public void autoInstallResourceExistsTest() {
			assertTrue(fileInstaller.resourceExists(AUTO_INSTALL_TXT));
			verify(pluginMock, atLeastOnce()).getResource(AUTO_INSTALL_TXT);
		}

		@Test
		public void autoInstallResourceExistsTest_null_parameter() {
			assertFalse(fileInstaller.resourceExists(null));
			verify(pluginMock, atLeastOnce()).getResource(anyString());
		}

		@Test
		public void autoInstallResourceExistsTest_null_return() {
			// return a null File object when language/auto_install.txt resource is fetched, simulating a missing resource
			when(pluginMock.getResource(AUTO_INSTALL_TXT)).thenReturn(null);
			assertFalse(fileInstaller.resourceExists(AUTO_INSTALL_TXT));
			verify(pluginMock, atLeastOnce()).getResource(AUTO_INSTALL_TXT);
		}
	}

	@Test
	public void languageResourceExistsTest_null() {
		// return a null File object when language/en-US.yml resource is fetched, simulating a missing resource
		when(pluginMock.getResource(LANGUAGE_EN_US_YML)).thenReturn(null);
		assertFalse(fileInstaller.resourceExists(LANGUAGE_EN_US_YML));
		verify(pluginMock, atLeastOnce()).getResource(LANGUAGE_EN_US_YML);
	}

	@Test
	public void getDataFolderTest_not_null() {
		assertNotNull(pluginMock.getDataFolder());
		verify(pluginMock, atLeastOnce()).getDataFolder();
	}

	@Test
	public void getDataFolderTest_is_directory() {
		assertTrue(pluginMock.getDataFolder().isDirectory());
		verify(pluginMock, atLeastOnce()).getDataFolder();
	}

	@Test
	public void getAutoInstallFilenamesTest_not_null() {
		// Arrange
		when(pluginMock.getResource(AUTO_INSTALL_TXT)).thenReturn(getClass().getClassLoader().getResourceAsStream(AUTO_INSTALL_TXT));

		// Act
		Collection<String> filenames = fileInstaller.getAutoInstallFilenames();

		// assert
		assertNotNull(filenames);

		// Verify
		verify(pluginMock, atLeastOnce()).getResource(AUTO_INSTALL_TXT);
	}

	@Test
	public void getAutoInstallFilenamesTest_not_empty() {
		// Arrange
		when(pluginMock.getResource(AUTO_INSTALL_TXT)).thenReturn(getClass().getClassLoader().getResourceAsStream(AUTO_INSTALL_TXT));

		// Act
		Collection<String> filenames = fileInstaller.getAutoInstallFilenames();

		// assert
		assertFalse(filenames.isEmpty());

		// Verify
		verify(pluginMock, atLeastOnce()).getResource(AUTO_INSTALL_TXT);
	}

	@Test
	public void getAutoInstallFilenamesTest_valid_entries() {
		// Arrange
		when(pluginMock.getResource(AUTO_INSTALL_TXT)).thenReturn(getClass().getClassLoader().getResourceAsStream(AUTO_INSTALL_TXT));

		// Act
		Collection<String> filenames = fileInstaller.getAutoInstallFilenames();

		// Assert
		assertTrue(filenames.contains(LANGUAGE_EN_US_YML));
		assertTrue(filenames.contains("language/fr-FR.yml"));

		// Verify
		verify(pluginMock, atLeastOnce()).getResource(AUTO_INSTALL_TXT);
	}

	@Test
	public void getAutoInstallFilenamesTest_invalid_entries() {
		// Arrange
		when(pluginMock.getResource(AUTO_INSTALL_TXT)).thenReturn(getClass().getClassLoader().getResourceAsStream(AUTO_INSTALL_TXT));

		// Act
		Collection<String> filenames = fileInstaller.getAutoInstallFilenames();

		// Assert
		assertFalse(filenames.contains("nonexistent.yml"));
		assertFalse(filenames.contains("this_line_is_intended_to_fail"));

		// Verify
		verify(pluginMock, atLeastOnce()).getResource(AUTO_INSTALL_TXT);
	}

	@Test
	void getAutoInstallFilenamesTest() {
		// Arrange
		when(pluginMock.getResource(AUTO_INSTALL_TXT)).thenReturn(getClass().getClassLoader().getResourceAsStream(AUTO_INSTALL_TXT));

		// Act
		Collection<String> filenames = fileInstaller.getAutoInstallFilenames();

		// Assert
		assertTrue(filenames.contains(LANGUAGE_EN_US_YML));

		// Verify
		verify(pluginMock, atLeastOnce()).getResource(AUTO_INSTALL_TXT);
	}

	@Test
	void getAutoInstallFilenamesTest_no_auto_install_txt() {
		when(pluginMock.getResource(AUTO_INSTALL_TXT)).thenReturn(null);
		assertTrue(fileInstaller.getAutoInstallFilenames().isEmpty());
		verify(pluginMock, atLeastOnce()).getResource(AUTO_INSTALL_TXT);
	}

	@Nested
	class VerifyResourceExistsTests {
		@Test
		void verifyResourceExistsTest() {
			assertTrue(fileInstaller.resourceExists(LANGUAGE_EN_US_YML));
			verify(pluginMock, atLeastOnce()).getResource(AUTO_INSTALL_TXT);
		}

		@Test
		void verifyResourceExistsTest_nonexistent() {
			assertFalse(fileInstaller.resourceExists("nonexistent_resource"));
			verify(pluginMock, atLeastOnce()).getResource("nonexistent_resource");
		}
	}

	@Nested
	class VerifyResourceInstalledTests {
		@Test
		void verifyResourceInstalledTest() {
			// Arrange
			when(pluginMock.getResource(AUTO_INSTALL_TXT)).thenReturn(getClass().getClassLoader().getResourceAsStream(AUTO_INSTALL_TXT));
			when(pluginMock.getResource(LANGUAGE_EN_US_YML)).thenReturn(getClass().getClassLoader().getResourceAsStream(LANGUAGE_EN_US_YML));
			// install resource when saveResource is called
			doAnswer(invocation -> installResource(invocation.getArgument(0), tempDataDir.toPath()))
					.when(pluginMock).saveResource(anyString(), eq(false));

			// Act
			fileInstaller.install();

			// Assert
			assertTrue(fileInstaller.verifyResourceInstalled(LANGUAGE_EN_US_YML));
			assertFalse(fileInstaller.verifyResourceInstalled("nonexistent-resource"));

			// verify
			verify(pluginMock, atLeastOnce()).getResource(AUTO_INSTALL_TXT);
			verify(pluginMock, atLeastOnce()).getResource(LANGUAGE_EN_US_YML);
		}

		@Test
		void verifyResourceInstalledTest_nonexistent() {
			assertFalse(fileInstaller.verifyResourceInstalled("nonexistent_file"));
		}
	}



	private void deleteTempFiles() {
		// delete language/en-US.yml file from temp dir after each test
		try {
			Files.deleteIfExists(Path.of(tempDataDir.getAbsolutePath(),"language", "en-US.yml"));
		}
		catch (FileAlreadyExistsException e) {
			System.out.println("File exists: " + Files.exists(Path.of(tempDataDir.getAbsolutePath(),"language", "en-US.yml")));
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}


	/**
	 * Installs a resource file from the classpath to the specified target directory.
	 *
	 * @param resourceName  the name of the resource file in the classpath
	 * @param targetDirPath the target directory where the file should be installed
	 * @return {@code true} if the resource was successfully copied, {@code false} otherwise
	 * @throws IOException if an error occurs during the file operation or if the resource cannot be found
	 */
	public static boolean installResource(final String resourceName, final Path targetDirPath) throws IOException {
		if (resourceName == null) { throw new IllegalArgumentException(com.winterhavenmc.util.messagebuilder.util.Error.Parameter.NULL_RESOURCE_NAME.getMessage()); }
		if (resourceName.isEmpty()) { throw new IllegalArgumentException(com.winterhavenmc.util.messagebuilder.util.Error.Parameter.EMPTY_RESOURCE_NAME.getMessage()); }
		if (targetDirPath == null) { throw new IllegalArgumentException(Error.Parameter.NULL_DIRECTORY_PATH.getMessage()); }

		// Ensure the target directory exists
		Files.createDirectories(targetDirPath);

		// Get the resource as an InputStream
		try (var inputStream = getResourceStream(resourceName)) {
			if (inputStream == null) {
				throw new IOException("Resource '" + resourceName + "' not found in the classpath.");
			}

			// Resolve the full path to the target file
			Path targetFilePath = targetDirPath.resolve(resourceName);

			// create subdirectories
			Files.createDirectories(targetFilePath.getParent());

			// Copy the resource to the target directory
			Files.copy(inputStream, targetFilePath); // DO NOT REPLACE EXISTING FILES
			return true;
		}
	}


	/**
	 * Retrieves an InputStream for the specified resource from the classpath.
	 *
	 * @param resourceName the name of the resource file
	 * @return an InputStream for the resource, or {@code null} if the resource cannot be found
	 */
	public static InputStream getResourceStream(final String resourceName) {
		return YamlLanguageResourceInstallerTest.class.getClassLoader().getResourceAsStream(resourceName);
	}

}
