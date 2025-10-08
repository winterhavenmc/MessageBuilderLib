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

package com.winterhavenmc.library.messagebuilder.adapters.pipeline.extractors.killer;

import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.extractors.Adapter;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.extractors.killer.Killable;
import org.bukkit.entity.LivingEntity;
import org.bukkit.loot.LootContext;

import java.util.Optional;


/**
 * Adapter that maps supported objects to the {@link Killable} interface for macro extraction.
 *
 * <p>This adapter enables the use of the {@code {OBJECT.KILLER}} macro by wrapping objects
 * that can report who killed them. This includes:
 * <ul>
 *   <li>Custom plugin-defined objects implementing {@link Killable}</li>
 *   <li>{@link LivingEntity} types from Bukkit, where {@code getKiller()} is available</li>
 * </ul>
 *
 * <p>This adapter is commonly used to generate messages about deaths, such as kill feeds
 * or death chest logs.
 */
public class KillerAdapter implements Adapter
{
	/**
	 * Attempts to adapt an object to the {@link Killable} interface.
	 *
	 * @param obj the object to adapt
	 * @return an {@code Optional<Killable>} if supported, otherwise empty
	 */
	public Optional<Killable> adapt(final Object obj)
	{
		return switch (obj)
		{
			case Killable killable -> Optional.of(killable);
			case LivingEntity livingEntity -> Optional.of(livingEntity::getKiller);
			case LootContext lootContext -> Optional.of(lootContext::getKiller);
			case null, default -> Optional.empty();
		};
	}

}
