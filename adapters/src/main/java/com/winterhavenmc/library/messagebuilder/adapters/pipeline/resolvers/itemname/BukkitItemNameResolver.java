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

import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.resolvers.itemname.ItemNameResolver;

import net.kyori.adventure.text.minimessage.MiniMessage;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;


public class BukkitItemNameResolver implements ItemNameResolver
{
	public String resolve(final ItemStack itemStack)
	{
		MiniMessage miniMessage = MiniMessage.miniMessage();

		if (itemStack != null && itemStack.hasItemMeta() && itemStack.getItemMeta() != null)
		{
			if (itemStack.getItemMeta().hasItemName()) return ChatColor.stripColor(itemStack.getItemMeta().getItemName());
			else if (itemStack.getItemMeta().hasDisplayName()) return ChatColor.stripColor(itemStack.getItemMeta().getDisplayName());
		}

		return "";
	}
}
