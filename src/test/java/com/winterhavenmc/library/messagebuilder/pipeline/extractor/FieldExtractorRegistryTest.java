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
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.name.NameAdapter;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.name.Nameable;
import com.winterhavenmc.library.messagebuilder.keys.MacroKey;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class FieldExtractorRegistryTest
{
	private FieldExtractorRegistry registry;
	private MacroKey baseKey;


	private <T> Map<MacroKey, Object> invokeExtractor(Adapter adapter, T value, String baseKeyString)
	{
		MacroKey baseKey = MacroKey.of(baseKeyString).orElseThrow();
		return registry.extractFields(adapter, value, baseKey);
	}


	@BeforeEach
	void setup()
	{
		registry = new FieldExtractorRegistry();
		baseKey = MacroKey.of("PLAYER").orElseThrow();
	}


	@Test
	void testNameableFieldExtraction()
	{
		NameAdapter adapter = new NameAdapter();
		Nameable nameable = mock(Nameable.class);
		when(nameable.getName()).thenReturn("Alex");

		Map<MacroKey, Object> result = invokeExtractor(adapter, nameable, "PLAYER");

		assertEquals(2, result.size());
		assertEquals("Alex", result.get(MacroKey.of("PLAYER").orElseThrow()));
		assertEquals("Alex", result.get(MacroKey.of("PLAYER.NAME").orElseThrow()));
	}


	@Test
	void testNullAdapterReturnsEmptyMap()
	{
		Map<MacroKey, Object> result = registry.extractFields(null, "value", baseKey);
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
