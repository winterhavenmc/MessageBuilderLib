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
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.AbstractSectionQueryHandler;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.Section;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.SectionQueryHandler;
import com.winterhavenmc.util.messagebuilder.util.LocalizedException;

import org.bukkit.configuration.ConfigurationSection;

import java.util.List;
import java.util.Optional;

import static com.winterhavenmc.util.messagebuilder.util.LocalizedException.MessageKey.INVALID_SECTION;
import static com.winterhavenmc.util.messagebuilder.util.LocalizedException.MessageKey.PARAMETER_NULL;


/**
 * Handles queries for message records from the current language file. The constructor takes the 'MESSAGES' configuration
 * section as a parameter, and throws an exception if the provided configuration section is not the language file
 * message section.
 */
public class MessageSectionQueryHandler extends AbstractSectionQueryHandler implements SectionQueryHandler {

	private final static Section section = Section.MESSAGES;
	private final static Class<?> primaryType = MessageRecord.class;
	private final static List<Class<?>> handledTypes = List.of(MessageRecord.class);

	private final ConfigurationSection messageSection;


	/**
	 * Class constructor
	 *
	 * @param configurationSupplier the configuration supplier that provides access to the configuration object for the language file.
	 */
	public MessageSectionQueryHandler(YamlConfigurationSupplier configurationSupplier) {
		super(configurationSupplier, section, primaryType, handledTypes);

		// check that 'MESSAGES' section returned by the configuration supplier is not null
		if (configurationSupplier.getSection(Section.MESSAGES) == null) {
			throw new LocalizedException(INVALID_SECTION, section.name());
		}

		// set field from parameter
		this.messageSection = configurationSupplier.getSection(Section.MESSAGES);
	}


	/**
	 * Retrieve a message record from the language file for the provided {@link MessageId}
	 *
	 * @param messageId the MessageId of the message record to be retrieved
	 * @return the message record for the MessageId
	 * @param <MessageId> an enum member that is used as a key to retrieve messages from the language file
	 */
	public <MessageId extends Enum<MessageId>> Optional<MessageRecord> getRecord(final MessageId messageId) {
		if (messageId == null) { throw new LocalizedException(PARAMETER_NULL, "messageId"); }

		return MessageRecord.getRecord(messageId, messageSection);
	}

}
