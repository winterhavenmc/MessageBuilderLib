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

import com.winterhavenmc.library.messagebuilder.keys.MacroKey;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.AdapterRegistry;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.name.NameAdapter;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.name.Nameable;
import com.winterhavenmc.library.messagebuilder.pipeline.containers.MacroObjectMap;
import com.winterhavenmc.library.messagebuilder.pipeline.containers.MacroStringMap;
import com.winterhavenmc.library.messagebuilder.pipeline.extractor.FieldExtractor;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
	@Mock FieldExtractor fieldExtractorMock;
	@Mock MacroObjectMap macroObjectMapMock;
	@Mock Nameable nameableMock;

	private CompositeResolver resolver;

	private final MacroKey rootKey = MacroKey.of("ROOT").orElseThrow();
	private final MacroKey childKey = MacroKey.of("CHILD").orElseThrow();


	@BeforeEach
	void setUp()
	{
		resolver = new CompositeResolver(adapterRegistryMock, fieldExtractorMock);
	}


//	@Test
//	void testResolve_with_nested_key()
//	{
//		MacroKey macroKey = MacroKey.of("RESOLVED.CHILD").orElseThrow();
//
//		Object rootValue = new Object();
//		Object adaptedValue = new Object();
//
//		MacroStringMap childMap = new MacroStringMap();
//		childMap.put(macroKey, "value");
//
//		// Recursive stub: resolve(childKey) returns a known map
//		CompositeResolver spyResolver = spy(resolver);
//		doReturn(childMap).when(spyResolver).resolve(childKey, macroObjectMapMock);
//
//		when(macroObjectMapMock.get(rootKey)).thenReturn(Optional.of(rootValue));
//		when(adapterRegistryMock.getMatchingAdapters(rootValue)).thenReturn(Stream.of(adapterMock));
//
//		when(adapterMock.adapt(rootValue)).thenReturn((Optional) Optional.of(adaptedValue));
//
//		MacroObjectMap objectMap = new MacroObjectMap();
//
//		objectMap.put(childKey, new Object());
//		when(fieldExtractorMock.extract(adapterMock, adaptedValue, rootKey)).thenReturn(childMap);
//
//		MacroStringMap resultMap = spyResolver.resolve(rootKey, macroObjectMapMock);
//
//		assertFalse(resultMap.isEmpty());
//		assertEquals("value", resultMap.get(macroKey));
//	}


//	@Test
//	void testResolve_with_nested_key()
//	{
//		MacroKey macroKey = MacroKey.of("RESOLVED.CHILD").orElseThrow();
//
//		Object rootValue = new Object();
//		Object adaptedValue = new Object();
//
//		MacroStringMap childMap = new MacroStringMap();
//		childMap.put(macroKey, "value");
//
//		// Recursive stub: resolve(childKey) returns a known map
////		CompositeResolver spyResolver = spy(resolver);
////		doReturn(childMap).when(spyResolver).resolve(childKey, macroObjectMapMock);
//
//		when(macroObjectMapMock.get(rootKey)).thenReturn(Optional.of(rootValue));
//		when(adapterRegistryMock.getMatchingAdapters(rootValue)).thenReturn(Stream.of(adapterMock));
//		when(adapterMock.adapt(rootValue)).thenReturn((Optional) Optional.of(adaptedValue));
//
//		MacroObjectMap objectMap = new MacroObjectMap();
//
//		objectMap.put(childKey, new Object());
//		when(fieldExtractorMock.extract(adapterMock, adaptedValue, rootKey)).thenReturn(childMap);
//
//		MacroStringMap resultMap = resolver.resolve(rootKey, macroObjectMapMock);
//
//		assertFalse(resultMap.isEmpty());
//		assertEquals("value", resultMap.get(macroKey));
//	}


	@Test
	@Disabled
	void testResolve_player()
	{
		// Arrange
		when(macroObjectMapMock.get(rootKey)).thenReturn(Optional.of(playerMock));
		when(adapterRegistryMock.getMatchingAdapters(playerMock)).thenReturn(Stream.of(adapterMock));
		when(adapterMock.adapt(playerMock)).thenReturn(Optional.of(nameableMock));
		when(nameableMock.getName()).thenReturn("Player_Name");

		// Act
		MacroStringMap resultMap = resolver.resolve(rootKey, macroObjectMapMock);

		// Assert
		assertFalse(resultMap.isEmpty());
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
