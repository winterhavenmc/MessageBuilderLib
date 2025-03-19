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

package com.winterhavenmc.util.messagebuilder.resources.language.yaml.section;

import com.winterhavenmc.util.messagebuilder.resources.language.yaml.YamlConfigurationSupplier;
import com.winterhavenmc.util.messagebuilder.validation.ValidationException;

import java.util.Objects;

import static com.winterhavenmc.util.messagebuilder.validation.MessageKey.PARAMETER_NULL;
import static com.winterhavenmc.util.messagebuilder.validation.Parameter.CONFIGURATION_SUPPLIER;
import static com.winterhavenmc.util.messagebuilder.validation.Parameter.SECTION;
import static com.winterhavenmc.util.messagebuilder.validation.Validator.validate;


/**
 * A factory that produces section query handlers for each top level section of the language file,
 * as enumerated by the constants of the {@link Section} enum
 */
public class SectionQueryHandlerFactory
{
	private final YamlConfigurationSupplier configurationSupplier;


	/**
	 * Class constructor for the section query handler factory. The constructor accepts a Bukkit Configuration
	 * as a parameter, and passes the appropriate top level ConfigurationSection to the constructor of the
	 * section query handler being produced.
	 *
	 * @param configurationSupplier the provider of the language configuration
	 */
	public SectionQueryHandlerFactory(YamlConfigurationSupplier configurationSupplier)
	{
		validate(configurationSupplier, Objects::isNull, () -> new ValidationException(PARAMETER_NULL, CONFIGURATION_SUPPLIER));

		this.configurationSupplier = configurationSupplier;
	}


	/**
	 * Get a section query handler. If the requested query handler is not in the cache, a new instance is created
	 * to be returned and placed in the cache.
	 *
	 * @param section A constant of the Section enum specifying which SectionQueryHandler to return
	 * @return The requested SectionQueryHandler
	 */
	public SectionQueryHandler getQueryHandler(Section section)
	{
		validate(section, Objects::isNull, () -> new ValidationException(PARAMETER_NULL, SECTION));

		return section.getQueryHandler(configurationSupplier);
	}


	/**
	 * Creates a query handler based on the provided section.
	 *
	 * @param section the section for which the query handler is to be created
	 * @return the corresponding SectionQueryHandler
	 * @throws ValidationException if section parameter is null
	 */
	public SectionQueryHandler createSectionHandler(Section section)
	{
		validate(section, Objects::isNull, () -> new ValidationException(PARAMETER_NULL, SECTION));

		return section.getQueryHandler(configurationSupplier);
	}

}
