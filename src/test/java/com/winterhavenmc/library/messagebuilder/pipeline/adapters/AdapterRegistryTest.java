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

package com.winterhavenmc.library.messagebuilder.pipeline.adapters;

import com.winterhavenmc.library.messagebuilder.pipeline.adapters.displayname.DisplayNameAdapter;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.displayname.DisplayNameable;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.location.Locatable;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.name.NameAdapter;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.name.Nameable;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.quantity.Quantifiable;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.uuid.Identifiable;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.uuid.UniqueIdAdapter;

import com.winterhavenmc.library.messagebuilder.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;


@ExtendWith(MockitoExtension.class)
class AdapterRegistryTest
{

	@Mock
	AdapterContextContainer adapterContextContainerMock;
	private AdapterRegistry registry;


	@BeforeEach
	void setUp()
	{
		registry = new AdapterRegistry(adapterContextContainerMock);
	}

//	@Test
//	void testBuiltInAdaptersAreRegistered()
//	{
//		assertInstanceOf(NameAdapter.class, registry.getAdapter(Nameable.class));
//		assertInstanceOf(DisplayNameAdapter.class, registry.getAdapter(DisplayNameable.class));
//		assertNotNull(registry.getAdapter(Locatable.class));
//		assertNotNull(registry.getAdapter(Quantifiable.class));
//		assertInstanceOf(UniqueIdAdapter.class, registry.getAdapter(Identifiable.class));
//	}

//	@Test
//	void testGetAdapterReturnsSameInstance()
//	{
//		Adapter first = registry.getAdapter(Nameable.class);
//		Adapter second = registry.getAdapter(Nameable.class);
//		assertSame(first, second, "Adapters should be cached and return the same instance");
//	}

//	@Test
//	void testGetAdapterReturnsNullForUnknownType()
//	{
//		class UnknownType
//		{
//		}
//		assertNull(registry.getAdapter(UnknownType.class));
//	}

//	@Test
//	void testRegisterAndGetCustomAdapter()
//	{
//		class CustomType implements Nameable
//		{
//			@Override
//			public String getName()
//			{
//				return "custom test type";
//			}
//		}
//		Adapter customAdapter = mock(Adapter.class);
//		registry.register(CustomType.class, () -> customAdapter);
//
//		Adapter retrieved = registry.getAdapter(CustomType.class);
//		assertSame(customAdapter, retrieved);
//	}

	@Test
	void testGetMatchingAdaptersReturnsCorrectAdapters()
	{
		class SampleEntity implements Nameable, DisplayNameable
		{
			@Override
			public String getDisplayName()
			{
				return "test display name";
			}

			@Override
			public String getName()
			{
				return "test name";
			}
		}

		SampleEntity entity = new SampleEntity();
		assertEquals(2, registry.getMatchingAdapters(entity).count(), "Should return 2 matching adapters");
	}

	@Test
	void testGetMatchingAdaptersReturnsEmptyStreamForNull()
	{
		assertTrue(registry.getMatchingAdapters(null).findAny().isEmpty());
	}

//	@Test
//	void testRegisterThrowsOnNullTypeParameter()
//	{
//		// Act
//		ValidationException exception = assertThrows(ValidationException.class,
//				() -> registry.register(null, () -> mock(Adapter.class)));
//
//		// Assert
//		assertEquals("The parameter 'type' cannot be null.", exception.getMessage());
//	}

//	@Test
//	void testRegisterThrowsOnNullParameter()
//	{
//		// Act
//		ValidationException exception = assertThrows(ValidationException.class,
//				() -> registry.register(Nameable.class, null));
//
//		// Assert
//		assertEquals("The parameter 'adapter' cannot be null.", exception.getMessage());
//	}

//	@Test
//	void testGetAdapterThrowsOnNullParameter()
//	{
//		// Act
//		ValidationException exception = assertThrows(ValidationException.class,
//				() -> registry.getAdapter(null));
//
//		// Assert
//		assertEquals("The parameter 'type' cannot be null.", exception.getMessage());
//	}
}
