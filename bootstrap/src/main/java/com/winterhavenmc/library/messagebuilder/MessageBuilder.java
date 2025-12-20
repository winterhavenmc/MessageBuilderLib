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

import com.winterhavenmc.library.messagebuilder.adapters.pipeline.resolvers.spawnlocation.BukkitSpawnLocationResolver;
import com.winterhavenmc.library.messagebuilder.adapters.pipeline.resolvers.worldname.BukkitWorldNameResolver;
import com.winterhavenmc.library.messagebuilder.adapters.pipeline.retrievers.spawnlocation.SpawnLocationRetriever;
import com.winterhavenmc.library.messagebuilder.adapters.pipeline.retrievers.worldname.WorldNameRetrieverFactory;
import com.winterhavenmc.library.messagebuilder.adapters.resources.configuration.BukkitConfigRepository;
import com.winterhavenmc.library.messagebuilder.adapters.resources.configuration.BukkitWorldRepository;
import com.winterhavenmc.library.messagebuilder.adapters.resources.language.*;
import com.winterhavenmc.library.messagebuilder.adapters.resources.sound.YamlSoundRepository;
import com.winterhavenmc.library.messagebuilder.adapters.pipeline.MessagePipeline;

import com.winterhavenmc.library.messagebuilder.adapters.resources.sound.YamlSoundResourceManager;
import com.winterhavenmc.library.messagebuilder.core.context.AccessorCtx;
import com.winterhavenmc.library.messagebuilder.core.context.FormatterCtx;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.Pipeline;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.resolvers.spawnlocation.SpawnLocationResolver;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.resolvers.worldname.WorldNameResolver;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.resolvers.worldname.WorldNameRetriever;
import com.winterhavenmc.library.messagebuilder.core.ports.resources.ResourceManager;
import com.winterhavenmc.library.messagebuilder.core.ports.resources.language.*;
import com.winterhavenmc.library.messagebuilder.core.message.Message;
import com.winterhavenmc.library.messagebuilder.core.message.ValidMessage;
import com.winterhavenmc.library.messagebuilder.core.ports.resources.sound.SoundRepository;

import com.winterhavenmc.library.messagebuilder.models.configuration.ConfigRepository;
import com.winterhavenmc.library.messagebuilder.models.configuration.worlds.WorldRepository;
import com.winterhavenmc.library.messagebuilder.models.keys.MessageKey;
import com.winterhavenmc.library.messagebuilder.models.keys.ValidMessageKey;
import com.winterhavenmc.library.messagebuilder.models.recipient.Recipient;
import com.winterhavenmc.library.messagebuilder.models.time.Tick;
import com.winterhavenmc.library.messagebuilder.models.validation.*;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.time.temporal.TemporalUnit;
import java.util.Objects;

import static com.winterhavenmc.library.messagebuilder.BootstrapUtility.*;
import static com.winterhavenmc.library.messagebuilder.models.validation.ErrorMessageKey.PARAMETER_NULL;
import static com.winterhavenmc.library.messagebuilder.models.validation.ErrorMessageKey.RELOAD_FAILED;
import static com.winterhavenmc.library.messagebuilder.models.validation.Parameter.*;
import static com.winterhavenmc.library.messagebuilder.models.validation.Validator.*;


/**
 * A class that implements a builder pattern for messages to be sent to a player or console.
 * <p>
 * This class should be instantiated in a plugin's onEnable method, and the build method {@code compose} is called
 * with a CommandSender as recipient and an enum constant whose name is used to reference the message key defined
 * in the language file. Macro replacements can then be assigned with a chained method call to
 * the {@code setMacro} method, which can be repeated as necessary to set all the macros to be replaced
 * in the message string. Finally, the {@code send} method is called as a final chained method call
 * to effectuate the actual sending of the message.
 * <p>
 * If the message is configured in the language file with a repeat-delay, an entry will be added to
 * the message cooldown map for the player / message, and a task started to remove the entry from the
 * cooldown map after the configured repeat-delay time has elapsed.
 * <p>
 * <i>example:</i>
 * {@snippet lang="java":
 * MessageBuilder messageBuilder = MessageBuilder.create(plugin)
 *
 * messageBuilder.compose(recipient, MessageId.MESSAGE_KEY)
 *     .setMacro(Macro.PLACEHOLDER1, object)
 *     .setMacro(Macro.PLACEHOLDER2, replacementString)
 *     .send();
 * }
 * <p>
 * Note that any object may be passed as the replacement string, which will be converted using
 * that object's toString method. Some placeholders, such as {@code RECIPIENT}, {@code PLUGIN} and {@code SERVER} are
 * automatically populated along with any subfields in all messages and are therefore always
 * available as message placeholders,
 */
public final class MessageBuilder
{
	public final static TemporalUnit TICKS = new Tick();
	private final static String EXCEPTION_MESSAGES = "exception.messages";

	private final Plugin plugin;
	private final ResourceManager languageResourceManager;
	private final ResourceManager soundResourceManager;
	private final ConstantRepository constants;
	private final SoundRepository sounds;
	private final ConfigRepository configRepository;
	private final WorldRepository worlds;
	private final ItemRepository itemRepository;
	private final Pipeline messagePipeline;


	/**
	 * A private constructor for the class that can only be called from within this class.
	 * Use the static factory method {@code create} to instantiate an instance of this class.
	 *
	 * @param plugin                  an instance of the plugin
	 * @param languageResourceManager an instance of the language resource manager
	 */
	private MessageBuilder(final Plugin plugin,
						   final ResourceManager languageResourceManager,
						   final ResourceManager soundResourceManager,
						   final ConstantRepository constants,
						   final SoundRepository sounds,
						   final ConfigRepository configRepository,
						   final WorldRepository worlds,
						   final ItemRepository itemRepository,
						   final Pipeline messagePipeline)
	{
		this.plugin = plugin;
		this.languageResourceManager = languageResourceManager;
		this.soundResourceManager = soundResourceManager;
		this.constants = constants;
		this.sounds = sounds;
		this.configRepository = configRepository;
		this.worlds = worlds;
		this.itemRepository = itemRepository;
		this.messagePipeline = messagePipeline;
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
		// validate parameter
		validate(plugin, Objects::isNull, throwing(PARAMETER_NULL, Parameter.PLUGIN));

		// Create MiniMessage instance
		final MiniMessage miniMessage = MiniMessage.miniMessage();

		// create plugin configuration repository
		final ConfigRepository configRepository = BukkitConfigRepository.create(plugin);

		// Create custom item factory
		CustomItemFactory customItemFactory = new CustomItemFactory(plugin, miniMessage);

		// create language resource manager and repositories
		final LanguageResourceManager languageResourceManager = YamlLanguageResourceManager.create(plugin, configRepository);
		final ConstantRepository constantRepository = new YamlConstantRepository(languageResourceManager);
		final ItemRepository itemRepository = new YamlItemRepository(plugin, languageResourceManager, customItemFactory);
		final MessageRepository messageRepository = new YamlMessageRepository(languageResourceManager);

		// create sound resource manager and repositories
		final ResourceManager soundResourceManager = YamlSoundResourceManager.create(plugin, configRepository);
		final SoundRepository soundRepository = new YamlSoundRepository(plugin, soundResourceManager);

		final WorldNameRetriever worldNameRetriever = WorldNameRetrieverFactory.getWorldNameRetriever(plugin.getServer().getPluginManager().getPlugin("Multiverse-Core"));
		final WorldNameResolver worldNameResolver = BukkitWorldNameResolver.create(plugin, worldNameRetriever);

		final SpawnLocationRetriever spawnLocationRetriever = SpawnLocationRetriever.create(plugin.getServer().getPluginManager().getPlugin("Multiverse-Core"));
		final SpawnLocationResolver spawnLocationResolver = BukkitSpawnLocationResolver.create(spawnLocationRetriever);

		// create world repository
		final WorldRepository worldRepository = BukkitWorldRepository.create(plugin, worldNameResolver, spawnLocationResolver);

		// create context containers
		final FormatterCtx formatterCtx = createFormatterContextContainer(plugin, configRepository, constantRepository, miniMessage);
		final AccessorCtx accessorCtx = createAccessorContextContainer(plugin, itemRepository, formatterCtx);

		// create message pipeline
		final MessagePipeline messagePipeline = MessagePipeline.createMessagePipeline(plugin, messageRepository, soundRepository, formatterCtx, accessorCtx);

		// return instantiation of MessageBuilder library
		return new MessageBuilder(plugin, languageResourceManager, soundResourceManager,
				constantRepository, soundRepository, configRepository, worldRepository, itemRepository, messagePipeline);
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
		// exception thrown if null enum constant passed as messageId parameter
		ValidMessageKey validMessageKey = MessageKey.of(messageId).isValid().orElseThrow(() ->
				new ValidationException(PARAMETER_NULL, Parameter.MESSAGE_ID));

		// return ValidMessage on valid Recipient, else empty no-op message
		return switch (Recipient.of(recipient))
		{
			case Recipient.Valid valid -> new ValidMessage(plugin, valid, validMessageKey, messagePipeline);
			case Recipient.Proxied proxied -> new ValidMessage(plugin, proxied, validMessageKey, messagePipeline);
			case Recipient.Invalid ignored -> Message.empty();
		};
	}


	/**
	 * Reload resources
	 */
	public boolean reload()
	{
		boolean languageResourceResult = languageResourceManager.reload();
		boolean soundResourceResult = soundResourceManager.reload();

		validate(languageResourceResult, bool -> bool.equals(false), logging(LogLevel.WARN, RELOAD_FAILED, LANGUAGE_RESOURCE));
		validate(soundResourceResult, bool -> bool.equals(false), logging(LogLevel.WARN, RELOAD_FAILED, SOUND_RESOURCE));

		return languageResourceResult && soundResourceResult; // both true else false
	}

	enum ReloadStatus
	{
		SUCCESS, FAIL
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
							   final ResourceManager languageResourceManager,
							   final ResourceManager soundResourceManager,
							   final ConstantRepository constantRepository,
							   final SoundRepository soundRepository,
							   final ConfigRepository configRepository,
							   final WorldRepository enabledWorlds,
							   final ItemRepository itemRepository,
							   final MessagePipeline messagePipeline)
	{
		validate(plugin, Objects::isNull, throwing(PARAMETER_NULL, Parameter.PLUGIN));
		validate(languageResourceManager, Objects::isNull, throwing(PARAMETER_NULL, LANGUAGE_RESOURCE_MANAGER));
		validate(soundResourceManager, Objects::isNull, throwing(PARAMETER_NULL, SOUND_RESOURCE_MANAGER));
		validate(messagePipeline, Objects::isNull, throwing(PARAMETER_NULL, MESSAGE_PROCESSOR));

		return new MessageBuilder(plugin, languageResourceManager, soundResourceManager,
				constantRepository, soundRepository, configRepository, enabledWorlds, itemRepository, messagePipeline);
	}


	/**
	 * Provides external access to the config repository
	 */
	public ConfigRepository config()
	{
		return this.configRepository;
	}


	/**
	 * Provides external access to the constant repository
	 */
	public ConstantRepository constants()
	{
		return constants;
	}


	/**
	 * Provides external access to the item repository
	 */
	public ItemRepository items()
	{
		return this.itemRepository;
	}


	/**
	 * Provides external access to the sound repository
	 */
	public SoundRepository sounds()
	{
		return sounds;
	}


	/**
	 * Provides external access to the enabled worlds provider
	 */
	public WorldRepository worlds()
	{
		return worlds;
	}

}
