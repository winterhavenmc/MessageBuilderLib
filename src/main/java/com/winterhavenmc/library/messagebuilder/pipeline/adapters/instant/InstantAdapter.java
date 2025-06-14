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

package com.winterhavenmc.library.messagebuilder.pipeline.adapters.instant;

import com.winterhavenmc.library.messagebuilder.pipeline.adapters.Adapter;

import java.util.Optional;

/**
 * Adapts a class that contains an Instant and an appropriate accessor method
 */
public class InstantAdapter implements Adapter
{
	/**
	 * Adapt an object to an Optional of Instantable
	 *
	 * @param obj the object to adapt
	 * @return {@code Optional<Instantable>} object, or empty Optional if not adaptable
	 */
	@Override
	public Optional<Instantable> adapt(final Object obj)
	{
		return (obj instanceof Instantable instantable)
				? Optional.of(instantable)
				: Optional.empty();
	}

}
