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

import com.winterhavenmc.util.messagebuilder.macro.processor.ProcessorType;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;


/**
 * This class implements a map of macro objects that have been passed in by the message builder
 * to be processed for replacement strings. The map key is an enum member, and the corresponding value
 * is the object to be processed. It is backed by a HashMap.
 */
public class ContextMap {

	// Backing store HashMap (ed: linked hash map to maintain insertion order TODO: investigate best map type here
	private final Map<String, ContextContainer<?>> contextMap = new LinkedHashMap<>();


	/**
	 * Puts a new value into the context map with its associated ProcessorType.
	 *
	 * @param key           the unique key for the value
	 * @param container     the context container wrapping the value and its ProcessorType
	 */
	public void put(String key, ContextContainer<?> container) {
		contextMap.put(key, container);
	}


	/**
	 * Creates and puts a new value with its associated ProcessorType into the context map.
	 *
	 * @param key           the unique key for the value
	 * @param value         the value to store
	 * @param processorType the processor type associated with the value
	 * @param <T>           the type of the value
	 */
	public <T> void put(String key, T value, ProcessorType processorType) {
		contextMap.put(key, ContextContainer.of(value, processorType));
	}


	/**
	 * Retrieves a context container by its key.
	 *
	 * @param key the unique key for the value
	 * @return an Optional containing the ContextContainer, or empty if not found
	 */
	public Optional<ContextContainer<?>> getContainer(String key) {
		return Optional.ofNullable(contextMap.get(key));
	}


	/**
	 * Retrieves the value by its key, cast to the specified type.
	 *
	 * @param key   the unique key for the value
	 * @param clazz the expected class type of the value
	 * @param <T>   the type of the value
	 * @return an Optional containing the value, or empty if not found or type mismatch
	 */
	public <T> Optional<T> getValue(String key, Class<T> clazz) {
		ContextContainer<?> container = contextMap.get(key);
		if (container != null && clazz.isInstance(container.value())) {
			return Optional.of(clazz.cast(container.value()));
		}
		return Optional.empty();
	}


	/**
	 * Retrieves the processor type associated with a given key.
	 *
	 * @param key the unique key for the value
	 * @return an Optional containing the ProcessorType, or empty if not found
	 */
	public Optional<ProcessorType> getProcessorType(String key) {
		ContextContainer<?> container = contextMap.get(key);
		return container != null ? Optional.of(container.processorType()) : Optional.empty();
	}


	/**
	 * Checks if the map contains a value for the specified key.
	 *
	 * @param key the unique key to check
	 * @return true if the key exists, false otherwise
	 */
	public boolean containsKey(String key) {
		return contextMap.containsKey(key);
	}


	/**
	 * Removes an entry from the map by key.
	 *
	 * @param key The enum member used as the key.
	 * @return The object that was removed, or {@code null} if no mapping existed for the key.
	 */
	public ContextContainer<?> remove(final String key) {
		return this.contextMap.remove(key);
	}

	/**
	 * Returns a set view of the mappings in the map.
	 *
	 * @return A set of entries in the map.
	 */
	public Set<Map.Entry<String, ContextContainer<?>>> entrySet() {
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
