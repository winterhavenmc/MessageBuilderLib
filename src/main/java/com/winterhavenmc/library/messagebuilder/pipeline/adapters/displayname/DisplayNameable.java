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

package com.winterhavenmc.library.messagebuilder.pipeline.adapters.displayname;


import com.winterhavenmc.library.messagebuilder.keys.MacroKey;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.AdapterContextContainer;
import com.winterhavenmc.library.messagebuilder.pipeline.maps.MacroStringMap;

import java.util.Optional;
import java.util.function.Predicate;

import static com.winterhavenmc.library.messagebuilder.pipeline.adapters.Adapter.BuiltIn.DISPLAY_NAME;
import static com.winterhavenmc.library.messagebuilder.pipeline.adapters.Adapter.UNKNOWN_VALUE;


/**
 * An interface representing objects that expose a <i>display name</i> for macro replacement.
 *
 * <p>Implementing this interface allows objects to automatically contribute to the
 * {@code {OBJECT.DISPLAY_NAME}} placeholder replacement in localized message strings.
 * The extracted value is validated to ensure it is non-null and non-blank.
 *
 * <p>This interface is primarily used by the {@link DisplayNameAdapter} and is supported
 * out of the box for Bukkit {@link org.bukkit.entity.Player}, {@link org.bukkit.Nameable},
 * and {@link org.bukkit.World} types.
 *
 * <p>Plugins can also implement this interface on custom types to enable automatic
 * macro support for display names.
 *
 * @see DisplayNameAdapter
 * @see com.winterhavenmc.library.messagebuilder.pipeline.adapters.Adapter Adapter
 */
@FunctionalInterface
public interface DisplayNameable
{
	/**
	 * Returns the display name associated with this object.
	 *
	 * @return a string representing the display name, or {@code null} if not available
	 */
	String getDisplayName();


	/**
	 * A predicate used to validate that a display name is non-null and non-blank.
	 */
	Predicate<String> VALID_DISPLAY_NAME = displayName -> displayName != null && !displayName.isBlank();


	/**
	 * Extracts a {@link MacroStringMap} containing the formatted display name
	 * under the {@code DISPLAY_NAME} subkey of the given base key.
	 *
	 * @param baseKey the root macro key from which to derive the full macro path
	 * @param ctx     the adapter context containing any contextual helpers
	 * @return a {@code MacroStringMap} containing a single entry for the display name,
	 * or an empty map if the name is invalid or the key cannot be derived
	 */
	default MacroStringMap extractDisplayName(final MacroKey baseKey, final AdapterContextContainer ctx)
	{
		return baseKey.append(DISPLAY_NAME)
				.map(macroKey -> new MacroStringMap()
						.with(macroKey, formatDisplayName(this.getDisplayName()).orElse(UNKNOWN_VALUE)))
				.orElseGet(MacroStringMap::empty);
	}


	/**
	 * Attempts to format and return a display name if valid.
	 *
	 * @param displayName the raw display name string
	 * @return an optional containing the display name if valid, or empty otherwise
	 */
	static Optional<String> formatDisplayName(final String displayName)
	{
		return (VALID_DISPLAY_NAME.test(displayName))
				? Optional.of(displayName)
				: Optional.empty();
	}

}
