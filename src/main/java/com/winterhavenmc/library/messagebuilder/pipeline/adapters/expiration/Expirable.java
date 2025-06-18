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

package com.winterhavenmc.library.messagebuilder.pipeline.adapters.expiration;

import com.winterhavenmc.library.messagebuilder.keys.MacroKey;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.AdapterContextContainer;
import com.winterhavenmc.library.messagebuilder.pipeline.containers.MacroStringMap;

import java.time.Instant;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;

import static com.winterhavenmc.library.messagebuilder.pipeline.adapters.Adapter.UNKNOWN_VALUE;
import static com.winterhavenmc.library.messagebuilder.pipeline.adapters.Adapter.BuiltIn.*;
import static com.winterhavenmc.library.messagebuilder.pipeline.adapters.duration.Durationable.durationUntil;
import static com.winterhavenmc.library.messagebuilder.pipeline.adapters.duration.Durationable.formatDuration;
import static com.winterhavenmc.library.messagebuilder.pipeline.adapters.instant.Instantable.formatInstant;


/**
 * Provides an adapter for objects that expose an expiration {@link java.time.Instant}.
 *
 * <p>This package defines the {@link Expirable}
 * interface for adapting expiration timestamps into human-readable macro values.
 * It enables support for both duration-based and instant-based macro replacements.
 *
 * <p>The following macro keys are supported (assuming a macro key prefix of {@code OBJECT}):
 * <ul>
 *     <li>{@code %OBJECT.EXPIRATION.DURATION%} — A formatted duration string representing time remaining until expiration</li>
 *     <li>{@code %OBJECT.EXPIRATION.INSTANT%} — A localized date/time string representing the expiration instant</li>
 * </ul>
 *
 * <p>The duration string is formatted using a
 * {@link com.winterhavenmc.library.messagebuilder.pipeline.formatters.duration.DurationFormatter DurationFormatter}
 * implementation, such as {@code LocalizedDurationFormatter}, and the instant string is formatted using a
 * {@link java.time.format.DateTimeFormatter DateTimeFormatter} constructed with
 * {@link java.time.format.FormatStyle FormatStyle} and a
 * {@link com.winterhavenmc.library.messagebuilder.resources.configuration.LocaleProvider LocaleProvider}.
 *
 * <p>These macros provide flexibility to server operators, allowing them to choose the display format
 * that best suits their message context.
 *
 * <p>This adapter does not attempt to adapt Bukkit-native types. Only plugin-defined types that implement
 * {@link com.winterhavenmc.library.messagebuilder.pipeline.adapters.expiration.Expirable} will be supported.
 *
 * @see com.winterhavenmc.library.messagebuilder.pipeline.adapters.expiration.Expirable Expirable
 * @see com.winterhavenmc.library.messagebuilder.pipeline.adapters.Adapter Adapter
 * @see com.winterhavenmc.library.messagebuilder.pipeline.formatters.duration.DurationFormatter DurationFormatter
 */
@FunctionalInterface
public interface Expirable
{
	/**
	 * Returns the expiration timestamp for this object.
	 *
	 * @return an {@link Instant} representing when the object expires
	 */
	Instant getExpiration();


	/**
	 * Extracts macro key-value pairs for this object's expiration, including a formatted duration
	 * and a formatted instant string.
	 *
	 * <p>Uses the provided macro key as a base, and appends {@code EXPIRATION.DURATION} and
	 * {@code EXPIRATION.INSTANT} keys, resolving their values using the supplied formatter context.
	 *
	 * @param baseKey the top-level macro key associated with this object
	 * @param lowerBound the smallest unit of time to be displayed in the duration (e.g., {@code ChronoUnit.MINUTES})
	 * @param formatStyle the formatting style for displaying the instant (e.g., {@code FormatStyle.MEDIUM})
	 * @param ctx the context container with formatters and world resolvers
	 * @return a {@link MacroStringMap} containing populated expiration macro keys and their string values
	 */
	default MacroStringMap extractExpiration(final MacroKey baseKey,
											 final ChronoUnit lowerBound,
											 final FormatStyle formatStyle,
											 final AdapterContextContainer ctx)
	{
		MacroStringMap resultMap = new MacroStringMap();

		baseKey.append(EXPIRATION).ifPresent(protectionKey ->
		{
			// formatted duration (with 1/3 of lower bound duration added, to compensate for processing delay)
			protectionKey.append(DURATION).ifPresent(macroKey ->
					resultMap.put(macroKey,
							formatDuration(durationUntil(this.getExpiration().plus(lowerBound.getDuration().dividedBy(3))),
									lowerBound, ctx.formatterContainer().durationFormatter())
							.orElse(UNKNOWN_VALUE)));

			// formatted date/time from Instant
			protectionKey.append(INSTANT).ifPresent(macroKey ->
					resultMap.put(macroKey,
							formatInstant(this.getExpiration(), formatStyle, ctx.formatterContainer().localeProvider())
							.orElse(UNKNOWN_VALUE)));
		});

		return resultMap;
	}

}
