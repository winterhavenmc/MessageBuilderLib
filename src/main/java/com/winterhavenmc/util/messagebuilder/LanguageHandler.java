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

package com.winterhavenmc.util.messagebuilder;

import com.winterhavenmc.util.TimeUnit;
import org.bukkit.World;
import org.bukkit.configuration.Configuration;

import java.util.List;
import java.util.Optional;

public interface LanguageHandler {

//	/**
//	 * Retrieve a MessageRecord
//	 *
//	 * @param messageId an Enum constant that is the key for a message record
//	 * @return the MessageRecord for the key
//	 * @param <MessageId> an Enum constant
//	 */
//	<MessageId extends Enum<MessageId>> MessageRecord getMessageRecord(final MessageId messageId);

//	/**
//	 * Get item name from language specific messages file
//	 *
//	 * @return String ITEM_NAME, or empty string if key not found
//	 */
//	Optional<String> getItemName();

//	/**
//	 * Get a message record from the current message configuration
//	 * @param messageId an Enum constant that is used as the key for a message record
//	 * @param configuration the current message configuration
//	 * @return {@link Optional} {@link MessageRecord} the message record returned for the messageId key from the configuration
//	 * @param <MessageId> an Enum constant
//	 */
//	<MessageId extends Enum<MessageId>> // parameterized type
//	Optional<MessageRecord> // return type
//	getMessageRecord // method name
//	(final MessageId messageId, final Configuration configuration); // parameters
//
//	Optional<ItemRecord> getItemRecord(final String itemKey);

	Configuration getConfiguration();

	//	/**
//	 * Get item name from language specific messages file
//	 *
//	 * @param itemKey the unique key for the item
//	 * @return String ITEM_NAME, or empty string if key not found
//	 */
//	Optional<String> getItemName(final String itemKey);
//
//	/**
//	 * Get configured plural item name from language file
//	 *
//	 * @return the formatted plural display name of an item, or empty string if key not found
//	 */
//	Optional<String> getItemNamePlural(final String itemKey);
//
//
//	/**
//	 * Get configured inventory item name from language file
//	 *
//	 * @return the formatted inventory display name of an item, or empty string if key not found
//	 */
//	Optional<String> getInventoryItemName(final String itemKey);
//
//
//	/**
//	 * Get item lore from language specific messages file
//	 *
//	 * @return List of strings, one string for each line of lore, or empty list if key not found
//	 */
//	List<String> getItemLore(final String itemKey);


	//TODO: change this (or add) to getLocale
	String getConfigLanguage();

	/**
	 * Get spawn display name from language file
	 *
	 * @return the formatted display name for the world spawn, or empty string if key not found
	 */
	Optional<String> getSpawnDisplayName();


	/**
	 * Get home display name from language file
	 *
	 * @return the formatted display name for home, or empty string if key not found
	 */
	Optional<String> getHomeDisplayName();


	/**
	 * Format the time string with days, hours, minutes and seconds as necessary
	 *
	 * @param duration a time duration in milliseconds
	 * @return formatted time string
	 */
	String getTimeString(final long duration);


	/**
	 * Format the time string with days, hours, minutes and seconds as necessary, to the granularity passed
	 *
	 * @param duration a time duration in milliseconds
	 * @param timeUnit the time granularity to display (days | hours | minutes | seconds)
	 * @return formatted time string
	 */
	String getTimeString(final long duration, final TimeUnit timeUnit);


	/**
	 * Get string by path in message file
	 * @param path the message path for the string being retrieved
	 * @return String - the string retrieved by path from message file
	 */
	Optional<String> getString(final String path);


	/**
	 * Get List of String by path in message file
	 * @param path the message path for the string list being retrieved
	 * @return List of String - the string list retrieved by path from message file
	 */
	List<String> getStringList(final String path);


//	Optional<String> getWorldName(final World world);


	/**
	 * Reload messages into Configuration object
	 */
	void reload();

}
