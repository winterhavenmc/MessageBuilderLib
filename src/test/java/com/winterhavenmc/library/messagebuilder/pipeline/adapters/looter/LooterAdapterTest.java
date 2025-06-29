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

package com.winterhavenmc.library.messagebuilder.pipeline.adapters.looter;

import com.winterhavenmc.library.messagebuilder.pipeline.adapters.AdapterContextContainer;
import com.winterhavenmc.library.messagebuilder.pipeline.resolvers.worldname.WorldNameResolver;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Optional;

import org.bukkit.loot.LootContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
public class LooterAdapterTest
{
	@Mock WorldNameResolver worldNameResolverMock;
	@Mock AdapterContextContainer adapterContextContainerMock;
	@Mock Player playerMock;
	@Mock World worldMock;

	class TestObject implements Lootable
	{
		@Override
		public Entity getLooter()
		{
			return playerMock;
		}
	}


	@Test @DisplayName("adapt with valid Player")
	public void adapt_with_valid_player_returns_adapted_player()
	{
		// Arrange
		TestObject testObject = new TestObject();

		// Act
		Optional<Lootable> adapter = new LooterAdapter().adapt(testObject);
		Optional<Entity> looter = adapter.map(Lootable::getLooter);

		// Assert
		assertTrue(looter.isPresent());
		assertEquals(playerMock, looter.get(), "The adapter should return the mock Player.");
	}


	@Test @DisplayName("adapt with valid LootContext")
	public void adapt_with_valid_LootContext_returns_adapted_LootContext()
	{
		// Arrange
		Location location = new Location(worldMock, 11, 12, 13);
		LootContext lootContext = new LootContext.Builder(location).killer(playerMock).build();

		// Act
		Optional<Lootable> adapter = new LooterAdapter().adapt(lootContext);
		Optional<Entity> looter = adapter.map(Lootable::getLooter);

		// Assert
		assertTrue(looter.isPresent());
		assertEquals(playerMock, looter.get(), "The adapter should return the mock Player.");
	}


	@Test @DisplayName("adapt with null")
	public void adapt_with_null_parameter_returns_empty_optional()
	{
		// Arrange & Act
		Optional<Lootable> adapter = new LooterAdapter().adapt(null);

		// Assert
		assertEquals(Optional.empty(), adapter, "The adapter should return an empty optional for a null parameter.");
	}
}
