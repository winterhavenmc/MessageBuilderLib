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

package com.winterhavenmc.library.messagebuilder.pipeline.adapters.killer;

import com.winterhavenmc.library.messagebuilder.keys.MacroKey;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.AdapterContextContainer;
import com.winterhavenmc.library.messagebuilder.pipeline.maps.MacroStringMap;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Entity;

import java.util.Optional;
import java.util.function.Predicate;

import static com.winterhavenmc.library.messagebuilder.pipeline.adapters.Adapter.BuiltIn.KILLER;
import static com.winterhavenmc.library.messagebuilder.pipeline.adapters.Adapter.UNKNOWN_VALUE;


/**
 * An interface for objects that provide information about the entity that killed them.
 *
 * <p>This abstraction is used to populate the {@code {OBJECT.KILLER}} macro field in messages.
 * It allows server operators to define custom or built-in behaviors for logging or describing
 * the cause of death in messages.
 *
 * <p>The most common implementation source is a
 * {@link org.bukkit.entity.LivingEntity LivingEntity},
 * whose {@code getKiller()} method returns the killing {@link org.bukkit.entity.Entity Entity}.
 * However, plugin-defined objects may implement this interface to expose similar semantics.
 */
@FunctionalInterface
public interface Killable
{
	/**
	 * Returns the killer of this object, typically a {@link org.bukkit.entity.Player Player}.
	 *
	 * @return the killer as an {@link Entity}, or {@code null} if not available
	 */
	Entity getKiller();


	/**
	 * Extracts a macro string map with the killer's name, using the given base key and context.
	 *
	 * @param baseKey the macro key that identifies the root placeholder
	 * @param ctx the adapter context container with formatting tools
	 * @return a {@link MacroStringMap} containing the killer field, or an empty map if not resolvable
	 */
	default MacroStringMap extractKiller(final MacroKey baseKey, final AdapterContextContainer ctx)
	{
		return baseKey.append(KILLER)
				.map(macroKey -> new MacroStringMap()
				.with(macroKey, formatKiller(this.getKiller()).orElse(UNKNOWN_VALUE)))
				.orElseGet(MacroStringMap::empty);
	}


	/**
	 * Predicate that evaluates whether a killer is considered valid and non-blank.
	 */
	Predicate<Entity> VALID_KILLER = killer -> killer != null
			&& !killer.getName().isBlank();


	/**
	 * Formats the killer name for use in placeholder replacement.
	 *
	 * @param killer the entity that performed the kill
	 * @return an {@code Optional<String>} containing the killer's name, if valid
	 */
	static Optional<String> formatKiller(final Entity killer)
	{
		return (VALID_KILLER.test(killer))
				? Optional.of(killer.getName())
				: Optional.empty();
	}

}
