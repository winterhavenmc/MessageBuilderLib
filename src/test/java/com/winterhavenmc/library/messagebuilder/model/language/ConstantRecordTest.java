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

package com.winterhavenmc.library.messagebuilder.model.language;

import com.winterhavenmc.library.messagebuilder.keys.ConstantKey;
import com.winterhavenmc.library.messagebuilder.keys.ValidConstantKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;


class ConstantRecordTest
{
	@Test
	void of_with_valid_section()
	{
		// Arrange
		ValidConstantKey constantKey = ConstantKey.of("SPAWN.DISPLAY_NAME").isValid().orElseThrow();

		ConfigurationSection constantEntrySection = new MemoryConfiguration();
		constantEntrySection.set("SPAWN.DISPLAY_NAME", "World Spawn");

		// Act
		ConstantRecord constantRecord = ConstantRecord.from(constantKey, constantEntrySection);

		// Assert
		assertInstanceOf(ValidConstantRecord.class, constantRecord);
	}


	@Test
	void of_with_null_section()
	{
		// Arrange
		ValidConstantKey constantKey = ConstantKey.of("SPAWN.DISPLAY_NAME").isValid().orElseThrow();

		// Act
		ConstantRecord constantRecord = ConstantRecord.from(constantKey, null);

		// Assert
		assertInstanceOf(InvalidConstantRecord.class, constantRecord);
	}

}
