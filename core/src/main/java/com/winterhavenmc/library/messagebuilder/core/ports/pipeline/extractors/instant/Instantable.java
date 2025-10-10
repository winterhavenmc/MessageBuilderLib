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

package com.winterhavenmc.library.messagebuilder.core.ports.pipeline.extractors.instant;

import com.winterhavenmc.library.messagebuilder.core.context.AdapterCtx;
import com.winterhavenmc.library.messagebuilder.models.configuration.LocaleProvider;
import com.winterhavenmc.library.messagebuilder.models.keys.ValidMacroKey;
import com.winterhavenmc.library.messagebuilder.core.maps.MacroStringMap;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Optional;

import static com.winterhavenmc.library.messagebuilder.core.ports.pipeline.extractors.Adapter.BuiltIn.INSTANT;
import static com.winterhavenmc.library.messagebuilder.core.ports.pipeline.extractors.Adapter.UNKNOWN_VALUE;


/**
 * An interface for types that expose a single {@link Instant} value, such as a timestamp
 * for creation, expiration, modification, or other moments in time.
 *
 * <p>When used with the MessageBuilderLib adapter pipeline, implementing this interface enables
 * support for macro placeholders like:
 * <pre>{@code [OBJECT.INSTANT}}</pre>
 * which will be automatically replaced with a localized string representation of the instant,
 * using the system’s configured {@link FormatStyle} and {@link java.util.Locale}.
 *
 * <p>This allows plugin-defined types to seamlessly integrate timestamp-based information
 * into translatable message strings.
 */
@FunctionalInterface
public interface Instantable
{
	/**
	 * Returns the {@link Instant} that this object represents.
	 *
	 * @return an {@code Instant}, or {@code null} if none is defined
	 */
	Instant getInstant();


	/**
	 * Extracts a formatted timestamp field from this {@code Instantable}, based on the given
	 * {@code FormatStyle} and locale context.
	 *
	 * @param baseKey the macro string prefix under which the {@code INSTANT} field will be inserted
	 * @param formatStyle the {@link FormatStyle} to use for localization
	 * @param ctx the adapter context container providing a {@link LocaleProvider}
	 * @return a {@code MacroStringMap} with the formatted timestamp under {@code baseKey.INSTANT}
	 */
	default MacroStringMap extractInstant(final ValidMacroKey baseKey,
										  final FormatStyle formatStyle,
										  final AdapterCtx ctx)
	{
		return baseKey.append(INSTANT).isValid()
				.map(macroKey -> new MacroStringMap()
				.with(macroKey, formatInstant(this.getInstant(), formatStyle, ctx.formatterCtx()
						.localeProvider()).orElse(UNKNOWN_VALUE)))
				.orElseGet(MacroStringMap::empty);
	}


	/**
	 * Formats the provided {@link Instant} using the specified {@code FormatStyle} and locale.
	 *
	 * @param instant the instant to format
	 * @param formatStyle the formatting style to use
	 * @param localeProvider a provider of the current locale and timezone
	 * @return an {@code Optional<String>} containing the formatted timestamp, or empty if the instant is {@code null}
	 */
	static Optional<String> formatInstant(final Instant instant,
										  final FormatStyle formatStyle,
										  final LocaleProvider localeProvider)
	{
		if (instant == null) { return Optional.empty(); }

		DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(formatStyle);
		ZonedDateTime zonedDateTime = instant.atZone(localeProvider.getZoneId());

		return Optional.of(formatter.format(zonedDateTime));
	}

}
