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

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("unused")
public final class MessageBuilder<MessageId extends Enum<MessageId>, Macro extends Enum<Macro>> {

	private final LanguageHandler languageHandler;


	/**
	 * Class constructor
	 *
	 * @param plugin reference to plugin main class
	 */
	public MessageBuilder(final JavaPlugin plugin) {
		this.languageHandler = new YamlLanguageHandler(plugin);
	}


	/**
	 * Initiate a message
	 * @param recipient the command sender to whom the message will be sent
	 * @param messageId the message identifier
	 * @return Message - an initialized message object
	 */
	public Message<MessageId, Macro> build(final CommandSender recipient, final MessageId messageId) {
		return new Message<>(recipient, messageId, languageHandler);
	}


	/**
	 * Check if a message is enabled in the configuration file
	 * @param messageId the message identifier
	 * @return true if message is enabled, false if not
	 */
	public boolean isEnabled(final MessageId messageId) {
		return languageHandler.isEnabled(messageId);
	}


	/**
	 * Get the configured repeat delay for a message
	 * @param messageId the message identifier
	 * @return long - the message repeat delay (in seconds)
	 */
	public long getRepeatDelay(final MessageId messageId) {
		return languageHandler.getRepeatDelay(messageId);
	}


	/**
	 * get message text from language file
	 *
	 * @param messageId the message identifier
	 * @return String message text, or empty string if no message string found
	 */
	public String getMessage(final MessageId messageId) {
		return languageHandler.getMessage(messageId);
	}


	/**
	 * Get item name from language specific messages file, with translated color codes
	 *
	 * @return the formatted item name from language file, or empty string if key not found
	 */
	public String getItemName() {
		return languageHandler.getItemName();
	}


	/**
	 * Get configured plural item name from language file
	 *
	 * @return the formatted item plural name from language file, or empty string if key not found
	 */
	public String getItemNamePlural() {
		return languageHandler.getItemNamePlural();
	}


	/**
	 * Get configured inventory item name from language file
	 *
	 * @return the formatted inventory display name of an item, or empty string if key not found
	 */
	public String getInventoryItemName() {
		return languageHandler.getInventoryItemName();
	}


	/**
	 * Get item lore from language specific messages file, with translated color codes
	 *
	 * @return List of strings, one string for each line of lore, or empty list if key not found
	 */
	public List<String> getItemLore() {
		return languageHandler.getItemLore();
	}


	/**
	 * Get spawn display name from language file
	 *
	 * @return the formatted display name for the world spawn, or empty string if key not found
	 */
	public String getSpawnDisplayName() {
		return languageHandler.getSpawnDisplayName();
	}


	/**
	 * Get home display name from language file
	 *
	 * @return the formatted display name for home, or empty string if key not found
	 */
	public String getHomeDisplayName() {
		return languageHandler.getHomeDisplayName();
	}


	/**
	 * Format the time string with days, hours, minutes and seconds as necessary
	 *
	 * @param duration a time duration in milliseconds
	 * @return formatted time string
	 */
	public String getTimeString(final long duration) {
		return languageHandler.getTimeString(duration);
	}


	/**
	 * Format the time string with days, hours, minutes and seconds as necessary, to the granularity passed
	 *
	 * @param duration a time duration in milliseconds
	 * @param timeUnit the time granularity to display (days | hours | minutes | seconds)
	 * @return formatted time string
	 * @throws NullPointerException if parameter is null
	 */
	public String getTimeString(final long duration, final TimeUnit timeUnit) {
		return languageHandler.getTimeString(duration, timeUnit);
	}



	/**
	 * Get string by path in message file
	 * @param path the message path for the string being retrieved
	 * @return String - the string retrieved by path from message file
	 */
	public String getString(final String path) {
		return languageHandler.getString(path);
	}


	/**
	 * Reload messages from configured language file
	 */
	public void reload() {
		languageHandler.reload();
	}

}
