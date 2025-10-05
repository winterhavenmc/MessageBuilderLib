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

package com.winterhavenmc.library.messagebuilder.core.pipeline.adapters.owner;

import com.winterhavenmc.library.messagebuilder.core.pipeline.adapters.Adapter;
import org.bukkit.entity.Tameable;

import java.util.Optional;


/**
 * Adapter that extracts ownership information from objects that either implement {@link Ownable}
 * or are {@link Tameable} Bukkit entities.
 *
 * <p>This adapter enables support for the {@code {OBJECT.OWNER}} macro by wrapping objects
 * that expose a semantic concept of ownership. It checks for two sources:
 * <ul>
 *   <li>Objects that directly implement {@link Ownable} (e.g., plugin-defined types such as a "DeathChest")</li>
 *   <li>{@link Tameable} entities, where ownership is derived from {@code getOwner()}</li>
 * </ul>
 *
 * <p>Use cases include pet ownership, data-bound entity or item ownership, and permission-based
 * structures such as protected regions or storage blocks. Because {@link Ownable} defines ownership
 * via an {@link org.bukkit.entity.AnimalTamer}, it supports {@link org.bukkit.OfflinePlayer} references,
 * allowing message replacement even for offline owners.
 */
public class OwnerAdapter implements Adapter
{
	/**
	 * Attempts to adapt the given object to the {@link Ownable} interface, either directly or by wrapping
	 * supported Bukkit types.
	 *
	 * @param obj the object to adapt
	 * @return an {@code Optional<Ownable>} if the object is supported, otherwise {@code Optional.empty()}
	 */
	@Override
	public Optional<Ownable> adapt(final Object obj)
	{
		return switch (obj)
		{
			case Ownable ownable -> Optional.of(ownable);
			case Tameable tameable -> Optional.of(tameable::getOwner);
			case null, default -> Optional.empty();
		};
	}

}
