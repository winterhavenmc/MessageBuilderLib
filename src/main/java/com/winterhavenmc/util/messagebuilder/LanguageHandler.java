package com.winterhavenmc.util.messagebuilder;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

interface LanguageHandler {

	/**
	 * Get message keys; used for testing
	 * @return Set of String containing message keys
	 */
	@SuppressWarnings("unused")
	Collection<String> getMessageKeys();


	/**
	 * Check if message is enabled
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


	/**
	 * Get item name from language specific messages file, with translated color codes
	 *
	 * @return String ITEM_NAME, or empty string if key not found
	 */
	String getItemName();


	/**
	 * Get configured plural item name from language file
	 *
	 * @return the formatted plural display name of an item, or empty string if key not found
	 */
	String getItemNamePlural();


	/**
	 * Get configured inventory item name from language file
	 *
	 * @return the formatted inventory display name of an item, or empty string if key not found
	 */
	String getInventoryItemName();


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
	String getSpawnDisplayName();


	/**
	 * Get home display name from language file
	 *
	 * @return the formatted display name for home, or empty string if key not found
	 */
	String getHomeDisplayName();


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
	 * @throws NullPointerException if parameter is null
	 */
	String getTimeString(final long duration, final TimeUnit timeUnit);


	/**
	 * Reload messages into Configuration object
	 */
	void reload();

}
