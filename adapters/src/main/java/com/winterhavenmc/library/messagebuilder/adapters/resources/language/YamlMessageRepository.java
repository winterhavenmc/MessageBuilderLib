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

package com.winterhavenmc.library.messagebuilder.adapters.resources.language;

import com.winterhavenmc.library.messagebuilder.core.ports.resourcemanagers.language.LanguageResourceManager;
import com.winterhavenmc.library.messagebuilder.core.ports.resourcemanagers.language.MessageRepository;
import com.winterhavenmc.library.messagebuilder.core.ports.resourcemanagers.language.SectionProvider;

import com.winterhavenmc.library.messagebuilder.models.keys.ValidMessageKey;
import com.winterhavenmc.library.messagebuilder.models.language.InvalidMessageRecord;
import com.winterhavenmc.library.messagebuilder.models.language.InvalidRecordReason;
import com.winterhavenmc.library.messagebuilder.models.language.MessageRecord;

import com.winterhavenmc.library.messagebuilder.models.language.Section;
import com.winterhavenmc.library.messagebuilder.validation.ErrorMessageKey;
import com.winterhavenmc.library.messagebuilder.validation.Parameter;
import com.winterhavenmc.library.messagebuilder.validation.Validator;

import java.util.Objects;

import static com.winterhavenmc.library.messagebuilder.validation.Validator.validate;


public class YamlMessageRepository implements MessageRepository
{
	private final SectionProvider sectionProvider;


	public YamlMessageRepository(final LanguageResourceManager languageResourceManager)
	{
		validate(languageResourceManager, Objects::isNull, Validator.throwing(ErrorMessageKey.PARAMETER_NULL, Parameter.LANGUAGE_RESOURCE_MANAGER));
		this.sectionProvider = languageResourceManager.getSectionProvider(Section.MESSAGES);
	}


	@Override
	public MessageRecord getMessageRecord(final ValidMessageKey validMessageKey)
	{
		// confirm message section is not null
		if (sectionProvider.getSection() == null) return new InvalidMessageRecord(validMessageKey, InvalidRecordReason.MESSAGE_SECTION_MISSING);

		// confirm message entry in section is valid
		else return (sectionProvider.getSection().isConfigurationSection(validMessageKey.toString()))
				? MessageRecord.of(validMessageKey, sectionProvider.getSection().getConfigurationSection(validMessageKey.toString()))
				: MessageRecord.empty(validMessageKey, InvalidRecordReason.MESSAGE_ENTRY_MISSING);
	}
}
