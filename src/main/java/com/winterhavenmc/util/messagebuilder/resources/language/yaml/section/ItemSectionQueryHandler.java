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

package com.winterhavenmc.util.messagebuilder.resources.language.yaml.section;

import com.winterhavenmc.util.messagebuilder.resources.QueryHandler;
import com.winterhavenmc.util.messagebuilder.resources.RecordKey;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.YamlConfigurationSupplier;
import com.winterhavenmc.util.messagebuilder.validation.ValidationException;
import com.winterhavenmc.util.messagebuilder.validation.ValidationHandler;

import java.util.Objects;
import java.util.Optional;

import static com.winterhavenmc.util.messagebuilder.validation.ExceptionMessageKey.*;
import static com.winterhavenmc.util.messagebuilder.validation.Parameter.*;
import static com.winterhavenmc.util.messagebuilder.validation.ValidationHandler.throwing;
import static com.winterhavenmc.util.messagebuilder.validation.Validator.validate;


/**
 * Handles queries for item records from the current language file. The constructor takes the ITEMS configuration
 * section as a parameter, and throws an exception if the provided configuration section is not the language file
 * item section.
 */
public class ItemSectionQueryHandler implements QueryHandler<ItemRecord>
{
	private final static Section section = Section.ITEMS;
	private final YamlConfigurationSupplier configurationSupplier;


	/**
	 * Class constructor
	 *
	 * @param configurationSupplier the supplier for the configuration object of the language file.
	 */
	public ItemSectionQueryHandler(final YamlConfigurationSupplier configurationSupplier)
	{
		validate(configurationSupplier, Objects::isNull, throwing(PARAMETER_NULL, CONFIGURATION_SUPPLIER));

		this.configurationSupplier = configurationSupplier;
	}


	/**
	 * Retrieve an item record from the language file for the currently configured language. If a record cannot be
	 * found for the keyPath, an empty Optional will be returned.
	 *
	 * @param key the keyPath for the item record in the language file
	 * @return an {@code Optional} ItemRecord if a matching record was found, or an empty Optional if not.
	 */
	@Override
	public Optional<ItemRecord> getRecord(final RecordKey key)
	{
		return Optional.ofNullable(configurationSupplier.getSection(section).getConfigurationSection(key.toString()))
				.map(itemEntry -> new ItemRecord(key,
						Optional.ofNullable(itemEntry.getString(Field.NAME_SINGULAR.getKeyPath())),
						Optional.ofNullable(itemEntry.getString(Field.NAME_PLURAL.getKeyPath())),
						Optional.ofNullable(itemEntry.getString(Field.INVENTORY_NAME_SINGULAR.getKeyPath())),
						Optional.ofNullable(itemEntry.getString(Field.INVENTORY_NAME_PLURAL.getKeyPath())),
						itemEntry.getStringList(Field.LORE.getKeyPath())));
	}


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
}
