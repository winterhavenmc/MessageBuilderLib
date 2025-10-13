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

package com.winterhavenmc.library.messagebuilder.adapters.resources.language;

import com.winterhavenmc.library.messagebuilder.models.language.Section;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class YamlLanguageStringProviderTest
{
	@Mock private FileConfiguration configuration;
	@Mock private ConfigurationSection mockSection;

	private YamlLanguageSectionProvider provider;


	@BeforeEach
	void setUp()
	{
		Supplier<Configuration> configSupplier = () -> configuration;
		provider = new YamlLanguageSectionProvider(configSupplier, Section.MESSAGES.toString());
	}

	@Test
	void getConfigurationSection_returns_valid_section()
	{
		// Arrange
		when(configuration.getConfigurationSection("MESSAGES")).thenReturn(mockSection);

		// Act
		ConfigurationSection result = provider.getSection();

		// Assert
		assertSame(mockSection, result);

		// Verify
		verify(configuration).getConfigurationSection("MESSAGES");
	}

}
