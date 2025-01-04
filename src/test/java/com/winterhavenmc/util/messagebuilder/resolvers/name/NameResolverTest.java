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

package com.winterhavenmc.util.messagebuilder.resolvers.name;

import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

public class NameResolverTest {

	@Nested
	class CommandSenderNameResolverTests {
		@Test
		public void testGetDisplayName_withValidCommandSender() {
			// Arrange
			CommandSender commandSenderMock = mock(CommandSender.class, "MockCommandSender");
			when(commandSenderMock.getName()).thenReturn("Command Sender Name");
			String name = "";

			// Act
			Nameable resolver = NameResolver.asNameable(commandSenderMock);
			if (resolver != null) {
				name = resolver.getName();
			}

			// Assert
			assertEquals("Command Sender Name", name, "The resolver should return the name from the CommandSender.");

			verify(commandSenderMock, atLeastOnce()).getName();
		}

		@Test
		public void testGetName_withNull() {
			// Arrange
			Player nullPlayer = null;
			String name = null;

			// Act
			Nameable resolver = NameResolver.asNameable(nullPlayer);
			if (resolver != null) {
				name = resolver.getName();
			}

			// Assert
			assertNull(name, "The resolver should return an empty string for a null Player.");
		}
	}


	@Nested
	class EntityNameableTests {

		@Test
		void testGetName_withValidWorld() {
			// Arrange
			World worldMock = mock(World.class, "MockWorld");
			when(worldMock.getName()).thenReturn("world_name");
			String name = "";

			// Act
			Nameable resolver = NameResolver.asNameable(worldMock);
			if (resolver != null) {
				name = resolver.getName();
			}

			// Assert
			assertEquals("world_name", name, "The resolver should return the name from the World.");
		}
	}

	@Nested
	class ServerNameableTests {

		@Test
		void testGetDisplayName_withValidServer() {
			// Arrange
			Server serverMock = mock(Server.class, "MockServer");
			when(serverMock.getName()).thenReturn("Server Name");
			String name = "";

			// Act
			Nameable resolver = NameResolver.asNameable(serverMock);
			if (resolver != null) {
				name = resolver.getName();
			}

			// Assert
			assertEquals("Server Name", name, "The resolver should return the name from the Server.");
		}
	}

	@Nested
	class PluginNameableTests {

		@Test
		void testGetDisplayName_withValidPlugin() {
			// Arrange
			Plugin pluginMock = mock(Plugin.class, "MockPlugin");
			when(pluginMock.getName()).thenReturn("Plugin Name");
			String name = "";

			// Act
			Nameable resolver = NameResolver.asNameable(pluginMock);
			if (resolver != null) {
				name = resolver.getName();
			}

			// Assert
			assertEquals("Plugin Name", name, "The resolver should return the name from the Plugin.");
		}
	}

}
