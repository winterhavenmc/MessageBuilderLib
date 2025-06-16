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
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.duration.DurationAdapter;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.expiration.ExpirationAdapter;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.instant.InstantAdapter;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.location.LocationAdapter;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.killer.KillerAdapter;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.looter.LooterAdapter;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.name.NameAdapter;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.owner.OwnerAdapter;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.protection.ProtectionAdapter;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.quantity.QuantityAdapter;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.uuid.UniqueIdAdapter;

import java.util.*;
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


	public AdapterRegistry(final AdapterContextContainer ctx)
	{
		// Register adapters in preferred priority order
		register(new DisplayNameAdapter(ctx));
		register(new DurationAdapter());
		register(new ExpirationAdapter());
		register(new InstantAdapter());
		register(new NameAdapter());
		register(new OwnerAdapter());
		register(new KillerAdapter());
		register(new LooterAdapter());
		register(new LocationAdapter());
		register(new QuantityAdapter());
		register(new ProtectionAdapter());
		register(new UniqueIdAdapter());
	}


	public void register(final Adapter adapter)
	{
		validate(adapter, Objects::isNull, throwing(PARAMETER_NULL, ADAPTER));
		adapters.add(adapter);
	}


	/**
	 * Returns all adapters that support the given value (based on instanceof checks inside each adapter).
	 */
	public Stream<Adapter> getMatchingAdapters(final Object object)
	{
		return (object != null)
				? adapters.stream().filter(adapter -> adapter.supports(object))
				: Stream.empty();
	}

}
