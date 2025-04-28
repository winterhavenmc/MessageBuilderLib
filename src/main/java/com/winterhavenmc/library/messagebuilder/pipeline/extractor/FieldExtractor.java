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
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;

import static com.winterhavenmc.library.messagebuilder.pipeline.adapters.Adapter.BuiltIn.*;


public class FieldExtractor implements Extractor
{
	@Override
	public <T> Map<MacroKey, Object> extract(Adapter adapter, T adapted, MacroKey baseKey)
	{
		Map<MacroKey, Object> fields = new HashMap<>();

		switch (adapter)
		{
			case NameAdapter ignored when adapted instanceof Nameable nameable ->
			{
				baseKey.append(NAME).ifPresent(macroKey -> fields.put(macroKey, nameable.getName()));
				fields.putIfAbsent(baseKey, nameable.getName());
			}

			case DisplayNameAdapter ignored when adapted instanceof DisplayNameable displayNameable ->
			{
				baseKey.append(DISPLAY_NAME).ifPresent(macroKey -> fields.put(macroKey, displayNameable.getDisplayName()));
				fields.putIfAbsent(baseKey, displayNameable.getDisplayName());
			}

			case UniqueIdAdapter ignored when adapted instanceof Identifiable identifiable ->
			{
				baseKey.append(UUID).ifPresent(macroKey -> fields.put(macroKey, identifiable.getUniqueId()));
				fields.putIfAbsent(baseKey, identifiable.getUniqueId());
			}

			case LocationAdapter ignored when adapted instanceof Locatable locatable ->
			{
				if (!baseKey.toString().endsWith(LOCATION.name()))
				{
					baseKey = baseKey.append(LOCATION).orElse(baseKey);
				}

				fields.putIfAbsent(baseKey, getLocationString(locatable.getLocation()));
				baseKey.append(LocationField.STRING).ifPresent(macroKey ->
						fields.putIfAbsent(macroKey, getLocationString(locatable.getLocation())));
				baseKey.append(LocationField.WORLD).ifPresent(macroKey ->
						fields.putIfAbsent(macroKey, getLocationWorldName(locatable.getLocation())));
				baseKey.append(LocationField.X).ifPresent(macroKey ->
						fields.putIfAbsent(macroKey, locatable.getLocation().getBlockX()));
				baseKey.append(LocationField.Y).ifPresent(macroKey ->
						fields.putIfAbsent(macroKey, locatable.getLocation().getBlockY()));
				baseKey.append(LocationField.Z).ifPresent(macroKey ->
						fields.putIfAbsent(macroKey, locatable.getLocation().getBlockZ()));
			}

			case QuantityAdapter ignored when adapted instanceof Quantifiable quantifiable ->
			{
				baseKey.append(QUANTITY).ifPresent(macroKey -> fields.put(macroKey, quantifiable.getQuantity()));
				fields.putIfAbsent(baseKey, quantifiable.getQuantity());
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
