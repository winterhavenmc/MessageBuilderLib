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

import com.winterhavenmc.library.messagebuilder.adapters.pipeline.formatters.duration.LocalizedDurationFormatter;
import com.winterhavenmc.library.messagebuilder.adapters.pipeline.formatters.duration.Time4jDurationFormatter;
import com.winterhavenmc.library.messagebuilder.adapters.pipeline.formatters.number.LocaleNumberFormatter;
import com.winterhavenmc.library.messagebuilder.adapters.pipeline.resolvers.itemname.BukkitItemDisplayNameResolver;
import com.winterhavenmc.library.messagebuilder.adapters.pipeline.resolvers.itemname.BukkitItemNameResolver;
import com.winterhavenmc.library.messagebuilder.adapters.pipeline.resolvers.itemname.BukkitItemPluralNameResolver;
import com.winterhavenmc.library.messagebuilder.adapters.pipeline.resolvers.worldname.BukkitWorldNameResolver;
import com.winterhavenmc.library.messagebuilder.adapters.pipeline.retrievers.itemname.ItemDisplayNameRetriever;
import com.winterhavenmc.library.messagebuilder.adapters.pipeline.retrievers.itemname.ItemNameRetriever;
import com.winterhavenmc.library.messagebuilder.adapters.pipeline.retrievers.itemname.PersistentPluralNameRetriever;

import com.winterhavenmc.library.messagebuilder.core.context.AccessorCtx;
import com.winterhavenmc.library.messagebuilder.core.context.FormatterCtx;
import com.winterhavenmc.library.messagebuilder.core.context.NameResolverCtx;

import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.formatters.duration.DurationFormatter;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.resolvers.worldname.WorldNameResolver;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.resolvers.worldname.WorldNameRetriever;

import com.winterhavenmc.library.messagebuilder.core.ports.resources.language.*;
import com.winterhavenmc.library.messagebuilder.core.ports.resources.language.ItemRepository;

import com.winterhavenmc.library.messagebuilder.models.configuration.ConfigRepository;

import net.kyori.adventure.text.minimessage.MiniMessage;

import org.bukkit.plugin.Plugin;

import org.jspecify.annotations.NonNull;

import static com.winterhavenmc.library.messagebuilder.adapters.pipeline.retrievers.worldname.WorldNameRetrieverFactory.getWorldNameRetriever;


/**
 * A companion utility class to facilitate arranging components necessary to initialize the MessageBuilder library.
 */
public final class BootstrapUtility
{
	private BootstrapUtility() { /* Private constructor to prevent instantiation of utility class */ }


	/**
	 * A static factory method to create a context container for dependency injection into resolvers
	 *
	 * @param plugin instance of the plugin
	 * @return the context container for to be injected into resolvers
	 */
	static @NonNull FormatterCtx createFormatterContextContainer(final Plugin plugin,
																 final ConfigRepository configRepository,
																 final ConstantRepository constants,
																 final MiniMessage miniMessage)
	{
		final LocaleNumberFormatter localeNumberFormatter = new LocaleNumberFormatter(configRepository);
		final Time4jDurationFormatter time4jDurationFormatter = new Time4jDurationFormatter(configRepository);
		final DurationFormatter durationFormatter = new LocalizedDurationFormatter(time4jDurationFormatter, constants);

		return new FormatterCtx(configRepository, durationFormatter, localeNumberFormatter, miniMessage);
	}


	/**
	 * A static factory method to create a context container for dependency injection into adapters
	 *
	 * @param plugin instance of the plugin
	 * @return a populated context container
	 */
	static AccessorCtx createAccessorContextContainer(final Plugin plugin,
													  final ItemRepository itemRepository,
													  final FormatterCtx formatterCtx)
	{
		final WorldNameRetriever worldNameRetriever = getWorldNameRetriever(plugin.getServer().getPluginManager().getPlugin("Multiverse-Core"));

		WorldNameResolver worldNameResolver = BukkitWorldNameResolver.create(plugin, worldNameRetriever);
		BukkitItemNameResolver bukkitItemNameResolver = new BukkitItemNameResolver();
		BukkitItemDisplayNameResolver bukkitItemDisplayNameResolver = new BukkitItemDisplayNameResolver();

		NameResolverCtx nameResolverCtx = new NameResolverCtx(new ItemNameRetriever(), new ItemDisplayNameRetriever(), new PersistentPluralNameRetriever(plugin, formatterCtx.miniMessage()));
		BukkitItemPluralNameResolver bukkitItemPluralNameResolver = new BukkitItemPluralNameResolver(nameResolverCtx);

		return new AccessorCtx(worldNameResolver, bukkitItemNameResolver, bukkitItemDisplayNameResolver,
				bukkitItemPluralNameResolver, formatterCtx);
	}

}
