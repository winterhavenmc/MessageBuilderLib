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
import com.winterhavenmc.library.messagebuilder.pipeline.maps.MacroStringMap;

import java.util.Optional;
import java.util.function.Predicate;

import static com.winterhavenmc.library.messagebuilder.pipeline.adapters.Adapter.BuiltIn.NAME;
import static com.winterhavenmc.library.messagebuilder.pipeline.adapters.Adapter.UNKNOWN_VALUE;

/**
 * A functional interface representing an object with a name field, commonly used in
 * {@link com.winterhavenmc.library.messagebuilder.pipeline.adapters.Adapter Adapter}
 * implementations that expose a {@code getName()} method.
 *
 * <p>This interface includes utility methods for validating, formatting, and extracting
 * the name field as part of macro resolution in the message pipeline.
 *
 * <p>It is designed for use with {@code Adapter} classes to wrap Bukkit objects or
 * domain models that provide a display name or identifier suitable for macro replacement.
 *
 * @see com.winterhavenmc.library.messagebuilder.pipeline.adapters.name.NameAdapter NameAdapter
 * @see com.winterhavenmc.library.messagebuilder.pipeline.maps.MacroStringMap MacroStringMap
 */
@FunctionalInterface
public interface Nameable
{
	/**
	 * Returns the name associated with the implementing object.
	 *
	 * @return the name of the object, typically used for macro substitution
	 */
	String getName();


	/**
	 * Extracts the name field into a
	 * {@link com.winterhavenmc.library.messagebuilder.pipeline.maps.MacroStringMap},
	 * using the provided base key to construct a dot-notated macro key (e.g., {@code OBJECT.NAME}).
	 *
	 * <p>The name is formatted and validated before being inserted into the map.
	 * If the base key cannot be extended (e.g., due to a parse error), an empty map is returned.
	 *
	 * @param baseKey the top-level macro key to use as the namespace for the field
	 * @param ctx an unused adapter context container (reserved for future use)
	 * @return a map containing the extracted name value, or an empty map if the name is invalid
	 */
	default MacroStringMap extractName(final MacroKey baseKey, final AdapterContextContainer ctx)
	{
		return baseKey.append(NAME)
				.map(macroKey -> new MacroStringMap()
				.with(macroKey, formatName(this.getName()).orElse(UNKNOWN_VALUE)))
				.orElseGet(MacroStringMap::empty);
	}


	/**
	 * A predicate that determines whether a given name string is valid.
	 *
	 * <p>A name is considered valid if it is non-{@code null} and not blank
	 * (i.e., not composed entirely of whitespace).
	 *
	 * <p>This predicate is primarily used by {@link #formatName(String)} to decide whether
	 * a name should be included in macro substitution output.
	 */
	Predicate<String> VALID_NAME = name -> name != null && !name.isBlank();


	/**
	 * Formats the given name string, returning it as an {@link Optional} if valid.
	 * Names are considered valid if they are non-null and not blank.
	 *
	 * @param name the raw name string to validate and format
	 * @return an optional containing the formatted name, or empty if invalid
	 */
	static Optional<String> formatName(final String name)
	{
		return (VALID_NAME.test(name))
				? Optional.of(name)
				: Optional.empty();
	}

}
