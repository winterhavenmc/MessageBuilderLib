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

package com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.item;

import com.winterhavenmc.util.messagebuilder.keys.RecordKey;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.SectionRecord;
import org.bukkit.configuration.ConfigurationSection;


public sealed interface ItemRecord extends SectionRecord permits ValidItemRecord, InvalidItemRecord
{
	static ItemRecord from(final RecordKey itemKey, final ConfigurationSection itemEntry)
	{
		return itemEntry == null
				? ItemRecord.empty(itemKey)
				: ValidItemRecord.create(itemKey, itemEntry);
	}


	static InvalidItemRecord empty(final RecordKey itemKey)
	{
		return new InvalidItemRecord(itemKey, "Missing item section.");
	}


	enum Field
	{
		NAME_SINGULAR("NAME.SINGULAR"),
		NAME_PLURAL("NAME.PLURAL"),
		INVENTORY_NAME_SINGULAR("INVENTORY_NAME.SINGULAR"),
		INVENTORY_NAME_PLURAL("INVENTORY_NAME.PLURAL"),
		LORE("LORE");

		private final String keyPath; // keyPath field

		// constructor for enum constants
		Field(String keyPath) { this.keyPath = keyPath; }

		// getter for keyPath field
		public String getKeyPath() { return this.keyPath; }
	}

}
