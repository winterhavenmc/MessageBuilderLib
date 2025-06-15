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

package com.winterhavenmc.library.messagebuilder.pipeline.adapters.owner;

import com.winterhavenmc.library.messagebuilder.keys.MacroKey;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.AdapterContextContainer;
import com.winterhavenmc.library.messagebuilder.pipeline.containers.MacroStringMap;
import org.bukkit.entity.AnimalTamer;

import java.util.Optional;
import java.util.function.Predicate;

import static com.winterhavenmc.library.messagebuilder.pipeline.adapters.Adapter.BuiltIn.OWNER;
import static com.winterhavenmc.library.messagebuilder.pipeline.adapters.Adapter.UNKNOWN_VALUE;


@FunctionalInterface
public interface Ownable
{
	AnimalTamer getOwner();

	/**
	 * Returns a new MacroStringMap containing all fields extracted from an Ownable type
	 *
	 * @param baseKey the top level key for the fields of this object
	 * @return a MacroStringMap containing the fields extracted for objects of Ownable type
	 */
	default MacroStringMap extractOwner(final MacroKey baseKey, final AdapterContextContainer ctx)
	{
		return baseKey.append(OWNER)
				.map(macroKey -> new MacroStringMap()
				.with(macroKey, formatOwner(this.getOwner()).orElse(UNKNOWN_VALUE)))
				.orElseGet(MacroStringMap::empty);
	}


	/**
	 * Predicate to test against null or blank name field
	 */
	Predicate<AnimalTamer> VALID_OWNER_NAME = animalTamer -> animalTamer != null
			&& animalTamer.getName() != null
			&& !animalTamer.getName().isBlank();


	/**
	 * Default method that returns a formatted string of an owner's name
	 *
	 * @return {@code Optional<String>} containing a formatted String of an owner's name,
	 * or an empty Optional if not found
	 */
	static Optional<String> formatOwner(final AnimalTamer owner)
	{
		return (VALID_OWNER_NAME.test(owner))
				? Optional.ofNullable(owner.getName())
				: Optional.empty();
	}

}
