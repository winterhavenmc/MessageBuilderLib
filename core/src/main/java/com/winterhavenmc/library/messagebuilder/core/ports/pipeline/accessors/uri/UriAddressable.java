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

package com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.uri;

import com.winterhavenmc.library.messagebuilder.core.context.AccessorCtx;
import com.winterhavenmc.library.messagebuilder.core.maps.MacroStringMap;
import com.winterhavenmc.library.messagebuilder.models.keys.ValidMacroKey;

import java.net.URI;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

import static com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.Accessor.BuiltIn.URL;
import static com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.Accessor.UNKNOWN_VALUE;


@FunctionalInterface
public interface UriAddressable
{
	URI getUri();

	Predicate<URI> VALID_URI = Objects::nonNull;


	default MacroStringMap extractUri(final ValidMacroKey baseKey, final AccessorCtx ctx)
	{
		return baseKey.append(URL).isValid()
				.map(macroKey -> new MacroStringMap().with(macroKey, formatUri(this.getUri()).orElse(UNKNOWN_VALUE)))
				.orElseGet(MacroStringMap::empty);
	}


	static Optional<String> formatUri(final URI uri)
	{
		return (VALID_URI.test(uri))
				? Optional.of(uri.toString())
				: Optional.empty();
	}

}
