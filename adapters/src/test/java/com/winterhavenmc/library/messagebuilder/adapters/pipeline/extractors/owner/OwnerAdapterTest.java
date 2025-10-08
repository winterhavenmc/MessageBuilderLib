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

package com.winterhavenmc.library.messagebuilder.adapters.pipeline.extractors.owner;

import com.winterhavenmc.library.messagebuilder.core.pipeline.adapters.owner.Ownable;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Player;

import java.util.Optional;

import org.bukkit.entity.Tameable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class OwnerAdapterTest
{
	@Mock Player playerMock;
	@Mock Tameable tameableMock;

	class TestObject implements Ownable
	{
		@Override
		public AnimalTamer getOwner()
		{
			return playerMock;
		}
	}


	@Test @DisplayName("adapt with implementation of Ownable")
	public void adapt_with_valid_Ownable_returns_adapted_Ownable()
	{
		// Arrange
		TestObject testObject = new TestObject();

		// Act
		Optional<Ownable> adapter = new OwnerAdapter().adapt(testObject);
		Optional<AnimalTamer> owner = adapter.map(Ownable::getOwner);

		// Assert
		assertTrue(owner.isPresent());
		assertEquals(playerMock, owner.get(), "The adapted object should return the mock Player.");
	}


	@Test @DisplayName("adapt with valid Tameable")
	public void adapt_with_valid_Tameable_returns_adapted_Tameable()
	{
		// Arrange
		when(tameableMock.getOwner()).thenReturn(playerMock);

		// Act
		Optional<Ownable> adapter = new OwnerAdapter().adapt(tameableMock);
		Optional<AnimalTamer> owner = adapter.map(Ownable::getOwner);

		// Assert
		assertTrue(owner.isPresent());
		assertEquals(playerMock, owner.get(), "The adapted object should return the mock Player.");
	}


	@Test @DisplayName("adapt with null")
	public void adapt_with_null_parameter_returns_empty_optional()
	{
		// Arrange & Act
		Optional<Ownable> adapter = new OwnerAdapter().adapt(null);

		// Assert
		assertEquals(Optional.empty(), adapter, "The adapter should return an empty optional for a null parameter.");
	}
}
