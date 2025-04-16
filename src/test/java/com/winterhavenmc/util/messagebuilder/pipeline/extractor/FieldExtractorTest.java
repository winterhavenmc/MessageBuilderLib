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

package com.winterhavenmc.util.messagebuilder.pipeline.extractor;

import com.winterhavenmc.util.messagebuilder.adapters.Adapter;
import com.winterhavenmc.util.messagebuilder.adapters.displayname.DisplayNameAdapter;
import com.winterhavenmc.util.messagebuilder.adapters.location.LocationAdapter;
import com.winterhavenmc.util.messagebuilder.adapters.name.NameAdapter;
import com.winterhavenmc.util.messagebuilder.adapters.quantity.QuantityAdapter;
import com.winterhavenmc.util.messagebuilder.adapters.uuid.UniqueIdAdapter;
import com.winterhavenmc.util.messagebuilder.recordkey.RecordKey;
import org.bukkit.Location;
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

	private FieldExtractor extractor;
	private RecordKey baseKey;


	@BeforeEach
	void setup()
	{
		extractor = new FieldExtractor();
		baseKey = RecordKey.of("TEST").orElseThrow();
	}


	@Test
	void testNameAdapter()
	{
		var nameable = mock(com.winterhavenmc.util.messagebuilder.adapters.name.Nameable.class);
		when(nameable.getName()).thenReturn("TestName");

		Map<RecordKey, Object> result = extractor.extract(nameAdapterMock, nameable, baseKey);

		assertEquals(2, result.size());
		assertTrue(result.containsKey(baseKey));
		assertTrue(result.containsKey(baseKey.append("NAME").orElseThrow()));
		assertEquals("TestName", result.get(baseKey));
	}


	@Test
	void testDisplayNameAdapter()
	{
		var displayNameable = mock(com.winterhavenmc.util.messagebuilder.adapters.displayname.DisplayNameable.class);
		when(displayNameable.getDisplayName()).thenReturn("FancyName");

		Map<RecordKey, Object> result = extractor.extract(displayNameAdapterMock, displayNameable, baseKey);

		assertEquals(2, result.size());
		assertEquals("FancyName", result.get(baseKey));
		assertTrue(result.containsKey(baseKey.append("DISPLAY_NAME").orElseThrow()));
	}


	@Test
	void testUniqueIdAdapter()
	{
		var identifiable = mock(com.winterhavenmc.util.messagebuilder.adapters.uuid.Identifiable.class);
		UUID uuid = UUID.randomUUID();
		when(identifiable.getUniqueId()).thenReturn(uuid);

		Map<RecordKey, Object> result = extractor.extract(uniqueIdAdapterMock, identifiable, baseKey);

		assertEquals(2, result.size());
		assertEquals(uuid, result.get(baseKey));
		assertTrue(result.containsKey(baseKey.append("UUID").orElseThrow()));
	}


	@Test
	void testLocationAdapter()
	{
		var locatable = mock(com.winterhavenmc.util.messagebuilder.adapters.location.Locatable.class);
		Location location = mock(Location.class);
		when(locatable.gatLocation()).thenReturn(location);

		Map<RecordKey, Object> result = extractor.extract(locationAdapterMock, locatable, baseKey);

		assertEquals(2, result.size());
		assertEquals(location, result.get(baseKey));
		assertTrue(result.containsKey(baseKey.append("LOCATION").orElseThrow()));
	}


	@Test
	void testQuantityAdapter()
	{
		var quantifiable = mock(com.winterhavenmc.util.messagebuilder.adapters.quantity.Quantifiable.class);
		when(quantifiable.getQuantity()).thenReturn(42);

		Map<RecordKey, Object> result = extractor.extract(quantityAdapter, quantifiable, baseKey);

		assertEquals(2, result.size());
		assertEquals(42, result.get(baseKey));
		assertTrue(result.containsKey(baseKey.append("QUANTITY").orElseThrow()));
	}


	@Test
	void testNoOpAdapter()
	{
		@SuppressWarnings("unchecked")
		Adapter<Object> unknownAdapter = mock(Adapter.class);

		Object randomObject = new Object();
		Map<RecordKey, Object> result = extractor.extract(unknownAdapter, randomObject, baseKey);

		assertTrue(result.isEmpty(), "Expected result to be empty for unmatched adapter type");
	}

}
