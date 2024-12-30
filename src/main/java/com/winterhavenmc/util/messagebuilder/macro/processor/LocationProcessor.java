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
import com.winterhavenmc.util.messagebuilder.macro.Namespace;
import com.winterhavenmc.util.messagebuilder.macro.NamespaceKey;
import com.winterhavenmc.util.messagebuilder.query.QueryHandler;
import com.winterhavenmc.util.messagebuilder.util.Error;
import com.winterhavenmc.util.messagebuilder.util.WorldNameUtility;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.PluginManager;


public class LocationProcessor extends AbstractProcessor {

	public LocationProcessor(QueryHandler queryHandler) {
		super(queryHandler);
	}

	@Override
	public <T> ResultMap resolveContext(final String key, final ContextMap contextMap, final T value) {
		if (key == null) { throw new IllegalArgumentException(Error.PARAMETER_NULL_NAMESPACED_KEY.getMessage()); }
		if (key.isBlank()) { throw new IllegalArgumentException((Error.PARAMETER_EMPTY_NAMESPACED_KEY.getMessage())); }
		if (contextMap == null) { throw new IllegalArgumentException(Error.PARAMETER_NULL_QUERY_HANDLER.getMessage()); }
		if (value == null) { throw new IllegalArgumentException(Error.PARAMETER_NULL_ITEM_KEY.name()); }

		// get server plugin manager
		PluginManager pluginManager = Bukkit.getPluginManager();

		// get world name utility
		WorldNameUtility worldNameUtility = new WorldNameUtility(pluginManager);

		// create empty result map
		ResultMap resultMap = new ResultMap();

		// if passed object is not a Location, return empty result map
		if (value instanceof Location location) {

			String resultKey = key;

			// get location strings from location object
			String locationWorld = worldNameUtility.getWorldName(location.getWorld()).orElse(UNKNOWN_VALUE);
			String locationX = String.valueOf(location.getBlockX());
			String locationY = String.valueOf(location.getBlockY());
			String locationZ = String.valueOf(location.getBlockZ());
			String locationString = locationWorld + " [" + locationX + ", " + locationY + ", " + locationZ + "]";

			// if macroName does not end in _LOCATION, suffix it to the end of the key
			if (!resultKey.endsWith("_LOCATION")) {
				resultKey = resultKey.concat("_LOCATION");
			}

			// create new map entries for location string and separate fields
			resultMap.put(NamespaceKey.create(resultKey, Namespace.Category.MACRO), locationString);
			resultMap.put(NamespaceKey.create(resultKey, Namespace.Category.MACRO) + "_WORLD", locationWorld);
			resultMap.put(NamespaceKey.create(resultKey, Namespace.Category.MACRO) + "_X", locationX);
			resultMap.put(NamespaceKey.create(resultKey, Namespace.Category.MACRO) + "_Y", locationY);
			resultMap.put(NamespaceKey.create(resultKey, Namespace.Category.MACRO) + "_Z", locationY);
		}

		// return result map
		return resultMap;
	}

}
