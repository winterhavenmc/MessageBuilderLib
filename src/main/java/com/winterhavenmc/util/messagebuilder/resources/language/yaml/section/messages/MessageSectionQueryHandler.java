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

package com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.messages;

import com.winterhavenmc.util.messagebuilder.resources.language.yaml.YamlConfigurationSupplier;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.Section;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.SectionQueryHandler;
import com.winterhavenmc.util.messagebuilder.validation.ValidationException;

import java.util.Objects;
import java.util.Optional;

import static com.winterhavenmc.util.messagebuilder.validation.MessageKey.PARAMETER_NULL;
import static com.winterhavenmc.util.messagebuilder.validation.Parameter.CONFIGURATION_SUPPLIER;
import static com.winterhavenmc.util.messagebuilder.validation.Parameter.KEY;
import static com.winterhavenmc.util.messagebuilder.validation.Validate.validate;


/**
 * Handles queries for message records from the current language file. The constructor takes the 'MESSAGES' configuration
 * section as a parameter, and throws an exception if the provided configuration section is not the language file
 * message section.
 */
public class MessageSectionQueryHandler implements SectionQueryHandler
{
	private final static Section section = Section.MESSAGES;
	private final YamlConfigurationSupplier configurationSupplier;


	/**
	 * Class constructor
	 *
	 * @param configurationSupplier the configuration supplier that provides access to the configuration object for the language file.
	 */
	public MessageSectionQueryHandler(YamlConfigurationSupplier configurationSupplier)
	{
		validate(configurationSupplier, Objects::isNull, () -> new ValidationException(PARAMETER_NULL, CONFIGURATION_SUPPLIER));

		this.configurationSupplier = configurationSupplier;
	}


	/**
	 * Retrieve a message record from the language file for the provided messageId
	 *
	 * @param key the MessageId of the message record to be retrieved
	 * @return the message record for the MessageId
	 */
	@Override
	public Optional<MessageRecord> getRecord(final String key)
	{
		validate(key, Objects::isNull, () -> new ValidationException(PARAMETER_NULL, KEY));
		validate(key, String::isBlank, () -> new ValidationException(PARAMETER_NULL, KEY));

		return MessageRecord.getRecord(key, configurationSupplier.getSection(section));
	}

}
