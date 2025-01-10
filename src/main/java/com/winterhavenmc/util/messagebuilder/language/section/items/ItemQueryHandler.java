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

package com.winterhavenmc.util.messagebuilder.language.section.items;

import com.winterhavenmc.util.messagebuilder.language.section.Section;
import com.winterhavenmc.util.messagebuilder.language.section.SectionQueryHandler;
import com.winterhavenmc.util.messagebuilder.util.Error;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Optional;


/**
 * Handles queries for item records from the current language file. The constructor takes the ITEMS configuration
 * section as a parameter, and throws an exception if the provided configuration section is not the language file
 * item section.
 */
public class ItemQueryHandler implements SectionQueryHandler<ItemRecord> {

	private final ConfigurationSection itemSection;


	/**
	 * Class constructor
	 *
	 * @param itemSection the "ITEMS" configuration section of the language file.
	 */
	public ItemQueryHandler(ConfigurationSection itemSection) {
		if (itemSection == null) { throw new IllegalArgumentException(Error.Parameter.NULL_SECTION_ITEMS.getMessage()); }

		// only allow the 'ITEMS' section of the language file to be passed as the constructor parameter
		if (!Section.ITEMS.name().equals(itemSection.getName())) {
			System.out.println("Invalid 'ITEMS' section: " + itemSection.getName());
			throw new IllegalArgumentException(Error.Parameter.INVALID_SECTION_ITEMS.getMessage());
		}
		this.itemSection = itemSection;
	}


	/**
	 * Retrieve an item record from the language file for the currently configured language. If a record cannot be
	 * found for the itemKey, an empty Optional will be returned.
	 * @param keyPath the itemKey for the item record in the language file
	 * @return an {@code Optional} ItemRecord if a matching record was found, or an empty Optional if not.
	 */
	public Optional<ItemRecord> getRecord(final String keyPath) {
		if (keyPath == null) { throw new IllegalArgumentException(Error.Parameter.NULL_ITEM_KEY.getMessage()); }

		// get configuration section for item key
		ConfigurationSection itemEntry = itemSection.getConfigurationSection(keyPath);
		if (itemEntry == null) { return Optional.empty(); }

		// return new ItemRecord
		return ItemRecord.query(keyPath, itemSection);
	}


	/**
	 * Return the Section enum constant for this query handler type
	 *
	 * @return the ITEMS Section constant, establishing this query handler type
	 */
	@Override
	public Section getSection() {
		return Section.ITEMS;
	}


	/**
	 * The primary type returned by this query handler. A query handler may provide methods that return
	 * values of other types.
	 *
	 * @return ItemRecord.class as the primary type returned by this query handler
	 */
	@Override
	public Class<ItemRecord> getHandledType() {
		return ItemRecord.class;
	}

}
