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

package com.winterhavenmc.util.messagebuilder.resolvers.location;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.Entity;


public class LocationResolver {
	public static Locatable asLocatable(Object obj) {
		return switch (obj) {
			case Location location -> new LocationObjectLocationResolver(location);
			case Entity entity -> entity::getLocation;
			case Block block -> block::getLocation;
			case BlockState blockState -> blockState::getLocation;
			case DoubleChest doubleChest -> doubleChest::getLocation;
			case null, default -> null;
		};
	}

	public static record LocationObjectLocationResolver(Location location) implements Locatable { }
}
