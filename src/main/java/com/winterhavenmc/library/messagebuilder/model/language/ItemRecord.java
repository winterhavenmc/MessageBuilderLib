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

package com.winterhavenmc.library.messagebuilder.model.language;

import com.winterhavenmc.library.messagebuilder.keys.RecordKey;
import org.bukkit.configuration.ConfigurationSection;


public sealed interface ItemRecord extends SectionRecord permits ValidItemRecord, InvalidItemRecord
{
	/**
	 * Factory method that constructs a {@code MessageRecord} from a YAML configuration section.
	 * Returns a {@link ValidMessageRecord} if the section is non-null and valid,
	 * or an {@link InvalidMessageRecord} if the section is missing.
	 *
	 * @param itemKey the unique record key used to identify this item
	 * @param itemEntry the corresponding YAML configuration section
	 * @return a valid or invalid {@code MessageRecord} depending on input
	 */
	static ItemRecord from(final RecordKey itemKey, final ConfigurationSection itemEntry)
	{
		return itemEntry == null
				? ItemRecord.empty(itemKey)
				: ValidItemRecord.create(itemKey, itemEntry);
	}


	/**
	 * Returns an {@link InvalidItemRecord} representing a missing or invalid item definition.
	 *
	 * @param itemKey the key associated with the missing record
	 * @return a placeholder {@code ItemRecord} indicating an empty or unresolved item
	 */
	static InvalidItemRecord empty(final RecordKey itemKey)
	{
		return new InvalidItemRecord(itemKey, "Missing item section.");
	}


	/**
	 * Enum representing the fields defined in a {@link ValidItemRecord}.
	 * <p>
	 * Each constant in this enum maps to a specific key in the YAML configuration.
	 * This enum provides a single source of truth for these keys, which may be
	 * decoupled from the enum constant names in the future.
	 *
	 * <p>This design ensures central schema management and supports future extensions
	 * such as metadata annotations or type hints per field.
	 */
	enum Field
	{
		NAME_SINGULAR("NAME.SINGULAR"),
		NAME_PLURAL("NAME.PLURAL"),
		INVENTORY_NAME_SINGULAR("INVENTORY_NAME.SINGULAR"),
		INVENTORY_NAME_PLURAL("INVENTORY_NAME.PLURAL"),
		LORE("LORE");

		private final String keyString;

		Field(String keyString) { this.keyString = keyString; }

		/**
		 * Returns the raw YAML key string associated with this field.
		 *
		 * @return the field key string
		 */
		public String toKey() { return this.keyString; }
	}

}
