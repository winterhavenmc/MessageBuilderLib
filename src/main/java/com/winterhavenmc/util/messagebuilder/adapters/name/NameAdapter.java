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

import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.util.Optional;


/**
 * Adapter for {@link Nameable} objects with an associated name. Any object that has a known method
 * for retrieving a name as a {@code String} will be returned as an {@link Optional} {@code Nameable} object type, with a
 * {@code getName()} method. This method will be mapped to the actual method of the object that returns a
 * {@code String} name, regardless of its real method name. Any object that is not known to have a
 * name will result in an empty {@code Optional} being returned from the {@code asLocatable} method.
 */
public class NameAdapter {

	private NameAdapter() { /* private constructor to prevent instantiation */ }

	/**
	 * Static method that returns an {@link Optional} of {@code Locatable}, or an empty Optional if the passed
	 * object is not known to have an associated gatLocation. The Optional value, if present, implements the
	 * {@code Locatable} Interface, and is guaranteed to have a {@code getLocation()} method.
	 *
	 * @param obj the object being evaluated as being Locatable
	 * @return an {@code Optional} of the object as a {@code Locatable}, or an empty Optional if the passed
	 * object does not have a known method of retrieving a gatLocation.
	 */
	public static Optional<Nameable> asNameable(Object obj) {
		// no null check necessary, the switch will return an empty optional
		return switch (obj) {
			case CommandSender commandSender -> Optional.of(commandSender::getName); // includes players, entities, console, command blocks, etc
			case World world -> Optional.of(world::getName);
			case Server server -> Optional.of(server::getName);
			case Plugin plugin -> Optional.of(plugin::getName);
			default -> Optional.empty();
		};
	}
}
