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

package com.winterhavenmc.library.messagebuilder.pipeline.extractor;

import com.winterhavenmc.library.messagebuilder.pipeline.adapters.Adapter;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.displayname.DisplayNameAdapter;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.displayname.DisplayNameable;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.location.Locatable;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.location.LocationAdapter;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.name.NameAdapter;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.name.Nameable;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.quantity.Quantifiable;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.quantity.QuantityAdapter;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.uuid.Identifiable;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.uuid.UniqueIdAdapter;
import com.winterhavenmc.library.messagebuilder.keys.MacroKey;
import com.winterhavenmc.library.messagebuilder.pipeline.containers.MacroStringMap;
import org.bukkit.Location;

import static com.winterhavenmc.library.messagebuilder.pipeline.adapters.Adapter.BuiltIn.*;


public class FieldExtractor implements Extractor
{
	public <T> MacroStringMap extract(MacroKey baseKey, Adapter adapter, T adapted)
	{
		MacroStringMap fields = new MacroStringMap();

		switch (adapter)
		{
			case NameAdapter __ when adapted instanceof Nameable nameable ->
			{
				baseKey.append(NAME).ifPresent(macroKey -> fields.put(macroKey, nameable.getName()));
				fields.put(baseKey, nameable.getName());
			}

			case DisplayNameAdapter __ when adapted instanceof DisplayNameable displayNameable ->
			{
				baseKey.append(DISPLAY_NAME).ifPresent(macroKey -> fields.put(macroKey, displayNameable.getDisplayName()));
				fields.putIfAbsent(baseKey, displayNameable.getDisplayName());
			}

			case UniqueIdAdapter __ when adapted instanceof Identifiable identifiable ->
			{
				baseKey.append(UUID).ifPresent(macroKey -> fields.put(macroKey, identifiable.getUniqueId().toString()));
				fields.putIfAbsent(baseKey, identifiable.getUniqueId().toString());
			}

			case QuantityAdapter __ when adapted instanceof Quantifiable quantifiable ->
			{
				String quantityString = String.valueOf(quantifiable.getQuantity());
				baseKey.append(QUANTITY).ifPresent(macroKey -> fields.put(macroKey, quantityString));
				fields.putIfAbsent(baseKey, quantityString);
			}

//			case Adapter a when a instanceof LocationAdapter && adapted instanceof Locatable locatable -> // stricter version
			case LocationAdapter __ when adapted instanceof Locatable locatable ->
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
					baseKey.append(LocationField.X).ifPresent(macroKey -> fields.put(macroKey, String.valueOf(location.getBlockX())));
					baseKey.append(LocationField.Y).ifPresent(macroKey -> fields.put(macroKey, String.valueOf(location.getBlockY())));
					baseKey.append(LocationField.Z).ifPresent(macroKey -> fields.put(macroKey, String.valueOf(location.getBlockZ())));
				}
			}

			default -> {} // no-op
		}

		return fields;
	}


	String getLocationWorldName(final Location location)
	{
		return (location != null && location.getWorld() != null)
				? location.getWorld().getName()
				: "???";
	}


	String getLocationString(final Location location)
	{
		if (location == null)
		{
			return "???";
		}

		String worldName = getLocationWorldName(location);

		return worldName + " [" + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ() + "]";
	}


	enum LocationField
	{
		STRING, WORLD, X, Y, Z
	}

}
