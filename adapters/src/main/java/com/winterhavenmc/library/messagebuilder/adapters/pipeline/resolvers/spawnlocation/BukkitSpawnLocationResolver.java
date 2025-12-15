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

import com.winterhavenmc.library.messagebuilder.adapters.pipeline.retrievers.spawnlocation.SpawnLocationRetriever;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.resolvers.spawnlocation.SpawnLocationResolver;

import org.bukkit.Location;
import org.bukkit.World;

import java.util.Optional;


public final class BukkitSpawnLocationResolver implements SpawnLocationResolver
{
	private final SpawnLocationRetriever retriever;


	/**
	 * Constructor
	 */
	private BukkitSpawnLocationResolver(final SpawnLocationRetriever retriever)
	{
		this.retriever = retriever;
	}


	/**
	 * Static factory method
	 */
	public static SpawnLocationResolver create(final SpawnLocationRetriever spawnLocationRetriever)
	{
		return new BukkitSpawnLocationResolver(spawnLocationRetriever);
	}


	/**
	 * Returns the default Bukkit world spawn location by calling {@link World#getSpawnLocation()}.
	 *
	 * @param world the {@link World} to resolve a location for the world's spawn
	 * @return Optional containing the world's spawn location as defined by Bukkit
	 */
	public Optional<Location> resolve(final World world)
	{
		return retriever.getSpawnLocation(world);
	}

}
