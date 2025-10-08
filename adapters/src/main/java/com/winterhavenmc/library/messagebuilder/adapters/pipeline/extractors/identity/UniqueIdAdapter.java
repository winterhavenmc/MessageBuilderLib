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

package com.winterhavenmc.library.messagebuilder.adapters.pipeline.extractors.identity;

import com.winterhavenmc.library.messagebuilder.core.pipeline.adapters.Adapter;
import com.winterhavenmc.library.messagebuilder.core.pipeline.adapters.identity.Identifiable;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.profile.PlayerProfile;

import java.util.Optional;
import java.util.UUID;


/**
 * Adapter for identifying objects with a unique {@link UUID}.
 *
 * <p>This adapter supports Bukkit types that expose UUIDs directly, including:
 * <ul>
 *     <li>{@link org.bukkit.entity.Entity}</li>
 *     <li>{@link org.bukkit.OfflinePlayer}</li>
 *     <li>{@link org.bukkit.profile.PlayerProfile}</li>
 *     <li>{@link org.bukkit.World}</li>
 * </ul>
 *
 * <p>It also supports any object implementing the {@link Identifiable} interface.</p>
 */
public class UniqueIdAdapter implements Adapter
{
	/**
	 * Attempts to adapt the given object to an {@link Identifiable} instance.
	 *
	 * <p>This method supports the following object types:
	 * <ul>
	 *     <li>{@link Identifiable} — returned directly</li>
	 *     <li>{@link org.bukkit.entity.Entity} — adapts via {@code getUniqueId()}</li>
	 *     <li>{@link org.bukkit.OfflinePlayer} — adapts via {@code getUniqueId()}</li>
	 *     <li>{@link org.bukkit.profile.PlayerProfile} — adapts via {@code getUniqueId()}</li>
	 *     <li>{@link org.bukkit.World} — adapts via {@code getUID()}</li>
	 * </ul>
	 *
	 * @param obj the object to adapt
	 * @return an {@code Optional} containing an {@code Identifiable} instance if the object is supported;
	 *         otherwise an empty {@code Optional}
	 */
	@Override
	public Optional<Identifiable> adapt(final Object obj)
	{
		return switch (obj)
		{
			case Identifiable identifiable -> Optional.of(identifiable);
			case Entity entity -> Optional.of(entity::getUniqueId);
			case PlayerProfile playerProfile -> Optional.of(playerProfile::getUniqueId);
			case OfflinePlayer offlinePlayer -> Optional.of(offlinePlayer::getUniqueId);
			case World world -> Optional.of(world::getUID);
			case null, default -> Optional.empty();
		};
	}

}
