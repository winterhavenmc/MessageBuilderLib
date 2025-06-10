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

package com.winterhavenmc.library.messagebuilder.pipeline.adapters;

import java.util.Optional;


public interface Adapter
{
	String UNKNOWN_VALUE = "=";

	Optional<?> adapt(Object obj);


	default boolean supports(final Object value)
	{
		return adapt(value).isPresent();
	}


	enum BuiltIn
	{
		// single value fields
		NAME,
		DISPLAY_NAME,
		OWNER,
		LOOTER,
		KILLER,
		DURATION,
		INSTANT,
		QUANTITY,
		UUID,

		// compound fields
		LOCATION,
		EXPIRATION,
		PROTECTION,
	}

}
