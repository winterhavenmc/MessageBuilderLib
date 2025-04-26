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

package com.winterhavenmc.util.messagebuilder.pipeline.resolvers;

import com.winterhavenmc.util.messagebuilder.pipeline.formatters.duration.BoundedDuration;
import com.winterhavenmc.util.messagebuilder.pipeline.formatters.duration.LocalizedDurationFormatter;
import com.winterhavenmc.util.messagebuilder.pipeline.formatters.number.LocaleNumberFormatter;
import com.winterhavenmc.util.messagebuilder.keys.MacroKey;
import com.winterhavenmc.util.messagebuilder.pipeline.context.ContextMap;
import com.winterhavenmc.util.messagebuilder.pipeline.result.ResultMap;
import com.winterhavenmc.util.messagebuilder.model.locale.LocaleSupplier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class AtomicResolverTest
{
	@Mock LocaleSupplier localeSupplierMock;
	@Mock ContextMap contextMapMock;
	@Mock LocalizedDurationFormatter durationFormatter;

	private AtomicResolver resolver;

	private final MacroKey key = MacroKey.of("TEST").orElseThrow();


	@BeforeEach
	void setUp()
	{
		LocaleNumberFormatter localeNumberFormatterMock = new LocaleNumberFormatter(localeSupplierMock);
		ResolverContextContainer resolverContextContainer = new ResolverContextContainer(durationFormatter, localeNumberFormatterMock);
		resolver = new AtomicResolver(resolverContextContainer);
	}


	@Test
	void testResolve_durationFormatsUsingFormatter_one_parameter()
	{
		// Arrange
		Duration duration = Duration.ofMinutes(5);
		when(contextMapMock.get(key)).thenReturn(Optional.of(duration));
		when(durationFormatter.format(any(Duration.class), any(ChronoUnit.class))).thenReturn("5 minutes");

		// Act
		ResultMap result = resolver.resolve(key, contextMapMock);

		// Assert
		assertEquals("5 minutes", result.get(key));

		// Verify
		verify(contextMapMock, atLeastOnce()).get(key);
		verify(durationFormatter, atLeastOnce()).format(any(Duration.class), any(ChronoUnit.class));
	}


	@Test
	void testResolve_durationFormatsUsingFormatter_two_parameter()
	{
		// Arrange
		Duration duration = Duration.ofMinutes(5);
		when(contextMapMock.get(key)).thenReturn(Optional.of(duration));
		when(durationFormatter.format(eq(duration), any(ChronoUnit.class))).thenReturn("5 minutes");

		// Act
		ResultMap result = resolver.resolve(key, contextMapMock);

		// Assert
		assertEquals("5 minutes", result.get(key));

		// Verify
		verify(contextMapMock, atLeastOnce()).get(key);
		verify(durationFormatter, atLeastOnce()).format(eq(duration), any(ChronoUnit.class));
	}


	@Test
	void testResolve_stringIsStoredDirectly()
	{
		// Arrange
		when(contextMapMock.get(key)).thenReturn(Optional.of("Hello world"));

		// Act
		ResultMap result = resolver.resolve(key, contextMapMock);

		// Assert
		assertEquals("Hello world", result.get(key));

		// Verify
		verify(contextMapMock, atLeastOnce()).get(key);
	}


	@Test
	void testResolve_numberToString()
	{
		// Arrange
		when(contextMapMock.get(key)).thenReturn(Optional.of(42));
		when(localeSupplierMock.get()).thenReturn(Locale.US);

		// Act
		ResultMap result = resolver.resolve(key, contextMapMock);

		// Assert
		assertEquals("42", result.get(key));

		// Verify
		verify(contextMapMock, atLeastOnce()).get(key);
		verify(localeSupplierMock, atLeastOnce()).get();
	}


	@Test
	void testResolve_boundedDuration()
	{
		// Arrange
		BoundedDuration boundedDuration = new BoundedDuration(Duration.ofSeconds(12), ChronoUnit.SECONDS);
		when(contextMapMock.get(key)).thenReturn(Optional.of(boundedDuration));
		when(durationFormatter.format(boundedDuration.duration(), boundedDuration.precision())).thenReturn("12 seconds");

		// Act
		ResultMap result = resolver.resolve(key, contextMapMock);

		// Assert
		assertEquals("12 seconds", result.get(key));

		// Verify
		verify(contextMapMock, atLeastOnce()).get(key);
	}


	@Test
	void testResolve_duration()
	{
		// Arrange
		Duration duration = Duration.ofSeconds(15);
		when(contextMapMock.get(key)).thenReturn(Optional.of(duration));
		when(durationFormatter.format(duration, ChronoUnit.SECONDS)).thenReturn("15 minutes");

		// Act
		ResultMap result = resolver.resolve(key, contextMapMock);

		// Assert
		assertEquals("15 minutes", result.get(key));

		// Verify
		verify(contextMapMock, atLeastOnce()).get(key);
	}


	@Test
	void testResolve_numberToString_large_english()
	{
		// Arrange
		when(contextMapMock.get(key)).thenReturn(Optional.of(420000));
		when(localeSupplierMock.get()).thenReturn(Locale.US);

		// Act
		ResultMap result = resolver.resolve(key, contextMapMock);

		// Assert
		assertEquals("420,000", result.get(key));

		// Verify
		verify(contextMapMock, atLeastOnce()).get(key);
		verify(localeSupplierMock, atLeastOnce()).get();
	}


	@Test
	void testResolve_numberToString_large_german()
	{
		// Arrange
		when(contextMapMock.get(key)).thenReturn(Optional.of(420000));
		when(localeSupplierMock.get()).thenReturn(Locale.GERMAN);

		// Act
		ResultMap result = resolver.resolve(key, contextMapMock);

		// Assert
		assertEquals("420.000", result.get(key));

		// Verify
		verify(contextMapMock, atLeastOnce()).get(key);
		verify(localeSupplierMock, atLeastOnce()).get();
	}


	@Test
	void testResolve_enumToString()
	{
		// Arrange
		when(contextMapMock.get(key)).thenReturn(Optional.of(Thread.State.RUNNABLE));

		// Act
		ResultMap result = resolver.resolve(key, contextMapMock);

		// Assert
		assertEquals("RUNNABLE", result.get(key));

		// Verify
		verify(contextMapMock, atLeastOnce()).get(key);
	}


	@Test
	void testResolve_uuidToString()
	{
		// Arrange
		UUID uuid = UUID.randomUUID();
		when(contextMapMock.get(key)).thenReturn(Optional.of(uuid));

		// Act
		ResultMap result = resolver.resolve(key, contextMapMock);

		// Assert
		assertEquals(uuid.toString(), result.get(key));

		// Verify
		verify(contextMapMock, atLeastOnce()).get(key);
	}


	@Test
	void testResolve_booleanToString()
	{
		// Arrange
		when(contextMapMock.get(key)).thenReturn(Optional.of(true));

		// Act
		ResultMap result = resolver.resolve(key, contextMapMock);

		// Assert
		assertEquals("true", result.get(key));

		// Verify
		verify(contextMapMock, atLeastOnce()).get(key);
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
		when(contextMapMock.get(key)).thenReturn(Optional.of(custom));

		// Act
		ResultMap result = resolver.resolve(key, contextMapMock);

		// Assert
		assertEquals("custom-toString", result.get(key));

		// Verify
		verify(contextMapMock, atLeastOnce()).get(key);
	}


	@Test
	void testResolve_keyNotPresent_returnsEmptyMap()
	{
		// Arrange
		when(contextMapMock.get(key)).thenReturn(Optional.empty());

		// Act
		ResultMap result = resolver.resolve(key, contextMapMock);

		// Assert
		assertTrue(result.isEmpty());

		// Verify
		verify(contextMapMock, atLeastOnce()).get(key);
	}

}
