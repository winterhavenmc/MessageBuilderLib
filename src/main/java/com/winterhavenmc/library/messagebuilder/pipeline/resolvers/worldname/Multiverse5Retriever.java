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
import org.bukkit.plugin.Plugin;
import org.mvplugins.multiverse.core.MultiverseCore;

import java.util.Optional;


/**
 * A {@link WorldNameRetriever} implementation that retrieves the alias name of a
 * {@link World} using the <strong>Multiverse-Core</strong> API.
 * <p>
 * This class interacts directly with {@link MultiverseCore} and its {@code WorldManager}
 * to look up world metadata, including alias and display formatting. This implementation
 * retrieves a world alias or name using Multiverse version 5.
 * <p>
 * If the world is not managed by Multiverse, or if any part of the plugin's world
 * management system is unavailable, this retriever returns {@code null}.
 * <p>
 * Typically used internally by {@link PluginResolver}, and not intended
 * to be used directly unless fine-grained access to Multiverse aliases is needed.
 *
 * @see WorldNameRetriever
 * @see PluginResolver
 * @see MultiverseCore
 * @see World
 */
public class Multiverse5Retriever implements WorldNameRetriever
{
	private final Plugin plugin;


	/**
	 * Constructs a {@code Multiverse4Retriever} with the given Multiverse-Core instance.
	 *
	 * @param plugin the active {@link MultiverseCore} plugin instance
	 */
	public Multiverse5Retriever(Plugin plugin)
	{
		this.plugin = plugin;
	}


	/**
	 * Attempts to retrieve the alias or name of the given {@link World}
	 * using the Multiverse world manager.
	 * <p>
	 * If the world is not managed by Multiverse, or if plugin internals are unavailable,
	 * this method returns {@code null}.
	 *
	 * @param world the Bukkit world to retrieve an alias for
	 * @return the world alias or name from Multiverse, or {@code null} if unavailable
	 */
	@Override
	public Optional<String> getWorldName(final World world)
	{
		return (world != null && plugin instanceof MultiverseCore)
				? Optional.of(((MultiverseCore) plugin).getApi().getWorldManager().getWorld(world).getOrNull().getAliasOrName())
				: Optional.empty();
	}

}
