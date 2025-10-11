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

package com.winterhavenmc.library.messagebuilder.adapters.pipeline.accessors.displayname;


import com.winterhavenmc.library.messagebuilder.core.context.AdapterCtx;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.displayname.DisplayNameable;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.resolvers.itemname.ItemDisplayNameResolver;

import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.resolvers.worldname.WorldNameResolver;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class DisplayNameAccessorTest
{
	@Mock WorldNameResolver worldNameResolverMock;
	@Mock AdapterCtx adapterContextContainerMock;
	@Mock Player playerMock;
	@Mock Entity entityMock;
	@Mock World worldMock;
	@Mock ItemStack itemStackMock;
	@Mock
	ItemDisplayNameResolver itemDisplayNameResolverMock;


	@Test @DisplayName("adapt with valid Player")
	public void adapt_with_valid_player()
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


	@Test @DisplayName("adapt with valid Entity")
	void getDisplayName_with_valid_entity()
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


	@Test @DisplayName("adapt with valid ItemStack")
	void getDisplayName_with_valid_ItemStack()
	{
		// Arrange
		when(adapterContextContainerMock.itemDisplayNameResolver()).thenReturn(itemDisplayNameResolverMock);
		when(itemDisplayNameResolverMock.resolve(itemStackMock)).thenReturn("Resolved ItemStack Display Name");

		// Act
		Optional<DisplayNameable> adapter = new DisplayNameAdapter(adapterContextContainerMock).adapt(itemStackMock);
		Optional<String> displayName = adapter.map(DisplayNameable::getDisplayName);

		// Assert
		assertTrue(displayName.isPresent());
		assertEquals("Resolved ItemStack Display Name", displayName.get(), "The adapter should return the displayName from the ItemStack.");
	}


	@Test @DisplayName("adapt with valid World")
	void getDisplayName_with_valid_world()
	{
		when(adapterContextContainerMock.worldNameResolver()).thenReturn(worldNameResolverMock);
		when(worldNameResolverMock.resolve(worldMock)).thenReturn("Resolved World Name");

		DisplayNameAdapter adapter = new DisplayNameAdapter(adapterContextContainerMock);
		Optional<DisplayNameable> adapted = adapter.adapt(worldMock);

		assertTrue(adapted.isPresent());
		assertEquals("Resolved World Name", adapted.get().getDisplayName());
	}


	@Test @DisplayName("adapt with null")
	public void adapt_with_null_object()
	{
		// Arrange & Act
		Optional<DisplayNameable> adapter = new DisplayNameAdapter(adapterContextContainerMock).adapt(null);
		Optional<String> displayName = adapter.map(DisplayNameable::getDisplayName);

		// Assert
		assertTrue(displayName.isEmpty(), "The adapter should return an empty string for a null Player.");
	}

}
