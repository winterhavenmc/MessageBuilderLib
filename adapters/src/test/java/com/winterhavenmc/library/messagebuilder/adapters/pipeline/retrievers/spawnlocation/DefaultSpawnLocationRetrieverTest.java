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

import com.winterhavenmc.library.messagebuilder.adapters.pipeline.retrievers.worldname.DefaultWorldNameRetriever;
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
	void getWorldName_with_valid_world()
	{
		// Arrange
		DefaultWorldNameRetriever retriever = new DefaultWorldNameRetriever();
		when(worldMock.getName()).thenReturn("world_name");

		// Act
		Optional<String> result = retriever.getWorldName(worldMock);

		// Assert
		assertEquals(Optional.of("world_name"), result);

		// Verify
		verify(worldMock, atLeastOnce()).getName();
	}


	@Test
	void getWorldName_with_null_world()
	{
		// Arrange
		DefaultWorldNameRetriever retriever = new DefaultWorldNameRetriever();

		// Act
		Optional<String> result = retriever.getWorldName(null);

		// Assert
		assertEquals(Optional.empty(), result);
	}

}