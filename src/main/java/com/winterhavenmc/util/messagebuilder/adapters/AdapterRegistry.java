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

package com.winterhavenmc.util.messagebuilder.adapters;

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

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static com.winterhavenmc.util.messagebuilder.validation.ErrorMessageKey.PARAMETER_NULL;
import static com.winterhavenmc.util.messagebuilder.validation.Parameter.*;
import static com.winterhavenmc.util.messagebuilder.validation.ValidationHandler.throwing;
import static com.winterhavenmc.util.messagebuilder.validation.Validator.validate;


/**
 * Registry of adapters provides cache of built-in and plugin registered adapters.
 * Lazy loading of adapters is provided by computeIfAbsent method.
 */
public class AdapterRegistry
{
	private final Map<Class<?>, Supplier<? extends Adapter<?>>> ADAPTER_MAP = new LinkedHashMap<>();
    private final Map<Class<?>, Adapter<?>> ADAPTER_CACHE = new ConcurrentHashMap<>();


	/**
	 * Class constructor registers all built-in adapters
	 */
	public AdapterRegistry()
	{
		registerAdapter(DisplayNameable.class, DisplayNameAdapter::new);
		registerAdapter(Nameable.class, NameAdapter::new);
		registerAdapter(Locatable.class, LocationAdapter::new);
		registerAdapter(Identifiable.class, UniqueIdAdapter::new);
		registerAdapter(Quantifiable.class, QuantityAdapter::new);
	}


	/**
	 * Register an adapter and store in the backing map
	 *
	 * @param type the class of the adaptable interface
	 * @param supplier a supplier containing the constructor for the adapter
	 * @param <T> the adapter type
	 */
	public <T> void registerAdapter(final Class<T> type, final Supplier<Adapter<T>> supplier)
	{
		validate(type, Objects::isNull, throwing(PARAMETER_NULL, TYPE));
		validate(supplier, Objects::isNull, throwing(PARAMETER_NULL, ADAPTER));

		ADAPTER_MAP.put(type, supplier);
	}


	/**
	 * Retrieve an adapter from the cache, or instantiate and place a new instance in the cache if not present
	 *
	 * @param type the class of the adapter
	 * @return the adapter retrieved from the map, or null if no adapter present for type
	 * @param <T> the type of the adapter
	 */
	@SuppressWarnings("unchecked")
	public <T> Adapter<T> getAdapter(Class<T> type)
	{
		validate(type, Objects::isNull, throwing(PARAMETER_NULL, TYPE));

		return (Adapter<T>) ADAPTER_CACHE.computeIfAbsent(type, t -> {
			Supplier<? extends Adapter<?>> supplier = ADAPTER_MAP.get(t);
			return (supplier != null)
					? supplier.get()
					: null;
		});
	}


	/**
	 * Retrieve a stream of matching adapters for value type
	 *
	 * @param value the value to match adapter types
	 * @return stream of matching adapters
	 * @param <T> the class type of the value
	 */
	@SuppressWarnings("unchecked")
	public <T> Stream<Adapter<T>> matchingAdapters(T value)
	{
		return (value == null)
				? Stream.empty()
				: ADAPTER_MAP.keySet().stream()
						.filter(supplier -> supplier.isAssignableFrom(value.getClass()))
						.map(supplier -> (Adapter<T>) getAdapter(supplier))
						.filter(Objects::nonNull);
	}

}
