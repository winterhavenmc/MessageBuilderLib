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


/**
 * A {@link WorldNameRetriever} implementation that retrieves the alias name of a
 * {@link org.bukkit.World} using the <strong>Multiverse-Core</strong> API.
 * <p>
 * This class interacts directly with {@link MultiverseCore} and its {@code MVWorldManager}
 * to look up world metadata, including alias and display formatting.
 * <p>
 * If the world is not managed by Multiverse, or if any part of the plugin's world
 * management system is unavailable, this retriever returns {@code null}.
 * <p>
 * Typically used internally by {@link MultiverseWorldNameResolver}, and not intended
 * to be used directly unless fine-grained access to Multiverse aliases is needed.
 *
 * @see WorldNameRetriever
 * @see MultiverseWorldNameResolver
 * @see MultiverseCore
 * @see org.bukkit.World
 */
public class MultiverseWorldNameRetriever implements WorldNameRetriever
{
	private final MultiverseCore multiverseCore;


	/**
	 * Constructs a {@code MultiverseWorldNameRetriever} with the given Multiverse-Core instance.
	 *
	 * @param multiverseCore the active {@link MultiverseCore} plugin instance
	 */
	public MultiverseWorldNameRetriever(MultiverseCore multiverseCore)
	{
		this.multiverseCore = multiverseCore;
	}


	/**
	 * Attempts to retrieve the alias name of the given {@link org.bukkit.World}
	 * using the Multiverse world manager.
	 * <p>
	 * If the world is not managed by Multiverse, or if plugin internals are unavailable,
	 * this method returns {@code null}.
	 *
	 * @param world the Bukkit world to retrieve an alias for
	 * @return the world alias from Multiverse, or {@code null} if unavailable
	 */
	@Override
	public String getWorldName(World world)
	{
		if (world == null || multiverseCore == null || multiverseCore.getMVWorldManager() == null)
		{
			return null;
		}
		return multiverseCore.getMVWorldManager().getMVWorld(world).getColoredWorldString();
	}

}
