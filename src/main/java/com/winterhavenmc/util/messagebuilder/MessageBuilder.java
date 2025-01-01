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
import com.winterhavenmc.util.messagebuilder.languages.*;
import com.winterhavenmc.util.messagebuilder.macro.MacroHandler;
import com.winterhavenmc.util.messagebuilder.query.ConfigurationQueryHandler;
import com.winterhavenmc.util.messagebuilder.query.ItemRecord;
import com.winterhavenmc.util.messagebuilder.query.MessageRecord;
import com.winterhavenmc.util.messagebuilder.query.QueryHandler;
import com.winterhavenmc.util.messagebuilder.util.Error;
import com.winterhavenmc.util.messagebuilder.util.WorldNameUtility;

import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.util.Collections;
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
 * <p>
 * If the message is configured in the language file with a repeat-delay, an entry will be added to
 * the message cooldown map for the player / message, and a task started to remove the entry from the
 * cooldown map after the configured repeat-delay time has elapsed.
 * <p>
 * <i>example:</i>
 * <pre>
 * {@code messageBuilder.compose(recipient, MessageId.ENABLED_MESSAGE)
 *     .setMacro(Macro.PLACEHOLDER1, object)
 *     .setMacro(Macro.PLACEHOLDER2, replacementString)
 *     .send();
 * }
 * </pre>
 * <p>
 * Note that any object may be passed as the replacement string, which will be converted using
 * that object's toString method, except in the case of some placeholder keys that are treated
 * specially by the doMacroReplacements method. Special keys are:
 * ITEM or ITEM_NAME, ITEM_QUANTITY, WORLD or WORLD_NAME, PLAYER or PLAYER_NAME, LOCATION or PLAYER_LOCATION,
 * DURATION or DURATION_MINUTES
 *
 * @param <MessageId> An enum whose members correspond to a message key in a language file
 * @param <Macro>     An enum whose members correspond to a string replacement placeholder in a message string
 */
public final class MessageBuilder<MessageId extends Enum<MessageId>, Macro extends Enum<Macro>> {

	private final static String DEFAULT = "DEFAULT";

	private final Plugin plugin;
	private final LanguageHandler languageHandler;
	private final QueryHandler queryHandler;
	private final MacroHandler macroHandler;
	private final WorldNameUtility worldNameUtility;


	/**
	 * Class constructor
	 *
	 * @param plugin reference to plugin main class
	 */
	public MessageBuilder(final Plugin plugin) {
		this.plugin = plugin;
		this.languageHandler = new YamlLanguageHandler(plugin, new YamlLanguageFileLoader(plugin));
		this.queryHandler = new ConfigurationQueryHandler(plugin, languageHandler.getConfiguration());
		this.macroHandler = new MacroHandler(plugin, queryHandler);
		this.worldNameUtility = new WorldNameUtility(plugin.getServer().getPluginManager());
	}


	/**
	 * Set both delimiters to the same specific character
	 *
	 * @param character the character to use for both delimiters
	 */
	public void setDelimiters(final Character character) {
		MacroHandler.MacroDelimiter.LEFT.set(character);
		MacroHandler.MacroDelimiter.RIGHT.set(character);
	}


	/**
	 * Set delimiters to unique characters by passing two parameters
	 *
	 * @param leftCharacter  the character to use for the left delimiter
	 * @param rightCharacter the character to use for the right delimiter
	 */
	public void setDelimiters(final Character leftCharacter, final Character rightCharacter) {
		MacroHandler.MacroDelimiter.LEFT.set(leftCharacter);
		MacroHandler.MacroDelimiter.RIGHT.set(rightCharacter);
	}


	/**
	 * Initiate a message
	 *
	 * @param recipient the command sender to whom the message will be sent
	 * @param messageId the message identifier
	 * @return Message - an initialized message object
	 */
	public Message<MessageId, Macro> compose(final CommandSender recipient, final MessageId messageId) {
		if (messageId == null) {
			throw new IllegalArgumentException(Error.Parameter.NULL_MESSAGE_ID.getMessage());
		}
		return new Message<>(plugin, queryHandler, macroHandler, recipient, messageId);
	}


	/**
	 * Check if a message is enabled in the configuration file
	 *
	 * @param messageId the message identifier
	 * @return true if message is enabled, false if not
	 */
	public boolean isEnabled(final MessageId messageId) {
		if (messageId == null) {
			throw new IllegalArgumentException(Error.Parameter.NULL_MESSAGE_ID.getMessage());
		}
		return queryHandler.getMessageRecord(messageId).map(MessageRecord::enabled).orElse(false);
	}


	/**
	 * Get the configured repeat delay for a message
	 *
	 * @param messageId the message identifier
	 * @return long - the message repeat delay (in seconds)
	 */
	public long getRepeatDelay(final MessageId messageId) {
		if (messageId == null) {
			throw new IllegalArgumentException(Error.Parameter.NULL_MESSAGE_ID.getMessage());
		}
		return queryHandler.getMessageRecord(messageId).map(MessageRecord::repeatDelay).orElse(0L);
	}


	/**
	 * get message text from language file
	 *
	 * @param messageId the message identifier
	 * @return String message text, or empty string if no message string found
	 */
	public String getMessage(final MessageId messageId) {
		if (messageId == null) {
			throw new IllegalArgumentException(Error.Parameter.NULL_MESSAGE_ID.getMessage());
		}
		return queryHandler.getMessageRecord(messageId).map(MessageRecord::message).orElse("");
	}


	/**
	 * Get DEFAULT item name from language specific messages file
	 *
	 * @return item name from language file, or empty {@link Optional} if key not found
	 */
	public Optional<String> getItemNameSingular() {
		return getItemNameSingular(DEFAULT);
	}


	/**
	 * Get item name from language specific messages file
	 *
	 * @return the item name from language file, or empty {@link Optional} if key not found
	 */
	public Optional<String> getItemNameSingular(final String itemKey) {
		if (itemKey == null) {
			throw new IllegalArgumentException(Error.Parameter.NULL_ITEM_KEY.getMessage());
		}
		return queryHandler.getItemRecord(itemKey).flatMap(ItemRecord::itemName);
	}


	/**
	 * Get configured plural item name from language file
	 *
	 * @return the item plural name from language file, or empty {@link Optional} if entry not found for key
	 */
	public Optional<String> getItemNamePlural() {
		return getItemNamePlural(DEFAULT);
	}


	/**
	 * Get configured plural item name from language file
	 *
	 * @return the item plural name from language file, or empty {@link Optional} if entry not found for key
	 */
	public Optional<String> getItemNamePlural(final String itemKey) {
		if (itemKey == null) {
			throw new IllegalArgumentException(Error.Parameter.NULL_ITEM_KEY.getMessage());
		}
		return queryHandler.getItemRecord(itemKey).flatMap(ItemRecord::itemNamePlural);
	}


	/**
	 * Get configured inventory item name from language file
	 *
	 * @return the inventory display name of an item, as a String wrapped in an {@link Optional}
	 */
	public Optional<String> getInventoryItemNameSingular() {
		return getInventoryItemNameSingular(DEFAULT);
	}


	/**
	 * Get configured inventory item name from language file
	 *
	 * @return the inventory display name of an item, as a String wrapped in an {@link Optional}
	 */
	public Optional<String> getInventoryItemNameSingular(final String itemKey) {
		if (itemKey == null) {
			throw new IllegalArgumentException(Error.Parameter.NULL_ITEM_KEY.getMessage());
		}
		return queryHandler.getItemRecord(itemKey).flatMap(ItemRecord::itemName);
	}


	/**
	 * Get configured inventory item name from language file
	 *
	 * @return the inventory display name of an item, as a String wrapped in an {@link Optional}
	 */
	public Optional<String> getInventoryItemNamePlural() {
		return getInventoryItemNamePlural(DEFAULT);
	}

	/**
	 * Get configured inventory item name from language file
	 *
	 * @return the inventory display name of an item, as a String wrapped in an {@link Optional}
	 */
	public Optional<String> getInventoryItemNamePlural(final String itemKey) {
		if (itemKey == null) {
			throw new IllegalArgumentException(Error.Parameter.NULL_ITEM_KEY.getMessage());
		}
		return queryHandler.getItemRecord(itemKey).flatMap(ItemRecord::itemNamePlural);
	}


	/**
	 * Get item lore from language specific messages file, with translated color codes
	 *
	 * @return {@link List}&lt;{@link String}&gt; of strings, one string for each line of lore, or empty list if not found
	 */
	public List<String> getItemLore() {
		return getItemLore(DEFAULT);
	}


	/**
	 * Get item lore from language specific messages file, with translated color codes
	 *
	 * @return {@link List}&lt;{@link String}&gt; of strings, one string for each line of lore, or empty list if not found
	 */
	public List<String> getItemLore(final String itemKey) {
		if (itemKey == null) {
			throw new IllegalArgumentException(Error.Parameter.NULL_ITEM_KEY.getMessage());
		}
		Optional<ItemRecord> itemRecord = queryHandler.getItemRecord(itemKey);
		if (itemRecord.isEmpty()) {
			return Collections.emptyList();
		}
		return itemRecord.get().itemLore();
	}


	/**
	 * Get spawn display name from language file
	 *
	 * @return the formatted display name for the world spawn, as a String wrapped in an {@link Optional}
	 */
	public Optional<String> getSpawnDisplayName() {
		return queryHandler.getSpawnDisplayName();
	}


	/**
	 * Get home display name from language file
	 *
	 * @return the formatted display name for home, as a String wrapped in an {@link Optional}
	 */
	public Optional<String> getHomeDisplayName() {
		return queryHandler.getHomeDisplayName();
	}


	/**
	 * Format the time string with days, hours, minutes and seconds as necessary
	 *
	 * @param duration a time duration in milliseconds
	 * @return {@link String} &ndash; formatted time string
	 */
	public String getTimeString(final long duration) {
		return queryHandler.getTimeString(duration);
	}


	/**
	 * Format the time string with days, hours, minutes and seconds as necessary, to the granularity passed
	 *
	 * @param duration a time duration in milliseconds
	 * @param timeUnit the time granularity to display (days | hours | minutes | seconds)
	 * @return {@link String} &ndash; formatted time string
	 */
	public String getTimeString(final long duration, final TimeUnit timeUnit) {
		return queryHandler.getTimeString(duration, timeUnit);
	}


	/**
	 * Get string by path in message file
	 *
	 * @param path the message path for the string being retrieved
	 * @return {@link String} &ndash; the string retrieved by path from message file, wrapped in an {@link Optional}
	 */
	public Optional<String> getString(final String path) {
		return queryHandler.getString(path);
	}


	/**
	 * Get List of String by path in message file
	 *
	 * @param path the message path for the string list being retrieved
	 * @return List of String - the string list retrieved by path from message file
	 */
	public List<String> getStringList(final String path) {
		return queryHandler.getStringList(path);
	}


	/**
	 * Get optional string of world name or multiverse alias if available
	 *
	 * @param world the world to retrieve name
	 * @return Optional String containing world name or multiverse alias
	 */
	public Optional<String> getWorldName(final World world) {
		return worldNameUtility.getWorldName(world);
	}

	/**
	 * Reload messages from configured language file
	 */
	public void reload() {
		languageHandler.reload();
	}

}
