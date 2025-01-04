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

package com.winterhavenmc.util.messagebuilder.resolvers.quantity;

import org.bukkit.inventory.ItemStack;
import java.util.Collection;


public class QuantityResolver {
	public static Quantifiable asQuantifiable(Object obj) {
		return switch (obj) {
			case ItemStack itemStack -> itemStack::getAmount;
			case Collection<?> collection -> collection::size;
			case null, default -> null;
		};
	}

	public static String getPlaceHolder() {
		return "QUANTITY";
	}

}
