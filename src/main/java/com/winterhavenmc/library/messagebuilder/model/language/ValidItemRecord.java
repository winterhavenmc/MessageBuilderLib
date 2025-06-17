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
 * A validated, immutable {@link ItemRecord} representing a single entry from the
 * {@code ITEMS} section of a language YAML file.
 * <p>
 * This class is created via the {@link #create(RecordKey, ConfigurationSection)} factory method,
 * which performs all necessary validation and applies default values where appropriate. Once constructed,
 * instances are considered safe and complete and can be used without additional checks.
 *
 * @see ItemRecord
 * @see com.winterhavenmc.library.messagebuilder.keys.RecordKey RecordKey
 * @see com.winterhavenmc.library.messagebuilder.query.QueryHandler QueryHandler
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
	 * A data object record for item information contained in the language file. This class also contains
	 * an enum of fields with their corresponding path key, and a static method for retrieving a record.
	 *
	 * @param key the keyPath in the language file for this record
	 * @param nameSingular the singular name of this item
	 * @param namePlural the plural name of this item
	 * @param inventoryItemSingular the singular inventory name of this item
	 * @param inventoryItemPlural the plural inventory name of this item
	 * @param itemLore a List of Strings containing the lines of lore for this item
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
	 * Creates a {@code ValidItemRecord} from the provided key and value.
	 * <p>
	 * This method should be called only after validation, typically from
	 * {@code ItemRecord.from(RecordKey, ConfigurationSection)}.
	 *
	 * @param key the unique constant key
	 * @param section the configuration section containing the item definition
	 * @return a validated constant record instance
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

	@Override
	public String nameFor(final int quantity)
	{
		return (quantity == 1)
				? nameSingular
				: namePlural;
	}

}
