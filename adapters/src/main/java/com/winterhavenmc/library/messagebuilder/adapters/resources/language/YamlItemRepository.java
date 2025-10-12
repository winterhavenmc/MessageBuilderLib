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

import com.winterhavenmc.library.messagebuilder.core.ports.resources.language.ItemRepository;
import com.winterhavenmc.library.messagebuilder.core.ports.resources.SectionProvider;

import com.winterhavenmc.library.messagebuilder.models.keys.ValidItemKey;
import com.winterhavenmc.library.messagebuilder.models.language.InvalidItemRecord;
import com.winterhavenmc.library.messagebuilder.models.language.InvalidRecordReason;
import com.winterhavenmc.library.messagebuilder.models.language.ItemRecord;

import com.winterhavenmc.library.messagebuilder.models.language.Section;
import com.winterhavenmc.library.messagebuilder.models.validation.ErrorMessageKey;
import com.winterhavenmc.library.messagebuilder.models.validation.Parameter;

import java.util.Objects;

import static com.winterhavenmc.library.messagebuilder.models.validation.Validator.throwing;
import static com.winterhavenmc.library.messagebuilder.models.validation.Validator.validate;


public final class YamlItemRepository implements ItemRepository
{
	private final SectionProvider sectionProvider;


	public YamlItemRepository(final YamlLanguageResourceManager languageResourceManager)
	{
		validate(languageResourceManager, Objects::isNull, throwing(ErrorMessageKey.PARAMETER_NULL, Parameter.LANGUAGE_RESOURCE_MANAGER));
		this.sectionProvider = languageResourceManager.getSectionProvider(Section.ITEMS);
	}


	@Override
	public ItemRecord getRecord(final ValidItemKey validItemKey)
	{
		// confirm item section is not null
		if (sectionProvider.getSection() == null) return new InvalidItemRecord(validItemKey, InvalidRecordReason.ITEM_SECTION_MISSING);

		// confirm item entry in section is valid
		else return (sectionProvider.getSection().isConfigurationSection(validItemKey.toString()))
				? ItemRecord.of(validItemKey, sectionProvider.getSection().getConfigurationSection(validItemKey.toString()))
				: ItemRecord.empty(validItemKey, InvalidRecordReason.ITEM_ENTRY_MISSING);
	}
}
