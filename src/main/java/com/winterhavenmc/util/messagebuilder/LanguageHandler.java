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

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface LanguageHandler {

	/**
	 * Get all keys for the message section of the language file. This method is only used for unit testing.
	 *
	 * @return Collection of String containing the keys for the message section of the language file
	 */
	@SuppressWarnings("unused")
	Collection<String> getMessageKeys();


	/**
	 * Check if message is enabled. If an 'enabled' key does not exist for a valid message,
	 * and a default does not exist, returns true so that the enabled setting is optional
	 * in otherwise valid messages that contain a 'string' key.
	 *
	 * @param <MessageId> parameterized type enum member for messageId
	 * @param messageId   message identifier to check
	 * @return true if message is enabled, false if not
	 * @throws NullPointerException if parameter is null
	 */
	<MessageId extends Enum<MessageId>> boolean isEnabled(final MessageId messageId);


	/**
	 * get message repeat delay from language file
	 *
	 * @param <MessageId> parameterized type enum member for messageId
	 * @param messageId   message identifier to retrieve message delay
	 * @return long - message repeat delay in seconds
	 */
	<MessageId extends Enum<MessageId>> long getRepeatDelay(final MessageId messageId);


	/**
	 * get message text from language file
	 *
	 * @param <MessageId> parameterized type enum member for messageId
	 * @param messageId   message identifier to retrieve message text
	 * @return String message text, or empty string if no message string found
	 */
	<MessageId extends Enum<MessageId>> String getMessage(final MessageId messageId);

	<MessageId extends Enum<MessageId>> String getTitle(final MessageId messageId);

	<MessageId extends Enum<MessageId>> String getSubtitle(final MessageId messageId);

	<MessageId extends Enum<MessageId>> int getTitleFadeIn(final MessageId messageId);

	<MessageId extends Enum<MessageId>> int getTitleStay(final MessageId messageId);

	<MessageId extends Enum<MessageId>> int getTitleFadeOut(final MessageId messageId);


	/**
	 * Get item name from language specific messages file, with translated color codes
	 *
	 * @return String ITEM_NAME, or empty string if key not found
	 */
	Optional<String> getItemName();


	/**
	 * Get item name from language specific messages file, with translated color codes
	 *
	 * @param def a default value to use if the item name cannot be found in the config
	 * @return String ITEM_NAME, or empty string if key not found
	 */
	Optional<String> getItemName(String def);

	/**
	 * Get configured plural item name from language file
	 *
	 * @return the formatted plural display name of an item, or empty string if key not found
	 */
	Optional<String> getItemNamePlural();


	/**
	 * Get configured inventory item name from language file
	 *
	 * @return the formatted inventory display name of an item, or empty string if key not found
	 */
	Optional<String> getInventoryItemName();


	/**
	 * Get item lore from language specific messages file, with translated color codes
	 *
	 * @return List of strings, one string for each line of lore, or empty list if key not found
	 */
	List<String> getItemLore();


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


	Optional<String> getWorldName(final World world);


	/**
	 * Reload messages into Configuration object
	 */
	void reload();

	Optional<String> getWorldAlias(final World world);

}
