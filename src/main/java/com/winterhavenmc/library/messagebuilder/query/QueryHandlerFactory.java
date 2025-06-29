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

package com.winterhavenmc.library.messagebuilder.query;

import com.winterhavenmc.library.messagebuilder.resources.language.SectionResourceManager;
import com.winterhavenmc.library.messagebuilder.resources.language.SectionProvider;
import com.winterhavenmc.library.messagebuilder.model.language.Section;
import com.winterhavenmc.library.messagebuilder.model.language.SectionRecord;

import java.util.Objects;

import static com.winterhavenmc.library.messagebuilder.validation.ErrorMessageKey.PARAMETER_NULL;
import static com.winterhavenmc.library.messagebuilder.validation.Parameter.LANGUAGE_RESOURCE_MANAGER;
import static com.winterhavenmc.library.messagebuilder.validation.Parameter.SECTION;
import static com.winterhavenmc.library.messagebuilder.validation.Validator.throwing;
import static com.winterhavenmc.library.messagebuilder.validation.Validator.validate;


public class QueryHandlerFactory
{
	private final SectionResourceManager languageResourceManager;


	/**
	 * Class constructor
	 *
	 */
	public QueryHandlerFactory(final SectionResourceManager languageResourceManager)
	{
		validate(languageResourceManager, Objects::isNull, throwing(PARAMETER_NULL, LANGUAGE_RESOURCE_MANAGER));

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
		validate(section, Objects::isNull, throwing(PARAMETER_NULL, SECTION));

		SectionProvider provider = languageResourceManager.getSectionProvider(section);
		return section.createHandler(provider);
	}

}
