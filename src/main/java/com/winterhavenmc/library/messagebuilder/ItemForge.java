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

import com.winterhavenmc.library.messagebuilder.keys.InvalidItemKey;
import com.winterhavenmc.library.messagebuilder.keys.InvalidKeyReason;
import com.winterhavenmc.library.messagebuilder.keys.ItemKey;
import com.winterhavenmc.library.messagebuilder.keys.ValidItemKey;
import com.winterhavenmc.library.messagebuilder.model.language.ItemRecord;
import com.winterhavenmc.library.messagebuilder.model.language.Section;
import com.winterhavenmc.library.messagebuilder.model.language.ValidItemRecord;
import com.winterhavenmc.library.messagebuilder.query.QueryHandler;
import com.winterhavenmc.library.messagebuilder.query.QueryHandlerFactory;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class ItemForge
{
	private final static Material DEFAULT_MATERIAL = Material.STICK;
	private static NamespacedKey namespacedKey;

	private final ItemFactory itemFactory;
	private final QueryHandler<ItemRecord> itemQueryHandler;
	private final MiniMessage miniMessage;
	public static final LegacyComponentSerializer LEGACY_SERIALIZER = LegacyComponentSerializer.legacySection();


	ItemForge(final Plugin plugin, final QueryHandlerFactory queryHandlerFactory)
	{
		ItemForge.namespacedKey = new NamespacedKey(plugin, "CUSTOM_ITEM");
		this.itemFactory = plugin.getServer().getItemFactory();
		this.itemQueryHandler = queryHandlerFactory.getQueryHandler(Section.ITEMS);
		this.miniMessage = MiniMessage.miniMessage();
	}


	public Optional<ItemStack> createItem(final String key)
	{
		return createItem(key, 1);
	}


	public Optional<ItemStack> createItem(final String key, final int quantity)
	{
		if (ItemKey.of(key) instanceof ValidItemKey validItemKey)
		{
			ItemRecord itemRecord = itemQueryHandler.getRecord(validItemKey);
			if (itemRecord instanceof ValidItemRecord validItemRecord)
			{
				Material material = Material.matchMaterial(validItemRecord.material());
				if (material == null || !material.isItem())
				{
					material = DEFAULT_MATERIAL;
				}

				ItemStack itemStack = new ItemStack(material, quantity);
				ItemMeta itemMeta = itemFactory.getItemMeta(material);

				if (itemMeta != null)
				{
					setItemName(validItemRecord, itemMeta);
					setItemLore(validItemRecord, itemMeta);
					setItemFlags(itemMeta);
					setItemPersistentData(itemRecord, itemMeta);
				}

				itemStack.setItemMeta(itemMeta);

				return Optional.of(itemStack);
			}
		}

		return Optional.empty();
	}


	private void setItemName(ValidItemRecord validItemRecord, ItemMeta itemMeta)
	{
		if (validItemRecord.nameSingular() != null && !validItemRecord.nameSingular().isEmpty())
		{
			Component nameComponent = miniMessage.deserialize(validItemRecord.nameSingular());
			String name = LEGACY_SERIALIZER.serialize(nameComponent);
			itemMeta.setItemName(name);
			itemMeta.setDisplayName(name);
		}
	}


	private void setItemLore(ValidItemRecord validItemRecord, ItemMeta itemMeta)
	{
		if (validItemRecord.lore() != null && !validItemRecord.lore().isEmpty())
		{
			List<String> formattedLore = new ArrayList<>();
			for (String line : validItemRecord.lore())
			{
				Component loreLineComponent = miniMessage.deserialize(line);
				formattedLore.add(LEGACY_SERIALIZER.serialize(loreLineComponent));
			}
			itemMeta.setLore(formattedLore);
		}
	}


	private void setItemPersistentData(ItemRecord itemRecord, ItemMeta itemMeta)
	{
		itemMeta.getPersistentDataContainer().set(namespacedKey, PersistentDataType.STRING, itemRecord.key().toString());
	}


	private static void setItemFlags(final ItemMeta itemMeta)
	{
		itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
	}


	public static boolean isCustomItem(final ItemStack itemStack)
	{
		return (itemStack != null
				&& itemStack.hasItemMeta()
				&& itemStack.getItemMeta() != null
				&& itemStack.getItemMeta().getPersistentDataContainer().has(namespacedKey));
	}


	public static Optional<String> getItemKeyString(final ItemStack itemStack)
	{
		return (isCustomItem(itemStack) && itemStack.hasItemMeta() && itemStack.getItemMeta() != null)
				? Optional.ofNullable(itemStack.getItemMeta().getPersistentDataContainer().get(namespacedKey, PersistentDataType.STRING))
				: Optional.empty();
	}


	public static ItemKey getItemKey(final ItemStack itemStack)
	{
		return getItemKeyString(itemStack).map(ItemKey::of)
				.orElseGet(() -> new InvalidItemKey("unknown", InvalidKeyReason.KEY_INVALID));
	}


	public Optional<String> getItemName(final String key)
	{
		ItemKey itemKey = ItemKey.of(key);
		if (itemKey instanceof ValidItemKey)
		{
			ItemRecord itemRecord = itemQueryHandler.getRecord(itemKey);
			if (itemRecord instanceof ValidItemRecord validItemRecord)
			{
				return Optional.of(validItemRecord.nameSingular());
			}
		}

		return Optional.empty();
	}
}
