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

package com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.items;

import com.winterhavenmc.util.messagebuilder.resources.language.yaml.YamlConfigurationSupplier;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.Section;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.SectionQueryHandler;
import com.winterhavenmc.util.messagebuilder.validation.ValidationException;

import org.bukkit.configuration.ConfigurationSection;

import java.util.Objects;
import java.util.Optional;

import static com.winterhavenmc.util.messagebuilder.validation.MessageKey.PARAMETER_EMPTY;
import static com.winterhavenmc.util.messagebuilder.validation.MessageKey.PARAMETER_NULL;
import static com.winterhavenmc.util.messagebuilder.validation.Parameter.*;
import static com.winterhavenmc.util.messagebuilder.validation.Validate.validate;


/**
 * Handles queries for item records from the current language file. The constructor takes the ITEMS configuration
 * section as a parameter, and throws an exception if the provided configuration section is not the language file
 * item section.
 */
public class ItemSectionQueryHandler implements SectionQueryHandler
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
		validate(configurationSupplier, Objects::isNull, () -> new ValidationException(PARAMETER_NULL, CONFIGURATION_SUPPLIER));

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
	public Optional<ItemRecord> getRecord(final String key)
	{
		validate(key, Objects::isNull, () -> new ValidationException(PARAMETER_NULL, KEY));
		validate(key, String::isBlank, () -> new ValidationException(PARAMETER_EMPTY, KEY));

		// get configuration section for item key
		ConfigurationSection itemEntry = configurationSupplier.getSection(section).getConfigurationSection(key);
		if (itemEntry == null) { return Optional.empty(); }

		// return new ItemRecord
		return ItemRecord.getRecord(key, configurationSupplier.getSection(section));
	}

}
