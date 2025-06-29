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

package com.winterhavenmc.library.messagebuilder.pipeline.resolvers.worldname;

import org.bukkit.World;


/**
 * Default implementation of {@link WorldNameResolver} that returns the raw name
 * of a {@link org.bukkit.World} using {@code World#getName()}.
 * <p>
 * This resolver is used when no aliasing system (such as Multiverse) is available,
 * or when default Bukkit world names are preferred.
 * <p>
 * Example output values include {@code "world"}, {@code "world_nether"}, or
 * {@code "custom_world"}.
 *
 * @see WorldNameResolver
 * @see org.bukkit.World#getName()
 */
public class DefaultWorldNameResolver implements WorldNameResolver
{
	/**
	 * Returns the default Bukkit world name by calling {@link org.bukkit.World#getName()}.
	 *
	 * @param world the {@link org.bukkit.World} to resolve a name for
	 * @return the world's raw name as defined by Bukkit
	 */
	@Override
	public String resolveWorldName(final World world)
	{
		return world.getName();
	}
}
