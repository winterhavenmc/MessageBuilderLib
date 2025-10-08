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

import com.winterhavenmc.library.messagebuilder.adapters.pipeline.extractors.MacroFieldExtractor;

import com.winterhavenmc.library.messagebuilder.core.maps.MacroObjectMap;
import com.winterhavenmc.library.messagebuilder.core.maps.MacroStringMap;
import com.winterhavenmc.library.messagebuilder.core.pipeline.adapters.AdapterRegistry;
import com.winterhavenmc.library.messagebuilder.adapters.pipeline.extractors.name.NameAdapter;
import com.winterhavenmc.library.messagebuilder.core.pipeline.adapters.name.Nameable;

import com.winterhavenmc.library.messagebuilder.models.keys.MacroKey;
import com.winterhavenmc.library.messagebuilder.models.keys.ValidMacroKey;

import org.bukkit.entity.Player;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class CompositeResolverTest
{
	@Mock Player playerMock;
	@Mock NameAdapter adapterMock;
	@Mock AdapterRegistry adapterRegistryMock;
	@Mock MacroFieldExtractor fieldExtractorMock;
	@Mock MacroObjectMap macroObjectMapMock;
	@Mock Nameable nameableMock;

	private CompositeResolver resolver;

	private final ValidMacroKey rootKey = MacroKey.of("ROOT").isValid().orElseThrow();
	private final ValidMacroKey childKey = MacroKey.of("CHILD").isValid().orElseThrow();


	@BeforeEach
	void setUp()
	{
		resolver = new CompositeResolver(adapterRegistryMock, fieldExtractorMock);
	}


	@Test
	void testResolve_with_nested_key()
	{
		ValidMacroKey macroKey = MacroKey.of("RESOLVED.CHILD").isValid().orElseThrow();

		Object rootValue = new Object();
		Object adaptedValue = new Object();

		MacroStringMap childMap = new MacroStringMap();
		childMap.put(macroKey, "value");

		CompositeResolver spyResolver = spy(resolver);

		when(macroObjectMapMock.get(rootKey)).thenReturn(Optional.of(rootValue));
		when(adapterRegistryMock.getMatchingAdapters(rootValue)).thenReturn(Stream.of(adapterMock));

		when(adapterMock.adapt(rootValue)).thenReturn((Optional) Optional.of(adaptedValue));

		MacroObjectMap objectMap = new MacroObjectMap();

		objectMap.put(childKey, new Object());
		when(fieldExtractorMock.extract(rootKey, adapterMock, adaptedValue)).thenReturn(childMap);

		MacroStringMap resultMap = spyResolver.resolve(rootKey, macroObjectMapMock);

		assertFalse(resultMap.isEmpty());
		assertEquals("value", resultMap.get(macroKey));
	}


	@Test
	void testResolve_with_missing_macro_key()
	{
		// Arrange
		when(macroObjectMapMock.get(rootKey)).thenReturn(Optional.empty());

		// Act
		MacroStringMap resultMap = resolver.resolve(rootKey, macroObjectMapMock);

		// Assert
		assertTrue(resultMap.isEmpty());
	}


	@Test
	void testResolve_with_no_adapter()
	{
		// Arrange
		when(macroObjectMapMock.get(rootKey)).thenReturn(Optional.of("test"));
		when(adapterRegistryMock.getMatchingAdapters("test")).thenReturn(Stream.empty());

		// Act
		MacroStringMap resultMap = resolver.resolve(rootKey, macroObjectMapMock);

		// Assert
		assertTrue(resultMap.isEmpty());
	}


	@Test
	void testResolve_with_unadaptable_value()
	{
		// Arrange
		when(macroObjectMapMock.get(rootKey)).thenReturn(Optional.of("test"));
		when(adapterRegistryMock.getMatchingAdapters("test")).thenReturn(Stream.of(adapterMock));
		when(adapterMock.adapt("test")).thenReturn(Optional.empty());

		// Act
		MacroStringMap resultMap = resolver.resolve(rootKey, macroObjectMapMock);

		// Assert
		assertTrue(resultMap.isEmpty());
	}

}
