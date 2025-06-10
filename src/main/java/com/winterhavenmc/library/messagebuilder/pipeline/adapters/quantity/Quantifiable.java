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

import static com.winterhavenmc.library.messagebuilder.pipeline.adapters.Adapter.BuiltIn.QUANTITY;


/**
 * An interface that describes objects that have a {@code getQuantity()}
 * method that returns a quantity as an {@code int}.
 */
@FunctionalInterface
public interface Quantifiable
{
	int getQuantity();


	/**
	 * Returns a new MacroStringMap containing all fields extracted from a Quantifiable type
	 *
	 * @param baseKey the top level key for the fields of this object
	 * @param ctx containing numberFormatter the duration formatter to be used to convert the duration to a String
	 * @return a MacroStringMap containing the fields extracted for objects of Quantifiable type
	 */
	default MacroStringMap extractQuantity(final MacroKey baseKey, final AdapterContextContainer ctx)
	{
		return baseKey.append(QUANTITY)
				.map(macroKey -> new MacroStringMap()
				.with(macroKey, formatQuantity(getQuantity(), ctx.formatterContainer().localeNumberFormatter())))
				.orElseGet(MacroStringMap::empty);
	}


	/**
	 * Returns a formatted string representing a duration of time, using the supplied DurationFormatter
	 *
	 * @param numberFormatter the number formatter to be used to convert the quantity to a String
	 * @return a formatted String representing the quantity of the Quantifiable conforming object
	 */
	static String formatQuantity(final int quantity,
								 final NumberFormatter numberFormatter)
	{
		return numberFormatter.getFormatted(quantity);
	}

}
