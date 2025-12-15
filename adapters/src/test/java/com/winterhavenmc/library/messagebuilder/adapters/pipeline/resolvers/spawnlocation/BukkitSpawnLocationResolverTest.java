package com.winterhavenmc.library.messagebuilder.adapters.pipeline.resolvers.spawnlocation;

import com.winterhavenmc.library.messagebuilder.adapters.pipeline.retrievers.spawnlocation.SpawnLocationRetriever;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.resolvers.spawnlocation.SpawnLocationResolver;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.PluginManager;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class BukkitSpawnLocationResolverTest
{
	@Mock SpawnLocationRetriever retrieverMock;
	@Mock PluginManager pluginManagerMock;
	@Mock World worldMock;


	@Test
	void get_returns_valid_resolver()
	{
		// Act
		SpawnLocationResolver resolver = BukkitSpawnLocationResolver.create(retrieverMock);

		// Assert
		assertInstanceOf(BukkitSpawnLocationResolver.class, resolver);
	}


	@Test
	void resolve_returns_spawn_location()
	{
		// Arrange
		Location location = new Location(worldMock, 10, 11, 12);
		when(retrieverMock.getSpawnLocation(worldMock)).thenReturn(Optional.of(location));

		// Act
		SpawnLocationResolver resolver = BukkitSpawnLocationResolver.create(retrieverMock);
		Optional<Location> result = resolver.resolve(worldMock);

		// Assert
		assertTrue(result.isPresent());
		assertEquals(location, result.get());

		// Verify
		verify(retrieverMock, atLeastOnce()).getSpawnLocation(worldMock);
	}

}
