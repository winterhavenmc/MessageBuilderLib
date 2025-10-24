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

package com.winterhavenmc.library.messagebuilder.adapters.pipeline.resolvers.worldname;

import com.winterhavenmc.library.messagebuilder.adapters.pipeline.retrievers.worldname.DefaultRetriever;
import com.winterhavenmc.library.messagebuilder.adapters.pipeline.retrievers.worldname.MultiverseRetriever;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.resolvers.worldname.WorldNameResolver;

import org.bukkit.World;
import org.bukkit.plugin.Plugin;

import java.util.Optional;


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
 *<p>
 * TODO: edit this comment to reflect the removal of the get method
 * <p>This class is only instantiated when <strong>Multiverse-Core</strong> is detected and enabled
 * at runtime. Use {@code WorldNameResolver#get(PluginManager)} to safely select
 * the appropriate implementation.
 *
 * @see WorldNameResolver
 * @see DefaultResolver
 * @see World
 * @see org.mvplugins.multiverse.core.MultiverseCore MultiverseCore
 */
public final class MultiverseResolver implements WorldNameResolver
{
	private final Plugin mvPlugin;


	/**
	 * Constructs a {@code MultiverseV4WorldNameResolver} using the given instance
	 * of {@link org.mvplugins.multiverse.core.MultiverseCore}.
	 *
	 * @param mvPlugin the active Multiverse-Core plugin instance
	 */
	public MultiverseResolver(Plugin mvPlugin)
	{
		this.mvPlugin = mvPlugin;
	}


	/**
	 * Attempts to retrieve the alias name of the specified world using
	 * {@code Multiverse4Retriever}. If the alias is null or blank,
	 * falls back to {@code world.getName()}.
	 *
	 * @param world the {@link World} whose alias or name should be returned
	 * @return the Multiverse alias if available, otherwise the Bukkit world name,
	 * or {@code "∅"} (NULL symbol) if the world is {@code null}
	 */
	@Override
	public String resolve(final World world)
	{
		if (world == null) { return "∅"; }

		Optional<String> result = switch (mvPlugin)
		{
			case org.mvplugins.multiverse.core.MultiverseCore mv5Plugin -> new MultiverseRetriever(mv5Plugin).getWorldName(world);
			case null, default -> new DefaultRetriever().getWorldName(world);
		};

		return (result.isPresent() && !result.get().isBlank())
				? result.get()
				: world.getName();
	}

}
