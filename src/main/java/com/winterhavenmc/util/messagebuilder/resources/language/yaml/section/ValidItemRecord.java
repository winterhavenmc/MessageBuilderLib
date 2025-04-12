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

package com.winterhavenmc.util.messagebuilder.resources.language.yaml.section;

import com.winterhavenmc.util.messagebuilder.recordkey.RecordKey;
import com.winterhavenmc.util.messagebuilder.util.Pluralizable;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;


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


	public static ValidItemRecord create(RecordKey key, ConfigurationSection itemEntry)
	{
		return new ValidItemRecord(key,
				itemEntry.getString(Field.NAME_SINGULAR.getKeyPath()),
				itemEntry.getString(Field.NAME_PLURAL.getKeyPath()),
				itemEntry.getString(Field.INVENTORY_NAME_SINGULAR.getKeyPath()),
				itemEntry.getString(Field.INVENTORY_NAME_PLURAL.getKeyPath()),
				itemEntry.getStringList(Field.LORE.getKeyPath()));
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
