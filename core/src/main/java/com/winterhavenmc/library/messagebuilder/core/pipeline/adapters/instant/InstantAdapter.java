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

package com.winterhavenmc.library.messagebuilder.core.pipeline.adapters.instant;

import com.winterhavenmc.library.messagebuilder.core.pipeline.adapters.Adapter;

import java.util.Optional;

/**
 * An adapter that supports any object implementing the {@link Instantable} interface.
 *
 * <p>This adapter enables macro resolution for objects that expose an {@link java.time.Instant}
 * via the {@code getInstant()} method. It contributes the macro field:
 * <pre>{@code {OBJECT.INSTANT}}</pre>
 * to the {@code MacroStringMap} used for message composition and formatting.
 *
 * <p>This adapter is automatically registered with the {@code AdapterRegistry} in the core pipeline
 * and is available for plugin-defined objects that implement the {@code Instantable} interface.
 */
public class InstantAdapter implements Adapter
{
	/**
	 * Attempts to adapt an object into an {@link Instantable}, if it already implements the interface.
	 *
	 * @param obj the object to adapt
	 * @return an {@code Optional} containing the adapted {@code Instantable}, or empty if unsupported
	 */
	@Override
	public Optional<Instantable> adapt(final Object obj)
	{
		return (obj instanceof Instantable instantable)
				? Optional.of(instantable)
				: Optional.empty();
	}

}
