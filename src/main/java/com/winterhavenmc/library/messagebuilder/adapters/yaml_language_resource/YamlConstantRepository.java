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

import com.winterhavenmc.library.messagebuilder.keys.ValidConstantKey;
import com.winterhavenmc.library.messagebuilder.ports.language_resource.ConstantRepository;
import com.winterhavenmc.library.messagebuilder.resources.language.SectionProvider;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.winterhavenmc.library.messagebuilder.validation.ErrorMessageKey.PARAMETER_NULL;
import static com.winterhavenmc.library.messagebuilder.validation.Parameter.SECTION_SUPPLIER;
import static com.winterhavenmc.library.messagebuilder.validation.Validator.*;


public class YamlConstantRepository implements ConstantRepository
{
	private final SectionProvider sectionProvider;


	public YamlConstantRepository(final SectionProvider sectionProvider)
	{
		validate(sectionProvider, Objects::isNull, throwing(PARAMETER_NULL, SECTION_SUPPLIER));
		this.sectionProvider = sectionProvider;
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
