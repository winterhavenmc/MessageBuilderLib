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

import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MultiverseWorld;
import com.winterhavenmc.util.messagebuilder.LanguageHandler;
import com.winterhavenmc.util.messagebuilder.macro.MacroObjectMap;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Optional;


public class LocationProcessor extends AbstractProcessor implements Processor {

	public LocationProcessor(JavaPlugin plugin, LanguageHandler languageHandler) {
		super(plugin, languageHandler);
	}

	@Override
	public ResultMap doReplacements(final MacroObjectMap macroObjectMap, final String key, final Object object) {

		Location location = null;

		// if object is Entity, set use entity location
		if (object instanceof Entity entity) {
			location = entity.getLocation();
		}
		// if object is Location, use location
		else if (object instanceof Location) {
			location = (Location) object;
		}

		ResultMap resultMap = new ResultMap();

		// if location is null return empty map
		if (location == null) {
			return resultMap;
		}

		// get location strings
		String locationWorld = getWorldName(location).orElse(UNKNOWN_VALUE);
		String locationX = String.valueOf(location.getBlockX());
		String locationY = String.valueOf(location.getBlockY());
		String locationZ = String.valueOf(location.getBlockZ());
		String locationString = locationWorld + " [" + locationX + ", " + locationY + ", " + locationZ + "]";

		// if key does not end in _LOCATION or LOC, add it for location string keys
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
		if (location == null) {
			return Optional.empty();
		}

		// get reference to Multiverse-Core if installed
		MultiverseCore mvCore = (MultiverseCore) plugin.getServer().getPluginManager().getPlugin("Multiverse-Core");

		// declare resultString with world name for location
		String resultString;
		if (location.getWorld() != null) {
			resultString = location.getWorld().getName();
		}
		else {
			// get name of first world
			resultString = plugin.getServer().getWorlds().get(0).getName();
		}

		// if Multiverse is enabled, use Multiverse world alias if available
		if (mvCore != null && mvCore.isEnabled()) {

			// get Multiverse world object
			MultiverseWorld mvWorld = mvCore.getMVWorldManager().getMVWorld(location.getWorld());

			// if Multiverse alias is not null or empty, set world name to alias if set
			if (mvWorld != null && mvWorld.getAlias() != null && !mvWorld.getAlias().isEmpty()) {
				resultString = mvWorld.getAlias();
			}
		}

		// return resultString
		return Optional.of(resultString);
	}

}
