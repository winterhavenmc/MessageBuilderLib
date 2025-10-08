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

package com.winterhavenmc.library.messagebuilder.core.pipeline.adapters.pluralname;


import com.winterhavenmc.library.messagebuilder.models.keys.ValidMacroKey;
import com.winterhavenmc.library.messagebuilder.core.pipeline.adapters.Adapter;
import com.winterhavenmc.library.messagebuilder.core.context.AdapterCtx;
import com.winterhavenmc.library.messagebuilder.core.maps.MacroStringMap;

import java.util.Optional;
import java.util.function.Predicate;

import static com.winterhavenmc.library.messagebuilder.core.pipeline.adapters.Adapter.BuiltIn.PLURAL_NAME;
import static com.winterhavenmc.library.messagebuilder.core.pipeline.adapters.Adapter.UNKNOWN_VALUE;


/**
 * An interface representing objects that expose a <i>pluralized name</i> for macro replacement.
 *
 * <p>Implementing this interface allows objects to automatically contribute to the
 * {@code {OBJECT.DISPLAY_NAME}} placeholder replacement in localized message strings.
 * The extracted value is validated to ensure it is non-null and non-blank.
 *
 * <p>This interface is primarily used by the {@code PluralNameAdapter} and is supported
 * out of the box for Bukkit {@link org.bukkit.entity.Player}, {@link org.bukkit.Nameable},
 * and {@link org.bukkit.World} types.
 *
 * <p>Plugins can also implement this interface on custom types to enable automatic
 * macro support for display names.
 *
 * @see Adapter Adapter
 */
@FunctionalInterface
public interface PluralNameable
{
	/**
	 * Returns the display name associated with this object.
	 *
	 * @return a string representing the display name, or {@code null} if not available
	 */
	String getPluralName();


	/**
	 * A predicate used to validate that a display name is non-null and non-blank.
	 */
	Predicate<String> VALID_PLURAL_NAME = pluralName -> pluralName != null && !pluralName.isBlank();


	/**
	 * Extracts a {@link MacroStringMap} containing the formatted plural name
	 * under the {@code PLURAL_NAME} subkey of the given base string.
	 *
	 * @param baseKey the root macro string from which to derive the full macro path
	 * @param ctx     the adapter context containing any contextual helpers
	 * @return a {@code MacroStringMap} containing a single entry for the plural name,
	 * or an empty map if the name is invalid or the string cannot be derived
	 */
	default MacroStringMap extractPluralName(final ValidMacroKey baseKey, final AdapterCtx ctx)
	{
		return baseKey.append(PLURAL_NAME).isValid()
				.map(macroKey -> new MacroStringMap()
						.with(macroKey, formatPluralName(this.getPluralName()).orElse(UNKNOWN_VALUE)))
				.orElseGet(MacroStringMap::empty);
	}


	/**
	 * Attempts to format and return a plural name if valid.
	 *
	 * @param pluralName the raw display name string
	 * @return an optional containing the display name if valid, or empty otherwise
	 */
	static Optional<String> formatPluralName(final String pluralName)
	{
		return (VALID_PLURAL_NAME.test(pluralName))
				? Optional.of(pluralName)
				: Optional.empty();
	}

}
