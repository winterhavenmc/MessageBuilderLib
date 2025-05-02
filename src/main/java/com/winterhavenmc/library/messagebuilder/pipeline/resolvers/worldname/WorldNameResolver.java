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

import com.onarandombox.MultiverseCore.MultiverseCore;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;


public interface WorldNameResolver
{
	String resolveWorldName(World world);


	static WorldNameResolver getResolver(final PluginManager pluginManager)
	{
		Plugin plugin = pluginManager.getPlugin("Multiverse-Core");

		return (plugin instanceof MultiverseCore multiverseCore && plugin.isEnabled())
				? new MultiverseWorldNameResolver(multiverseCore)
				: new DefaultWorldNameResolver();
	}

}
