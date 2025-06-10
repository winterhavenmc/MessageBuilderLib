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
import com.winterhavenmc.library.messagebuilder.pipeline.containers.MacroStringMap;
import org.bukkit.entity.AnimalTamer;

import java.util.Optional;
import java.util.function.Predicate;

import static com.winterhavenmc.library.messagebuilder.pipeline.adapters.Adapter.BuiltIn.KILLER;
import static com.winterhavenmc.library.messagebuilder.pipeline.adapters.Adapter.UNKNOWN_VALUE;


@FunctionalInterface
public interface Killable
{
	AnimalTamer getKiller();


	/**
	 * Returns a new MacroStringMap containing all fields extracted from a Lootable type
	 *
	 * @param baseKey the top level key for the fields of this object
	 * @return a MacroStringMap containing the fields extracted for objects of Lootable type
	 */
	default MacroStringMap extractKiller(final MacroKey baseKey, final AdapterContextContainer ctx)
	{
		return baseKey.append(KILLER)
				.map(macroKey -> new MacroStringMap()
				.with(macroKey, formatKiller(this.getKiller()).orElse(UNKNOWN_VALUE)))
				.orElseGet(MacroStringMap::empty);
	}


	Predicate<AnimalTamer> VALID_KILLER_NAME = killer -> killer != null && killer.getName() != null && !killer.getName().isBlank();


	/**
	 * Returns a formatted string of a killer's name
	 *
	 * @return {@code Optional<String>} containing a formatted String of a killer's name,
	 * or an empty Optional if not found
	 */
	static Optional<String> formatKiller(final AnimalTamer killer)
	{
		return (VALID_KILLER_NAME.test(killer))
				? Optional.ofNullable(killer.getName())
				: Optional.empty();
	}

}
