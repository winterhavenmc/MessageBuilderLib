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

package com.winterhavenmc.library.messagebuilder.adapters.pipeline.accessors.quantity;

import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.Accessor;

import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.quantity.Quantifiable;

import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.Optional;


/**
 * Accessor implementation for extracting quantity values from supported objects.
 * <p>
 * This adapter enables macro substitution for {@code {OBJECT.QUANTITY}} by adapting
 * known types into {@link Quantifiable} instances. Supported types include:
 * <ul>
 *     <li>{@link Quantifiable} — directly implemented by plugins</li>
 *     <li>{@link ItemStack} — maps to {@code getAmount()}</li>
 *     <li>{@link Chest} — maps to inventory size via {@code getInventory().getSize()}</li>
 *     <li>{@link Inventory} — maps to inventory size via {@code getSize()}</li>
 *     <li>{@link Collection} — maps to {@code size()}</li>
 * </ul>
 *
 * <p>Plugins may implement {@link Quantifiable} directly to add custom macro support
 * for other object types.
 *
 * @see Quantifiable
 * @see Accessor
 */
public class BukkitQuantityAccessor implements Accessor
{
	/**
	 * Attempts to adapt the given object into a {@link Quantifiable} instance.
	 *
	 * @param obj the object to adapt
	 * @return an {@code Optional<Quantifiable>} if the object is supported, otherwise empty
	 */
	@Override
	public Optional<Quantifiable> adapt(final Object obj)
	{
		return switch (obj)
		{
			case Quantifiable quantifiable -> Optional.of(quantifiable);
			case ItemStack itemStack -> Optional.of(itemStack::getAmount);
			case Chest chest -> Optional.of(() -> chest.getInventory().getSize());
			case Inventory inventory -> Optional.of(inventory::getSize);
			case Collection<?> collection -> Optional.of(collection::size);
			case null, default -> Optional.empty();
		};
	}

}
