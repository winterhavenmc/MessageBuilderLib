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

package com.winterhavenmc.library.messagebuilder.util;

import org.bukkit.Bukkit;

import java.util.logging.Logger;


public enum ServerPlatform
{
	CRAFTBUKKIT,
	SPIGOT,
	PAPER,
	PURPUR,
	OTHER;

	// cache detected platform on startup
	private static final ServerPlatform DETECTED = detectPlatform();


	private static ServerPlatform detectPlatform()
	{
		final String name = Bukkit.getName();
		if (name.equalsIgnoreCase("Purpur")) return PURPUR;
		else if (name.equalsIgnoreCase("Paper")) return PAPER;
		else if (name.equalsIgnoreCase("Spigot")) return SPIGOT;
		else if (name.equalsIgnoreCase("CraftBukkit")) return CRAFTBUKKIT;
		else try
		{
			ClassLoader serverClassLoader = Bukkit.getServer().getClass().getClassLoader();
			Class.forName("io.papermc.paper.configuration.Configuration", false, serverClassLoader);
			return PAPER;
		}
		catch (ClassNotFoundException ignored)
		{
			Logger.getLogger("DetectPlatform").warning("Could not detect server platform.");
		}

		return OTHER;
	}


	public static ServerPlatform detect()
	{
		return DETECTED;
	}


	public static boolean isPaper()
	{
		return DETECTED == PAPER || DETECTED == PURPUR;
	}

}
