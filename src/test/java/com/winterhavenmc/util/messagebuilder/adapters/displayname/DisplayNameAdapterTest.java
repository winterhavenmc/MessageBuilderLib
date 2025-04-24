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

package com.winterhavenmc.util.messagebuilder.adapters.displayname;

import com.winterhavenmc.util.messagebuilder.pipeline.adapters.displayname.DisplayNameAdapter;
import com.winterhavenmc.util.messagebuilder.pipeline.adapters.displayname.DisplayNameable;
import com.winterhavenmc.util.messagebuilder.util.AdapterContextContainer;
import com.winterhavenmc.util.messagebuilder.worldname.WorldNameResolver;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class DisplayNameAdapterTest
{
	@Mock WorldNameResolver worldNameResolverMock;
	@Mock
	AdapterContextContainer adapterContextContainerMock;

	@Mock Player playerMock;
	@Mock Entity entityMock;
	@Mock World worldMock;


	@Nested
	class PlayerDisplayNameAdapterTests
	{
		@Test @DisplayName("Get DisplayName with valid Player.")
		public void testGetDisplayName_withValidPlayer()
		{
			// Arrange
			when(playerMock.getDisplayName()).thenReturn("&aPlayer One Display Name");

			// Act
			Optional<DisplayNameable> adapter = new DisplayNameAdapter(adapterContextContainerMock).adapt(playerMock);
			Optional<String> displayName = adapter.map(DisplayNameable::getDisplayName);

			// Assert
			assertTrue(displayName.isPresent());
			assertEquals("&aPlayer One Display Name", displayName.get(), "The adapter should return the displayName from the Player.");
		}


		@Test
		public void testGetDisplayName_withNullPlayer()
		{
			// Arrange & Act
			Optional<DisplayNameable> adapter = new DisplayNameAdapter(adapterContextContainerMock).adapt(null);
			Optional<String> displayName = adapter.map(DisplayNameable::getDisplayName);

			// Assert
			assertTrue(displayName.isEmpty(), "The adapter should return an empty string for a null Player.");
		}


		@Test @DisplayName("Test constructor with null player.")
		public void testConstructor_withNullPlayer()
		{
			// Arrange & Act
			Optional<DisplayNameable> adapter = new DisplayNameAdapter(adapterContextContainerMock).adapt(null);
			Optional<String> displayName = adapter.map(DisplayNameable::getDisplayName);

			// assert
			assertTrue(displayName.isEmpty());
		}
	}


	@Nested @DisplayName("DisplayNameable Tests for Entity")
	class EntityDisplayNameableTests
	{
		@Test @DisplayName("Get DisplayName with valid Entity.")
		void testGetDisplayName_withValidEntity()
		{
			// Arrange
			when(entityMock.getCustomName()).thenReturn("Custom Entity Name");

			// Act
			Optional<DisplayNameable> adapter = new DisplayNameAdapter(adapterContextContainerMock).adapt(entityMock);
			Optional<String> displayName = adapter.map(DisplayNameable::getDisplayName);

			// Assert
			assertTrue(displayName.isPresent());
			assertEquals("Custom Entity Name", displayName.get(), "The adapter should return the displayName from the Entity.");
		}
	}


	@Nested @DisplayName("DisplayNameable Tests for World.")
	class WorldDisplayNameableTests
	{
		@Test
		void testGetDisplayName_withValidWorld()
		{
			when(worldNameResolverMock.resolveWorldName(worldMock)).thenReturn("Resolved World Name");
			when(adapterContextContainerMock.worldNameResolver()).thenReturn(worldNameResolverMock);

			DisplayNameAdapter adapter = new DisplayNameAdapter(adapterContextContainerMock);
			Optional<DisplayNameable> adapted = adapter.adapt(worldMock);

			assertTrue(adapted.isPresent());
			assertEquals("Resolved World Name", adapted.get().getDisplayName());
		}
	}

}
