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

package com.winterhavenmc.util.messagebuilder.query.domain.item;

import com.winterhavenmc.util.messagebuilder.namespace.Namespace;
import com.winterhavenmc.util.messagebuilder.query.domain.DomainQueryHandler;
import com.winterhavenmc.util.messagebuilder.util.Error;
import com.winterhavenmc.util.messagebuilder.util.ReadOnlyConfigurationSection;
import com.winterhavenmc.util.messagebuilder.util.ReadOnlyConfigurationSectionAdapter;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Optional;


/**
 * Handles queries for item records from the current language file. The constructor takes the ITEMS configuration
 * section as a parameter, and throws an exception if the provided configuration section is not the language file
 * item section.
 */
public class ItemQueryHandler implements DomainQueryHandler<ItemRecord> {

	private final static Namespace.Domain domain = Namespace.Domain.ITEMS;

	private final ReadOnlyConfigurationSection itemSection;


	/**
	 * Class constructor
	 *
	 * @param itemSection the "ITEMS" configuration section of the language file.
	 */
	public ItemQueryHandler(ConfigurationSection itemSection) {
		if (itemSection == null) { throw new IllegalArgumentException(Error.Parameter.NULL_CONFIGURATION_SECTION.getMessage()); }

		// only allow the 'ITEMS' section of the language file to be passed as the constructor parameter
		if (!itemSection.getName().equals(domain.toString())) {
			System.out.println("Item section: " + itemSection.getName());
			throw new IllegalArgumentException(Error.Parameter.INVALID_SECTION_ITEMS.getMessage());
		}

		// set field
		this.itemSection = ReadOnlyConfigurationSectionAdapter.of(itemSection);
	}


	@Override
	public Namespace.Domain getDomain() {
		return domain;
	}


	public Optional<ItemRecord> ItemRecord(final String keyPath) {
		return getRecord(keyPath);
	}


	/**
	 * Retrieve an item record from the language file for the currently configured language. If a record cannot be
	 * found for the itemKey, an empty Optional will be returned.
	 * @param itemKey the itemKey for the item record in the language file
	 * @return an {@code Optional} ItemRecord if a matching record was found, or an empty Optional if not.
	 */
	public Optional<ItemRecord> getRecord(final String itemKey) {
		if (itemKey == null) { throw new IllegalArgumentException(Error.Parameter.NULL_ITEM_KEY.getMessage()); }

		// get configuration section for item key
		ConfigurationSection itemEntry = itemSection.getConfigurationSection(itemKey);
		if (itemEntry == null) { return Optional.empty(); }

		// return new ItemRecord
		return ItemRecord.query(itemKey, itemSection);
	}

	/**
	 * Retrieve an item record from the language file for the currently configured language. If a record cannot be
	 * found for the itemKey, an empty Optional will be returned.
	 * @param itemKey the itemKey for the item record in the language file
	 * @return an {@code Optional} ItemRecord if a matching record was found, or an empty Optional if not.
	 */
	public Optional<ItemRecord> getItemRecord(final String itemKey) {
		if (itemKey == null) { throw new IllegalArgumentException(Error.Parameter.NULL_ITEM_KEY.getMessage()); }

		// get configuration section for item key
		ConfigurationSection itemEntry = itemSection.getConfigurationSection(itemKey);
		if (itemEntry == null) { return Optional.empty(); }

		// return new ItemRecord
		return ItemRecord.query(itemKey, itemSection);
	}

}
