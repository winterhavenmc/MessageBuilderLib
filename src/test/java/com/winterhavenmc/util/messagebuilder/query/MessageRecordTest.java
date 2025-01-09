/*
 * Copyright (c) 2024-2025 Tim Savage.
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

package com.winterhavenmc.util.messagebuilder.query;

import com.winterhavenmc.util.messagebuilder.query.domain.message.MessageRecord;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.winterhavenmc.util.messagebuilder.messages.MessageId.*;
import static org.junit.jupiter.api.Assertions.*;


class MessageRecordTest {

	private ConfigurationSection messageSection;

	@BeforeEach
	void setUp() {
		// create new yaml configuration
		FileConfiguration fileConfiguration = new YamlConfiguration();



		// get messages section of configuration
		messageSection = fileConfiguration.getConfigurationSection("MESSAGES");
	}

	@AfterEach
	void tearDown() {
		messageSection = null;
	}

	//TODO: Each test should have its own distinct test entries in the language configuration resource
	// that are only used for that test, so changes to entries will not effect other tests

	@Test
	void constructorTest() {
		MessageRecord testRecord = new MessageRecord(
				ENABLED_MESSAGE.toString(),
				true,
				"this is a test message",
				11,
				"this is a test title",
				22,
				33,
				44,
				"this is a test subtitle");
		assertNotNull(testRecord, "the newly created record is null.");
	}


}
