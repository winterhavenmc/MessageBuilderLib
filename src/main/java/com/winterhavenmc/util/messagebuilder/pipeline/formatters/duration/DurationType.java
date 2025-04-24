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

package com.winterhavenmc.util.messagebuilder.pipeline.formatters.duration;

import java.time.Duration;
import java.time.temporal.ChronoUnit;


public enum DurationType
{
	NORMAL(""),
	LESS_THAN("< {DURATION}"),
	UNLIMITED("unlimited");

	private final String fallback;


	DurationType(final String failback)
	{
		this.fallback = failback;
	}


	String getFallback()
	{
		return this.fallback;
	}


	/**
	 * Classifies a {@link Duration} into one of three {@link DurationType} categories used for formatting:
	 * <ul>
	 *   <li>{@link #UNLIMITED} – if the duration is negative</li>
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


	public static boolean isUnlimited(Duration duration)
	{
		return duration != null && duration.isNegative();
	}


	public static boolean isLessThan(Duration duration, ChronoUnit precision)
	{
		if (duration == null || precision == null || isUnlimited(duration)) { return false; }

		Duration threshold = Duration.of(1, precision);
		return duration.compareTo(threshold) < 0;
	}

}
