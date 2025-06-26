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


/**
 * A {@link WorldNameResolver} implementation that retrieves world name aliases
 * using the <strong>Multiverse-Core</strong> plugin.
 * <p>
 * If an alias is found for the given {@link World}, it is returned as the
 * display name. If no alias is available (e.g., null or blank), the resolver
 * falls back to the default world name from {@link World#getName()}.
 * <p>
 * If the input world is {@code null}, this resolver returns the string
 * {@code "NULL WORLD"}.
 *
 * <p>This class is only instantiated when <strong>Multiverse-Core</strong> is detected and enabled
 * at runtime. Use {@link WorldNameResolver#get(PluginManager)} to safely select
 * the appropriate implementation.
 *
 * @see WorldNameResolver
 * @see Multiverse4Retriever
 * @see DefaultResolver
 * @see World
 * @see MultiverseCore
 */
public final class PluginResolver implements WorldNameResolver
{
	private final Plugin plugin;


	/**
	 * Constructs a {@code MultiverseV4WorldNameResolver} using the given instance
	 * of {@link MultiverseCore}.
	 *
	 * @param plugin the active Multiverse-Core plugin instance
	 */
	public PluginResolver(Plugin plugin)
	{
		this.plugin = plugin;
	}


	/**
	 * Attempts to retrieve the alias name of the specified world using
	 * {@link Multiverse4Retriever}. If the alias is null or blank,
	 * falls back to {@code world.getName()}.
	 *
	 * @param world the {@link World} whose alias or name should be returned
	 * @return the Multiverse alias if available, otherwise the world name;
	 *         or {@code "NULL WORLD"} if the world is {@code null}
	 */
	@Override
	public String resolve(final World world)
	{
		if (world == null) { return "NULL"; }

		WorldNameRetriever retriever = switch (plugin)
		{
			case com.onarandombox.MultiverseCore.MultiverseCore mvPlugin -> new Multiverse4Retriever(mvPlugin);
			case org.mvplugins.multiverse.core.MultiverseCore mvPlugin -> new Multiverse5Retriever(mvPlugin);
			default -> new DefaultRetriever();
		};

		final String result = retriever.getWorldName(world);

		return (result != null && !result.isBlank())
				? result
				: world.getName();
	}

}
