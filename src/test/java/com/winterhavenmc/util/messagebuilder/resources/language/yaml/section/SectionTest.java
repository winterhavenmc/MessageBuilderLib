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

import com.winterhavenmc.util.messagebuilder.resources.language.yaml.YamlConfigurationSupplier;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.constants.ConstantSectionQueryHandler;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.items.ItemSectionQueryHandler;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.messages.MessageSectionQueryHandler;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.time.TimeSectionQueryHandler;
import com.winterhavenmc.util.messagebuilder.util.MockUtility;
import org.bukkit.configuration.Configuration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.*;

class SectionTest {


	@Test
	void getHandlerClass() {
		assertEquals(ConstantSectionQueryHandler.class, Section.CONSTANTS.getHandlerClass());
		assertEquals(ItemSectionQueryHandler.class, Section.ITEMS.getHandlerClass());
		assertEquals(MessageSectionQueryHandler.class, Section.MESSAGES.getHandlerClass());
		assertEquals(TimeSectionQueryHandler.class, Section.TIME.getHandlerClass());
	}

	@Test
	void getSingularName() {
		assertEquals("Constant", Section.CONSTANTS.getSingularName());
		assertEquals("Item", Section.ITEMS.getSingularName());
		assertEquals("Message", Section.MESSAGES.getSingularName());
		assertEquals("Time", Section.TIME.getSingularName());
	}

	@Test
	void getPluralName() {
		assertEquals("Constants", Section.CONSTANTS.getPluralName());
		assertEquals("Items", Section.ITEMS.getPluralName());
		assertEquals("Messages", Section.MESSAGES.getPluralName());
		assertEquals("Times", Section.TIME.getPluralName());
	}

	@Test
	void getMnemonic() {
		assertEquals("CONST", Section.CONSTANTS.getMnemonic());
		assertEquals("ITEM", Section.ITEMS.getMnemonic());
		assertEquals("MSG", Section.MESSAGES.getMnemonic());
		assertEquals("TIME", Section.TIME.getMnemonic());
	}

	@ParameterizedTest
	@EnumSource
	void getQueryHandler(Section section) {
		// Arrange
		Configuration configuration = MockUtility.loadConfigurationFromResource("language/en-US.yml");
		YamlConfigurationSupplier configurationSupplier = new YamlConfigurationSupplier(configuration);

		// Act & Assert
		assertNotNull(section.getQueryHandler(configurationSupplier));
	}

	@Test
	void values() {
		Section[] values = Section.values();
		assertEquals(Section.CONSTANTS, values[0]);
		assertEquals(Section.ITEMS, values[1]);
		assertEquals(Section.MESSAGES, values[2]);
		assertEquals(Section.TIME, values[3]);
	}

	@Test
	void valueOf() {
		assertEquals(Section.CONSTANTS, Section.valueOf("CONSTANTS"));
		assertEquals(Section.ITEMS, Section.valueOf("ITEMS"));
		assertEquals(Section.MESSAGES, Section.valueOf("MESSAGES"));
		assertEquals(Section.TIME, Section.valueOf("TIME"));
	}
}