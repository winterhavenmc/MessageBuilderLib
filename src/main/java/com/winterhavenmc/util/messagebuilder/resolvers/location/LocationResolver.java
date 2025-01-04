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

import java.util.Optional;


public class LocationResolver {
	public static Optional<Locatable> asLocatable(Object obj) {
		return switch (obj) {
			case Location location -> Optional.of(new LocationObjectLocationResolver(location));
			case Entity entity -> Optional.of(entity::getLocation);
			case Block block -> Optional.of(block::getLocation);
			case BlockState blockState -> Optional.of(blockState::getLocation);
			case DoubleChest doubleChest -> Optional.of(doubleChest::getLocation);
			case null, default -> Optional.empty();
		};
	}

	public record LocationObjectLocationResolver(Location location) implements Locatable { }
}
