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

package com.winterhavenmc.util.messagebuilder.resources.language.yaml;

import com.winterhavenmc.util.messagebuilder.model.language.Section;
import com.winterhavenmc.util.messagebuilder.resources.language.LanguageSectionProvider;
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
class LanguageSectionProviderTest
{
	@Mock
	private FileConfiguration configuration;

	@Mock
	private ConfigurationSection mockSection;

	private Supplier<Configuration> configSupplier;
	private LanguageSectionProvider provider;


	@BeforeEach
	void setUp()
	{
		configSupplier = () -> configuration;
		provider = new LanguageSectionProvider(configSupplier, Section.MESSAGES);
	}

	@Test
	void getSectionReturnsCorrectSection()
	{
		when(configuration.getConfigurationSection("MESSAGES")).thenReturn(mockSection);

		ConfigurationSection result = provider.getSection();

		assertSame(mockSection, result);
		verify(configuration).getConfigurationSection("MESSAGES");
	}
}
