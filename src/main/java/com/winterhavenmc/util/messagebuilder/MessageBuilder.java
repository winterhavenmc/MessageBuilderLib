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

import com.winterhavenmc.util.messagebuilder.util.LocalizedException;
import com.winterhavenmc.util.time.Tick;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.time.temporal.TemporalUnit;
import java.util.Locale;
import java.util.ResourceBundle;

import static com.winterhavenmc.util.messagebuilder.util.LocalizedException.MessageKey.RELOAD_FAILED;
import static com.winterhavenmc.util.messagebuilder.util.LocalizedException.MessageKey.PARAMETER_NULL;


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

	public static final TemporalUnit TICKS = new Tick();

	private final Plugin plugin;
	private final LanguageResourceManager languageResourceManager;
	private final YamlLanguageQueryHandler languageQueryHandler;
	private final MacroHandler macroQueryHandler;

	// this will be moved to a configuration, but are globally accessible for now
	private static final String ERROR_BUNDLE_NAME = "language.errors";
	public final static ResourceBundle BUNDLE = ResourceBundle.getBundle(ERROR_BUNDLE_NAME, Locale.getDefault());


	/**
	 * Class constructor
	 *
	 * @param plugin reference to plugin main class
	 */
	public MessageBuilder(final Plugin plugin) {
		if (plugin == null) { throw new LocalizedException(PARAMETER_NULL, "plugin"); }

		this.plugin = plugin;
		YamlLanguageResourceInstaller resourceInstaller = new YamlLanguageResourceInstaller(plugin);
		YamlLanguageResourceLoader resourceLoader = new YamlLanguageResourceLoader(plugin);
		this.languageResourceManager = YamlLanguageResourceManager.getInstance(resourceInstaller, resourceLoader);
		this.languageQueryHandler = new YamlLanguageQueryHandler(languageResourceManager.getConfigurationSupplier());
		this.macroQueryHandler = new MacroHandler();
	}


	/**
	 * Class constructor <br> ** FOR TESTING PURPOSES ONLY ** <br>
	 * This constructor is intended only for injecting mocks, for isolated testing of this class, and no other purpose.
	 * Its visibility is restricted to package-private, so it cannot be used to instantiate an instance of the class
	 * from outside its package.
	 *
	 * @param pluginMock reference to plugin main class
	 */
	MessageBuilder(final Plugin pluginMock,
	               final YamlLanguageResourceManager languageResourceManagerMock,
	               final YamlLanguageQueryHandler languageQueryHandlerMock,
	               final MacroHandler macroQueryHandlerMock) {

		this.plugin = pluginMock;
		this.languageResourceManager = languageResourceManagerMock;
		this.languageQueryHandler = languageQueryHandlerMock;
		this.macroQueryHandler = macroQueryHandlerMock;
	}


	/**
	 * Initiate the message building sequence
	 *
	 * @param recipient the command sender to whom the message will be sent
	 * @param messageId the message identifier
	 * @return MessageKey - an initialized message object
	 */
	public Message<MessageId, Macro> compose(final CommandSender recipient, final MessageId messageId) {
		if (recipient == null) { throw new LocalizedException(PARAMETER_NULL, "recipient"); }
		if (messageId == null) { throw new LocalizedException(PARAMETER_NULL, "messageId"); }

		return new Message<>(plugin, languageQueryHandler, macroQueryHandler, recipient, messageId);
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
		if (!languageResourceManager.reload()) {
			plugin.getLogger().warning(BUNDLE.getString(RELOAD_FAILED.name()));
		}
	}

}
