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

import com.winterhavenmc.util.messagebuilder.validation.ValidationException;
import com.winterhavenmc.util.messagebuilder.util.MockUtility;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.winterhavenmc.util.messagebuilder.resources.language.yaml.Option.DEFAULT_LANGUAGE_TAG;
import static com.winterhavenmc.util.messagebuilder.resources.language.yaml.Option.RESOURCE_LANGUAGE_EN_US_YML;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class YamlLanguageResourceLoaderTest
{
	@TempDir File tempDataDir;
	@Mock Plugin pluginMock;

	FileConfiguration pluginConfiguration;
	Configuration languageConfiguration;

	YamlLanguageResourceLoader yamlLanguageResourceLoader;


	@BeforeEach
	public void setUp() throws IOException
	{
		// create real plugin config
		pluginConfiguration = new YamlConfiguration();
		pluginConfiguration.set("locale", DEFAULT_LANGUAGE_TAG);
		pluginConfiguration.set("language", DEFAULT_LANGUAGE_TAG);

		// create real language configuration
		languageConfiguration = MockUtility.loadConfigurationFromResource("language/en-US.yml");

		// create new real file loader
		yamlLanguageResourceLoader = new YamlLanguageResourceLoader(pluginMock);

		// install resource to temp directory; assert return value is true, indicating success
		long bytes = MockUtility.installResource(RESOURCE_LANGUAGE_EN_US_YML.toString(), tempDataDir.toPath());
		assertTrue(bytes > 0);
		System.out.println("Bytes copied: " + bytes + "     filepath: " + tempDataDir.getAbsolutePath());
	}

	@AfterEach
	public void tearDown()
	{
		pluginMock = null;
		pluginConfiguration = null;
		languageConfiguration = null;
		yamlLanguageResourceLoader = null;
	}


	@Test
	public void testLoad()
	{
		// Arrange
		when(pluginMock.getLogger()).thenReturn(Logger.getLogger(this.getClass().getName()));
		LanguageTag languageTag = new LanguageTag("en-US");

		// Act
		Configuration configuration = yamlLanguageResourceLoader.load(languageTag);

		// Assert
		assertNotNull(configuration);

		// Verify
		verify(pluginMock, atLeastOnce()).getLogger();
	}


	@Test
	public void testLoad_file_not_found()
	{
		// Arrange
		when(pluginMock.getConfig()).thenReturn(pluginConfiguration);
		when(pluginMock.getLogger()).thenReturn(Logger.getLogger(this.getClass().getName()));

		// Act
		Configuration configuration = yamlLanguageResourceLoader.load();

		// Assert
		assertNotNull(configuration);

		// Verify
		verify(pluginMock, atLeastOnce()).getConfig();
		verify(pluginMock, atLeastOnce()).getLogger();
	}


	@Test
	void getConfiguredLanguageTag()
	{
		// Arrange
		when(pluginMock.getConfig()).thenReturn(pluginConfiguration);

		// Act
		String languageTag = yamlLanguageResourceLoader.getConfiguredLanguageTag(pluginMock);

		// Assert
		assertEquals("en-US", languageTag);

		// Verify
		verify(pluginMock, atLeastOnce()).getConfig();
	}


	@Test
	void getConfiguredLanguageTag_parameter_null_plugin()
	{
		ValidationException exception = assertThrows(ValidationException.class,
				() -> yamlLanguageResourceLoader.getConfiguredLanguageTag(null));

		assertEquals("The parameter 'plugin' cannot be null.", exception.getMessage());
	}

}
