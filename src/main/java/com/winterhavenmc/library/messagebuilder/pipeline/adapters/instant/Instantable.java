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

package com.winterhavenmc.library.messagebuilder.pipeline.adapters.instant;

import com.winterhavenmc.library.messagebuilder.keys.MacroKey;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.AdapterContextContainer;
import com.winterhavenmc.library.messagebuilder.pipeline.containers.MacroStringMap;
import com.winterhavenmc.library.messagebuilder.resources.configuration.LocaleProvider;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Optional;

import static com.winterhavenmc.library.messagebuilder.pipeline.adapters.Adapter.BuiltIn.INSTANT;
import static com.winterhavenmc.library.messagebuilder.pipeline.adapters.Adapter.UNKNOWN_VALUE;


/**
 * An interface that can be implemented by any class that contains
 * an {@code Instant} and appropriately named accessor method
 */
@FunctionalInterface
public interface Instantable
{
	Instant getInstant();


	/**
	 * Returns a new MacroStringMap containing all fields extracted from an Instantable conforming object
	 *
	 * @param baseKey the top level key for the fields of this object
	 * @param formatStyle the format style to use for formatting date/time
	 * @param ctx containing a provider of the currently configured locale
	 * @return a MacroStringMap containing the fields extracted for objects of Instantable type
	 */
	default MacroStringMap extractInstant(final MacroKey baseKey,
										  final FormatStyle formatStyle,
										  final AdapterContextContainer ctx)
	{
		return baseKey.append(INSTANT)
				.map(macroKey -> new MacroStringMap()
				.with(macroKey, formatInstant(this.getInstant(), formatStyle, ctx.formatterContainer()
						.localeProvider()).orElse(UNKNOWN_VALUE)))
				.orElseGet(MacroStringMap::empty);
	}


	/**
	 * Returns a formatted string representing an instant in time, using the supplied style and locale
	 *
	 * @param instant the time instant to format to String
	 * @param formatStyle the formatting style
	 * @param localeProvider a provider of the current locale setting in plugin config.yml
	 * @return {@code Optional<String>} containing the formatted instant
	 */
	static Optional<String> formatInstant(final Instant instant,
										  final FormatStyle formatStyle,
										  final LocaleProvider localeProvider)
	{
		return Optional.of(DateTimeFormatter
				.ofLocalizedDateTime(formatStyle)
				.withLocale(localeProvider.getLocale())
				.format(instant));
	}

}
