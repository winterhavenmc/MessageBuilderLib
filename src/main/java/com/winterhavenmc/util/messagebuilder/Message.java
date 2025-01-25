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

import com.winterhavenmc.util.messagebuilder.context.ContextMap;
import com.winterhavenmc.util.messagebuilder.resources.language.LanguageQueryHandler;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.Section;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.messages.MessageSectionQueryHandler;
import com.winterhavenmc.util.messagebuilder.macro.*;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.messages.MessageRecord;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Optional;


/**
 * The message object being built with builder pattern
 *
 * @param <MessageId> the unique identifier of a message in the language file
 * @param <Macro> a macro placeholder value to be added to the context map
 */
public final class Message<MessageId extends Enum<MessageId>, Macro> {

	// reference to plugin main class
	private final Plugin plugin;

	// context map
	private final ContextMap contextMap;

	// required parameters
	private final CommandSender recipient;
	private final MessageId messageId;
	private final LanguageQueryHandler languageQueryHandler;
	private final MacroHandler macroHandler;


	/**
	 * Class constructor
	 *
	 * @param plugin       reference to plugin main class
	 * @param languageQueryHandler the ItemRecord handler for message records
	 * @param macroHandler reference to macro processor class
	 * @param recipient    message recipient
	 * @param messageId    message identifier
	 */
	public Message(final Plugin plugin,
				final LanguageQueryHandler languageQueryHandler,
                final MacroHandler macroHandler,
                final CommandSender recipient,
                final MessageId messageId) {

		this.plugin = plugin;
		this.languageQueryHandler = languageQueryHandler;
		this.macroHandler = macroHandler;
		this.recipient = recipient;
		this.messageId = messageId;
		this.contextMap = new ContextMap(recipient);
	}


	/**
	 * set macro for message replacements
	 *
	 * @param macro token for placeholder
	 * @param value object that contains value that will be substituted in message
	 * @return this message object with macro value set in map
	 */
	public <T> Message<MessageId, Macro> setMacro(final Macro macro, final T value) {

		// use macro toString value as key
		String key = macro.toString();

//		// get macro expected type from macro enum method
//		var handledType = ProcessorType.matchType(value).getHandledType();
//
//		// check the type against the expected type and throw exception if mismatched
//		if (!handledType.isInstance(unwrappedValue)) {
//			throw new IllegalArgumentException(
//					"Value type does not match the expected type for macro: " + macro +
//							". Expected: " + handledType.getName() +
//							", Provided: " + unwrappedValue.getClass().getName());
//		}
//
//		// get matching processor type for object
//		ProcessorType processorType = ProcessorType.matchType(unwrappedValue);

		// put value and processor type into context map
		this.contextMap.put(key, value);

		// return this instance of Message class to the builder chain
		return this;
	}


	/**
	 * Final step of message builder, performs replacements and sends message to recipient
	 */
	public void send() {
		// get message query handler
		MessageSectionQueryHandler sectionQueryHandler = (MessageSectionQueryHandler) languageQueryHandler.getSectionQueryHandler(Section.MESSAGES);
		if (sectionQueryHandler instanceof MessageSectionQueryHandler messageSectionQueryHandler) {

			// get optional message record
			Optional<MessageRecord> messageRecord = messageSectionQueryHandler.getRecord(messageId);

			// if message record is empty or not enabled, return
			if (messageRecord.isEmpty() || !messageRecord.get().enabled()) {
				return;
			}

			// get cooldown instance
			MessageCooldownMap<MessageId> messageCooldownMap = MessageCooldownMap.getInstance(plugin);

			// if message is not cooled, do nothing and return
			if (messageCooldownMap.isCooling(recipient, messageId, messageRecord.get().repeatDelay())) {
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
				messageCooldownMap.put(messageId, (Entity) recipient);
			}
		}
	}


	private void displayTitle() {

		if (recipient instanceof Entity) {

			// get title string
			String titleString;

			titleString = macroHandler.replaceMacros(recipient, contextMap,
					languageQueryHandler.getMessageRecord(messageId).map(MessageRecord::title).orElse(""));

			// get subtitle string
			String subtitleString;
				subtitleString = macroHandler.replaceMacros(recipient, contextMap,
						languageQueryHandler.getMessageRecord(messageId).map(MessageRecord::subtitle).orElse(""));

			// only send title if either title string or subtitle string is not empty
			if (!titleString.isEmpty() || !subtitleString.isEmpty()) {

				// get title timing values
				int titleFadeIn = languageQueryHandler.getMessageRecord(messageId).map(MessageRecord::titleFadeIn).orElse(20);
				int titleStay = languageQueryHandler.getMessageRecord(messageId).map(MessageRecord::titleStay).orElse(70);
				int titleFadeOut = languageQueryHandler.getMessageRecord(messageId).map(MessageRecord::titleFadeOut).orElse(10);

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
		Optional<MessageRecord> messageRecord = languageQueryHandler.getMessageRecord(messageId);

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

		String messageString;

		Optional<MessageRecord> messageRecord = languageQueryHandler.getMessageRecord(messageId);

		// if message entry could not be found, then return empty string
		if (messageRecord.isEmpty()) {
			return "";
		}

		messageString = macroHandler.replaceMacros(recipient, contextMap, messageRecord.get().message());

		return ChatColor.translateAlternateColorCodes('&', messageString);
	}

}
