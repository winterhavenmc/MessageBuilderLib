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

import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.logging.Logger;


public class YamlFileInstallerTests {

	private final Plugin mockPlugin = mock(Plugin.class);
	private final static File autoInstallFile = new File(Paths.get("src","test", "resources", "language", "auto_install.txt").toUri());
	private final static File enLanguageFile = new File(Paths.get("src", "test", "resources", "language", "en-US.yml").toUri());

	private YamlFileInstaller yamlFileInstaller;
	private Collection<String> filenames;

	@BeforeEach
	public void setUp() throws IOException {

		String tempDir = Files.createTempDirectory("PluginData").toFile().getAbsolutePath();

		// return real logger for mock plugin
        when(mockPlugin.getLogger()).thenReturn(Logger.getLogger("Test Logger"));

		// return real file input stream for mock plugin get resource
		when(mockPlugin.getResource("language/auto_install.txt"))
				.thenReturn(new FileInputStream(autoInstallFile));

		// return real file input stream for mock plugin get resource
		when(mockPlugin.getResource("language/en-US.yml"))
				.thenReturn(new FileInputStream(enLanguageFile));

		// return temporary directory for mock plugin data directory
		when(mockPlugin.getDataFolder()).thenReturn(new File(tempDir));

		// create instance of installer
		yamlFileInstaller = new YamlFileInstaller(mockPlugin);

		// get filenames from installer
		filenames = yamlFileInstaller.getAutoInstallFilenames();
	}

	@AfterEach
	public void tearDown() {
		// destroy objects
		yamlFileInstaller = null;
		filenames = null;
	}


	@Test
	public void autoInstallResourceExistsTest() {
		assertTrue(yamlFileInstaller.verifyResourceExists("language/auto_install.txt"));
	}

	@Test
	public void autoInstallResourceExistsTest_null() {
		when(mockPlugin.getResource("language/auto_install.txt"))
				.thenReturn(null);
		assertFalse(yamlFileInstaller.verifyResourceExists("language/auto_install.txt"));
	}

	@Test
	public void languageResourceExistsTest() {
		assertTrue(yamlFileInstaller.verifyResourceExists("language/en-US.yml"));
	}

	@Test
	public void languageResourceExistsTest_null() {
		when(mockPlugin.getResource("language/en-US.yml"))
				.thenReturn(null);
		assertFalse(yamlFileInstaller.verifyResourceExists("language/en-US.yml"));
	}

	@Test
	public void getDataFolderTest_not_null() {
		assertNotNull(mockPlugin.getDataFolder());
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
	public void installTest() {
		assertFalse(yamlFileInstaller.verifyResourceInstalled("en-US.yml"));
		yamlFileInstaller.install();
//		assertTrue(yamlFileInstaller.isResourceInstalled("en-US.yml"));
	}

}
