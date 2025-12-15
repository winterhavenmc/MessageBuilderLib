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

package com.winterhavenmc.library.messagebuilder.adapters.pipeline.resolvers.itemname;

import com.winterhavenmc.library.messagebuilder.adapters.pipeline.retrievers.itemname.*;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.resolvers.itemname.ItemPluralNameResolver;

import com.winterhavenmc.library.messagebuilder.core.ports.resources.language.ItemRepository;
import net.kyori.adventure.text.minimessage.MiniMessage;

import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.Optional;


public class BukkitItemPluralNameResolver implements ItemPluralNameResolver
{
	private final static String EMPTY_STRING = "";
	private final ItemNameRetriever nameRetriever;
	private final ItemDisplayNameRetriever displayNameRetriever;
	private final NameRetriever pluralNameRetriever;


	public BukkitItemPluralNameResolver(final Plugin plugin, final MiniMessage miniMessage)
	{
		nameRetriever = new ItemNameRetriever();
		displayNameRetriever = new ItemDisplayNameRetriever();
		pluralNameRetriever = new PersistentPluralNameRetriever(plugin, miniMessage);
	}


	public BukkitItemPluralNameResolver(final ItemRepository itemRepository, final MiniMessage miniMessage)
	{
		nameRetriever = new ItemNameRetriever();
		displayNameRetriever = new ItemDisplayNameRetriever();
		pluralNameRetriever = new ItemPluralNameRetriever(itemRepository, miniMessage);
	}


	public String resolve(final ItemStack itemStack)
	{
		return Optional.of(itemStack)
				.filter(ItemStack::hasItemMeta)
				.flatMap(item -> pluralNameRetriever.retrieve(item)
						.or(() -> displayNameRetriever.retrieve(item))
						.or(() -> nameRetriever.retrieve(item)))
				.orElse(EMPTY_STRING);
	}

}
