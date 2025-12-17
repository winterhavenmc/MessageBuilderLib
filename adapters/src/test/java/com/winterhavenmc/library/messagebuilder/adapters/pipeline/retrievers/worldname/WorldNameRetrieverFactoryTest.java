package com.winterhavenmc.library.messagebuilder.adapters.pipeline.retrievers.worldname;

import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.resolvers.worldname.WorldNameRetriever;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mvplugins.multiverse.core.MultiverseCore;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class WorldNameRetrieverFactoryTest
{
	@Mock MultiverseCore mvPluginMock;
	@Mock Plugin pluginMock;


	@Test
	void getWorldNameRetriever_returns_multiverse_factory_when_multiverse_present()
	{
		// Arrange
		when(mvPluginMock.isEnabled()).thenReturn(true);

		// Act
		WorldNameRetriever result = WorldNameRetrieverFactory.getWorldNameRetriever(mvPluginMock);

		// Assert
		assertInstanceOf(MultiverseWorldNameRetriever.class, result);

		// Verify
		verify(mvPluginMock, atLeastOnce()).isEnabled();
	}


	@Test
	void getWorldNameRetriever_returns_default_factory_when_multiverse_not_present()
	{
		// Act
		WorldNameRetriever result = WorldNameRetrieverFactory.getWorldNameRetriever(pluginMock);

		// Assert
		assertInstanceOf(DefaultWorldNameRetriever.class, result);
	}

}
