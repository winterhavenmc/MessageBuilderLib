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

import org.bukkit.Nameable;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Optional;


/**
 * Adapter for DisplayNameable objects with an associated display name. Any object that has a known method
 * for retrieving a {@code String} display name will be returned as an {@link Optional} DisplayNameable object type, with a
 * {@code getDisplayName()} method. This method will be mapped to the method of the object that returns a
 * {@code String} display name, regardless of its actual method name. Any object that is not known to have a
 * display name will result in an empty {@code Optional} being returned from the static {@code asDisplayNameable} method.
 */
public class DisplayNameAdapter {

	private DisplayNameAdapter() { /* private constructor to prevent instantiation */ }

	/**
	 * Static method that returns an {@link Optional} of {@code DisplayNameable}, or an empty Optional if the passed
	 * object is not known to have an associated display name. The Optional value, if present, implements the
	 * {@code DisplayNameable} Interface, and is guaranteed to have a {@code getDisplayName()} method.
	 *
	 * @param obj the object being evaluated as to be DisplayNameable
	 * @return {@code Optional} of the object as a {@code DisplayNameable}, or an empty Optional if the passed
	 * object does not have a known method of retrieving a display name.
	 */
	public static Optional<DisplayNameable> asDisplayNameable(Object obj) {
		// no null check necessary, the switch will return an empty optional
		return switch (obj) {
			case Player player -> Optional.of(player::getDisplayName);
			case Nameable nameable -> Optional.of(nameable::getCustomName);
			case World world -> Optional.of(world::getName); //TODO: get Multiverse alias for world DisplayName, else use regular name
			default -> Optional.empty();
		};
	}
}
