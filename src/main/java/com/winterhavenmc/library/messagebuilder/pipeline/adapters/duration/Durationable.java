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

package com.winterhavenmc.library.messagebuilder.pipeline.adapters.duration;

import com.winterhavenmc.library.messagebuilder.keys.ValidMacroKey;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.AdapterContextContainer;
import com.winterhavenmc.library.messagebuilder.pipeline.maps.MacroStringMap;
import com.winterhavenmc.library.messagebuilder.pipeline.formatters.duration.DurationFormatter;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static com.winterhavenmc.library.messagebuilder.pipeline.adapters.Adapter.BuiltIn.DURATION;
import static com.winterhavenmc.library.messagebuilder.pipeline.adapters.Adapter.UNKNOWN_VALUE;


/**
 * Represents an object that exposes a {@link java.time.Duration}, enabling it to participate
 * in macro substitution using duration-based placeholders.
 *
 * <p>Objects implementing this interface can provide a duration value via {@link #getDuration()},
 * and automatically extract localized string representations of that duration through the
 * {@link #extractDuration(ValidMacroKey, ChronoUnit, AdapterContextContainer)} method.
 *
 * <p>This interface supports usage in message templates via the {@code [OBJECT.DURATION}} macro.
 * Formatting is handled by the {@link com.winterhavenmc.library.messagebuilder.pipeline.formatters.duration.DurationFormatter}
 * implementation provided in the {@link AdapterContextContainer}.
 *
 * <p>Three utility methods are provided:
 * <ul>
 *   <li>{@link #extractDuration(ValidMacroKey, ChronoUnit, AdapterContextContainer)} — generates a
 *       {@link MacroStringMap} with the localized string representation of the duration</li>
 *   <li>{@link #formatDuration(Duration, ChronoUnit, DurationFormatter)} — formats a duration using
 *       the configured {@code DurationFormatter}</li>
 *   <li>{@link #durationUntil(Instant)} — helper for computing the {@code Duration} until a future
 *       {@link java.time.Instant}</li>
 * </ul>
 *
 * <p>Durations are automatically classified using the {@link com.winterhavenmc.library.messagebuilder.pipeline.formatters.duration.DurationType}
 * enum into:
 * <ul>
 *   <li>{@code NORMAL} — durations above 1 unit</li>
 *   <li>{@code LESS_THAN} — durations under the specified {@link ChronoUnit} lower bound</li>
 *   <li>{@code UNLIMITED} — negative durations signifying no expiration or restriction</li>
 * </ul>
 *
 * @see java.time.Duration
 * @see java.time.temporal.ChronoUnit
 * @see com.winterhavenmc.library.messagebuilder.pipeline.formatters.duration.DurationFormatter DurationFormatter
 * @see com.winterhavenmc.library.messagebuilder.pipeline.formatters.duration.DurationType DurationType
 */
@FunctionalInterface
public interface Durationable
{
	/**
	 * Returns the duration value associated with this object.
	 *
	 * @return the duration, or {@code null} if unavailable
	 */
	Duration getDuration();


	/**
	 * Extracts a {@link MacroStringMap} containing a single entry mapping the provided
	 * {@link ValidMacroKey} (with {@code .DURATION} appended) to a localized string representation
	 * of this object's duration.
	 *
	 * @param baseKey the top-level string to which {@code DURATION} will be appended
	 * @param lowerBound the smallest {@link ChronoUnit} to display (e.g., {@code MINUTES}, {@code SECONDS})
	 * @param ctx context container providing the
	 * {@link com.winterhavenmc.library.messagebuilder.pipeline.formatters.duration.DurationFormatter}
	 * @return a {@code MacroStringMap} containing the extracted duration string; empty if the string could not be constructed
	 */
	default MacroStringMap extractDuration(final ValidMacroKey baseKey,
										   final ChronoUnit lowerBound,
										   final AdapterContextContainer ctx)
	{
		return baseKey.append(DURATION).isValid()
				.map(macroKey -> new MacroStringMap()
				.with(macroKey, formatDuration(this.getDuration(), lowerBound, ctx.formatterContainer()
						.durationFormatter()).orElse(UNKNOWN_VALUE)))
				.orElseGet(MacroStringMap::empty);
	}


	/**
	 * Uses the configured {@link DurationFormatter} to format the given {@link Duration}
	 * according to the specified precision.
	 *
	 * @param duration the duration to format
	 * @param lowerBound the lowest precision unit to include in the result (e.g., seconds, minutes)
	 * @param durationFormatter the formatter to use for localization
	 * @return an {@code Optional<String>} containing the formatted result, or empty if input is {@code null}
	 */
	static Optional<String> formatDuration(final Duration duration,
										   final ChronoUnit lowerBound,
										   final DurationFormatter durationFormatter)
	{
		return Optional.ofNullable(durationFormatter.format(duration, lowerBound));
	}


	/**
	 * Computes the duration from the current moment until the given {@link Instant}.
	 *
	 * @param instant a future point in time
	 * @return the computed duration, or {@link Duration#ZERO} if the instant is {@code null}
	 */
	static Duration durationUntil(final Instant instant)
	{
		return (instant != null)
				? Duration.between(Instant.now(), instant)
				: Duration.ZERO;
	}

}
