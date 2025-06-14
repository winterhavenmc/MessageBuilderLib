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

package com.winterhavenmc.library.messagebuilder.pipeline.extractor;

import com.winterhavenmc.library.messagebuilder.pipeline.adapters.Adapter;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.AdapterContextContainer;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.displayname.DisplayNameAdapter;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.location.Locatable;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.location.LocationAdapter;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.name.NameAdapter;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.name.Nameable;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.quantity.Quantifiable;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.quantity.QuantityAdapter;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.uuid.Identifiable;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.uuid.UniqueIdAdapter;
import com.winterhavenmc.library.messagebuilder.keys.MacroKey;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.displayname.DisplayNameable;
import com.winterhavenmc.library.messagebuilder.pipeline.containers.MacroStringMap;

import org.bukkit.Location;
import org.bukkit.World;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class FieldExtractorTest
{
	@Mock NameAdapter nameAdapterMock;
	@Mock DisplayNameAdapter displayNameAdapterMock;
	@Mock UniqueIdAdapter uniqueIdAdapterMock;
	@Mock LocationAdapter locationAdapterMock;
	@Mock QuantityAdapter quantityAdapter;
	@Mock World worldMock;

	@Mock Nameable nameableMock;
	@Mock DisplayNameable displayNameableMock;
	@Mock Identifiable identifiableMock;
	@Mock Locatable locatableMock;
	@Mock Quantifiable quantifiableMock;
	@Mock AdapterContextContainer adapterContextContainerMock;

	FieldExtractor extractor;
	MacroKey baseKey;


	@BeforeEach
	void setup()
	{
		extractor = new FieldExtractor(adapterContextContainerMock);
		baseKey = MacroKey.of("TEST").orElseThrow();
	}


	@Test
	void testNameAdapter()
	{
		MacroStringMap expected = new MacroStringMap();
		expected.put(baseKey, "TestName");
		expected.put(baseKey.append(Adapter.BuiltIn.NAME).orElseThrow(), "TestName_subfield");

		when(nameableMock.getName()).thenReturn("TestName");
		when(nameableMock.extractName(baseKey, adapterContextContainerMock)).thenReturn(expected);

		MacroStringMap result = extractor.extract(baseKey, nameAdapterMock, nameableMock);

		assertEquals(2, result.size());
		assertTrue(result.containsKey(baseKey));
		assertNotNull(result.get(baseKey));
		assertTrue(result.containsKey(baseKey.append(Adapter.BuiltIn.NAME).orElseThrow()));
		assertEquals("TestName", result.get(baseKey));
		assertEquals("TestName_subfield", result.get(baseKey.append(Adapter.BuiltIn.NAME).orElseThrow()));
	}


	@Test
	void testDisplayNameAdapter()
	{
		// Arrange
		MacroStringMap expected = new MacroStringMap();
		expected.put(baseKey, "FancyName");
		expected.put(baseKey.append(Adapter.BuiltIn.DISPLAY_NAME).orElseThrow(), "FancyName_subfield");
		when(displayNameableMock.getDisplayName()).thenReturn("FancyName");
		when(displayNameableMock.extractDisplayName(baseKey, adapterContextContainerMock)).thenReturn(expected);

		// Act
		MacroStringMap result = extractor.extract(baseKey, displayNameAdapterMock, displayNameableMock);

		// Assert
		assertEquals(2, result.size());
		assertNotNull(result.get(baseKey));
		assertEquals("FancyName", result.get(baseKey));
		assertTrue(result.containsKey(baseKey.append(Adapter.BuiltIn.DISPLAY_NAME).orElseThrow()));
	}


	@Test
	void testUniqueIdAdapter()
	{
		// Arrange
		UUID uuid = new UUID(42, 42);
		MacroStringMap expected = new MacroStringMap();
		expected.put(baseKey, uuid.toString());
		expected.put(baseKey.append(Adapter.BuiltIn.UUID).orElseThrow(), uuid.toString());
		when(identifiableMock.extractUid(baseKey, adapterContextContainerMock)).thenReturn(expected);

		// Act
		MacroStringMap result = extractor.extract(baseKey, uniqueIdAdapterMock, identifiableMock);

		// Assert
		assertEquals(2, result.size());
		assertNotNull(result.get(baseKey));
		assertEquals(uuid.toString(), result.get(baseKey));
		assertTrue(result.containsKey(baseKey.append(Adapter.BuiltIn.UUID).orElseThrow()));
	}


	@Test
	void testLocationAdapter()
	{
		// Arrange
		Location location = new Location(worldMock, 11, 12,13);
		MacroKey locationKey = baseKey.append("LOCATION").orElseThrow();
		MacroStringMap expected = new MacroStringMap();
		expected.put(locationKey, "test-world [11, 12, 13]");
		expected.put(locationKey.append("STRING").orElseThrow(), "test-world [11, 12, 13]");
		expected.put(locationKey.append("WORLD").orElseThrow(), "test-world");
		expected.put(locationKey.append("X").orElseThrow(), "11");
		expected.put(locationKey.append("Y").orElseThrow(), "12");
		expected.put(locationKey.append("Z").orElseThrow(), "13");
		when(locatableMock.extractLocation(baseKey, adapterContextContainerMock)).thenReturn(expected);

		// Act
		MacroStringMap result = extractor.extract(baseKey, locationAdapterMock, locatableMock);

		// Assert
		assertEquals("test-world [11, 12, 13]", result.get(locationKey));
		assertEquals("test-world [11, 12, 13]", result.get(locationKey.append("STRING").orElseThrow()));
		assertEquals("test-world", result.get(locationKey.append("WORLD").orElseThrow()));
		assertEquals("11", result.get(locationKey.append("X").orElseThrow()));
		assertEquals("12", result.get(locationKey.append("Y").orElseThrow()));
		assertEquals("13", result.get(locationKey.append("Z").orElseThrow()));
		assertTrue(result.containsKey(locationKey));
	}


	@Test
	void testLocationAdapter_location_key()
	{
		// Arrange
		Location location = new Location(worldMock, 11, 12,13);
		MacroKey locationKey = MacroKey.of("LOCATION").orElseThrow();
		MacroStringMap expected = new MacroStringMap();
		expected.put(locationKey, "test-world [11, 12, 13]");
		expected.put(locationKey.append("STRING").orElseThrow(), "test-world [11, 12, 13]");
		expected.put(locationKey.append("WORLD").orElseThrow(), "test-world");
		expected.put(locationKey.append("X").orElseThrow(), "11");
		expected.put(locationKey.append("Y").orElseThrow(), "12");
		expected.put(locationKey.append("Z").orElseThrow(), "13");
		when(locatableMock.extractLocation(locationKey, adapterContextContainerMock)).thenReturn(expected);
		when(worldMock.getName()).thenReturn("test-world");

		// Act
		MacroStringMap result = extractor.extract(locationKey, locationAdapterMock, locatableMock);

		// Assert
		assertEquals("test-world [11, 12, 13]", result.get(locationKey));
		assertEquals(worldMock.getName(), result.get(locationKey.append("WORLD").orElseThrow()));
		assertEquals(String.valueOf(location.getBlockX()), result.get(locationKey.append("X").orElseThrow()));
		assertEquals(String.valueOf(location.getBlockY()), result.get(locationKey.append("Y").orElseThrow()));
		assertEquals(String.valueOf(location.getBlockZ()), result.get(locationKey.append("Z").orElseThrow()));
		assertTrue(result.containsKey(locationKey));
	}


	@Test
	void testLocationAdapter_null_world()
	{
		// Arrange
		Location location = new Location(null, 11, 12,13);
		MacroKey locationKey = MacroKey.of("LOCATION").orElseThrow();
		MacroStringMap expected = new MacroStringMap();
		expected.put(locationKey, "- [11, 12, 13]");
		expected.put(locationKey.append("STRING").orElseThrow(), "- [11, 12, 13]");
		expected.put(locationKey.append("WORLD").orElseThrow(), "-");
		expected.put(locationKey.append("X").orElseThrow(), "11");
		expected.put(locationKey.append("Y").orElseThrow(), "12");
		expected.put(locationKey.append("Z").orElseThrow(), "13");
		when(locatableMock.extractLocation(locationKey, adapterContextContainerMock)).thenReturn(expected);

		// Act
		MacroStringMap result = extractor.extract(locationKey, locationAdapterMock, locatableMock);

		// Assert
		assertEquals("- [11, 12, 13]", result.get(locationKey));
		assertEquals(String.valueOf(location.getBlockX()), result.get(locationKey.append("X").orElseThrow()));
		assertEquals(String.valueOf(location.getBlockY()), result.get(locationKey.append("Y").orElseThrow()));
		assertEquals(String.valueOf(location.getBlockZ()), result.get(locationKey.append("Z").orElseThrow()));
		assertTrue(result.containsKey(locationKey));
	}


	@Test
	void testQuantityAdapter()
	{
		// Arrange
		MacroStringMap expected = new MacroStringMap();
		expected.put(baseKey, "42");
		expected.put(baseKey.append(Adapter.BuiltIn.QUANTITY).orElseThrow(), "42");
		when(quantifiableMock.extractQuantity(baseKey, adapterContextContainerMock)).thenReturn(expected);

		// Act
		MacroStringMap result = extractor.extract(baseKey, quantityAdapter, quantifiableMock);

		assertEquals(2, result.size());
		assertEquals("42", result.get(baseKey));
		assertTrue(result.containsKey(baseKey.append(Adapter.BuiltIn.QUANTITY).orElseThrow()));
	}


	@Test
	void testNoOpAdapter()
	{
		Adapter unknownAdapter = mock(Adapter.class);

		Object randomObject = new Object();
		MacroStringMap result = extractor.extract(baseKey, unknownAdapter, randomObject);

		assertTrue(result.isEmpty(), "Expected result to be empty for unmatched adapter type");
	}

//	@Test
//	void getLocationString_null()
//	{
//		String result = extractor.getLocationString(null);
//
//		assertEquals("???", result);
//	}
}
