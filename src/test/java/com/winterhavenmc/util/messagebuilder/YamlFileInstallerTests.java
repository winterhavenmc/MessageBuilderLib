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

package com.winterhavenmc.util.messagebuilder;

import com.winterhavenmc.util.messagebuilder.mocks.MockPlugin;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.logging.Logger;

import static com.winterhavenmc.util.messagebuilder.mocks.MockPlugin.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class YamlFileInstallerTests {

	private Plugin plugin;

	private YamlFileInstaller yamlFileInstaller;
	private Collection<String> filenames;


	@BeforeAll
	public static void preSetUp() {
		System.out.println("Temporary data directory exists: " + getDataFolder().isDirectory());
		verifyLangDir();
	}

	@BeforeEach
	public void setUp() throws IOException {

		// create new mock plugin
		plugin = mock(Plugin.class, "MockPlugin");

		// return temporary directory for mock plugin data directory
		when(plugin.getDataFolder()).thenReturn(MockPlugin.getDataFolder());

		// return real logger for mock plugin
		when(plugin.getLogger()).thenReturn(Logger.getLogger(this.getClass().getName()));

		// return real file input streams for mock plugin resources
		doAnswer(invocation -> getResourceStream(invocation.getArgument(0)))
				.when(plugin).getResource(anyString());

		// install resource when saveResource is called
		doAnswer(invocation -> installResource(invocation.getArgument(0)))
				.when(plugin).saveResource(anyString(), eq(false));

		// create instance of installer
		yamlFileInstaller = new YamlFileInstaller(plugin);
		yamlFileInstaller.install();

		// get filenames from installer
		filenames = yamlFileInstaller.getAutoInstallFilenames();
	}

	@AfterEach
	public void tearDown() {
		plugin = null;
		yamlFileInstaller = null;
		filenames = null;
	}


	@Test
	void verifyLanguageDirectoryTest_exists() {
		assertTrue(new File(getDataFolder(), "language").isDirectory(),
				"the language directory should exist but it does not.");
	}

	@Test
	public void autoInstallResourceExistsTest() {
		assertTrue(yamlFileInstaller.verifyResourceExists(AUTO_INSTALL_TXT));
		verify(plugin, atLeastOnce()).getResource(AUTO_INSTALL_TXT);
	}

	@Test
	public void autoInstallResourceExistsTest_null() {
		// return a null File object when language/auto_install.txt resource is fetched, simulating a missing resource
		when(plugin.getResource(AUTO_INSTALL_TXT)).thenReturn(null);
		assertFalse(yamlFileInstaller.verifyResourceExists(AUTO_INSTALL_TXT));
		verify(plugin, atLeastOnce()).getResource(AUTO_INSTALL_TXT);
	}

	@Test
	public void languageResourceExistsTest() {
		assertTrue(yamlFileInstaller.verifyResourceExists(LANGUAGE_EN_US_YML));
		verify(plugin, atLeastOnce()).getResource(LANGUAGE_EN_US_YML);
	}

	@Test
	public void languageResourceExistsTest_null() {
		// return a null File object when language/en-US.yml resource is fetched, simulating a missing resource
		when(plugin.getResource(LANGUAGE_EN_US_YML)).thenReturn(null);
		assertFalse(yamlFileInstaller.verifyResourceExists(LANGUAGE_EN_US_YML));
		verify(plugin, atLeastOnce()).getResource(LANGUAGE_EN_US_YML);
	}

	@Test
	public void getDataFolderTest_not_null() {
		assertNotNull(plugin.getDataFolder());
		verify(plugin, atLeastOnce()).getDataFolder();
	}

	@Test
	public void getDataFolderTest_is_directory() {
		assertTrue(plugin.getDataFolder().isDirectory());
		verify(plugin, atLeastOnce()).getDataFolder();
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
		assertTrue(filenames.contains("en-US.yml"));
		assertTrue(filenames.contains("fr-FR.yml"));
	}

	@Test
	public void getAutoInstallFilenamesTest_invalid_entries() {
		assertFalse(filenames.contains("nonexistent.yml"));
		assertFalse(filenames.contains("this_line_is_intended_to_fail"));
	}

	@Test
	void getAutoInstallFilenamesTest() {
		assertTrue(yamlFileInstaller.getAutoInstallFilenames().contains("en-US.yml"));
		verify(plugin, atLeastOnce()).getResource(AUTO_INSTALL_TXT);
	}

	@Test
	void getAutoInstallFilenamesTest_no_auto_install_txt() {
		when(plugin.getResource(AUTO_INSTALL_TXT)).thenReturn(null);
		assertTrue(yamlFileInstaller.getAutoInstallFilenames().isEmpty());
		verify(plugin, atLeastOnce()).getResource(AUTO_INSTALL_TXT);
	}

	@Test
	void verifyResourceExistsTest() {
		assertTrue(yamlFileInstaller.verifyResourceExists(LANGUAGE_EN_US_YML));
		verify(plugin, atLeastOnce()).getResource(AUTO_INSTALL_TXT);
	}

	@Test
	void verifyResourceExistsTest_nonexistent() {
		assertFalse(yamlFileInstaller.verifyResourceExists("nonexistent"));
		verify(plugin, atLeastOnce()).getResource(AUTO_INSTALL_TXT);
	}

	@Test
	void verifyResourceInstalledTest() {
		assertTrue(yamlFileInstaller.verifyResourceInstalled(LANGUAGE_EN_US_YML));
	}

	@Test
	void verifyResourceInstalledTest_nonexistent() {
		assertFalse(yamlFileInstaller.verifyResourceInstalled("nonexistent"));
	}

}
