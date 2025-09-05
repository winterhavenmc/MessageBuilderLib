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

package com.winterhavenmc.library.messagebuilder.resources.language;

import com.winterhavenmc.library.messagebuilder.model.language.Section;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;

import java.util.function.Supplier;


public class LanguageSectionProvider implements SectionProvider
{
	private final Supplier<Configuration> configSupplier;
	private final Section section;


	public LanguageSectionProvider(Supplier<Configuration> configSupplier, Section section)
	{
		this.configSupplier = configSupplier;
		this.section = section;
	}


	@Override
	public ConfigurationSection getSection()
	{
		return configSupplier.get().getConfigurationSection(section.name());
	}

}
