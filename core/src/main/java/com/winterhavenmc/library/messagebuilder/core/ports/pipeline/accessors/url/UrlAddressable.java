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

package com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.url;


import com.winterhavenmc.library.messagebuilder.models.keys.ValidMacroKey;
import com.winterhavenmc.library.messagebuilder.core.context.AccessorCtx;
import com.winterhavenmc.library.messagebuilder.core.maps.MacroStringMap;

import java.util.Optional;
import java.util.function.Predicate;

import static com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.Accessor.BuiltIn.URL;
import static com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.Accessor.UNKNOWN_VALUE;


@FunctionalInterface
public interface UrlAddressable
{
	String getUrl();

	Predicate<String> VALID_URL = version -> version != null && !version.isBlank();


	default MacroStringMap extractUrl(final ValidMacroKey baseKey, final AccessorCtx ctx)
	{
		return baseKey.append(URL).isValid()
				.map(macroKey -> new MacroStringMap()
						.with(macroKey, formatUrl(this.getUrl()).orElse(UNKNOWN_VALUE)))
				.orElseGet(MacroStringMap::empty);
	}


	static Optional<String> formatUrl(final String versionString)
	{
		return (VALID_URL.test(versionString))
				? Optional.of(versionString)
				: Optional.empty();
	}

}
