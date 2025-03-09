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

import com.winterhavenmc.util.messagebuilder.resources.language.LanguageQueryHandlerFactory;
import com.winterhavenmc.util.messagebuilder.validation.ValidationException;

import static com.winterhavenmc.util.messagebuilder.validation.MessageKey.PARAMETER_INVALID;
import static com.winterhavenmc.util.messagebuilder.validation.Parameter.RESOURCE_TYPE;
import static com.winterhavenmc.util.messagebuilder.validation.Predicates.resourceTypeValid;
import static com.winterhavenmc.util.messagebuilder.validation.Validate.validate;


/**
 * Base for resource factories.
 * Implements common functionality for all resource factories.
 */
public abstract class AbstractQueryHandlerFactory implements QueryHandlerFactory
{
	@Override
	public LanguageQueryHandlerFactory getFactory(ResourceType resourceType)
	{
		validate(resourceType, resourceTypeValid(resourceType), () -> new ValidationException(PARAMETER_INVALID, RESOURCE_TYPE));

		return switch(resourceType)
		{
			case ResourceType.LANGUAGE -> createLanguageFactory();
		};
	}

	protected abstract LanguageQueryHandlerFactory createLanguageFactory();
}
