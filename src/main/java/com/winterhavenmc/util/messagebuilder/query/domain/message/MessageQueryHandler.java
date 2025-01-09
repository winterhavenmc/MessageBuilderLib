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

package com.winterhavenmc.util.messagebuilder.query.domain.message;

import com.winterhavenmc.util.messagebuilder.namespace.Namespace;
import com.winterhavenmc.util.messagebuilder.query.domain.DomainQueryHandler;
import com.winterhavenmc.util.messagebuilder.util.Error;
import com.winterhavenmc.util.messagebuilder.util.ReadOnlyConfigurationSection;
import com.winterhavenmc.util.messagebuilder.util.ReadOnlyConfigurationSectionAdapter;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Optional;


/**
 * Handles queries for message records from the current language file. The constructor takes the 'MESSAGES' configuration
 * section as a parameter, and throws an exception if the provided configuration section is not the language file
 * message section.
 */
public class MessageQueryHandler implements DomainQueryHandler<MessageRecord> {
	private final static Namespace.Domain domain = Namespace.Domain.MESSAGES;
	private final ConfigurationSection messageSection;


	/**
	 * Class constructor
	 *
	 * @param messageSection the 'MESSAGES' configuration section of the language file.
	 */
	public MessageQueryHandler(ConfigurationSection messageSection) {
		if (messageSection == null) { throw new IllegalArgumentException(Error.Parameter.NULL_SECTION_MESSAGES.getMessage()); }

		// only allow the 'MESSAGES' section of the language file to be passed as the constructor parameter
		String currentPath = messageSection.getName();
		if (!currentPath.equals(domain.toString())) {
			throw new IllegalArgumentException(Error.Parameter.INVALID_SECTION_MESSAGES.getMessage());
		}

		// set field from parameter
		this.messageSection = messageSection;
	}


	/**
	 * Get the namespace domain for this query handler
	 *
	 * @return the namespace domain for this query handler
	 */
	@Override
	public Namespace.Domain getDomain() {
		return domain;
	}


	/**
	 * Retrieve a message record from the language file for the provided {@link MessageId}
	 *
	 * @param messageId the MessageId of the message record to be retrieved
	 * @return the message record for the MessageId
	 * @param <MessageId> an enum member that is used as a key to retrieve messages from the language file
	 */
	public <MessageId extends Enum<MessageId>>
	Optional<MessageRecord> getRecord(final MessageId messageId) {
		if (messageId == null) { throw new IllegalArgumentException(Error.Parameter.NULL_MESSAGE_KEY.getMessage()); }

		// get configuration section for MessageId
		ReadOnlyConfigurationSection messageEntry = ReadOnlyConfigurationSectionAdapter.of(messageSection.getConfigurationSection(messageId.name()));
//		if (messageEntry == null) { return Optional.empty(); }

		// return new MessageRecord
		return MessageRecord.getRecord(messageId, messageEntry);
	}

}
