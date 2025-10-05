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

package com.winterhavenmc.library.messagebuilder.core.ports.formatters.duration;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

/**
 * Formats {@link java.time.Duration} values into localized human-readable strings.
 *
 * <p>This functional interface defines the contract for converting a {@link Duration}
 * into a formatted {@link String}, using a specified {@link ChronoUnit} as the lower bound
 * of precision. This enables fine-grained control over the formatting of durations, such as
 * "2 hours", "3 minutes", or "1 day, 4 hours", depending on the formatter implementation.
 *
 * <p>Implementations of this interface may use different localization strategies and formatting
 * libraries. For example, {@code PrettyTimeDurationFormatter} uses the Time4J library to generate
 * natural-language strings.
 *
 * @see java.time.Duration
 * @see java.time.temporal.ChronoUnit
 */
@FunctionalInterface
public interface DurationFormatter
{
	String format(final Duration duration, final ChronoUnit lowerBound);
}
