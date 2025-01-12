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

package com.winterhavenmc.util.messagebuilder.language.yaml;

import com.winterhavenmc.util.messagebuilder.language.LanguageQueryHandler;
import com.winterhavenmc.util.messagebuilder.language.yaml.section.Section;
import com.winterhavenmc.util.messagebuilder.language.yaml.section.SectionQueryHandler;
import com.winterhavenmc.util.messagebuilder.language.yaml.section.SectionQueryHandlerFactory;
import com.winterhavenmc.util.messagebuilder.language.yaml.section.SectionQueryHandlerRegistry;
import com.winterhavenmc.util.messagebuilder.language.yaml.section.items.ItemQueryHandler;
import com.winterhavenmc.util.messagebuilder.language.yaml.section.items.ItemRecord;
import com.winterhavenmc.util.messagebuilder.language.yaml.section.messages.MessageQueryHandler;
import com.winterhavenmc.util.messagebuilder.language.yaml.section.messages.MessageRecord;

import java.util.Optional;

import static com.winterhavenmc.util.messagebuilder.util.Error.*;


public class YamlLanguageQueryHandler implements LanguageQueryHandler {

	private final SectionQueryHandlerRegistry sectionQueryHandlerRegistry;


	/**
	 * Class constructor
	 *
	 * @param configurationSupplier the language configuration
	 */
	public YamlLanguageQueryHandler(final ConfigurationSupplier configurationSupplier) {
		if (configurationSupplier == null) { throw new IllegalArgumentException(Parameter.NULL_CONFIGURATION.getMessage()); }

		// create the query handler registry
		sectionQueryHandlerRegistry = new SectionQueryHandlerRegistry();

		// create the section factory
		SectionQueryHandlerFactory sectionQueryHandlerFactory = new SectionQueryHandlerFactory(configurationSupplier);

		// Register the section handlers in the registry
		for (Section section : Section.values()) {
			sectionQueryHandlerRegistry.registerQueryHandler(section, sectionQueryHandlerFactory.createSectionHandler(section));
		}
//		sectionQueryHandlerRegistry.registerQueryHandler(Section.CONSTANTS, sectionQueryHandlerFactory.createSectionHandler(Section.CONSTANTS));
//		sectionQueryHandlerRegistry.registerQueryHandler(Section.ITEMS, sectionQueryHandlerFactory.createSectionHandler(Section.ITEMS));
//		sectionQueryHandlerRegistry.registerQueryHandler(Section.MESSAGES, sectionQueryHandlerFactory.createSectionHandler(Section.MESSAGES));
//		sectionQueryHandlerRegistry.registerQueryHandler(Section.TIME, sectionQueryHandlerFactory.createSectionHandler(Section.TIME));
	}


	@Override
	public SectionQueryHandler<?> getQueryHandler(Section section) {
		return (SectionQueryHandler<?>) sectionQueryHandlerRegistry.getQueryHandler(section);
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
