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

package com.winterhavenmc.library.messagebuilder.core.pipeline.adapters.pluralname;

import com.winterhavenmc.library.messagebuilder.core.context.AdapterCtx;
import com.winterhavenmc.library.messagebuilder.core.pipeline.adapters.Adapter;
import org.bukkit.inventory.ItemStack;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.resolvers.itemname.ItemPluralNameResolver;

import java.util.Optional;


/**
 * Adapter implementation for extracting display names from supported object types.
 *
 * <p>This adapter converts objects into {@link PluralNameable} instances to support macro
 * replacement using the {@code {OBJECT.DISPLAY_NAME}} placeholder.
 *
 * <p>Supported types include:
 * <ul>
 *     <li>Objects implementing {@link PluralNameable}</li>
 *     <li>{@link ItemStack} — mapped to {@link PluralNameable#getPluralName()}</li>
 *     {@link ItemPluralNameResolver ItemPluralNameResolver}</li>
 * </ul>
 *
 * @see PluralNameable
 * @see Adapter
 */
public class PluralNameAdapter implements Adapter
{
	private final AdapterCtx ctx;


	/**
	 * Constructs a new {@code PluralNameAdapter} with the given context container.
	 *
	 * @param ctx the adapter context providing services such as world name resolution
	 */
	public PluralNameAdapter(final AdapterCtx ctx)
	{
		this.ctx = ctx;
	}


	/**
	 * Attempts to adapt the given object to a {@link PluralNameable} instance.
	 *
	 * @param obj the object to adapt
	 * @return an optional {@code PluralNameable} if supported, or empty otherwise
	 */
	@Override
	public Optional<PluralNameable> adapt(final Object obj)
	{
		return switch (obj)
		{
			case PluralNameable pluralNameable -> Optional.of(pluralNameable);
			case ItemStack itemStack -> Optional.of(() -> ctx.itemPluralNameResolver().resolve(itemStack));
			case null, default -> Optional.empty();
		};
	}

}
