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

import com.winterhavenmc.util.messagebuilder.macro.MacroProcessorHandler;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Optional;


/**
 * A class that implements a builder pattern for messages to be sent to a player or console.
 * <p>
 * It should be instantiated in a plugin's onEnable method, and the build method is called
 * with a CommandSender as recipient and a MessageId enum member to reference the message defined
 * in the language file. Macro replacements can then be assigned with a chained method call to
 * the setMacro method, which can be repeated as necessary to set all the macros to be replaced
 * in the message string. Finally, the send method is called, usually as a final chained method call
 * to effectuate the actual sending of the message.
 *
 * If the message is configured in the language file with a repeat-delay, an entry will be added to
 * the message cooldown map for the player / message, and a task started to remove the entry from the
 * cooldown map after the configured repeat-delay time has elapsed.
 * <p>
 * Example:
 * <pre>
 *     messageBuilder.compose(recipient, MessageId.MESSAGE_TO_SEND)
 *         .setMacro(Macro.PLACEHOLDER1, object)
 *         .setMacro(Macro.PLACEHOLDER2, replacementString)
 *         .send();
 * </pre>
 *
 * Note that any object may be passed as the replacement string, which will be converted using
 * that object's toString method, except in the case of some placeholder keys that are treated
 * specially by the doMacroReplacements method. Special keys are:
 * ITEM or ITEM_NAME, ITEM_QUANTITY, WORLD or WORLD_NAME, PLAYER or PLAYER_NAME, LOCATION or PLAYER_LOCATION,
 * DURATION or DURATION_MINUTES
 *
 * @param <MessageId> An enum whose members correspond to a message key in a language file
 * @param <Macro> An enum whose members correspond to a string replacement placeholder in a message string
 */
@SuppressWarnings("unused")
public final class MessageBuilder<MessageId extends Enum<MessageId>, Macro extends Enum<Macro>> {

	private final LanguageHandler languageHandler;
	private final JavaPlugin plugin;
	private final MacroProcessorHandler macroProcessorHandler;

	/**
	 * Class constructor
	 *
	 * @param plugin reference to plugin main class
	 */
	public MessageBuilder(final JavaPlugin plugin) {
		this.plugin = plugin;
		this.languageHandler = new YamlLanguageHandler(plugin);
		this.macroProcessorHandler = new MacroProcessorHandler(plugin, languageHandler);
	}


	/**
	 * Initiate a message
	 *
	 * @param recipient the command sender to whom the message will be sent
	 * @param messageId the message identifier
	 * @return Message - an initialized message object
	 * @deprecated use compose method instead
	 */
	@Deprecated
	public Message<MessageId, Macro> build(final CommandSender recipient, final MessageId messageId) {
		return new Message<>(plugin, languageHandler, macroProcessorHandler, recipient, messageId);
	}


	/**
	 * Initiate a message
	 * @param recipient the command sender to whom the message will be sent
	 * @param messageId the message identifier
	 * @return Message - an initialized message object
	 */
	public Message<MessageId, Macro> compose(final CommandSender recipient, final MessageId messageId) {
		return new Message<>(plugin, languageHandler, macroProcessorHandler, recipient, messageId);
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
	public Optional<String> getItemName() {
		return languageHandler.getItemName();
	}


	/**
	 * Get configured plural item name from language file
	 *
	 * @return the formatted item plural name from language file, or empty string if key not found
	 */
	public Optional<String> getItemNamePlural() {
		return languageHandler.getItemNamePlural();
	}


	/**
	 * Get configured inventory item name from language file
	 *
	 * @return the formatted inventory display name of an item, as a String wrapped in an {@link Optional}
	 */
	public Optional<String> getInventoryItemName() {
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
	 * @return the formatted display name for the world spawn, as a String wrapped in an {@link Optional}
	 */
	public Optional<String> getSpawnDisplayName() {
		return languageHandler.getSpawnDisplayName();
	}


	/**
	 * Get home display name from language file
	 *
	 * @return the formatted display name for home, as a String wrapped in an {@link Optional}
	 */
	public Optional<String> getHomeDisplayName() {
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
	 */
	public String getTimeString(final long duration, final TimeUnit timeUnit) {
		return languageHandler.getTimeString(duration, timeUnit);
	}


	/**
	 * Get string by path in message file
	 * @param path the message path for the string being retrieved
	 * @return String - the string retrieved by path from message file, wrapped in an {@link Optional}
	 */
	public Optional<String> getString(final String path) {
		return languageHandler.getString(path);
	}


	/**
	 * Get List of String by path in message file
	 * @param path the message path for the string list being retrieved
	 * @return List of String - the string list retrieved by path from message file
	 */
	public List<String> getStringList(final String path) {
		return languageHandler.getStringList(path);
	}


	public Optional<String> getWorldName(final World world) {
		return languageHandler.getWorldName(world);
	}

	/**
	 * Reload messages from configured language file
	 */
	public void reload() {
		languageHandler.reload();
	}

}
