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

package com.winterhavenmc.util.messagebuilder.resources;

import com.winterhavenmc.util.messagebuilder.resources.language.LanguageResourceManager;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.SectionProvider;
import com.winterhavenmc.util.messagebuilder.model.language.Section;
import com.winterhavenmc.util.messagebuilder.model.language.SectionRecord;
import com.winterhavenmc.util.messagebuilder.validation.ValidationHandler;

import java.util.Objects;

import static com.winterhavenmc.util.messagebuilder.validation.ErrorMessageKey.PARAMETER_NULL;
import static com.winterhavenmc.util.messagebuilder.validation.Parameter.LANGUAGE_RESOURCE_MANAGER;
import static com.winterhavenmc.util.messagebuilder.validation.Validator.validate;


public class QueryHandlerFactory
{
	private final LanguageResourceManager languageResourceManager;


	/**
	 * Class constructor
	 *
	 */
	public QueryHandlerFactory(final LanguageResourceManager languageResourceManager)
	{
		validate(languageResourceManager, Objects::isNull, ValidationHandler.throwing(PARAMETER_NULL, LANGUAGE_RESOURCE_MANAGER));

		this.languageResourceManager = languageResourceManager;
	}


	/**
	 * Retrieve query handler for section
	 *
	 * @param section the section for which a query handler is returned
	 * @return a query handler for the section
	 */
	public <R extends SectionRecord> QueryHandler<R> getQueryHandler(final Section section)
	{
		SectionProvider provider = languageResourceManager.getSectionProvider(section);
		return section.createHandler(provider);
	}

}
