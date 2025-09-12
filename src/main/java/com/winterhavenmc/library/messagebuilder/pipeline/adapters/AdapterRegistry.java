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
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.pluralname.PluralNameAdapter;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.protection.ProtectionAdapter;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.quantity.QuantityAdapter;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.uuid.UniqueIdAdapter;

import java.util.*;
import java.util.stream.Stream;

import static com.winterhavenmc.library.messagebuilder.validation.ErrorMessageKey.PARAMETER_NULL;
import static com.winterhavenmc.library.messagebuilder.validation.Parameter.ADAPTER;
import static com.winterhavenmc.library.messagebuilder.validation.Validator.throwing;
import static com.winterhavenmc.library.messagebuilder.validation.Validator.validate;


/**
 * Maintains an ordered registry of all {@link Adapter} instances available to the macro resolution pipeline.
 *
 * <p>This registry is responsible for:
 * <ul>
 *   <li>Registering built-in adapters at construction time, in a defined priority order</li>
 *   <li>Allowing additional plugin-defined adapters to be registered at runtime</li>
 *   <li>Providing a filtered stream of adapters that support a given object type</li>
 * </ul>
 *
 * <p>Adapters are evaluated in the order they are registered, ensuring predictable
 * precedence when multiple adapters populate the same macro keys. The first adapter to
 * return a value for a given string wins, and later adapters cannot overwrite it.
 *
 * <p>This allows plugin developers and library consumers to control the resolution order by
 * selectively registering their own adapters before or after the built-in set.
 *
 * @see Adapter
 * @see AdapterContextContainer
 * @see com.winterhavenmc.library.messagebuilder.pipeline.resolvers.CompositeResolver CompositeResolver
 */
public class AdapterRegistry
{
	private final List<Adapter> adapters = new ArrayList<>();


	/**
	 * Constructs an {@code AdapterRegistry} and registers all built-in adapters in preferred priority order.
	 *
	 * @param ctx a context container providing shared utilities to each adapter
	 */
	public AdapterRegistry(final AdapterContextContainer ctx)
	{
		// Register adapters in preferred priority order
		register(new PluralNameAdapter(ctx));
		register(new DisplayNameAdapter(ctx));
		register(new NameAdapter(ctx));
		register(new DurationAdapter());
		register(new ExpirationAdapter());
		register(new InstantAdapter());
		register(new OwnerAdapter());
		register(new KillerAdapter());
		register(new LooterAdapter());
		register(new LocationAdapter());
		register(new QuantityAdapter());
		register(new ProtectionAdapter());
		register(new UniqueIdAdapter());
	}


	/**
	 * Registers a new {@link Adapter} into the registry.
	 *
	 * <p>Adapters are added to the end of the internal list and evaluated in the order
	 * they were registered. It is the caller's responsibility to ensure adapters are registered
	 * in a preferred resolution priority.
	 *
	 * @param adapter the adapter to register
	 * @throws NullPointerException if the adapter is {@code null}
	 */
	public final void register(final Adapter adapter)
	{
		validate(adapter, Objects::isNull, throwing(PARAMETER_NULL, ADAPTER));
		adapters.add(adapter);
	}


	/**
	 * Returns a stream of all registered {@link Adapter} instances that support the given object.
	 *
	 * <p>Each adapter is evaluated in order, and only those for which
	 * {@link Adapter#supports(Object)} returns {@code true} are included in the result.
	 *
	 * @param object the object to test for adapter support
	 * @return a stream of supporting adapters, or an empty stream if the object is {@code null}
	 */
	public Stream<Adapter> getMatchingAdapters(final Object object)
	{
		return (object != null)
				? adapters.stream().filter(adapter -> adapter.supports(object))
				: Stream.empty();
	}

}
