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

package com.winterhavenmc.util.messagebuilder.adapters.name;

import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import org.bukkit.profile.PlayerProfile;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
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


	@Nested
	class CommandSenderNameAdapterTests
	{
		@Test
		public void testGetDisplayName_withValidCommandSender()
		{
			// Arrange
			when(commandSenderMock.getName()).thenReturn("Command Sender Name");
			String name = "";

			// Act
			Optional<Nameable> adapter = new NameAdapter().adapt(commandSenderMock);
			if (adapter.isPresent())
			{
				name = adapter.get().getName();
			}

			// Assert
			assertEquals("Command Sender Name", name, "The adapter should return the name from the CommandSender.");

			verify(commandSenderMock, atLeastOnce()).getName();
		}

		@Test
		public void testGetName_withNull()
		{
			// Arrange
			String name = null;

			// Act
			Optional<Nameable> adapter = new NameAdapter().adapt(null);
			if (adapter.isPresent())
			{
				name = adapter.get().getName();
			}
			// Assert
			assertNull(name, "The adapter should return an empty string for a null Player.");
		}
	}


	@Test
	public void testGetDisplayName_withValidOfflinePlayer()
	{
		// Arrange
		when(offlinePlayerMock.getName()).thenReturn("Offline Player Name");
		String name = "";

		// Act
		Optional<Nameable> adapter = new NameAdapter().adapt(offlinePlayerMock);
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
		Optional<Nameable> adapter = new NameAdapter().adapt(playerProfileMock);
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
		void testGetName_withValidWorld()
		{
			// Arrange
			when(worldMock.getName()).thenReturn("world_name");
			String name = "";

			// Act
			Optional<Nameable> adapter = new NameAdapter().adapt(worldMock);
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
			Optional<Nameable> adapter = new NameAdapter().adapt(serverMock);
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
			Optional<Nameable> adapter = new NameAdapter().adapt(pluginMock);
			if (adapter.isPresent())
			{
				name = adapter.get().getName();
			}

			// Assert
			assertEquals("Plugin Name", name, "The adapter should return the name from the Plugin.");
		}

	}

}
