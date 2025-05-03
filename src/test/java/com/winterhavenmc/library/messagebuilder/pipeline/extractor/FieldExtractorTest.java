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

	private FieldExtractor extractor;
	private MacroKey baseKey;


	@BeforeEach
	void setup()
	{
		extractor = new FieldExtractor();
		baseKey = MacroKey.of("TEST").orElseThrow();
	}


	@Test
	void testNameAdapter()
	{
		when(nameableMock.getName()).thenReturn("TestName");

		MacroStringMap result = extractor.extract(nameAdapterMock, nameableMock, baseKey);

		assertEquals(2, result.size());
		assertTrue(result.containsKey(baseKey));
		assertNotNull(result.get(baseKey));
		assertTrue(result.containsKey(baseKey.append(Adapter.BuiltIn.NAME).orElseThrow()));
		assertEquals("TestName", result.get(baseKey));
	}


	@Test
	void testDisplayNameAdapter()
	{
		when(displayNameableMock.getDisplayName()).thenReturn("FancyName");

		MacroStringMap result = extractor.extract(displayNameAdapterMock, displayNameableMock, baseKey);

		assertEquals(2, result.size());
		assertNotNull(result.get(baseKey));
		assertEquals("FancyName", result.get(baseKey));
		assertTrue(result.containsKey(baseKey.append("DISPLAY_NAME").orElseThrow()));
	}


	@Test
	void testUniqueIdAdapter()
	{
		UUID uuid = UUID.randomUUID();
		when(identifiableMock.getUniqueId()).thenReturn(uuid);

		MacroStringMap result = extractor.extract(uniqueIdAdapterMock, identifiableMock, baseKey);

		assertEquals(2, result.size());
		assertNotNull(result.get(baseKey));
		assertEquals(uuid.toString(), result.get(baseKey));
		assertTrue(result.containsKey(baseKey.append(Adapter.BuiltIn.UUID).orElseThrow()));
	}


	@Test
	void testLocationAdapter()
	{
		MacroKey locationKey = (!baseKey.toString().endsWith("LOCATION"))
				? baseKey.append("LOCATION").orElseThrow()
				: baseKey;

		when(worldMock.getName()).thenReturn("test-world");
		Location location = new Location(worldMock, 11, 12,13);
		when(locatableMock.getLocation()).thenReturn(location);

		MacroStringMap result = extractor.extract(locationAdapterMock, locatableMock, baseKey);

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
		MacroKey locationKey = baseKey;

		if (!locationKey.toString().endsWith("LOCATION"))
		{
			locationKey = locationKey.append("LOCATION").orElseThrow();
		}

		Location location = new Location(null, 11, 12,13);
		when(locatableMock.getLocation()).thenReturn(location);

		MacroStringMap result = extractor.extract(locationAdapterMock, locatableMock, baseKey);

//		assertEquals(6, result.size());
		assertEquals("??? [11, 12, 13]", result.get(locationKey));
		assertEquals(String.valueOf(location.getBlockX()), result.get(locationKey.append("X").orElseThrow()));
		assertEquals(String.valueOf(location.getBlockY()), result.get(locationKey.append("Y").orElseThrow()));
		assertEquals(String.valueOf(location.getBlockZ()), result.get(locationKey.append("Z").orElseThrow()));
		assertTrue(result.containsKey(locationKey));
	}


	@Test
	void testQuantityAdapter()
	{
		when(quantifiableMock.getQuantity()).thenReturn(42);

		MacroStringMap result = extractor.extract(quantityAdapter, quantifiableMock, baseKey);

		assertEquals(2, result.size());
		assertEquals("42", result.get(baseKey));
		assertTrue(result.containsKey(baseKey.append(Adapter.BuiltIn.QUANTITY).orElseThrow()));
	}


	@Test
	void testNoOpAdapter()
	{
		Adapter unknownAdapter = mock(Adapter.class);

		Object randomObject = new Object();
		MacroStringMap result = extractor.extract(unknownAdapter, randomObject, baseKey);

		assertTrue(result.isEmpty(), "Expected result to be empty for unmatched adapter type");
	}

}
