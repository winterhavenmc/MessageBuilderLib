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

package com.winterhavenmc.util.messagebuilder.language.section.query.message;

import com.winterhavenmc.util.messagebuilder.language.section.Section;
import com.winterhavenmc.util.messagebuilder.language.section.SectionQueryHandler;
import com.winterhavenmc.util.messagebuilder.util.Error;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Optional;


/**
 * Handles queries for message records from the current language file. The constructor takes the 'MESSAGES' configuration
 * section as a parameter, and throws an exception if the provided configuration section is not the language file
 * message section.
 */
public class MessageQueryHandler implements SectionQueryHandler<MessageRecord> {
	private final static Section SECTION = Section.MESSAGES;
	private final ConfigurationSection messageSection;


	/**
	 * Class constructor
	 *
	 * @param messageSection the 'MESSAGES' configuration section of the language file.
	 */
	public MessageQueryHandler(ConfigurationSection messageSection) {
		if (messageSection == null) { throw new IllegalArgumentException(Error.Parameter.NULL_SECTION_MESSAGES.getMessage()); }
		System.out.println("section name: " + messageSection.getName());
		System.out.println("section path: " + messageSection.getCurrentPath());
		System.out.println(" domain name: " + SECTION.name());

		//TODO: find method to get passed messageSection key to compare with domain.name()
		// only allow the 'MESSAGES' section of the language file to be passed as the constructor parameter
		System.out.println("domain.name() equals section.getName(): " + SECTION.name().equals(messageSection.getName()));
		if (!SECTION.name().equals(messageSection.getName())) {
			System.out.println("then why you thowin', bro?");
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
	 * Get the namespace domain for this query handler
	 *
	 * @return the namespace domain for this query handler
	 */
	@Override
	public Section getSection() {
		return SECTION;
	}

	/**
	 * @return
	 */
	@Override
	public Class<MessageRecord> getHandledType() {
		return null;
	}

}
