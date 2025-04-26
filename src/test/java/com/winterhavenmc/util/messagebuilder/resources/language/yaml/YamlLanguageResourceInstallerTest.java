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

import com.winterhavenmc.util.messagebuilder.resources.configuration.LanguageTag;
import com.winterhavenmc.util.messagebuilder.util.MockUtility;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Comparator;
import java.util.Locale;
import java.util.logging.Logger;

import static com.winterhavenmc.util.messagebuilder.resources.language.yaml.YamlLanguageResourceInstaller.*;
import static com.winterhavenmc.util.messagebuilder.util.MockUtility.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class YamlLanguageResourceInstallerTest
{
	@TempDir File tempDataDir;
	@Mock Plugin pluginMock;

	YamlLanguageResourceInstaller resourceInstaller;

	@BeforeEach
	public void setUp()
	{
		when(pluginMock.getLogger()).thenReturn(Logger.getLogger(this.getClass().getName()));
		when(pluginMock.getDataFolder()).thenReturn(tempDataDir);

		doAnswer(invocation -> getClass().getClassLoader().getResourceAsStream(invocation.getArgument(0)))
				.when(pluginMock).getResource(anyString());

		// create real instance of installer
		resourceInstaller = new YamlLanguageResourceInstaller(pluginMock);
		resourceInstaller.autoInstall();
	}


	@Nested
	class PatternTests
	{
		@Test
		void testWhitespacePattern()
		{
			assertTrue(WHITESPACE.matcher(" ").find());
			assertTrue(WHITESPACE.matcher("\t").find());
			assertTrue(WHITESPACE.matcher("\n").find());
			assertFalse(WHITESPACE.matcher("abc").find());
		}

		@Test
		void testTwoOrMoreDotsPattern() {
			assertTrue(TWO_OR_MORE_DOTS.matcher("../example/path/name").find());
			assertTrue(TWO_OR_MORE_DOTS.matcher("example/../../path/name").find());
			assertFalse(TWO_OR_MORE_DOTS.matcher(".dotfile").find());
		}

		@Test
		void testLeadingSlashesPattern() {
			assertTrue(LEADING_SLASHES.matcher("/abc").find());
			assertTrue(LEADING_SLASHES.matcher("//xyz").find());
			assertFalse(LEADING_SLASHES.matcher("abc/xyz").find());
		}

		@Test
		void testTwoOrMoreSlashesPattern() {
			assertTrue(TWO_OR_MORE_SLASHES.matcher("//").find());
			assertTrue(TWO_OR_MORE_SLASHES.matcher("example//path/name").find());
			assertFalse(TWO_OR_MORE_SLASHES.matcher("example/path/name").find());
		}
	}


//	@Test
//	public void isInstalledForTag_parameter_null() {
//		ValidationException exception = assertThrows(ValidationException.class,
//				() -> resourceInstaller.isInstalledForTag(null));
//
//		assertEquals("The parameter 'languageTag' cannot be null.", exception.getMessage());
//	}

//	@Test
//	public void testInstallIfMissing_parameter_null() {
//		ValidationException exception = assertThrows(ValidationException.class,
//				() -> resourceInstaller.installIfMissing(null));
//
//		assertEquals("The parameter 'languageTag' cannot be null.", exception.getMessage());
//	}


//	@Nested
//	public class MockingSetupTests {

//		@Test
//		public void testResourceStream() throws IOException {
//			// Arrange & Act
//			InputStream resourceStream = getClass().getClassLoader().getResourceAsStream(YamlLanguageSetting.RESOURCE_LANGUAGE_EN_US_YML.toString());
//
//			// Assert
//			assertNotNull(resourceStream, "Resource stream should not be null.");
//
//			// Clean up
//			resourceStream.close();
//		}


//		@Test
//		public void testSaveResource_MocksCorrectly() throws IOException {
//			// Arrange
//			Path targetFilePath = tempDataDir.toPath().resolve(YamlLanguageSetting.RESOURCE_LANGUAGE_EN_US_YML.toString());
//
//			// Ensure parent directories exist
//			Files.createDirectories(targetFilePath.getParent());
//
//			// Simulate saving the resource by copying it from the test resources
//			InputStream resourceStream = getClass().getClassLoader().getResourceAsStream(YamlLanguageSetting.RESOURCE_LANGUAGE_EN_US_YML.toString());
//			copyResource(resourceStream, targetFilePath);
//			// when(pluginMock.saveResource(LANGUAGE_EN_US_YML, false)).thenReturn(copyResource(LANGUAGE_EN_US_YML, targetFilePath));
//
//			// Act
//			pluginMock.saveResource(YamlLanguageSetting.RESOURCE_LANGUAGE_EN_US_YML.toString(), false);
//
//			// Assert
//			File savedFile = new File(tempDataDir, YamlLanguageSetting.RESOURCE_LANGUAGE_EN_US_YML.toString());
//			assertTrue(savedFile.exists(), "The resource file should be saved to the data directory.");
//		}
//	}


	@Test
	public void testGetAutoInstall_name_constant()
	{
		assertEquals("language/auto_install.txt", resourceInstaller.getAutoInstallResourcePath());
	}


	@Test
	public void testGetAutoInstallResourceNames()
	{
		// Arrange & Act
		Collection<String> autoInstallFilenames = resourceInstaller.getAutoInstallResourceNames(resourceInstaller.getAutoInstallResourcePath());

		// Assert
		assertFalse(autoInstallFilenames.isEmpty());
		assertTrue(autoInstallFilenames.contains("language/en-US.yml"));
		assertFalse(autoInstallFilenames.contains("language/en-GB.yml"));

		// Verify
		verify(pluginMock, atLeastOnce()).getResource(resourceInstaller.getAutoInstallResourcePath());
	}


	@Test
	public void resourceExists()
	{
		assertTrue(resourceInstaller.resourceExists(resourceInstaller.getAutoInstallResourcePath()));
	}


	@Test
	public void isInstalled_true()
	{
		// Arrange
		LanguageTag languageTag = LanguageTag.of(Locale.US).orElseThrow();

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
	public void isInstalled_false() {
		// Arrange
		LanguageTag languageTag = LanguageTag.of(Locale.ROOT)
				.orElse(LanguageTag.of(Locale.US).orElseThrow());

		// Act
		boolean result = resourceInstaller.isInstalledForTag(languageTag);

		// Assert
		assertFalse(result);
	}


	@Test
	public void testInstallByNameIfMissing() {
		// Mock
		doAnswer(invocation -> installResource(invocation.getArgument(0), tempDataDir.toPath()))
				.when(pluginMock).saveResource(anyString(), eq(false));

		// Arrange
		LanguageTag languageTag = LanguageTag.of(Locale.US).orElseThrow();

		// Act
		YamlLanguageResourceInstaller.InstallerStatus status = resourceInstaller.installIfMissing(languageTag);

		// Assert
		assertEquals(YamlLanguageResourceInstaller.InstallerStatus.SUCCESS, status);

		// Verify
		verify(pluginMock, atLeastOnce()).saveResource(anyString(), eq(false));
	}


	@Test
	public void testInstallByNameIfMissing_file_exists() {
		// Mock
		doAnswer(invocation -> installResource(invocation.getArgument(0), tempDataDir.toPath()))
				.when(pluginMock).saveResource(anyString(), eq(false));

		// Arrange
		LanguageTag languageTag = LanguageTag.of(Locale.US).orElseThrow();
		resourceInstaller.install(languageTag);

		// Act
		YamlLanguageResourceInstaller.InstallerStatus status = resourceInstaller.installIfMissing(languageTag);

		// Assert
		assertEquals(YamlLanguageResourceInstaller.InstallerStatus.FILE_EXISTS, status);
	}


	@Test
	public void testInstall_ByName_success() {
		// Arrange
		doAnswer(invocation -> installResource(invocation.getArgument(0), tempDataDir.toPath()))
				.when(pluginMock).saveResource(anyString(), eq(false));

		// Act
		YamlLanguageResourceInstaller.InstallerStatus status = resourceInstaller.installByName(YamlLanguageSetting.RESOURCE_LANGUAGE_EN_US_YML.toString());

		// Assert
		assertEquals(YamlLanguageResourceInstaller.InstallerStatus.SUCCESS, status);
	}


	@Test
	public void testInstall_ByName_resource_unavailable() {
		// Act
		YamlLanguageResourceInstaller.InstallerStatus status = resourceInstaller.installByName("nonexistent-resource");

		// Assert
		assertEquals(YamlLanguageResourceInstaller.InstallerStatus.UNAVAILABLE, status);
	}


	@Test
	public void testInstall_ByName_file_exists() {
		// Arrange
		doAnswer(invocation -> installResource(invocation.getArgument(0), tempDataDir.toPath()))
				.when(pluginMock).saveResource(anyString(), eq(false));

		resourceInstaller.installByName(YamlLanguageSetting.RESOURCE_LANGUAGE_EN_US_YML.toString());

		// Act
		YamlLanguageResourceInstaller.InstallerStatus status = resourceInstaller.installByName(YamlLanguageSetting.RESOURCE_LANGUAGE_EN_US_YML.toString());

		// Assert
		assertEquals(YamlLanguageResourceInstaller.InstallerStatus.FILE_EXISTS, status);
	}


	@Test
	public void testInstall_ByName_parameter_null() {
		// Arrange
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> resourceInstaller.installByName(null));

		// Assert
		assertEquals("The parameter 'resourceName' cannot be null.", exception.getMessage());
	}


	@Test
	public void testInstallByNameForTag_success() {
		// Mock
		doAnswer(invocation -> installResource(invocation.getArgument(0), tempDataDir.toPath()))
				.when(pluginMock).saveResource(anyString(), eq(false));

		// Arrange
		LanguageTag languageTag = LanguageTag.of(Locale.US).orElseThrow();

		// Act
		YamlLanguageResourceInstaller.InstallerStatus status = resourceInstaller.install(languageTag);

		// Assert
		assertEquals(YamlLanguageResourceInstaller.InstallerStatus.SUCCESS, status);

		// Verify
		verify(pluginMock, atLeastOnce()).saveResource(anyString(), eq(false));
	}


	@Test
	public void testInstallByNameForTag_file_exists() {
		// Arrange
		doAnswer(invocation -> installResource(invocation.getArgument(0), tempDataDir.toPath()))
				.when(pluginMock).saveResource(anyString(), eq(false));

		LanguageTag languageTag = LanguageTag.of(Locale.US).orElseThrow();
		resourceInstaller.install(languageTag);

		// Act
		YamlLanguageResourceInstaller.InstallerStatus status = resourceInstaller.install(languageTag);

		// Assert
		assertEquals(YamlLanguageResourceInstaller.InstallerStatus.FILE_EXISTS, status);
	}


	@Test
	public void testInstallByNameForTag_resource_unavailable()
	{
		// Arrange
		LanguageTag languageTag = LanguageTag.of(Locale.ROOT).orElseThrow();

		// Act
		YamlLanguageResourceInstaller.InstallerStatus status = resourceInstaller.install(languageTag);

		// Assert
		assertEquals(YamlLanguageResourceInstaller.InstallerStatus.UNAVAILABLE, status);
	}


//	@Test
//	public void testInstallByNameForTag_parameter_null()
//	{
//		// Arrange
//		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
//				() -> resourceInstaller.install(null));
//
//		// Assert
//		assertEquals("The parameter 'languageTag' cannot be null.", exception.getMessage());
//	}


	@Test
	public void verifyLanguageDirectoryTest_exists() throws IOException
	{
		// Arrange
		MockUtility.installResource("language/en-US.yml", new File(tempDataDir, "language").toPath());

		// Act
		File languageDir = new File(tempDataDir, "language");

		// Assert
		assertTrue(languageDir.isDirectory(),"the language directory should exist but it does not.");
	}


	@Test
	public void testInstallByNameResource_CreatesFileSuccessfully() throws IOException
	{
		// Arrange: Create a temporary directory for the test
		Path tempDir = Files.createTempDirectory("installer-test");

		// Act: Install a resource into the temporary directory
		long result = installResource(YamlLanguageSetting.RESOURCE_LANGUAGE_EN_US_YML.toString(), tempDir);

		// Assert: Verify the file exists and was successfully copied
		assertTrue(result> 0, "ResourceType should have been installed successfully.");
		assertTrue(Files.exists(tempDir.resolve(YamlLanguageSetting.RESOURCE_LANGUAGE_EN_US_YML.toString())));

		// Cleanup: Delete the temporary directory and its contents
		Files.walk(tempDir)
				.sorted(Comparator.reverseOrder())
				.forEach(path -> path.toFile().delete());
	}


	@Nested
	class AutoInstallResourceTests
	{
		@Test
		public void testGetAutoInstallResourceName()
		{
			assertEquals("language/auto_install.txt", resourceInstaller.getAutoInstallResourcePath());
		}

		@Test
		public void autoInstallResourceExistsTest()
		{
			// Arrange
			LanguageTag languageTag = LanguageTag.of(Locale.US).orElseThrow();

			// Act
			boolean result = resourceInstaller.resourceExists(languageTag);

			// Assert
			assertTrue(result);

			// Verify
			verify(pluginMock, atLeastOnce()).getResource( resourceInstaller.getAutoInstallResourcePath());
		}

//		@Test
//		public void autoInstallResourceExistsTest_null_parameter() {
//			// Act
//			ValidationException exception = assertThrowsExactly(ValidationException.class,
//					() -> resourceInstaller.resourceExists((LanguageTag) null));
//
//			// Assert
//			assertEquals("The parameter 'languageTag' cannot be null.", exception.getMessage());
//
//			// Verify
//			verify(pluginMock, atLeastOnce()).getResource(anyString());
//		}
	}


	@Test
	public void testResourceExists()
	{
		// Arrange
		when(pluginMock.getResource(resourceInstaller.getAutoInstallResourcePath()))
				.thenReturn(MockUtility.getResourceStream( resourceInstaller.getAutoInstallResourcePath()));

		// Act
		boolean result = resourceInstaller.resourceExists( resourceInstaller.getAutoInstallResourcePath());

		// Assert
		assertTrue(result);

		// Verify
		verify(pluginMock, atLeastOnce()).getResource( resourceInstaller.getAutoInstallResourcePath());
	}


	@Test
	public void testResourceExists_parameter_null()
	{
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
		LanguageTag languageTag = LanguageTag.of(Locale.US).orElseThrow();

		// return a null File object when language/en-US.yml resource is fetched, simulating a missing resource
		when(pluginMock.getResource(YamlLanguageSetting.RESOURCE_LANGUAGE_EN_US_YML.toString())).thenReturn(null);

		// Act
		boolean result = resourceInstaller.resourceExists(languageTag);

		// Assert
		assertFalse(result);

		// Verify
		verify(pluginMock, atLeastOnce()).getResource(YamlLanguageSetting.RESOURCE_LANGUAGE_EN_US_YML.toString());
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
		assertTrue(filenames.contains("language/en-US.yml"));
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
	public void getAutoInstallByNameResourceNamesTest() {
		// Arrange
		when(pluginMock.getResource( resourceInstaller.getAutoInstallResourcePath())).thenReturn(getClass().getClassLoader().getResourceAsStream( resourceInstaller.getAutoInstallResourcePath()));

		// Act
		Collection<String> filenames = resourceInstaller.getAutoInstallResourceNames( resourceInstaller.getAutoInstallResourcePath());

		// Assert
		assertTrue(filenames.contains("language/en-US.yml"));

		// Verify
		verify(pluginMock, atLeastOnce()).getResource( resourceInstaller.getAutoInstallResourcePath());
	}

	@Test
	public void getAutoInstallFilenamesTest_no_auto_install_ByName_txt() {
		when(pluginMock.getResource( resourceInstaller.getAutoInstallResourcePath())).thenReturn(null);
		assertTrue(resourceInstaller.getAutoInstallResourceNames( resourceInstaller.getAutoInstallResourcePath()).isEmpty());
		verify(pluginMock, atLeastOnce()).getResource( resourceInstaller.getAutoInstallResourcePath());
	}

	@Nested
	class VerifyResourceExistsTests {
		@Test
		public void verifyResourceExistsTest() {
			// Arrange
			LanguageTag languageTag = LanguageTag.of(Locale.US).orElseThrow();

			// Act
			boolean result = resourceInstaller.resourceExists(YamlLanguageResourceManager.getResourceName(languageTag));

			// Assert
			assertTrue(result);

			// Verify
			verify(pluginMock, atLeastOnce()).getResource( resourceInstaller.getAutoInstallResourcePath());
		}

		@Test
		public void verifyResourceExistsTest_nonexistent() {
			assertFalse(resourceInstaller.resourceExists("nonexistent_resource"));
		}
	}

	@Nested
	class VerifyResourceTypeInstalledTests {
		@Test
		public void verifyResourceInstalledTest() {
			// Arrange
			when(pluginMock.getResource( resourceInstaller.getAutoInstallResourcePath())).thenReturn(getClass().getClassLoader().getResourceAsStream( resourceInstaller.getAutoInstallResourcePath()));
			when(pluginMock.getResource(YamlLanguageSetting.RESOURCE_LANGUAGE_EN_US_YML.toString())).thenReturn(getClass().getClassLoader().getResourceAsStream(YamlLanguageSetting.RESOURCE_LANGUAGE_EN_US_YML.toString()));
			// install resource when saveResource is called
			doAnswer(invocation -> installResource(invocation.getArgument(0), tempDataDir.toPath()))
					.when(pluginMock).saveResource(anyString(), eq(false));

			// Act
			resourceInstaller.autoInstall();

			// Assert
			assertTrue(resourceInstaller.isInstalled("language/en-US.yml"));
			assertFalse(resourceInstaller.isInstalled("nonexistent-resource"));

			// verify
			verify(pluginMock, atLeastOnce()).getResource( resourceInstaller.getAutoInstallResourcePath());
			verify(pluginMock, atLeastOnce()).getResource(YamlLanguageSetting.RESOURCE_LANGUAGE_EN_US_YML.toString());
		}

		@Test
		public void verifyResourceInstalledTest_nonexistent() {
			assertFalse(resourceInstaller.isInstalled("nonexistent_file"));
		}
	}


//	public void deleteTempFiles()
//	{
//		// delete language/en-US.yml file from temp dir after each test
//		try {
//			Files.deleteIfExists(Path.of(tempDataDir.getAbsolutePath(),"language", "en-US.yml"));
//		}
//		catch (IOException e) {
//			System.out.println("File exists: " + Files.exists(Path.of(tempDataDir.getAbsolutePath(),"language", "en-US.yml")));
//		}
//	}

}
