/*
 * Copyright (c) 2024-2025 Tim Savage.
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

package com.winterhavenmc.library.messagebuilder.model.language;

import com.winterhavenmc.library.messagebuilder.keys.RecordKey;
import com.winterhavenmc.library.messagebuilder.util.Pluralizable;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;


/**
 * A validated, immutable {@link ItemRecord} representing a localized or macro-enabled item
 * definition loaded from the {@code ITEMS} section of a language YAML file.
 * <p>
 * A {@code ValidItemRecord} includes singular and plural display names, optional inventory-specific
 * name overrides, and a list of lore lines. It also implements {@link Pluralizable} to provide
 * dynamic name selection based on quantity.
 *
 * <p>This class is instantiated using the static {@link #create(RecordKey, ConfigurationSection)} method,
 * which applies YAML-based parsing and normalization of optional fields.
 *
 * @see ItemRecord
 * @see InvalidItemRecord
 * @see com.winterhavenmc.library.messagebuilder.keys.RecordKey RecordKey
 * @see com.winterhavenmc.library.messagebuilder.util.Pluralizable Pluralizable
 */
public final class ValidItemRecord implements ItemRecord, Pluralizable
{
	RecordKey key;
	String nameSingular;
	String namePlural;
	String inventoryItemSingular;
	String inventoryItemPlural;
	List<String> itemLore;


	/**
	 * Constructs a {@code ValidItemRecord} with parsed item metadata.
	 * This constructor is private; use {@link #create(RecordKey, ConfigurationSection)} instead.
	 *
	 * @param key the key that uniquely identifies this item record
	 * @param nameSingular the singular item name
	 * @param namePlural the plural item name
	 * @param inventoryItemSingular the inventory-specific singular display name
	 * @param inventoryItemPlural the inventory-specific plural display name
	 * @param itemLore the list of lore lines (may be empty but not {@code null})
	 */
	private ValidItemRecord(RecordKey key,
							String nameSingular,
							String namePlural,
							String inventoryItemSingular,
							String inventoryItemPlural,
							List<String> itemLore)
	{
		this.key = key;
		this.nameSingular = nameSingular;
		this.namePlural = namePlural;
		this.inventoryItemSingular = inventoryItemSingular;
		this.inventoryItemPlural = inventoryItemPlural;
		this.itemLore = itemLore;
	}


	/**
	 * Creates a {@code ValidItemRecord} from a configuration section.
	 * <p>
	 * This method parses each field from the YAML structure and wraps the
	 * result into a validated record. All fields are assumed to be non-null
	 * if present, and fallback behavior is the caller's responsibility.
	 *
	 * @param key the unique identifier for this item
	 * @param section the configuration section representing the item
	 * @return a new validated item record
	 */
	public static ValidItemRecord create(RecordKey key, ConfigurationSection section)
	{
		return new ValidItemRecord(key,
				section.getString(Field.NAME_SINGULAR.toKey()),
				section.getString(Field.NAME_PLURAL.toKey()),
				section.getString(Field.INVENTORY_NAME_SINGULAR.toKey()),
				section.getString(Field.INVENTORY_NAME_PLURAL.toKey()),
				section.getStringList(Field.LORE.toKey()));
	}


	/**
	 * Returns the appropriate name form (singular or plural) based on quantity.
	 *
	 * @param quantity the number of items
	 * @return {@code nameSingular} if quantity is 1; otherwise {@code namePlural}
	 */
	@Override
	public String nameFor(final int quantity)
	{
		return (quantity == 1)
				? nameSingular
				: namePlural;
	}


	@Override
	public RecordKey key()
	{
		return key;
	}

	@Override
	public String nameSingular()
	{
		return nameSingular;
	}

	@Override
	public String namePlural()
	{
		return namePlural;
	}

}
