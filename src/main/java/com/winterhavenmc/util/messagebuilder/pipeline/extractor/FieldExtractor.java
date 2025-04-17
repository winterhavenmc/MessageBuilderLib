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

package com.winterhavenmc.util.messagebuilder.pipeline.extractor;

import com.winterhavenmc.util.messagebuilder.adapters.Adapter;
import com.winterhavenmc.util.messagebuilder.adapters.displayname.DisplayNameAdapter;
import com.winterhavenmc.util.messagebuilder.adapters.displayname.DisplayNameable;
import com.winterhavenmc.util.messagebuilder.adapters.location.Locatable;
import com.winterhavenmc.util.messagebuilder.adapters.location.LocationAdapter;
import com.winterhavenmc.util.messagebuilder.adapters.name.NameAdapter;
import com.winterhavenmc.util.messagebuilder.adapters.name.Nameable;
import com.winterhavenmc.util.messagebuilder.adapters.quantity.Quantifiable;
import com.winterhavenmc.util.messagebuilder.adapters.quantity.QuantityAdapter;
import com.winterhavenmc.util.messagebuilder.adapters.uuid.Identifiable;
import com.winterhavenmc.util.messagebuilder.adapters.uuid.UniqueIdAdapter;
import com.winterhavenmc.util.messagebuilder.keys.MacroKey;

import java.util.HashMap;
import java.util.Map;

import static com.winterhavenmc.util.messagebuilder.adapters.Adapter.BuiltIn.*;


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
				baseKey.append(LOCATION).ifPresent(macroKey -> fields.put(macroKey, locatable.getLocation()));
				fields.putIfAbsent(baseKey, locatable.getLocation());
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

}
