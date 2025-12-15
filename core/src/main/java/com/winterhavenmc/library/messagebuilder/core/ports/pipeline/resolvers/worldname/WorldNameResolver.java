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

package com.winterhavenmc.library.messagebuilder.core.ports.pipeline.resolvers.worldname;

import java.util.UUID;


/**
 * A runtime-resolvable strategy interface for obtaining a display-friendly name
 * for a {@link org.bukkit.World}, optionally integrating with external plugins
 * such as <strong>Multiverse-Core</strong>.
 * <p>
 * This interface builds upon {@link WorldNameRetriever} by providing a static
 * factory method that selects an appropriate implementation based on the server
 * environment at runtime.
 * <p>
 * When {@code Multiverse-Core} is installed and enabled, the returned
 * implementation uses Multiverse APIs to obtain world aliases. Otherwise,
 * a fallback implementation is used that simply returns the default
 * world name from {@link org.bukkit.World#getName()}.
 *
 * @see WorldNameRetriever
 * @see org.bukkit.World
 */
@FunctionalInterface
public interface WorldNameResolver
{
	/**
	 * Resolves the user-facing name of the given {@link org.bukkit.World}, using
	 * either the native Bukkit name or a plugin-provided alias.
	 *
	 * @param worldUid the {@link UUID} of the world whose name is to be resolved
	 * @return the display or alias name for the world
	 */
	String resolve(UUID worldUid);
}
