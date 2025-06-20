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

package com.winterhavenmc.library.messagebuilder.pipeline.adapters.location;

import com.winterhavenmc.library.messagebuilder.pipeline.adapters.Adapter;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Raid;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.Inventory;
import org.bukkit.loot.LootContext;

import java.util.Optional;


/**
 * Adapter for objects that expose a {@link org.bukkit.Location}, either directly or via a {@link Locatable} interface.
 *
 * <p>This adapter provides macro key support for structured location data, including:
 * <ul>
 *   <li>{@code [OBJECT.LOCATION}} – the full location string, including world and coordinates</li>
 *   <li>{@code [OBJECT.LOCATION.WORLD}}, {@code [OBJECT.LOCATION.X}}, {@code Y}, {@code Z} – individual components</li>
 * </ul>
 *
 * <p>The world name is resolved using the
 * {@link com.winterhavenmc.library.messagebuilder.pipeline.resolvers.worldname.WorldNameResolver WorldNameResolver}
 * if Multiverse is installed and enabled, allowing server-defined aliases to appear in resolved messages.
 *
 * <p>This adapter supports the following object types:
 * <ul>
 *   <li>{@code Locatable} – objects implementing the {@link Locatable} interface</li>
 *   <li>{@link org.bukkit.entity.Entity}</li>
 *   <li>{@link org.bukkit.OfflinePlayer}</li>
 *   <li>{@link org.bukkit.block.Block} and {@link org.bukkit.block.BlockState}</li>
 *   <li>{@link org.bukkit.block.DoubleChest}</li>
 *   <li>{@link org.bukkit.inventory.Inventory}</li>
 *   <li>{@code Raid}</li>
 *   <li>{@link org.bukkit.Location} itself</li>
 * </ul>
 *
 * <p>Each type is adapted to a {@code Locatable} using a method reference that returns the underlying {@code Location}.
 *
 * @see Locatable
 * @see com.winterhavenmc.library.messagebuilder.pipeline.adapters.Adapter Adapter
 * @see com.winterhavenmc.library.messagebuilder.pipeline.resolvers.worldname.WorldNameResolver WorldNameResolver
 */
public class LocationAdapter implements Adapter
{
	@Override
	public Optional<Locatable> adapt(final Object obj)
	{
		return switch (obj)
		{
			case Locatable locatable -> Optional.of(locatable);
			case Entity entity -> Optional.of(entity::getLocation);
			case OfflinePlayer offlinePlayer -> Optional.of((offlinePlayer::getLocation));
			case Block block -> Optional.of(block::getLocation);
			case BlockState blockState -> Optional.of(blockState::getLocation);
			case DoubleChest doubleChest -> Optional.of(doubleChest::getLocation);
			case Inventory inventory -> Optional.of(inventory::getLocation);
			case Raid raid -> Optional.of(raid::getLocation);
			case LootContext lootContext -> Optional.of(lootContext::getLocation);
			case Location location -> Optional.of(location::clone);
			case null, default -> Optional.empty();
		};
	}

}
