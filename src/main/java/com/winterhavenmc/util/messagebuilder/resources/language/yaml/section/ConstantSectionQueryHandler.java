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

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.winterhavenmc.util.messagebuilder.validation.ErrorMessageKey.PARAMETER_NULL;
import static com.winterhavenmc.util.messagebuilder.validation.Parameter.*;
import static com.winterhavenmc.util.messagebuilder.validation.ValidationHandler.throwing;
import static com.winterhavenmc.util.messagebuilder.validation.Validator.validate;


/**
 * Query handler for the 'CONSTANTS' section of the language file.
 */
public class ConstantSectionQueryHandler implements QueryHandler<ConstantRecord>
{
	private final static Section section = Section.CONSTANTS;
	private final YamlConfigurationSupplier configurationSupplier;


	/**
	 * Class constructor
	 *
	 * @param configurationSupplier the provider of the current language configuration object
	 * @throws ValidationException if the {@code yamlConfigurationSupplier} parameter is null or invalid
	 */
	public ConstantSectionQueryHandler(final YamlConfigurationSupplier configurationSupplier)
	{
		validate(configurationSupplier, Objects::isNull, throwing(PARAMETER_NULL, CONFIGURATION_SUPPLIER));

		this.configurationSupplier = configurationSupplier;
	}


	/**
	 * Query the constants section of the language file for a String with keyPath
	 *
	 * @param key the keyPath of the String to be retrieved
	 * @return an {@code Optional} String containing the String retrieved with keyPath, or an empty Optional if no
	 * value was found for the keyPath
	 */
	public Optional<String> getString(final RecordKey key)
	{
		return Optional.ofNullable(configurationSupplier.getSection(section).getString(key.toString()));
	}


	/**
	 * Query the constants section of the language file for a List of String with the keyPath
	 *
	 * @param key the keyPath of the List to be retrieved
	 * @return a {@code List} of String containing the values retrieved using keyPath, or an empty List if no
	 * value was found for the keyPath
	 */
	public List<String> getStringList(final RecordKey key)
	{
		return configurationSupplier.getSection(section).getStringList(key.toString());
	}


	/**
	 * Query the constants section of the language file for an {@code int} with the keyPath
	 *
	 * @param key the keyPath of the {@code int} to be retrieved
	 * @return {@code int} containing the values retrieved using keyPath, or zero (0) if no
	 * value was found for the keyPath
	 */
	public int getInt(final RecordKey key)
	{
		return configurationSupplier.getSection(section).getInt(key.toString());
	}


	/**
	 * Stub method until implemented
	 *
	 * @param key the record key
	 * @return a ConstantRecord
	 */
	@Override
	public ConstantRecord getRecord(final RecordKey key)
	{
		Object constantEntry = configurationSupplier.getSection(section).get(key.toString());

		return (constantEntry == null)
				? ConstantRecord.empty(key)
				: ConstantRecord.from(key, constantEntry);
	}

}
