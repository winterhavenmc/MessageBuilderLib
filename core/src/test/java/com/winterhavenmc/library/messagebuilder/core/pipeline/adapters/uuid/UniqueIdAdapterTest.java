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

package com.winterhavenmc.library.messagebuilder.core.pipeline.adapters.uuid;

import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.profile.PlayerProfile;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UniqueIdAdapterTest
{
	@Mock Entity entityMock;
	@Mock PlayerProfile playerProfileMock;

	@Test
	public void testGetUUID_withValidEntity()
	{
		// Arrange
		when(entityMock.getUniqueId()).thenReturn(new UUID(0, 1));
		UUID uuid = null;

		// Act
		Optional<Identifiable> resolver = new UniqueIdAdapter().adapt(entityMock);
		if (resolver.isPresent())
		{
			uuid = resolver.get().getUniqueId();
		}

		// Assert
		assertEquals(new UUID(0, 1), uuid, "The resolver should return a UUID from the Entity.");

		// Verify
		verify(entityMock, atLeastOnce()).getUniqueId();
	}


	@Test
	public void testGetUUID_withValidPlayerProfile()
	{
		// Arrange
		when(playerProfileMock.getUniqueId()).thenReturn(new UUID(0, 1));
		UUID uuid = null;

		// Act
		Optional<Identifiable> resolver = new UniqueIdAdapter().adapt(playerProfileMock);
		if (resolver.isPresent())
		{
			uuid = resolver.get().getUniqueId();
		}

		// Assert
		assertEquals(new UUID(0, 1), uuid, "The resolver should return a UUID from the PlayerProfile.");

		// Verify
		verify(playerProfileMock, atLeastOnce()).getUniqueId();
	}


	@Test
	public void testGetUUID_withValidOfflinePlayer()
	{
		// Arrange
		OfflinePlayer offlinePlayerMock = mock(OfflinePlayer.class, "MockOfflinePlayer");
		when(offlinePlayerMock.getUniqueId()).thenReturn(new UUID(0, 1));
		UUID uuid = null;

		// Act
		Optional<Identifiable> resolver = new UniqueIdAdapter().adapt(offlinePlayerMock);
		if (resolver.isPresent())
		{
			uuid = resolver.get().getUniqueId();
		}

		// Assert
		assertEquals(new UUID(0, 1), uuid, "The resolver should return a UUID from the OfflinePlayer.");

		// Verify
		verify(offlinePlayerMock, atLeastOnce()).getUniqueId();
	}


	@Test
	public void testGetUUID_withValidWorld()
	{
		// Arrange
		World worldMock = mock(World.class, "MockWorld");
		when(worldMock.getUID()).thenReturn(new UUID(1, 1));
		UUID uuid = null;

		// Act
		Optional<Identifiable> resolver = new UniqueIdAdapter().adapt(worldMock);
		if (resolver.isPresent())
		{
			uuid = resolver.get().getUniqueId();
		}

		// Assert
		assertEquals(new UUID(1, 1), uuid, "The resolver should return a UUID from the World.");

		// Verify
		verify(worldMock, atLeastOnce()).getUID();
	}


	@Test
	public void testGetUUID_withNull()
	{
		// Arrange
		UUID uuid = null;

		// Act
		Optional<Identifiable> resolver = new UniqueIdAdapter().adapt(null);
		if (resolver.isPresent())
		{
			uuid = resolver.get().getUniqueId();
		}

		// Assert
		assertNull(uuid, "The resolver should return a null when passed a null.");
	}

}
