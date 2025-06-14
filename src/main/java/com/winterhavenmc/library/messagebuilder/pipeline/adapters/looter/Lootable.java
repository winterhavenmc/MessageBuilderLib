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
import com.winterhavenmc.library.messagebuilder.pipeline.containers.MacroStringMap;

import org.bukkit.entity.AnimalTamer;

import java.util.Optional;
import java.util.function.Predicate;

import static com.winterhavenmc.library.messagebuilder.pipeline.adapters.Adapter.BuiltIn.LOOTER;
import static com.winterhavenmc.library.messagebuilder.pipeline.adapters.Adapter.UNKNOWN_VALUE;


@FunctionalInterface
public interface Lootable
{
	AnimalTamer getLooter();


	/**
	 * Returns a new MacroStringMap containing all fields extracted from a Lootable type
	 *
	 * @param baseKey the top level key for the fields of this object
	 * @return a MacroStringMap containing the fields extracted for objects of Lootable type
	 */
	default MacroStringMap extractLooter(final MacroKey baseKey, final AdapterContextContainer ctx)
	{
		return baseKey.append(LOOTER)
				.map(macroKey -> new MacroStringMap()
				.with(macroKey, formatLooter(getLooter()).orElse(UNKNOWN_VALUE)))
				.orElseGet(MacroStringMap::empty);
	}


	/**
	 * Predicate to test against null or blank looter name
	 */
	Predicate<AnimalTamer> VALID_LOOTER = looter -> looter != null
			&& looter.getName() != null
			&& !looter.getName().isBlank();

	/**
	 * Returns a formatted string of a looter's name
	 *
	 * @param looter an OfflinePlayer used as a placeholder
	 * @return {@code Optional<String>} containing a formatted String of a looter's name,
	 * or an empty Optional if not found
	 */
	static Optional<String> formatLooter(final AnimalTamer looter)
	{
		return (VALID_LOOTER.test(looter))
				? Optional.ofNullable(looter.getName())
				: Optional.empty();
	}

}
