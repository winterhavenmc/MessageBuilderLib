package com.winterhavenmc.library.messagebuilder.adapters.pipeline.resolvers.spawnlocation;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mvplugins.multiverse.core.MultiverseCore;
import org.mvplugins.multiverse.core.MultiverseCoreApi;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class PluginBasedResolverTest
{
	@Mock Plugin pluginMock;
	@Mock World worldMock;
	@Mock PluginDescriptionFile descriptionFileMock;
	@Mock MultiverseCore multiverseCoreMock;
	@Mock MultiverseCoreApi multiverseCoreApiMock;

	@Test
	void resolve_returns_location_with_valid_world()
	{
		// Arrange
		Location location = new Location(worldMock, 10, 20, 30);
		when(worldMock.getSpawnLocation()).thenReturn(location);
		when(pluginMock.getDescription()).thenReturn(descriptionFileMock);
		when(descriptionFileMock.getVersion()).thenReturn("5.0");
		PluginBasedResolver resolver = new PluginBasedResolver(pluginMock);

		// Act
		Optional<Location> result = resolver.resolve(worldMock);

		// Assert
		assertTrue(result.isPresent());
		assertEquals(location, result.get());

		// Verify
		verify(worldMock, atLeastOnce()).getSpawnLocation();
	}


	@Test
	void resolve_returns_location_with_supported_multiverse_version()
	{
		// Arrange
		Location location = new Location(worldMock, 10, 20, 30);
		when(worldMock.getSpawnLocation()).thenReturn(location);
		when(pluginMock.getDescription()).thenReturn(descriptionFileMock);
		when(descriptionFileMock.getVersion()).thenReturn("5.0");
		PluginBasedResolver resolver = new PluginBasedResolver(pluginMock);

		// Act
		Optional<Location> result = resolver.resolve(worldMock);

		// Assert
		assertTrue(result.isPresent());
		assertEquals(location, result.get());

		// Verify
		verify(worldMock, atLeastOnce()).getSpawnLocation();
	}


	@Test
	void resolve_returns_location_with_unsupported_multiverse_version()
	{
		// Arrange
		Location location = new Location(worldMock, 10, 20, 30);
		when(worldMock.getSpawnLocation()).thenReturn(location);
		when(pluginMock.getDescription()).thenReturn(descriptionFileMock);
		when(descriptionFileMock.getVersion()).thenReturn("4.0");
		PluginBasedResolver resolver = new PluginBasedResolver(pluginMock);

		// Act
		Optional<Location> result = resolver.resolve(worldMock);

		// Assert
		assertTrue(result.isPresent());
		assertEquals(location, result.get());

		// Verify
		verify(worldMock, atLeastOnce()).getSpawnLocation();
	}


	@Test
	void resolve_returns_location_with_valid_multiverse_plugin()
	{
		// Arrange
		Location location = new Location(worldMock, 10, 20, 30);
//		when(worldMock.getSpawnLocation()).thenReturn(location);
//		when(multiverseCoreMock.getApi()).thenReturn(multiverseCoreApiMock);
		when(multiverseCoreMock.getDescription()).thenReturn(descriptionFileMock);
		when(descriptionFileMock.getVersion()).thenReturn("5.0");
		PluginBasedResolver resolver = new PluginBasedResolver(multiverseCoreMock);

		// Act
		Optional<Location> result = resolver.resolve(worldMock);

		// Assert
		assertTrue(result.isPresent());
//		assertEquals(location, result.get());

		// Verify
//		verify(worldMock, atLeastOnce()).getSpawnLocation();
	}


	// plugin instanceof org.mvplugins.multiverse.core.MultiverseCore


	@Test
	void resolve_returns_empty_optional_with_null_world()
	{
		// Arrange
		PluginBasedResolver resolver = new PluginBasedResolver(pluginMock);

		// Act
		Optional<Location> result = resolver.resolve(null);

		// Assert
		assertTrue(result.isEmpty());
	}

}