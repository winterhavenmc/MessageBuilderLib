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

package com.winterhavenmc.util.messagebuilder.adapters.uuid;

import com.winterhavenmc.util.messagebuilder.adapters.Adapter;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.profile.PlayerProfile;

import java.util.Optional;


/**
 * Resolver for Identifiable objects with an associated UUID. Any object that has a known method for retrieving
 * a UUID will be returned as an Identifiable object type, with a getUniqueID method. This method will be mapped
 * to the actual method of the object that returns a UUID, regardless of its method name. Any object that is not
 * known to have a UUID will result in an empty {@code Optional} being returned from the asIdentifiable method.
 */
public class UniqueIdAdapter implements Adapter<Identifiable>
{
	/**
	 * Static method that returns an object of type Identifiable, or null if the passed object is not known to have
	 * an associated UUID.
	 *
	 * @param obj the object being evaluated as being Identifiable
	 * @return the object, wrapped in a Identifiable type, with its method to retrieve a UUID mapped to
	 * the getUniqueId() method of the Identifiable type.
	 */
	@Override
	public Optional<Identifiable> adapt(final Object obj)
	{
		return switch (obj)
		{
			case Entity entity -> Optional.of(entity::getUniqueId);
			case PlayerProfile playerProfile -> Optional.of(playerProfile::getUniqueId);
			case OfflinePlayer offlinePlayer -> Optional.of(offlinePlayer::getUniqueId);
			case World world -> Optional.of(world::getUID);
			case null, default -> Optional.empty();
		};
	}


	@Override
	public Class<Identifiable> getInterface()
	{
		return Identifiable.class;
	}

}
