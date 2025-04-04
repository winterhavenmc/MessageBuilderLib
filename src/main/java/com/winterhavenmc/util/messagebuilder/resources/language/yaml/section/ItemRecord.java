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

import com.winterhavenmc.util.messagebuilder.resources.RecordKey;
import com.winterhavenmc.util.messagebuilder.util.Pluralizable;

import java.util.List;
import java.util.Optional;


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
public record ItemRecord(
		RecordKey key,
		Optional<String> nameSingular,
		Optional<String> namePlural,
		Optional<String> inventoryItemSingular,
		Optional<String> inventoryItemPlural,
		List<String> itemLore) implements SectionRecord, Pluralizable
{
	@Override
	public Optional<String> nameFor(int quantity)
	{
		return (quantity == 1) ? nameSingular : namePlural;
	}

}
