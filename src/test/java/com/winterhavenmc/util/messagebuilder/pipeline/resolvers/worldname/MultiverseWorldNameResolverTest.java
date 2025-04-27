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

import com.onarandombox.MultiverseCore.MVWorld;
import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MVWorldManager;

import org.bukkit.World;

import org.junit.jupiter.api.Disabled;
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
	@Disabled
	void getWorldName()
	{
		// Arrange
		when(multiverseCoreMock.getMVWorldManager()).thenReturn(mvWorldManagerMock);
		when(mvWorldManagerMock.getMVWorld(worldMock)).thenReturn(mvWorldMock);
		when(mvWorldMock.getAlias()).thenReturn("MV World Alias");

		WorldNameResolver resolver = new MultiverseWorldNameResolver(multiverseCoreMock);

		// Act
		String result = resolver.resolveWorldName(worldMock);

		// Assert
		assertEquals("MV World Alias", result);

		// Verify
		verify(mvWorldMock, atLeastOnce()).getAlias();
	}


	@Test
	@Disabled
	void getWorldName_no_alias()
	{
		// Arrange
		when(worldMock.getName()).thenReturn("test-world");
		when(multiverseCoreMock.getMVWorldManager()).thenReturn(mvWorldManagerMock);
		when(mvWorldManagerMock.getMVWorld(worldMock)).thenReturn(mvWorldMock);
		when(mvWorldMock.getAlias()).thenReturn(null);

		WorldNameResolver resolver = new MultiverseWorldNameResolver(multiverseCoreMock);

		// Act
		String result = resolver.resolveWorldName(worldMock);

		// Assert
		assertEquals("test-world", result);

		// Verify
		verify(mvWorldMock, atLeastOnce()).getAlias();
	}


	@Test
	@Disabled
	void getWorldName_blank_alias()
	{
		// Arrange
		when(worldMock.getName()).thenReturn("test-world");
		when(multiverseCoreMock.getMVWorldManager()).thenReturn(mvWorldManagerMock);
		when(mvWorldManagerMock.getMVWorld(worldMock)).thenReturn(mvWorldMock);
		when(mvWorldMock.getAlias()).thenReturn("");

		WorldNameResolver resolver = new MultiverseWorldNameResolver(multiverseCoreMock);

		// Act
		String result = resolver.resolveWorldName(worldMock);

		// Assert
		assertEquals("test-world", result);

		// Verify
		verify(mvWorldMock, atLeastOnce()).getAlias();
		verify(worldMock, atLeastOnce()).getName();
	}


	@Test
	@Disabled
	void getWorldName_mvWorld_null()
	{
		// Arrange
		when(worldMock.getName()).thenReturn("test-world");
		when(multiverseCoreMock.getMVWorldManager()).thenReturn(mvWorldManagerMock);
		when(mvWorldManagerMock.getMVWorld(worldMock)).thenReturn(null);

		WorldNameResolver resolver = new MultiverseWorldNameResolver(multiverseCoreMock);

		// Act
		String result = resolver.resolveWorldName(worldMock);

		// Assert
		assertEquals("test-world", result);

		// Verify
		verify(worldMock, atLeastOnce()).getName();
	}

}