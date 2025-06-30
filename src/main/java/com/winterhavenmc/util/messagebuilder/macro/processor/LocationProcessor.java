/*
 * Copyright (c) 2022 Tim Savage.
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

package com.winterhavenmc.util.messagebuilder.macro.processor;

import com.winterhavenmc.util.messagebuilder.LanguageHandler;
import com.winterhavenmc.util.messagebuilder.macro.MacroObjectMap;
import com.winterhavenmc.util.messagebuilder.worldname.WorldNameResolver;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;

import java.util.Optional;


public class LocationProcessor extends AbstractProcessor implements Processor {

	public LocationProcessor(final LanguageHandler languageHandler) {
		super(languageHandler);
	}

	@Override
	public ResultMap execute(final MacroObjectMap macroObjectMap, final String key, final Object object) {

		// create empty result map
		ResultMap resultMap = new ResultMap();

		// if passed object is not a Location, return empty result map
		if (!(object instanceof Location location)) {
			return resultMap;
		}

		// get location strings
		String locationWorld = getWorldName(location).orElse(UNKNOWN_VALUE);
		String locationX = String.valueOf(location.getBlockX());
		String locationY = String.valueOf(location.getBlockY());
		String locationZ = String.valueOf(location.getBlockZ());
		String locationString = locationWorld + " [" + locationX + ", " + locationY + ", " + locationZ + "]";

		// if key does not end in _LOCATION or _LOC, add it for location string keys
		String[] keySuffixes = {"_LOCATION", "_LOC"};

		// put location strings in map
		for (String keySuffix : keySuffixes) {
			if (key.endsWith(keySuffix)) {
				keySuffix = "";
			}

			resultMap.put(key + keySuffix, locationString);
			resultMap.put(key + keySuffix + "_WORLD", locationWorld);
			resultMap.put(key + keySuffix + "_X", locationX);
			resultMap.put(key + keySuffix + "_Y", locationY);
			resultMap.put(key + keySuffix + "_Z", locationZ);
		}

		// put original key with location string in resultMap
		resultMap.put(key, locationString);

		return resultMap;
	}


	/**
	 * Get world name for location, using Multiverse alias if available
	 *
	 * @param location the location used to retrieve world name
	 * @return bukkit world name or multiverse alias as {@link Optional} wrapped String
	 */
	private Optional<String> getWorldName(final Location location) {

		// check for null parameter
		if (location == null)
		{
			return Optional.empty();
		}

		// get server instance from static reference to access the plugin manager and worlds
		// note this is the only processor that needed external access, so we are resorting to static references
		// in order to avoid otherwise unnecessary dependency injection
		Server server = Bukkit.getServer();

		// declare resultString with world name for location
		String resultString;
		if (location.getWorld() != null)
		{
			WorldNameResolver resolver = WorldNameResolver.get(server.getPluginManager());
			resultString = resolver.resolve(location.getWorld());
		}
		else
		{
			// get name of first world
			resultString = server.getWorlds().getFirst().getName();
		}

		// return resultString
		return Optional.of(resultString);
	}

}
