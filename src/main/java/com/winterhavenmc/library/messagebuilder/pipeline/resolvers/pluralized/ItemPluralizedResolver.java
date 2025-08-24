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

package com.winterhavenmc.library.messagebuilder.pipeline.resolvers.pluralized;

import com.winterhavenmc.library.messagebuilder.ItemForge;
import com.winterhavenmc.library.messagebuilder.keys.ItemKey;
import com.winterhavenmc.library.messagebuilder.keys.ValidItemKey;
import com.winterhavenmc.library.messagebuilder.model.language.ItemRecord;
import com.winterhavenmc.library.messagebuilder.model.language.Section;
import com.winterhavenmc.library.messagebuilder.model.language.ValidItemRecord;
import com.winterhavenmc.library.messagebuilder.query.QueryHandler;
import com.winterhavenmc.library.messagebuilder.query.QueryHandlerFactory;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;


public class ItemPluralizedResolver
{
	private final QueryHandlerFactory queryHandlerFactory;


	public ItemPluralizedResolver(final QueryHandlerFactory queryHandlerFactory)
	{
		this.queryHandlerFactory = queryHandlerFactory;
	}


	public String resolve(final ItemStack itemStack)
	{
		MiniMessage miniMessage = MiniMessage.miniMessage();

		if (itemStack != null && itemStack.hasItemMeta() && itemStack.getItemMeta() != null)
		{
			// check if item is custom item defined in language file, and if so, try to set pluralized name
			if (ItemForge.isCustomItem(itemStack))
			{
				QueryHandler<ItemRecord> queryHandler = queryHandlerFactory.getQueryHandler(Section.ITEMS);
				Optional<String> itemKeyString = ItemForge.getCustomItemKey(itemStack);
				if (itemKeyString.isPresent())
				{
					ItemKey itemKey = ItemKey.of(itemKeyString.get());
					if (itemKey instanceof ValidItemKey validItemKey)
					{
						ItemRecord itemRecord = queryHandler.getRecord(validItemKey);
						if (itemRecord instanceof ValidItemRecord validItemRecord)
						{
							String pluralString = validItemRecord.namePlural().replaceAll("\\{QUANTITY}", String.valueOf(itemStack.getAmount()));
							Component component = miniMessage.deserialize(pluralString, Formatter.choice("choice", itemStack.getAmount()));
							return ItemForge.LEGACY_SERIALIZER.serializeOr(component, "");
						}
					}
				}
			}

			if (itemStack.getItemMeta().hasDisplayName()) return ChatColor.stripColor(itemStack.getItemMeta().getDisplayName());
			else if (itemStack.getItemMeta().hasItemName()) return ChatColor.stripColor(itemStack.getItemMeta().getItemName());
		}

		return "";
	}
}
