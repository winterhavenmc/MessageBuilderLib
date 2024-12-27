/*
 * Copyright (c) 2024 Tim Savage.
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

package com.winterhavenmc.util.messagebuilder.macro;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * This class implements a map of macro objects that have been passed in by the message builder
 * to be processed for replacement strings. The map key is an enum member, and the corresponding value
 * is the object to be processed. It is backed by a HashMap.
 */
public class ContextMap {

	// Backing store HashMap (ed: linked hash map to maintain insertion order TODO: investigate best map type here
	private final Map<CompositeKey, Object> contextMap = new LinkedHashMap<>();

	/**
	 * Inserts an object into the map.
	 *
	 * @param compositeKey The key
	 * @param object The object to be processed for replacement strings.
	 */
	public void put(CompositeKey compositeKey, Object object) {
		this.contextMap.put(compositeKey, object);
	}

	/**
	 * Retrieves an object from the map by key.
	 *
	 * @param compositeKey The enum member plus unique string used as the key.
	 * @return The object retrieved from the map by key, or {@code null} if not found.
	 */
	public Object get(final CompositeKey compositeKey) {
		return this.contextMap.get(compositeKey);
	}

	/**
	 * Checks if an entry exists in the map for the given key.
	 *
	 * @param compositeKey The enum member + string used as the key.
	 * @return {@code true} if an entry exists in the map for the key, {@code false} otherwise.
	 */
	public boolean containsKey(final CompositeKey compositeKey) {
		return this.contextMap.containsKey(compositeKey);
	}

	/**
	 * Removes an entry from the map by key.
	 *
	 * @param compositeKey The enum member used as the key.
	 * @return The object that was removed, or {@code null} if no mapping existed for the key.
	 */
	public Object remove(final CompositeKey compositeKey) {
		return this.contextMap.remove(compositeKey);
	}

	/**
	 * Returns a set view of the mappings in the map.
	 *
	 * @return A set of entries in the map.
	 */
	public Set<Map.Entry<CompositeKey, Object>> entrySet() {
		return this.contextMap.entrySet();
	}

	/**
	 * Clears all entries in the map.
	 */
	public void clear() {
		this.contextMap.clear();
	}

	/**
	 * Returns the number of entries in the map.
	 *
	 * @return The size of the map.
	 */
	public int size() {
		return this.contextMap.size();
	}

	/**
	 * Checks if the map is empty.
	 *
	 * @return {@code true} if the map contains no entries, {@code false} otherwise.
	 */
	public boolean isEmpty() {
		return this.contextMap.isEmpty();
	}
}
