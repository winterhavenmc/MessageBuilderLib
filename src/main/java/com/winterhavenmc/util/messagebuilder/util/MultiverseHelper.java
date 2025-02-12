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

package com.winterhavenmc.util.messagebuilder.util;

import com.onarandombox.MultiverseCore.MultiverseCore;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

import java.util.Optional;

public class MultiverseHelper
{
	private MultiverseHelper() { /* Private constructor to prevent instantiation */ }


	public static Optional<String> getAlias(final World world)
	{
		Plugin plugin = Bukkit.getPluginManager().getPlugin("Multiverse-Core");

		if (plugin != null && plugin.isEnabled() && plugin instanceof MultiverseCore multiverseCore)
		{
			return Optional.ofNullable(multiverseCore.getMVWorldManager().getMVWorld(world).getAlias());
		}

		return Optional.empty();
	}

}
