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

import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.resolvers.spawnlocation.SpawnLocationResolver;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.util.Optional;


public sealed interface BukkitSpawnLocationResolver extends SpawnLocationResolver permits DefaultResolver, PluginBasedResolver
{
	Optional<Location> resolve(World world);


	static BukkitSpawnLocationResolver get(final PluginManager pluginManager)
	{
		if (pluginManager == null)
		{
			return new DefaultResolver();
		}

		Plugin plugin = pluginManager.getPlugin("Multiverse-Core");

		return (plugin != null && plugin.isEnabled())
				? new PluginBasedResolver(plugin)
				: new DefaultResolver();
	}

}
