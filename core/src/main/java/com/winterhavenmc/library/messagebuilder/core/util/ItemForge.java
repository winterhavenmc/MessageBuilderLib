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

package com.winterhavenmc.library.messagebuilder.core.util;

import com.winterhavenmc.library.messagebuilder.models.Delimiter;
import com.winterhavenmc.library.messagebuilder.models.keys.InvalidItemKey;
import com.winterhavenmc.library.messagebuilder.models.keys.InvalidKeyReason;
import com.winterhavenmc.library.messagebuilder.models.keys.ItemKey;
import com.winterhavenmc.library.messagebuilder.models.keys.ValidItemKey;
import com.winterhavenmc.library.messagebuilder.models.language.ItemRecord;
import com.winterhavenmc.library.messagebuilder.models.language.ValidItemRecord;
import com.winterhavenmc.library.messagebuilder.core.ports.resources.language.ItemRepository;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;


public class ItemForge
{
	private final static Material DEFAULT_MATERIAL = Material.STICK;
	private static Plugin plugin;
	private static NamespacedKey ITEM_KEY;

	private final ItemRepository itemRepository;
	private final MiniMessage miniMessage;
	public static final LegacyComponentSerializer LEGACY_SERIALIZER = LegacyComponentSerializer.legacySection();


	public ItemForge(final Plugin plugin, final ItemRepository itemRepository)
	{
		ItemForge.plugin = plugin;
		ItemForge.ITEM_KEY = new NamespacedKey(plugin, "ITEM_KEY");
		this.itemRepository = itemRepository;
		this.miniMessage = MiniMessage.miniMessage();
	}


	public Optional<ItemStack> createItem(final String key)
	{
		return (ItemKey.of(key) instanceof ValidItemKey validItemKey)
				? createItem(validItemKey, 1, Map.of())
				: Optional.empty();
	}


	public Optional<ItemStack> createItem(final String key, final int quantity)
	{
		return (ItemKey.of(key) instanceof ValidItemKey validItemKey)
				? createItem(validItemKey, quantity, Map.of())
				: Optional.empty();
	}


	public Optional<ItemStack> createItem(final String key, final int quantity, final Map<String, String> replacements)
	{
		return (ItemKey.of(key) instanceof ValidItemKey validItemKey)
				? createItem(validItemKey, quantity, replacements)
				: Optional.empty();
	}


	public Optional<ItemStack> createItem(final ValidItemKey validItemKey)
	{
		return createItem(validItemKey, 1);
	}


	public Optional<ItemStack> createItem(final ValidItemKey validItemKey, final int quantity)
	{
		return createItem(validItemKey, quantity, Map.of());
	}


//	public static Optional<ItemStack> createItem(final Plugin plugin, final ValidItemKey validItemKey,
//												 final int quantity, final Map<String, String> replacements)
//	{
//		//TODO: static method to create new item given parameters and settings
//
//		return Optional.empty();
//	}


	public Optional<ItemStack> createItem(final ValidItemKey validItemKey,
										  final int quantity,
										  final Map<String, String> replacements)
	{
		if (itemRepository.getItemRecord(validItemKey) instanceof ValidItemRecord validItemRecord)
		{
			Material material = Material.matchMaterial(validItemRecord.material());
			if (material == null || !material.isItem())
			{
				material = DEFAULT_MATERIAL;
			}
			ItemStack itemStack = new ItemStack(material, quantity);
			ItemMeta itemMeta = itemStack.getItemMeta();
			if (itemMeta != null)
			{
				setItemName(validItemRecord, itemMeta, replacements);
				setItemDisplayName(validItemRecord, itemMeta, replacements);
				setItemLore(validItemRecord, itemMeta, replacements);
				setItemPersistentData(validItemRecord, itemMeta);
				setItemFlags(itemMeta);
			}
			itemStack.setItemMeta(itemMeta);
			return Optional.of(itemStack);
		}
		else
		{
			return Optional.empty();
		}
	}


	private void setItemName(final ValidItemRecord validItemRecord, final ItemMeta itemMeta)
	{
		setItemName(validItemRecord, itemMeta, Map.of());
	}


	private void setItemName(final ValidItemRecord validItemRecord,
									final ItemMeta itemMeta,
									final Map<String, String> replacements)
	{
		String itemName = validItemRecord.name();

		if (itemName != null && !itemName.isEmpty())
		{
			for (Map.Entry<String, String> entry : replacements.entrySet())
			{
				itemName = itemName.replace(Delimiter.OPEN + entry.getKey() + Delimiter.CLOSE, entry.getValue());
			}

			Component nameComponent = miniMessage.deserialize(itemName);
			String formattedName = LEGACY_SERIALIZER.serialize(nameComponent);
			itemMeta.setItemName(formattedName);
		}
	}


	private void setItemDisplayName(final ValidItemRecord validItemRecord, final ItemMeta itemMeta)
	{
		setItemDisplayName(validItemRecord, itemMeta, Map.of());
	}


	private void setItemDisplayName(final ValidItemRecord validItemRecord,
									final ItemMeta itemMeta,
									final Map<String, String> replacements)
	{
		String displayName = validItemRecord.displayName();

		if (displayName != null && !displayName.isEmpty())
		{
			for (Map.Entry<String, String> entry : replacements.entrySet())
			{
				displayName = displayName.replace(Delimiter.OPEN + entry.getKey() + Delimiter.CLOSE, entry.getValue());
			}

			Component displayNameComponent = miniMessage.deserialize(displayName);
			String formattedDisplayName = LEGACY_SERIALIZER.serialize(displayNameComponent);
			itemMeta.setDisplayName(formattedDisplayName);
		}
	}


	private void setItemLore(final ValidItemRecord validItemRecord, final ItemMeta itemMeta)
	{
		setItemLore(validItemRecord, itemMeta, Map.of());
	}


	private void setItemLore(final ValidItemRecord validItemRecord,
							 final ItemMeta itemMeta,
							 final Map<String, String> replacements)
	{
		if (validItemRecord.lore() != null && !validItemRecord.lore().isEmpty())
		{
			List<String> formattedLore = new ArrayList<>();
			for (String line : validItemRecord.lore())
			{
				for (Map.Entry<String, String> entry : replacements.entrySet())
				{
					line = line.replace(Delimiter.OPEN + entry.getKey() + Delimiter.CLOSE, entry.getValue());
				}
				Component loreLineComponent = miniMessage.deserialize(line);
				formattedLore.add(LEGACY_SERIALIZER.serialize(loreLineComponent));
			}
			itemMeta.setLore(formattedLore);
		}
	}


	private static void setItemPersistentData(final ValidItemRecord itemRecord, final ItemMeta itemMeta)
	{
		NamespacedKey nsk = new NamespacedKey(plugin, itemRecord.key().toString());
		itemMeta.getPersistentDataContainer().set(ITEM_KEY, PersistentDataType.STRING, itemRecord.key().toString());
		itemMeta.getPersistentDataContainer().set(nsk, PersistentDataType.STRING, itemRecord.key().toString());
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
				&& itemStack.getItemMeta().getPersistentDataContainer().has(ITEM_KEY));
	}


	public static boolean isItem(final String itemKey, final ItemStack itemStack)
	{
		return (itemStack != null
				&& itemStack.hasItemMeta()
				&& itemStack.getItemMeta() != null
				&& itemStack.getItemMeta().getPersistentDataContainer().has(ITEM_KEY)
				&& itemKey.equals(itemStack.getItemMeta().getPersistentDataContainer().get(ITEM_KEY, PersistentDataType.STRING)));
	}


	public static Optional<String> getItemKeyString(final ItemStack itemStack)
	{
		return (itemStack.hasItemMeta() && itemStack.getItemMeta() != null)
				? Optional.ofNullable(itemStack.getItemMeta().getPersistentDataContainer().get(ITEM_KEY, PersistentDataType.STRING))
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
		if (itemKey instanceof ValidItemKey validItemKey)
		{
			ItemRecord itemRecord = itemRepository.getItemRecord(validItemKey);
			if (itemRecord instanceof ValidItemRecord validItemRecord)
			{
				return Optional.of(validItemRecord.name());
			}
		}

		return Optional.empty();
	}


	public Optional<String> getItemDisplayName(final String key)
	{
		ItemKey itemKey = ItemKey.of(key);
		if (itemKey instanceof ValidItemKey validItemKey)
		{
			ItemRecord itemRecord = itemRepository.getItemRecord(validItemKey);
			if (itemRecord instanceof ValidItemRecord validItemRecord)
			{
				return Optional.of(validItemRecord.displayName());
			}
		}

		return Optional.empty();
	}


	public Optional<ItemRecord> getItemRecord(final String key)
	{
		ItemKey itemKey = ItemKey.of(key);
		if (itemKey instanceof ValidItemKey validItemKey)
		{
			ItemRecord itemRecord = itemRepository.getItemRecord(validItemKey);
			if (itemRecord instanceof ValidItemRecord validItemRecord)
			{
				return Optional.of(validItemRecord);
			}
		}

		return Optional.empty();
	}


	public Optional<ItemRecord> getItemRecord(final ValidItemKey validItemKey)
	{
		ItemRecord itemRecord = itemRepository.getItemRecord(validItemKey);
		if (itemRecord instanceof ValidItemRecord validItemRecord)
		{
			return Optional.of(validItemRecord);
		}

		return Optional.empty();
	}

}
