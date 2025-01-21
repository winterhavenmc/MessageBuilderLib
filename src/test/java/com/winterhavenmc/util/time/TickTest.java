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

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;

import static org.junit.jupiter.api.Assertions.*;

class TickTest {

	private static final long TICK_DURATION_MS = 50;
	private final Tick tick = new Tick();

	@Test
	void testGetDuration() {
		Duration expectedDuration = Duration.ofMillis(TICK_DURATION_MS);
		assertEquals(expectedDuration, tick.getDuration(), "Tick duration should be 50ms.");
	}

	@Test
	void testIsDurationEstimated() {
		assertFalse(tick.isDurationEstimated(), "Ticks should not have an estimated duration.");
	}

	@Test
	void testIsDateBased() {
		assertFalse(tick.isDateBased(), "Ticks should not be date-based.");
	}

	@Test
	void testIsTimeBased() {
		assertTrue(tick.isTimeBased(), "Ticks should be time-based.");
	}

	@Test
	void testAddTo() {
		Instant now = Instant.now();
		Temporal result = tick.addTo(now, 10); // Add 10 ticks (500ms)
		assertEquals(now.plus(10 * TICK_DURATION_MS, ChronoUnit.MILLIS), result,
				"Adding 10 ticks should move the temporal by 500ms.");
	}

	@Test
	void testBetween() {
		Instant start = Instant.now();
		Instant end = start.plus(500, ChronoUnit.MILLIS); // 10 ticks
		long ticksBetween = tick.between(start, end);
		assertEquals(10, ticksBetween, "There should be 10 ticks between the two instants.");
	}

	@Test
	void testBetweenWithPartialTicks() {
		Instant start = Instant.now();
		Instant end = start.plus(525, ChronoUnit.MILLIS); // 10.5 ticks
		long ticksBetween = tick.between(start, end);
		assertEquals(10, ticksBetween, "Only full ticks should be counted.");
	}

	@Test
	void testAddToNegativeTicks() {
		Instant now = Instant.now();
		Temporal result = tick.addTo(now, -5); // Subtract 5 ticks (-250ms)
		assertEquals(now.minus(5 * TICK_DURATION_MS, ChronoUnit.MILLIS), result,
				"Adding -5 ticks should move the temporal back by 250ms.");
	}

	@Test
	void testToString() {
		assertEquals("Ticks (50ms)", tick.toString(), "The string representation should match.");
	}

	@Test
	void testGetDurationEquality() {
		TemporalAmount durationFromTicks = tick.getDuration();
		Duration expectedDuration = Duration.ofMillis(TICK_DURATION_MS);
		assertEquals(expectedDuration, durationFromTicks, "Tick duration should equal 50ms.");
	}

	@Test
	void testAddToEdgeCases() {
		Instant now = Instant.now();
		Temporal resultZero = tick.addTo(now, 0); // Add 0 ticks
		assertEquals(now, resultZero, "Adding 0 ticks should not change the temporal.");

		Temporal resultLarge = tick.addTo(now, Long.MAX_VALUE / TICK_DURATION_MS);
		assertNotNull(resultLarge, "Adding a large number of ticks should not throw an exception.");
	}

	@Test
	void testBetweenLargeDifference() {
		Instant start = Instant.EPOCH;
		Instant end = start.plus(Long.MAX_VALUE / TICK_DURATION_MS, ChronoUnit.MILLIS);
		long ticksBetween = tick.between(start, end);
		assertTrue(ticksBetween > 0, "Large differences in time should yield valid tick counts.");
	}
}
