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

package com.winterhavenmc.util.messagebuilder;

import com.winterhavenmc.util.messagebuilder.adapters.AdapterRegistry;
import com.winterhavenmc.util.messagebuilder.formatters.number.LocaleNumberFormatter;
import com.winterhavenmc.util.messagebuilder.pipeline.MessagePipeline;
import com.winterhavenmc.util.messagebuilder.pipeline.cooldown.CooldownMap;
import com.winterhavenmc.util.messagebuilder.pipeline.extractor.FieldExtractor;
import com.winterhavenmc.util.messagebuilder.pipeline.matcher.PlaceholderMatcher;
import com.winterhavenmc.util.messagebuilder.pipeline.replacer.MacroReplacer;
import com.winterhavenmc.util.messagebuilder.pipeline.resolver.AtomicResolver;
import com.winterhavenmc.util.messagebuilder.pipeline.resolver.CompositeResolver;
import com.winterhavenmc.util.messagebuilder.pipeline.resolver.FieldResolver;
import com.winterhavenmc.util.messagebuilder.pipeline.retriever.MessageRetriever;
import com.winterhavenmc.util.messagebuilder.pipeline.sender.MessageSender;
import com.winterhavenmc.util.messagebuilder.pipeline.sender.Sender;
import com.winterhavenmc.util.messagebuilder.pipeline.sender.TitleSender;
import com.winterhavenmc.util.messagebuilder.resources.QueryHandlerFactory;
import com.winterhavenmc.util.messagebuilder.resources.language.LanguageResourceManager;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.YamlLanguageResourceInstaller;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.YamlLanguageResourceLoader;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.YamlLanguageResourceManager;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.Section;
import com.winterhavenmc.util.messagebuilder.util.AdapterContextContainer;
import com.winterhavenmc.util.messagebuilder.util.LocaleSupplier;
import com.winterhavenmc.util.messagebuilder.pipeline.resolver.ResolverContextContainer;
import com.winterhavenmc.util.messagebuilder.formatters.duration.DurationFormatter;
import com.winterhavenmc.util.messagebuilder.formatters.duration.LocalizedDurationFormatter;
import com.winterhavenmc.util.messagebuilder.formatters.duration.Time4jDurationFormatter;
import com.winterhavenmc.util.messagebuilder.worldname.WorldNameResolver;

import org.bukkit.plugin.Plugin;

import org.jetbrains.annotations.NotNull;

import java.util.List;


class Orchestrator
{
	private Orchestrator() { /* Private constructor to prevent instantiation of utility class */ }


	static LanguageResourceManager getLanguageResourceManager(Plugin plugin)
	{
		final YamlLanguageResourceInstaller resourceInstaller = new YamlLanguageResourceInstaller(plugin);
		final YamlLanguageResourceLoader resourceLoader = new YamlLanguageResourceLoader(plugin);

		return YamlLanguageResourceManager.getInstance(resourceInstaller, resourceLoader);
	}


	private static @NotNull MacroReplacer getMacroReplacer(final ResolverContextContainer resolverContextContainer, final AdapterContextContainer adapterContextContainer)
	{
		final AdapterRegistry adapterRegistry = new AdapterRegistry(adapterContextContainer);
		final FieldExtractor fieldExtractor = new FieldExtractor();
		final CompositeResolver compositeResolver = new CompositeResolver(adapterRegistry, fieldExtractor);
		final AtomicResolver atomicResolver = new AtomicResolver(resolverContextContainer);
		final FieldResolver fieldResolver = new FieldResolver(List.of(compositeResolver, atomicResolver)); // atomic must come last
		final PlaceholderMatcher placeholderMatcher = new PlaceholderMatcher();

		return new MacroReplacer(fieldResolver, placeholderMatcher);
	}


	static @NotNull MessagePipeline getMessagePipeline(final QueryHandlerFactory queryHandlerFactory,
													   final ResolverContextContainer resolverContextContainer,
													   final AdapterContextContainer adapterContextContainer)
	{
		final MessageRetriever messageRetriever = new MessageRetriever(queryHandlerFactory.getQueryHandler(Section.MESSAGES));
		final MacroReplacer macroReplacer = getMacroReplacer(resolverContextContainer, adapterContextContainer);
		final CooldownMap cooldownMap = new CooldownMap();
		final List<Sender> senders = List.of(new MessageSender(cooldownMap), new TitleSender(cooldownMap));

		return new MessagePipeline(messageRetriever, macroReplacer, cooldownMap, senders);
	}


	static ResolverContextContainer getResolverContextContainer(Plugin plugin, QueryHandlerFactory queryHandlerFactory)
	{
		final LocaleSupplier localeSupplier = LocaleSupplier.create(plugin);
		final LocaleNumberFormatter localeNumberFormatter = new LocaleNumberFormatter(localeSupplier);
		final Time4jDurationFormatter time4jDurationFormatter = new Time4jDurationFormatter(localeSupplier);
		final DurationFormatter durationFormatter = new LocalizedDurationFormatter(time4jDurationFormatter, queryHandlerFactory);

		return new ResolverContextContainer(durationFormatter, localeNumberFormatter);
	}


	static AdapterContextContainer getAdapterContext(final Plugin plugin)
	{
		return new AdapterContextContainer(WorldNameResolver.getResolver(plugin.getServer().getPluginManager()));
	}

}
