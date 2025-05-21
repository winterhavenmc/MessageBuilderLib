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

package com.winterhavenmc.library.messagebuilder.resources.configuration;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class LanguageProviderTest
{
	@Mock Plugin pluginMock;
	@Mock FileConfiguration configurationMock;


	@BeforeEach
	void setUp()
	{
		when(pluginMock.getConfig()).thenReturn(configurationMock);
	}


	@Test
	void returns_language_setting_when_present()
	{
		// Arrange
		when(configurationMock.getString("language")).thenReturn("custom");

		// Act
		LanguageProvider provider = LanguageProvider.create(pluginMock);
		LanguageSetting setting = provider.get();

		// Assert
		assertEquals("custom", setting.name());

		// Verify
		verify(configurationMock, atLeastOnce()).getString("language");
	}


	@Test
	void falls_back_to_locale_setting_when_language_setting_missing()
	{
		// Arrange
		when(configurationMock.getString("language")).thenReturn(null);
		when(configurationMock.getString("locale")).thenReturn("fr-FR");

		// Act
		LanguageProvider provider = LanguageProvider.create(pluginMock);
		LanguageSetting setting = provider.get();

		// Assert
		assertEquals("fr-FR", setting.name());

		// Verify
		verify(configurationMock, atLeast(2)).getString(anyString());
	}


	@Test
	void falls_back_to_system_default_if_both_settings_missing()
	{
		// Arrange
		when(configurationMock.getString("language")).thenReturn(null);
		when(configurationMock.getString("locale")).thenReturn(null);

		// Act
		LanguageProvider provider = LanguageProvider.create(pluginMock);
		LanguageSetting setting = provider.get();

		// Assert
		assertEquals("en-US", setting.name()); // The hardcoded fallback

		// Verify
		verify(configurationMock, atLeast(2)).getString(anyString());
	}


	@Test
	void getName_when_language_setting_present()
	{
		// Arrange
		when(configurationMock.getString("language")).thenReturn("custom");

		// Act
		LanguageProvider provider = LanguageProvider.create(pluginMock);

		// Assert
		assertEquals("custom", provider.getName());

		// Verify
		verify(configurationMock, atLeastOnce()).getString("language");
	}

}
