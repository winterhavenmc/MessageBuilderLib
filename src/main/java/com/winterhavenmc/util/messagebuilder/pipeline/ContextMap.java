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

package com.winterhavenmc.util.messagebuilder.pipeline;

import com.winterhavenmc.util.messagebuilder.validation.ValidationException;
import org.bukkit.command.CommandSender;

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static com.winterhavenmc.util.messagebuilder.validation.MessageKey.PARAMETER_EMPTY;
import static com.winterhavenmc.util.messagebuilder.validation.MessageKey.PARAMETER_NULL;
import static com.winterhavenmc.util.messagebuilder.validation.Parameter.KEY;
import static com.winterhavenmc.util.messagebuilder.validation.Validate.validate;


/**
 * This class implements a map of macro objects that have been passed in by the message builder
 * to be processed for replacement strings. The map key is an enum member, and the corresponding value
 * is the object to be processed. It is backed by a HashMap.
 */
public class ContextMap
{
	private final CommandSender recipient;
	private final String messageId;

	// Backing store map (use linked hash map to maintain insertion order)
	private final Map<String, Object> internalMap = new ConcurrentHashMap<>();


	/**
	 * Class constructor
	 *
	 * @param recipient the message recipient
	 * @param messageId the message unique identifier as a string
	 */
	public ContextMap(final CommandSender recipient, final String messageId)
	{
		this.recipient = recipient;
		this.messageId = messageId;
	}


	/**
	 * Retrieve recipient
	 *
	 * @return the recipient that was used to create the context map
	 */
	public CommandSender getRecipient()
	{
		return recipient;
	}


	/**
	 * Retrieve messageId
	 *
	 * @return {@code MessageId} the MessageId associated with this context map
	 */

	public String getMessageId()
	{
		return messageId;
	}


	/**
	 * Create and puts a new value with its associated ProcessorType into the context map.
	 *
	 * @param key           the unique key for the value
	 * @param value         the value to store
	 * @param <T>           the type of the value
	 */
	public <T> void put(final String key, final T value)
	{
		validate(key, Objects::isNull, () -> new ValidationException(PARAMETER_NULL, KEY));
		validate(key, String::isBlank, () -> new ValidationException(PARAMETER_EMPTY, KEY));
		// allow null value to be inserted into the context map. uncomment line below to throw exception on null 'value' parameter
		//staticValidate(value, Objects::isNull, () -> new ValidationException(PARAMETER_NULL, VALUE));

		// insert value into map with key, replacing null values with string "NULL"
		internalMap.put(key, Objects.requireNonNullElse(value, "NULL"));
	}


	/**
	 * Retrieve a value from the context map for the specified key.
	 *
	 * @param key the context map key
	 * @return the value for the key
	 */
	public Optional<Object> getOpt(final String key)
	{
		validate(key, Objects::isNull, () -> new ValidationException(PARAMETER_NULL, KEY));
		validate(key, String::isBlank, () -> new ValidationException(PARAMETER_EMPTY, KEY));

		return Optional.ofNullable(internalMap.get(key));
	}


	/**
	 * Retrieve a value from the context map for the specified key.
	 *
	 * @param key the context map key
	 * @return the value for the key
	 */
	public Object get(final String key)
	{
		validate(key, Objects::isNull, () -> new ValidationException(PARAMETER_NULL, KEY));
		validate(key, String::isBlank, () -> new ValidationException(PARAMETER_EMPTY, KEY));

		return internalMap.get(key);
	}


	/**
	 * Check if the map contains a value for the specified key.
	 *
	 * @param key the unique key to check
	 * @return true if the key exists, false otherwise
	 */
	public boolean contains(final String key)
	{
		validate(key, Objects::isNull, () -> new ValidationException(PARAMETER_NULL, KEY));
		validate(key, String::isBlank, () -> new ValidationException(PARAMETER_EMPTY, KEY));

		return internalMap.containsKey(key);
	}


	/**
	 * Remove an entry from the map by key.
	 *
	 * @param key The enum member used as the key.
	 * @return The object that was removed, or {@code null} if no mapping existed for the key.
	 */
	public Object remove(final String key)
	{
		validate(key, Objects::isNull, () -> new ValidationException(PARAMETER_NULL, KEY));
		validate(key, String::isBlank, () -> new ValidationException(PARAMETER_EMPTY, KEY));

		return internalMap.remove(key);
	}


	/**
	 * Returns a set view of the mappings in the map.
	 *
	 * @return A set of entries in the map.
	 */
	public @NotNull Set<Map.Entry<String, Object>> entrySet()
	{
		return internalMap.entrySet();
	}


	/**
	 * Clears all entries in the map.
	 */
	public void clear()
	{
		internalMap.clear();
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
