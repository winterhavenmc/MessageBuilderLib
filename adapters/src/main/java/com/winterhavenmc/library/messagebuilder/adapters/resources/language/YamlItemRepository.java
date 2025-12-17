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

package com.winterhavenmc.library.messagebuilder.adapters.resources.language;

import com.winterhavenmc.library.messagebuilder.core.ports.resources.SectionProvider;
import com.winterhavenmc.library.messagebuilder.core.ports.resources.language.ItemRepository;

import com.winterhavenmc.library.messagebuilder.core.ports.resources.language.LanguageResourceManager;
import com.winterhavenmc.library.messagebuilder.models.Delimiter;
import com.winterhavenmc.library.messagebuilder.models.keys.*;
import com.winterhavenmc.library.messagebuilder.models.language.*;

import com.winterhavenmc.library.messagebuilder.models.language.InvalidRecordReason;
import com.winterhavenmc.library.messagebuilder.models.language.item.InvalidItemRecord;
import com.winterhavenmc.library.messagebuilder.models.language.item.ItemRecord;
import com.winterhavenmc.library.messagebuilder.models.language.item.ValidItemRecord;
import com.winterhavenmc.library.messagebuilder.models.validation.ErrorMessageKey;
import com.winterhavenmc.library.messagebuilder.models.validation.Parameter;

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

import java.util.*;

import static com.winterhavenmc.library.messagebuilder.models.validation.Validator.throwing;
import static com.winterhavenmc.library.messagebuilder.models.validation.Validator.validate;


public final class YamlItemRepository implements ItemRepository
{
	public static final LegacyComponentSerializer LEGACY_SERIALIZER = LegacyComponentSerializer.legacySection();
	private static final String ITEM_KEY_STRING = "ITEM_KEY";
	private static final String PLURAL_KEY_STRING = "PLURAL_NAME";
	private static final Material DEFAULT_MATERIAL = Material.STICK;

	private final Plugin plugin;
	private final SectionProvider sectionProvider;
	private final NamespacedKey namespacedKey;
	private final MiniMessage miniMessage;


	public YamlItemRepository(final Plugin plugin, final LanguageResourceManager languageResourceManager)
	{
		validate(plugin, Objects::isNull, throwing(ErrorMessageKey.PARAMETER_NULL, Parameter.PLUGIN));
		validate(languageResourceManager, Objects::isNull, throwing(ErrorMessageKey.PARAMETER_NULL, Parameter.LANGUAGE_RESOURCE_MANAGER));

		this.plugin = plugin;
		this.sectionProvider = languageResourceManager.getSectionProvider(Section.ITEMS);
		this.namespacedKey = new NamespacedKey(plugin, ITEM_KEY_STRING);
		this.miniMessage = MiniMessage.miniMessage();
	}


	@Override
	public ItemRecord getRecord(final ValidItemKey validItemKey)
	{
		// confirm item section is not null
		if (sectionProvider.getSection() == null) return new InvalidItemRecord(validItemKey, InvalidRecordReason.ITEM_SECTION_MISSING);

		// confirm item entry in section is valid
		else return (sectionProvider.getSection().isConfigurationSection(validItemKey.toString()))
				? ItemRecord.of(validItemKey, sectionProvider.getSection().getConfigurationSection(validItemKey.toString()))
				: ItemRecord.empty(validItemKey, InvalidRecordReason.ITEM_ENTRY_MISSING);
	}


	@Override
	public Optional<ItemStack> createItem(final ValidItemKey validItemKey)
	{
		return createItem(validItemKey, 1, Map.of());
	}


	@Override
	public Optional<ItemStack> createItem(final ValidItemKey validItemKey, final int quantity)
	{
		return createItem(validItemKey, quantity, Map.of());
	}


	@Override
	public Optional<ItemStack> createItem(final ValidItemKey validItemKey,
										  final int quantity,
										  final Map<String, String> replacements)
	{
		return (validItemKey != null && getRecord(validItemKey) instanceof ValidItemRecord validItemRecord)
				? createItem(validItemRecord, quantity, replacements)
				: Optional.empty();
	}


	Optional<ItemStack> createItem(final ValidItemRecord validItemRecord,
								   final int quantity,
								   final Map<String, String> replacements)
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


	@Override
	public Optional<String> name(final ValidItemKey validItemKey)
	{
		return (validItemKey != null && getRecord(validItemKey) instanceof ValidItemRecord validItemRecord)
				? Optional.of(validItemRecord.name())
				: Optional.empty();
	}


	@Override
	public Optional<String> displayName(final ValidItemKey validItemKey)
	{
		return (validItemKey != null && getRecord(validItemKey) instanceof ValidItemRecord validItemRecord)
				? Optional.of(validItemRecord.displayName())
				: Optional.empty();
	}


	@Override
	public Optional<ItemRecord> getRecordOpt(final ValidItemKey validItemKey)
	{
		ItemRecord itemRecord = getRecord(validItemKey);
		if (itemRecord instanceof ValidItemRecord validItemRecord)
		{
			return Optional.of(validItemRecord);
		}

		return Optional.empty();
	}


	@Override
	public boolean isItem(final ItemStack itemStack)
	{
		return (itemStack != null
				&& itemStack.hasItemMeta()
				&& itemStack.getItemMeta() != null
				&& itemStack.getItemMeta().getPersistentDataContainer().has(namespacedKey, PersistentDataType.STRING));
	}


	private Optional<String> getItemKeyString(ItemStack itemStack)
	{
		return (itemStack.hasItemMeta() && itemStack.getItemMeta() != null)
				? Optional.ofNullable(itemStack.getItemMeta().getPersistentDataContainer().get(namespacedKey, PersistentDataType.STRING))
				: Optional.empty();
	}


	@Override
	public ItemKey key(final ItemStack itemStack)
	{
		return getItemKeyString(itemStack).map(ItemKey::of)
				.orElseGet(() -> new InvalidItemKey("unknown", InvalidKeyReason.KEY_INVALID));
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


	private void setItemPersistentData(final ValidItemRecord itemRecord, final ItemMeta itemMeta)
	{
		itemMeta.getPersistentDataContainer().set(namespacedKey, PersistentDataType.STRING, itemRecord.key().toString());

		if (itemRecord.pluralName() != null && !itemRecord.pluralName().isBlank())
		{
			NamespacedKey pluralKey = new NamespacedKey(plugin, PLURAL_KEY_STRING);
			itemMeta.getPersistentDataContainer().set(pluralKey, PersistentDataType.STRING, itemRecord.pluralName());
		}
	}


	private void setItemFlags(final ItemMeta itemMeta)
	{
		itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
	}

}
