/*
 * Copyright (c) 2024-2025 Tim Savage.
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

package com.winterhavenmc.util.messagebuilder.resources.language;

import com.winterhavenmc.util.messagebuilder.resources.language.yaml.YamlConfigurationSupplier;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.Section;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.SectionQueryHandler;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.items.ItemRecord;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.messages.MessageRecord;

import java.util.Optional;


public interface LanguageQueryHandler {

	/**
	 * Get a section query handler for a section type
	 *
	 * @param section the namespace Section that dictates the query handler type to be returned
	 * @return a query handler of the type dictated by the passed namespace Section
	 */
	SectionQueryHandler getSectionQueryHandler(Section section);


	YamlConfigurationSupplier getConfigurationSupplier();


	/**
	 * Convenience method to retrieve an item record
	 *
	 * @param keyPath the keyPath of the item record to retrieve
	 * @return an {@link Optional} containing the item record, or an empty {@code Optional} if no record could be found.
	 */
	Optional<ItemRecord> getItemRecord(final String keyPath);


	/**
	 * Convenience method to retrieve a message record
	 *
	 * @param messageId the MessageId of the message to be retrieved
	 * @return  an {@link Optional} containing the message record, or an empty {@code Optional} if no record could be found.
	 * @param <MessageId> An enum constant representing the message identifier in the language file
	 */
	<MessageId extends Enum<MessageId>> Optional<MessageRecord<MessageId>> getMessageRecord(String messageId);
}
