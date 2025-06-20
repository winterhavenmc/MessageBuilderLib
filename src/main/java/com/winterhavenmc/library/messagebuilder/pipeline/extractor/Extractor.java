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

import com.winterhavenmc.library.messagebuilder.pipeline.adapters.Adapter;
import com.winterhavenmc.library.messagebuilder.keys.MacroKey;
import com.winterhavenmc.library.messagebuilder.pipeline.maps.MacroStringMap;


/**
 * Functional interface representing a strategy for extracting macro-replaceable fields
 * from an object that has been successfully adapted by a given {@link Adapter}.
 * <p>
 * The extractor maps the values returned by the adapted object into a {@link MacroStringMap},
 * keyed by the appropriate macro placeholder keys derived from the {@link MacroKey} base.
 * <p>
 * Each {@code Adapter} is responsible for adapting a specific object type to a known
 * functional interface (e.g., {@code Nameable}, {@code Locatable}, {@code Ownable}, etc.).
 * The {@code Extractor} then calls the appropriate extraction method for that interface,
 * using the provided base macro key and context.
 */
@FunctionalInterface
public interface Extractor
{
	/**
	 * Extracts a set of macro string values from an adapted object and returns
	 * a map of placeholder keys to replacement strings.
	 *
	 * @param baseKey the base {@link MacroKey} representing the placeholder prefix
	 * @param adapter the adapter used to produce the adapted object
	 * @param adapted the result of adapting an object to a known macro field interface
	 * @param <T>     the type of the adapted object
	 * @return a {@link MacroStringMap} containing all extracted macro values
	 */
	<T> MacroStringMap extract(MacroKey baseKey, Adapter adapter, T adapted);
}
