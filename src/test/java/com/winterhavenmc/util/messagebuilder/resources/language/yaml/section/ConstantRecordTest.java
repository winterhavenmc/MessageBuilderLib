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

import com.winterhavenmc.util.messagebuilder.keys.RecordKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;


class ConstantRecordTest
{
	@Test
	void from_valid_section()
	{
		// Arrange
		RecordKey constantKey = RecordKey.of("SPAWN.DISPLAY_NAME").orElseThrow();

		ConfigurationSection constantEntrySection = new MemoryConfiguration();
		constantEntrySection.set("SPAWN.DISPLAY_NAME", "World Spawn");

		// Act
		ConstantRecord constantRecord = ConstantRecord.from(constantKey, constantEntrySection);

		// Assert
		assertInstanceOf(ValidConstantRecord.class, constantRecord);
	}


	@Test
	void from_null_section()
	{
		// Arrange
		RecordKey constantKey = RecordKey.of("SPAWN.DISPLAY_NAME").orElseThrow();

		// Act
		ConstantRecord constantRecord = ConstantRecord.from(constantKey, null);

		// Assert
		assertInstanceOf(InvalidConstantRecord.class, constantRecord);
	}

}
