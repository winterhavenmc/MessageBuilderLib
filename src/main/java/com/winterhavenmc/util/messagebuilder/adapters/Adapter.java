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

import java.util.Optional;
import java.util.function.Supplier;


public interface Adapter<T>
{
    Optional<?> adapt(Object obj);
    Class<T> getInterface();


    enum BuiltIn
    {
        NAME(Nameable.class, NameAdapter::new),
        DISPLAY_NAME(DisplayNameable.class, DisplayNameAdapter::new),
        UUID(Identifiable.class, UniqueIdAdapter::new),
        LOCATION(Locatable.class, LocationAdapter::new),
        QUANTITY(Quantifiable.class, QuantityAdapter::new);

        private final Class<?> type;
        private final Supplier<? extends Adapter<?>> supplier;


        <T> BuiltIn(Class<T> type, Supplier<? extends Adapter<T>> supplier)
        {
            this.type = type;
            this.supplier = supplier;
        }

        Class<?> getType()
        {
            return this.type;
        }

        Adapter<?> create()
        {
            return supplier.get();
        }
    }

}
