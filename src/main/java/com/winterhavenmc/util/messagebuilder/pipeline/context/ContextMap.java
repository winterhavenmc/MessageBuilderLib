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

import com.winterhavenmc.util.messagebuilder.keys.MacroKey;
import com.winterhavenmc.util.messagebuilder.model.recipient.Recipient;
import com.winterhavenmc.util.messagebuilder.keys.RecordKey;
import org.bukkit.entity.Entity;
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
	private final Recipient.Valid recipient;
	private final RecordKey messageKey;

	private final Map<MacroKey, Object> internalMap = new ConcurrentHashMap<>();

	private enum InitialField
	{
		RECIPIENT,
		LOCATION
	}


	/**
	 * Class constructor
	 *
	 * @param recipient the message recipient
	 * @param messageKey the message unique identifier as a string
	 */
	private ContextMap(final Recipient.Valid recipient, final RecordKey messageKey)
	{
		this.recipient = recipient;
		this.messageKey = messageKey;

		addRecipientContext();
	}

	/**
	 * Add the recipient fields to the context map, including location field if the recipient is a player
	 */
	public void addRecipientContext()
	{
		MacroKey macroKey = MacroKey.of(InitialField.RECIPIENT).orElseThrow();
		MacroKey locationMacroKey = macroKey.append(InitialField.LOCATION).orElseThrow();

		this.putIfAbsent(macroKey, this.getRecipient());

		if (this.getRecipient().sender() instanceof Entity entity)
		{
			this.putIfAbsent(locationMacroKey, entity.getLocation());
		}
	}


	/**
	 * Static factory method returns optional of context map
	 *
	 * @param recipient the message recipient
	 * @param messageKey the message key
	 * @return an Optional of a new instance of {@code ContextMap}
	 */
	public static Optional<ContextMap> of(final Recipient.Valid recipient, final RecordKey messageKey)
	{
		return Optional.of(new ContextMap(recipient, messageKey));
	}


	/**
	 * Retrieve Optional recipient
	 *
	 * @return the recipient that was used to create the context map
	 */
	public Recipient.Valid getRecipient()
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
	 * Create and puts a new value with its into the context map.
	 *
	 * @param macroKey      the unique key for the value
	 * @param value         the value to store
	 * @param <T>           the type of the value
	 */
	public <T> void put(final MacroKey macroKey, final T value)
	{
		// insert value into map with key, replacing null values with string "NULL"
		internalMap.put(macroKey, Objects.requireNonNullElse(value, "NULL"));
	}


	/**
	 * Create and puts a new value with its associated ProcessorType into the context map.
	 *
	 * @param macroKey      the unique key for the value
	 * @param value         the value to store
	 * @param <T>           the type of the value
	 */
	public <T> void putIfAbsent(final MacroKey macroKey, final T value)
	{
        // insert value into map with key, replacing null values with string "NULL"
		internalMap.putIfAbsent(macroKey, Objects.requireNonNullElse(value, "NULL"));
	}


	/**
	 * Retrieve a value from the context map for the specified key.
	 *
	 * @param macroKey the context map key
	 * @return the value for the key
	 */
	public Optional<Object> get(final MacroKey macroKey)
	{
		return Optional.ofNullable(internalMap.get(macroKey));
	}


	/**
	 * Check if the map contains a value for the specified key.
	 *
	 * @param macroKey the unique key to check
	 * @return true if the key exists, false otherwise
	 */
	public boolean contains(final MacroKey macroKey)
	{
		return internalMap.containsKey(macroKey);
	}


	/**
	 * Returns a set view of the mappings in the map.
	 *
	 * @return A set of entries in the map.
	 */
	public @NotNull Set<Map.Entry<MacroKey, Object>> entrySet()
	{
		return internalMap.entrySet();
	}

}
