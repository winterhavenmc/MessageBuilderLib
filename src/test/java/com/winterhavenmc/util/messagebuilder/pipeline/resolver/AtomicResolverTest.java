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

import com.winterhavenmc.util.messagebuilder.keys.MacroKey;
import com.winterhavenmc.util.messagebuilder.pipeline.context.ContextMap;
import com.winterhavenmc.util.messagebuilder.pipeline.result.ResultMap;
import com.winterhavenmc.util.messagebuilder.util.LocaleSupplier;
import com.winterhavenmc.util.messagebuilder.util.ResolverContext;
import com.winterhavenmc.util.time.PrettyTimeFormatter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
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
	@Mock ContextMap contextMap;

	private PrettyTimeFormatter timeFormatter;
	private AtomicResolver resolver;

	private final MacroKey key = MacroKey.of("TEST").orElseThrow();


	@BeforeEach
	void setUp()
	{
		timeFormatter = mock(PrettyTimeFormatter.class);
		ResolverContext resolverContext = new ResolverContext(localeSupplierMock, timeFormatter);
		resolver = new AtomicResolver(resolverContext);
	}


	@Test
	void testResolve_durationFormatsUsingFormatter()
	{
		Duration duration = Duration.ofMinutes(5);
		when(contextMap.get(key)).thenReturn(Optional.of(duration));
		when(timeFormatter.getFormatted(any(), eq(duration))).thenReturn("in 5 minutes");

		ResultMap result = resolver.resolve(key, contextMap);

		assertEquals("in 5 minutes", result.get(key));
	}


	@Test
	void testResolve_stringIsStoredDirectly()
	{
		when(contextMap.get(key)).thenReturn(Optional.of("Hello world"));

		ResultMap result = resolver.resolve(key, contextMap);

		assertEquals("Hello world", result.get(key));
	}


	@Test
	void testResolve_numberToString()
	{
		when(contextMap.get(key)).thenReturn(Optional.of(42));
		when(localeSupplierMock.get()).thenReturn(Locale.US);

		ResultMap result = resolver.resolve(key, contextMap);

		assertEquals("42", result.get(key));
	}


	@Test
	void testResolve_numberToString_large_english()
	{
		when(contextMap.get(key)).thenReturn(Optional.of(420000));
		when(localeSupplierMock.get()).thenReturn(Locale.US);

		ResultMap result = resolver.resolve(key, contextMap);

		assertEquals("420,000", result.get(key));
	}


	@Test
	void testResolve_numberToString_large_german()
	{
		when(contextMap.get(key)).thenReturn(Optional.of(420000));
		when(localeSupplierMock.get()).thenReturn(Locale.GERMAN);

		ResultMap result = resolver.resolve(key, contextMap);

		assertEquals("420.000", result.get(key));
	}


	@Test
	void testResolve_enumToString()
	{
		when(contextMap.get(key)).thenReturn(Optional.of(Thread.State.RUNNABLE));

		ResultMap result = resolver.resolve(key, contextMap);

		assertEquals("RUNNABLE", result.get(key));
	}


	@Test
	void testResolve_uuidToString()
	{
		UUID uuid = UUID.randomUUID();
		when(contextMap.get(key)).thenReturn(Optional.of(uuid));

		ResultMap result = resolver.resolve(key, contextMap);

		assertEquals(uuid.toString(), result.get(key));
	}


	@Test
	void testResolve_booleanToString()
	{
		when(contextMap.get(key)).thenReturn(Optional.of(true));

		ResultMap result = resolver.resolve(key, contextMap);

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
		when(contextMap.get(key)).thenReturn(Optional.of(custom));

		ResultMap result = resolver.resolve(key, contextMap);

		assertEquals("custom-toString", result.get(key));
	}


	@Test
	void testResolve_keyNotPresent_returnsEmptyMap()
	{
		// Arrange
		when(contextMap.get(key)).thenReturn(Optional.empty());

		// Act
		ResultMap result = resolver.resolve(key, contextMap);

		// Assert
		assertTrue(result.isEmpty());
	}

}
