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

package com.winterhavenmc.library.messagebuilder.core.pipeline.adapters.location;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Raid;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.Player;

import org.bukkit.inventory.Inventory;
import org.bukkit.loot.LootContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class LocationAdapterTest
{
	@Mock Player playerMock;
	@Mock OfflinePlayer offlinePlayerMock;
	@Mock Block blockMock;
	@Mock BlockState blockStateMock;
	@Mock DoubleChest doubleChestMock;
	@Mock World worldMock;
	@Mock Inventory inventoryMock;
	@Mock Raid raidMock;
	@Mock Chest chestMock;

	Location location;


	@BeforeEach
	void setUp()
	{
		location = new Location(worldMock, 1,2,3);
	}


	@Test
	public void testGetLocation_withValidLocation()
	{
		// Arrange & Act
		Location result = new LocationAdapter()
				.adapt(location)
				.map(Locatable::getLocation).orElseThrow();

		// Assert
		assertEquals(location, result, "The resolver should return the getLocation from the Location.");
	}


	@Test
	public void testGetLocation_withValidPlayer()
	{
		// Arrange
		when(playerMock.getLocation()).thenReturn(location);

		// Act
		Location result = new LocationAdapter()
				.adapt(playerMock)
				.map(Locatable::getLocation).orElseThrow();

		// Assert
		assertEquals(location, result, "The resolver should return the getLocation from the Player.");
	}


	@Test
	public void testGetLocation_withValidOfflinePlayer()
	{
		// Arrange
		when(offlinePlayerMock.getLocation()).thenReturn(location);

		// Act
		Location result = new LocationAdapter()
				.adapt(offlinePlayerMock)
				.map(Locatable::getLocation).orElseThrow();

		// Assert
		assertEquals(location, result, "The resolver should return the getLocation from the OfflinePlayer.");
	}


	@Test
	public void testGetLocation_withValidBlock()
	{
		// Arrange
		when(blockMock.getLocation()).thenReturn(location);

		// Act
		Location result = new LocationAdapter()
				.adapt(blockMock)
				.map(Locatable::getLocation).orElseThrow();

		// Assert
		assertEquals(location, result, "The resolver should return the getLocation from the Block.");
	}


	@Test
	public void testGetLocation_withValidBlockState()
	{
		// Arrange
		when(blockStateMock.getLocation()).thenReturn(location);

		// Act
		Location result = new LocationAdapter()
				.adapt(blockStateMock)
				.map(Locatable::getLocation).orElseThrow();

		// Assert
		assertEquals(location, result, "The resolver should return the getLocation from the BlockState.");
	}


	@Test
	public void testGetLocation_withValidChest()
	{
		// Arrange
		when(chestMock.getLocation()).thenReturn(location);

		// Act
		Location result = new LocationAdapter()
				.adapt(chestMock)
				.map(Locatable::getLocation).orElseThrow();

		// Assert
		assertEquals(location, result, "The resolver should return the getLocation from the Chest.");

		// Verify
		verify(chestMock, atLeastOnce()).getLocation();
	}


	@Test
	public void testGetLocation_withValidDoubleChest()
	{
		// Arrange
		when(doubleChestMock.getLocation()).thenReturn(location);

		// Act
		Location result = new LocationAdapter()
				.adapt(doubleChestMock)
				.map(Locatable::getLocation).orElseThrow();

		// Assert
		assertEquals(location, result, "The resolver should return the getLocation from the DoubleChest.");

		// Verify
		verify(doubleChestMock, atLeastOnce()).getLocation();
	}


	@Test @DisplayName("adapt with valid LootContext")
	public void adapt_with_valid_LootContext_returns_adapted_Location()
	{
		// Arrange
		Location location = new Location(worldMock, 11, 12, 13);
		LootContext lootContext = new LootContext.Builder(location).killer(playerMock).build();

		// Act
		Optional<Locatable> adapted = new LocationAdapter().adapt(lootContext);
		Optional<Location> result = adapted.map(Locatable::getLocation);

		// Assert
		assertTrue(result.isPresent());
		assertEquals(location, result.get(), "The adapter should return the location.");
	}


	@Test
	public void testGetLocation_withNull()
	{
		// Arrange
		Location location = null;

		// Assert
		Optional<Location> result = new LocationAdapter()
				.adapt(location)
				.map(Locatable::getLocation);

		// Assert
		assertTrue(result.isEmpty());
	}


	@Test
	public void testGetLocation_with_inventory()
	{
		// Arrange
		when(inventoryMock.getLocation()).thenReturn(location);

		// Act
		Location result = new LocationAdapter()
				.adapt(inventoryMock)
				.map(Locatable::getLocation).orElseThrow();

		// Assert
		assertEquals(location, result, "The resolver should return the getLocation from the Inventory.");
	}


	@Test
	public void testGetLocation_with_raid()
	{
		// Arrange
		when(raidMock.getLocation()).thenReturn(location);

		// Act
		Location result = new LocationAdapter()
				.adapt(raidMock)
				.map(Locatable::getLocation).orElseThrow();

		// Assert
		assertEquals(location, result, "The resolver should return the getLocation from the DoubleChest.");
	}


	@Test
	public void testGetLocation_with_location()
	{
		// Arrange
		Location testLocation = new Location(worldMock, 11, 12, 13);

		// Act
		Location result = new LocationAdapter()
				.adapt(testLocation)
				.map(Locatable::getLocation).orElseThrow();

		// Assert
		assertEquals(testLocation, result, "The resolver should return the getLocation from the Location.");
	}

}
