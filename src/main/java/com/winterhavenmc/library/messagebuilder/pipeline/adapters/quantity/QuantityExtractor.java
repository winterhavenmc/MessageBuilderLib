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
import com.winterhavenmc.library.messagebuilder.pipeline.containers.MacroStringMap;
import com.winterhavenmc.library.messagebuilder.pipeline.formatters.number.LocaleNumberFormatter;

import static com.winterhavenmc.library.messagebuilder.pipeline.adapters.Adapter.BuiltIn.QUANTITY;


@Deprecated
public class QuantityExtractor
{
	private final static String UNKNOWN_STRING = "-";
	private final LocaleNumberFormatter numberFormatter;


	public QuantityExtractor(LocaleNumberFormatter numberFormatter)
	{
		this.numberFormatter = numberFormatter;
	}


	MacroStringMap extract(final MacroKey baseKey,
				 final Quantifiable quantifiable)
	{
		MacroStringMap resultMap = new MacroStringMap();
		baseKey.append(QUANTITY).ifPresent(macroKey -> resultMap.put(macroKey, getQuantityString(quantifiable)));
		return resultMap;
	}


	String getQuantityString(final Quantifiable quantifiable)
	{
		return numberFormatter.getFormatted(quantifiable.getQuantity());
	}

}
