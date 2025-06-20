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


/**
 * Adapter for objects that implement the {@link Expirable} interface.
 *
 * <p>This adapter allows eligible objects to be recognized as providing expiration-related data.
 * If the provided object implements {@code Expirable}, it will be wrapped and returned for
 * macro extraction of expiration-based values such as duration and timestamp.
 *
 * <p>Supports macro replacements such as:
 * <ul>
 *   <li>{@code [OBJECT.EXPIRATION.DURATION}} – formatted time until expiration</li>
 *   <li>{@code [OBJECT.EXPIRATION.INSTANT}} – formatted expiration date/time</li>
 * </ul>
 *
 * <p>This adapter is commonly used in time-sensitive contexts such as expiring items, events,
 * cooldowns, or protection windows where expiration tracking is necessary.
 */
public class ExpirationAdapter implements Adapter
{
	/**
	 * Attempts to adapt the given object into an {@link Expirable}, if applicable.
	 *
	 * @param obj the object to be checked for adaptation
	 * @return an {@code Optional} containing the object as an {@code Expirable}, or empty if not supported
	 */
	@Override
	public Optional<Expirable> adapt(final Object obj)
	{
		return (obj instanceof Expirable expirable)
				? Optional.of(expirable)
				: Optional.empty();
	}

}
