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

import com.winterhavenmc.library.messagebuilder.core.ports.resources.language.ConstantRepository;
import com.winterhavenmc.library.messagebuilder.core.ports.resources.language.SectionProvider;

import com.winterhavenmc.library.messagebuilder.models.keys.ValidConstantKey;

import com.winterhavenmc.library.messagebuilder.models.language.Section;
import com.winterhavenmc.library.messagebuilder.validation.ErrorMessageKey;
import com.winterhavenmc.library.messagebuilder.validation.Parameter;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.winterhavenmc.library.messagebuilder.validation.Validator.throwing;
import static com.winterhavenmc.library.messagebuilder.validation.Validator.validate;


public final class YamlConstantRepository implements ConstantRepository
{
	private final SectionProvider sectionProvider;


	public YamlConstantRepository(final LanguageResourceManager languageResourceManager)
	{
		validate(languageResourceManager, Objects::isNull, throwing(ErrorMessageKey.PARAMETER_NULL, Parameter.LANGUAGE_RESOURCE_MANAGER));
		this.sectionProvider = languageResourceManager.getSectionProvider(Section.CONSTANTS);
	}


	@Override
	public Optional<String> getString(ValidConstantKey validConstantKey)
	{
		return (sectionProvider.getSection() != null && sectionProvider.getSection().contains(validConstantKey.toString()))
				? Optional.ofNullable(sectionProvider.getSection().getString(validConstantKey.toString()))
				: Optional.empty();
	}


	@Override
	public List<String> getStringList(ValidConstantKey validConstantKey)
	{
		return (sectionProvider.getSection() != null && sectionProvider.getSection().contains(validConstantKey.toString()))
				? sectionProvider.getSection().getStringList(validConstantKey.toString())
				: List.of();
	}


	@Override
	public Optional<Integer> getInteger(ValidConstantKey validConstantKey)
	{
		return (sectionProvider.getSection() != null && sectionProvider.getSection().contains(validConstantKey.toString()))
				? Optional.of(sectionProvider.getSection().getInt(validConstantKey.toString()))
				: Optional.empty();
	}


	@Override
	public Optional<Boolean> getBoolean(ValidConstantKey validConstantKey)
	{
		return (sectionProvider.getSection() != null && sectionProvider.getSection().contains(validConstantKey.toString()))
				? Optional.of(sectionProvider.getSection().getBoolean((validConstantKey.toString())))
				: Optional.empty();
	}
}
