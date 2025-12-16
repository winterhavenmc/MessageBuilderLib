package com.winterhavenmc.library.messagebuilder.adapters.pipeline.resolvers.worldname;

import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.resolvers.worldname.WorldNameResolver;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.resolvers.worldname.WorldNameRetriever;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mvplugins.multiverse.core.MultiverseCore;

import java.util.Optional;
import java.util.UUID;

import static com.winterhavenmc.library.messagebuilder.models.DefaultSymbol.UNKNOWN_WORLD;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class BukkitWorldNameResolverTest
{
	@Mock Plugin pluginMock;
	@Mock Server serverMock;
	@Mock PluginManager pluginManagerMock;
	@Mock World worldMock;
	@Mock MultiverseCore mvPluginMock;
	@Mock WorldNameRetriever retrieverMock;


	@Test
	void create_returns_valid_WorldNameResolver()
	{
		// Arrange & Act
		WorldNameResolver resolver = BukkitWorldNameResolver.create(pluginMock, retrieverMock);

		// Assert
		assertInstanceOf(BukkitWorldNameResolver.class, resolver);
	}


	@Test
	void resolve_returns_string_using_retriever()
	{
		// Arrange
		UUID worldUid = new UUID(42, 42);
		when(pluginMock.getServer()).thenReturn(serverMock);
		when(serverMock.getWorld(worldUid)).thenReturn(worldMock);
		when(retrieverMock.getWorldName(worldMock)).thenReturn(Optional.of("test_world"));
		WorldNameResolver resolver = BukkitWorldNameResolver.create(pluginMock, retrieverMock);

		// Act
		String result = resolver.resolve(worldUid);

		// Assert
		assertEquals("test_world", result);

		// Verify
		verify(pluginMock, atLeastOnce()).getServer();
		verify(serverMock, atLeastOnce()).getWorld(worldUid);
		verify(retrieverMock, atLeastOnce()).getWorldName(worldMock);
	}


	@Test
	void resolve_returns_unknown_world_symbol_given_invalid_world()
	{
		// Arrange
		when(pluginMock.getServer()).thenReturn(serverMock);
		WorldNameResolver resolver = BukkitWorldNameResolver.create(pluginMock, retrieverMock);

		// Act
		String result = resolver.resolve(null);

		// Assert
		assertEquals(UNKNOWN_WORLD.symbol(), result);

		// Verify
		verify(pluginMock, atLeastOnce()).getServer();
	}

}
