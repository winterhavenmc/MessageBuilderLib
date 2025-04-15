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

package com.winterhavenmc.util.messagebuilder.pipeline.processors;

import com.winterhavenmc.util.messagebuilder.pipeline.result.ResultMap;
import com.winterhavenmc.util.messagebuilder.recipient.InvalidRecipient;
import com.winterhavenmc.util.messagebuilder.recipient.RecipientResult;
import com.winterhavenmc.util.messagebuilder.recipient.ValidRecipient;
import com.winterhavenmc.util.messagebuilder.messages.MessageId;
import com.winterhavenmc.util.messagebuilder.pipeline.context.ContextMap;
import com.winterhavenmc.util.messagebuilder.recordkey.RecordKey;

import com.winterhavenmc.util.messagebuilder.validation.ValidationException;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.util.Locale;

import static com.winterhavenmc.util.messagebuilder.validation.ErrorMessageKey.PARAMETER_INVALID;
import static com.winterhavenmc.util.messagebuilder.validation.Parameter.RECIPIENT;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class DurationProcessorTest
{
	@Mock Player playerMock;
	@Mock ConsoleCommandSender consoleMock;

	ValidRecipient recipient;
	RecordKey messageKey;


	@BeforeEach
	void setUp()
	{
		recipient = switch (RecipientResult.from(playerMock)) {
			case ValidRecipient validRecipient -> validRecipient;
			case InvalidRecipient ignored -> throw new ValidationException(PARAMETER_INVALID, RECIPIENT);
		};
		messageKey = RecordKey.of(MessageId.ENABLED_MESSAGE).orElseThrow();
	}

	@AfterEach
	void tearDown()
	{
		playerMock = null;
		consoleMock = null;
	}


	@Test @DisplayName("resolveContext_with_player with Valid parameters")
	void resolveContext()
	{
		// Arrange
		when(playerMock.getLocale()).thenReturn("en-US");
		ContextMap contextMap = ContextMap.of(recipient, messageKey).orElseThrow();
		Duration durationObject = Duration.ofMillis(12300);
		contextMap.putIfAbsent(messageKey, durationObject);
		MacroProcessor macroProcessor = new DurationProcessor();

		// Act
		ResultMap resultMap = macroProcessor.resolveContext(messageKey, contextMap);

		// Assert
		assertTrue(resultMap.containsKey(messageKey));
		assertEquals("12 seconds", resultMap.get(messageKey));
	}


	@Test @DisplayName("resolveContext_with_player with console as recipient")
	void resolveContext_console()
	{
		// Arrange
		when(playerMock.getLocale()).thenReturn("en-US");
		ContextMap contextMap = ContextMap.of(recipient, messageKey).orElseThrow();
		Duration durationObject = Duration.ofMillis(12300);
		contextMap.putIfAbsent(messageKey, durationObject);
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
		ResultMap resultMap = macroProcessor.resolveContext(messageKey, contextMap);

		// Assert
		assertTrue(resultMap.containsKey(messageKey));
		assertEquals("12 seconds", resultMap.get(messageKey));
	}


	@Test @DisplayName("resolveContext_with_player with wrong type for duration in map")
	void resolveContext_value_not_duration()
	{
		// Arrange
		when(playerMock.getLocale()).thenReturn("en-US");
		ContextMap contextMap = ContextMap.of(recipient, messageKey).orElseThrow();
		Object object = "a string";
		contextMap.putIfAbsent(messageKey, object);
		MacroProcessor macroProcessor = new DurationProcessor();

		// Act
		ResultMap resultMap = macroProcessor.resolveContext(messageKey, contextMap);

		// Assert
		assertFalse(resultMap.containsKey(messageKey));
	}

}
