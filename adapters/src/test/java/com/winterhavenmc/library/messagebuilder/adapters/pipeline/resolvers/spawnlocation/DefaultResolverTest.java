package com.winterhavenmc.library.messagebuilder.adapters.pipeline.resolvers.spawnlocation;

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
class DefaultResolverTest
{
	@Mock World worldMock;


	@Test
	void resolve_returns_location_with_valid_world()
	{
		// Arrange
		Location location = new Location(worldMock, 10, 20, 30);
		when(worldMock.getSpawnLocation()).thenReturn(location);
		DefaultResolver resolver = new DefaultResolver();

		// Act
		Optional<Location> result = resolver.resolve(worldMock);

		// Assert
		assertTrue(result.isPresent());
		assertEquals(location, result.get());

		// Verify
		verify(worldMock, atLeastOnce()).getSpawnLocation();
	}


	@Test
	void resolve_returns_empty_optional_with_null_world()
	{
		// Arrange
		DefaultResolver resolver = new DefaultResolver();

		// Act
		Optional<Location> result = resolver.resolve(null);

		// Assert
		assertTrue(result.isEmpty());
	}

}
