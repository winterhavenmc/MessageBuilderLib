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

import com.winterhavenmc.util.messagebuilder.adapters.displayname.DisplayNameAdapter;
import com.winterhavenmc.util.messagebuilder.adapters.displayname.DisplayNameable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class AdapterRegistryTest
{
	private AdapterRegistry adapterRegistry;


	@BeforeEach
	void setUp()
    {
		adapterRegistry = new AdapterRegistry();
	}


	@Test
	void testGetAdapter_WithBuiltInAdapter_ShouldReturnInstance()
    {
		// Act
		Adapter adapter = adapterRegistry.getAdapter(DisplayNameable.class);

		// Assert
		assertNotNull(adapter);
		assertInstanceOf(DisplayNameAdapter.class, adapter);
	}


	@Test
	void testGetAdapter_CachesBuiltInAdapters()
    {
		// Act
		Adapter firstCall = adapterRegistry.getAdapter(DisplayNameable.class);
		Adapter secondCall = adapterRegistry.getAdapter(DisplayNameable.class);

		// Assert
		assertSame(firstCall, secondCall, "Adapter should be cached and reused.");
	}


	@Test
	void testGetAdapter_UnregisteredType_ShouldReturnNull()
    {
		// Act
		Adapter adapter = adapterRegistry.getAdapter(UUID.class); // Arbitrary unregistered type

		// Assert
		assertNull(adapter, "Should return null for unregistered types.");
	}


	@Test
	void testRegisterAdapter_ShouldOverrideBuiltInAdapter()
    {
		// Arrange
		Adapter mockAdapter = mock(Adapter.class);

		// Act
		adapterRegistry.registerAdapter(DisplayNameable.class, mockAdapter);
		Adapter retrievedAdapter = adapterRegistry.getAdapter(DisplayNameable.class);

		// Assert
		assertSame(mockAdapter, retrievedAdapter, "Custom adapter should override built-in adapter.");
	}


	@Test
	void testRegisterAdapter_CustomAdapter_ShouldBeRetrievable()
    {
		// Arrange
		Adapter customAdapter = mock(Adapter.class);
		Class<?> customType = String.class; // Arbitrary type

		// Act
		adapterRegistry.registerAdapter(customType, customAdapter);
		Adapter retrievedAdapter = adapterRegistry.getAdapter(customType);

		// Assert
		assertSame(customAdapter, retrievedAdapter, "Custom adapter should be retrievable.");
	}
}
