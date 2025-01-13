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

import com.winterhavenmc.util.messagebuilder.resources.language.*;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.*;
import com.winterhavenmc.util.messagebuilder.macro.MacroHandler;
import com.winterhavenmc.util.messagebuilder.util.Error;

import com.winterhavenmc.util.messagebuilder.util.Toolkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.util.logging.Logger;


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

	private final Plugin plugin;
	private static Logger pluginLogger;
	private final LanguageResourceHandler languageResourceHandler;
	private final LanguageQueryHandler languageQueryHandler;
	private final MacroHandler macroQueryHandler;


	/**
	 * Class constructor
	 *
	 * @param plugin reference to plugin main class
	 */
	public MessageBuilder(final Plugin plugin) {
		if (plugin == null) { throw new IllegalArgumentException(Error.Parameter.NULL_PLUGIN.getMessage()); }

		this.plugin = plugin;
		MessageBuilder.pluginLogger = plugin.getLogger();
		this.languageResourceHandler = new YamlLanguageResourceHandler(plugin.getConfig(), new YamlLanguageResourceLoader(plugin));
		this.languageQueryHandler = new YamlLanguageQueryHandler(languageResourceHandler.getConfigurationSupplier());
		this.macroQueryHandler = new MacroHandler(languageQueryHandler);
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

		return new Message<>(plugin, languageQueryHandler, macroQueryHandler, recipient, messageId);
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
	 * Return an instance of the language file ItemRecord handler
	 *
	 * @return the ItemRecord handler for the language file
	 */
	LanguageQueryHandler getLanguageQueryHandler() {
		return this.languageQueryHandler;
	}


	/**
	 * Reload messages from configured language file
	 */
	public void reload() {
		if (!languageResourceHandler.reload()) {
			plugin.getLogger().warning(Error.LanguageConfiguration.RELOAD_FAILED.getMessage());
		}
	}


	/**
	 * Static method to retrieve an instance of the plugin logger
	 * @return the plugin logger
	 */
	public static Logger getPluginLogger() {
		return pluginLogger;
	}

}
