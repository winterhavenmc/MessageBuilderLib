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

package com.winterhavenmc.library.messagebuilder.pipeline.adapters.expiration;

import com.winterhavenmc.library.messagebuilder.pipeline.adapters.Adapter;

import java.util.Optional;


public class ExpirationAdapter implements Adapter
{
	@Override
	public Optional<Expirable> adapt(final Object obj)
	{
		return (obj instanceof Expirable expirable)
				? Optional.of(expirable)
				: Optional.empty();
	}


//	/**
//	 * Returns true if the given object is of a type supported by this adapter interface
//	 *
//	 * @param value the object to evaluate for adaptability
//	 * @return {@code true} if the object is of an adaptable type, or {@code false} if not
//	 */
//	@Override
//	public boolean supports(final Object value)
//	{
//		return value instanceof Expirable;
//	}

}
