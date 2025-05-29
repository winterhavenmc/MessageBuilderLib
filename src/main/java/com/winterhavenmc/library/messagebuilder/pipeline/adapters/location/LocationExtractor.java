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

package com.winterhavenmc.library.messagebuilder.pipeline.adapters.location;

import com.winterhavenmc.library.messagebuilder.keys.MacroKey;
import com.winterhavenmc.library.messagebuilder.pipeline.containers.MacroStringMap;
import com.winterhavenmc.library.messagebuilder.pipeline.formatters.number.LocaleNumberFormatter;
import org.bukkit.Location;

import static com.winterhavenmc.library.messagebuilder.pipeline.adapters.Adapter.BuiltIn.LOCATION;


@Deprecated
public class LocationExtractor
{
	private final static String UNKNOWN_STRING = "-";
	private final LocaleNumberFormatter numberFormatter;


	public LocationExtractor(final LocaleNumberFormatter numberFormatter)
	{
		this.numberFormatter = numberFormatter;
	}


	MacroStringMap extract(final MacroKey baseKey,
				 final Locatable locatable)
	{
		MacroStringMap resultMap = new MacroStringMap();
		baseKey.append(LOCATION).ifPresent(macroKey -> locationExtractor(baseKey, locatable, resultMap));
		return resultMap;
	}


	void locationExtractor(MacroKey baseKey, Locatable locatable, MacroStringMap fields)
	{
		Location location = locatable.getLocation();
		if (location != null)
		{
			if (!baseKey.toString().endsWith("LOCATION"))
			{
				baseKey = baseKey.append(LOCATION).orElse(baseKey);
			}

			fields.put(baseKey, getLocationString(location));
			baseKey.append(LocationField.STRING).ifPresent(macroKey -> fields.put(macroKey, getLocationString(location)));
			baseKey.append(LocationField.WORLD).ifPresent(macroKey -> fields.put(macroKey, getLocationWorldName(location)));
			baseKey.append(LocationField.X).ifPresent(macroKey -> fields.put(macroKey, numberFormatter.getFormatted(location.getBlockX())));
			baseKey.append(LocationField.Y).ifPresent(macroKey -> fields.put(macroKey, numberFormatter.getFormatted(location.getBlockY())));
			baseKey.append(LocationField.Z).ifPresent(macroKey -> fields.put(macroKey, numberFormatter.getFormatted(location.getBlockZ())));
		}
	}


	String getLocationWorldName(final Location location)
	{
		return (location != null && location.getWorld() != null)
				? location.getWorld().getName()
				: UNKNOWN_STRING;
	}


	String getLocationString(final Location location)
	{
		return (location != null)
				? getLocationWorldName(location) +
				" [" + String.join(", ",
				numberFormatter.getFormatted(location.getBlockX()),
				numberFormatter.getFormatted(location.getBlockY()),
				numberFormatter.getFormatted(location.getBlockZ())) + "]"
				: UNKNOWN_STRING;
	}


	enum LocationField
	{
		STRING, WORLD, X, Y, Z
	}
}
