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

package com.winterhavenmc.library.messagebuilder.adapters.pipeline.accessors;

import com.winterhavenmc.library.messagebuilder.adapters.pipeline.accessors.identity.UniqueIdAdapter;
import com.winterhavenmc.library.messagebuilder.adapters.pipeline.accessors.quantity.BukkitQuantityAccessor;
import com.winterhavenmc.library.messagebuilder.core.context.AdapterCtx;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.Accessor;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.AccessorRegistry;
import com.winterhavenmc.library.messagebuilder.adapters.pipeline.accessors.displayname.DisplayNameAdapter;
import com.winterhavenmc.library.messagebuilder.adapters.pipeline.accessors.duration.DurationAdapter;
import com.winterhavenmc.library.messagebuilder.adapters.pipeline.accessors.expiration.ExpirationAdapter;
import com.winterhavenmc.library.messagebuilder.adapters.pipeline.accessors.instant.InstantAdapter;
import com.winterhavenmc.library.messagebuilder.adapters.pipeline.accessors.location.LocationAdapter;
import com.winterhavenmc.library.messagebuilder.adapters.pipeline.accessors.killer.KillerAdapter;
import com.winterhavenmc.library.messagebuilder.adapters.pipeline.accessors.looter.LooterAdapter;
import com.winterhavenmc.library.messagebuilder.adapters.pipeline.accessors.name.NameAdapter;
import com.winterhavenmc.library.messagebuilder.adapters.pipeline.accessors.owner.OwnerAdapter;
import com.winterhavenmc.library.messagebuilder.adapters.pipeline.accessors.pluralname.PluralNameAdapter;
import com.winterhavenmc.library.messagebuilder.adapters.pipeline.accessors.protection.ProtectionAdapter;
import com.winterhavenmc.library.messagebuilder.adapters.pipeline.accessors.url.UrlAdapter;
import com.winterhavenmc.library.messagebuilder.adapters.pipeline.accessors.version.VersionAdapter;

import java.util.*;
import java.util.stream.Stream;

import static com.winterhavenmc.library.messagebuilder.models.validation.ErrorMessageKey.PARAMETER_NULL;
import static com.winterhavenmc.library.messagebuilder.models.validation.Parameter.ADAPTER;
import static com.winterhavenmc.library.messagebuilder.models.validation.Validator.throwing;
import static com.winterhavenmc.library.messagebuilder.models.validation.Validator.validate;


/**
 * Maintains an ordered registry of all {@link Accessor} instances available to the macro resolution pipeline.
 *
 * <p>This registry is responsible for:
 * <ul>
 *   <li>Registering built-in accessors at construction time, in a defined priority order</li>
 *   <li>Allowing additional plugin-defined accessors to be registered at runtime</li>
 *   <li>Providing a filtered stream of accessors that support a given object type</li>
 * </ul>
 *
 * <p>Adapters are evaluated in the order they are registered, ensuring predictable
 * precedence when multiple accessors populate the same macro keys. The first adapter to
 * return a value for a given string wins, and later accessors cannot overwrite it.
 *
 * <p>This allows plugin developers and library consumers to control the resolution order by
 * selectively registering their own accessors before or after the built-in set.
 *
 * @see Accessor
 * @see AdapterCtx
 */
public class FieldAccessorRegistry implements AccessorRegistry
{
	private final List<Accessor> accessors = new ArrayList<>();


	/**
	 * Constructs an {@code AccessorRegistry} and registers all built-in accessors in preferred priority order.
	 *
	 * @param ctx a context container providing shared utilities to each adapter
	 */
	public FieldAccessorRegistry(final AdapterCtx ctx)
	{
		// Register accessors in preferred priority order
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
		register(new BukkitQuantityAccessor());
		register(new ProtectionAdapter());
		register(new UniqueIdAdapter());
		register(new UrlAdapter(ctx));
		register(new VersionAdapter(ctx));
	}


	/**
	 * Registers a new {@link Accessor} into the registry.
	 *
	 * <p>Adapters are added to the end of the internal list and evaluated in the order
	 * they were registered. It is the caller's responsibility to ensure accessors are registered
	 * in a preferred resolution priority.
	 *
	 * @param accessor the accessor to register
	 * @throws NullPointerException if the accessor is {@code null}
	 */
	@Override
	public final void register(final Accessor accessor)
	{
		validate(accessor, Objects::isNull, throwing(PARAMETER_NULL, ADAPTER));
		accessors.add(accessor);
	}


	/**
	 * Returns a stream of all registered {@link Accessor} instances that support the given object.
	 *
	 * <p>Each adapter is evaluated in order, and only those for which
	 * {@link Accessor#supports(Object)} returns {@code true} are included in the result.
	 *
	 * @param object the object to test for adapter support
	 * @return a stream of supporting accessors, or an empty stream if the object is {@code null}
	 */
	@Override
	public Stream<Accessor> getMatchingAdapters(final Object object)
	{
		return (object != null)
				? accessors.stream().filter(adapter -> adapter.supports(object))
				: Stream.empty();
	}

}
