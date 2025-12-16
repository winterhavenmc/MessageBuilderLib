package com.winterhavenmc.library.messagebuilder.adapters.pipeline.retrievers.spawnlocation;

import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mvplugins.multiverse.core.MultiverseCore;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class SpawnLocationRetrieverTest
{
	@Mock Plugin pluginMock;
	@Mock MultiverseCore mvPluginMock;


	@Test
	void test_create_with_multiverse()
	{
		// Arrange
		when(mvPluginMock.isEnabled()).thenReturn(true);

		// Act
		SpawnLocationRetriever locationRetriever = SpawnLocationRetriever.create(mvPluginMock);

		// Assert
		assertInstanceOf(MultiverseSpawnLocationRetriever.class, locationRetriever);

		// Verify
		verify(mvPluginMock, atLeastOnce()).isEnabled();
	}


	@Test
	void test_create_without_multiverse()
	{
		// Arrange
		// Act
		SpawnLocationRetriever locationRetriever = SpawnLocationRetriever.create(pluginMock);

		// Assert
		assertInstanceOf(DefaultSpawnLocationRetriever.class, locationRetriever);
	}

}
