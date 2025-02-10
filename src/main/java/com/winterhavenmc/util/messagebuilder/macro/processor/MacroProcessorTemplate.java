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

import org.bukkit.Location;


/**
 * This skeleton class makes the queryHandler available to any extending classes,
 * as well as the UNKNOWN_VALUE string constant.
 * Methods used by all classes may be placed here in the future.
 */
public abstract class MacroProcessorTemplate implements MacroProcessor
{
	final static String UNKNOWN_VALUE = "???";


	/**
	 * Resolve location fields and place them in a result map
	 *
	 * @param key the base key for the location fields to be created under
	 * @param location the location to extract fields
	 * @return a {@link ResultMap} populated with the location fields
	 */
	static ResultMap insertLocationFields(final String key, final Location location)
	{
		ResultMap resultMap = new ResultMap();

		// copy of original key before appending component field suffixes
		String resultKey = key;

		// Get components
		String locationWorld;
		if (location.getWorld() == null) {
			locationWorld = UNKNOWN_VALUE;
		}
		else {
			locationWorld = location.getWorld().getName();
		}
		String locationX = String.valueOf(location.getBlockX());
		String locationY = String.valueOf(location.getBlockY());
		String locationZ = String.valueOf(location.getBlockZ());
		String locationString = locationWorld + " [" + locationX + ", " + locationY + ", " + locationZ + "]";

		// if macroName does not end in _LOCATION, suffix it to the end of the key
		if (!resultKey.endsWith(".LOCATION")) {
			resultKey = resultKey.concat(".LOCATION");
		}

		// Store placeholders
		resultMap.put(resultKey, locationString);
		resultMap.put(resultKey.concat(".WORLD"), locationWorld);
		resultMap.put(resultKey.concat(".X"), locationX);
		resultMap.put(resultKey.concat(".Y"), locationY);
		resultMap.put(resultKey.concat(".Z"), locationZ);

		return resultMap;
	}

}
