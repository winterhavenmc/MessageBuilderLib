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

package com.winterhavenmc.util.messagebuilder.adapters.location;

import com.winterhavenmc.util.messagebuilder.adapters.Adapter;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.Player;

import org.junit.jupiter.api.BeforeEach;
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

}
