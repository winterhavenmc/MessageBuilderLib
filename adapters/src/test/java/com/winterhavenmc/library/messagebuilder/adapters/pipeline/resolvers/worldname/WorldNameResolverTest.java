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

package com.winterhavenmc.library.messagebuilder.adapters.pipeline.resolvers.worldname;

import com.winterhavenmc.library.messagebuilder.core.ports.resolvers.worldname.DefaultResolver;
import com.winterhavenmc.library.messagebuilder.core.ports.resolvers.worldname.PluginResolver;
import com.winterhavenmc.library.messagebuilder.core.ports.resolvers.worldname.WorldNameResolver;
import org.bukkit.World;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mvplugins.multiverse.core.MultiverseCore;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class WorldNameResolverTest
{
	@Mock PluginManager pluginManagerMock;
	@Mock World worldMock;
	@Mock MultiverseCore multiverseCoreMock;
	@Mock PluginDescriptionFile descriptionMock;


	@Test
	void getResolver()
	{
		// Arrange
		WorldNameResolver resolver = WorldNameResolver.get(pluginManagerMock);

		// Act
		assertNotNull(resolver);

		// Assert
		assertInstanceOf(DefaultResolver.class, resolver);
	}


	@Test
	void resolveWorldName()
	{
		// Arrange
		when(worldMock.getName()).thenReturn("test_world");
		WorldNameResolver resolver = WorldNameResolver.get(pluginManagerMock);

		// Act
		var result = resolver.resolve(worldMock);

		// Assert
		assertEquals("test_world", result);
	}


	@Test
	void resolveWorldName_multiverse_fail()
	{
		// Arrange
		WorldNameResolver resolver = new PluginResolver(multiverseCoreMock);

		// Act
		var result = resolver.resolve(worldMock);

		// Assert
		assertNull(result);
	}


	@Test
	void resolveWorldName_multiverse_succeed()
	{
		// Arrange
		WorldNameResolver resolver = mock(PluginResolver.class);
		when(resolver.resolve(worldMock)).thenReturn("fake alias");

		// Act
		var result = resolver.resolve(worldMock);

		// Assert
		assertEquals("fake alias", result);
	}


	@Test
	void getResolver_for_multiverse_5()
	{
		// Arrange
		when(pluginManagerMock.getPlugin("Multiverse-Core")).thenReturn(multiverseCoreMock);
		when(multiverseCoreMock.isEnabled()).thenReturn(true);

		// Act
		WorldNameResolver resolver = WorldNameResolver.get(pluginManagerMock);

		// Assert
		assertInstanceOf(PluginResolver.class, resolver);

		// Verify
		verify(pluginManagerMock, atLeastOnce()).getPlugin("Multiverse-Core");
		verify(multiverseCoreMock, atLeastOnce()).isEnabled();
	}

}
