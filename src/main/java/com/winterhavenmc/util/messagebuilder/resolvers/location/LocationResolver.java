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


/**
 * Resolver for Locatable objects with an associated location. Any object that has a known method
 * for retrieving a {@code Location} will be returned as a Locatable object type, with a
 * getLocation method. This method will be mapped to the actual method of the object that returns a
 * {@code Location}, regardless of its actual method name. Any object that is not known to have a
 * location will result in an empty {@code Optional} being returned from the {@code asLocatable} method.
 */
public class LocationResolver {

	/**
	 * Static method that returns an {@link Optional} of {@code Locatable}, or an empty Optional if the passed
	 * object is not known to have an associated location. The Optional value, if present, implements the
	 * {@code Locatable} Interface, and is guaranteed to have a {@code getLocation()} method.
	 *
	 * @param obj the object being evaluated as being Locatable
	 * @return an Optional of the object as a {@code Locatable}, or an empty {@code Optional} if the passed
	 * object does not have a known method of retrieving a location.
	 */
	public static Optional<Locatable> asLocatable(Object obj) {
		return switch (obj) {
			case Location location -> Optional.of(location::clone);
			case Entity entity -> Optional.of(entity::getLocation);
			case Block block -> Optional.of(block::getLocation);
			case BlockState blockState -> Optional.of(blockState::getLocation);
			case DoubleChest doubleChest -> Optional.of(doubleChest::getLocation);
			case null, default -> Optional.empty();
		};
	}

}
