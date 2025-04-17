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
import com.winterhavenmc.util.messagebuilder.adapters.displayname.DisplayNameable;
import com.winterhavenmc.util.messagebuilder.adapters.location.LocationAdapter;
import com.winterhavenmc.util.messagebuilder.adapters.location.Locatable;
import com.winterhavenmc.util.messagebuilder.adapters.name.NameAdapter;
import com.winterhavenmc.util.messagebuilder.adapters.name.Nameable;
import com.winterhavenmc.util.messagebuilder.adapters.quantity.QuantityAdapter;
import com.winterhavenmc.util.messagebuilder.adapters.quantity.Quantifiable;
import com.winterhavenmc.util.messagebuilder.adapters.uuid.UniqueIdAdapter;
import com.winterhavenmc.util.messagebuilder.adapters.uuid.Identifiable;
import com.winterhavenmc.util.messagebuilder.keys.MacroKey;

import org.bukkit.Location;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class FieldExtractorIntegrationTest
{
	private final FieldExtractorRegistry registry = new FieldExtractorRegistry();


	@ParameterizedTest(name = "{index}: {0}")
	@MethodSource("adapterProvider")
	void testFieldExtractionWithAdapter(
			String label,
			Adapter adapter,
			Object value,
			String baseKey,
			Adapter.BuiltIn subKey,
			Object expectedValue)
	{
		Map<MacroKey, Object> result = invokeExtractorUntyped(adapter, value, baseKey);
		MacroKey base = MacroKey.of(baseKey).orElseThrow();
		MacroKey sub = base.append(subKey).orElseThrow();

		assertEquals(expectedValue, result.get(base), "Expected value for base key " + base);
		assertEquals(expectedValue, result.get(sub), "Expected value for sub key " + sub);
	}


	/**
	 * Invokes the extractor with unchecked casting to match parameterized test inputs.
	 * The adapter and value are expected to be type-compatible.
	 */
	private Map<MacroKey, Object> invokeExtractorUntyped(Adapter adapter, Object value, String baseKeyString) {
		MacroKey baseKey = MacroKey.of(baseKeyString).orElseThrow();
		return registry.extractFields(adapter, value, baseKey);
	}


	static Stream<Arguments> adapterProvider()
	{
		UUID uuid = UUID.randomUUID();
		Location location = mock(Location.class);

		return Stream.of(
				Arguments.of("NameAdapter", new NameAdapter(), nameableMock("Steve"), "PLAYER", Adapter.BuiltIn.NAME, "Steve"),
				Arguments.of("DisplayNameAdapter", new DisplayNameAdapter(), displayNameableMock("Captain Steve"), "PLAYER", Adapter.BuiltIn.DISPLAY_NAME, "Captain Steve"),
				Arguments.of("UniqueIdAdapter", new UniqueIdAdapter(), identifiableMock(uuid), "PLAYER", Adapter.BuiltIn.UUID, uuid),
				Arguments.of("QuantityAdapter", new QuantityAdapter(), quantifiableMock(42), "ITEM", Adapter.BuiltIn.QUANTITY, 42),
				Arguments.of("LocationAdapter", new LocationAdapter(), locatableMock(location), "LOCATION", Adapter.BuiltIn.LOCATION, location)
		);
	}


	private static Nameable nameableMock(String name)
	{
		Nameable mock = mock(Nameable.class);
		when(mock.getName()).thenReturn(name);
		return mock;
	}



	private static DisplayNameable displayNameableMock(String name)
	{
		DisplayNameable mock = mock(DisplayNameable.class);
		when(mock.getDisplayName()).thenReturn(name);
		return mock;
	}



	private static Identifiable identifiableMock(UUID uuid)
	{
		Identifiable mock = mock(Identifiable.class);
		when(mock.getUniqueId()).thenReturn(uuid);
		return mock;
	}



	private static Quantifiable quantifiableMock(int qty)
	{
		Quantifiable mock = mock(Quantifiable.class);
		when(mock.getQuantity()).thenReturn(qty);
		return mock;
	}



	private static Locatable locatableMock(Location location)
	{
		Locatable mock = mock(Locatable.class);
		when(mock.getLocation()).thenReturn(location);
		return mock;
	}

}
