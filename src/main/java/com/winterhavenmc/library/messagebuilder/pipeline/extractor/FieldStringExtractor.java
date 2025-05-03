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

import com.winterhavenmc.library.messagebuilder.keys.MacroKey;
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
import com.winterhavenmc.library.messagebuilder.pipeline.containers.MacroStringMap;
import org.bukkit.Location;

import static com.winterhavenmc.library.messagebuilder.pipeline.adapters.Adapter.BuiltIn.*;


public class FieldStringExtractor
{
	public <T> MacroStringMap extract(Adapter adapter, T adapted, MacroKey baseKey)
	{
		MacroStringMap fields = new MacroStringMap();

		switch (adapter)
		{
			case NameAdapter ignored when adapted instanceof Nameable nameable ->
			{
				fields.put(baseKey, nameable.getName());
				baseKey.append(NAME).ifPresent(macroKey -> fields.put(macroKey, nameable.getName()));
			}

			case DisplayNameAdapter ignored when adapted instanceof DisplayNameable displayNameable ->
			{
				fields.put(baseKey, displayNameable.getDisplayName());
				baseKey.append(DISPLAY_NAME).ifPresent(macroKey -> fields.put(macroKey, displayNameable.getDisplayName()));
			}

			case UniqueIdAdapter ignored when adapted instanceof Identifiable identifiable ->
			{
				fields.put(baseKey, identifiable.getUniqueId().toString());
				baseKey.append(UUID).ifPresent(macroKey -> fields.put(macroKey, identifiable.getUniqueId().toString()));
			}

			case LocationAdapter ignored when adapted instanceof Locatable locatable ->
			{
				Location location = locatable.getLocation();
				if (location != null) {
					baseKey.append(LOCATION).ifPresent(locationKey ->
					{
						fields.put(baseKey, getLocationString(locatable.getLocation()));
						locationKey.append(LocationField.STRING).ifPresent(k -> fields.put(k, getLocationString(location)));
						locationKey.append(LocationField.WORLD).ifPresent(k -> fields.put(k, getLocationWorldName(location)));
						locationKey.append(LocationField.X).ifPresent(k -> fields.put(k, String.valueOf(location.getBlockX())));
						locationKey.append(LocationField.Y).ifPresent(k -> fields.put(k, String.valueOf(location.getBlockY())));
						locationKey.append(LocationField.Z).ifPresent(k -> fields.put(k, String.valueOf(location.getBlockZ())));
					});
				}
			}

			case QuantityAdapter ignored when adapted instanceof Quantifiable quantifiable ->
			{
				String quantityString = String.valueOf(quantifiable.getQuantity());
				fields.put(baseKey, quantityString);
				baseKey.append(QUANTITY).ifPresent(macroKey -> fields.put(macroKey, quantityString));
			}

			default -> {} // no-op
		}

		return fields;
	}


	private String getLocationWorldName(final Location location)
	{
		return (location != null && location.getWorld() != null)
				? location.getWorld().getName()
				: "???";
	}


	private String getLocationString(final Location location)
	{
		String worldName = (location.getWorld() != null)
				? location.getWorld().getName()
				: "???";

		return worldName + " [" + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ() + "]";
	}

	enum LocationField
	{
		STRING, WORLD, X, Y, Z
	}

}
