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

package com.winterhavenmc.library.messagebuilder.pipeline.adapters.uuid;

import com.winterhavenmc.library.messagebuilder.keys.MacroKey;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.Adapter;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.AdapterContextContainer;
import com.winterhavenmc.library.messagebuilder.pipeline.maps.MacroStringMap;

import java.util.Optional;
import java.util.UUID;

import static com.winterhavenmc.library.messagebuilder.pipeline.adapters.Adapter.UNKNOWN_VALUE;

/**
 * An interface that describes objects
 * that have a {@code getUniqueId()}
 * method that returns a valid UUID.
 */
@FunctionalInterface
public interface Identifiable
{
	UUID getUniqueId();


	/**
	 * Returns a new MacroStringMap containing all fields extracted from an Identifiable type
	 *
	 * @param baseKey      the top level key for the fields of this object
	 * @return a MacroStringMap containing the fields extracted for objects of Identifiable type
	 */
	default MacroStringMap extractUid(final MacroKey baseKey, final AdapterContextContainer ctx)
	{
		return baseKey.append(Adapter.BuiltIn.UUID)
				.map(macroKey -> new MacroStringMap()
				.with(macroKey, formatUid(this.getUniqueId()).orElse(UNKNOWN_VALUE)))
				.orElseGet(MacroStringMap::empty);
	}


	/**
	 * Default method that returns a formatted string of a uuid
	 *
	 * @return {@code Optional<String>} containing a formatted String of a uuid,
	 * or an empty Optional if not found
	 */
	static Optional<String> formatUid(final UUID uniqueId)
	{
		return (uniqueId != null)
				? Optional.of(String.valueOf(uniqueId))
				: Optional.empty();
	}

}
