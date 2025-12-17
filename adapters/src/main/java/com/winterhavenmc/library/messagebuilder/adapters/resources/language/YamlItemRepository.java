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
import com.winterhavenmc.library.messagebuilder.models.keys.*;
import com.winterhavenmc.library.messagebuilder.models.language.*;

import com.winterhavenmc.library.messagebuilder.models.language.InvalidRecordReason;
import com.winterhavenmc.library.messagebuilder.models.language.item.InvalidItemRecord;
import com.winterhavenmc.library.messagebuilder.models.language.item.ItemRecord;
import com.winterhavenmc.library.messagebuilder.models.language.item.ValidItemRecord;
import com.winterhavenmc.library.messagebuilder.models.validation.ErrorMessageKey;
import com.winterhavenmc.library.messagebuilder.models.validation.Parameter;

import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.*;

import static com.winterhavenmc.library.messagebuilder.adapters.resources.language.CustomItemFactory.ITEM_KEY_STRING;
import static com.winterhavenmc.library.messagebuilder.models.validation.Validator.throwing;
import static com.winterhavenmc.library.messagebuilder.models.validation.Validator.validate;


public final class YamlItemRepository implements ItemRepository
{
	public static final LegacyComponentSerializer LEGACY_SERIALIZER = LegacyComponentSerializer.legacySection();

	private final SectionProvider sectionProvider;
	private final NamespacedKey namespacedKey;
	private final CustomItemFactory customItemFactory;


	public YamlItemRepository(final Plugin plugin,
							  final LanguageResourceManager languageResourceManager,
							  final CustomItemFactory customItemFactory)
	{
		validate(plugin, Objects::isNull, throwing(ErrorMessageKey.PARAMETER_NULL, Parameter.PLUGIN));
		validate(languageResourceManager, Objects::isNull, throwing(ErrorMessageKey.PARAMETER_NULL, Parameter.LANGUAGE_RESOURCE_MANAGER));

		this.customItemFactory = customItemFactory;
		this.sectionProvider = languageResourceManager.getSectionProvider(Section.ITEMS);
		this.namespacedKey = new NamespacedKey(plugin, ITEM_KEY_STRING);
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
		return (validItemKey != null && record(validItemKey) instanceof ValidItemRecord validItemRecord)
				? customItemFactory.createItem(validItemRecord, quantity, (replacements != null) ? replacements : Map.of())
				: Optional.empty();
	}


	@Override
	public Optional<String> name(final ValidItemKey validItemKey)
	{
		return (validItemKey != null && record(validItemKey) instanceof ValidItemRecord validItemRecord)
				? Optional.of(validItemRecord.name())
				: Optional.empty();
	}


	@Override
	public Optional<String> displayName(final ValidItemKey validItemKey)
	{
		return (validItemKey != null && record(validItemKey) instanceof ValidItemRecord validItemRecord)
				? Optional.of(validItemRecord.displayName())
				: Optional.empty();
	}


	@Override
	public ItemRecord record(final ValidItemKey validItemKey)
	{
		// confirm item section is not null
		if (sectionProvider.getSection() == null) return new InvalidItemRecord(validItemKey, InvalidRecordReason.ITEM_SECTION_MISSING);

			// confirm item entry in section is valid
		else return (sectionProvider.getSection().isConfigurationSection(validItemKey.toString()))
				? ItemRecord.of(validItemKey, sectionProvider.getSection().getConfigurationSection(validItemKey.toString()))
				: ItemRecord.empty(validItemKey, InvalidRecordReason.ITEM_ENTRY_MISSING);
	}


	@Override
	public Optional<ItemRecord> recordOpt(final ValidItemKey validItemKey)
	{
		ItemRecord itemRecord = record(validItemKey);
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


	@Override
	public ItemKey key(final ItemStack itemStack)
	{
		return itemKeyString(itemStack).map(ItemKey::of)
				.orElseGet(() -> new InvalidItemKey("unknown", InvalidKeyReason.KEY_INVALID));
	}


	private Optional<String> itemKeyString(ItemStack itemStack)
	{
		return (itemStack.hasItemMeta() && itemStack.getItemMeta() != null)
				? Optional.ofNullable(itemStack.getItemMeta().getPersistentDataContainer().get(namespacedKey, PersistentDataType.STRING))
				: Optional.empty();
	}

}
