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

package com.winterhavenmc.library.messagebuilder.adapters.pipeline.resolvers.spawnlocation;

import com.winterhavenmc.library.messagebuilder.adapters.pipeline.retrievers.spawnlocation.DefaultRetriever;
import com.winterhavenmc.library.messagebuilder.adapters.pipeline.retrievers.spawnlocation.SpawnLocationRetriever;
import org.bukkit.Location;
import org.bukkit.World;


public final class DefaultResolver implements BukkitSpawnLocationResolver
{
	/**
	 * Returns the default Bukkit world name by calling {@link World#getName()}.
	 *
	 * @param world the {@link World} to resolve a name for
	 * @return the world's raw name as defined by Bukkit
	 */
	@Override
	public Location resolve(final World world)
	{
		if (world == null) { return null; }

		SpawnLocationRetriever retriever = new DefaultRetriever();

		return retriever.getSpawnLocation(world).orElse(null);
	}

}
