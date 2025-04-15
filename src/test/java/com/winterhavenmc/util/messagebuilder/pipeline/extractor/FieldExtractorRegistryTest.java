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
import com.winterhavenmc.util.messagebuilder.adapters.name.NameAdapter;
import com.winterhavenmc.util.messagebuilder.adapters.name.Nameable;
import com.winterhavenmc.util.messagebuilder.recordkey.RecordKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class FieldExtractorRegistryTest
{
	private FieldExtractorRegistry registry;
	private RecordKey baseKey;


	private <T> Map<RecordKey, Object> invokeExtractor(Adapter<T> adapter, T value, String baseKeyString)
	{
		RecordKey baseKey = RecordKey.of(baseKeyString).orElseThrow();
		return registry.extractFields(adapter, value, baseKey);
	}


	@BeforeEach
	void setup()
	{
		registry = new FieldExtractorRegistry();
		baseKey = RecordKey.of("PLAYER").orElseThrow();
	}


	@Test
	void testNameableFieldExtraction()
	{
		NameAdapter adapter = new NameAdapter();
		Nameable nameable = mock(Nameable.class);
		when(nameable.getName()).thenReturn("Alex");

		Map<RecordKey, Object> result = invokeExtractor(adapter, nameable, "PLAYER");

		assertEquals(2, result.size());
		assertEquals("Alex", result.get(RecordKey.of("PLAYER").orElseThrow()));
		assertEquals("Alex", result.get(RecordKey.of("PLAYER.NAME").orElseThrow()));
	}


	@Test
	void testNullAdapterReturnsEmptyMap()
	{
		Map<RecordKey, Object> result = registry.extractFields(null, "value", baseKey);
		assertTrue(result.isEmpty());
	}


	@Test
	void testNullValueThrowsException()
	{
		NameAdapter adapter = new NameAdapter();
		assertThrows(NullPointerException.class, () -> registry.extractFields(adapter, null, baseKey));
	}


	@Test
	void testNullBaseKeyThrowsException()
	{
		NameAdapter adapter = new NameAdapter();
		Nameable nameable = mock(Nameable.class);
		assertThrows(NullPointerException.class, () -> registry.extractFields(adapter, nameable, null));
	}

}
