/*
 * Copyright (c) 2024-2025 Tim Savage.
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

package com.winterhavenmc.util.messagebuilder.context;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class implements a map of macro objects that have been passed in by the message builder
 * to be processed for replacement strings. The map key is an enum member, and the corresponding value
 * is the object to be processed. It is backed by a HashMap.
 */
public class ContextMap {

	private final CommandSender recipient;

	// Backing store map (use linked hash map to maintain insertion order) TODO: investigate best map type here
	private final Map<String, Object> internalMap = new ConcurrentHashMap<>();


	/**
	 * Class constructor
	 *
	 * @param recipient the message recipient
	 */
	public ContextMap(CommandSender recipient) {
		this.recipient = recipient;
	}


	/**
	 * Retrieve recipient
	 *
	 * @return the recipient that was used to create the context map
	 */
	public CommandSender getRecipient() {
		return recipient;
	}


	/**
	 * Creates and puts a new value with its associated ProcessorType into the context map.
	 *
	 * @param key           the unique key for the value
	 * @param value         the value to store
	 * @param <T>           the type of the value
	 */
	public <T> void put(String key, T value) {
		internalMap.put(key, Objects.requireNonNullElse(value, "NULL"));
	}

	public Object get(String key) {
		return internalMap.get(key);
	}

	/**
	 * Checks if the map contains a value for the specified key.
	 *
	 * @param key the unique key to check
	 * @return true if the key exists, false otherwise
	 */
	public boolean containsKey(String key) {
		return internalMap.containsKey(key);
	}


	/**
	 * Removes an entry from the map by key.
	 *
	 * @param key The enum member used as the key.
	 * @return The object that was removed, or {@code null} if no mapping existed for the key.
	 */
	public Object remove(final String key) {
		return internalMap.remove(key);
	}


	/**
	 * Returns a set view of the mappings in the map.
	 *
	 * @return A set of entries in the map.
	 */
	public @NotNull Set<Map.Entry<String, Object>> entrySet() {
		return internalMap.entrySet();
	}


	/**
	 * Clears all entries in the map.
	 */
	public void clear() {
		internalMap.clear();
	}

	/**
	 * Returns the number of entries in the map.
	 *
	 * @return The size of the map.
	 */
	public int size() {
		return this.internalMap.size();
	}

	/**
	 * Checks if the map is empty.
	 *
	 * @return {@code true} if the map contains no entries, {@code false} otherwise.
	 */
	public boolean isEmpty() {
		return this.internalMap.isEmpty();
	}

}
