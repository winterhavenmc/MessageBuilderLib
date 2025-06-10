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

package com.winterhavenmc.library.messagebuilder.pipeline.adapters.displayname;

import com.winterhavenmc.library.messagebuilder.pipeline.adapters.Adapter;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.AdapterContextContainer;
import com.winterhavenmc.library.messagebuilder.pipeline.resolvers.worldname.WorldNameResolver;
import org.bukkit.Nameable;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Optional;


/**
 * Adapter for DisplayNameable objects with an associated display name. Any object that has a known method
 * for retrieving a {@code String} display name will be returned as an {@link Optional} DisplayNameable object type, with a
 * {@code getDisplayName()} method. This method will be mapped to the method of the object that returns a
 * {@code String} display name, regardless of its actual method name. Any object that is not known to have a
 * display name will result in an empty {@code Optional} being returned from the static {@code asDisplayNameable} method.
 */
public class DisplayNameAdapter implements Adapter
{
	private final AdapterContextContainer ctx;


	/**
	 * Constructor for DisplayNameAdapter
	 *
	 * @param ctx a context container carrying injectable components for use by this class
	 */
	public DisplayNameAdapter(final AdapterContextContainer ctx)
	{
		this.ctx = ctx;
	}


	/**
	 * Adapt a type to conform to the {@link DisplayNameable} interface, making available a field with a value
	 * equivalent to a displayName, and making it available via the {@code getDisplayName()} method
	 * of the {@code DisplayNameable} interface
	 *
	 * @param obj the object to be adapted to the {@link DisplayNameable} interface
	 * @return an {@code Optional} containing an adapted typed, or an empty Optional if the type could not be adapted
	 */
	@Override
	public Optional<DisplayNameable> adapt(final Object obj)
	{
		return switch (obj)
		{
			case DisplayNameable displayNameable -> Optional.of(displayNameable);
			case Player player -> Optional.of(player::getDisplayName);
			case Nameable nameable -> Optional.of(nameable::getCustomName); // Note: this is bukkit Nameable, NOT MessageBuilder Nameable
			case World world -> Optional.of(() -> ctx.worldNameResolver().resolveWorldName(world));
			case null, default -> Optional.empty();
		};
	}

}
