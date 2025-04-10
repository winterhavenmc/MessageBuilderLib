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

package com.winterhavenmc.util.messagebuilder.resources.language.yaml.section;

import com.winterhavenmc.util.messagebuilder.resources.RecordKey;
import com.winterhavenmc.util.messagebuilder.util.MockUtility;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.winterhavenmc.util.messagebuilder.messages.MessageId.ENABLED_MESSAGE;
import static org.junit.jupiter.api.Assertions.*;


class ValidMessageRecordTest
{

	Configuration configuration;
	ConfigurationSection messageSection;


	@BeforeEach
	public void setUp()
	{
		// create real configuration from resource
		configuration = MockUtility.loadConfigurationFromResource("language/en-US.yml");

		// get messages section of configuration
		messageSection = configuration.getConfigurationSection("MESSAGES");
	}


	@Test
	void constructorTest()
	{
		ValidMessageRecord validMessageRecord = new ValidMessageRecord(
				RecordKey.of(ENABLED_MESSAGE).orElseThrow(),
				true,
				"this is a test message",
				Duration.ofSeconds(11),
				"this is a test title",
				22,
				33,
				44,
				"this is a test subtitle");

		assertNotNull(validMessageRecord, "the newly created record is null.");
	}

}
