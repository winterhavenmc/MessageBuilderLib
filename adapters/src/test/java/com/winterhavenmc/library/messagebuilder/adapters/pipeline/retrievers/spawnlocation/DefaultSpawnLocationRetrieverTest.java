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

package com.winterhavenmc.library.messagebuilder.adapters.pipeline.retrievers.spawnlocation;

import org.bukkit.Location;
import org.bukkit.World;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class DefaultSpawnLocationRetrieverTest
{
	@Mock World worldMock;


	@Test
	void getSpawnLocation_with_valid_world()
	{
		// Arrange
		Location location = new Location(worldMock, 10, 20, 30);

		DefaultSpawnLocationRetriever retriever = new DefaultSpawnLocationRetriever();
		when(worldMock.getSpawnLocation()).thenReturn(location);

		// Act
		Optional<Location> result = retriever.getSpawnLocation(worldMock);

		// Assert
		assertTrue(result.isPresent());
		assertEquals(location, result.get());

		// Verify
		verify(worldMock, atLeastOnce()).getSpawnLocation();
	}


	@Test
	void getSpawnLocation_with_null_world()
	{
		// Arrange
		DefaultSpawnLocationRetriever retriever = new DefaultSpawnLocationRetriever();

		// Act
		Optional<Location> result = retriever.getSpawnLocation(null);

		// Assert
		assertEquals(Optional.empty(), result);
	}

}
