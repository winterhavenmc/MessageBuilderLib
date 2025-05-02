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

package com.winterhavenmc.library.messagebuilder.pipeline.resolvers.worldname;

import com.onarandombox.MultiverseCore.MultiverseCore;
import org.bukkit.World;
import org.bukkit.plugin.PluginManager;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class WorldNameResolverTest
{
	@Mock PluginManager pluginManagerMock;
	@Mock World worldMock;
	@Mock MultiverseCore multiverseCoreMock;


	@Test
	void getResolver()
	{
		// Arrange
		WorldNameResolver resolver = WorldNameResolver.getResolver(pluginManagerMock);

		// Act
		assertNotNull(resolver);

		// Assert
		assertInstanceOf(DefaultWorldNameResolver.class, resolver);
	}


	@Test
	void resolveWorldName()
	{
		// Arrange
		when(worldMock.getName()).thenReturn("test_world");
		WorldNameResolver resolver = WorldNameResolver.getResolver(pluginManagerMock);

		// Act
		var result = resolver.resolveWorldName(worldMock);

		// Assert
		assertEquals("test_world", result);
	}


	@Test
	void resolveWorldName_multiverse_fail()
	{
		// Arrange
		WorldNameResolver resolver = new MultiverseWorldNameResolver(multiverseCoreMock);

		// Act
		var result = resolver.resolveWorldName(worldMock);

		// Assert
		assertNull(result);
	}


	@Test
	void resolveWorldName_multiverse_succeed()
	{
		// Arrange
		WorldNameResolver resolver = mock(MultiverseWorldNameResolver.class);
		when(resolver.resolveWorldName(worldMock)).thenReturn("fake alias");

		// Act
		var result = resolver.resolveWorldName(worldMock);

		// Assert
		assertEquals("fake alias", result);
	}




	@Test
	void getResolver_multiverse()
	{
		// Arrange
		when(pluginManagerMock.getPlugin("Multiverse-Core")).thenReturn(multiverseCoreMock);
		when(multiverseCoreMock.isEnabled()).thenReturn(true);

		// Act
		WorldNameResolver resolver = WorldNameResolver.getResolver(pluginManagerMock);

		// Assert
		assertInstanceOf(MultiverseWorldNameResolver.class, resolver);

		// Verify
		verify(pluginManagerMock, atLeastOnce()).getPlugin("Multiverse-Core");
		verify(multiverseCoreMock, atLeastOnce()).isEnabled();
	}

}
