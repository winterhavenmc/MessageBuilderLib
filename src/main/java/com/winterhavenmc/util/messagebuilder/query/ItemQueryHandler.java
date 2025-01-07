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

package com.winterhavenmc.util.messagebuilder.query;

import com.winterhavenmc.util.messagebuilder.util.Error;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Optional;


/**
 * Handles queries for item records from the current language file. The constructor takes the ITEMS configuration
 * section as a parameter, and throws an exception if the provided configuration section is not the language file
 * item section.
 */
public class ItemQueryHandler {

	private final static String ITEM_SECTION = "ITEMS";

	private final ConfigurationSection itemSection;

	public ItemQueryHandler(ConfigurationSection itemSection) {
		if (itemSection == null) { throw new IllegalArgumentException(Error.Parameter.NULL_CONFIGURATION_SECTION.getMessage()); }

		// only allow the 'ITEMS' section of the language file to be passed as the constructor parameter
		String currentPath = itemSection.getCurrentPath();
		if (currentPath == null || !currentPath.equals(ITEM_SECTION)) {
			throw new IllegalArgumentException(Error.Parameter.INVALID_ITEM_SECTION.getMessage());
		}

		this.itemSection = itemSection;
	}


	public Optional<ItemRecord> getItemRecord(final String itemKey) {
		if (itemKey == null) { throw new IllegalArgumentException(Error.Parameter.NULL_ITEM_KEY.getMessage()); }

		// get configuration section for item key
		ConfigurationSection itemEntry = itemSection.getConfigurationSection(itemKey);
		if (itemEntry == null) { return Optional.empty(); }

		// return new ItemRecord
		return ItemRecord.getRecord(itemKey, itemSection);
	}

}
