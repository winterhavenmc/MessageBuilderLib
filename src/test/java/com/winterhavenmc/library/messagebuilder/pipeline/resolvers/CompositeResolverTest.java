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

package com.winterhavenmc.library.messagebuilder.pipeline.resolvers;

import com.winterhavenmc.library.messagebuilder.pipeline.adapters.AdapterRegistry;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.name.NameAdapter;
import com.winterhavenmc.library.messagebuilder.keys.MacroKey;
import com.winterhavenmc.library.messagebuilder.pipeline.containers.MacroObjectMap;
import com.winterhavenmc.library.messagebuilder.pipeline.extractor.FieldExtractor;
import com.winterhavenmc.library.messagebuilder.pipeline.containers.MacroStringMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class CompositeResolverTest
{
	@Mock NameAdapter adapterMock;


	private AdapterRegistry adapterRegistry;
	private FieldExtractor fieldExtractor;
	private CompositeResolver resolver;
	private MacroObjectMap macroObjectMap;

	private final MacroKey rootKey = MacroKey.of("ROOT").orElseThrow();
	private final MacroKey childKey = MacroKey.of("CHILD").orElseThrow();

	@BeforeEach
	void setUp() {
		adapterRegistry = mock(AdapterRegistry.class);
		fieldExtractor = mock(FieldExtractor.class);
		macroObjectMap = mock(MacroObjectMap.class);
		resolver = new CompositeResolver(adapterRegistry, fieldExtractor);
	}

	@Test
	void resolve_withNestedKey_mergesSubResults()
	{
		MacroKey macroKey = MacroKey.of("RESOLVED.CHILD").orElseThrow();

		Object rootValue = new Object();
		Object adaptedValue = new Object();

		MacroStringMap childMap = new MacroStringMap();
		childMap.put(macroKey, "value");

		// Recursive stub: resolve(childKey) returns a known map
		CompositeResolver spyResolver = spy(resolver);
		doReturn(childMap).when(spyResolver).resolve(childKey, macroObjectMap);

		when(macroObjectMap.get(rootKey)).thenReturn(Optional.of(rootValue));
		when(adapterRegistry.getMatchingAdapters(rootValue)).thenReturn(Stream.of(adapterMock));

		when(adapterMock.adapt(rootValue)).thenReturn((Optional) Optional.of(adaptedValue));

		when(fieldExtractor.extract(adapterMock, adaptedValue, rootKey)).thenReturn(Map.of(childKey, new Object()));

		MacroStringMap result = spyResolver.resolve(rootKey, macroObjectMap);

		assertFalse(result.isEmpty());
		assertEquals("value", result.get(macroKey));
	}

	@Test
	void resolve_withMissingContextKey_returnsEmptyMap() {
		when(macroObjectMap.get(rootKey)).thenReturn(Optional.empty());

		MacroStringMap result = resolver.resolve(rootKey, macroObjectMap);

		assertTrue(result.isEmpty());
	}

	@Test
	void resolve_withNoAdapters_returnsEmptyMap() {
		when(macroObjectMap.get(rootKey)).thenReturn(Optional.of("test"));
		when(adapterRegistry.getMatchingAdapters("test")).thenReturn(Stream.empty());

		MacroStringMap result = resolver.resolve(rootKey, macroObjectMap);

		assertTrue(result.isEmpty());
	}

	@Test
	void resolve_withUnadaptableValue_returnsEmptyMap()
	{
		when(macroObjectMap.get(rootKey)).thenReturn(Optional.of("test"));
		when(adapterRegistry.getMatchingAdapters("test")).thenReturn(Stream.of(adapterMock));
		when(adapterMock.adapt("test")).thenReturn(Optional.empty());

		MacroStringMap result = resolver.resolve(rootKey, macroObjectMap);

		assertTrue(result.isEmpty());
	}
}
