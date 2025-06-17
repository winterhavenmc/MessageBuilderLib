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
import org.bukkit.plugin.PluginManager;


/**
 * A {@link WorldNameResolver} implementation that retrieves world name aliases
 * using the {@code Multiverse-Core} plugin.
 * <p>
 * If an alias is found for the given {@link org.bukkit.World}, it is returned as the
 * display name. If no alias is available (e.g., null or blank), the resolver
 * falls back to the default world name from {@link org.bukkit.World#getName()}.
 * <p>
 * If the input world is {@code null}, this resolver returns the string
 * {@code "NULL WORLD"}.
 *
 * <p>This class is only instantiated when {@code Multiverse-Core} is detected and enabled
 * at runtime. Use {@link WorldNameResolver#getResolver(PluginManager)} to safely select
 * the appropriate implementation.
 *
 * @see WorldNameResolver
 * @see MultiverseWorldNameRetriever
 * @see DefaultWorldNameResolver
 * @see org.bukkit.World
 * @see MultiverseCore
 */
public class MultiverseWorldNameResolver implements WorldNameResolver
{
	private final MultiverseCore multiverseCore;


	/**
	 * Constructs a {@code MultiverseWorldNameResolver} using the given instance
	 * of {@link MultiverseCore}.
	 *
	 * @param multiverseCore the active Multiverse-Core plugin instance
	 */
	public MultiverseWorldNameResolver(MultiverseCore multiverseCore)
	{
		this.multiverseCore = multiverseCore;
	}


	/**
	 * Attempts to retrieve the alias name of the specified world using
	 * {@link MultiverseWorldNameRetriever}. If the alias is null or blank,
	 * falls back to {@code world.getName()}.
	 *
	 * @param world the {@link org.bukkit.World} whose alias or name should be returned
	 * @return the Multiverse alias if available, otherwise the world name;
	 *         or {@code "NULL WORLD"} if the world is {@code null}
	 */
	@Override
	public String resolveWorldName(final World world)
	{
		if (world == null) { return "NULL WORLD"; }

		String mvAlias = new MultiverseWorldNameRetriever(multiverseCore).getWorldName(world);
		return (mvAlias == null || mvAlias.isBlank())
				? world.getName()
				: mvAlias;
	}

}
