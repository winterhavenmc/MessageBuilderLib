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

package com.winterhavenmc.util.messagebuilder.resolvers.uuid;

import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.profile.PlayerProfile;


/**
 * Resolver for Identifiable objects with an associated UUID. Any object that has a known method for retrieving
 * a UUID will be returned as an Identifiable object type, with a getUniqueID method. This method will be mapped
 * to the actual method of the object that returns a UUID, regardless of its signature. Any object that is not
 * known to have a UUID will result in a null being returned from the asIdentifiable method.
 */
public class UUIDResolver {

	/**
	 * Static method that returns an object of type Identifiable, or null if the passed object is not known to have
	 * an associated UUID.
	 *
	 * @param obj the object being evaluated as being Identifiable
	 * @return the object, wrapped in a Identifiable type, with its method to retrieve a UUID mapped to
	 * the getUniqueId() method of the Identifiable type.
	 */
	public static Identifiable asIdentifiable(Object obj) {
		return switch (obj) {
			case Entity entity -> entity::getUniqueId;
			case PlayerProfile playerProfile -> playerProfile::getUniqueId;
			case OfflinePlayer offlinePlayer -> offlinePlayer::getUniqueId;
			case World world -> world::getUID;
			case null, default -> null;
		};
	}
}
