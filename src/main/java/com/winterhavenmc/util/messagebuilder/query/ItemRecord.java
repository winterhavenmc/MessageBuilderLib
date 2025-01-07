/*
 * Copyright (c) 2024 Tim Savage.
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

package com.winterhavenmc.util.messagebuilder.query;

import com.winterhavenmc.util.messagebuilder.util.Error;
import org.bukkit.configuration.ConfigurationSection;

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
		String key,
		Optional<String> nameSingular,
		Optional<String> namePlural,
		Optional<String> inventoryItemSingular,
		Optional<String> inventoryItemPlural,
		List<String> itemLore) implements Pluralizable {


	/**
	 * Enum of ItemRecord fields and their corresponding keyPath. This enum is the source of truth for
	 * item record field constants and their corresponding keyPaths. Other field metadata may be
	 * encapsulated in this enum in the future.
	 */
	enum Field {
		NAME_SINGULAR("NAME.SINGULAR"),
		NAME_PLURAL("NAME.PLURAL"),
		INVENTORY_NAME_SINGULAR("INVENTORY_NAME.SINGULAR"),
		INVENTORY_NAME_PLURAL("INVENTORY_NAME.PLURAL"),
		LORE("LORE");

		Field(String keyPath) { this.keyPath = keyPath; } // constructor for enum constants
		private final String keyPath; // keyPath field
		String getKeyPath() { return this.keyPath; } // getter for keyPath field
	}


	/**
	 * Static method for retrieving an ItemRecord from the language file. Return an ItemRecord if one can be found
	 * for the itemKey, or returns an empty {@link Optional} if no record could be found.
	 *
	 * @param itemKey the key for the item to be retrieved
	 * @param itemSection the ITEMS section of the language file
	 * @return An {@code ItemRecord} from the language file, or an empty Optional if no record could be found
	 * for the provided key in the provided {@code ConfigurationSection}.
	 */
	public static Optional<ItemRecord> getRecord(final String itemKey, final ConfigurationSection itemSection) {
		if (itemKey == null) { throw new IllegalArgumentException(Error.Parameter.NULL_ITEM_KEY.getMessage()); }

		// get configuration section for item key
		ConfigurationSection itemEntry = itemSection.getConfigurationSection(itemKey);
		if (itemEntry == null) { return Optional.empty(); }

		// return new ItemRecord
		return Optional.of(new ItemRecord(itemKey,
				Optional.ofNullable(itemEntry.getString(Field.NAME_SINGULAR.getKeyPath())),
				Optional.ofNullable(itemEntry.getString(Field.NAME_PLURAL.getKeyPath())),
				Optional.ofNullable(itemEntry.getString(Field.INVENTORY_NAME_SINGULAR.getKeyPath())),
				Optional.ofNullable(itemEntry.getString(Field.INVENTORY_NAME_PLURAL.getKeyPath())),
				itemEntry.getStringList(Field.LORE.getKeyPath())));
	}

}
