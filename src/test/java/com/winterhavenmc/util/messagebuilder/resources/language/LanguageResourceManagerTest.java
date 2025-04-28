/*
 * Copyright (c) 2022-2025 Tim Savage.
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

package com.winterhavenmc.util.messagebuilder.resources.language;

import com.winterhavenmc.util.messagebuilder.model.language.Section;
import com.winterhavenmc.util.messagebuilder.resources.configuration.LanguageTag;
import com.winterhavenmc.util.messagebuilder.util.MockUtility;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class LanguageResourceManagerTest
{
	@Mock LanguageResourceInstaller languageResourceInstallerMock;
	@Mock LanguageResourceLoader languageResourceLoaderMock;
	@Mock ConfigurationSection constantsSectionMock;
	@Mock ConfigurationSection itemsSectionMock;
	@Mock ConfigurationSection messagesSectionMock;
	@Mock Configuration languageConfigurationMock;

	LanguageResourceManager resourceManager;
	Configuration languageConfiguration;
	FileConfiguration pluginConfiguration;


	@BeforeEach
	void setUp()
	{
		// create real plugin config
		pluginConfiguration = new YamlConfiguration();
		pluginConfiguration.set("language", "en-US");
		pluginConfiguration.set("locale", "en-US");

		// create real language configuration
		languageConfiguration = MockUtility.loadConfigurationFromResource(LanguageSetting.RESOURCE_LANGUAGE_EN_US_YML.toString());

		// instantiate real language resource manager
		resourceManager = new LanguageResourceManager(languageResourceInstallerMock, languageResourceLoaderMock, languageConfigurationMock);
	}


	@Test
	void testConstructor()
	{
		LanguageResourceManager languageResourceManager = new LanguageResourceManager(languageResourceInstallerMock, languageResourceLoaderMock);

		assertNotNull(languageResourceManager);
	}


	@ParameterizedTest
	@EnumSource(Section.class)
	void getSectionProvider(Section section)
	{
		lenient().when(languageConfigurationMock.getConfigurationSection("MESSAGES")).thenReturn(messagesSectionMock);
		lenient().when(languageConfigurationMock.getConfigurationSection("ITEMS")).thenReturn(itemsSectionMock);
		lenient().when(languageConfigurationMock.getConfigurationSection("CONSTANTS")).thenReturn(constantsSectionMock);


		// Arrange & Act
		SectionProvider sectionProvider = resourceManager.getSectionProvider(section);

		// Assert
		assertNotNull(sectionProvider);
		assertInstanceOf(LanguageSectionProvider.class, sectionProvider);
		assertNotNull(sectionProvider.getSection());
	}


	@Nested
	class ReloadTests
	{
		@Test
		void testReload()
		{
			// Arrange & Act
			boolean success = resourceManager.reload();

			// Assert
			assertTrue(success);
		}


		@Test
		void testReload_new_config()
		{
			// Arrange
			FileConfiguration newLanguageConfiguration = new YamlConfiguration();
			newLanguageConfiguration.set("test_key", "test_value");

			// Act
			boolean success = resourceManager.reload();

			// Assert
			assertTrue(success);
		}
	}


	@Test
	void testGetResourceName()
	{
		// Arrange
		LanguageTag languageTag = LanguageTag.of(Locale.US).orElseThrow();

		// Act & Assert
		assertEquals("language/en-US.yml", LanguageResourceManager.getResourceName(languageTag));
		assertNotEquals("language/fr-FR.yml", LanguageResourceManager.getResourceName(languageTag));
	}


	@Test
	void testGetFileName()
	{
		// Arrange
		LanguageTag languageTag = LanguageTag.of(Locale.US).orElseThrow();

		// Act & Assert
		assertEquals("language" + File.separator + "en-US.yml", LanguageResourceManager.getFileName(languageTag));
		assertNotEquals("language" + File.separator + "fr-FR.yml", LanguageResourceManager.getFileName(languageTag));
	}

}
