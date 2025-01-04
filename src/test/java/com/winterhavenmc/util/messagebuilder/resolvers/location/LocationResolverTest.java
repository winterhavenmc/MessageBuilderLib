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

package com.winterhavenmc.util.messagebuilder.resolvers.location;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.Player;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class LocationResolverTest {

	@Test
	public void testGetLocation_withValidLocation() {
		// Arrange
		World worldMock = mock(World.class, "MockWorld");
		Location location = new Location(worldMock, 1,2,3);

		// Act
		Optional<Locatable> resolver = LocationResolver.asLocatable(location);
		if (resolver.isPresent()) {
			location = resolver.get().location();
		}

		// Assert
		assertEquals(new Location(worldMock, 1, 2, 3), location, "The resolver should return the location from the Location.");
	}

	@Test
	public void testGetLocation_withValidPlayer() {
		// Arrange
		Player playerMock = mock(Player.class, "MockPlayer");
		World worldMock = mock(World.class, "MockWorld");
		when(playerMock.getLocation()).thenReturn(new Location(worldMock, 1,2,3));
		Location location = null;

		// Act
		Optional<Locatable> resolver = LocationResolver.asLocatable(playerMock);
		if (resolver.isPresent()) {
			location = resolver.get().location();
		}

		// Assert
		assertEquals(new Location(worldMock, 1, 2, 3), location, "The resolver should return the location from the Player.");
	}

	@Test
	public void testGetLocation_withValidBlock() {
		// Arrange
		Block blockMock = mock(Block.class, "MockBlock");
		World worldMock = mock(World.class, "MockWorld");
		when(blockMock.getLocation()).thenReturn(new Location(worldMock, 1,2,3));
		Location location = null;

		// Act
		Optional<Locatable> resolver = LocationResolver.asLocatable(blockMock);
		if (resolver.isPresent()) {
			location = resolver.get().location();
		}

		// Assert
		assertEquals(new Location(worldMock, 1, 2, 3), location, "The resolver should return the location from the Block.");
	}

	@Test
	public void testGetLocation_withValidBlockState() {
		// Arrange
		BlockState blockStateMock = mock(BlockState.class, "MockBlockState");
		World worldMock = mock(World.class, "MockWorld");
		when(blockStateMock.getLocation()).thenReturn(new Location(worldMock, 1,2,3));
		Location location = null;

		// Act
		Optional<Locatable> resolver = LocationResolver.asLocatable(blockStateMock);
		if (resolver.isPresent()) {
			location = resolver.get().location();
		}

		// Assert
		assertEquals(new Location(worldMock, 1, 2, 3), location, "The resolver should return the location from the BlockState.");
	}

	@Test
	public void testGetLocation_withValidDoubleChest() {
		// Arrange
		DoubleChest doubleChestMock = mock(DoubleChest.class, "MockDoubleChest");
		World worldMock = mock(World.class, "MockWorld");
		when(doubleChestMock.getLocation()).thenReturn(new Location(worldMock, 1,2,3));
		Location location = null;

		// Act
		Optional<Locatable> resolver = LocationResolver.asLocatable(doubleChestMock);
		if (resolver.isPresent()) {
			location = resolver.get().location();
		}

		// Assert
		assertEquals(new Location(worldMock, 1, 2, 3), location, "The resolver should return the location from the DoubleChest.");
	}

	@Test
	public void testGetLocation_withNull() {
		// Arrange
		Location location = null;

		// Act & Assert
		Optional<Locatable> resolver = LocationResolver.asLocatable(null);
		if (resolver.isPresent()) {
			location = resolver.get().location();
		}
		assertNull(location);
	}

}
