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

package com.winterhavenmc.util.time;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalUnit;


public final class Tick implements TemporalUnit
{
	private static final long TICK_DURATION_MS = 50;

	@Override
	public Duration getDuration()
	{
		return Duration.ofMillis(TICK_DURATION_MS);
	}

	@Override
	public boolean isDurationEstimated()
	{
		return false; // Ticks are precise, not an estimated duration.
	}

	@Override
	public boolean isDateBased()
	{
		return false; // A tick is not based on dates.
	}

	@Override
	public boolean isTimeBased()
	{
		return true; // A tick is based on time.
	}

	@Override
	public <R extends Temporal> R addTo(R temporal, long amount)
	{
		return (R) temporal.plus(amount * TICK_DURATION_MS, ChronoUnit.MILLIS);
	}

	@Override
	public long between(Temporal temporal1Inclusive, Temporal temporal2Exclusive)
	{
		long millisBetween = ChronoUnit.MILLIS.between(temporal1Inclusive, temporal2Exclusive);
		return millisBetween / TICK_DURATION_MS;
	}

	@Override
	public String toString()
	{
		return "Ticks (50ms)";
	}

}
