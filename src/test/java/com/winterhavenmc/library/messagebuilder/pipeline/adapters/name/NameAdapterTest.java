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

package com.winterhavenmc.library.messagebuilder.pipeline.adapters.name;

import com.winterhavenmc.library.messagebuilder.pipeline.adapters.AdapterContextContainer;
import com.winterhavenmc.library.messagebuilder.pipeline.resolvers.itemname.ItemNameResolver;

import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import org.bukkit.profile.PlayerProfile;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class NameAdapterTest
{
	@Mock CommandSender commandSenderMock;
	@Mock PlayerProfile playerProfileMock;
	@Mock OfflinePlayer offlinePlayerMock;
	@Mock Server serverMock;
	@Mock World worldMock;
	@Mock Plugin pluginMock;
	@Mock AdapterContextContainer adapterContextContainerMock;
	@Mock ItemStack itemStackMock;
	@Mock ItemNameResolver itemNameResolverMock;



	@Nested
	class NameAdapterTests
	{
		@Test
		public void getName_with_Valid_CommandSender()
		{
			// Arrange
			when(commandSenderMock.getName()).thenReturn("Command Sender Name");
			String name = "";

			// Act
			Optional<Nameable> adapter = new NameAdapter(adapterContextContainerMock).adapt(commandSenderMock);
			if (adapter.isPresent())
			{
				name = adapter.get().getName();
			}

			// Assert
			assertEquals("Command Sender Name", name, "The adapter should return the name from the CommandSender.");

			verify(commandSenderMock, atLeastOnce()).getName();
		}

		@Test
		public void adapt_with_null_parameter_returns_empty_optional()
		{
			// Arrange
			String name = null;

			// Act
			Optional<Nameable> adapter = new NameAdapter(adapterContextContainerMock).adapt(null);
			if (adapter.isPresent())
			{
				name = adapter.get().getName();
			}

			// Assert
			assertNull(name, "The adapter should return an empty string for a null Player.");
		}
	}


	@Test
	public void testGetName_with_valid_OfflinePlayer()
	{
		// Arrange
		when(offlinePlayerMock.getName()).thenReturn("Offline Player Name");
		String name = "";

		// Act
		Optional<Nameable> adapter = new NameAdapter(adapterContextContainerMock).adapt(offlinePlayerMock);
		if (adapter.isPresent())
		{
			name = adapter.get().getName();
		}

		// Assert
		assertEquals("Offline Player Name", name, "The adapter should return the name from the OfflinePlayer.");

		verify(offlinePlayerMock, atLeastOnce()).getName();
	}


	@Test
	public void testGetDisplayName_with_playerProfile()
	{
		// Arrange
		when(playerProfileMock.getName()).thenReturn("Player Profile Name");
		String name = "";

		// Act
		Optional<Nameable> adapter = new NameAdapter(adapterContextContainerMock).adapt(playerProfileMock);
		if (adapter.isPresent())
		{
			name = adapter.get().getName();
		}

		// Assert
		assertEquals("Player Profile Name", name, "The adapter should return the name from the PlayerProfile.");

		verify(playerProfileMock, atLeastOnce()).getName();
	}


	@Nested
	class EntityNameableTests
	{
		@Test
		void testAdaptedField_withValidWorld()
		{
			// Arrange
			when(worldMock.getName()).thenReturn("world_name");
			String name = "";

			// Act
			Optional<Nameable> adapter = new NameAdapter(adapterContextContainerMock).adapt(worldMock);
			if (adapter.isPresent())
			{
				name = adapter.get().getName();
			}

			// Assert
			assertEquals("world_name", name, "The adapter should return the name from the World.");
		}
	}


	@Nested
	class ServerNameableTests
	{
		@Test
		void testGetDisplayName_withValidServer()
		{
			// Arrange
			when(serverMock.getName()).thenReturn("Server Name");
			String name = "";

			// Act
			Optional<Nameable> adapter = new NameAdapter(adapterContextContainerMock).adapt(serverMock);
			if (adapter.isPresent())
			{
				name = adapter.get().getName();
			}

			// Assert
			assertEquals("Server Name", name, "The adapter should return the name from the Server.");
		}
	}


	@Nested
	class PluginNameableTests
	{
		@Test
		void testGetDisplayName_withValidPlugin()
		{
			// Arrange
			when(pluginMock.getName()).thenReturn("Plugin Name");
			String name = "";

			// Act
			Optional<Nameable> adapter = new NameAdapter(adapterContextContainerMock).adapt(pluginMock);
			if (adapter.isPresent())
			{
				name = adapter.get().getName();
			}

			// Assert
			assertEquals("Plugin Name", name, "The adapter should return the name from the Plugin.");
		}

	}


	@Test @DisplayName("adapt with valid ItemStack")
	void getDisplayName_with_valid_ItemStack()
	{
		// Arrange
		when(adapterContextContainerMock.itemNameResolver()).thenReturn(itemNameResolverMock);
		when(itemNameResolverMock.resolve(itemStackMock)).thenReturn("Resolved ItemStack Name");

		// Act
		Optional<Nameable> adapter = new NameAdapter(adapterContextContainerMock).adapt(itemStackMock);
		Optional<String> displayName = adapter.map(Nameable::getName);

		// Assert
		assertTrue(displayName.isPresent());
		assertEquals("Resolved ItemStack Name", displayName.get(), "The adapter should return the name from the ItemStack.");
	}


}
