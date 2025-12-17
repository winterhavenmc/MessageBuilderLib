package com.winterhavenmc.library.messagebuilder.adapters.pipeline.retrievers.spawnlocation;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mvplugins.multiverse.core.MultiverseCoreApi;
import org.mvplugins.multiverse.core.world.MultiverseWorld;
import org.mvplugins.multiverse.core.world.WorldManager;
import org.mvplugins.multiverse.external.vavr.control.Option;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class MultiverseSpawnLocationRetrieverTest
{
	@Mock ItemStack itemStackMock;
	@Mock World worldMock;
	@Mock MultiverseCoreApi multiverseCoreApiMock;
	@Mock WorldManager worldManagerMock;
	@Mock MultiverseWorld mvWorldMock;


	@Test
	void getSpawnLocation_returns_empty_optional_given_null_world()
	{
		// Arrange
		final MultiverseSpawnLocationRetriever locationRetriever = new MultiverseSpawnLocationRetriever();

		try (MockedStatic<MultiverseCoreApi> mvApi = Mockito.mockStatic(MultiverseCoreApi.class))
		{
			mvApi.when(MultiverseCoreApi::get).thenReturn(multiverseCoreApiMock);

			// Act
			Optional<Location> result = locationRetriever.getSpawnLocation(null);

			// Assert
			assertTrue(result.isEmpty());
		}
	}


	@Test
	void getSpawnLocation_logs_error_on_invalid_mvApi()
	{
		// Arrange
		final MultiverseSpawnLocationRetriever locationRetriever = new MultiverseSpawnLocationRetriever();

		// Act
		Optional<Location> result = locationRetriever.getSpawnLocation(worldMock);

		// Assert
		assertTrue(result.isEmpty());
	}


	@Test
	void getSpawnLocation_returns_empty_optional_given_null_mvWorld()
	{
		// Arrange
		final Location location = new Location(worldMock, 10, 20, 30);
		final MultiverseSpawnLocationRetriever locationRetriever = new MultiverseSpawnLocationRetriever();

		try (MockedStatic<MultiverseCoreApi> mvApi = Mockito.mockStatic(MultiverseCoreApi.class))
		{
			mvApi.when(MultiverseCoreApi::get).thenReturn(multiverseCoreApiMock);
			when(multiverseCoreApiMock.getWorldManager()).thenReturn(worldManagerMock);
			when(worldManagerMock.getWorld(worldMock)).thenReturn(Option.of(null));

			// Act
			Optional<Location> result = locationRetriever.getSpawnLocation(worldMock);

			// Assert
			assertTrue(result.isEmpty());

			// Verify
			verify(multiverseCoreApiMock, atLeastOnce()).getWorldManager();
			verify(worldManagerMock, atLeastOnce()).getWorld(worldMock);
		}
	}


	@Test
	void getSpawnLocation_returns_empty_optional_when_multiverse_returns_null_world()
	{
		// Arrange
		final Location location = new Location(worldMock, 10, 20, 30);
		final MultiverseSpawnLocationRetriever locationRetriever = new MultiverseSpawnLocationRetriever();

		try (MockedStatic<MultiverseCoreApi> mvApi = Mockito.mockStatic(MultiverseCoreApi.class))
		{
			mvApi.when(MultiverseCoreApi::get).thenReturn(multiverseCoreApiMock);
			when(multiverseCoreApiMock.getWorldManager()).thenReturn(worldManagerMock);
			when(worldManagerMock.getWorld(worldMock)).thenReturn(Option.of(null));

			// Act
			Optional<Location> result = locationRetriever.getSpawnLocation(worldMock);

			// Assert
			assertTrue(result.isEmpty());

			// Verify
			verify(multiverseCoreApiMock, atLeastOnce()).getWorldManager();
			verify(worldManagerMock, atLeastOnce()).getWorld(worldMock);
		}
	}


	@Test
	void getSpawnLocation_returns_optional_location()
	{
		// Arrange
		final Location location = new Location(worldMock, 10, 20, 30);
		final MultiverseSpawnLocationRetriever locationRetriever = new MultiverseSpawnLocationRetriever();

		try (MockedStatic<MultiverseCoreApi> mvApi = Mockito.mockStatic(MultiverseCoreApi.class))
		{
			mvApi.when(MultiverseCoreApi::get).thenReturn(multiverseCoreApiMock);
			when(multiverseCoreApiMock.getWorldManager()).thenReturn(worldManagerMock);
			when(worldManagerMock.getWorld(worldMock)).thenReturn(Option.of(mvWorldMock));
			when(mvWorldMock.getSpawnLocation()).thenReturn(location);

			// Act
			Optional<Location> result = locationRetriever.getSpawnLocation(worldMock);

			// Assert
			assertTrue(result.isPresent());
			assertEquals(location, result.get());

			// Verify
			verify(multiverseCoreApiMock, atLeastOnce()).getWorldManager();
			verify(worldManagerMock, atLeastOnce()).getWorld(worldMock);
			verify(mvWorldMock, atLeastOnce()).getSpawnLocation();
		}
	}

}
