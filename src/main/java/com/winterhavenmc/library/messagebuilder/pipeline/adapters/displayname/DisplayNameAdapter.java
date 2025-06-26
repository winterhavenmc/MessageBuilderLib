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

import org.bukkit.Nameable;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Optional;


/**
 * Adapter implementation for extracting display names from supported object types.
 *
 * <p>This adapter converts objects into {@link DisplayNameable} instances to support macro
 * replacement using the {@code {OBJECT.DISPLAY_NAME}} placeholder.
 *
 * <p>Supported types include:
 * <ul>
 *     <li>Objects implementing {@link DisplayNameable}</li>
 *     <li>{@link Player} — mapped to {@link Player#getDisplayName()}</li>
 *     <li>{@link Nameable} — mapped to {@link Nameable#getCustomName()}</li>
 *     <li>{@link World} — mapped using the
 *     {@link com.winterhavenmc.library.messagebuilder.pipeline.resolvers.worldname.WorldNameResolver WorldNameResolver}</li>
 * </ul>
 *
 * @see DisplayNameable
 * @see Adapter
 */
public class DisplayNameAdapter implements Adapter
{
	private final AdapterContextContainer ctx;


	/**
	 * Constructs a new {@code DisplayNameAdapter} with the given context container.
	 *
	 * @param ctx the adapter context providing services such as world name resolution
	 */
	public DisplayNameAdapter(final AdapterContextContainer ctx)
	{
		this.ctx = ctx;
	}


	/**
	 * Attempts to adapt the given object to a {@link DisplayNameable} instance.
	 *
	 * @param obj the object to adapt
	 * @return an optional {@code DisplayNameable} if supported, or empty otherwise
	 */
	@Override
	public Optional<DisplayNameable> adapt(final Object obj)
	{
		return switch (obj)
		{
			case DisplayNameable displayNameable -> Optional.of(displayNameable);
			case Player player -> Optional.of(player::getDisplayName);
			case Nameable nameable -> Optional.of(nameable::getCustomName); // Note: this is bukkit Nameable, NOT MessageBuilder Nameable
			case World world -> Optional.of(() -> ctx.worldNameResolver().resolve(world));
			case null, default -> Optional.empty();
		};
	}

}
