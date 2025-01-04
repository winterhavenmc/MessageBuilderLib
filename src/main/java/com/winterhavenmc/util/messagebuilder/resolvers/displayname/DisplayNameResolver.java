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

import org.bukkit.Nameable;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Optional;

public class DisplayNameResolver {

	public static Optional<DisplayNameable> asDisplayNameable(Object obj) {
		return switch (obj) {
			case Player player -> Optional.of(player::getDisplayName);
			case Nameable nameable -> Optional.of(nameable::getCustomName);
			case World world -> Optional.of(world::getName); //TODO: get Multiverse alias for world DisplayName, else use regular name
			case null, default -> Optional.empty();
		};
	}
}
