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

package com.winterhavenmc.util.messagebuilder.pipeline.processor;

import com.winterhavenmc.util.messagebuilder.pipeline.ContextMap;
import com.winterhavenmc.util.messagebuilder.messages.MessageId;

import com.winterhavenmc.util.messagebuilder.pipeline.processors.DurationProcessor;
import com.winterhavenmc.util.messagebuilder.pipeline.processors.MacroProcessor;
import com.winterhavenmc.util.messagebuilder.pipeline.processors.ResultMap;
import com.winterhavenmc.util.messagebuilder.validation.ValidationException;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class DurationProcessorTest {

	@Mock Player playerMock;
	@Mock ConsoleCommandSender consoleMock;


	@AfterEach
	void tearDown() {
		playerMock = null;
		consoleMock = null;
	}


	@Test
	void testResolveContext_parameter_null_key() {
		ContextMap contextMap = new ContextMap(playerMock, MessageId.ENABLED_MESSAGE.name());
		MacroProcessor macroProcessor = new DurationProcessor();
		ValidationException exception = assertThrows(ValidationException.class,
				() -> macroProcessor.resolveContext(null, contextMap));

		assertEquals("The parameter 'key' cannot be null.", exception.getMessage());
	}


	@Test
	void testResolveContext_parameter_empty_key() {
		ContextMap contextMap = new ContextMap(playerMock, MessageId.ENABLED_MESSAGE.name());
		MacroProcessor macroProcessor = new DurationProcessor();
		ValidationException exception = assertThrows(ValidationException.class,
				() -> macroProcessor.resolveContext("", contextMap));

		assertEquals("The parameter 'key' cannot be empty.", exception.getMessage());
	}


	@Test
	void testResolveContext_parameter_null_context_map() {
		MacroProcessor macroProcessor = new DurationProcessor();
		ValidationException exception = assertThrows(ValidationException.class,
				() -> macroProcessor.resolveContext("KEY", null));

		assertEquals("The parameter 'contextMap' cannot be null.", exception.getMessage());
	}


	@Test
	void resolveContext() {
		// Arrange
		when(playerMock.getLocale()).thenReturn("en-US");

		String keyPath = "DURATION";
		ContextMap contextMap = new ContextMap(playerMock, MessageId.ENABLED_MESSAGE.name());
		Duration durationObject = Duration.ofMillis(12300);
		contextMap.put(keyPath,durationObject);
		MacroProcessor macroProcessor = new DurationProcessor();

		// Act
		ResultMap resultMap = macroProcessor.resolveContext(keyPath, contextMap);

		// Assert
		assertTrue(resultMap.containsKey(keyPath));
		assertEquals("12 seconds", resultMap.get(keyPath));
	}


	@Test
	void resolveContext_console() {
		// Arrange
		String keyPath = "DURATION";
		ContextMap contextMap = new ContextMap(consoleMock, MessageId.ENABLED_MESSAGE.name());
		Duration durationObject = Duration.ofMillis(12300);
		contextMap.put(keyPath,durationObject);
		MacroProcessor macroProcessor = new DurationProcessor();

		// Mock the static method Locale.forLanguageTag
		try (MockedStatic<Locale> mockedLocale = mockStatic(Locale.class)) {
			// Define the mock behavior
			mockedLocale.when(() -> Locale.forLanguageTag("en-US")).thenReturn(Locale.US);

			mockedLocale.when(() -> Locale.forLanguageTag("fr-FR"))
					.thenReturn(Locale.FRANCE);

			// Test the mocked behavior
			Locale enLocale = Locale.forLanguageTag("en-US");
			Locale frLocale = Locale.forLanguageTag("fr-FR");

			assertEquals("en", enLocale.getLanguage());
			assertEquals("US", enLocale.getCountry());

			assertEquals("fr", frLocale.getLanguage());
			assertEquals("FR", frLocale.getCountry());

			// Verify the method was called with the expected arguments
			mockedLocale.verify(() -> Locale.forLanguageTag("en-US"));
			mockedLocale.verify(() -> Locale.forLanguageTag("fr-FR"));

			// Define the mock behavior
			mockedLocale.when(Locale::getDefault).thenReturn(Locale.US);

			// Test the mocked behavior
			Locale defaultLocale = Locale.getDefault();

			assertEquals("en", defaultLocale.getLanguage());
			assertEquals("US", defaultLocale.getCountry());

			// Verify the method was called with the expected arguments
			mockedLocale.verify(() -> Locale.forLanguageTag("en-US"));
		}

		// Act
		ResultMap resultMap = macroProcessor.resolveContext(keyPath, contextMap);

		// Assert
		assertTrue(resultMap.containsKey(keyPath));
		assertEquals("12 seconds", resultMap.get(keyPath));
	}


	@Test
	void resolveContext_value_not_duration() {
		// Arrange
		String keyPath = "DURATION";
		ContextMap contextMap = new ContextMap(playerMock, MessageId.ENABLED_MESSAGE.name());
		Object object = "a string";
		contextMap.put(keyPath, object);
		MacroProcessor macroProcessor = new DurationProcessor();

		// Act
		ResultMap resultMap = macroProcessor.resolveContext(keyPath, contextMap);

		// Assert
		assertFalse(resultMap.containsKey(keyPath));
	}

}
