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

import com.winterhavenmc.library.messagebuilder.core.context.NameResolverCtx;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.resolvers.itemname.ItemPluralNameResolver;

import org.bukkit.inventory.ItemStack;

import java.util.Optional;


public class BukkitItemPluralNameResolver implements ItemPluralNameResolver
{
	private final static String EMPTY_STRING = "";
	private final NameResolverCtx ctx;


	public BukkitItemPluralNameResolver(final NameResolverCtx ctx)
	{
		this.ctx = ctx;
	}


	public String resolve(final ItemStack itemStack)
	{
		return (itemStack != null)
			? Optional.of(itemStack)
				.filter(ItemStack::hasItemMeta)
				.flatMap(item -> ctx.pluralNameRetriever().retrieve(item)
						.or(() -> ctx.displayNameRetriever().retrieve(item))
						.or(() -> ctx.nameRetriever().retrieve(item)))
				.orElse(EMPTY_STRING)
			: EMPTY_STRING;
	}

}
