/*
 * Copyright (c) 2022-2025 Tim Savage.
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

package com.winterhavenmc.library.time;

public enum TimeUnit
{
	// pass number of milliseconds for each time unit as parameter to constructor
	MILLISECONDS(1L),
	TICKS(50L),
	SECONDS(1_000L),
	MINUTES(60_000L),
	HOURS(3_600_000L),
	DAYS(86_400_000L),
	WEEKS(604_800_000L),
	MONTHS(2_629_800_000L),
	YEARS(31_557_600_000L);

	// number of milliseconds for each time unit
	private final long millis;

	/**
	 * Class constructor
	 * @param millis milliseconds for time unit
	 */
	TimeUnit(final long millis)
	{
		this.millis = millis;
	}

	public final long toMillis(final long duration)
	{
		return convert(duration, MILLISECONDS);
	}

	public final long toTicks(final long duration)
	{
		return convert(duration, TICKS);
	}

	public final long toSeconds(final long duration)
	{
		return convert(duration, SECONDS);
	}

	public final long toMinutes(final long duration)
	{
		return convert(duration, MINUTES);
	}

	public final long toHours(final long duration)
	{
		return convert(duration, HOURS);
	}

	public final long toDays(final long duration)
	{
		return convert(duration, DAYS);
	}

	public final long toWeeks(final long duration)
	{
		return convert(duration, WEEKS);
	}

	public final long toMonths(final long duration)
	{
		return convert(duration, MONTHS);
	}

	public final long toYears(final long duration)
	{
		return convert(duration, YEARS);
	}

	public final long convert(final long duration, final TimeUnit unit)
	{
		if (duration < Long.MIN_VALUE / this.millis)
		{
			throw new IllegalArgumentException("duration of " + duration + " " + this + " would cause an underflow in conversion to " + unit + ".");
		}
		if (duration > Long.MAX_VALUE / this.millis)
		{
			throw new IllegalArgumentException("duration of " + duration + " " + this + " would cause an overflow in conversion to " + unit + ".");
		}
		return duration * this.millis / unit.millis;
	}

	/**
	 * Get the number of milliseconds for each time unit.
	 *
	 * @return the number of milliseconds equal to each time unit
	 */
	public final long getMillis()
	{
		return this.millis;
	}


	public final boolean isLessThan(final long millis)
	{
		return this.millis < millis;
	}


	public final boolean isLessThan(final TimeUnit timeUnit)
	{
		return this.millis < timeUnit.millis;
	}


	public final boolean isGreaterThan(final long millis)
	{
		return this.millis > millis;
	}


	public final boolean isGreaterThan(final TimeUnit timeUnit)
	{
		return this.millis > timeUnit.millis;
	}


	public final long times(int multiplier)
	{
		return this.millis * multiplier;
	}


	public final long justShyOf(int number)
	{
		return this.millis * number - 1;
	}


	public final long one()
	{
		return this.millis;
	}

}
