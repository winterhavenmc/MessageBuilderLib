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

package com.winterhavenmc.util.messagebuilder.util;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class LocaleSupplierTest
{
	@Mock Plugin pluginMock;
	FileConfiguration configuration;


	@BeforeEach
	void setUp()
	{
		configuration = new YamlConfiguration();
		configuration.set("language", "fr-FR");
	}


	@Test
	void create()
	{
		// Arrange
		when(pluginMock.getConfig()).thenReturn(configuration);

		// Act
		LocaleSupplier localeSupplier = LocaleSupplier.create(pluginMock);

		// Assert
		assertNotNull(localeSupplier);

		// Verify
		verify(pluginMock, atLeastOnce()).getConfig();
	}


	@Test
	void testGet()
	{
		// Arrange
		when(pluginMock.getConfig()).thenReturn(configuration);
		LocaleSupplier localeSupplier = LocaleSupplier.create(pluginMock);

		// Act
		Locale locale = localeSupplier.get();

		// Assert
		assertEquals(Locale.FRANCE, locale);

		// Verify
		verify(pluginMock, atLeastOnce()).getConfig();
	}


	@Test
	void testGet_null_locale()
	{
		// Arrange
		configuration = new YamlConfiguration();
		when(pluginMock.getConfig()).thenReturn(configuration);
		LocaleSupplier localeSupplier = LocaleSupplier.create(pluginMock);

		// Act
		Locale locale = localeSupplier.get();

		// Assert
		assertEquals(Locale.getDefault(), locale);

		// Verify
		verify(pluginMock, atLeastOnce()).getConfig();
	}

}
