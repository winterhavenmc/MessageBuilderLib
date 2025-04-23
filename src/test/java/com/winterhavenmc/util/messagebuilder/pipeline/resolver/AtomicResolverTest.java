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

package com.winterhavenmc.util.messagebuilder.pipeline.resolver;

import com.winterhavenmc.util.messagebuilder.formatters.number.LocaleNumberFormatter;
import com.winterhavenmc.util.messagebuilder.keys.MacroKey;
import com.winterhavenmc.util.messagebuilder.pipeline.context.ContextMap;
import com.winterhavenmc.util.messagebuilder.pipeline.result.ResultMap;
import com.winterhavenmc.util.messagebuilder.util.LocaleSupplier;
import com.winterhavenmc.util.messagebuilder.formatters.duration.Time4jDurationFormatter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
	@Mock
	Time4jDurationFormatter durationFormatter;

	private AtomicResolver resolver;

	private final MacroKey key = MacroKey.of("TEST").orElseThrow();


	@BeforeEach
	void setUp()
	{
		LocaleNumberFormatter localeNumberFormatterMock = new LocaleNumberFormatter(localeSupplierMock);
		ResolverContextContainer resolverContextContainer = new ResolverContextContainer(durationFormatter, localeNumberFormatterMock);
		resolver = new AtomicResolver(resolverContextContainer);
	}


//	@Test
//	void testResolve_durationFormatsUsingFormatter_one_parameter()
//	{
//		Duration duration = Duration.ofMinutes(5);
//		when(contextMapMock.get(key)).thenReturn(Optional.of(duration));
////		doReturn("5 minutes").when(durationFormatter.format(any(Duration.class), any(ChronoUnit.class)));
//		when(durationFormatter.format(any(Duration.class), any(ChronoUnit.class))).thenReturn("5 minutes");
//
//		ResultMap result = resolver.resolve(key, contextMapMock);
//
//		assertEquals("5 minutes", result.get(key));
//	}


//	@Test
//	void testResolve_durationFormatsUsingFormatter_two_parameter()
//	{
//		Duration duration = Duration.ofMinutes(5);
//		when(contextMapMock.get(key)).thenReturn(Optional.of(duration));
//		when(durationFormatter.format(eq(duration), any(ChronoUnit.class))).thenReturn("5 minutes");
//
//		ResultMap result = resolver.resolve(key, contextMapMock);
//
//		assertEquals("5 minutes", result.get(key));
//	}


	@Test
	void testResolve_stringIsStoredDirectly()
	{
		when(contextMapMock.get(key)).thenReturn(Optional.of("Hello world"));

		ResultMap result = resolver.resolve(key, contextMapMock);

		assertEquals("Hello world", result.get(key));
	}


	@Test
	void testResolve_numberToString()
	{
		when(contextMapMock.get(key)).thenReturn(Optional.of(42));
		when(localeSupplierMock.get()).thenReturn(Locale.US);

		ResultMap result = resolver.resolve(key, contextMapMock);

		assertEquals("42", result.get(key));
	}


	@Test
	void testResolve_numberToString_large_english()
	{
		when(contextMapMock.get(key)).thenReturn(Optional.of(420000));
		when(localeSupplierMock.get()).thenReturn(Locale.US);

		ResultMap result = resolver.resolve(key, contextMapMock);

		assertEquals("420,000", result.get(key));
	}


	@Test
	void testResolve_numberToString_large_german()
	{
		when(contextMapMock.get(key)).thenReturn(Optional.of(420000));
		when(localeSupplierMock.get()).thenReturn(Locale.GERMAN);

		ResultMap result = resolver.resolve(key, contextMapMock);

		assertEquals("420.000", result.get(key));
	}


	@Test
	void testResolve_enumToString()
	{
		when(contextMapMock.get(key)).thenReturn(Optional.of(Thread.State.RUNNABLE));

		ResultMap result = resolver.resolve(key, contextMapMock);

		assertEquals("RUNNABLE", result.get(key));
	}


	@Test
	void testResolve_uuidToString()
	{
		UUID uuid = UUID.randomUUID();
		when(contextMapMock.get(key)).thenReturn(Optional.of(uuid));

		ResultMap result = resolver.resolve(key, contextMapMock);

		assertEquals(uuid.toString(), result.get(key));
	}


	@Test
	void testResolve_booleanToString()
	{
		when(contextMapMock.get(key)).thenReturn(Optional.of(true));

		ResultMap result = resolver.resolve(key, contextMapMock);

		assertEquals("true", result.get(key));
	}


	@Test
	void testResolve_unknownObjectUsesToString()
	{
		Object custom = new Object()
		{
			@Override
			public String toString()
			{
				return "custom-toString";
			}
		};
		when(contextMapMock.get(key)).thenReturn(Optional.of(custom));

		ResultMap result = resolver.resolve(key, contextMapMock);

		assertEquals("custom-toString", result.get(key));
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
	}

}
