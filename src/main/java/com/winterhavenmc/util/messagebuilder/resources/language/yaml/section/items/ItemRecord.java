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

package com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.items;

import com.winterhavenmc.util.messagebuilder.validation.ValidationException;
import com.winterhavenmc.util.messagebuilder.util.Pluralizable;

import org.bukkit.configuration.ConfigurationSection;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.Section.ITEMS;
import static com.winterhavenmc.util.messagebuilder.validation.MessageKey.*;
import static com.winterhavenmc.util.messagebuilder.validation.Parameter.ITEM_SECTION;
import static com.winterhavenmc.util.messagebuilder.validation.Parameter.KEY;
import static com.winterhavenmc.util.messagebuilder.validation.Predicates.sectionNameNotEqual;
import static com.winterhavenmc.util.messagebuilder.validation.Validate.validate;


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
		List<String> itemLore) implements Pluralizable
{


	/**
	 * Enum of ItemRecord fields and their corresponding keyPath. This enum is the source of truth for
	 * item record field constants and their corresponding keyPaths. Other field metadata may be
	 * encapsulated in this enum in the future.
	 */
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
		String getKeyPath() { return this.keyPath; }
	}


	/**
	 * Static method for retrieving an {@link ItemRecord} from the language file. Return an ItemRecord if one can be found
	 * for the kepPath, or returns an empty {@link Optional} if no record could be found.
	 *
	 * @param key the key for the item record to be retrieved
	 * @param itemSection the ITEMS section of the language file
	 * @return An {@code ItemRecord} from the language file, or an empty Optional if no record could be found
	 * for the provided key in the provided {@code ConfigurationSection}.
	 */
	public static Optional<ItemRecord> getRecord(final String key, final ConfigurationSection itemSection)
	{
		validate(key, Objects::isNull, () -> new ValidationException(PARAMETER_NULL, KEY));
		validate(key, String::isBlank, () -> new ValidationException(PARAMETER_EMPTY, KEY));
		validate(itemSection, Objects::isNull, () -> new ValidationException(PARAMETER_NULL, ITEM_SECTION));
		validate(itemSection, sectionNameNotEqual(ITEMS), () -> new ValidationException(PARAMETER_INVALID, ITEM_SECTION));

		// get configuration section for item key
		ConfigurationSection itemEntry = itemSection.getConfigurationSection(key);
		if (itemEntry == null) { return Optional.empty(); }

		// return new ItemRecord
		return Optional.of(new ItemRecord(key,
				// looping over these would be nice, and checking types against those listed in the query handler
				// would be to any fields that do not match a type listed in the query handler will be returned as
				// an empty optional or empty list or throw an exception
				Optional.ofNullable(itemEntry.getString(Field.NAME_SINGULAR.getKeyPath())),
				Optional.ofNullable(itemEntry.getString(Field.NAME_PLURAL.getKeyPath())),
				Optional.ofNullable(itemEntry.getString(Field.INVENTORY_NAME_SINGULAR.getKeyPath())),
				Optional.ofNullable(itemEntry.getString(Field.INVENTORY_NAME_PLURAL.getKeyPath())),
				itemEntry.getStringList(Field.LORE.getKeyPath())));
	}


	@Override
	public Optional<String> nameFor(int quantity)
	{
		if (quantity != 1)
		{
			return namePlural;
		}
		return nameSingular;
	}

}
