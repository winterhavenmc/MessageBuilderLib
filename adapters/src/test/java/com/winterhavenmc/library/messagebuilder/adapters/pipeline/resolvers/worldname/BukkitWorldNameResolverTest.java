package com.winterhavenmc.library.messagebuilder.adapters.pipeline.resolvers.worldname;

import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.resolvers.worldname.WorldNameResolver;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.resolvers.worldname.WorldNameRetriever;
import org.bukkit.World;
import org.bukkit.plugin.PluginManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mvplugins.multiverse.core.MultiverseCore;

import java.util.Optional;

import static com.winterhavenmc.library.messagebuilder.models.DefaultSymbol.UNKNOWN_WORLD;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class BukkitWorldNameResolverTest
{
	@Mock PluginManager pluginManagerMock;
	@Mock World worldMock;
	@Mock MultiverseCore mvPluginMock;
	@Mock WorldNameRetriever retrieverMock;


	@Test
	void create_returns_valid_WorldNameResolver()
	{
		// Arrange & Act
		WorldNameResolver resolver = BukkitWorldNameResolver.create(retrieverMock);

		// Assert
		assertInstanceOf(BukkitWorldNameResolver.class, resolver);
	}


	@Test
	void resolve_returns_string_using_retriever()
	{
		// Arrange
		when(retrieverMock.getWorldName(worldMock)).thenReturn(Optional.of("test_world"));
		WorldNameResolver resolver = BukkitWorldNameResolver.create(retrieverMock);

		// Act
		String result = resolver.resolve(worldMock);

		// Assert
		assertEquals("test_world", result);

		// Verify
		verify(retrieverMock, atLeastOnce()).getWorldName(worldMock);
	}


	@Test
	void resolve_returns_unknown_world_symbol_given_invalid_world()
	{
		// Arrange
		WorldNameResolver resolver = BukkitWorldNameResolver.create(retrieverMock);

		// Act
		String result = resolver.resolve(null);

		// Assert
		assertEquals(UNKNOWN_WORLD.symbol(), result);
	}

}
