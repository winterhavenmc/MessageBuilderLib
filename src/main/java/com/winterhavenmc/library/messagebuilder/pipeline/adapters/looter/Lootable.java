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

package com.winterhavenmc.library.messagebuilder.pipeline.adapters.looter;

import com.winterhavenmc.library.messagebuilder.keys.MacroKey;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.AdapterContextContainer;
import com.winterhavenmc.library.messagebuilder.pipeline.maps.MacroStringMap;

import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Entity;

import java.util.Optional;
import java.util.function.Predicate;

import static com.winterhavenmc.library.messagebuilder.pipeline.adapters.Adapter.BuiltIn.LOOTER;
import static com.winterhavenmc.library.messagebuilder.pipeline.adapters.Adapter.UNKNOWN_VALUE;


/**
 * Represents an object that exposes looter or claimant information for permission or access control.
 *
 * <p>This interface is intended for scenarios where an entity (typically a player) has
 * permission or claim to retrieve or interact with an object, such as a container or reward drop.
 * It does not imply that looting has already occurred, only that the looter is authorized to do so.
 *
 * <p>The looter is expected to be resolvable to a name-compatible {@link AnimalTamer}, such as a
 * {@link org.bukkit.OfflinePlayer OfflinePlayer} or {@link org.bukkit.entity.Player Player}.
 *
 * <p>Macro placeholder resolution supports usage of keys like {@code {OBJECT.LOOTER}}.
 */
@FunctionalInterface
public interface Lootable
{
	/**
	 * Returns the looter or claimant entity who has permission to access the lootable object.
	 *
	 * @return the looter as an {@link AnimalTamer}, or {@code null} if unknown
	 */
	Entity getLooter();


	/**
	 * Extracts the looter's name as a macro replacement field.
	 *
	 * @param baseKey the macro key root
	 * @param ctx the adapter context with formatting support
	 * @return a {@link MacroStringMap} containing the looter key and value if resolvable
	 */
	default MacroStringMap extractLooter(final MacroKey baseKey, final AdapterContextContainer ctx)
	{
		return baseKey.append(LOOTER)
				.map(macroKey -> new MacroStringMap()
				.with(macroKey, formatLooter(getLooter()).orElse(UNKNOWN_VALUE)))
				.orElseGet(MacroStringMap::empty);
	}


	/**
	 * Predicate to validate whether a looter has a valid, non-blank name.
	 */
	Predicate<Entity> VALID_LOOTER = looter -> looter != null
			&& !looter.getName().isBlank();


	/**
	 * Formats the looter name for placeholder substitution.
	 *
	 * @param looter the entity who has permission to loot the object
	 * @return an {@code Optional<String>} containing the name if valid, otherwise empty
	 */
	static Optional<String> formatLooter(final Entity looter)
	{
		return (VALID_LOOTER.test(looter))
				? Optional.of(looter.getName())
				: Optional.empty();
	}

}
