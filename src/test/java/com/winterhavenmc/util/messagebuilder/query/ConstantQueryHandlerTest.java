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

package com.winterhavenmc.util.messagebuilder.query;

import com.winterhavenmc.util.messagebuilder.namespace.Namespace;
import com.winterhavenmc.util.messagebuilder.query.domain.constant.ConstantQueryHandler;
import com.winterhavenmc.util.messagebuilder.util.MockUtility;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ConstantQueryHandlerTest {

	ConstantQueryHandler queryHandler;
	ConfigurationSection section;

	@BeforeEach
	void setUp() {
		// get configuration from resource
		Configuration configuration = MockUtility.loadConfigurationFromResource("language/en-US.yml");
		section = configuration.getConfigurationSection(Namespace.Domain.CONSTANTS.name());

		// get real ItemRecord handler with constants section of language file
		queryHandler = new ConstantQueryHandler(section);
	}

	@AfterEach
	void tearDown() {
		queryHandler = null;
	}

	@Test
	void testNotNull() {
		assertNotNull(queryHandler);
	}

	@Test
	void testSection_null() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() ->  new ConstantQueryHandler(null));
		assertEquals("The constantSection parameter cannot be null.", exception.getMessage());
	}

	@Test
	void testSectionName_equals_domain() {
		assertEquals(Namespace.Domain.CONSTANTS.name(), section.getName());
	}

	@Test
	void getString_valid_path() {
		assertEquals(Optional.of("&aSpawn"), queryHandler.getString("SPAWN.DISPLAY_NAME"));
	}

	@Test
	void getString_invalid_path() {
		assertEquals(Optional.empty(), queryHandler.getString("INVALID_PATH"));
	}

	@Test
	void getStringList_valid_path() {
		assertEquals(List.of("item 1", "item 2", "item 3"), queryHandler.getStringList("TEST_LIST"));
	}

	@Test
	void getStringList_invalid_path() {
		assertEquals(Collections.emptyList(), queryHandler.getStringList("INVALID_PATH"));
	}

	@Test
	void getInt_valid_path() {
		assertEquals(42, queryHandler.getInt("TEST_INT"));
	}

	@Test
	void getInt_invalid_path() {
		assertEquals(0, queryHandler.getInt("INVALID_PATH"));
	}

}
