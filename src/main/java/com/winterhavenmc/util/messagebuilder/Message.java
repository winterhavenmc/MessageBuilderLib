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

import com.winterhavenmc.util.messagebuilder.macro.*;
import com.winterhavenmc.util.messagebuilder.macro.processor.ProcessorType;
import com.winterhavenmc.util.messagebuilder.query.MessageRecord;
import com.winterhavenmc.util.messagebuilder.query.QueryHandler;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Optional;

@SuppressWarnings("unused")
public final class Message<MessageId extends Enum<MessageId>, Macro> {

	// string constant
	private final static String UNKNOWN_VALUE = "???";

	// reference to plugin main class
	private final Plugin plugin;

	// context map
	private final ContextMap contextMap = new ContextMap();

	// required parameters
	private final CommandSender recipient;
	private final MessageId messageId;
	private final QueryHandler queryHandler;
	private final MacroHandler macroHandler;

	// optional parameters
	private String altMessage;
	private String altTitle;
	private String altSubtitle;


	/**
	 * Class constructor
	 *
	 * @param plugin       reference to plugin main class
	 * @param queryHandler the query handler for message records
	 * @param macroHandler reference to macro processor class
	 * @param recipient    message recipient
	 * @param messageId    message identifier
	 */
	public Message(final Plugin plugin,
				final QueryHandler queryHandler,
                final MacroHandler macroHandler,
                final CommandSender recipient,
                final MessageId messageId) {

		this.plugin = plugin;
		this.queryHandler = queryHandler;
		this.macroHandler = macroHandler;
		this.recipient = recipient;
		this.messageId = messageId;
	}


	/**
	 * set macro for message replacements
	 *
	 * @param macro token for placeholder
	 * @param value object that contains value that will be substituted in message
	 * @return this message object with macro value set in map
	 */
	@SuppressWarnings("UnusedReturnValue")
	public <T> Message<MessageId, Macro> setMacro(final MacroKey macro, final T value) {

		//TODO: if value is an optional, get unwrapped value. to be reimplemented later. see commented code below

		// create name spaced key
		String key = NamespaceKey.create(macro.toString(), Namespace.Domain.MACRO);

		// get macro expected type from macro enum method
		Class<?> expectedType = macro.getAssociatedType();

		// check the type against the expected type and throw exception if mismatched
		if (!expectedType.isInstance(value)) {
			throw new IllegalArgumentException(
					"Value type does not match the expected type for macro: " + macro +
							". Expected: " + expectedType.getName() +
							", Provided: " + value.getClass().getName());
		}

		// get matching processor type for object
		ProcessorType processorType = ProcessorType.matchType(value);

		// put value and processor type into context map
		this.contextMap.put(key, value, processorType);

		// return this instance of Message class to the builder chain
		return this;
	}

		//TODO: add back optional unwrapping

	/**
	 * Final step of message builder, performs replacements and sends message to recipient
	 */
	public void send() {

		// get optional message record
		Optional<MessageRecord> messageRecord = queryHandler.getMessageRecord(messageId);

		// if message record is empty or not enabled, return
		if (messageRecord.isEmpty() || !messageRecord.get().enabled()) {
			return;
		}

		// get cooldown instance
		MessageCooldown<MessageId> messageCooldown = MessageCooldown.getInstance(plugin);

		// if message is not cooled, do nothing and return
		if (messageCooldown.isCooling(recipient, messageId, messageRecord.get().repeatDelay())) {
			return;
		}

		// send message to player
		if (!this.toString().isEmpty()) {
			recipient.sendMessage(this.toString());
		}

		// if titles enabled in config, display titles
		if (plugin.getConfig().getBoolean("titles-enabled")) {
			displayTitle();
		}

		// if message repeat delay value is greater than zero and recipient is entity, add entry to messageCooldownMap
		if (messageRecord.get().repeatDelay() > 0 && recipient instanceof Entity) {
			messageCooldown.put(messageId, (Entity) recipient);
		}
	}


	private void displayTitle() {

		if (recipient instanceof Entity) {

			ProcessorType processorType = ProcessorType.ENTITY;

			// get title string
			String titleString;
			if (altTitle != null && !altTitle.isEmpty()) {
				titleString = macroHandler.replaceMacros(recipient, contextMap, altTitle);
			}
			else {
				titleString = macroHandler.replaceMacros(recipient, contextMap,
						queryHandler.getMessageRecord(messageId).map(MessageRecord::title).orElse(""));
			}

			// get subtitle string
			String subtitleString;
			if (altSubtitle != null && !altSubtitle.isEmpty()) {
				subtitleString = macroHandler.replaceMacros(recipient, contextMap, altSubtitle);
			}
			else {
				subtitleString = macroHandler.replaceMacros(recipient, contextMap,
						queryHandler.getMessageRecord(messageId).map(MessageRecord::subtitle).orElse(""));
			}

			// only send title if either title string or subtitle string is not empty
			if (!titleString.isEmpty() || !subtitleString.isEmpty()) {

				// get title timing values
				int titleFadeIn = queryHandler.getMessageRecord(messageId).map(MessageRecord::titleFadeIn).orElse(20);
				int titleStay = queryHandler.getMessageRecord(messageId).map(MessageRecord::titleStay).orElse(70);
				int titleFadeOut = queryHandler.getMessageRecord(messageId).map(MessageRecord::titleFadeOut).orElse(10);

				// if title string is empty, add format code, else it won't display with subtitle only
				if (titleString.isEmpty()) {
					titleString = "&r";
				}

				// convert formatting codes
				titleString = ChatColor.translateAlternateColorCodes('&', titleString);
				subtitleString = ChatColor.translateAlternateColorCodes('&', subtitleString);

				// cast recipient to player
				Player player = (Player) recipient;

				// send title to player
				player.sendTitle(titleString, subtitleString, titleFadeIn, titleStay, titleFadeOut);
			}
		}
	}


	/**
	 * performs replacements and returns formatted message string
	 */
	@Override
	public String toString() {

		// get optional MessageRecord
		Optional<MessageRecord> messageRecord = queryHandler.getMessageRecord(messageId);

		// if message entry not found or message not enabled for messageId, return empty string
		if (messageRecord.isEmpty() || !messageRecord.get().enabled()) {
			return "";
		}

		return getMessageString();
	}


	/**
	 * Get formatted message string with macros replaced, using alternate message if set
	 *
	 * @return message string to send
	 */
	private String getMessageString() {

		ProcessorType processorType = ProcessorType.STRING;
		String messageString;

		if (altMessage != null && !altMessage.isEmpty()) {
			messageString = macroHandler.replaceMacros(recipient, contextMap, altMessage);
		}
		else {
			Optional<MessageRecord> messageRecord = queryHandler.getMessageRecord(messageId);

			// if message entry could not be found, then return empty string
			if (messageRecord.isEmpty()) {
				return "";
			}

			messageString = macroHandler.replaceMacros(recipient, contextMap, messageRecord.get().message());
		}
		return ChatColor.translateAlternateColorCodes('&', messageString);
	}


	/**
	 * Set alternate message string
	 *
	 * @param altMessage the alternative message string
	 * @return this message object with alternative message string set
	 */
	public Message<MessageId, Macro> setAltMessage(final String altMessage) {
		if (altMessage != null) {
			this.altMessage = altMessage;
		}
		return this;
	}


	/**
	 * Set alternate title string
	 *
	 * @param altTitle the alternate title string
	 * @return this message object with alternate title string set
	 */
	public Message<MessageId, Macro> setAltTitle(final String altTitle) {
		if (altTitle != null) {
			this.altTitle = altTitle;
		}
		return this;
	}


	/**
	 * Set alternate subtitle string
	 *
	 * @param altSubtitle the alternate subtitle string
	 * @return this message object with alternate title string set
	 */
	public Message<MessageId, Macro> setAltSubtitle(final String altSubtitle) {
		if (altSubtitle != null) {
			this.altSubtitle = altSubtitle;
		}
		return this;
	}

}
