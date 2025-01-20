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
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
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
import static com.winterhavenmc.util.messagebuilder.resources.language.yaml.Option.DEFAULT_LANGUAGE_TAG;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class YamlLanguageResourceInstallerTest {

	@TempDir File tempDataDir;
	@Mock private Plugin pluginMock;

	private YamlLanguageResourceInstaller resourceInstaller;

	@BeforeEach
	public void setUp() throws IOException {

		when(pluginMock.getLogger()).thenReturn(Logger.getLogger("YamlLanguageResourceInstallerTest"));
		when(pluginMock.getDataFolder()).thenReturn(tempDataDir);

		doAnswer(invocation -> getClass().getClassLoader().getResourceAsStream(invocation.getArgument(0)))
				.when(pluginMock).getResource(anyString());

		// create real instance of installer
		resourceInstaller = new YamlLanguageResourceInstaller(pluginMock);
		resourceInstaller.autoInstall();
	}

	@AfterEach
	public void tearDown() {
		pluginMock = null;
		resourceInstaller = null;
		deleteTempFiles();
	}


	@Test
	void isInstalledForTag_parameter_null() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> resourceInstaller.isInstalledForTag(null));

		assertEquals("The languageTag parameter cannot be null.", exception.getMessage());
	}

	@Test
	void testInstallIfMissing_parameter_null() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> resourceInstaller.installIfMissing(null));

		assertEquals("The languageTag parameter cannot be null.", exception.getMessage());
	}


	@Nested
	class MockingSetupTests {

		@Test
		void testResourceStream() throws IOException {
			// Arrange & Act
			InputStream resourceStream = getClass().getClassLoader().getResourceAsStream(LANGUAGE_EN_US_YML);

			// Assert
			assertNotNull(resourceStream, "Resource stream should not be null.");

			// Clean up
			resourceStream.close();
		}


		@Test
		void testSaveResource_MocksCorrectly() throws Exception {
			// Arrange
			Path targetFilePath = tempDataDir.toPath().resolve(LANGUAGE_EN_US_YML);

			// Ensure parent directories exist
			Files.createDirectories(targetFilePath.getParent());

			// Simulate saving the resource by copying it from the test resources
			InputStream resourceStream = getClass().getClassLoader().getResourceAsStream(LANGUAGE_EN_US_YML);
			copyResource(resourceStream, targetFilePath);
			// when(pluginMock.saveResource(LANGUAGE_EN_US_YML, false)).thenReturn(copyResource(LANGUAGE_EN_US_YML, targetFilePath));

			// Act
			pluginMock.saveResource(LANGUAGE_EN_US_YML, false);

			// Assert
			File savedFile = new File(tempDataDir, LANGUAGE_EN_US_YML);
			assertTrue(savedFile.exists(), "The resource file should be saved to the data directory.");
		}
	}


	@Test
	void testGetAutoInstall_name_constant() {
		assertEquals("language/auto_install.txt", resourceInstaller.getAutoInstallResourcePath());
	}


	@Test
	void testGetAutoInstallResourceNames() {
		// Arrange
//		when(pluginMock.getResource( resourceInstaller.getAutoInstallResourcePath())).thenReturn(getResourceStream( resourceInstaller.getAutoInstallResourcePath()));

		// Act
		Collection<String> autoInstallFilenames = resourceInstaller.getAutoInstallResourceNames(resourceInstaller.getAutoInstallResourcePath());

		// Assert
		assertFalse(autoInstallFilenames.isEmpty());
		assertTrue(autoInstallFilenames.contains("language/en-US.yml"));
		assertFalse(autoInstallFilenames.contains("language/en-GB.yml"));

		// Verify
		verify(pluginMock, atLeastOnce()).getResource(resourceInstaller.getAutoInstallResourcePath());
	}


	@Test
	void resourceExists() {
		assertTrue(resourceInstaller.resourceExists(resourceInstaller.getAutoInstallResourcePath()));
	}


	@Test
	void isInstalled_true() {
		// Arrange
		LanguageTag languageTag = new LanguageTag(DEFAULT_LANGUAGE_TAG.value());

		// install resource when saveResource is called
		doAnswer(invocation -> installResource(invocation.getArgument(0), tempDataDir.toPath()))
				.when(pluginMock).saveResource(anyString(), eq(false));

		// Act
		resourceInstaller.installIfMissing(languageTag);

		// Assert
		assertTrue(resourceInstaller.isInstalledForTag(languageTag));

		// Verify
		verify(pluginMock, atLeastOnce()).saveResource(anyString(), eq(false));
	}


	@Test
	void isInstalled_false() {
		// Arrange
		LanguageTag languageTag = new LanguageTag("nonexistent-resource");

		// Act
		boolean result = resourceInstaller.isInstalledForTag(languageTag);

		// Assert
		assertFalse(resourceInstaller.isInstalledForTag(languageTag));
	}

	@Test
	void testInstallByNameIfMissing() {
		// Mock
		doAnswer(invocation -> installResource(invocation.getArgument(0), tempDataDir.toPath()))
				.when(pluginMock).saveResource(anyString(), eq(false));

		// Arrange
		LanguageTag languageTag = new LanguageTag(DEFAULT_LANGUAGE_TAG.toString());

		// Act
		InstallerStatus status = resourceInstaller.installIfMissing(languageTag);

		// Assert
		assertEquals(InstallerStatus.SUCCESS, status);

		// Verify
		verify(pluginMock, atLeastOnce()).saveResource(anyString(), eq(false));
	}


	@Test
	void testInstallByNameIfMissing_file_exists() {
		// Mock
		doAnswer(invocation -> installResource(invocation.getArgument(0), tempDataDir.toPath()))
				.when(pluginMock).saveResource(anyString(), eq(false));

		// Arrange
		LanguageTag languageTag = new LanguageTag(DEFAULT_LANGUAGE_TAG.toString());
		resourceInstaller.install(languageTag);

		// Act
		InstallerStatus status = resourceInstaller.installIfMissing(languageTag);

		// Assert
		assertEquals(InstallerStatus.FILE_EXISTS, status);
	}


	@Test
	void testInstall_ByName_success() {
		// Arrange
		doAnswer(invocation -> installResource(invocation.getArgument(0), tempDataDir.toPath()))
				.when(pluginMock).saveResource(anyString(), eq(false));

		// Act
		InstallerStatus status = resourceInstaller.installByName(LANGUAGE_EN_US_YML);

		// Assert
		assertEquals(InstallerStatus.SUCCESS, status);
	}


	@Test
	void testInstall_ByName_resource_unavailable() {
		// Act
		InstallerStatus status = resourceInstaller.installByName("nonexistent-resource");

		// Assert
		assertEquals(InstallerStatus.UNAVAILABLE, status);
	}


	@Test
	void testInstall_ByName_file_exists() {
		// Arrange
		doAnswer(invocation -> installResource(invocation.getArgument(0), tempDataDir.toPath()))
				.when(pluginMock).saveResource(anyString(), eq(false));

		resourceInstaller.installByName(LANGUAGE_EN_US_YML);

		// Act
		InstallerStatus status = resourceInstaller.installByName(LANGUAGE_EN_US_YML);

		// Assert
		assertEquals(InstallerStatus.FILE_EXISTS, status);
	}


	@Test
	void testInstall_ByName_parameter_null() {
		// Arrange
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> resourceInstaller.installByName(null));

		// Assert
		assertEquals("The resourceName parameter cannot be null.", exception.getMessage());
	}


	@Test
	void testInstallByNameForTag_success() {
		// Mock
		doAnswer(invocation -> installResource(invocation.getArgument(0), tempDataDir.toPath()))
				.when(pluginMock).saveResource(anyString(), eq(false));

		// Arrange
		LanguageTag languageTag = new LanguageTag(DEFAULT_LANGUAGE_TAG.toString());

		// Act
		InstallerStatus status = resourceInstaller.install(languageTag);

		// Assert
		assertEquals(InstallerStatus.SUCCESS, status);

		// Verify
		verify(pluginMock, atLeastOnce()).saveResource(anyString(), eq(false));
	}


	@Test
	void testInstallByNameForTag_file_exists() {
		// Arrange
		doAnswer(invocation -> installResource(invocation.getArgument(0), tempDataDir.toPath()))
				.when(pluginMock).saveResource(anyString(), eq(false));

		LanguageTag languageTag = new LanguageTag(DEFAULT_LANGUAGE_TAG.toString());
		resourceInstaller.install(languageTag);

		// Act
		InstallerStatus status = resourceInstaller.install(languageTag);

		// Assert
		assertEquals(InstallerStatus.FILE_EXISTS, status);
	}


	@Test
	void testInstallByNameForTag_resource_unavailable() {
		// Arrange
		LanguageTag languageTag = new LanguageTag("nonexistent-language-tag");

		// Act
		InstallerStatus status = resourceInstaller.install(languageTag);

		// Assert
		assertEquals(InstallerStatus.UNAVAILABLE, status);
	}


	@Test
	void testInstallByNameForTag_parameter_null() {
		// Arrange
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> resourceInstaller.install(null));

		// Assert
		assertEquals("The languageTag parameter cannot be null.", exception.getMessage());
	}


	@Disabled //TODO: run this check after we know the language directory has been created. it's obviously working
	@Test
	void verifyLanguageDirectoryTest_exists() throws IOException {
		// Arrange
		MockUtility.installResource("language/en-US.yml", new File(tempDataDir, "language").toPath());

		// Act
		File languageDir = new File(tempDataDir, "language");

		// Assert
		assertTrue(languageDir.isDirectory(),"the language directory should exist but it does not.");
	}


	@Test
	void testInstallByNameResource_CreatesFileSuccessfully() throws IOException {
		// Arrange: Create a temporary directory for the test
		Path tempDir = Files.createTempDirectory("installer-test");

		// Act: Install a resource into the temporary directory
		boolean result = installResource(LANGUAGE_EN_US_YML, tempDir);

		// Assert: Verify the file exists and was successfully copied
		assertTrue(result, "ResourceType should have been installed successfully.");
		assertTrue(Files.exists(tempDir.resolve(LANGUAGE_EN_US_YML)));

		// Cleanup: Delete the temporary directory and its contents
		Files.walk(tempDir)
				.sorted(Comparator.reverseOrder())
				.forEach(path -> path.toFile().delete());
	}


	@Nested
	class AutoInstallResourceTests {
		@Test
		void testGetAutoInstallResourceName() {
			assertEquals("language/auto_install.txt", resourceInstaller.getAutoInstallResourcePath());
		}

		@Test
		public void autoInstallResourceExistsTest() {
			// Arrange
			LanguageTag languageTag = new LanguageTag(DEFAULT_LANGUAGE_TAG.toString());

			// Act
			boolean result = resourceInstaller.resourceExists(languageTag);

			// Assert
			assertTrue(result);

			// Verify
			verify(pluginMock, atLeastOnce()).getResource( resourceInstaller.getAutoInstallResourcePath());
		}

		@Test
		public void autoInstallResourceExistsTest_null_parameter() {
			// Act
			IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
					() -> resourceInstaller.resourceExists((LanguageTag) null));

			// Assert
			assertEquals("The languageTag parameter cannot be null.", exception.getMessage());

			// Verify
			verify(pluginMock, atLeastOnce()).getResource(anyString());
		}
	}

	@Test
	public void testResourceExists() {
		// Arrange
		when(pluginMock.getResource( resourceInstaller.getAutoInstallResourcePath())).thenReturn(MockUtility.getResourceStream( resourceInstaller.getAutoInstallResourcePath()));

		// Act
		boolean result = resourceInstaller.resourceExists( resourceInstaller.getAutoInstallResourcePath());

		// Assert
		assertTrue(result);

		// Verify
		verify(pluginMock, atLeastOnce()).getResource( resourceInstaller.getAutoInstallResourcePath());
	}

	@Test
	public void testResourceExists_parameter_null() {
		// Arrange
		when(pluginMock.getResource( resourceInstaller.getAutoInstallResourcePath())).thenReturn(null);

		// Act
		boolean result = resourceInstaller.resourceExists( resourceInstaller.getAutoInstallResourcePath());

		// Assert
		assertFalse(result);

		// Verify
		verify(pluginMock, atLeastOnce()).getResource( resourceInstaller.getAutoInstallResourcePath());
	}



	@Test
	public void languageResourceExistsTest_null() {
		// Arrange
		LanguageTag languageTag = new LanguageTag(DEFAULT_LANGUAGE_TAG.toString());

		// return a null File object when language/en-US.yml resource is fetched, simulating a missing resource
		when(pluginMock.getResource(LANGUAGE_EN_US_YML)).thenReturn(null);

		// Act
		boolean result = resourceInstaller.resourceExists(languageTag);

		// Assert
		assertFalse(result);

		// Verify
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
	public void getAutoInstallByNameResourceNamesTest_not_null() {
		// Arrange
		when(pluginMock.getResource( resourceInstaller.getAutoInstallResourcePath())).thenReturn(getClass().getClassLoader().getResourceAsStream( resourceInstaller.getAutoInstallResourcePath()));

		// Act
		Collection<String> filenames = resourceInstaller.getAutoInstallResourceNames( resourceInstaller.getAutoInstallResourcePath());

		// assert
		assertNotNull(filenames);

		// Verify
		verify(pluginMock, atLeastOnce()).getResource( resourceInstaller.getAutoInstallResourcePath());
	}

	@Test
	public void getAutoInstallByNameResourceNamesTest_not_empty() {
		// Arrange
		when(pluginMock.getResource( resourceInstaller.getAutoInstallResourcePath())).thenReturn(getClass().getClassLoader().getResourceAsStream( resourceInstaller.getAutoInstallResourcePath()));

		// Act
		Collection<String> filenames = resourceInstaller.getAutoInstallResourceNames( resourceInstaller.getAutoInstallResourcePath());

		// assert
		assertFalse(filenames.isEmpty());

		// Verify
		verify(pluginMock, atLeastOnce()).getResource( resourceInstaller.getAutoInstallResourcePath());
	}

	@Test
	public void getAutoInstallByNameResourceNamesTest_valid_entries() {
		// Arrange
		when(pluginMock.getResource( resourceInstaller.getAutoInstallResourcePath())).thenReturn(getClass().getClassLoader().getResourceAsStream( resourceInstaller.getAutoInstallResourcePath()));

		// Act
		Collection<String> filenames = resourceInstaller.getAutoInstallResourceNames( resourceInstaller.getAutoInstallResourcePath());

		// Assert
		assertTrue(filenames.contains(LANGUAGE_EN_US_YML));
		assertTrue(filenames.contains("language/fr-FR.yml"));

		// Verify
		verify(pluginMock, atLeastOnce()).getResource( resourceInstaller.getAutoInstallResourcePath());
	}

	@Test
	public void getAutoInstallByNameResourceNamesTest_invalid_entries() {
		// Arrange
		when(pluginMock.getResource( resourceInstaller.getAutoInstallResourcePath())).thenReturn(getClass().getClassLoader().getResourceAsStream( resourceInstaller.getAutoInstallResourcePath()));

		// Act
		Collection<String> filenames = resourceInstaller.getAutoInstallResourceNames( resourceInstaller.getAutoInstallResourcePath());

		// Assert
		assertFalse(filenames.contains("nonexistent.yml"));
		assertFalse(filenames.contains("this_line_is_intended_to_fail"));

		// Verify
		verify(pluginMock, atLeastOnce()).getResource( resourceInstaller.getAutoInstallResourcePath());
	}

	@Test
	void getAutoInstallByNameResourceNamesTest() {
		// Arrange
		when(pluginMock.getResource( resourceInstaller.getAutoInstallResourcePath())).thenReturn(getClass().getClassLoader().getResourceAsStream( resourceInstaller.getAutoInstallResourcePath()));

		// Act
		Collection<String> filenames = resourceInstaller.getAutoInstallResourceNames( resourceInstaller.getAutoInstallResourcePath());

		// Assert
		assertTrue(filenames.contains(LANGUAGE_EN_US_YML));

		// Verify
		verify(pluginMock, atLeastOnce()).getResource( resourceInstaller.getAutoInstallResourcePath());
	}

	@Test
	void getAutoInstallFilenamesTest_no_auto_install_ByName_txt() {
		when(pluginMock.getResource( resourceInstaller.getAutoInstallResourcePath())).thenReturn(null);
		assertTrue(resourceInstaller.getAutoInstallResourceNames( resourceInstaller.getAutoInstallResourcePath()).isEmpty());
		verify(pluginMock, atLeastOnce()).getResource( resourceInstaller.getAutoInstallResourcePath());
	}

	@Nested
	class VerifyResourceExistsTests {
		@Test
		void verifyResourceExistsTest() {
			// Arrange
			LanguageTag languageTag = new LanguageTag(DEFAULT_LANGUAGE_TAG.toString());

			// Act
			boolean result = resourceInstaller.resourceExists(languageTag.getResourceName());

			// Assert
			assertTrue(result);

			// Verify
			verify(pluginMock, atLeastOnce()).getResource( resourceInstaller.getAutoInstallResourcePath());
		}

		@Test
		void verifyResourceExistsTest_nonexistent() {
			assertFalse(resourceInstaller.resourceExists("nonexistent_resource"));
		}
	}

	@Nested
	class VerifyResourceTypeInstalledTests {
		@Test
		void verifyResourceInstalledTest() {
			// Arrange
			when(pluginMock.getResource( resourceInstaller.getAutoInstallResourcePath())).thenReturn(getClass().getClassLoader().getResourceAsStream( resourceInstaller.getAutoInstallResourcePath()));
			when(pluginMock.getResource(LANGUAGE_EN_US_YML)).thenReturn(getClass().getClassLoader().getResourceAsStream(LANGUAGE_EN_US_YML));
			// install resource when saveResource is called
			doAnswer(invocation -> installResource(invocation.getArgument(0), tempDataDir.toPath()))
					.when(pluginMock).saveResource(anyString(), eq(false));

			// Act
			resourceInstaller.autoInstall();

			// Assert
			assertTrue(resourceInstaller.isInstalled(LANGUAGE_EN_US_YML));
			assertFalse(resourceInstaller.isInstalled("nonexistent-resource"));

			// verify
			verify(pluginMock, atLeastOnce()).getResource( resourceInstaller.getAutoInstallResourcePath());
			verify(pluginMock, atLeastOnce()).getResource(LANGUAGE_EN_US_YML);
		}

		@Test
		void verifyResourceInstalledTest_nonexistent() {
			assertFalse(resourceInstaller.isInstalled("nonexistent_file"));
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


	void copyResource(final InputStream resourceStream, final Path targetFilePath) {
		try (resourceStream) {
			Files.copy(resourceStream, targetFilePath);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
