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

package com.winterhavenmc.util.messagebuilder.pipeline.resolvers.worldname;

import org.bukkit.plugin.PluginManager;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@ExtendWith(MockitoExtension.class)
class WorldNameResolverTest
{
	@Mock PluginManager pluginManagerMock;


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


//	@Test
//	@Disabled
//	void getResolver_multiverse()
//	{
//		// Arrange
//		when(pluginManagerMock.getPlugin("Multiverse-Core")).thenReturn(multiverseCore);
//		when(multiverseCore.isEnabled()).thenReturn(true);
//
//		// Act
//		WorldNameResolver resolver = WorldNameResolver.getResolver(pluginManagerMock);
//
//		// Assert
//		assertInstanceOf(MultiverseWorldNameResolver.class, resolver);
//
//		// Verify
//		verify(pluginManagerMock, atLeastOnce()).getPlugin("Multiverse-Core");
//		verify(multiverseCore, atLeastOnce()).isEnabled();
//	}

}
