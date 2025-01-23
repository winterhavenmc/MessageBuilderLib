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

import com.winterhavenmc.util.messagebuilder.context.ContextContainer;
import com.winterhavenmc.util.messagebuilder.context.ContextMap;
import com.winterhavenmc.util.messagebuilder.resources.language.LanguageQueryHandler;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.YamlConfigurationSupplier;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.YamlLanguageQueryHandler;

import org.bukkit.World;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.winterhavenmc.util.messagebuilder.util.MockUtility.loadConfigurationFromResource;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class WorldProcessorTest {

	@Mock Player playerMock;
	@Mock World worldMock;
	@Mock YamlLanguageQueryHandler languageQueryHandlerMock;

	LanguageQueryHandler languageQueryHandler;


	@BeforeEach
	public void setUp() {
		Configuration configuration = loadConfigurationFromResource("language/en-US.yml");
		YamlConfigurationSupplier configurationSupplier = new YamlConfigurationSupplier(configuration);
		languageQueryHandler = new YamlLanguageQueryHandler(configurationSupplier);
	}

	@AfterEach
	public void tearDown() {
		playerMock = null;
		worldMock = null;
		languageQueryHandlerMock = null;
		languageQueryHandler = null;
	}


	@Test
	void resolveContext() {
		// Arrange
		when(worldMock.getName()).thenReturn("test_world");

		String keyPath = "SOME_WORLD";
		ContextMap contextMap = new ContextMap(playerMock);
		MacroProcessor macroProcessor = new WorldProcessor(languageQueryHandlerMock);
		contextMap.put(keyPath, ContextContainer.of(worldMock, ProcessorType.WORLD));

		// Act
		ResultMap resultMap = macroProcessor.resolveContext(keyPath, contextMap, worldMock);

		// Assert
		assertTrue(resultMap.containsKey(keyPath));
		assertEquals("test_world", resultMap.get(keyPath));
	}


	@Test
	void resolveContext_with_null_world() {
		String keyPath = "SOME_WORLD";
		ContextMap contextMap = new ContextMap(playerMock);
		MacroProcessor macroProcessor = new WorldProcessor(languageQueryHandlerMock);
		contextMap.put(keyPath, ContextContainer.of(worldMock, ProcessorType.WORLD));
		ResultMap resultMap = macroProcessor.resolveContext(keyPath, contextMap, null);

		assertFalse(resultMap.containsKey(keyPath));
		assertTrue(resultMap.isEmpty());
	}

}
