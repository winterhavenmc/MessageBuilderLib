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

import com.winterhavenmc.library.messagebuilder.keys.MacroKey;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.AdapterContextContainer;
import com.winterhavenmc.library.messagebuilder.pipeline.containers.MacroStringMap;
import com.winterhavenmc.library.messagebuilder.pipeline.formatters.duration.DurationFormatter;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static com.winterhavenmc.library.messagebuilder.pipeline.adapters.Adapter.BuiltIn.DURATION;
import static com.winterhavenmc.library.messagebuilder.pipeline.adapters.Adapter.UNKNOWN_VALUE;


/**
 * An interface that can be implemented by any class that contains
 * a duration and appropriately named accessor method
 */
@FunctionalInterface
public interface Durationable
{
	Duration getDuration();


	/**
	 * Returns a new MacroStringMap containing all fields extracted from a Durationable type
	 *
	 * @param baseKey the top level key for the fields of this object
	 * @param lowerBound a Temporal unit for use as a lower bound of units displayed
	 * @param ctx containing durationFormatter the duration formatter to be used to convert the duration to a String
	 * @return a MacroStringMap containing the fields extracted for objects of Durationable type
	 */
	default MacroStringMap extractDuration(final MacroKey baseKey,
										   final ChronoUnit lowerBound,
										   final AdapterContextContainer ctx)
	{
		return baseKey.append(DURATION)
				.map(macroKey -> new MacroStringMap()
				.with(macroKey, formatDuration(this.getDuration(), lowerBound, ctx.formatterContainer()
						.durationFormatter()).orElse(UNKNOWN_VALUE)))
				.orElseGet(MacroStringMap::empty);
	}


	/**
	 * Returns a formatted string representing a duration of time, using the supplied DurationFormatter
	 *
	 * @param duration the duration to convert to a formatted string
	 * @param lowerBound a Temporal unit for use as a lower bound of units displayed
	 * @param durationFormatter the duration formatter to be used to convert the duration to a String
	 * @return an {@code Optional<String>} wrapping a formatted string representation of the duration
	 */
	static Optional<String> formatDuration(final Duration duration,
										   final ChronoUnit lowerBound,
										   final DurationFormatter durationFormatter)
	{
		return Optional.ofNullable(durationFormatter.format(duration, lowerBound));
	}


	static Duration durationUntil(final Instant instant)
	{
		return Duration.between(Instant.now(), instant);
	}

}
