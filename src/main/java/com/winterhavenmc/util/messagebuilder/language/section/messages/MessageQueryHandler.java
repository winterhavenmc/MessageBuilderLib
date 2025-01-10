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

package com.winterhavenmc.util.messagebuilder.language.section.messages;

import com.winterhavenmc.util.messagebuilder.language.section.Section;
import com.winterhavenmc.util.messagebuilder.language.section.SectionQueryHandler;
import com.winterhavenmc.util.messagebuilder.language.section.items.ItemRecord;
import com.winterhavenmc.util.messagebuilder.util.Error;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;
import java.util.Optional;


/**
 * Handles queries for message records from the current language file. The constructor takes the 'MESSAGES' configuration
 * section as a parameter, and throws an exception if the provided configuration section is not the language file
 * message section.
 */
public class MessageQueryHandler implements SectionQueryHandler<MessageRecord> {

	private final ConfigurationSection messageSection;


	/**
	 * Class constructor
	 *
	 * @param messageSection the 'MESSAGES' configuration section of the language file.
	 */
	public MessageQueryHandler(ConfigurationSection messageSection) {
		if (messageSection == null) { throw new IllegalArgumentException(Error.Parameter.NULL_SECTION_MESSAGES.getMessage()); }

		if (!Section.MESSAGES.name().equals(messageSection.getName())) {
			throw new IllegalArgumentException(Error.Parameter.INVALID_SECTION_MESSAGES.getMessage());
		}

		// set field from parameter
		this.messageSection = messageSection;
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

		// get configuration section for MessageId
		ConfigurationSection messageEntry = messageSection.getConfigurationSection(messageId.name());
		if (messageEntry == null) { return Optional.empty(); }

		// return new MessageRecord
		return MessageRecord.getRecord(messageId, messageEntry);
	}


	/**
	 * Return the Section enum constant for this query handler type
	 *
	 * @return the MESSAGES Section constant, establishing this query handler type
	 */
	@Override
	public Section getSection() {
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
