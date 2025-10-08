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

package com.winterhavenmc.library.messagebuilder.core.ports.resources;

import com.winterhavenmc.library.messagebuilder.configuration.LocaleProvider;
import com.winterhavenmc.library.messagebuilder.core.ports.resources.language.LanguageConfigConstant;
import com.winterhavenmc.library.messagebuilder.core.ports.resources.language.LanguageResourceInstaller;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
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

import static com.winterhavenmc.library.messagebuilder.core.ports.resources.language.LanguageResourceInstaller.*;
import static com.winterhavenmc.library.messagebuilder.core.util.MockUtility.installResource;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class LanguageResourceInstallerTest
{
	@TempDir File tempDataDir;
	@Mock Plugin pluginMock;
	@Mock LocaleProvider localeProviderMock;
	@Mock FileConfiguration fileConfigurationMock;

	LanguageResourceInstaller resourceInstaller;

	@BeforeEach
	public void setUp()
	{
		when(localeProviderMock.getLocale()).thenReturn(Locale.US);
		when(pluginMock.getLogger()).thenReturn(Logger.getLogger(this.getClass().getName()));
		when(pluginMock.getDataFolder()).thenReturn(tempDataDir);

		doAnswer(invocation -> getClass().getClassLoader().getResourceAsStream(invocation.getArgument(0)))
				.when(pluginMock).getResource(anyString());

		// create real instance of installer
		resourceInstaller = new LanguageResourceInstaller(pluginMock, localeProviderMock);
		resourceInstaller.autoInstall();
	}


	@Nested
	class PatternTests
	{
		@Test
		void contains_whitespace()
		{
			assertTrue(WHITESPACE.matcher(" ").find());
			assertTrue(WHITESPACE.matcher("\t").find());
			assertTrue(WHITESPACE.matcher("\n").find());
			assertFalse(WHITESPACE.matcher("abc").find());
		}

		@Test
		void contains_two_or_more_dots()
		{
			assertTrue(TWO_OR_MORE_DOTS.matcher("../example/path/name").find());
			assertTrue(TWO_OR_MORE_DOTS.matcher("example/../../path/name").find());
			assertFalse(TWO_OR_MORE_DOTS.matcher(".dotfile").find());
		}

		@Test
		void contains_leading_slashes()
		{
			assertTrue(LEADING_SLASHES.matcher("/abc").find());
			assertTrue(LEADING_SLASHES.matcher("//xyz").find());
			assertFalse(LEADING_SLASHES.matcher("abc/xyz").find());
		}

		@Test
		void contains_two_or_more_adjacent_slashes()
		{
			assertTrue(TWO_OR_MORE_SLASHES.matcher("//").find());
			assertTrue(TWO_OR_MORE_SLASHES.matcher("example//path/name").find());
			assertFalse(TWO_OR_MORE_SLASHES.matcher("example/path/name").find());
		}
	}


	@Test
	public void getAutoInstallSet_contains_only_valid_entry()
	{
		// Arrange & Act
		Collection<String> autoInstallFilenames = resourceInstaller.getAutoInstallSet(resourceInstaller.getAutoInstallResourceName());

		// Assert
		assertFalse(autoInstallFilenames.isEmpty());
		assertTrue(autoInstallFilenames.contains("language/en-US.yml"));
		assertFalse(autoInstallFilenames.contains("language/en-GB.yml"));

		// Verify
		verify(pluginMock, atLeastOnce()).getResource(resourceInstaller.getAutoInstallResourceName());
	}


	@Nested
	class IsInstalledTests
	{
		@Test
		void file_does_exists()
		{
			// Arrange
			String filename = "language/en-US.yml";

			// install resource when saveResource is called
			doAnswer(invocation -> installResource(invocation.getArgument(0), tempDataDir.toPath()))
					.when(pluginMock).saveResource(anyString(), eq(false));

			// Act
			resourceInstaller.installIfMissing(filename);

			// Assert
			assertTrue(resourceInstaller.isInstalled(filename));

			// Verify
			verify(pluginMock, atLeastOnce()).saveResource(anyString(), eq(false));
		}

		@Test
		void file_does_not_exist()
		{
			// Arrange
			String filename = "nonexistent_resource";

			// Act
			boolean result = resourceInstaller.isInstalled(filename);

			// Assert
			assertFalse(result);
		}

		@Test
		void filename_is_null()
		{
			// Arrange
			String filename = null;

			// Act
			boolean result = resourceInstaller.isInstalled(filename);

			// Assert
			assertFalse(result);
		}
	}


	@Nested
	class InstallByNameTests
	{
		@Test
		public void success()
		{
			// Arrange
			doAnswer(invocation -> installResource(invocation.getArgument(0), tempDataDir.toPath()))
					.when(pluginMock).saveResource(anyString(), eq(false));

			// Act
			LanguageResourceInstaller.InstallerStatus status = resourceInstaller.installByName("language/en-US.yml");

			// Assert
			assertEquals(LanguageResourceInstaller.InstallerStatus.SUCCESS, status);
		}

		@Test
		public void file_exists()
		{
			// Mock
			doAnswer(invocation -> installResource(invocation.getArgument(0), tempDataDir.toPath()))
					.when(pluginMock).saveResource(anyString(), eq(false));

			// Arrange
			String filename = "language/en-US.yml";
			resourceInstaller.installByName(filename);

			// Act
			LanguageResourceInstaller.InstallerStatus status = resourceInstaller.installIfMissing(filename);

			// Assert
			assertEquals(LanguageResourceInstaller.InstallerStatus.FILE_EXISTS, status);
		}


		@Test
		public void resource_unavailable()
		{
			// Act
			LanguageResourceInstaller.InstallerStatus status = resourceInstaller.installByName("nonexistent-resource");

			// Assert
			assertEquals(LanguageResourceInstaller.InstallerStatus.UNAVAILABLE, status);
		}
	}


	@Test
	public void testInstall_ByName_parameter_null()
	{
		// Arrange
		// Act
		InstallerStatus result = resourceInstaller.installByName(null);

		// Assert
		assertEquals(InstallerStatus.UNAVAILABLE, result);
	}


	@Test
	public void verifyLanguageDirectoryTest_exists() throws IOException
	{
		// Arrange
		installResource("language/en-US.yml", new File(tempDataDir, "language").toPath());

		// Act
		File languageDir = new File(tempDataDir, "language");

		// Assert
		assertTrue(languageDir.isDirectory(), "the language directory should exist but it does not.");
	}


	@Test
	public void testInstallByNameResource_CreatesFileSuccessfully() throws IOException
	{
		// Arrange: Create a temporary directory for the test
		Path tempDir = Files.createTempDirectory("installer-test");

		// Act: Install a resource into the temporary directory
		long result = installResource("language/en-US.yml", tempDir);

		// Assert: Verify the file exists and was successfully copied
		assertTrue(result > 0, "Resource should have been installed successfully.");
		assertTrue(Files.exists(tempDir.resolve("language/en-US.yml")));

		// Cleanup: Delete the temporary directory and its contents
		Files.walk(tempDir)
				.sorted(Comparator.reverseOrder())
				.forEach(path -> path.toFile().delete());
	}


	@Nested
	class AutoInstallResourceTests
	{
		@Test
		public void auto_install_resource_exists()
		{
			assertEquals("language/auto_install.txt", resourceInstaller.getAutoInstallResourceName());
		}

	}


	@Test
	public void getDataFolderTest_not_null()
	{
		// Assert
		assertNotNull(pluginMock.getDataFolder());

		// Verify
		verify(pluginMock, atLeastOnce()).getDataFolder();
	}

	@Test
	public void getDataFolderTest_is_directory()
	{
		// Assert
		assertTrue(pluginMock.getDataFolder().isDirectory());

		// Verify
		verify(pluginMock, atLeastOnce()).getDataFolder();
	}

	@Test
	public void getAutoInstallByNameResourceNamesTest_not_null()
	{
		// Arrange
		when(pluginMock.getResource(resourceInstaller.getAutoInstallResourceName()))
				.thenReturn(getClass().getClassLoader()
				.getResourceAsStream(resourceInstaller.getAutoInstallResourceName()));

		// Act
		Collection<String> filenames = resourceInstaller.getAutoInstallSet(resourceInstaller.getAutoInstallResourceName());

		// assert
		assertNotNull(filenames);

		// Verify
		verify(pluginMock, atLeastOnce()).getResource(resourceInstaller.getAutoInstallResourceName());
	}

	@Test
	public void getAutoInstallByNameResourceNamesTest_not_empty()
	{
		// Arrange
		when(pluginMock.getResource(resourceInstaller.getAutoInstallResourceName()))
				.thenReturn(getClass().getClassLoader().getResourceAsStream(resourceInstaller.getAutoInstallResourceName()));

		// Act
		Collection<String> filenames = resourceInstaller.getAutoInstallSet(resourceInstaller.getAutoInstallResourceName());

		// assert
		assertFalse(filenames.isEmpty());

		// Verify
		verify(pluginMock, atLeastOnce()).getResource(resourceInstaller.getAutoInstallResourceName());
	}

	@Test
	public void getAutoInstallByNameResourceNamesTest_valid_entries()
	{
		// Arrange
		when(pluginMock.getResource(resourceInstaller.getAutoInstallResourceName())).thenReturn(getClass().getClassLoader().getResourceAsStream(resourceInstaller.getAutoInstallResourceName()));

		// Act
		Collection<String> filenames = resourceInstaller.getAutoInstallSet(resourceInstaller.getAutoInstallResourceName());

		// Assert
		assertTrue(filenames.contains("language/en-US.yml"));
		assertTrue(filenames.contains("language/fr-FR.yml"));

		// Verify
		verify(pluginMock, atLeastOnce()).getResource(resourceInstaller.getAutoInstallResourceName());
	}

	@Test
	public void getAutoInstallByNameResourceNamesTest_invalid_entries()
	{
		// Arrange
		when(pluginMock.getResource(resourceInstaller.getAutoInstallResourceName())).thenReturn(getClass().getClassLoader().getResourceAsStream(resourceInstaller.getAutoInstallResourceName()));

		// Act
		Collection<String> filenames = resourceInstaller.getAutoInstallSet(resourceInstaller.getAutoInstallResourceName());

		// Assert
		assertFalse(filenames.contains("nonexistent.yml"));
		assertFalse(filenames.contains("this_line_is_intended_to_fail"));

		// Verify
		verify(pluginMock, atLeastOnce()).getResource(resourceInstaller.getAutoInstallResourceName());
	}

	@Test
	public void getAutoInstallByNameResourceNamesTest()
	{
		// Arrange
		when(pluginMock.getResource(resourceInstaller.getAutoInstallResourceName())).thenReturn(getClass().getClassLoader().getResourceAsStream(resourceInstaller.getAutoInstallResourceName()));

		// Act
		Collection<String> filenames = resourceInstaller.getAutoInstallSet(resourceInstaller.getAutoInstallResourceName());

		// Assert
		assertTrue(filenames.contains("language/en-US.yml"));

		// Verify
		verify(pluginMock, atLeastOnce()).getResource(resourceInstaller.getAutoInstallResourceName());
	}

	@Test
	public void getAutoInstallFilenamesTest_no_auto_install_ByName_txt()
	{
		when(pluginMock.getResource(resourceInstaller.getAutoInstallResourceName())).thenReturn(null);
		assertTrue(resourceInstaller.getAutoInstallSet(resourceInstaller.getAutoInstallResourceName()).isEmpty());
		verify(pluginMock, atLeastOnce()).getResource(resourceInstaller.getAutoInstallResourceName());
	}


	@Nested
	class VerifyResourceTypeInstalledTests
	{
		@Test
		public void verifyResourceInstalledTest()
		{
			// Arrange
			when(pluginMock.getResource(resourceInstaller.getAutoInstallResourceName())).thenReturn(getClass().getClassLoader().getResourceAsStream(resourceInstaller.getAutoInstallResourceName()));
			when(pluginMock.getResource(LanguageConfigConstant.RESOURCE_LANGUAGE_EN_US_YML.toString())).thenReturn(getClass().getClassLoader().getResourceAsStream(LanguageConfigConstant.RESOURCE_LANGUAGE_EN_US_YML.toString()));
			// install resource when saveResource is called
			doAnswer(invocation -> installResource(invocation.getArgument(0), tempDataDir.toPath()))
					.when(pluginMock).saveResource(anyString(), eq(false));

			// Act
			resourceInstaller.autoInstall();

			// Assert
			assertTrue(resourceInstaller.isInstalled("language/en-US.yml"));
			assertFalse(resourceInstaller.isInstalled("nonexistent-resource"));

			// verify
			verify(pluginMock, atLeastOnce()).getResource(resourceInstaller.getAutoInstallResourceName());
			verify(pluginMock, atLeastOnce()).getResource(LanguageConfigConstant.RESOURCE_LANGUAGE_EN_US_YML.toString());
		}

		@Test
		public void verifyResourceInstalledTest_nonexistent()
		{
			assertFalse(resourceInstaller.isInstalled("nonexistent_file"));
		}
	}

}
