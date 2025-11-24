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

package com.winterhavenmc.library.messagebuilder.models.language.item;

import com.winterhavenmc.library.messagebuilder.models.keys.RecordKey;
import com.winterhavenmc.library.messagebuilder.models.keys.ValidItemKey;
import com.winterhavenmc.library.messagebuilder.models.language.message.InvalidRecordReason;
import com.winterhavenmc.library.messagebuilder.models.language.SectionRecord;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Optional;


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
 *   <li>{@link ValidItemRecord} – A fully parsed and validated item entry</li>
 *   <li>{@link InvalidItemRecord} – A fallback object representing a missing or invalid item definition</li>
 * </ul>
 *
 * <p>Instances are created using {@link #of(ValidItemKey, ConfigurationSection)},
 * which applies default behavior and validation automatically.
 * <p>
 * This interface extends {@link SectionRecord}, allowing all item records
 * to be safely passed through the macro and message systems once constructed.
 *
 * @see ValidItemRecord ValidItemRecord
 * @see InvalidItemRecord InvalidItemRecord
 * @see ValidItemKey ValidItemKey
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
	 * @param itemKey the string identifying this item record
	 * @param itemEntry the configuration section associated with this item
	 * @return a valid or invalid {@code ItemRecord}, depending on input
	 */
	static ItemRecord of(final ValidItemKey itemKey, final ConfigurationSection itemEntry)
	{
		return (itemEntry == null)
				? ItemRecord.empty(itemKey, InvalidRecordReason.ITEM_ENTRY_MISSING)
				: ValidItemRecord.create(itemKey, itemEntry);
	}


	/**
	 * Returns an {@link InvalidItemRecord} representing a missing or unresolved item section.
	 *
	 * @param itemKey the string that could not be resolved
	 * @return an invalid item record
	 */
	static InvalidItemRecord empty(final RecordKey itemKey, InvalidRecordReason reason)
	{
		return new InvalidItemRecord(itemKey, reason);
	}


	/**
	 * Enumeration of field keys within an {@code ItemRecord}, mapping enum constants
	 * to their corresponding YAML string paths.
	 * <p>
	 * This enum centralizes all known fields used for parsing and provides
	 * a stable location for field-to-path mappings. It also allows the YAML
	 * structure to evolve without requiring widespread changes to lookup logic.
	 *
	 * <p>Example usage:
	 * {@snippet lang="java":
	 *  String displayName = section.getString(ItemRecord.Field.DISPLAY_NAME.toKey());
	 * }
	 */
	enum Field
	{
		MATERIAL("MATERIAL"),
		NAME("NAME"),
		PLURAL_NAME("PLURAL_NAME"),
		DISPLAY_NAME("DISPLAY_NAME"),
		LORE("LORE");

		private final String keyString;

		Field(final String keyString)
		{
			this.keyString = keyString;
		}


		/**
		 * Returns the YAML string path string associated with this field.
		 *
		 * @return the raw configuration string
		 */
		public String toKey() { return this.keyString; }

		@Override
		public String toString() { return this.keyString; }
	}


	default Optional<ValidItemRecord> isValid()
	{
		return (this instanceof ValidItemRecord validItemRecord)
				? Optional.of(validItemRecord)
				: Optional.empty();
	}

}
