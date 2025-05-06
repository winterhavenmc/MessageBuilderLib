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

package com.winterhavenmc.library.messagebuilder.pipeline.adapters.name;

import com.winterhavenmc.library.messagebuilder.pipeline.adapters.Adapter;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.profile.PlayerProfile;

import java.util.Optional;


/**
 * Adapter for {@link Nameable} objects with an associated name. Any object that has a known method
 * for retrieving a name as a {@code String} will be returned as an {@link Optional} {@code Nameable} object type,
 * with a {@code getName()} method. This method will be mapped to the actual method of the object that returns a
 * {@code String} name, regardless of its real method name. Any object that is not known to have a
 * name will result in an empty {@code Optional} being returned from the {@code asLocatable} method.
 */
public class NameAdapter implements Adapter
{
	/**
	 * Return an {@link Optional} of {@code Nameable}, or an empty Optional if the passed
	 * object is not known to have an associated getName method. The Optional value, if present,
	 * implements the {@code Nameable} Interface, and is guaranteed to have a {@code getName()} method
	 * that maps to the adapted type's underlying {@code getName()} method.
	 *
	 * @param obj the object being evaluated as being {@code Nameable}
	 * @return an {@code Optional} of the object as a Nameable type, or an empty Optional if the passed
	 * object does not have a known method of retrieving a name.
	 */
	@Override
	public Optional<Nameable> adapt(final Object obj)
	{
		return switch (obj)
		{
			case Nameable nameable -> Optional.of(nameable);
			case CommandSender commandSender -> Optional.of(commandSender::getName);
			case PlayerProfile playerProfile -> Optional.of(playerProfile::getName);
			case World world -> Optional.of(world::getName);
			case Server server -> Optional.of(server::getName);
			case Plugin plugin -> Optional.of(plugin::getName);
			case null, default -> Optional.empty();
		};
	}


	@Override
	public boolean supports(Object value) {
		return value instanceof Nameable;
	}

}
