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

package com.winterhavenmc.util.messagebuilder.pipeline.context;

import com.winterhavenmc.util.messagebuilder.recipient.ValidRecipient;
import com.winterhavenmc.util.messagebuilder.util.RecordKey;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


/**
 * This class implements a map of macro objects that have been passed in by the message builder
 * to be processed for replacement strings. The map key is an enum member, and the corresponding value
 * is the object to be processed. It is backed by a HashMap.
 */
public class ContextMap
{
	private final ValidRecipient recipient;
	private final RecordKey messageKey;

	private final Map<RecordKey, Object> internalMap = new ConcurrentHashMap<>();


	/**
	 * Class constructor
	 *
	 * @param recipient the message protoRecipient
	 * @param messageKey the message unique identifier as a string
	 */
	private ContextMap(final ValidRecipient recipient, final RecordKey messageKey)
	{
		this.recipient = recipient;
		this.messageKey = messageKey;
	}


	public static Optional<ContextMap> of(final ValidRecipient protoRecipient, final RecordKey messageKey)
	{
		return Optional.of(new ContextMap(protoRecipient, messageKey));
	}


	/**
	 * Retrieve Optional protoRecipient
	 *
	 * @return the protoRecipient that was used to create the context map
	 */
	public ValidRecipient getRecipient()
	{
		return recipient;
	}


	/**
	 * Retrieve key
	 *
	 * @return {@code MessageId} the MessageId associated with this context map
	 */

	public RecordKey getMessageKey()
	{
		return messageKey;
	}


	/**
	 * Create and puts a new value with its associated ProcessorType into the context map.
	 *
	 * @param macroKey      the unique key for the value
	 * @param value         the value to store
	 * @param <T>           the type of the value
	 */
	public <T> void put(final RecordKey macroKey, final T value)
	{
        // insert value into map with key, replacing null values with string "NULL"
		internalMap.put(macroKey, Objects.requireNonNullElse(value, "NULL"));
	}


	/**
	 * Retrieve a value from the context map for the specified key.
	 *
	 * @param macroKey the context map key
	 * @return the value for the key
	 */
	public Optional<Object> get(final RecordKey macroKey)
	{
		return Optional.ofNullable(internalMap.get(macroKey));
	}


	/**
	 * Check if the map contains a value for the specified key.
	 *
	 * @param macroKey the unique key to check
	 * @return true if the key exists, false otherwise
	 */
	public boolean contains(final RecordKey macroKey)
	{
		return internalMap.containsKey(macroKey);
	}


	/**
	 * Remove an entry from the map by key.
	 *
	 * @param macroKey The enum member used as the key.
	 * @return The object that was removed, or {@code null} if no mapping existed for the key.
	 */
	public Object remove(final RecordKey macroKey)
	{
		return internalMap.remove(macroKey);
	}


	/**
	 * Returns a set view of the mappings in the map.
	 *
	 * @return A set of entries in the map.
	 */
	public @NotNull Set<Map.Entry<RecordKey, Object>> entrySet()
	{
		return internalMap.entrySet();
	}


	/**
	 * Clears all entries in the map.
	 */
	public void clear()
	{
		this.internalMap.clear();
	}

	/**
	 * Returns the number of entries in the map.
	 *
	 * @return The size of the map.
	 */
	public int size()
	{
		return this.internalMap.size();
	}

	/**
	 * Checks if the map is empty.
	 *
	 * @return {@code true} if the map contains no entries, {@code false} otherwise.
	 */
	public boolean isEmpty()
	{
		return this.internalMap.isEmpty();
	}

}
