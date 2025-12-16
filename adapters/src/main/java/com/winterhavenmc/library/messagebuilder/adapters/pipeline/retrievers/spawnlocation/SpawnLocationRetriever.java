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

package com.winterhavenmc.library.messagebuilder.adapters.pipeline.retrievers.spawnlocation;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.mvplugins.multiverse.core.MultiverseCore;

import java.util.Optional;


public interface SpawnLocationRetriever
{
	Optional<Location> getSpawnLocation(World world);


	static SpawnLocationRetriever create(final Plugin plugin)
	{
		return (plugin instanceof MultiverseCore mvPlugin && mvPlugin.isEnabled())
				? new MultiverseSpawnLocationRetriever()
				: new DefaultSpawnLocationRetriever();
	}
}
