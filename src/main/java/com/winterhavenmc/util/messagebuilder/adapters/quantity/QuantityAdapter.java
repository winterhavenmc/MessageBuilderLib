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

package com.winterhavenmc.util.messagebuilder.adapters.quantity;

import com.winterhavenmc.util.messagebuilder.adapters.Adapter;

import org.bukkit.inventory.ItemStack;
import java.util.Collection;
import java.util.Optional;


/**
 * Adapter for {@link Quantifiable} objects with an associated quantity. Any object that has a known method
 * for retrieving an {@code int} quantity will be returned as an {@link Optional} {@code Quantifiable} object type,
 * with the assurance of the existence of a {@code getQuantity()} method. This method will be mapped to the actual
 * method of the object that returns an {@code int} quantity, regardless of its real method name. Any object that
 * is not known to have a quantity will result in an empty {@code Optional} being returned from the
 * static {@code asQuantifiable} method.
 */
public class QuantityAdapter implements Adapter
{
	/**
	 * Static method that returns an {@link Optional} of {@code Quantifiable}, or an empty {@code Optional} if the passed
	 * object is not known to have an associated quantity. The {@code Optional} value, if present, implements the
	 * {@code Quantifiable} Interface, and is guaranteed to have a {@code getQuantity()} method.
	 *
	 * @param obj the object being evaluated as being Quantifiable
	 * @return an {@code Optional} of the object as a {@code Quantifiable}, or an empty Optional if the passed
	 * object does not have a known method of retrieving a quantity.
	 */
	@Override
	public Optional<Quantifiable> adapt(final Object obj)
	{
		return switch (obj)
		{
			case ItemStack itemStack -> Optional.of(itemStack::getAmount);
			case Collection<?> collection -> Optional.of(collection::size);
			case null, default -> Optional.empty();
		};
	}

}
