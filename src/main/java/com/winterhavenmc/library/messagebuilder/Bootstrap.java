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

import com.winterhavenmc.library.messagebuilder.adapters.yaml_language_resource.YamlConstantRepository;
import com.winterhavenmc.library.messagebuilder.adapters.yaml_language_resource.YamlItemRepository;
import com.winterhavenmc.library.messagebuilder.adapters.yaml_language_resource.YamlMessageRepository;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.AdapterRegistry;
import com.winterhavenmc.library.messagebuilder.pipeline.formatters.number.LocaleNumberFormatter;
import com.winterhavenmc.library.messagebuilder.pipeline.MessagePipeline;
import com.winterhavenmc.library.messagebuilder.pipeline.cooldown.CooldownMap;
import com.winterhavenmc.library.messagebuilder.pipeline.extractor.FieldExtractor;
import com.winterhavenmc.library.messagebuilder.pipeline.matcher.PlaceholderMatcher;
import com.winterhavenmc.library.messagebuilder.pipeline.processor.MessageProcessor;
import com.winterhavenmc.library.messagebuilder.pipeline.resolvers.AtomicResolver;
import com.winterhavenmc.library.messagebuilder.pipeline.resolvers.CompositeResolver;
import com.winterhavenmc.library.messagebuilder.pipeline.resolvers.FieldResolver;
import com.winterhavenmc.library.messagebuilder.pipeline.resolvers.itemname.ItemDisplayNameResolver;
import com.winterhavenmc.library.messagebuilder.pipeline.resolvers.itemname.ItemNameResolver;
import com.winterhavenmc.library.messagebuilder.pipeline.resolvers.itemname.ItemPluralNameResolver;
import com.winterhavenmc.library.messagebuilder.pipeline.retriever.MessageRetriever;
import com.winterhavenmc.library.messagebuilder.pipeline.sender.MessageSender;
import com.winterhavenmc.library.messagebuilder.pipeline.sender.Sender;
import com.winterhavenmc.library.messagebuilder.pipeline.sender.TitleSender;
import com.winterhavenmc.library.messagebuilder.ports.language_resource.ConstantRepository;
import com.winterhavenmc.library.messagebuilder.ports.language_resource.ItemRepository;
import com.winterhavenmc.library.messagebuilder.ports.language_resource.MessageRepository;
import com.winterhavenmc.library.messagebuilder.resources.configuration.LocaleProvider;
import com.winterhavenmc.library.messagebuilder.resources.language.*;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.AdapterContextContainer;
import com.winterhavenmc.library.messagebuilder.pipeline.formatters.FormatterContainer;
import com.winterhavenmc.library.messagebuilder.pipeline.formatters.duration.DurationFormatter;
import com.winterhavenmc.library.messagebuilder.pipeline.formatters.duration.LocalizedDurationFormatter;
import com.winterhavenmc.library.messagebuilder.pipeline.formatters.duration.Time4jDurationFormatter;
import com.winterhavenmc.library.messagebuilder.pipeline.resolvers.worldname.WorldNameResolver;

import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.plugin.Plugin;

import org.jetbrains.annotations.NotNull;

import java.util.List;


/**
 * A companion utility class to facilitate arranging components necessary to initialize the MessageBuilder library.
 */
public class Bootstrap
{
	private Bootstrap() { /* Private constructor to prevent instantiation of utility class */ }


	public static List<Sender> createSenders(final Plugin plugin, final CooldownMap cooldownMap)
	{
		final MiniMessage miniMessage = MiniMessage.miniMessage();
		final BukkitAudiences bukkitAudiences = BukkitAudiences.create(plugin);
		final MessageSender messageSender = new MessageSender(cooldownMap, miniMessage, bukkitAudiences);
		final TitleSender titleSender = new TitleSender(cooldownMap, miniMessage, bukkitAudiences);

		return List.of(messageSender, titleSender);
	}


	/**
	 * A static factory method to create the language resource manager
	 *
	 * @param plugin an instance of the plugin
	 * @return an instance of the language resource manager
	 */
	static LanguageResourceManager createLanguageResourceManager(final Plugin plugin)
	{
		final LanguageResourceInstaller resourceInstaller = new LanguageResourceInstaller(plugin);
		final LanguageResourceLoader resourceLoader = new LanguageResourceLoader(plugin);

		return new LanguageResourceManager(resourceInstaller, resourceLoader);
	}


	/**
	 * A static factory method to create a macro replacer instance
	 *
	 * @param formatterContainer the context container holding formatters
	 * @param adapterContextContainer the context container for dependency injection into adapters
	 * @return an instance of the macro replacer
	 */
	private static @NotNull MessageProcessor createMacroReplacer(final FormatterContainer formatterContainer,
																 final AdapterContextContainer adapterContextContainer)
	{
		final AdapterRegistry adapterRegistry = new AdapterRegistry(adapterContextContainer);
		final FieldExtractor fieldExtractor = new FieldExtractor(adapterContextContainer);
		final CompositeResolver compositeResolver = new CompositeResolver(adapterRegistry, fieldExtractor);
		final AtomicResolver atomicResolver = new AtomicResolver(formatterContainer);
		final FieldResolver fieldResolver = new FieldResolver(List.of(compositeResolver, atomicResolver)); // atomic must come last
		final PlaceholderMatcher placeholderMatcher = new PlaceholderMatcher();

		return new MessageProcessor(fieldResolver, placeholderMatcher);
	}


	/**
	 * A static factory method to create the message processing pipeline
	 *
	 * @param formatterContainer a context container which contains instances of string formatters for specific types
	 * @param adapterContextContainer a context container for injecting dependencies into adapters
	 * @return an instance of the message pipeline
	 */
	static @NotNull MessagePipeline createMessagePipeline(final Plugin plugin,
														  final LanguageResourceManager languageResourceManager,
														  final FormatterContainer formatterContainer,
														  final AdapterContextContainer adapterContextContainer)
	{
		final MessageRetriever messageRetriever = new MessageRetriever(languageResourceManager.messages());
		final MessageProcessor messageProcessor = createMacroReplacer(formatterContainer, adapterContextContainer);
		final CooldownMap cooldownMap = new CooldownMap();
		final List<Sender> messageSenders = createSenders(plugin, cooldownMap);

		return new MessagePipeline(messageRetriever, messageProcessor, cooldownMap, messageSenders);
	}


	/**
	 * A static factory method to create a context container for dependency injection into resolvers
	 *
	 * @param plugin instance of the plugin
	 * @return the context container for to be injected into resolvers
	 */
	static FormatterContainer createFormatterContainer(final Plugin plugin,
													   final LanguageResourceManager languageResourceManager)
	{
		final LocaleProvider localeProvider = LocaleProvider.create(plugin);
		final LocaleNumberFormatter localeNumberFormatter = new LocaleNumberFormatter(localeProvider);
		final Time4jDurationFormatter time4jDurationFormatter = new Time4jDurationFormatter(localeProvider);
		final DurationFormatter durationFormatter = new LocalizedDurationFormatter(time4jDurationFormatter, languageResourceManager);

		return new FormatterContainer(localeProvider, durationFormatter, localeNumberFormatter);
	}


	/**
	 * A static factory method to create a context container for dependency injection into adapters
	 *
	 * @param plugin instance of the plugin
	 * @return a populated context container
	 */
	static AdapterContextContainer createAdapterContextContainer(final Plugin plugin,
																 final LanguageResourceManager languageResourceManager,
																 final FormatterContainer formatterContainer)
	{
		WorldNameResolver worldNameResolver = WorldNameResolver.get(plugin.getServer().getPluginManager());
		ItemNameResolver itemNameResolver = new ItemNameResolver();
		ItemDisplayNameResolver itemDisplayNameResolver = new ItemDisplayNameResolver();
		ItemPluralNameResolver itemPluralNameResolver = new ItemPluralNameResolver(languageResourceManager);

		return new AdapterContextContainer(worldNameResolver, itemNameResolver, itemDisplayNameResolver,
				itemPluralNameResolver, formatterContainer);
	}


	static ConstantResolver createConstantResolver(final LanguageResourceManager languageResourceManager)
	{
		return new ConstantResolver(languageResourceManager);
	}


	static ItemForge createItemForge(final Plugin plugin, final LanguageResourceManager languageResourceManager)
	{
		return new ItemForge(plugin, languageResourceManager.items());
	}


	public static ConstantRepository getConstantRepository(final SectionProvider sectionProvider)
	{
		return new YamlConstantRepository(sectionProvider);
	}


	public static ItemRepository getItemRepository(final SectionProvider sectionProvider)
	{
		return new YamlItemRepository(sectionProvider);
	}


	public static MessageRepository getMessageRepository(final SectionProvider sectionProvider)
	{
		return new YamlMessageRepository(sectionProvider);
	}

}
