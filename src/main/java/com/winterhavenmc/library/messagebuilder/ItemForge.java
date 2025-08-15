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

import com.winterhavenmc.library.messagebuilder.keys.RecordKey;
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
	private final ItemFactory itemFactory;
	private final Plugin plugin;
	private final QueryHandler<ItemRecord> itemQueryHandler;
	private final MiniMessage miniMessage;
	private static final LegacyComponentSerializer LEGACY_SERIALIZER = LegacyComponentSerializer.legacySection();



	ItemForge(final Plugin plugin, final QueryHandlerFactory queryHandlerFactory)
	{
		this.plugin = plugin;
		this.itemFactory = plugin.getServer().getItemFactory();
		this.itemQueryHandler = queryHandlerFactory.getQueryHandler(Section.ITEMS);
		this.miniMessage = MiniMessage.miniMessage();

	}


	public Optional<ItemStack> createItem(final String key)
	{
		Optional<RecordKey> optionalRecordKey = RecordKey.of(key);

		if (optionalRecordKey.isPresent())
		{
			RecordKey recordKey = optionalRecordKey.get();

			ItemRecord itemRecord = itemQueryHandler.getRecord(recordKey);

			if (itemRecord instanceof ValidItemRecord validItemRecord)
			{
				Material material = Material.matchMaterial(validItemRecord.material());
				if (material == null)
				{
					material = DEFAULT_MATERIAL;
				}

				ItemStack itemStack = new ItemStack(material);
				ItemMeta itemMeta = itemFactory.getItemMeta(material);

				if (itemMeta != null)
				{
					if (validItemRecord.nameSingular() != null && !validItemRecord.nameSingular().isEmpty())
					{
						Component nameComponent = miniMessage.deserialize(validItemRecord.nameSingular());
						itemMeta.setDisplayName(LEGACY_SERIALIZER.serialize(nameComponent));
					}

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

					itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
					itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
					itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);

					NamespacedKey PERSISTENT_KEY = new NamespacedKey(plugin, itemRecord.key().toString());
					itemMeta.getPersistentDataContainer().set(PERSISTENT_KEY, PersistentDataType.BYTE, (byte) 1);
				}

				itemStack.setItemMeta(itemMeta);

				return Optional.of(itemStack);
			}
		}

		return Optional.empty();
	}

}
