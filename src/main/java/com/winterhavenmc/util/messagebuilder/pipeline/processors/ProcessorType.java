/*
 * Copyright (c) 2022-2025 Tim Savage.
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

package com.winterhavenmc.util.messagebuilder.pipeline.processors;

import java.util.function.Function;


public enum ProcessorType
{
	;

	private final Function<DependencyContext, MacroProcessor> creator;


	/**
	 * Enum constructor
	 *
	 */
	ProcessorType(final Function<DependencyContext, MacroProcessor> creator)
	{
		this.creator = creator;
	}


	/**
	 * Instantiate a macro processor for the type represented by this enum constant
	 *
	 * @param context a dependency injection container
	 * @return a newly created instance of a macro processor
	 */
	public MacroProcessor create(final DependencyContext context)
	{
		return creator.apply(context);
	}


	/**
	 * Static method that returns an appropriate {@code ProcessorType} for a given object. Null objects
	 * return a NULL processor type, and unmatched objects fall through to the default case and return
	 * an OBJECT processor type.
	 *
	 * @param object the object to match to a ProcessorType
	 * @return {@code ProcessorType} the matching processor type for the object
	 */
	public static ProcessorType matchType(final Object object)
	{
		return switch (object) {
			default -> null;
		};
	}

}
