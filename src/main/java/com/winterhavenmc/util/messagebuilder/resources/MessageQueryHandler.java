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

package com.winterhavenmc.util.messagebuilder.resources;

import com.winterhavenmc.util.messagebuilder.keys.RecordKey;
import com.winterhavenmc.util.messagebuilder.resources.language.SectionProvider;
import com.winterhavenmc.util.messagebuilder.model.language.message.MessageRecord;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Objects;

import static com.winterhavenmc.util.messagebuilder.validation.ErrorMessageKey.PARAMETER_NULL;
import static com.winterhavenmc.util.messagebuilder.validation.Parameter.SECTION_SUPPLIER;
import static com.winterhavenmc.util.messagebuilder.validation.ValidationHandler.throwing;
import static com.winterhavenmc.util.messagebuilder.validation.Validator.validate;


/**
 * Handles queries for message records from the current language file. The constructor takes the 'MESSAGES' configuration
 * section as a parameter, and throws an exception if the provided configuration section is not the language file
 * message section.
 */
public class MessageQueryHandler implements QueryHandler<MessageRecord>
{
	private final SectionProvider sectionProvider;


	/**
	 * Class constructor
	 *
	 */
	public MessageQueryHandler(final SectionProvider sectionProvider)
	{
		validate(sectionProvider, Objects::isNull, throwing(PARAMETER_NULL, SECTION_SUPPLIER));

		this.sectionProvider = sectionProvider;
	}


	/**
	 * Retrieve a message record from the language file for the provided key
	 *
	 * @param messageKey the MessageId of the message record to be retrieved
	 * @return the message record for the MessageId
	 */
	@Override
	public MessageRecord getRecord(final RecordKey messageKey)
	{
		ConfigurationSection section = sectionProvider.getSection();
		ConfigurationSection messageEntry = section.getConfigurationSection(messageKey.toString());

		return (messageEntry == null)
				? MessageRecord.empty(messageKey)
				: MessageRecord.from(messageKey, messageEntry);

	}

}
