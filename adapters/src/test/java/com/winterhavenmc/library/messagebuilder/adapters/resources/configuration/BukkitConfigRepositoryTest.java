/*
 * Copyright (c) 2025 Tim Savage.
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

package com.winterhavenmc.library.messagebuilder.adapters.resources.configuration;

import com.winterhavenmc.library.messagebuilder.models.configuration.*;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mock;

import java.time.ZoneId;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class BukkitConfigRepositoryTest
{
	@Mock
	Plugin pluginMock;
	FileConfiguration configuration;


	@BeforeEach
	void setUp()
	{
		configuration = new YamlConfiguration();
		configuration.set("language", "fr-FR");
		configuration.set("locale", "fr-FR");
	}


	@Test
	void create()
	{
		// Act
		ConfigProvider<LocaleSetting> localeProvider = BukkitConfigRepository.create(pluginMock);

		// Assert
		assertNotNull(localeProvider);
	}


	@Test
	void testGet()
	{
		// Arrange
		when(pluginMock.getConfig()).thenReturn(configuration);

		ConfigProvider<LocaleSetting> localeProvider = BukkitConfigRepository.create(pluginMock);

		// Act
		Locale locale = localeProvider.get().languageTag().getLocale();

		// Assert
		assertEquals(Locale.FRANCE, locale);

		// Verify
		verify(pluginMock, atLeastOnce()).getConfig();
	}


	@Test
	void testGet_empty_config()
	{
		// Arrange
		configuration = new YamlConfiguration();
		when(pluginMock.getConfig()).thenReturn(configuration);
		ConfigRepository configRepository = BukkitConfigRepository.create(pluginMock);

		// Act
		Locale locale = configRepository.locale();

		// Assert
		assertNotNull(locale);

		// Verify
		verify(pluginMock, atLeastOnce()).getConfig();
	}


	@Test
	void testLanguageTag()
	{
		// Arrange
		when(pluginMock.getConfig()).thenReturn(configuration);

		ConfigRepository configRepository = BukkitConfigRepository.create(pluginMock);

		// Act
		LanguageTag languageTag = configRepository.languageTag();

		// Assert
		assertEquals(Locale.FRANCE, languageTag.getLocale());

		// Verify
		verify(pluginMock, atLeastOnce()).getConfig();
	}

	@Test
	void locale()
	{
		// Arrange
		when(pluginMock.getConfig()).thenReturn(configuration);
		ConfigRepository configRepository = BukkitConfigRepository.create(pluginMock);

		// Act
		Locale locale = configRepository.locale();

		// Assert
		assertEquals(Locale.FRANCE, locale);

		// Verify
		verify(pluginMock, atLeastOnce()).getConfig();
	}


	@Test
	void getZoneId_returns_configured_timezone()
	{
		// Arrange
		configuration.set("timezone", "UTC");
		when(pluginMock.getConfig()).thenReturn(configuration);
		ConfigRepository configRepository = BukkitConfigRepository.create(pluginMock);

		// Act
		ZoneId result = BukkitConfigRepository.getZoneId(pluginMock);

		// Assert
		assertEquals(ZoneId.of("UTC"), result);

		// Verify
		verify(pluginMock, atLeastOnce()).getConfig();
	}


	@Test
	void getZoneId_returns_configured_system_default_timezone_when_not_set()
	{
		// Arrange
		when(pluginMock.getConfig()).thenReturn(configuration);
		ConfigRepository configRepository = BukkitConfigRepository.create(pluginMock);

		// Act
		ZoneId result = BukkitConfigRepository.getZoneId(pluginMock);

		// Assert
		assertEquals(ZoneId.systemDefault(), result);

		// Verify
		verify(pluginMock, atLeastOnce()).getConfig();
	}


	@Test
	void getZoneId_returns_configured_timezone_when_valid()
	{
		// Arrange
		when(pluginMock.getConfig()).thenReturn(configuration);
		ConfigRepository configRepository = BukkitConfigRepository.create(pluginMock);

		// Act
		ZoneId result = configRepository.zoneId();

		// Assert
		assertEquals(ZoneId.systemDefault(), result);

		// Verify
		verify(pluginMock, atLeastOnce()).getConfig();
	}


	@Test
	void getZoneId_returns_system_default_timezone_when_configured_is_invalid()
	{
		// Arrange
		configuration.set("timezone", "invalid");
		when(pluginMock.getConfig()).thenReturn(configuration);
		ConfigRepository configRepository = BukkitConfigRepository.create(pluginMock);

		// Act
		ZoneId result = configRepository.zoneId();

		// Assert
		assertEquals(ZoneId.systemDefault(), result);

		// Verify
		verify(pluginMock, atLeastOnce()).getConfig();
	}


//	@Mock Plugin pluginMock;
//	@Mock FileConfiguration configurationMock;
//
//
//	@BeforeEach
//	void setUp()
//	{
//		when(pluginMock.getConfig()).thenReturn(configurationMock);
//	}
//
//
//	@Test
//	void returns_language_setting_when_present()
//	{
//		// Arrange
//		when(configurationMock.getString("language")).thenReturn("custom");
//
//		// Act
//		LanguageProvider provider = BukkitLanguageProvider.create(pluginMock);
//		LanguageSetting setting = provider.get();
//
//		// Assert
//		assertEquals("custom", setting.name());
//
//		// Verify
//		verify(configurationMock, atLeastOnce()).getString("language");
//	}
//
//
//	@Test
//	void falls_back_to_locale_setting_when_language_setting_missing()
//	{
//		// Arrange
//		when(configurationMock.getString("language")).thenReturn(null);
//		when(configurationMock.getString("locale")).thenReturn("fr-FR");
//
//		// Act
//		LanguageProvider provider = BukkitLanguageProvider.create(pluginMock);
//		LanguageSetting setting = provider.get();
//
//		// Assert
//		assertEquals("fr-FR", setting.name());
//
//		// Verify
//		verify(configurationMock, atLeast(2)).getString(anyString());
//	}
//
//
//	@Test
//	void falls_back_to_system_default_if_both_settings_missing()
//	{
//		// Arrange
//		when(configurationMock.getString("language")).thenReturn(null);
//		when(configurationMock.getString("locale")).thenReturn(null);
//
//		// Act
//		LanguageProvider provider = BukkitLanguageProvider.create(pluginMock);
//		LanguageSetting setting = provider.get();
//
//		// Assert
//		assertEquals("en-US", setting.name()); // The hardcoded fallback
//
//		// Verify
//		verify(configurationMock, atLeast(2)).getString(anyString());
//	}
//
//
//	@Test
//	void getName_when_language_setting_present()
//	{
//		// Arrange
//		when(configurationMock.getString("language")).thenReturn("custom");
//
//		// Act
//		LanguageProvider provider = BukkitLanguageProvider.create(pluginMock);
//
//		// Assert
//		assertEquals("custom", provider.getName());
//
//		// Verify
//		verify(configurationMock, atLeastOnce()).getString("language");
//	}

}
