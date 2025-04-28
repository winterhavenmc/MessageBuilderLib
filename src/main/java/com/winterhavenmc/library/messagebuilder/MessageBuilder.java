/*
 * Copyright (c) 2022-2025 Tim Savage.
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

package com.winterhavenmc.library.messagebuilder;

import com.winterhavenmc.library.messagebuilder.keys.RecordKey;
import com.winterhavenmc.library.messagebuilder.model.message.Message;
import com.winterhavenmc.library.messagebuilder.model.message.ValidMessage;
import com.winterhavenmc.library.messagebuilder.pipeline.MessagePipeline;
import com.winterhavenmc.library.messagebuilder.model.recipient.Recipient;
import com.winterhavenmc.library.messagebuilder.query.QueryHandlerFactory;
import com.winterhavenmc.library.messagebuilder.resources.configuration.LocaleProvider;
import com.winterhavenmc.library.messagebuilder.resources.language.SectionResourceManager;
import com.winterhavenmc.library.messagebuilder.resources.language.LanguageResourceManager;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.AdapterContextContainer;
import com.winterhavenmc.library.messagebuilder.pipeline.resolvers.FormatterContainer;
import com.winterhavenmc.library.messagebuilder.validation.ValidationException;
import com.winterhavenmc.library.time.Tick;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.time.temporal.TemporalUnit;
import java.util.Objects;
import java.util.ResourceBundle;

import static com.winterhavenmc.library.messagebuilder.Orchestrator.*;
import static com.winterhavenmc.library.messagebuilder.validation.ErrorMessageKey.PARAMETER_NULL;
import static com.winterhavenmc.library.messagebuilder.validation.ErrorMessageKey.RELOAD_FAILED;
import static com.winterhavenmc.library.messagebuilder.validation.Parameter.*;
import static com.winterhavenmc.library.messagebuilder.validation.ValidationHandler.throwing;
import static com.winterhavenmc.library.messagebuilder.validation.Validator.validate;


/**
 * A class that implements a builder pattern for messages to be sent to a player or console.
 * <p>
 * It should be instantiated in a plugin's onEnable method, and the build method is called
 * with a CommandSender as recipient and an E enum member to reference the message defined
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
 * {@code
 * MessageBuilder messageBuilder = MessageBuilder.create(plugin)
 *
 * messageBuilder.compose(recipient, MessageId.MESSAGE_KEY)
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
	public final static TemporalUnit TICKS = new Tick();
	private final static String EXCEPTION_MESSAGES = "exception.messages";
	private final ResourceBundle BUNDLE;

	private final Plugin plugin;
	private final SectionResourceManager languageResourceManager;
	private final MessagePipeline messagePipeline;


	/**
	 * A private constructor for the class that can only be called from within this class.
	 * Use the static factory method {@code create} to instantiate an instance of this class.
	 *
	 * @param plugin an instance of the plugin
	 * @param languageResourceManager an instance of the language resource manager
	 */
	private MessageBuilder(final Plugin plugin,
	                       final SectionResourceManager languageResourceManager,
	                       final MessagePipeline messagePipeline)
	{
		this.plugin = plugin;
		this.languageResourceManager = languageResourceManager;
		this.messagePipeline = messagePipeline;
		BUNDLE = ResourceBundle.getBundle(EXCEPTION_MESSAGES, LocaleProvider.create(plugin).getLocale());
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
		validate(plugin, Objects::isNull, throwing(PARAMETER_NULL, PLUGIN));

		final SectionResourceManager languageResourceManager = getLanguageResourceManager(plugin);
		final QueryHandlerFactory queryHandlerFactory = new QueryHandlerFactory(languageResourceManager);
		final FormatterContainer formatterContainer = getResolverContextContainer(plugin, queryHandlerFactory);
		final AdapterContextContainer adapterContextContainer = getAdapterContextContainer(plugin);
		final MessagePipeline messagePipeline = getMessagePipeline(queryHandlerFactory, formatterContainer, adapterContextContainer);

		return new MessageBuilder(plugin, languageResourceManager, messagePipeline);
	}

	/**
	 * Initiate the message building sequence. Parameters of this method are passed into this library domain
	 * from the plugin, so robust validation is employed and type-safety is enforced by converting to domain specific
	 * types and validating on creation. If a null {@code CommandSender} is passed as the recipient parameter, an
	 * empty, no-op message is returned, as it is plausible a null or invalid value could be passed.
	 * The enum constant messageId parameter, conversely, cannot be null under normal conditions, and therefore
	 * throws a validation exception if a null is encountered.
	 *
	 * @param recipient the command sender to whom the message will be sent
	 * @param messageId the message identifier enum constant
	 * @return {@code Message} an initialized message object
	 */
	public <E extends Enum<E>> Message compose(final CommandSender recipient, final E messageId)
	{
		// exception thrown if null enum constant passed in messageId parameter
		RecordKey validMessageKey = RecordKey.of(messageId)
				.orElseThrow(() -> new ValidationException(PARAMETER_NULL, MESSAGE_ID));

		// return ValidMessage on valid Recipient, else empty no-op message
		return switch (Recipient.of(recipient))
		{
			case Recipient.Valid valid -> new ValidMessage(valid, validMessageKey, messagePipeline);
			case Recipient.Proxied ignored -> Message.empty();
			case Recipient.Invalid ignored -> Message.empty();
		};
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
							   final LanguageResourceManager languageResourceManager,
							   final MessagePipeline messagePipeline)
	{
		validate(plugin, Objects::isNull, throwing(PARAMETER_NULL, PLUGIN));
		validate(languageResourceManager, Objects::isNull, throwing(PARAMETER_NULL, LANGUAGE_RESOURCE_MANAGER));
		validate(messagePipeline, Objects::isNull, throwing(PARAMETER_NULL, MESSAGE_PROCESSOR));

		return new MessageBuilder(plugin, languageResourceManager, messagePipeline);
	}

}
