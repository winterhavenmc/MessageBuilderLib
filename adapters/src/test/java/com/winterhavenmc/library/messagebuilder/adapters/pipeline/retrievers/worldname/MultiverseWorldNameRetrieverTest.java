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

package com.winterhavenmc.library.messagebuilder.adapters.pipeline.retrievers.worldname;

import org.bukkit.World;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mvplugins.multiverse.core.MultiverseCore;
import org.mvplugins.multiverse.core.MultiverseCoreApi;
import org.mvplugins.multiverse.core.world.MultiverseWorld;
import org.mvplugins.multiverse.core.world.WorldManager;
import org.mvplugins.multiverse.external.vavr.control.Option;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class MultiverseWorldNameRetrieverTest
{
	@Mock World worldMock;
	@Mock MultiverseCore multiverseCoreMock;
	@Mock MultiverseCoreApi multiverseCoreApiMock;
	@Mock WorldManager worldManagerMock;
	@Mock MultiverseWorld mvWorldMock;
	@Mock Option<MultiverseWorld> optionWorldMock;

	private MockedStatic<MultiverseCoreApi> mvApi;

	@BeforeEach
	void setUp()
	{
		mvApi = mockStatic(MultiverseCoreApi.class);
	}

	@AfterEach
	void tearDown()
	{
		mvApi.close();
	}


	@Test
	void getWorldName_with_valid_world()
	{
		// Arrange
		mvApi.when(MultiverseCoreApi::get).thenReturn(multiverseCoreApiMock);
		when(multiverseCoreApiMock.getWorldManager()).thenReturn(worldManagerMock);
		when(worldManagerMock.getWorld(worldMock)).thenReturn(optionWorldMock);
		when(optionWorldMock.isEmpty()).thenReturn(false);
		when(optionWorldMock.getOrNull()).thenReturn(mvWorldMock);
		when(mvWorldMock.getAliasOrName()).thenReturn("World Alias");
		MultiverseWorldNameRetriever retriever = new MultiverseWorldNameRetriever(multiverseCoreMock);

		// Act
		Optional<String> result = retriever.getWorldName(worldMock);

		// Assert
		assertEquals(Optional.of("World Alias"), result);

		// Verify
		verify(multiverseCoreApiMock, atLeastOnce()).getWorldManager();
		verify(worldManagerMock, atLeastOnce()).getWorld(worldMock);
		verify(optionWorldMock, atLeastOnce()).isEmpty();
		verify(optionWorldMock, atLeastOnce()).getOrNull();
		verify(mvWorldMock, atLeastOnce()).getAliasOrName();
	}


	@Test
	void getWorldName_with_null_world()
	{
		// Arrange
		mvApi.when(MultiverseCoreApi::get).thenReturn(multiverseCoreApiMock);
		MultiverseWorldNameRetriever retriever = new MultiverseWorldNameRetriever(multiverseCoreMock);

		// Act
		Optional<String> result = retriever.getWorldName(null);

		// Assert
		assertEquals(Optional.empty(), result);
	}


	@Test
	void getWorldName_with_null_mvWorldManager()
	{
		// Arrange
		mvApi.when(MultiverseCoreApi::get).thenReturn(multiverseCoreApiMock);
		when(multiverseCoreApiMock.getWorldManager()).thenReturn(null);
		MultiverseWorldNameRetriever retriever = new MultiverseWorldNameRetriever(multiverseCoreMock);

		// Act
		Optional<String> result = retriever.getWorldName(worldMock);

		// Assert
		assertEquals(Optional.empty(), result);

		// Verify
		verify(multiverseCoreApiMock, atLeastOnce()).getWorldManager();
	}


	@Test
	void getWorldName_with_empty_option_mvWorld()
	{
		// Arrange
		mvApi.when(MultiverseCoreApi::get).thenReturn(multiverseCoreApiMock);
		when(multiverseCoreApiMock.getWorldManager()).thenReturn(worldManagerMock);
		when(worldManagerMock.getWorld(worldMock)).thenReturn(optionWorldMock);
		when(optionWorldMock.isEmpty()).thenReturn(true);
		MultiverseWorldNameRetriever retriever = new MultiverseWorldNameRetriever(multiverseCoreMock);

		// Act
		Optional<String> result = retriever.getWorldName(worldMock);

		// Assert
		assertEquals(Optional.empty(), result);

		// Verify
		verify(multiverseCoreApiMock, atLeastOnce()).getWorldManager();
		verify(worldManagerMock, atLeastOnce()).getWorld(worldMock);
		verify(optionWorldMock, atLeastOnce()).isEmpty();
	}


	@Test
	void getWorldName_with_null_option_world()
	{
		// Arrange
		mvApi.when(MultiverseCoreApi::get).thenReturn(multiverseCoreApiMock);
		when(multiverseCoreApiMock.getWorldManager()).thenReturn(worldManagerMock);
		when(worldManagerMock.getWorld(worldMock)).thenReturn(optionWorldMock);
		when(optionWorldMock.isEmpty()).thenReturn(false);
		when(optionWorldMock.getOrNull()).thenReturn(null);
		MultiverseWorldNameRetriever retriever = new MultiverseWorldNameRetriever(multiverseCoreMock);

		// Act
		Optional<String> result = retriever.getWorldName(worldMock);

		// Assert
		assertEquals(Optional.empty(), result);

		// Verify
		verify(multiverseCoreApiMock, atLeastOnce()).getWorldManager();
		verify(worldManagerMock, atLeastOnce()).getWorld(worldMock);
		verify(optionWorldMock, atLeastOnce()).isEmpty();
		verify(optionWorldMock, atLeastOnce()).getOrNull();
	}


	@Test
	void getWorldName_throws_mvApi_exception()
	{
		// Arrange
		mvApi.when(MultiverseCoreApi::get).thenThrow(IllegalStateException.class);
		MultiverseWorldNameRetriever retriever = new MultiverseWorldNameRetriever(multiverseCoreMock);

		// Act
		Optional<String> result = retriever.getWorldName(worldMock);

		// Assert
		assertEquals(Optional.empty(), result);
	}

}
