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
import org.bukkit.Location;
import org.bukkit.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
		var nameable = mock(Nameable.class);
		when(nameable.getName()).thenReturn("TestName");

		Map<MacroKey, Object> result = extractor.extract(nameAdapterMock, nameable, baseKey);

		assertEquals(2, result.size());
		assertTrue(result.containsKey(baseKey));
		assertTrue(result.containsKey(baseKey.append(Adapter.BuiltIn.NAME).orElseThrow()));
		assertEquals("TestName", result.get(baseKey));
	}


	@Test
	void testDisplayNameAdapter()
	{
		var displayNameable = mock(DisplayNameable.class);
		when(displayNameable.getDisplayName()).thenReturn("FancyName");

		Map<MacroKey, Object> result = extractor.extract(displayNameAdapterMock, displayNameable, baseKey);

		assertEquals(2, result.size());
		assertEquals("FancyName", result.get(baseKey));
		assertTrue(result.containsKey(baseKey.append("DISPLAY_NAME").orElseThrow()));
	}


	@Test
	void testUniqueIdAdapter()
	{
		var identifiable = mock(Identifiable.class);
		UUID uuid = UUID.randomUUID();
		when(identifiable.getUniqueId()).thenReturn(uuid);

		Map<MacroKey, Object> result = extractor.extract(uniqueIdAdapterMock, identifiable, baseKey);

		assertEquals(2, result.size());
		assertEquals(uuid, result.get(baseKey));
		assertTrue(result.containsKey(baseKey.append(Adapter.BuiltIn.UUID).orElseThrow()));
	}


	@Test
	void testLocationAdapter()
	{
		var locatable = mock(Locatable.class);

		MacroKey locationKey = baseKey;

		if (!locationKey.toString().endsWith("LOCATION"))
		{
			locationKey = locationKey.append("LOCATION").orElseThrow();
		}

		when(worldMock.getName()).thenReturn("test-world");
		Location location = new Location(worldMock, 11, 12,13);
		when(locatable.getLocation()).thenReturn(location);

		Map<MacroKey, Object> result = extractor.extract(locationAdapterMock, locatable, baseKey);

		assertEquals(6, result.size());
		assertEquals("test-world [11, 12, 13]", result.get(locationKey));
		assertEquals(location.getWorld().getName(), result.get(locationKey.append("WORLD").orElseThrow()));
		assertEquals(location.getBlockX(), result.get(locationKey.append("X").orElseThrow()));
		assertEquals(location.getBlockY(), result.get(locationKey.append("Y").orElseThrow()));
		assertEquals(location.getBlockZ(), result.get(locationKey.append("Z").orElseThrow()));
		assertTrue(result.containsKey(locationKey));
	}


	@Test
	void testLocationAdapter_null_world()
	{
		var locatable = mock(Locatable.class);

		MacroKey locationKey = baseKey;

		if (!locationKey.toString().endsWith("LOCATION"))
		{
			locationKey = locationKey.append("LOCATION").orElseThrow();
		}

		Location location = new Location(null, 11, 12,13);
		when(locatable.getLocation()).thenReturn(location);

		Map<MacroKey, Object> result = extractor.extract(locationAdapterMock, locatable, baseKey);

		assertEquals(6, result.size());
		assertEquals("??? [11, 12, 13]", result.get(locationKey));
		assertEquals(location.getBlockX(), result.get(locationKey.append("X").orElseThrow()));
		assertEquals(location.getBlockY(), result.get(locationKey.append("Y").orElseThrow()));
		assertEquals(location.getBlockZ(), result.get(locationKey.append("Z").orElseThrow()));
		assertTrue(result.containsKey(locationKey));
	}


	@Test
	void testQuantityAdapter()
	{
		var quantifiable = mock(Quantifiable.class);
		when(quantifiable.getQuantity()).thenReturn(42);

		Map<MacroKey, Object> result = extractor.extract(quantityAdapter, quantifiable, baseKey);

		assertEquals(2, result.size());
		assertEquals(42, result.get(baseKey));
		assertTrue(result.containsKey(baseKey.append(Adapter.BuiltIn.QUANTITY).orElseThrow()));
	}


	@Test
	void testNoOpAdapter()
	{
		Adapter unknownAdapter = mock(Adapter.class);

		Object randomObject = new Object();
		Map<MacroKey, Object> result = extractor.extract(unknownAdapter, randomObject, baseKey);

		assertTrue(result.isEmpty(), "Expected result to be empty for unmatched adapter type");
	}

}
