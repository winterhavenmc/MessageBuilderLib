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

package com.winterhavenmc.library.messagebuilder.adapters.pipeline.extractors.looter;

import com.winterhavenmc.library.messagebuilder.core.pipeline.adapters.Adapter;
import com.winterhavenmc.library.messagebuilder.core.pipeline.adapters.looter.Lootable;
import org.bukkit.loot.LootContext;

import java.util.Optional;


/**
 * Adapter that converts supported objects into the {@link Lootable} interface for macro resolution.
 *
 * <p>This adapter is designed to identify the entity who is entitled to loot a container or object,
 * not necessarily one who has already looted it. It is useful for generating access-related
 * messages such as "Only {OBJECT.LOOTER} may open this chest."
 *
 * <p>Supports:
 * <ul>
 *   <li>Objects explicitly implementing {@link Lootable}</li>
 *   <li>{@link LootContext} via its {@code getKiller()} method</li>
 * </ul>
 */
public class LooterAdapter implements Adapter
{
	/**
	 * Attempts to adapt an object into a {@link Lootable}.
	 *
	 * @param obj the object to adapt
	 * @return an {@code Optional<Lootable>} if supported, or empty otherwise
	 */
	public Optional<Lootable> adapt(final Object obj)
	{
		return switch (obj)
		{
			case Lootable lootable -> Optional.of(lootable);
			case LootContext lootContext -> Optional.of(lootContext::getKiller);
			case null, default -> Optional.empty();
		};
	}

}
