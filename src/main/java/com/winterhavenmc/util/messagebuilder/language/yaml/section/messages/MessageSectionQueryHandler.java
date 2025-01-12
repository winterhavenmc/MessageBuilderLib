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

package com.winterhavenmc.util.messagebuilder.language.yaml.section.messages;

import com.winterhavenmc.util.messagebuilder.language.yaml.ConfigurationSupplier;
import com.winterhavenmc.util.messagebuilder.language.yaml.section.Section;
import com.winterhavenmc.util.messagebuilder.language.yaml.section.SectionQueryHandler;
import com.winterhavenmc.util.messagebuilder.util.Error;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;
import java.util.Optional;


/**
 * Handles queries for message records from the current language file. The constructor takes the 'MESSAGES' configuration
 * section as a parameter, and throws an exception if the provided configuration section is not the language file
 * message section.
 */
public class MessageSectionQueryHandler implements SectionQueryHandler<MessageRecord> {

	private final ConfigurationSection messageSection;


	/**
	 * Class constructor
	 *
	 * @param configurationSupplier the configuration supplier that provides access to the configuration object for the language file.
	 */
	public MessageSectionQueryHandler(ConfigurationSupplier configurationSupplier) {
		if (configurationSupplier == null) { throw new IllegalArgumentException(Error.Parameter.NULL_SECTION_MESSAGES.getMessage()); }

		// allow only 'MESSAGES' configuration section to be passed into constructor
		if (!Section.MESSAGES.name().equals(configurationSupplier.getSection(Section.MESSAGES).getName())) {
			throw new IllegalArgumentException(Error.Parameter.INVALID_SECTION_MESSAGES.getMessage());
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
		if (messageId == null) { throw new IllegalArgumentException(Error.Parameter.NULL_MESSAGE_KEY.getMessage()); }

		return MessageRecord.getRecord(messageId, messageSection);
	}


	/**
	 * Return the Section enum constant for this query handler type
	 *
	 * @return the MESSAGES Section constant, establishing this query handler type
	 */
	@Override
	public Section getSectionType() {
		return Section.MESSAGES;
	}


	/**
	 * The primary type returned by this query handler. A query handler may provide methods that return
	 * values of other types.
	 *
	 * @return MessageRecord.class as the primary type returned by this query handler
	 */
	@Override
	public Class<MessageRecord> getHandledType() {
		return MessageRecord.class;
	}


	/**
	 * A list of the types returned by this query handler. A query handler should not provide methods that return
	 * values of other types.
	 *
	 * @return {@code List} of class types that are handled by this query handler
	 */
	@Override
	public List<Class<?>> listHandledTypes() {
		return List.of(MessageRecord.class);
	}

}
