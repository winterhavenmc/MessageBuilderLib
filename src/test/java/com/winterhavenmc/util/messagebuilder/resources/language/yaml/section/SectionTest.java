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

package com.winterhavenmc.util.messagebuilder.resources.language.yaml.section;

import com.winterhavenmc.util.messagebuilder.model.language.Section;
import com.winterhavenmc.util.messagebuilder.model.language.SectionRecord;
import com.winterhavenmc.util.messagebuilder.resources.QueryHandler;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.YamlConfigurationSupplier;
import com.winterhavenmc.util.messagebuilder.util.MockUtility;

import org.bukkit.configuration.Configuration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.*;

class SectionTest {


	@ParameterizedTest
	@EnumSource
	void getQueryHandler(Section section)
	{
		// Arrange
		Configuration configuration = MockUtility.loadConfigurationFromResource("language/en-US.yml");
		YamlConfigurationSupplier configurationSupplier = new YamlConfigurationSupplier(configuration);

		// Act
		QueryHandler<SectionRecord> queryHandler = section.createHandler(configurationSupplier);

		// Assert
		assertNotNull(queryHandler);
	}


	@Test
	void values()
	{
		Section[] values = Section.values();
		assertEquals(Section.CONSTANTS, values[0]);
		assertEquals(Section.ITEMS, values[1]);
		assertEquals(Section.MESSAGES, values[2]);
	}


	@Test
	void valueOf()
	{
		assertEquals(Section.CONSTANTS, Section.valueOf("CONSTANTS"));
		assertEquals(Section.ITEMS, Section.valueOf("ITEMS"));
		assertEquals(Section.MESSAGES, Section.valueOf("MESSAGES"));
	}

}
