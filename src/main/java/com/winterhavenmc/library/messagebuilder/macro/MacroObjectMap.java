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

package com.winterhavenmc.library.messagebuilder.macro;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * This class implements a map of macro objects that have been passed in by the message builder
 * to be processed for replacement strings. The map key is the Enum member for the Macro passed by the caller,
 * and the corresponding value is the object to be processed. It is backed by a HashMap.
 */
public class MacroObjectMap {

	// backing store hashmap
	private final Map<String, Object> macroObjectMap = new HashMap<>();


	/**
	 * A method to insert an object into the map
	 * @param macro A string of the Macro enum member name
	 * @param object The object to be processed for replacement strings
	 */
	public void put(String macro, Object object) {
		this.macroObjectMap.put(macro, object);
	}

	/**
	 * A method to retrieve an object from the map by key string
	 * @param key the string referencing the object to be retrieved
	 * @return the object retrieved from the map by key string
	 */
	public Object get(final String key) {
		return this.macroObjectMap.get(key);
	}

	/**
	 * Check if an entry exists in the map for the string key
	 * @param key a string used as a key in the map
	 * @return boolean true if an entry exists in the map for the key, false no entry exists for the key
	 */
	public boolean containsKey(final String key) {
		return this.macroObjectMap.containsKey(key);
	}

	/**
	 * Return a Set view of the mappings in the map
	 * @return Set of entries in the map
	 */
	Set<Map.Entry<String, Object>> entrySet() {
		return this.macroObjectMap.entrySet();
	}

}
