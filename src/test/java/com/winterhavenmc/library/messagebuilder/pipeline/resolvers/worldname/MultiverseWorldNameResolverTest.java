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

import com.onarandombox.MultiverseCore.MVWorld;
import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MVWorldManager;

import org.bukkit.World;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class MultiverseWorldNameResolverTest
{
	@Mock World worldMock;
	@Mock MultiverseCore multiverseCoreMock;
	@Mock MVWorldManager mvWorldManagerMock;
	@Mock MVWorld mvWorldMock;


	@Test
	void testResolveWorldName_null_world()
	{
		WorldNameResolver resolver = new MultiverseWorldNameResolver(multiverseCoreMock);

		String result = resolver.resolveWorldName(null);

		assertEquals("NULL WORLD", result);
	}


	@Test
	void testResolveWorldName_valid_world()
	{
		when(worldMock.getName()).thenReturn("test_world");
		WorldNameResolver resolver = new MultiverseWorldNameResolver(multiverseCoreMock);

		String result = resolver.resolveWorldName(worldMock);

		assertEquals("test_world", result);
	}


	@Test
	void getWorldName_blank_alias()
	{
		// Arrange
		when(worldMock.getName()).thenReturn("world-name");
		when(multiverseCoreMock.getMVWorldManager()).thenReturn(mvWorldManagerMock);
		when(mvWorldManagerMock.getMVWorld(worldMock)).thenReturn(mvWorldMock);
		when(mvWorldMock.getColoredWorldString()).thenReturn("");

		WorldNameResolver resolver = new MultiverseWorldNameResolver(multiverseCoreMock);

		// Act
		String result = resolver.resolveWorldName(worldMock);

		// Assert
		assertEquals("world-name", result);

		// Verify
		verify(mvWorldMock, atLeastOnce()).getColoredWorldString();
	}


	@Test
	void getWorldName_no_alias()
	{
		// Arrange
		when(mvWorldMock.getColoredWorldString()).thenReturn("World Alias");
		lenient().when(multiverseCoreMock.isEnabled()).thenReturn(true);
		when(multiverseCoreMock.getMVWorldManager()).thenReturn(mvWorldManagerMock);
		when(mvWorldManagerMock.getMVWorld(worldMock)).thenReturn(mvWorldMock);

		WorldNameResolver resolver = new MultiverseWorldNameResolver(multiverseCoreMock);

		// Act
		String result = resolver.resolveWorldName(worldMock);

		// Assert
		assertEquals("World Alias", result);

		// Verify
		verify(mvWorldMock, atLeastOnce()).getColoredWorldString();
	}

}
