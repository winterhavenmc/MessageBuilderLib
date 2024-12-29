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

import com.winterhavenmc.util.messagebuilder.macro.ContextMap;
import com.winterhavenmc.util.messagebuilder.macro.NamespacedKey;
import com.winterhavenmc.util.messagebuilder.query.QueryHandler;

import org.bukkit.Location;


public class LocationProcessor extends AbstractProcessor {

	public LocationProcessor(QueryHandler queryHandler) {
		super(queryHandler);
	}

	@Override
	public <T> ResultMap execute(final String key, final ContextMap contextMap, final T value) {

		// create empty result map
		ResultMap resultMap = new ResultMap();

		// if passed object is not a Location, return empty result map
		if (value instanceof Location location) {

			String resultKey = key;

			// get location strings from location object
			String locationWorld = queryHandler.getWorldName(location.getWorld()).orElse(UNKNOWN_VALUE);
			String locationX = String.valueOf(location.getBlockX());
			String locationY = String.valueOf(location.getBlockY());
			String locationZ = String.valueOf(location.getBlockZ());
			String locationString = locationWorld + " [" + locationX + ", " + locationY + ", " + locationZ + "]";

			// Key naming logic:
			// if key does end in _LOC, turn it into _LOCATION. The _LOC placeholders are deprecated and being removed.
			// this check is to ensure any placeholders still using the old _LOC encountered will have their keys placed
			// in the result map with only _LOCATION. This logic may be removed when backwards compatibility is desired or necessary.
			if (resultKey.endsWith("_LOC")) {
				resultKey = resultKey.concat("ATION");
			}

			// if macroName does not end in _LOCATION, tack it on
			if (!resultKey.endsWith("_LOCATION")) {
				resultKey = resultKey.concat("_LOCATION");
			}

			// create new map entries for location string and separate fields
			resultMap.put(NamespacedKey.create(resultKey, NamespacedKey.Category.MACRO), locationString);
			resultMap.put(NamespacedKey.create(resultKey, NamespacedKey.Category.MACRO) + "_WORLD", locationWorld);
			resultMap.put(NamespacedKey.create(resultKey, NamespacedKey.Category.MACRO) + "_X", locationX);
			resultMap.put(NamespacedKey.create(resultKey, NamespacedKey.Category.MACRO) + "_Y", locationY);
			resultMap.put(NamespacedKey.create(resultKey, NamespacedKey.Category.MACRO) + "_Z", locationY);
		}
		return resultMap;
	}

}
