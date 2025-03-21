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

import com.winterhavenmc.util.messagebuilder.pipeline.CooldownMap;
import com.winterhavenmc.util.messagebuilder.pipeline.MessageProcessor;
import com.winterhavenmc.util.messagebuilder.pipeline.MessageRetriever;
import com.winterhavenmc.util.messagebuilder.pipeline.MessageSender;
import com.winterhavenmc.util.messagebuilder.pipeline.TitleSender;
import com.winterhavenmc.util.messagebuilder.resources.QueryHandlerFactory;
import com.winterhavenmc.util.messagebuilder.resources.language.LanguageResourceManager;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.YamlLanguageResourceInstaller;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.YamlLanguageResourceLoader;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.YamlLanguageResourceManager;
import com.winterhavenmc.util.messagebuilder.pipeline.MacroReplacer;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.Section;
import com.winterhavenmc.util.messagebuilder.validation.ValidationException;
import com.winterhavenmc.util.time.Tick;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.time.temporal.TemporalUnit;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

import static com.winterhavenmc.util.messagebuilder.validation.MessageKey.RELOAD_FAILED;
import static com.winterhavenmc.util.messagebuilder.validation.MessageKey.PARAMETER_NULL;
import static com.winterhavenmc.util.messagebuilder.validation.Parameter.*;
import static com.winterhavenmc.util.messagebuilder.validation.Validator.validate;


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
 * {@code messageBuilder.compose(recipient, MessageId.ENABLED_MESSAGE.name())
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
 */
public final class MessageBuilder
{
	private static final String ERROR_BUNDLE_NAME = "language.errors";
	public final static ResourceBundle BUNDLE = ResourceBundle.getBundle(ERROR_BUNDLE_NAME, Locale.getDefault());
	public final static TemporalUnit TICKS = new Tick();

	private final Plugin plugin;
	private final LanguageResourceManager languageResourceManager;
	private final MessageProcessor messageProcessor;


	/**
	 * A private constructor for the class that can only be called from within this class.
	 * Use the static factory method {@code create} to instantiate an instance of this class.
	 *
	 * @param plugin an instance of the plugin
	 * @param languageResourceManager an instance of the language resource manager
	 */
	private MessageBuilder(final Plugin plugin,
	                       final LanguageResourceManager languageResourceManager,
	                       final MessageProcessor messageProcessor)
	{
		this.plugin = plugin;
		this.languageResourceManager = languageResourceManager;
		this.messageProcessor = messageProcessor;
	}


	/**
	 * A static factory method for instantiating this class. Due to the necessity of performing file I/O operations
	 * to instantiate this class, this static method has been provided to perform the potentially blocking operations
	 * before instantiation.The I/O dependencies are then injected into the constructor of the class, which is declared
	 * package-private to prevent instantiation from outside this class except by use of this static factory method.
	 * Finally, if successfully instantiated, an instance of the class will be returned. If file operations fail,
	 * the object will not be left in a partially instantiated state.
	 *
	 * @return an instance of this class
	 */
	public static MessageBuilder create(final Plugin plugin)
	{
		validate(plugin, Objects::isNull, () -> new ValidationException(PARAMETER_NULL, PLUGIN));

		YamlLanguageResourceInstaller resourceInstaller = new YamlLanguageResourceInstaller(plugin);
		YamlLanguageResourceLoader resourceLoader = new YamlLanguageResourceLoader(plugin);
		LanguageResourceManager languageResourceManager = YamlLanguageResourceManager.getInstance(resourceInstaller, resourceLoader);
		QueryHandlerFactory queryHandlerFactory = new QueryHandlerFactory(languageResourceManager.getConfigurationSupplier());
		CooldownMap cooldownMap = new CooldownMap();
		MacroReplacer macroReplacer = new MacroReplacer();
		MessageSender messageSender = new MessageSender(cooldownMap);
		TitleSender titleSender = new TitleSender(cooldownMap);
		MessageRetriever messageRetriever = new MessageRetriever(queryHandlerFactory.getQueryHandler(Section.MESSAGES));
		MessageProcessor messageProcessor = new MessageProcessor(messageRetriever, macroReplacer, cooldownMap, messageSender, titleSender);

		return new MessageBuilder(plugin, languageResourceManager, messageProcessor);
	}


	/**
	 * Class constructor <br> ** FOR TESTING PURPOSES ONLY ** <br>
	 * This constructor is intended only for injecting mocks, for isolated testing of this class, and no other purpose.
	 * Its visibility is restricted to package-private, so it cannot be used to instantiate an instance of the class
	 * from outside its package.
	 *
	 * @param plugin a mock plugin instance
	 * @param languageResourceManager a mock language resource manager instance
	 * @return an instance of this class, instantiated with the mock objects
	 */
	static MessageBuilder test(final Plugin plugin,
         final YamlLanguageResourceManager languageResourceManager,
         final MessageProcessor messageProcessor)
	{
		validate(plugin, Objects::isNull, () -> new ValidationException(PARAMETER_NULL, PLUGIN));
		validate(languageResourceManager, Objects::isNull, () -> new ValidationException(PARAMETER_NULL, LANGUAGE_RESOURCE_MANAGER));
		validate(messageProcessor, Objects::isNull, () -> new ValidationException(PARAMETER_NULL, MESSAGE_PROCESSOR));

		return new MessageBuilder(plugin, languageResourceManager, messageProcessor);
	}


	/**
	 * Initiate the message building sequence
	 *
	 * @param recipient the command sender to whom the message will be sent
	 * @param messageId the message identifier
	 * @return {@code Message} an initialized message object
	 */
	public <MessageId extends Enum<MessageId>>
	Message compose(final CommandSender recipient, final MessageId messageId)
	{
		validate(recipient, Objects::isNull, () -> new ValidationException(PARAMETER_NULL, RECIPIENT));
		validate(messageId, Objects::isNull, () -> new ValidationException(PARAMETER_NULL, MESSAGE_ID));

		return new Message(recipient, messageId.name(), messageProcessor);
	}


	/**
	 * Reload messages from configured language file
	 */
	public void reload()
	{
		if (!languageResourceManager.reload())
		{
			plugin.getLogger().warning(BUNDLE.getString(RELOAD_FAILED.name()));
		}
	}

}
