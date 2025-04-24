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

package com.winterhavenmc.util.messagebuilder.pipeline.adapters.quantity;

import com.winterhavenmc.util.messagebuilder.pipeline.adapters.quantity.Quantifiable;
import com.winterhavenmc.util.messagebuilder.pipeline.adapters.quantity.QuantityAdapter;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class QuantityAdapterTest
{
	@Nested
	class CollectionQuantityAdapterTests
	{
		@Test
		public void testGetQuantity_withValidCollection()
		{
			// Arrange
			Collection<String> collection = List.of("item1", "item2", "item3");

			// Act
			int quantity = -888;
			Optional<Quantifiable> resolver = new QuantityAdapter().adapt(collection);
			if (resolver.isPresent())
			{
				quantity = resolver.get().getQuantity();
			}

			// Assert
			assertEquals(3, quantity, "The resolver should return the count from the Collection.");
		}

		@Test
		public void testGetQuantity_withEmptyCollection()
		{
			// Arrange
			Collection<String> collection = Collections.emptyList();

			// Act
			int quantity = -777;
			Optional<Quantifiable> resolver = new QuantityAdapter().adapt(collection);
			if (resolver.isPresent())
			{
				quantity = resolver.get().getQuantity();
			}

			// Assert
			assertEquals(0, quantity, "The resolver should return 0 for an empty Collection.");
		}


		@Test
		public void testConstructor_withNullCollection()
		{
			// Act & Assert
			int quantity = -999;
			Collection<String> collection = null;
			Optional<Quantifiable> resolver = new QuantityAdapter().adapt(collection);
			if (resolver.isPresent())
			{
				quantity = resolver.get().getQuantity();
			}
			assertEquals(-999, quantity);
		}
	}


	@Nested
	class ItemStackQuantityAdapterTest
	{
		@Test
		public void testGetQuantity_withValidItemStack()
		{
			// Arrange
			ItemStack itemStackMock = Mockito.mock(ItemStack.class);
			Mockito.when(itemStackMock.getAmount()).thenReturn(64); // Simulate a stack of 64 items

			// Act
			int quantity = -888;
			Optional<Quantifiable> resolver = new QuantityAdapter().adapt(itemStackMock);
			if (resolver.isPresent())
			{
				quantity = resolver.get().getQuantity();
			}

			// Assert
			assertEquals(64, quantity, "The adapter should return the amount from the ItemStack.");
		}


		@Test
		public void testGetQuantity_withEmptyItemStack()
		{
			// Arrange
			ItemStack itemStackMock = Mockito.mock(ItemStack.class);
			Mockito.when(itemStackMock.getAmount()).thenReturn(0); // Simulate an empty stack

			// Act
			int quantity = -777;
			Optional<Quantifiable> resolver = new QuantityAdapter().adapt(itemStackMock);
			if (resolver.isPresent())
			{
				quantity = resolver.get().getQuantity();
			}

			// Assert
			assertEquals(0, quantity, "The adapter should return 0 for an empty ItemStack.");
		}


		@Test
		public void testConstructor_withNullItemStack()
		{
			// Act & Assert
			int quantity = -999;
			Optional<Quantifiable> resolver = new QuantityAdapter().adapt(null);
			if (resolver.isPresent())
			{
				quantity = resolver.get().getQuantity();
			}
			assertEquals(-999, quantity);
		}
	}

}
