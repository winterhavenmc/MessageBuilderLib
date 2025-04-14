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

package com.winterhavenmc.util.messagebuilder.adapters;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;


@ExtendWith(MockitoExtension.class)
class AdapterRegistryTest
{
	@Mock Adapter<MockType> adapterMock;
	private AdapterRegistry registry;

	interface MockType { }


	@BeforeEach
	public void setup() {
		registry = new AdapterRegistry();
	}



	@Test
	public void testRegisterAndRetrieveAdapter()
	{
		registry.registerAdapter(MockType.class, () -> adapterMock);

		Adapter<?> retrieved = registry.getAdapter(MockType.class);
		assertNotNull(retrieved);
		assertSame(adapterMock, retrieved);
	}


	@Test
	public void testNoAdaptersForNull()
	{
		assertEquals(0, registry.matchingAdapters(null).count());
	}


	@Test
	public void testAdapterIsCached() {
		registry.registerAdapter(MockType.class, () -> adapterMock);

		Adapter<?> first = registry.getAdapter(MockType.class);
		Adapter<?> second = registry.getAdapter(MockType.class);
		assertSame(first, second);
	}


	@Test
	public void testMatchingAdapterByAssignableType()
	{
		registry.registerAdapter(MockType.class, () -> adapterMock);

		class Impl implements MockType { }
		Impl mockObj = new Impl();

		List<Adapter<MockType>> matching = registry.matchingAdapters((MockType) mockObj).toList();

		assertEquals(1, matching.size());
		assertSame(adapterMock, matching.getFirst());
	}


	@Test
	public void testGetAdapterReturnsNullWhenNotRegistered() {
		class UnregisteredType {}

		Adapter<UnregisteredType> adapter = registry.getAdapter(UnregisteredType.class);

		assertNull(adapter, "Adapter for unregistered type should return null");

		// Optional: call again to verify it's cached (no recompute, no exception)
		Adapter<UnregisteredType> cached = registry.getAdapter(UnregisteredType.class);

		assertNull(cached, "Cached result should also be null");
	}
}
