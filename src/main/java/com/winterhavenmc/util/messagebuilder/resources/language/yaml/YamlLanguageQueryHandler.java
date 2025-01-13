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

package com.winterhavenmc.util.messagebuilder.resources.language.yaml;

import com.winterhavenmc.util.messagebuilder.resources.language.LanguageQueryHandler;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.Section;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.SectionQueryHandler;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.SectionQueryHandlerFactory;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.SectionQueryHandlerRegistry;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.items.ItemSectionQueryHandler;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.items.ItemRecord;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.messages.MessageSectionQueryHandler;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.messages.MessageRecord;

import java.util.Optional;

import static com.winterhavenmc.util.messagebuilder.util.Error.*;


public class YamlLanguageQueryHandler implements LanguageQueryHandler {

	private final SectionQueryHandlerRegistry sectionQueryHandlerRegistry;


	/**
	 * Class constructor
	 *
	 * @param yamlConfigurationSupplier the language configuration supplier
	 */
	public YamlLanguageQueryHandler(final YamlConfigurationSupplier yamlConfigurationSupplier) {
		if (yamlConfigurationSupplier == null) { throw new IllegalArgumentException(Parameter.NULL_CONFIGURATION.getMessage()); }

		// create the query handler registry
		sectionQueryHandlerRegistry = new SectionQueryHandlerRegistry();

		// create the section factory
		SectionQueryHandlerFactory sectionQueryHandlerFactory = new SectionQueryHandlerFactory(yamlConfigurationSupplier);

		// Register the section handlers in the registry
		//TODO: this will be replaced by cached lazy-loading
		for (Section section : Section.values()) {
			sectionQueryHandlerRegistry.registerQueryHandler(section, sectionQueryHandlerFactory.createSectionHandler(section));
		}
	}


	@Override
	public SectionQueryHandler<?> getQueryHandler(Section section) {
		return (SectionQueryHandler<?>) sectionQueryHandlerRegistry.getQueryHandler(section);
	}


	@Override
	public Optional<ItemRecord> getItemRecord(final String keyPath) {
		if (keyPath == null) { throw new IllegalArgumentException(Parameter.NULL_ITEM_KEY.getMessage()); }

		SectionQueryHandler<?> queryHandler = getQueryHandler(Section.ITEMS);
		if (queryHandler instanceof ItemSectionQueryHandler itemSectionQueryHandler) {
			return itemSectionQueryHandler.getRecord(keyPath);
		}
		return Optional.empty();
	}


	@Override
	public <MessageId extends Enum<MessageId>> Optional<MessageRecord> getMessageRecord(final MessageId messageId) {
		if (messageId == null) { throw new IllegalArgumentException(Parameter.NULL_MESSAGE_ID.getMessage()); }

		SectionQueryHandler<?> queryHandler = getQueryHandler(Section.MESSAGES);
		if (queryHandler instanceof MessageSectionQueryHandler messageSectionQueryHandler) {
			return messageSectionQueryHandler.getRecord(messageId);
		}
		return Optional.empty();
	}

}
