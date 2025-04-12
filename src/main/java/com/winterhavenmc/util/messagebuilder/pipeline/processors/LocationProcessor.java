/*
 * Copyright (c) 2022-2025 Tim Savage.
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

package com.winterhavenmc.util.messagebuilder.pipeline.processors;

import com.winterhavenmc.util.messagebuilder.pipeline.context.ContextMap;
import com.winterhavenmc.util.messagebuilder.util.RecordKey;

import org.bukkit.Location;


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
 *     <li><b>{<i>key</i>.LOCATION.WORLD}</b>: The name of the world (e.g., "world", "nether").</li>
 *     <li><b>{<i>key</i>.LOCATION.X}</b>: The integer X-coordinate of the location (e.g., "123").</li>
 *     <li><b>{<i>key</i>.LOCATION.Y}</b>: The integer Y-coordinate of the location (e.g., "64").</li>
 *     <li><b>{<i>key</i>.LOCATION.Z}</b>: The integer Z-coordinate of the location (e.g., "-789").</li>
 *     <li><b>{<i>key</i>.LOCATION}</b>: A preformatted string combining the world and coordinates
 *         (e.g., "world [123, 64, -789]").</li>
 * </ul>
 * 
 *
 * <p>
 * This processor ensures that placeholders are unique by suffixing ".LOCATION" to the key if it is not
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
 * // {HOME.LOCATION.WORLD} -> "world"
 * // {HOME.LOCATION.X} -> "123"
 * // {HOME.LOCATION.Y} -> "64"
 * // {HOME.LOCATION.Z} -> "-789"
 * // {HOME.LOCATION} -> "world [123, 64, -789]"
 * }
 * </pre>
 *
 * @see Location
 * @see ContextMap
 */
public class LocationProcessor extends MacroProcessorTemplate
{
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
	 * @throws IllegalArgumentException if any parameter is null or Invalid
	 */
	@Override
	public ResultMap resolveContext(final RecordKey key, final ContextMap contextMap)
	{
		ResultMap resultMap = new ResultMap();

		contextMap.get(key)
				.filter(Location.class::isInstance)
				.map(Location.class::cast)
				.ifPresent(location -> resultMap.putAll(insertLocationFields(key, location)));

		return resultMap;
	}

}
