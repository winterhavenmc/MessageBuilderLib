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

import com.winterhavenmc.util.messagebuilder.context.ContextMap;
import com.winterhavenmc.util.messagebuilder.util.LocalizedException;
import com.winterhavenmc.util.messagebuilder.util.WorldNameUtility;

import org.bukkit.Location;

import static com.winterhavenmc.util.messagebuilder.util.LocalizedException.MessageKey.PARAMETER_EMPTY;
import static com.winterhavenmc.util.messagebuilder.util.LocalizedException.MessageKey.PARAMETER_NULL;


/**
 * A processor for resolving placeholders related to Bukkit {@link Location} objects.
 * <p>
 * This processor extracts information from {@link Location} objects, such as the world name
 * and the X, Y, and Z coordinates, and populates a {@link ContextMap} with these values. It also
 * provides a preformatted string combining the world and coordinates for convenience.
 * 
 * <p>
 * The following placeholders are resolved:
 * <ul>
 *     <li><b>%<i>key</i>_LOCATION_WORLD%</b>: The name of the world (e.g., "world", "nether").</li>
 *     <li><b>%<i>key</i>_LOCATION_X%</b>: The integer X-coordinate of the location (e.g., "123").</li>
 *     <li><b>%<i>key</i>_LOCATION_Y%</b>: The integer Y-coordinate of the location (e.g., "64").</li>
 *     <li><b>%<i>key</i>_LOCATION_Z%</b>: The integer Z-coordinate of the location (e.g., "-789").</li>
 *     <li><b>%<i>key</i>_LOCATION%</b>: A preformatted string combining the world and coordinates
 *         (e.g., "world [123, 64, -789]").</li>
 * </ul>
 * 
 *
 * <p>
 * This processor ensures that placeholders are unique by suffixing "_LOCATION" to the key if it is not
 * already present. Null values or missing components, such as the world name, are replaced with the
 * default placeholder {@code UNKNOWN_VALUE}.
 * <b>Example Usage:</b>
 * <pre>
 * {@code
 * LocationProcessor processor = new LocationProcessor(queryHandler);
 * Location location = new Location(world, 123.45, 64.0, -789.12);
 * ResultMap resultMap = processor.resolveContext("HOME", contextMap, location);
 *
 * // Resolved Placeholders:
 * // %HOME_LOCATION_WORLD% -> "world"
 * // %HOME_LOCATION_X% -> "123"
 * // %HOME_LOCATION_Y% -> "64"
 * // %HOME_LOCATION_Z% -> "-789"
 * // %HOME_LOCATION% -> "world [123, 64, -789]"
 * }
 * </pre>
 *
 * @see Location
 * @see ContextMap
 * @see WorldNameUtility
 */
public class LocationProcessor extends MacroProcessorTemplate {


	/**
	 * Resolves placeholders from a {@link Location} object and populates the context map.
	 * <p>
	 * This method extracts location data, formats it as placeholders, and adds the resolved
	 * entries to the {@link ResultMap}. If the input value is not a {@link Location}, an empty
	 * {@link ResultMap} is returned.
	 * 
	 *
	 * <p>
	 * If the provided key does not end with "_LOCATION", the method automatically appends
	 * "_LOCATION" to ensure a consistent naming convention for location placeholders.
	 * Furthermore, other object types that include a location, such as ENTITY or PLAYER,
	 * may call this processor to add context entries to the map for their locations, if available.
	 * 
	 *
	 * @param key         the unique key or namespace for this macro entry
	 * @param contextMap  the {@link ContextMap} to populate with resolved placeholders
	 * @return a {@link ResultMap} containing the resolved placeholders and their replacements
	 * @throws IllegalArgumentException if any parameter is null or invalid
	 */
	@Override
	public ResultMap resolveContext(final String key, final ContextMap contextMap) {
		if (key == null) { throw new LocalizedException(PARAMETER_NULL, "key"); }
		if (key.isBlank()) { throw new LocalizedException(PARAMETER_EMPTY, "key"); }
		if (contextMap == null) { throw new LocalizedException(PARAMETER_NULL, "contextMap"); }

		// create empty result map
		ResultMap resultMap = new ResultMap();

		// get value from container
		Object value = contextMap.get(key);

		// if passed object is a Location, process fields
		if (value instanceof Location location) {

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
		}

		// return result map
		return resultMap;
	}

}
