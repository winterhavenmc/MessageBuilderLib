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

package com.winterhavenmc.library.messagebuilder.adapters.pipeline.resolvers.value;

import com.winterhavenmc.library.messagebuilder.adapters.pipeline.formatters.duration.LocalizedDurationFormatter;
import com.winterhavenmc.library.messagebuilder.adapters.pipeline.formatters.number.LocaleNumberFormatter;
import com.winterhavenmc.library.messagebuilder.models.configuration.LocaleProvider;
import com.winterhavenmc.library.messagebuilder.core.context.FormatterCtx;
import com.winterhavenmc.library.messagebuilder.core.maps.MacroObjectMap;
import com.winterhavenmc.library.messagebuilder.core.maps.MacroStringMap;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.formatters.duration.BoundedDuration;
import com.winterhavenmc.library.messagebuilder.models.keys.MacroKey;
import com.winterhavenmc.library.messagebuilder.models.keys.ValidMacroKey;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class AtomicResolverTest
{
	@Mock
	LocaleProvider localeProviderMock;
	@Mock
	MacroObjectMap macroObjectMapMock;
	@Mock
	LocalizedDurationFormatter durationFormatter;

	private AtomicResolver resolver;

	private final ValidMacroKey key = MacroKey.of("TEST").isValid().orElseThrow();


	@BeforeEach
	void setUp()
	{
		LocaleNumberFormatter localeNumberFormatterMock = new LocaleNumberFormatter(localeProviderMock);
		FormatterCtx formatterContainer = new FormatterCtx(localeProviderMock, durationFormatter, localeNumberFormatterMock);
		resolver = new AtomicResolver(formatterContainer);
	}


	@Test
	void testResolve_durationFormatsUsingFormatter_one_parameter()
	{
		// Arrange
		Duration duration = Duration.ofMinutes(5);
		when(macroObjectMapMock.get(key)).thenReturn(Optional.of(duration));
		when(durationFormatter.format(any(Duration.class), any(ChronoUnit.class))).thenReturn("5 minutes");

		// Act
		MacroStringMap result = resolver.resolve(key, macroObjectMapMock);

		// Assert
		assertEquals("5 minutes", result.get(key));

		// Verify
		verify(macroObjectMapMock, atLeastOnce()).get(key);
		verify(durationFormatter, atLeastOnce()).format(any(Duration.class), any(ChronoUnit.class));
	}


	@Test
	void testResolve_durationFormatsUsingFormatter_two_parameter()
	{
		// Arrange
		Duration duration = Duration.ofMinutes(5);
		when(macroObjectMapMock.get(key)).thenReturn(Optional.of(duration));
		when(durationFormatter.format(eq(duration), any(ChronoUnit.class))).thenReturn("5 minutes");

		// Act
		MacroStringMap result = resolver.resolve(key, macroObjectMapMock);

		// Assert
		assertEquals("5 minutes", result.get(key));

		// Verify
		verify(macroObjectMapMock, atLeastOnce()).get(key);
		verify(durationFormatter, atLeastOnce()).format(eq(duration), any(ChronoUnit.class));
	}


	@Test
	void testResolve_stringIsStoredDirectly()
	{
		// Arrange
		when(macroObjectMapMock.get(key)).thenReturn(Optional.of("Hello world"));

		// Act
		MacroStringMap result = resolver.resolve(key, macroObjectMapMock);

		// Assert
		assertEquals("Hello world", result.get(key));

		// Verify
		verify(macroObjectMapMock, atLeastOnce()).get(key);
	}


	@Test
	void testResolve_numberToString()
	{
		// Arrange
		when(macroObjectMapMock.get(key)).thenReturn(Optional.of(42));
		when(localeProviderMock.getLocale()).thenReturn(Locale.US);

		// Act
		MacroStringMap result = resolver.resolve(key, macroObjectMapMock);

		// Assert
		assertEquals("42", result.get(key));

		// Verify
		verify(macroObjectMapMock, atLeastOnce()).get(key);
		verify(localeProviderMock, atLeastOnce()).getLocale();
	}


	@Test
	void testResolve_boundedDuration()
	{
		// Arrange
		BoundedDuration boundedDuration = new BoundedDuration(Duration.ofSeconds(12), ChronoUnit.SECONDS);
		when(macroObjectMapMock.get(key)).thenReturn(Optional.of(boundedDuration));
		when(durationFormatter.format(boundedDuration.duration(), boundedDuration.precision())).thenReturn("12 seconds");

		// Act
		MacroStringMap result = resolver.resolve(key, macroObjectMapMock);

		// Assert
		assertEquals("12 seconds", result.get(key));

		// Verify
		verify(macroObjectMapMock, atLeastOnce()).get(key);
	}


	@Test
	void testResolve_duration()
	{
		// Arrange
		Duration duration = Duration.ofSeconds(15);
		when(macroObjectMapMock.get(key)).thenReturn(Optional.of(duration));
		when(durationFormatter.format(duration, ChronoUnit.SECONDS)).thenReturn("15 minutes");

		// Act
		MacroStringMap result = resolver.resolve(key, macroObjectMapMock);

		// Assert
		assertEquals("15 minutes", result.get(key));

		// Verify
		verify(macroObjectMapMock, atLeastOnce()).get(key);
	}


	@Test
	void testResolve_numberToString_large_english()
	{
		// Arrange
		when(macroObjectMapMock.get(key)).thenReturn(Optional.of(420000));
		when(localeProviderMock.getLocale()).thenReturn(Locale.US);

		// Act
		MacroStringMap result = resolver.resolve(key, macroObjectMapMock);

		// Assert
		assertEquals("420,000", result.get(key));

		// Verify
		verify(macroObjectMapMock, atLeastOnce()).get(key);
		verify(localeProviderMock, atLeastOnce()).getLocale();
	}


	@Test
	void testResolve_numberToString_large_german()
	{
		// Arrange
		when(macroObjectMapMock.get(key)).thenReturn(Optional.of(420000));
		when(localeProviderMock.getLocale()).thenReturn(Locale.GERMAN);

		// Act
		MacroStringMap result = resolver.resolve(key, macroObjectMapMock);

		// Assert
		assertEquals("420.000", result.get(key));

		// Verify
		verify(macroObjectMapMock, atLeastOnce()).get(key);
		verify(localeProviderMock, atLeastOnce()).getLocale();
	}


	@Test
	void testResolve_enumToString()
	{
		// Arrange
		when(macroObjectMapMock.get(key)).thenReturn(Optional.of(Thread.State.RUNNABLE));

		// Act
		MacroStringMap result = resolver.resolve(key, macroObjectMapMock);

		// Assert
		assertEquals("RUNNABLE", result.get(key));

		// Verify
		verify(macroObjectMapMock, atLeastOnce()).get(key);
	}


	@Test
	void testResolve_uuidToString()
	{
		// Arrange
		UUID uuid = UUID.randomUUID();
		when(macroObjectMapMock.get(key)).thenReturn(Optional.of(uuid));

		// Act
		MacroStringMap result = resolver.resolve(key, macroObjectMapMock);

		// Assert
		assertEquals(uuid.toString(), result.get(key));

		// Verify
		verify(macroObjectMapMock, atLeastOnce()).get(key);
	}


	@Test
	void testResolve_booleanToString()
	{
		// Arrange
		when(macroObjectMapMock.get(key)).thenReturn(Optional.of(true));

		// Act
		MacroStringMap result = resolver.resolve(key, macroObjectMapMock);

		// Assert
		assertEquals("true", result.get(key));

		// Verify
		verify(macroObjectMapMock, atLeastOnce()).get(key);
	}


	@Test
	void testResolve_unknownObjectUsesToString()
	{
		// Arrange
		Object custom = new Object()
		{
			@Override
			public String toString()
			{
				return "custom-toString";
			}
		};
		when(macroObjectMapMock.get(key)).thenReturn(Optional.of(custom));

		// Act
		MacroStringMap result = resolver.resolve(key, macroObjectMapMock);

		// Assert
		assertEquals("custom-toString", result.get(key));

		// Verify
		verify(macroObjectMapMock, atLeastOnce()).get(key);
	}


	@Test
	void testResolve_keyNotPresent_returnsEmptyMap()
	{
		// Arrange
		when(macroObjectMapMock.get(key)).thenReturn(Optional.empty());

		// Act
		MacroStringMap result = resolver.resolve(key, macroObjectMapMock);

		// Assert
		assertTrue(result.isEmpty());

		// Verify
		verify(macroObjectMapMock, atLeastOnce()).get(key);
	}

}
