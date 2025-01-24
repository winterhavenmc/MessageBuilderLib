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
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.items.ItemSectionQueryHandler;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.items.ItemRecord;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.messages.MessageSectionQueryHandler;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.messages.MessageRecord;
import com.winterhavenmc.util.messagebuilder.util.LocalizedException;

import java.util.Optional;

import static com.winterhavenmc.util.messagebuilder.util.LocalizedException.MessageKey.PARAMETER_NULL;


public class YamlLanguageQueryHandler implements LanguageQueryHandler {

	private final YamlConfigurationSupplier yamlConfigurationSupplier;

	/**
	 * Class constructor
	 *
	 * @param yamlConfigurationSupplier the language configuration supplier
	 */
	public YamlLanguageQueryHandler(final YamlConfigurationSupplier yamlConfigurationSupplier) {
		if (yamlConfigurationSupplier == null) { throw new LocalizedException(PARAMETER_NULL, "configurationSupplier"); }

		this.yamlConfigurationSupplier = yamlConfigurationSupplier;
	}


	@Override
	public SectionQueryHandler getSectionQueryHandler(Section section) {
		return section.getQueryHandler(yamlConfigurationSupplier);
	}


	/**
	 * @return the language configuration supplier
	 */
	@Override
	public YamlConfigurationSupplier getConfigurationSupplier() {
		return yamlConfigurationSupplier;
	}


	@Override
	public Optional<ItemRecord> getItemRecord(final String keyPath) {
		if (keyPath == null) { throw new LocalizedException(PARAMETER_NULL, "keyPath"); }

		ItemSectionQueryHandler itemSectionQueryHandler = Section.ITEMS.getQueryHandler(yamlConfigurationSupplier);
		return itemSectionQueryHandler.getRecord(keyPath);
	}


	@Override
	public <MessageId extends Enum<MessageId>> Optional<MessageRecord> getMessageRecord(final MessageId messageId) {
		if (messageId == null) { throw new LocalizedException(PARAMETER_NULL, "messageId"); }

		MessageSectionQueryHandler messageSectionQueryHandler = Section.MESSAGES.getQueryHandler(yamlConfigurationSupplier);
		return messageSectionQueryHandler.getRecord(messageId);
	}

}
