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

import com.winterhavenmc.util.messagebuilder.languages.*;
import com.winterhavenmc.util.messagebuilder.macro.MacroHandler;
import com.winterhavenmc.util.messagebuilder.query.YamlLangugageFileQueryHandler;
import com.winterhavenmc.util.messagebuilder.query.LanguageFileQueryHandler;
import com.winterhavenmc.util.messagebuilder.util.Error;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;


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
//TODO: ONLY THE RELOAD METHOD NEEDS TESTING FOR FULL COVERAGE OF THIS CLASS
public final class MessageBuilder<MessageId extends Enum<MessageId>, Macro extends Enum<Macro>> {
	private final Plugin plugin;
	private final LanguageHandler languageHandler;
	final LanguageFileQueryHandler queryHandler;
	private final MacroHandler macroHandler;


	/**
	 * Class constructor
	 *
	 * @param plugin reference to plugin main class
	 */
	public MessageBuilder(final Plugin plugin) {
		if (plugin == null) { throw new IllegalArgumentException(Error.Parameter.NULL_PLUGIN.getMessage()); }

		this.plugin = plugin;
		this.languageHandler = new YamlLanguageHandler(plugin, new YamlLanguageFileLoader(plugin));
		this.queryHandler = new YamlLangugageFileQueryHandler(plugin, languageHandler.getConfiguration());
		this.macroHandler = new MacroHandler(plugin, queryHandler);
	}


	/**
	 * Initiate the message building sequence
	 *
	 * @param recipient the command sender to whom the message will be sent
	 * @param messageId the message identifier
	 * @return Message - an initialized message object
	 */
	public Message<MessageId, Macro> compose(final CommandSender recipient, final MessageId messageId) {
		if (recipient == null) { throw new IllegalArgumentException(Error.Parameter.NULL_RECIPIENT.getMessage()); }
		if (messageId == null) { throw new IllegalArgumentException(Error.Parameter.NULL_MESSAGE_ID.getMessage()); }

		return new Message<>(plugin, queryHandler, macroHandler, recipient, messageId);
	}


	/**
	 *  Return an instance of the MessageBuilderToolkit for an advanced interface to MessageBuilder internals
	 * 
	 * @return a new MessageBuilderToolkit instance
	 */
	public Toolkit getToolkit() {
		return new MessageBuilderToolkit<>(this);
	}


	/**
	 * Return an instance of the language file query handler
	 *
	 * @return the query handler for the language file
	 */
	LanguageFileQueryHandler getQueryHandler() {
		return this.queryHandler;
	}


	/**
	 * Reload messages from configured language file
	 */
	public void reload() {
		if (!languageHandler.reload()) {
			plugin.getLogger().warning(Error.LanguageConfiguration.RELOAD_FAILED.getMessage());
		}
	}

}
