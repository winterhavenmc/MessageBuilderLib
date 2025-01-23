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

import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.winterhavenmc.util.messagebuilder.util.MockUtility.loadConfigurationFromResource;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class EntityProcessorTest {

	@Mock Player playerMock;
	@Mock Entity entityMock;

	LanguageQueryHandler queryHandler;


	@BeforeEach
	void setUp() {
		Configuration configuration = loadConfigurationFromResource("language/en-US.yml");
		YamlConfigurationSupplier configurationSupplier = new YamlConfigurationSupplier(configuration);
		queryHandler = new YamlLanguageQueryHandler(configurationSupplier);
	}

	@AfterEach
	void tearDown() {
		playerMock = null;
		entityMock = null;
		queryHandler = null;
	}

	@Test
	void resolveContext() {
		// Arrange
		when(entityMock.getName()).thenReturn("Entity Name");
		String keyPath = "ENTITY";
		ContextMap contextMap = new ContextMap(playerMock);
		contextMap.put(keyPath, ContextContainer.of(entityMock, ProcessorType.ENTITY));

		MacroProcessor macroProcessor = new EntityProcessor(queryHandler);

		// Act
		ResultMap resultMap = macroProcessor.resolveContext(keyPath, contextMap, entityMock);

		// Assert
		assertTrue(resultMap.containsKey(keyPath));
		assertEquals("Entity Name", resultMap.get(keyPath));
	}

	@Test
	void resolveContext_not_entity() {
		// Arrange
		String keyPath = "ENTITY";
		ContextMap contextMap = new ContextMap(playerMock);
		contextMap.put(keyPath, ContextContainer.of("string", ProcessorType.ENTITY));

		MacroProcessor macroProcessor = new EntityProcessor(queryHandler);

		// Act
		ResultMap resultMap = macroProcessor.resolveContext(keyPath, contextMap, "string");

		// Assert
		assertFalse(resultMap.containsKey(keyPath));
	}

}
