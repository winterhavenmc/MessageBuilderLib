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
import com.winterhavenmc.util.messagebuilder.validation.ValidationException;

import java.util.Objects;

import static com.winterhavenmc.util.messagebuilder.validation.MessageKey.PARAMETER_NULL;
import static com.winterhavenmc.util.messagebuilder.validation.Parameter.CONFIGURATION_SUPPLIER;
import static com.winterhavenmc.util.messagebuilder.validation.Parameter.SECTION;
import static com.winterhavenmc.util.messagebuilder.validation.Validate.validate;


public class YamlLanguageQueryHandler implements LanguageQueryHandler
{
	private final YamlConfigurationSupplier yamlConfigurationSupplier;

	/**
	 * Class constructor
	 *
	 * @param configurationSupplier the language configuration supplier
	 */
	public YamlLanguageQueryHandler(final YamlConfigurationSupplier configurationSupplier)
	{
		validate(configurationSupplier, Objects::isNull, () -> new ValidationException(PARAMETER_NULL, CONFIGURATION_SUPPLIER));

		this.yamlConfigurationSupplier = configurationSupplier;
	}


	@Override
	public SectionQueryHandler getSectionQueryHandler(Section section)
	{
		validate(section, Objects::isNull, () -> new ValidationException(PARAMETER_NULL, SECTION));

		return section.getQueryHandler(yamlConfigurationSupplier);
	}


	/**
	 * @return the language configuration supplier
	 */
	@Override
	public YamlConfigurationSupplier getConfigurationSupplier()
	{
		return yamlConfigurationSupplier;
	}

}
