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

import com.winterhavenmc.library.messagebuilder.models.configuration.ConfigRepository;
import com.winterhavenmc.library.messagebuilder.models.configuration.*;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mock;

import java.time.ZoneId;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
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
	void create_returns_valid_locale_provider()
	{
		// Act
		ConfigProvider<LocaleSetting> localeProvider = BukkitConfigRepository.create(pluginMock);

		// Assert
		assertNotNull(localeProvider);
	}


	@Test
	void get_returns_locale_setting()
	{
		// Arrange
		when(pluginMock.getConfig()).thenReturn(configuration);
		ConfigRepository configRepository = BukkitConfigRepository.create(pluginMock);

		LocaleSetting result = configRepository.get();

		assertInstanceOf(LocaleSetting.class, result);
	}


	@Test
	void get_returns_default_language_tag_given_empty_config()
	{
		// Arrange
		configuration = new YamlConfiguration();
		when(pluginMock.getConfig()).thenReturn(configuration);
		ConfigRepository configRepository = BukkitConfigRepository.create(pluginMock);

		// Act
		Locale locale = configRepository.locale();

		// Assert
		assertNotNull(locale);
		assertEquals(Locale.getDefault(), locale);

		// Verify
		verify(pluginMock, atLeastOnce()).getConfig();
	}


	@Nested
	class StringConfig
	{
		@Test
		void locale_returns_configured_locale()
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
		void logLocale_returns_configured_log_locale()
		{
			// Arrange
			when(pluginMock.getConfig()).thenReturn(configuration);
			ConfigRepository configRepository = BukkitConfigRepository.create(pluginMock);

			// Act
			Locale locale = configRepository.logLocale();

			// Assert
			assertEquals(Locale.FRANCE, locale);

			// Verify
			verify(pluginMock, atLeastOnce()).getConfig();
		}


		@Test
		void dateLocale_returns_configured_date_locale()
		{
			// Arrange
			when(pluginMock.getConfig()).thenReturn(configuration);
			ConfigRepository configRepository = BukkitConfigRepository.create(pluginMock);

			// Act
			Locale locale = configRepository.dateLocale();

			// Assert
			assertEquals(Locale.FRANCE, locale);

			// Verify
			verify(pluginMock, atLeastOnce()).getConfig();
		}


		@Test
		void timeLocale_returns_configured_time_locale()
		{
			// Arrange
			when(pluginMock.getConfig()).thenReturn(configuration);
			ConfigRepository configRepository = BukkitConfigRepository.create(pluginMock);

			// Act
			Locale locale = configRepository.timeLocale();

			// Assert
			assertEquals(Locale.FRANCE, locale);

			// Verify
			verify(pluginMock, atLeastOnce()).getConfig();
		}


		@Test
		void numberLocale_returns_configured_number_locale()
		{
			// Arrange
			when(pluginMock.getConfig()).thenReturn(configuration);
			ConfigRepository configRepository = BukkitConfigRepository.create(pluginMock);

			// Act
			Locale locale = configRepository.numberLocale();

			// Assert
			assertEquals(Locale.FRANCE, locale);

			// Verify
			verify(pluginMock, atLeastOnce()).getConfig();
		}
	}


	@Nested
	class MapConfig
	{
		@Test
		void locale_returns_configured_locale()
		{
			// Arrange
			configuration = new YamlConfiguration();
			configuration.set("locale.log", "fr-FR");

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
		void logLocale_returns_configured_log_locale()
		{
			// Arrange
			configuration = new YamlConfiguration();
			configuration.set("locale.log", "fr-FR");

			when(pluginMock.getConfig()).thenReturn(configuration);
			ConfigRepository configRepository = BukkitConfigRepository.create(pluginMock);

			// Act
			Locale result = configRepository.logLocale();

			// Assert
			assertEquals(Locale.FRANCE, result);

			// Verify
			verify(pluginMock, atLeastOnce()).getConfig();
		}


		@Test
		void dateLocale_returns_configured_date_locale()
		{
			// Arrange
			configuration = new YamlConfiguration();
			configuration.set("locale.date", "fr-FR");

			when(pluginMock.getConfig()).thenReturn(configuration);
			ConfigRepository configRepository = BukkitConfigRepository.create(pluginMock);

			// Act
			Locale locale = configRepository.dateLocale();

			// Assert
			assertEquals(Locale.FRANCE, locale);

			// Verify
			verify(pluginMock, atLeastOnce()).getConfig();
		}


		@Test
		void timeLocale_returns_configured_time_locale()
		{
			// Arrange
			configuration = new YamlConfiguration();
			configuration.set("locale.time", "fr-FR");

			when(pluginMock.getConfig()).thenReturn(configuration);
			ConfigRepository configRepository = BukkitConfigRepository.create(pluginMock);

			// Act
			Locale locale = configRepository.timeLocale();

			// Assert
			assertEquals(Locale.FRANCE, locale);

			// Verify
			verify(pluginMock, atLeastOnce()).getConfig();
		}


		@Test
		void numberLocale_returns_configured_number_locale()
		{
			// Arrange
			configuration = new YamlConfiguration();
			configuration.set("locale.number", "fr-FR");

			when(pluginMock.getConfig()).thenReturn(configuration);
			ConfigRepository configRepository = BukkitConfigRepository.create(pluginMock);

			// Act
			Locale locale = configRepository.numberLocale();

			// Assert
			assertEquals(Locale.FRANCE, locale);

			// Verify
			verify(pluginMock, atLeastOnce()).getConfig();
		}
	}


	@Nested
	class ZoneIdTests
	{
		@Test
		void getZoneId_returns_configured_timezone()
		{
			// Arrange
			configuration.set("timezone", "UTC");
			when(pluginMock.getConfig()).thenReturn(configuration);
			ConfigRepository configRepository = BukkitConfigRepository.create(pluginMock);

			// Act
			ZoneId result = new ZoneIdSetting(pluginMock).get();

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
			ZoneId result = new ZoneIdSetting(pluginMock).get();

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
	}


	@Nested
	class LanguageSettingTests
	{
		@Test
		void getLanguage_returns_configured_language()
		{
			// Arrange
			configuration.set("language", "language_file_name");
			when(pluginMock.getConfig()).thenReturn(configuration);
			ConfigRepository configRepository = BukkitConfigRepository.create(pluginMock);

			// Act
			String result = configRepository.language();

			// Assert
			assertEquals("language_file_name", result);

			// Verify
			verify(pluginMock, atLeastOnce()).getConfig();
		}


		@Test
		void getLanguage_returns_default_when_not_configured()
		{
			// Arrange
			configuration.set("language", null);
			when(pluginMock.getConfig()).thenReturn(configuration);
			ConfigRepository configRepository = BukkitConfigRepository.create(pluginMock);

			// Act
			String result = configRepository.language();

			// Assert
			assertEquals("en-US", result);

			// Verify
			verify(pluginMock, atLeastOnce()).getConfig();
		}
	}

	@ParameterizedTest
	@EnumSource(BukkitConfigRepository.ConfigKey.class)
	void test_enum_toString(BukkitConfigRepository.ConfigKey configKey)
	{
		assertEquals(configKey.key(), configKey.toString());
	}

}
