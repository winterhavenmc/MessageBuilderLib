/*
 * Copyright (c) 2022 Tim Savage.
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

package com.winterhavenmc.util.messagebuilder.macro.processor;

import com.winterhavenmc.util.messagebuilder.LanguageHandler;
import com.winterhavenmc.util.messagebuilder.YamlLanguageHandler;
import com.winterhavenmc.util.messagebuilder.macro.MacroObjectMap;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class LocationProcessorTest {

	@Mock Server serverMock;
	@Mock World worldMock;
	@Mock Plugin pluginMock;
	@Mock LanguageHandler languageHandler;
	Processor processor;


	@BeforeEach
	public void setUp() {
		processor = new LocationProcessor(languageHandler);
	}

	@Disabled
	@Test
	void execute() {
		String key = "SOME_LOCATION";

		when(worldMock.getName()).thenReturn("SOME_LOCATION_WORLD");
		Location location = new Location(worldMock, 80.0, 90.0, 100.0);

		MacroObjectMap macroObjectMap = new MacroObjectMap();
		macroObjectMap.put(key, location);

		ResultMap resultMap = processor.execute(macroObjectMap, key, location);
		assertTrue(resultMap.containsKey("SOME_LOCATION"));
		assertEquals("world", resultMap.get("SOME_LOCATION_WORLD"));
		assertEquals("80", resultMap.get("SOME_LOCATION_X"));
		assertEquals("90", resultMap.get("SOME_LOCATION_Y"));
		assertEquals("100", resultMap.get("SOME_LOCATION_Z"));
	}

}
