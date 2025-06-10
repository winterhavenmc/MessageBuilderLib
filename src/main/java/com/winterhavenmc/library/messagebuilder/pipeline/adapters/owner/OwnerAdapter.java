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

package com.winterhavenmc.library.messagebuilder.pipeline.adapters.owner;

import com.winterhavenmc.library.messagebuilder.pipeline.adapters.Adapter;
import org.bukkit.entity.Tameable;

import java.util.Optional;


/**
 * Adapter for {@link Ownable} objects with an associated owner. Any object that has a known method
 * for retrieving an Ownable will be returned as an {@link Optional} {@code Ownable} object type,
 * with a {@code getOwner()} method. This method will be mapped to the actual method of the object that returns
 * an {@code Optional} owner, regardless of its real method owner. Any object that is not known to have a
 * owner will result in an empty {@code Optional} being returned from the {@code asLocatable} method.
 */
public class OwnerAdapter implements Adapter
{
	/**
	 * Return an {@link Optional} of {@code Ownable}, or an empty Optional if the passed
	 * object is not known to have an associated getOwner method. The Optional value, if present,
	 * implements the {@code Ownable} Interface, and is guaranteed to have a {@code getOwner()} method
	 * that maps to the adapted type's underlying {@code getOwner()} method.
	 *
	 * @param obj the object being evaluated as being {@code Ownable}
	 * @return an {@code Optional} of the object as a Ownable type, or an empty Optional if the passed
	 * object does not have a known method of retrieving a owner.
	 */
	@Override
	public Optional<Ownable> adapt(final Object obj)
	{
		return switch (obj)
		{
			case Ownable ownable -> Optional.of(ownable);
			case Tameable tameable -> Optional.of(tameable::getOwner);
			case null, default -> Optional.empty();
		};
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
//		return value instanceof Ownable
//				|| value instanceof Tameable;
//	}

}
