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

package com.winterhavenmc.library.messagebuilder.adapters.resources.language;

import com.winterhavenmc.library.messagebuilder.adapters.util.MockUtility;
import com.winterhavenmc.library.messagebuilder.models.configuration.LocaleProvider;
import com.winterhavenmc.library.messagebuilder.core.ports.resources.language.*;
import com.winterhavenmc.library.messagebuilder.models.configuration.LanguageTag;
import com.winterhavenmc.library.messagebuilder.models.language.Section;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
	@Mock LanguageSectionProvider languageSectionProviderMock;
	@Mock LanguageResourceInstaller languageResourceInstallerMock;
	@Mock LanguageResourceLoader languageResourceLoaderMock;
	@Mock ConfigurationSection constantsSectionMock;
	@Mock ConfigurationSection itemsSectionMock;
	@Mock ConfigurationSection messagesSectionMock;
	@Mock Configuration languageConfigurationMock;
	@Mock Plugin pluginMock;
	@Mock ConstantRepository constantRepositoryMock;
	@Mock ItemRepository itemRepositoryMock;
	@Mock MessageRepository messageRepositoryMock;
	@Mock LocaleProvider localeProviderMock;

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
		languageConfiguration = MockUtility.loadConfigurationFromResource(LanguageConfigConstant.RESOURCE_LANGUAGE_EN_US_YML.toString());

		// instantiate real language resource manager
		resourceManager = new LanguageResourceManager(languageResourceInstallerMock, languageResourceLoaderMock, languageConfigurationMock);
	}


	@ParameterizedTest
	@EnumSource(Section.class)
	void getSectionProvider(Section section)
	{
		// Arrange
		lenient().when(languageConfigurationMock.getConfigurationSection("MESSAGES")).thenReturn(messagesSectionMock);
		lenient().when(languageConfigurationMock.getConfigurationSection("ITEMS")).thenReturn(itemsSectionMock);
		lenient().when(languageConfigurationMock.getConfigurationSection("CONSTANTS")).thenReturn(constantsSectionMock);

		// Act
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
			// Arrange
			when(languageResourceLoaderMock.load()).thenReturn(languageConfiguration);

			// Act
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
			when(languageResourceLoaderMock.load()).thenReturn(languageConfiguration);

			// Act
			boolean success = resourceManager.reload();

			// Assert
			assertTrue(success);
		}


		@Test
		@Disabled
		void reload_failure_returnsFalse()
		{
			// Arrange
			when(languageSectionProviderMock.getSection()).thenReturn(languageConfiguration);
			LanguageResourceLoader loader = new LanguageResourceLoader(pluginMock, localeProviderMock)
			{
				@Override
				public Configuration load() {
					return null; // Simulate failure
				}
			};

			LanguageResourceInstaller installer = new LanguageResourceInstaller(pluginMock, localeProviderMock);
			LanguageResourceManager languageResourceManager = new LanguageResourceManager(installer, loader);

			// Act
			boolean result = languageResourceManager.reload();

			// Assert
			assertFalse(result);

			// Verify
			verify(languageSectionProviderMock, atLeastOnce()).getSection();
		}
	}


	@Test
	@Disabled
	void getResourceName_returns_only_valid_string()
	{
		// Arrange
		LanguageTag languageTag = LanguageTag.of(Locale.US).orElseThrow();

		// Act & Assert
		assertEquals("language/en-US.yml", LanguageResourceManager.getResourceName(languageTag));
		assertNotEquals("language/fr-FR.yml", LanguageResourceManager.getResourceName(languageTag));
	}


	@Test
	@Disabled
	void getFileName_returns_only_valid_string()
	{
		// Arrange
		LanguageTag languageTag = LanguageTag.of(Locale.US).orElseThrow();

		// Act & Assert
		assertEquals("language" + File.separator + "en-US.yml", LanguageResourceManager.getFileName(languageTag));
		assertNotEquals("language" + File.separator + "fr-FR.yml", LanguageResourceManager.getFileName(languageTag));
	}

}
