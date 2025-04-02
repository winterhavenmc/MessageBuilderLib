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

package com.winterhavenmc.util.messagebuilder.pipeline;

import com.winterhavenmc.util.messagebuilder.pipeline.processors.DependencyContext;
import com.winterhavenmc.util.messagebuilder.pipeline.processors.ProcessorRegistry;
import com.winterhavenmc.util.messagebuilder.pipeline.processors.ProcessorType;
import com.winterhavenmc.util.messagebuilder.pipeline.processors.ResultMap;
import com.winterhavenmc.util.messagebuilder.validation.ValidationException;

import java.util.Objects;

import static com.winterhavenmc.util.messagebuilder.validation.ExceptionMessageKey.PARAMETER_NULL;
import static com.winterhavenmc.util.messagebuilder.validation.Parameter.CONTEXT_MAP;
import static com.winterhavenmc.util.messagebuilder.validation.Validator.validate;

public class ContextResolver implements Resolver
{
	private final ProcessorRegistry processorRegistry;


	/**
	 * Class constructor
	 */
	public ContextResolver()
	{
		processorRegistry = new ProcessorRegistry(new DependencyContext());
	}


	/**
	 * Convert the value objects contained in the context map into their string representations in a
	 * new result map.
	 *
	 * @param contextMap a map containing key/value pairs of placeholder strings and their corresponding value object
	 * @return {@code ResultMap} a map containing the placeholder strings and the string representations of the values
	 */
	@Override
	public ResultMap resolve(ContextMap contextMap)
	{
		validate(contextMap, Objects::isNull, () -> new ValidationException(PARAMETER_NULL, CONTEXT_MAP));

		return contextMap.entrySet().stream()
				.map(entry -> processorRegistry.get(ProcessorType.matchType(entry.getValue()))
						.resolveContext(entry.getKey(), contextMap))
				.collect(ResultMap::new, ResultMap::putAll, ResultMap::putAll);
	}

}
