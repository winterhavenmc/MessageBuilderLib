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

package com.winterhavenmc.util.messagebuilder.resolvers.uuid;

import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.profile.PlayerProfile;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class UUIDResolverTest {

	@Test
	public void testGetUUID_withValidEntity() {
		// Arrange
		Entity entityMock = mock(Entity.class, "MockEntity");
		when(entityMock.getUniqueId()).thenReturn(new UUID(0,1));
		UUID uuid = null;

		// Act
		Identifiable resolver = UUIDResolver.asIdentifiable(entityMock);
		if (resolver != null) {
			uuid = resolver.getUniqueId();
		}

		// Assert
		assertEquals(new UUID(0, 1), uuid, "The resolver should return a UUID from the Entity.");

		// verify
		verify(entityMock, atLeastOnce()).getUniqueId();
	}


	@Test
	public void testGetUUID_withValidPlayerProfile() {
		// Arrange
		PlayerProfile playerProfileMock = mock(PlayerProfile.class, "MockPlayerProfile");
		when(playerProfileMock.getUniqueId()).thenReturn(new UUID(0,1));
		UUID uuid = null;

		// Act
		Identifiable resolver = UUIDResolver.asIdentifiable(playerProfileMock);
		if (resolver != null) {
			uuid = resolver.getUniqueId();
		}

		// Assert
		assertEquals(new UUID(0, 1), uuid, "The resolver should return a UUID from the PlayerProfile.");

		// verify
		verify(playerProfileMock, atLeastOnce()).getUniqueId();
	}


	@Test
	public void testGetUUID_withValidOfflinePlayer() {
		// Arrange
		OfflinePlayer offlinePlayerMock = mock(OfflinePlayer.class, "MockOfflinePlayer");
		when(offlinePlayerMock.getUniqueId()).thenReturn(new UUID(0,1));
		UUID uuid = null;

		// Act
		Identifiable resolver = UUIDResolver.asIdentifiable(offlinePlayerMock);
		if (resolver != null) {
			uuid = resolver.getUniqueId();
		}

		// Assert
		assertEquals(new UUID(0, 1), uuid, "The resolver should return a UUID from the OfflinePlayer.");

		// verify
		verify(offlinePlayerMock, atLeastOnce()).getUniqueId();
	}


	@Test
	public void testGetUUID_withValidWorld() {
		// Arrange
		World worldMock = mock(World.class, "MockWorld");
		when(worldMock.getUID()).thenReturn(new UUID(1,1));
		UUID uuid = null;

		// Act
		Identifiable resolver = UUIDResolver.asIdentifiable(worldMock);
		if (resolver != null) {
			uuid = resolver.getUniqueId();
		}

		// Assert
		assertEquals(new UUID(1, 1), uuid, "The resolver should return a UUID from the World.");

		// verify
		verify(worldMock, atLeastOnce()).getUID();
	}


	@Test
	public void testGetUUID_withNull() {
		// Arrange
		UUID uuid = null;

		// Act
		Identifiable resolver = UUIDResolver.asIdentifiable(null);
		if (resolver != null) {
			uuid = resolver.getUniqueId();
		}

		// Assert
		assertNull(uuid, "The resolver should return a null when passed a null.");
	}

}
