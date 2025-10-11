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

import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.resolvers.itemname.ItemPluralNameResolver;
import com.winterhavenmc.library.messagebuilder.core.ports.resources.language.ItemRepository;
import com.winterhavenmc.library.messagebuilder.models.Delimiter;
import com.winterhavenmc.library.messagebuilder.core.util.ItemForge;

import com.winterhavenmc.library.messagebuilder.models.keys.ValidItemKey;
import com.winterhavenmc.library.messagebuilder.models.language.ValidItemRecord;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import java.util.regex.Pattern;


public class BukkitItemPluralNameResolver implements ItemPluralNameResolver
{
	private final ItemRepository itemRepository;


	public BukkitItemPluralNameResolver(final ItemRepository itemRepository)
	{
		this.itemRepository = itemRepository;
	}


	public String resolve(final ItemStack itemStack)
	{
		MiniMessage miniMessage = MiniMessage.miniMessage();

		if (itemStack != null && itemStack.hasItemMeta() && itemStack.getItemMeta() != null)
		{
			// check if item is custom item defined in language file, and if so, try to set pluralized name
			if (ItemForge.isCustomItem(itemStack))
			{
				if (ItemForge.getItemKey(itemStack) instanceof ValidItemKey validItemKey)
				{
					if (itemRepository.getItemRecord(validItemKey) instanceof ValidItemRecord validItemRecord)
					{
						String pluralString = validItemRecord.pluralName()
								.replaceAll(Pattern.quote(Delimiter.OPEN + "QUANTITY" + Delimiter.CLOSE), String.valueOf(itemStack.getAmount()));
						Component component = miniMessage.deserialize(pluralString, Formatter.choice("choice", itemStack.getAmount()));
						return ItemForge.LEGACY_SERIALIZER.serializeOr(component, "");
					}
				}
			}

			else if (itemStack.getItemMeta().hasDisplayName()) return ChatColor.stripColor(itemStack.getItemMeta().getDisplayName());
			else if (itemStack.getItemMeta().hasItemName()) return ChatColor.stripColor(itemStack.getItemMeta().getItemName());
		}

		return "";
	}
}
