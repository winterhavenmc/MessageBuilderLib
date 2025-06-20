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

package com.winterhavenmc.library.messagebuilder.pipeline.formatters.duration;

import java.time.Duration;
import java.time.temporal.ChronoUnit;


/**
 * Enumeration representing the classification of a {@link java.time.Duration}
 * for use in formatting logic and conditional display of duration-related values.
 *
 * <p>This type is used to determine how a given duration should be interpreted
 * when formatting: as a normal value, an "unlimited" sentinel, or a value less than
 * the minimum precision, such as "less than 1 second".
 *
 * <p>Each constant provides a fallback string used in cases where a duration cannot
 * or should not be formatted using a formatter.
 *
 * <ul>
 *     <li>{@code NORMAL} – a standard positive duration that can be formatted normally.</li>
 *     <li>{@code LESS_THAN} – a duration shorter than the specified {@link ChronoUnit} precision.</li>
 *     <li>{@code UNLIMITED} – a negative duration, which is treated as a sentinel for "no limit".</li>
 * </ul>
 *
 * @see java.time.Duration
 * @see java.time.temporal.ChronoUnit
 * @see com.winterhavenmc.library.messagebuilder.pipeline.formatters.duration.DurationFormatter
 */
public enum DurationType
{
	NORMAL(""),
	LESS_THAN("< {DURATION}"),
	UNLIMITED("∞");

	private final String fallback;


	/**
	 * Constructs a DurationType with a fallback formatting string.
	 *
	 * @param fallback the fallback string used in display
	 */
	DurationType(final String fallback)
	{
		this.fallback = fallback;
	}


	/**
	 * Returns the fallback string associated with this duration type.
	 *
	 * @return a fallback string used for rendering this classification
	 */
	String getFallback()
	{
		return this.fallback;
	}


	/**
	 * Classifies a {@link Duration} into one of three {@link DurationType} categories used for formatting:
	 * <ul>
	 *   <li>{@link #UNLIMITED} – if the duration is negative, it represents an unlimited time</li>
	 *   <li>{@link #LESS_THAN} – if the duration is non-negative and less than one unit of the specified precision</li>
	 *   <li>{@link #NORMAL} – if the duration is equal to or greater than one unit of the specified precision</li>
	 * </ul>
	 *
	 * <p>If the input duration is {@code null}, it is treated as {@code Duration.ZERO}, and classified
	 * according to the provided precision.</p>
	 *
	 * @param duration the duration to classify; may be {@code null}
	 * @param precision the {@link ChronoUnit} representing the formatting threshold unit; must not be {@code null}
	 * @return the {@link DurationType} corresponding to the given duration and precision
	 * @throws NullPointerException if {@code precision} is {@code null}
	 */
	public static DurationType classify(final Duration duration, final ChronoUnit precision)
	{
		return switch (duration)
		{
			case Duration d when isUnlimited(d) -> UNLIMITED;
			case Duration d when isLessThan(d, precision) -> LESS_THAN;
			case null -> LESS_THAN;
			default -> NORMAL;
		};
	}


	/**
	 * Checks if the given duration should be treated as "unlimited" (i.e., negative).
	 *
	 * @param duration the duration to check
	 * @return {@code true} if the duration is negative; otherwise {@code false}
	 */
	public static boolean isUnlimited(Duration duration)
	{
		return duration != null && duration.isNegative();
	}


	/**
	 * Checks if the given duration is shorter than the provided precision threshold.
	 *
	 * @param duration the duration to compare
	 * @param precision the precision unit used as threshold
	 * @return {@code true} if duration is non-null, non-negative, and less than 1 {@code precision} unit
	 */
	public static boolean isLessThan(Duration duration, ChronoUnit precision)
	{
		if (duration == null || precision == null || isUnlimited(duration)) { return false; }

		Duration threshold = Duration.of(1, precision);
		return duration.compareTo(threshold) < 0;
	}

}
