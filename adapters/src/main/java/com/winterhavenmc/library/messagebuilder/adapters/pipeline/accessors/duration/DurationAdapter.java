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

package com.winterhavenmc.library.messagebuilder.adapters.pipeline.accessors.duration;

import com.winterhavenmc.library.messagebuilder.adapters.pipeline.accessors.FieldAccessorRegistry;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.Accessor;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.duration.Durationable;

import java.util.Optional;


/**
 * Accessor for objects that expose a {@link java.time.Duration} via the {@link Durationable} interface.
 *
 * <p>This adapter supports macro resolution for duration-based placeholders such as
 * {@code {OBJECT.DURATION}}. It delegates all adaptation responsibilities to the object itself,
 * requiring it to explicitly implement {@link Durationable}.
 *
 * <p>This design assumes that no Bukkit-provided types expose a {@code getDuration()} method
 * by default. If such types are discovered in the future, they can be added to this adapter
 * using additional instanceof branches or method references.
 *
 * <p>Accessor type: {@link BuiltIn#DURATION} (single-value field).
 *
 * @see Durationable
 * @see FieldAccessorRegistry AccessorRegistry
 */
public class DurationAdapter implements Accessor
{
	/**
	 * Attempts to adapt the given object into a {@link Durationable} if it explicitly implements the interface.
	 *
	 * @param obj the object to adapt
	 * @return an {@code Optional<Durationable>} if the object is a {@code Durationable}, otherwise empty
	 */
	@Override
	public Optional<Durationable> adapt(final Object obj)
	{
		return (obj instanceof Durationable durationable)
				? Optional.of(durationable)
				: Optional.empty();
	}

}
