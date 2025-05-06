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

package com.winterhavenmc.library.messagebuilder.pipeline.adapters;

import com.winterhavenmc.library.messagebuilder.pipeline.adapters.displayname.DisplayNameAdapter;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.location.LocationAdapter;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.name.NameAdapter;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.quantity.QuantityAdapter;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.uuid.UniqueIdAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static com.winterhavenmc.library.messagebuilder.validation.ErrorMessageKey.PARAMETER_NULL;
import static com.winterhavenmc.library.messagebuilder.validation.Parameter.ADAPTER;
import static com.winterhavenmc.library.messagebuilder.validation.ValidationHandler.throwing;
import static com.winterhavenmc.library.messagebuilder.validation.Validator.validate;


/**
 * Registry of adapters provides cache of built-in and plugin registered adapters.
 * Lazy loading of adapters is provided by computeIfAbsent method.
 */
public class AdapterRegistry
{
	private final List<Adapter> adapters = new ArrayList<>();


	public AdapterRegistry(final AdapterContextContainer adapterContextContainer)
	{
		// Register adapters in preferred priority order
		register(new NameAdapter());
		register(new DisplayNameAdapter(adapterContextContainer));
		register(new LocationAdapter());
		register(new QuantityAdapter());
		register(new UniqueIdAdapter());
	}


	public void register(Adapter adapter)
	{
		validate(adapter, Objects::isNull, throwing(PARAMETER_NULL, ADAPTER));
		adapters.add(adapter);
	}


	/**
	 * Returns all adapters that support the given value (based on instanceof checks inside each adapter).
	 */
	public Stream<Adapter> getMatchingAdapters(Object value)
	{
		if (value == null) return Stream.empty();
		return adapters.stream().filter(adapter -> adapter.supports(value));
	}

}
