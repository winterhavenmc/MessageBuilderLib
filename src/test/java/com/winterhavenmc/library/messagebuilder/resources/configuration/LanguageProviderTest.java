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
	void returnsLanguageSettingWhenLanguageFieldPresent()
	{
		when(configurationMock.getString("language")).thenReturn("custom");

		LanguageProvider provider = LanguageProvider.create(pluginMock);
		LanguageSetting setting = provider.get();

		assertEquals("custom", setting.name());
	}


	@Test
	void fallsBackToLocaleFieldIfLanguageMissing()
	{
		when(configurationMock.getString("language")).thenReturn(null);
		when(configurationMock.getString("locale")).thenReturn("fr-FR");

		LanguageProvider provider = LanguageProvider.create(pluginMock);
		LanguageSetting setting = provider.get();

		assertEquals("fr-FR", setting.name());
	}


	@Test
	void fallsBackToDefaultIfBothFieldsMissing()
	{
		when(configurationMock.getString("language")).thenReturn(null);
		when(configurationMock.getString("locale")).thenReturn(null);

		LanguageProvider provider = LanguageProvider.create(pluginMock);
		LanguageSetting setting = provider.get();

		assertEquals("en-US", setting.name()); // The hardcoded fallback
	}


	@Test
	void test_getName()
	{
		when(configurationMock.getString("language")).thenReturn("custom");

		LanguageProvider provider = LanguageProvider.create(pluginMock);

		assertEquals("custom", provider.getName());
	}

}
