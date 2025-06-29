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


/**
 * A sealed interface representing a record loaded from the {@code ITEMS}
 * section of a language YAML file.
 * <p>
 * Item records contain metadata used to localize or customize item names,
 * inventory display names, and lore descriptions. These values may be used
 * dynamically in messages or as part of macro resolution, but are chiefly designed
 * to provide localized strings for items to be created for use in plugins.
 *
 * <h2>Implementations</h2>
 * <ul>
 *   <li>{@link com.winterhavenmc.library.messagebuilder.model.language.ValidItemRecord} –
 *       A fully parsed and validated item entry</li>
 *   <li>{@link com.winterhavenmc.library.messagebuilder.model.language.InvalidItemRecord} –
 *       A fallback object representing a missing or invalid item definition</li>
 * </ul>
 *
 * <p>Instances are created using {@link #from(RecordKey, ConfigurationSection)},
 * which applies default behavior and validation automatically.
 * <p>
 * This interface extends {@link SectionRecord}, allowing all item records
 * to be safely passed through the macro and message systems once constructed.
 *
 * @see com.winterhavenmc.library.messagebuilder.query.QueryHandler QueryHandler
 * @see com.winterhavenmc.library.messagebuilder.model.language.ValidItemRecord ValidItemRecord
 * @see com.winterhavenmc.library.messagebuilder.model.language.InvalidItemRecord InvalidItemRecord
 * @see com.winterhavenmc.library.messagebuilder.keys.RecordKey RecordKey
 */
public sealed interface ItemRecord extends SectionRecord permits ValidItemRecord, InvalidItemRecord
{
	/**
	 * Creates an {@code ItemRecord} from the given configuration section.
	 * <p>
	 * If the section is {@code null}, an {@link InvalidItemRecord} is returned
	 * with a reason indicating the failure. Otherwise, a {@link ValidItemRecord}
	 * is created using parsed and validated values.
	 *
	 * @param itemKey the key identifying this item record
	 * @param itemEntry the configuration section associated with this item
	 * @return a valid or invalid {@code ItemRecord}, depending on input
	 */
	static ItemRecord from(final RecordKey itemKey, final ConfigurationSection itemEntry)
	{
		return itemEntry == null
				? ItemRecord.empty(itemKey)
				: ValidItemRecord.create(itemKey, itemEntry);
	}


	/**
	 * Returns an {@link InvalidItemRecord} representing a missing or unresolved item section.
	 *
	 * @param itemKey the key that could not be resolved
	 * @return an invalid item record
	 */
	static InvalidItemRecord empty(final RecordKey itemKey)
	{
		return new InvalidItemRecord(itemKey, "Missing item section.");
	}


	/**
	 * Enumeration of field keys within an {@code ItemRecord}, mapping enum constants
	 * to their corresponding YAML key paths.
	 * <p>
	 * This enum centralizes all known fields used for parsing and provides
	 * a stable location for field-to-path mappings. It also allows the YAML
	 * structure to evolve without requiring widespread changes to lookup logic.
	 *
	 * <p>Example usage:
	 * {@snippet lang="java":
	 *  String singularName = section.getString(ItemRecord.Field.NAME_SINGULAR.toKey());
	 * }
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
		 * Returns the YAML key path string associated with this field.
		 *
		 * @return the raw configuration key string
		 */
		public String toKey() { return this.keyString; }
	}

}
