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

package com.winterhavenmc.util.messagebuilder.language.yaml.section.items;

import com.winterhavenmc.util.messagebuilder.language.yaml.YamlConfigurationSupplier;
import com.winterhavenmc.util.messagebuilder.language.yaml.section.Section;
import com.winterhavenmc.util.messagebuilder.language.yaml.section.SectionQueryHandler;
import com.winterhavenmc.util.messagebuilder.util.Error;

import org.bukkit.configuration.ConfigurationSection;

import java.util.List;
import java.util.Optional;


/**
 * Handles queries for item records from the current language file. The constructor takes the ITEMS configuration
 * section as a parameter, and throws an exception if the provided configuration section is not the language file
 * item section.
 */
public class ItemSectionQueryHandler implements SectionQueryHandler<ItemRecord> {

	private final YamlConfigurationSupplier configurationSupplier;


	/**
	 * Class constructor
	 *
	 * @param configurationSupplier the supplier for the configuration object of the language file.
	 */
	public ItemSectionQueryHandler(YamlConfigurationSupplier configurationSupplier) {
		if (configurationSupplier == null) { throw new IllegalArgumentException(Error.Parameter.NULL_SECTION_ITEMS.getMessage()); }

		// check that 'ITEMS' section returned by the configuration supplier is not null
		if (configurationSupplier.getSection(Section.ITEMS) == null) {
				throw new IllegalArgumentException(Error.Parameter.INVALID_SECTION_ITEMS.getMessage());
		}

		this.configurationSupplier = configurationSupplier;
	}


	/**
	 * Retrieve an item record from the language file for the currently configured language. If a record cannot be
	 * found for the keyPath, an empty Optional will be returned.
	 *
	 * @param keyPath the keyPath for the item record in the language file
	 * @return an {@code Optional} ItemRecord if a matching record was found, or an empty Optional if not.
	 */
	public Optional<ItemRecord> getRecord(final String keyPath) {
		if (keyPath == null) { throw new IllegalArgumentException(Error.Parameter.NULL_ITEM_KEY.getMessage()); }

		// get configuration section for item key
		ConfigurationSection itemEntry = configurationSupplier.getSection(Section.ITEMS).getConfigurationSection(keyPath);
		if (itemEntry == null) { return Optional.empty(); }

		// return new ItemRecord
		return ItemRecord.getRecord(keyPath, configurationSupplier.getSection(Section.ITEMS));
	}


	/**
	 * Return the Section enum constant for this query handler type
	 *
	 * @return the ITEMS Section constant, establishing this query handler type
	 */
	@Override
	public Section getSectionType() {
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

	/**
	 * A list of the types returned by this query handler. A query handler should not provide methods that return
	 * values of other types.
	 *
	 * @return {@code List} of class types that are handled by this query handler
	 */
	@Override
	public List<Class<?>> listHandledTypes() {
		return List.of(ItemRecord.class);
	}

}
