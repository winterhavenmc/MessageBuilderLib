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

import com.winterhavenmc.util.messagebuilder.validation.ValidationException;
import com.winterhavenmc.util.messagebuilder.validation.MessageKey;
import com.winterhavenmc.util.messagebuilder.validation.Parameter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

import java.util.Objects;
import java.util.Optional;

import static com.winterhavenmc.util.messagebuilder.validation.Validator.validate;


/**
 * A static helper class that provides a method to query Multiverse for a world alias if present.
 */
public class MultiverseHelper
{
	/**
	 * Class constructor
	 */
	private MultiverseHelper() { /* Private constructor to prevent instantiation */ }


	/**
	 * Attempt to retrieve a pretty formatted world alias from Multiverse for the given world.
	 *
	 * @param world a world to attempt lookup of a Multiverse alias
	 * @return an Optional string containing the world alias if available, otherwise an empty Optional
	 */
	public static Optional<String> getAlias(final World world)
	{
		validate(world, Objects::isNull, () -> new ValidationException(MessageKey.PARAMETER_NULL, Parameter.WORLD));

		Plugin plugin = Bukkit.getPluginManager().getPlugin("Multiverse-Core");

		if (plugin instanceof MultiverseCore multiverseCore && multiverseCore.isEnabled())
		{
			return Optional.ofNullable(multiverseCore.getMVWorldManager().getMVWorld(world).getAlias());
		}

		return Optional.empty();
	}

}
