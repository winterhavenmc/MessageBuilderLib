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

package com.winterhavenmc.library.messagebuilder.pipeline.extractor;

import com.winterhavenmc.library.messagebuilder.keys.MacroKey;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.AdapterContextContainer;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.displayname.DisplayNameable;
import com.winterhavenmc.library.messagebuilder.pipeline.containers.MacroStringMap;

import java.util.Optional;
import java.util.function.Predicate;

import static com.winterhavenmc.library.messagebuilder.pipeline.adapters.Adapter.BuiltIn.DISPLAY_NAME;
import static com.winterhavenmc.library.messagebuilder.pipeline.adapters.Adapter.UNKNOWN_VALUE;

public class DisplayNameExtractor
{
	private static final Predicate<String> VALID_DISPLAY_NAME = displayName -> displayName != null && !displayName.isBlank();


	/**
	 * Retrieve MacroStringMap with fields populated for objects that have a display name field or equivalent
	 *
	 * @param baseKey the base macro key for the object
	 * @return MacroStringMap of macro keys with extracted string values
	 */
	MacroStringMap extractDisplayName(final MacroKey baseKey, final DisplayNameable displayNameable, final AdapterContextContainer ctx)
	{
		return baseKey.append(DISPLAY_NAME)
				.map(macroKey -> new MacroStringMap()
						.with(macroKey, formatDisplayName(displayNameable.getDisplayName()).orElse(UNKNOWN_VALUE)))
				.orElseGet(MacroStringMap::empty);
	}


	/**
	 * Retrieve {@code Optional<String>} of valid display name for DisplayNameable implementing object
	 *
	 * @return {@code Optional<String>} of a valid display name, or an empty Optional if no valid display name found
	 */
	public static Optional<String> formatDisplayName(final String displayName)
	{
		return (VALID_DISPLAY_NAME.test(displayName))
				? Optional.of(displayName)
				: Optional.empty();
	}

}
