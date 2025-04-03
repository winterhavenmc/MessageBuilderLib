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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;


/**
 * Registry of adapters provides cache of built-in and plugin registered adapters.
 * Lazy loading of adapters is provided by computeIfAbsent method.
 */
public class AdapterRegistry
{
    private final Map<Class<?>, Adapter> adapterCache = new ConcurrentHashMap<>();
    private final Map<Class<?>, Supplier<Adapter>> builtInAdapters = Map.of(
            DisplayNameable.class, DisplayNameAdapter::new,
            Locatable.class, LocationAdapter::new,
            Nameable.class, NameAdapter::new,
            Quantifiable.class, QuantityAdapter::new,
            Identifiable.class, UniqueIdAdapter::new
    );


    public Adapter getAdapter(final Class<?> type)
    {
        return adapterCache.computeIfAbsent(type, key -> {
            Supplier<Adapter> supplier = builtInAdapters.get(key);
            return (supplier != null) ? supplier.get() : null;
        });
    }


    public void registerAdapter(final Class<?> type, final Adapter adapter)
    {
        adapterCache.put(type, adapter);
    }

}
