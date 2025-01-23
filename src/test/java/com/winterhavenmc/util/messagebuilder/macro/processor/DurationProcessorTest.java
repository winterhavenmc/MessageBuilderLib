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

package com.winterhavenmc.util.messagebuilder.macro.processor;

import com.winterhavenmc.util.messagebuilder.context.ContextContainer;
import com.winterhavenmc.util.messagebuilder.context.ContextMap;
import com.winterhavenmc.util.messagebuilder.context.Source;
import com.winterhavenmc.util.messagebuilder.context.SourceKey;
import com.winterhavenmc.util.messagebuilder.resources.language.LanguageQueryHandler;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.YamlConfigurationSupplier;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.YamlLanguageQueryHandler;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.util.Locale;

import static com.winterhavenmc.util.messagebuilder.util.MockUtility.loadConfigurationFromResource;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DurationProcessorTest {

	@Mock Player playerMock;
	@Mock ConsoleCommandSender consoleMock;

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
		queryHandler = null;
	}

	@Test
	void resolveContext() {
		// Arrange
		when(playerMock.getLocale()).thenReturn("en-US");

		String keyPath = "DURATION";
		ContextMap contextMap = new ContextMap(playerMock);
		Duration durationObject = Duration.ofMillis(12300);
		contextMap.put(keyPath, ContextContainer.of(durationObject, ProcessorType.DURATION));
		MacroProcessor macroProcessor = new DurationProcessor(queryHandler);

		// Act
		ResultMap resultMap = macroProcessor.resolveContext(keyPath, contextMap, durationObject);

		// Assert
		assertTrue(resultMap.containsKey(keyPath));
		assertEquals("12 seconds", resultMap.get(keyPath));
	}


	@Test
	void resolveContext_console() {
		// Arrange
		String keyPath = "DURATION";
		ContextMap contextMap = new ContextMap(consoleMock);
		Duration durationObject = Duration.ofMillis(12300);
		contextMap.put(keyPath, ContextContainer.of(durationObject, ProcessorType.DURATION));
		MacroProcessor macroProcessor = new DurationProcessor(queryHandler);

		// Act
		ResultMap resultMap = macroProcessor.resolveContext(keyPath, contextMap, durationObject);

		// Assert
		assertTrue(resultMap.containsKey(keyPath));
		assertEquals("12 seconds", resultMap.get(keyPath));
	}


	@Test
	void resolveContext_value_not_duration() {
		// Arrange
		String keyPath = "DURATION";
		ContextMap contextMap = new ContextMap(consoleMock);
		Object object = "a string";
		contextMap.put(keyPath, ContextContainer.of(object, ProcessorType.DURATION));
		MacroProcessor macroProcessor = new DurationProcessor(queryHandler);

		// Act
		ResultMap resultMap = macroProcessor.resolveContext(keyPath, contextMap, object);

		// Assert
		assertFalse(resultMap.containsKey(keyPath));
	}

}
