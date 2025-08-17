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
import com.winterhavenmc.library.messagebuilder.keys.ValidConstantKey;
import com.winterhavenmc.library.messagebuilder.model.language.InvalidRecordReason;
import com.winterhavenmc.library.messagebuilder.resources.language.SectionProvider;
import com.winterhavenmc.library.messagebuilder.model.language.ConstantRecord;
import com.winterhavenmc.library.messagebuilder.validation.ValidationException;

import org.bukkit.configuration.ConfigurationSection;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.winterhavenmc.library.messagebuilder.validation.ErrorMessageKey.PARAMETER_NULL;
import static com.winterhavenmc.library.messagebuilder.validation.Parameter.*;
import static com.winterhavenmc.library.messagebuilder.validation.Validator.throwing;
import static com.winterhavenmc.library.messagebuilder.validation.Validator.validate;


/**
 * Query handler for the 'CONSTANTS' section of the language file.
 */
public class ConstantQueryHandler implements QueryHandler<ConstantRecord>
{
	private final SectionProvider sectionProvider;


	/**
	 * Class constructor
	 *
	 * @throws ValidationException if the {@code yamlConfigurationSupplier} parameter is null or invalid
	 */
	public ConstantQueryHandler(final SectionProvider sectionProvider)
	{
		validate(sectionProvider, Objects::isNull, throwing(PARAMETER_NULL, SECTION_SUPPLIER));

		this.sectionProvider = sectionProvider;
	}


	/**
	 * Query the constants section of the language file for a String with keyPath
	 *
	 * @param key the keyPath of the String to be retrieved
	 * @return an {@code Optional} String containing the String retrieved with keyPath, or an empty Optional if no
	 * value was found for the keyPath
	 */
	public Optional<String> getString(final ValidConstantKey key)
	{
		return Optional.ofNullable(sectionProvider.getSection().getString(key.toString()));
	}


	/**
	 * Query the constants section of the language file for a List of String with the keyPath
	 *
	 * @param key the keyPath of the List to be retrieved
	 * @return a {@code List} of String containing the values retrieved using keyPath, or an empty List if no
	 * value was found for the keyPath
	 */
	public List<String> getStringList(final ValidConstantKey key)
	{
		return sectionProvider.getSection().getStringList(key.toString());
	}


	/**
	 * Query the constants section of the language file for an {@code int} with the keyPath
	 *
	 * @param key the keyPath of the {@code int} to be retrieved
	 * @return {@code int} containing the values retrieved using keyPath, or zero (0) if no
	 * value was found for the keyPath
	 */
	public int getInt(final ValidConstantKey key)
	{
		return sectionProvider.getSection().getInt(key.toString());
	}


	/**
	 * Stub method until implemented
	 *
	 * @param recordKey the record string
	 * @return a ConstantRecord
	 */
	@Override
	public ConstantRecord getRecord(final RecordKey recordKey)
	{
		ConfigurationSection section = sectionProvider.getSection();
		Object constantEntry = section.get(recordKey.toString());

		if (recordKey instanceof ValidConstantKey validConstantKey)
			return (constantEntry != null)
					? ConstantRecord.of(validConstantKey, constantEntry)
					: ConstantRecord.empty(validConstantKey, InvalidRecordReason.CONSTANT_ENTRY_MISSING);

		else return ConstantRecord.empty(recordKey, InvalidRecordReason.CONSTANT_KEY_INVALID);
	}

}
