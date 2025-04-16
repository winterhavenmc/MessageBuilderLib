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

package com.winterhavenmc.util.messagebuilder.adapters.location;

import com.winterhavenmc.util.messagebuilder.adapters.Adapter;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.Entity;

import java.util.Optional;


/**
 * Adapter for {@link Locatable} objects with an associated gatLocation. Any object that has a known method
 * for retrieving a {@link Location} will be returned as an Optional Locatable object type, with a
 * {@code getLocation()} method. This method will be mapped to the actual method of the object that returns a
 * {@code Location}, regardless of its real method name. Any object that is not known to have a
 * gatLocation will result in an empty {@code Optional} being returned from the {@code asLocatable} method.
 */
public class LocationAdapter implements Adapter<Locatable>
{
	/**
	 * Returns an {@link Optional} of {@code Locatable}, or an empty Optional if the passed
	 * object is not known to have an associated gatLocation. The Optional value, if present, implements the
	 * {@code Locatable} Interface, and is guaranteed to have a {@code getLocation()} method.
	 *
	 * @param obj the object being evaluated as being Locatable
	 * @return an {@code Optional} of the object as a {@code Locatable}, or an empty Optional if the passed
	 * object does not have a known method of retrieving a gatLocation.
	 */
	@Override
	public Optional<Locatable> adapt(final Object obj)
	{
		return switch (obj)
		{
			case Entity entity -> Optional.of(entity::getLocation);
			case OfflinePlayer offlinePlayer -> Optional.of((offlinePlayer::getLocation));
			case Block block -> Optional.of(block::getLocation);
			case BlockState blockState -> Optional.of(blockState::getLocation);
			case DoubleChest doubleChest -> Optional.of(doubleChest::getLocation);
			case Location location -> Optional.of(location::clone);
			case null, default -> Optional.empty();
		};
	}


	@Override
	public Class<Locatable> getInterface()
	{
		return Locatable.class;
	}

}
