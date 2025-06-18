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

package com.winterhavenmc.library.messagebuilder.pipeline.adapters.quantity;

import com.winterhavenmc.library.messagebuilder.keys.MacroKey;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.AdapterContextContainer;
import com.winterhavenmc.library.messagebuilder.pipeline.containers.MacroStringMap;
import com.winterhavenmc.library.messagebuilder.pipeline.formatters.number.NumberFormatter;

import java.util.Optional;

import static com.winterhavenmc.library.messagebuilder.pipeline.adapters.Adapter.BuiltIn.QUANTITY;
import static com.winterhavenmc.library.messagebuilder.pipeline.adapters.Adapter.UNKNOWN_VALUE;


/**
 * Represents objects that expose a numerical quantity for macro replacement.
 * <p>
 * This interface allows objects to contribute to the {@code {OBJECT.QUANTITY}} placeholder
 * using an integer-based {@link #getQuantity()} accessor. Implementing this interface enables
 * plugins to automatically extract and localize quantity values from plugin-defined or
 * Bukkit-provided objects.
 *
 * <p>Common examples of quantities include item stack sizes, inventory capacities,
 * or collection sizes.
 *
 * @see QuantityAdapter
 * @see com.winterhavenmc.library.messagebuilder.pipeline.adapters.Adapter Adapter
 */
@FunctionalInterface
public interface Quantifiable
{
	/**
	 * Returns the numeric quantity associated with this object.
	 *
	 * @return the quantity as an integer
	 */
	int getQuantity();


	/**
	 * Extracts a {@link MacroStringMap} containing the formatted quantity string.
	 * This value will be added using the subkey {@code QUANTITY} of the provided macro key.
	 *
	 * @param baseKey the macro key under which to store the formatted quantity
	 * @param ctx     the adapter context providing the number formatter
	 * @return a {@code MacroStringMap} with a formatted quantity string, or an empty map if the key cannot be derived
	 */
	default MacroStringMap extractQuantity(final MacroKey baseKey, final AdapterContextContainer ctx)
	{
		return baseKey.append(QUANTITY)
				.map(macroKey -> new MacroStringMap()
				.with(macroKey, formatQuantity(getQuantity(), ctx.formatterContainer().localeNumberFormatter()).orElse(UNKNOWN_VALUE)))
				.orElseGet(MacroStringMap::empty);
	}


	/**
	 * Converts a raw integer quantity into a localized string using the provided number formatter.
	 *
	 * @param quantity        the raw integer quantity
	 * @param numberFormatter the number formatter used for localization
	 * @return an optional containing the formatted string
	 */
	static Optional<String> formatQuantity(final int quantity,
										   final NumberFormatter numberFormatter)
	{
		return Optional.of(numberFormatter.getFormatted(quantity));
	}

}
