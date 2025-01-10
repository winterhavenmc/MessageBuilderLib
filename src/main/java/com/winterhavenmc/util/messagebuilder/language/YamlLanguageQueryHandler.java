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

package com.winterhavenmc.util.messagebuilder.language;

import com.winterhavenmc.util.messagebuilder.language.section.Section;
import com.winterhavenmc.util.messagebuilder.language.section.SectionQueryHandler;
import com.winterhavenmc.util.messagebuilder.language.section.SectionQueryHandlerFactory;
import com.winterhavenmc.util.messagebuilder.language.section.items.ItemQueryHandler;
import com.winterhavenmc.util.messagebuilder.language.section.items.ItemRecord;
import com.winterhavenmc.util.messagebuilder.language.section.messages.MessageQueryHandler;
import com.winterhavenmc.util.messagebuilder.language.section.messages.MessageRecord;
import com.winterhavenmc.util.messagebuilder.query.QueryHandlerRegistry;

import org.bukkit.configuration.Configuration;

import java.util.Optional;

import static com.winterhavenmc.util.messagebuilder.util.Error.*;


public class YamlLanguageQueryHandler implements LanguageQueryHandler {

	private final QueryHandlerRegistry queryHandlerRegistry;


	/**
	 * Class constructor
	 *
	 * @param configuration the language configuration
	 */
	public YamlLanguageQueryHandler(final Configuration configuration) {
		if (configuration == null) { throw new IllegalArgumentException(Parameter.NULL_CONFIGURATION.getMessage()); }

		// create the query handler registry
		queryHandlerRegistry = new QueryHandlerRegistry();

		// create the domain factory
		SectionQueryHandlerFactory sectionQueryHandlerFactory = new SectionQueryHandlerFactory(configuration);

		// Register the domain handlers in the registry
		queryHandlerRegistry.registerHandler(Section.CONSTANTS, sectionQueryHandlerFactory.createHandler(Section.CONSTANTS));
		queryHandlerRegistry.registerHandler(Section.ITEMS, sectionQueryHandlerFactory.createHandler(Section.ITEMS));
		queryHandlerRegistry.registerHandler(Section.MESSAGES, sectionQueryHandlerFactory.createHandler(Section.MESSAGES));
		queryHandlerRegistry.registerHandler(Section.TIME, sectionQueryHandlerFactory.createHandler(Section.TIME));
	}


	@Override
	public SectionQueryHandler<?> getQueryHandler(Section section) {
		return (SectionQueryHandler<?>) queryHandlerRegistry.getHandler(section);
	}


	@Override
	public Optional<ItemRecord> getItemRecord(final String itemKey) {
		if (itemKey == null) { throw new IllegalArgumentException(Parameter.NULL_ITEM_KEY.getMessage()); }

		SectionQueryHandler<?> queryHandler = getQueryHandler(Section.ITEMS);
		if (queryHandler instanceof ItemQueryHandler itemQueryHandler) {
			return itemQueryHandler.getRecord(itemKey);
		}
		return Optional.empty();
	}


	@Override
	public <MessageId extends Enum<MessageId>> Optional<MessageRecord> getMessageRecord(final MessageId messageId) {
		if (messageId == null) { throw new IllegalArgumentException(Parameter.NULL_MESSAGE_ID.getMessage()); }

		SectionQueryHandler<?> queryHandler = getQueryHandler(Section.MESSAGES);
		if (queryHandler instanceof MessageQueryHandler messageQueryHandler) {
			return messageQueryHandler.getRecord(messageId);
		}
		return Optional.empty();
	}

}
