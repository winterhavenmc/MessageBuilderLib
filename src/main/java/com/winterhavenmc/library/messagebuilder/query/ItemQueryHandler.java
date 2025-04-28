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

package com.winterhavenmc.library.messagebuilder.query;

import com.winterhavenmc.library.messagebuilder.keys.RecordKey;
import com.winterhavenmc.library.messagebuilder.model.language.item.ItemRecord;
import com.winterhavenmc.library.messagebuilder.resources.language.SectionProvider;

import org.bukkit.configuration.ConfigurationSection;

import java.util.Objects;

import static com.winterhavenmc.library.messagebuilder.validation.ErrorMessageKey.*;
import static com.winterhavenmc.library.messagebuilder.validation.Parameter.*;
import static com.winterhavenmc.library.messagebuilder.validation.ValidationHandler.throwing;
import static com.winterhavenmc.library.messagebuilder.validation.Validator.validate;


/**
 * Handles queries for item records from the current language file. The constructor takes the ITEMS configuration
 * section as a parameter, and throws an exception if the provided configuration section is not the language file
 * item section.
 */
public class ItemQueryHandler implements QueryHandler<ItemRecord>
{
	private final SectionProvider sectionProvider;


	/**
	 * Class constructor
	 *
	 */
	public ItemQueryHandler(final SectionProvider sectionProvider)
	{
		validate(sectionProvider, Objects::isNull, throwing(PARAMETER_NULL, SECTION_SUPPLIER));

		this.sectionProvider = sectionProvider;
	}


	/**
	 * Retrieve an item record from the language file for the currently configured language. If a record cannot be
	 * found for the keyPath, an empty Optional will be returned.
	 *
	 * @param key the keyPath for the item record in the language file
	 * @return an {@code Optional} ValidItemRecord if a matching record was found, or an empty Optional if not.
	 */
	@Override
	public ItemRecord getRecord(final RecordKey key)
	{
		ConfigurationSection section = sectionProvider.getSection();
		ConfigurationSection itemEntry = section.getConfigurationSection(key.toString());

		return (itemEntry == null)
				? ItemRecord.empty(key)
				: ItemRecord.from(key, itemEntry);
	}

}
