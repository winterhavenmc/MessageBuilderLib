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

package com.winterhavenmc.library.messagebuilder.pipeline.adapters.name;


import com.winterhavenmc.library.messagebuilder.keys.MacroKey;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.AdapterContextContainer;
import com.winterhavenmc.library.messagebuilder.pipeline.containers.MacroStringMap;

import java.util.Optional;
import java.util.function.Predicate;

import static com.winterhavenmc.library.messagebuilder.pipeline.adapters.Adapter.BuiltIn.NAME;
import static com.winterhavenmc.library.messagebuilder.pipeline.adapters.Adapter.UNKNOWN_VALUE;

/**
 * An interface that describes objects that have a {@code getName()}
 * method that returns a valid name as a {@code String}
 */
@FunctionalInterface
public interface Nameable
{
	String getName();


	/**
	 * Returns a new MacroStringMap containing all fields extracted from a Durationable type
	 *
	 * @param baseKey the top level key for the fields of this object
	 * @return a MacroStringMap containing the fields extracted for objects of Durationable type
	 */
	default MacroStringMap extractName(final MacroKey baseKey, final AdapterContextContainer ctx)
	{
		return baseKey.append(NAME)
				.map(macroKey -> new MacroStringMap()
				.with(macroKey, formatName(this.getName()).orElse(UNKNOWN_VALUE)))
				.orElseGet(MacroStringMap::empty);
	}


	Predicate<String> VALID_NAME = name -> name != null && !name.isBlank();


	/**
	 * Returns a formatted string of a name
	 *
	 * @return {@code Optional<String>} containing a formatted String of a name,
	 * or an empty Optional if not found
	 */
	static Optional<String> formatName(final String name)
	{
		return (VALID_NAME.test(name))
				? Optional.of(name)
				: Optional.empty();
	}

}
