/*
 * Copyright (c) 2025 Tim Savage.
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

import com.winterhavenmc.library.messagebuilder.adapters.pipeline.accessors.MacroFieldAccessor;
import com.winterhavenmc.library.messagebuilder.adapters.pipeline.cooldown.MessageCooldownMap;
import com.winterhavenmc.library.messagebuilder.adapters.pipeline.formatters.duration.LocalizedDurationFormatter;
import com.winterhavenmc.library.messagebuilder.adapters.pipeline.formatters.duration.Time4jDurationFormatter;
import com.winterhavenmc.library.messagebuilder.adapters.pipeline.formatters.number.LocaleNumberFormatter;
import com.winterhavenmc.library.messagebuilder.adapters.pipeline.matchers.RegexPlaceholderMatcher;
import com.winterhavenmc.library.messagebuilder.adapters.pipeline.processors.MessageProcessor;
import com.winterhavenmc.library.messagebuilder.adapters.pipeline.resolvers.value.MacroValueResolver;
import com.winterhavenmc.library.messagebuilder.adapters.pipeline.resolvers.value.AtomicResolver;
import com.winterhavenmc.library.messagebuilder.adapters.pipeline.resolvers.value.CompositeResolver;
import com.winterhavenmc.library.messagebuilder.adapters.pipeline.resolvers.itemname.BukkitItemDisplayNameResolver;
import com.winterhavenmc.library.messagebuilder.adapters.pipeline.resolvers.itemname.BukkitItemNameResolver;
import com.winterhavenmc.library.messagebuilder.adapters.pipeline.resolvers.itemname.BukkitItemPluralNameResolver;
import com.winterhavenmc.library.messagebuilder.adapters.pipeline.retrievers.LocalizedMessageRetriever;
import com.winterhavenmc.library.messagebuilder.adapters.pipeline.senders.KyoriMessageSender;
import com.winterhavenmc.library.messagebuilder.adapters.pipeline.senders.KyoriTitleSender;

import com.winterhavenmc.library.messagebuilder.adapters.resources.configuration.BukkitLocaleProvider;
import com.winterhavenmc.library.messagebuilder.adapters.resources.language.YamlLanguageResourceInstaller;
import com.winterhavenmc.library.messagebuilder.adapters.resources.language.YamlLanguageResourceLoader;
import com.winterhavenmc.library.messagebuilder.adapters.resources.language.YamlLanguageResourceManager;
import com.winterhavenmc.library.messagebuilder.adapters.resources.language.YamlItemForge;
import com.winterhavenmc.library.messagebuilder.adapters.resources.sound.YamlSoundResourceInstaller;
import com.winterhavenmc.library.messagebuilder.adapters.resources.sound.YamlSoundResourceLoader;
import com.winterhavenmc.library.messagebuilder.adapters.resources.sound.YamlSoundResourceManager;
import com.winterhavenmc.library.messagebuilder.core.ports.resources.sound.SoundRepository;
import com.winterhavenmc.library.messagebuilder.models.configuration.LocaleProvider;

import com.winterhavenmc.library.messagebuilder.core.context.AccessorCtx;
import com.winterhavenmc.library.messagebuilder.core.context.FormatterCtx;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.AccessorRegistry;
import com.winterhavenmc.library.messagebuilder.adapters.pipeline.MessagePipeline;
import com.winterhavenmc.library.messagebuilder.adapters.pipeline.resolvers.worldname.DefaultResolver;
import com.winterhavenmc.library.messagebuilder.adapters.pipeline.resolvers.worldname.MultiverseResolver;
import com.winterhavenmc.library.messagebuilder.adapters.pipeline.accessors.FieldAccessorRegistry;
import com.winterhavenmc.library.messagebuilder.core.ports.resources.language.*;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.formatters.duration.DurationFormatter;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.resolvers.worldname.WorldNameResolver;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.senders.Sender;
import com.winterhavenmc.library.messagebuilder.core.ports.resources.language.ItemForge;

import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.minimessage.MiniMessage;

import org.bukkit.plugin.Plugin;

import org.jetbrains.annotations.NotNull;

import java.util.List;


/**
 * A companion utility class to facilitate arranging components necessary to initialize the MessageBuilder library.
 */
public final class BootstrapUtility
{
	private BootstrapUtility() { /* Private constructor to prevent instantiation of utility class */ }


	public static List<Sender> createSenders(final Plugin plugin, final MessageCooldownMap messageCooldownMap, final SoundRepository sounds)
	{
		final MiniMessage miniMessage = MiniMessage.miniMessage();
		final BukkitAudiences bukkitAudiences = BukkitAudiences.create(plugin);
		final KyoriMessageSender messageSender = new KyoriMessageSender(messageCooldownMap, miniMessage, bukkitAudiences, sounds);
		final KyoriTitleSender titleSender = new KyoriTitleSender(messageCooldownMap, miniMessage, bukkitAudiences);

		return List.of(messageSender, titleSender);
	}


	/**
	 * A static factory method to create the language resource manager
	 *
	 * @param plugin an instance of the plugin
	 * @return an instance of the language resource manager
	 */
	static YamlLanguageResourceManager createLanguageResourceManager(final Plugin plugin,
																	 final LocaleProvider localeProvider)
	{
		final YamlLanguageResourceInstaller resourceInstaller = new YamlLanguageResourceInstaller(plugin, localeProvider);
		final YamlLanguageResourceLoader resourceLoader = new YamlLanguageResourceLoader(plugin, localeProvider);

		return new YamlLanguageResourceManager(resourceInstaller, resourceLoader);
	}

	static YamlSoundResourceManager createSoundResourceManager(final Plugin plugin,
															   final LocaleProvider localeProvider)
	{
		final YamlSoundResourceInstaller resourceInstaller = new YamlSoundResourceInstaller(plugin);
		final YamlSoundResourceLoader resourceLoader = new YamlSoundResourceLoader(plugin, localeProvider);

		return new YamlSoundResourceManager(plugin, resourceLoader, resourceInstaller);
	}

	/**
	 * A static factory method to create a macro replacer instance
	 *
	 * @param formatterCtx the context container holding formatters
	 * @param accessorCtx the context container for dependency injection into adapters
	 * @return an instance of the macro replacer
	 */
	private static @NotNull MessageProcessor createMacroReplacer(final FormatterCtx formatterCtx,
																 final AccessorCtx accessorCtx)
	{
		final AccessorRegistry accessorRegistry = new FieldAccessorRegistry(accessorCtx);
		final MacroFieldAccessor macroFieldAccessor = new MacroFieldAccessor(accessorCtx);
		final CompositeResolver compositeResolver = new CompositeResolver(accessorRegistry, macroFieldAccessor);
		final AtomicResolver atomicResolver = new AtomicResolver(formatterCtx);
		final MacroValueResolver macroValueResolver = new MacroValueResolver(List.of(compositeResolver, atomicResolver)); // atomic must come last
		final RegexPlaceholderMatcher placeholderMatcher = new RegexPlaceholderMatcher();

		return new MessageProcessor(macroValueResolver, placeholderMatcher);
	}


	/**
	 * A static factory method to create the message processing pipeline
	 *
	 * @param formatterCtx a context container which contains instances of string formatters for specific types
	 * @param accessorCtx a context container for injecting dependencies into adapters
	 * @return an instance of the message pipeline
	 */
	static @NotNull MessagePipeline createMessagePipeline(final Plugin plugin,
														  final MessageRepository messages,
														  final SoundRepository sounds,
														  final FormatterCtx formatterCtx,
														  final AccessorCtx accessorCtx)
	{
		final LocalizedMessageRetriever localizedMessageRetriever = new LocalizedMessageRetriever(messages);
		final MessageProcessor messageProcessor = createMacroReplacer(formatterCtx, accessorCtx);
		final MessageCooldownMap messageCooldownMap = new MessageCooldownMap();
		final List<Sender> messageSenders = createSenders(plugin, messageCooldownMap, sounds);

		return new MessagePipeline(localizedMessageRetriever, messageProcessor, messageCooldownMap, messageSenders);
	}


	/**
	 * A static factory method to create a context container for dependency injection into resolvers
	 *
	 * @param plugin instance of the plugin
	 * @return the context container for to be injected into resolvers
	 */
	static FormatterCtx createFormatterContextContainer(final Plugin plugin,
														final LocaleProvider localeProvider,
														final ConstantRepository constants)
	{
		final LocaleNumberFormatter localeNumberFormatter = new LocaleNumberFormatter(localeProvider);
		final Time4jDurationFormatter time4jDurationFormatter = new Time4jDurationFormatter(localeProvider);
		final DurationFormatter durationFormatter = new LocalizedDurationFormatter(time4jDurationFormatter, constants);

		return new FormatterCtx(localeProvider, durationFormatter, localeNumberFormatter);
	}


	/**
	 * A static factory method to create a context container for dependency injection into adapters
	 *
	 * @param plugin instance of the plugin
	 * @return a populated context container
	 */
	static AccessorCtx createAccessorContextContainer(final Plugin plugin,
													  final ItemRecordRepository itemRecordRepository,
													  final FormatterCtx formatterCtx)
	{
		WorldNameResolver worldNameResolver = getWorldNameResolver(plugin);
		BukkitItemNameResolver bukkitItemNameResolver = new BukkitItemNameResolver();
		BukkitItemDisplayNameResolver bukkitItemDisplayNameResolver = new BukkitItemDisplayNameResolver();
		BukkitItemPluralNameResolver bukkitItemPluralNameResolver = new BukkitItemPluralNameResolver(itemRecordRepository);

		return new AccessorCtx(worldNameResolver, bukkitItemNameResolver, bukkitItemDisplayNameResolver,
				bukkitItemPluralNameResolver, formatterCtx);
	}


	static ItemForge createItemForge(final Plugin plugin, final ItemRecordRepository items)
	{
		return new YamlItemForge(plugin, items);
	}


	static WorldNameResolver getWorldNameResolver(final Plugin plugin)
	{
		Plugin mvPlugin = plugin.getServer().getPluginManager().getPlugin("Multiverse-Core");

		return (mvPlugin != null && mvPlugin.isEnabled())
				? new MultiverseResolver(mvPlugin)
				: new DefaultResolver();
	}


	static LocaleProvider createLocaleProvider(final Plugin plugin)
	{
		return BukkitLocaleProvider.create(plugin);
	}

}
