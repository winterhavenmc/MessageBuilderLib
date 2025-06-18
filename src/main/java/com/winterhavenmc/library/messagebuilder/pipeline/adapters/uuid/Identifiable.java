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
 * Represents an object that exposes a unique {@link UUID}.
 *
 * <p>Implementations of this interface contribute a {@code {OBJECT.UUID}} macro to the
 * {@link com.winterhavenmc.library.messagebuilder.pipeline.maps.MacroStringMap}, allowing the UUID
 * of the object to be used in dynamically composed messages.</p>
 *
 * <p>This is especially useful for identifying objects such as players, entities, or worlds,
 * where the UUID serves as a persistent and unique reference.</p>
 */
@FunctionalInterface
public interface Identifiable
{
	/**
	 * Returns the unique ID for this object.
	 *
	 * @return a UUID that uniquely identifies this object
	 */
	UUID getUniqueId();


	/**
	 * Extracts a macro string map containing the UUID field.
	 *
	 * @param baseKey the top-level macro key
	 * @param ctx     the adapter context
	 * @return a {@code MacroStringMap} with the UUID value formatted as a string
	 */
	default MacroStringMap extractUid(final MacroKey baseKey, final AdapterContextContainer ctx)
	{
		return baseKey.append(Adapter.BuiltIn.UUID)
				.map(macroKey -> new MacroStringMap()
				.with(macroKey, formatUid(this.getUniqueId()).orElse(UNKNOWN_VALUE)))
				.orElseGet(MacroStringMap::empty);
	}


	/**
	 * Returns an optional string representation of a UUID.
	 *
	 * @param uniqueId the UUID to format
	 * @return an {@code Optional<String>} if the UUID is non-null
	 */
	static Optional<String> formatUid(final UUID uniqueId)
	{
		return (uniqueId != null)
				? Optional.of(String.valueOf(uniqueId))
				: Optional.empty();
	}

}
