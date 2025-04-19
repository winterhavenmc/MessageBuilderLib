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
import com.winterhavenmc.util.messagebuilder.pipeline.MessagePipeline;
import com.winterhavenmc.util.messagebuilder.pipeline.cooldown.CooldownMap;
import com.winterhavenmc.util.messagebuilder.pipeline.extractor.FieldExtractor;
import com.winterhavenmc.util.messagebuilder.pipeline.matcher.PlaceholderMatcher;
import com.winterhavenmc.util.messagebuilder.pipeline.replacer.MacroReplacer;
import com.winterhavenmc.util.messagebuilder.pipeline.resolver.AtomicResolver;
import com.winterhavenmc.util.messagebuilder.pipeline.resolver.CompositeResolver;
import com.winterhavenmc.util.messagebuilder.pipeline.resolver.ContextResolver;
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
import com.winterhavenmc.util.messagebuilder.util.AdapterContext;
import com.winterhavenmc.util.messagebuilder.util.LocaleSupplier;
import com.winterhavenmc.util.messagebuilder.util.ResolverContext;
import com.winterhavenmc.util.time.PrettyTimeFormatter;
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


	private static @NotNull MacroReplacer getMacroReplacer(final ResolverContext resolverContext, final AdapterContext adapterContext)
	{
		final AdapterRegistry adapterRegistry = new AdapterRegistry(adapterContext);
		final FieldExtractor fieldExtractor = new FieldExtractor();
		final CompositeResolver compositeResolver = new CompositeResolver(adapterRegistry, fieldExtractor);
		final AtomicResolver atomicResolver = new AtomicResolver(resolverContext);
		final ContextResolver contextResolver = new ContextResolver(List.of(compositeResolver, atomicResolver)); // atomic must come last
		final PlaceholderMatcher placeholderMatcher = new PlaceholderMatcher();

		return new MacroReplacer(contextResolver, placeholderMatcher);
	}


	static @NotNull MessagePipeline getMessagePipeline(final QueryHandlerFactory queryHandlerFactory,
													   final ResolverContext resolverContext,
													   final AdapterContext adapterContext)
	{
		final MessageRetriever messageRetriever = new MessageRetriever(queryHandlerFactory.getQueryHandler(Section.MESSAGES));
		final MacroReplacer macroReplacer = getMacroReplacer(resolverContext, adapterContext);
		final CooldownMap cooldownMap = new CooldownMap();
		final List<Sender> senders = List.of(new MessageSender(cooldownMap), new TitleSender(cooldownMap));

		return new MessagePipeline(messageRetriever, macroReplacer, cooldownMap, senders);
	}

}
