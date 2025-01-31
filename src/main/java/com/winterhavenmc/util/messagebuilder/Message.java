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
import com.winterhavenmc.util.messagebuilder.cooldown.CooldownMap;
import com.winterhavenmc.util.messagebuilder.macro.MacroReplacer;
import com.winterhavenmc.util.messagebuilder.pipeline.*;
import com.winterhavenmc.util.messagebuilder.resources.language.LanguageQueryHandler;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.messages.MessageRecord;

import com.winterhavenmc.util.messagebuilder.util.LocalizedException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

import static com.winterhavenmc.util.messagebuilder.util.LocalizedException.MessageKey.PARAMETER_NULL;
import static com.winterhavenmc.util.messagebuilder.util.LocalizedException.Parameter.*;


/**
 * The message object being built with builder pattern
 *
 * @param <MessageId> the unique identifier of a message in the language file
 * @param <Macro> a macro placeholder value to be added to the context map
 */
public final class Message<MessageId extends Enum<MessageId>, Macro extends Enum<Macro>> {

	// context map
	private final ContextMap contextMap;

	// required parameters
	private final CommandSender recipient;
	private final String messageId;
	private final LanguageQueryHandler languageQueryHandler;
	private final MacroReplacer macroReplacer;
	private final CooldownMap cooldownMap;


	/**
	 * Class constructor
	 *
	 * @param languageQueryHandler the ItemRecord handler for message records
	 * @param macroReplacer        reference to macro processor class
	 * @param recipient            message recipient
	 * @param messageId            message identifier
	 */
	public Message(
			final LanguageQueryHandler languageQueryHandler,
			final MacroReplacer macroReplacer,
			final CommandSender recipient,
			final String messageId,
			final CooldownMap cooldownMap
	)
	{
		this.languageQueryHandler = languageQueryHandler;
		this.macroReplacer = macroReplacer;
		this.recipient = recipient;
		this.messageId = messageId;
		this.cooldownMap = cooldownMap;
		this.contextMap = new ContextMap(recipient, messageId);
	}


	/**
	 * set macro for message replacements
	 *
	 * @param macro token for placeholder
	 * @param value object that contains value that will be substituted in message
	 * @return this message object with macro value set in map
	 */
	public <T> Message<MessageId, Macro> setMacro(final Macro macro, final T value)
	{
		if (macro == null) { throw new LocalizedException(PARAMETER_NULL, MACRO); }
		if (value == null) { throw new LocalizedException(PARAMETER_NULL, VALUE); }

		return setMacro(1, macro, value);
	}


	/**
	 * set macro for message replacements
	 *
	 * @param macro token for placeholder
	 * @param value object that contains value that will be substituted in message
	 * @return this message object with macro value set in map
	 */
	public <T> Message<MessageId, Macro> setMacro(int quantity, final Macro macro, final T value)
	{
		if (macro == null) { throw new LocalizedException(PARAMETER_NULL, MACRO); }
		if (value == null) { throw new LocalizedException(PARAMETER_NULL, VALUE); }

		// use macro enum constant name as key
		String key = macro.name();

		// put value into context map
		this.contextMap.put(key, value);
		this.contextMap.put(key + ".QUANTITY", quantity);

		// return this instance of Message class to the builder chain
		return this;
	}


	/**
	 * Final step of message builder, performs replacements and sends message to recipient
	 */
	public void send()
	{
		// get functional message retriever
		Retriever retriever = new MessageRetriever();

		// get optional message record
		Optional<MessageRecord> messageRecord = retriever.getRecord(messageId, languageQueryHandler);

		// if optional message record is empty, do nothing and return
		if (messageRecord.isEmpty()) {
			return;
		}

		// check if message and recipient are available for sending
		if (!isSendable(recipient, messageRecord.get())) {
			return;
		}

		// perform macro replacements
		Optional<MessageRecord> finalMesssageRecord = macroReplacer.replaceMacros(messageRecord.get(), contextMap);

		// send message
		finalMesssageRecord.ifPresent(record -> new MessageSender().send(recipient, record));
		finalMesssageRecord.ifPresent(record -> new TitleSender().send(recipient, record));
		finalMesssageRecord.ifPresent(record -> cooldownMap.putExpirationTime(recipient, record));
	}


	/**
	 * Check if prerequisites have been met for a message to be able to be sent
	 *
	 * @param recipient the intended recipient of the message
	 * @param messageRecord the message record
	 * @return {@code true} if the recipient/message is sendable, {@code false} if not
	 */
	boolean isSendable(final CommandSender recipient, MessageRecord messageRecord) {

		// if recipient is a player but is not online, return false
		if (recipient instanceof Player player && !player.isOnline()) {
			return false;
		}

		// return true if message is enabled and not in cooldown map, else false
		return messageRecord.enabled() && !cooldownMap.isCooling(recipient, messageRecord.messageId());
	}


	/**
	 * Examine the contents of the context map for testing purposes
	 *
	 * @param macro the key to retrieve from the context map
	 * @return {@code Object} the value stored in the map, or {@code null} if no value is present for key
	 */
	Object peek(Macro macro) {
		return contextMap.get(macro.name());
	}

}
