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

package com.winterhavenmc.library.messagebuilder.adapters.yaml_language_resource;

import com.winterhavenmc.library.messagebuilder.keys.ValidItemKey;
import com.winterhavenmc.library.messagebuilder.model.language.InvalidItemRecord;
import com.winterhavenmc.library.messagebuilder.model.language.InvalidRecordReason;
import com.winterhavenmc.library.messagebuilder.model.language.ItemRecord;
import com.winterhavenmc.library.messagebuilder.ports.language_resource.ItemRepository;
import com.winterhavenmc.library.messagebuilder.resources.language.SectionProvider;
import com.winterhavenmc.library.messagebuilder.validation.LogLevel;

import java.util.Objects;

import static com.winterhavenmc.library.messagebuilder.validation.ErrorMessageKey.PARAMETER_NULL;
import static com.winterhavenmc.library.messagebuilder.validation.Parameter.SECTION;
import static com.winterhavenmc.library.messagebuilder.validation.Parameter.SECTION_SUPPLIER;
import static com.winterhavenmc.library.messagebuilder.validation.Validator.*;


public class YamlItemRepository implements ItemRepository
{
	private final SectionProvider sectionProvider;


	public YamlItemRepository(final SectionProvider sectionProvider)
	{
		validate(sectionProvider, Objects::isNull, throwing(PARAMETER_NULL, SECTION_SUPPLIER));
		validate(sectionProvider.getSection(), Objects::isNull, logging(LogLevel.ERROR, PARAMETER_NULL, SECTION));

		this.sectionProvider = sectionProvider;
	}


	@Override
	public ItemRecord getItemRecord(ValidItemKey validItemKey)
	{
		// confirm item section is not null
		if (sectionProvider.getSection() == null) return new InvalidItemRecord(validItemKey, InvalidRecordReason.ITEM_SECTION_MISSING);

		// confirm item entry in section is valid
		else return (sectionProvider.getSection().isConfigurationSection(validItemKey.toString()))
				? ItemRecord.of(validItemKey, sectionProvider.getSection().getConfigurationSection(validItemKey.toString()))
				: ItemRecord.empty(validItemKey, InvalidRecordReason.ITEM_ENTRY_MISSING);
	}
}
