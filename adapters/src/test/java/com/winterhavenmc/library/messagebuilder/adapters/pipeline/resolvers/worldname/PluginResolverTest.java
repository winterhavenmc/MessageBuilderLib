package com.winterhavenmc.library.messagebuilder.adapters.pipeline.resolvers.worldname;

import org.bukkit.World;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mvplugins.multiverse.core.MultiverseCore;
import org.mvplugins.multiverse.core.MultiverseCoreApi;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class PluginResolverTest
{
	@Mock MultiverseCore mvPluginMock;
	@Mock MultiverseCoreApi multiverseCoreApiMock;
	@Mock World worldMock;


	@Test
	void resolve_returns_null_symbol_for_null_world()
	{
		// Arrange
		PluginResolver pluginResolver = new PluginResolver(mvPluginMock);

		// Act
		String result = pluginResolver.resolve(null);

		// Assert
		assertEquals("∅", result);
	}

	@Test
	void resolve_returns_world_name_when_given_null_mvPlugin()
	{
		// Arrange
		PluginResolver pluginResolver = new PluginResolver(null);
		when(worldMock.getName()).thenReturn("test-world");

		// Act
		String result = pluginResolver.resolve(worldMock);

		// Assert
		assertEquals("test-world", result);

		// Verify
		verify(worldMock, atLeastOnce()).getName();
	}

}
