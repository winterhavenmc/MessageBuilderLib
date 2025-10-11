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

package com.winterhavenmc.library.messagebuilder.core.ports.pipeline.formatters.duration;

import java.time.Duration;
import java.time.temporal.ChronoUnit;


/**
 * A simple data container that encapsulates a {@link java.time.Duration}
 * and its corresponding lower bound of precision, represented by a {@link java.time.temporal.ChronoUnit}.
 *
 * <p>This record is typically used in conjunction with duration formatters to specify
 * not only the length of time to be formatted, but also the level of detail or granularity
 * to include in the formatted output (e.g., rounding to the nearest minute, second, etc.).
 *
 * <p>For example, a {@code BoundedDuration} of {@code Duration.ofSeconds(4110)} with
 * {@code ChronoUnit.MINUTES} as the precision might format to {@code "1 hour, 8 minutes"} rather than
 * {@code "1 hour, 8 minutes and 30 seconds"}, depending on the formatter implementation.
 *
 * @param duration the time-based duration to format
 * @param precision the lowest {@link ChronoUnit} to include in the formatted result
 *
 * @see java.time.Duration
 * @see java.time.temporal.ChronoUnit
 * @see DurationFormatter
 */
public record BoundedDuration(Duration duration, ChronoUnit precision) { }
