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
import org.bukkit.plugin.Plugin;

import java.util.Optional;

public class NameResolver {

	public static Optional<Nameable> asNameable(Object obj) {
		return switch (obj) {
			case CommandSender commandSender -> Optional.of(commandSender::getName); // includes players, entities, console, command blocks, etc
			case World world -> Optional.of(world::getName);
			case Server server -> Optional.of(server::getName);
			case Plugin plugin -> Optional.of(plugin::getName);
			case null, default -> Optional.empty();
		};
	}
}
