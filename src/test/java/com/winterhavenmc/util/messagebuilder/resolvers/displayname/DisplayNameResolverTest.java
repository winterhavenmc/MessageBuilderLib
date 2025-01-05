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

package com.winterhavenmc.util.messagebuilder.resolvers.displayname;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DisplayNameResolverTest {

	@Nested
	class PlayerDisplayNameResolverTests {
		@Test
		public void testGetDisplayName_withValidPlayer() {
			// Arrange
			Player playerMock = mock(Player.class, "MockPlayer");
			when(playerMock.getDisplayName()).thenReturn("&aPlayer One Display Name");
			String displayName = "";

			// Act
			Optional<DisplayNameable> resolver = DisplayNameResolver.asDisplayNameable(playerMock);
			if (resolver.isPresent()) {
				displayName = resolver.get().getDisplayName();
			}

			// Assert
			assertEquals("&aPlayer One Display Name", displayName, "The resolver should return the displayName from the Player.");
		}

		@Test
		public void testGetDisplayName_withNullPlayer() {
			// Arrange
			Player nullPlayer = null;
			String displayName = null;

			// Act
			Optional<DisplayNameable> resolver = DisplayNameResolver.asDisplayNameable(nullPlayer);
			if (resolver.isPresent()) {
				displayName = resolver.get().getDisplayName();
			}

			// Assert
			assertNull(displayName, "The resolver should return an empty string for a null Player.");
		}

		@Test
		public void testConstructor_withNullPlayer() {
			// Arrange
			String displayName = null;

			// Act
			Optional<DisplayNameable> resolver = DisplayNameResolver.asDisplayNameable(null);
			if (resolver.isPresent()) {
				displayName = resolver.get().getDisplayName();
			}

			// assert
			assertNull(displayName);
		}
	}


	@Nested
	class EntityDisplayNameableTests {

		@Test
		void testGetDisplayName_withValidEntity() {
			// Arrange
			Entity entityMock = mock(Entity.class, "MockEntity");
			when(entityMock.getCustomName()).thenReturn("Custom Entity Name");
			String displayName = "";

			// Act
			Optional<DisplayNameable> resolver = DisplayNameResolver.asDisplayNameable(entityMock);
			if (resolver.isPresent()) {
				displayName = resolver.get().getDisplayName();
			}

			// Assert
			assertEquals("Custom Entity Name", displayName, "The resolver should return the displayName from the Entity.");
		}
	}

	@Nested
	class WorldDisplayNameableTests {

		@Test
		void testGetDisplayName_withValidWorld() {
			// Arrange
			World worldMock = mock(World.class, "MockWorld");
			when(worldMock.getName()).thenReturn("World Name");
			String displayName = "";

			// Act
			Optional<DisplayNameable> resolver = DisplayNameResolver.asDisplayNameable(worldMock);
			if (resolver.isPresent()) {
				displayName = resolver.get().getDisplayName();
			}

			// Assert
			assertEquals("World Name", displayName, "The resolver should return the displayName from the World.");
		}
	}

}
