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

package com.winterhavenmc.util.messagebuilder.formatters.duration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;


class DurationTypeTest
{
	// -----------------------------
	// Tests for classify(...)
	// -----------------------------

	@ParameterizedTest(name = "classify({0}, {1}) == {2}")
	@MethodSource("classifyCases")
	void testClassify(Duration input, ChronoUnit precision, DurationType expected) {
		assertEquals(expected, DurationType.classify(input, precision));
	}

	static Stream<Arguments> classifyCases()
	{
		return Stream.of(
				Arguments.of(null, ChronoUnit.MINUTES, DurationType.LESS_THAN),
				Arguments.of(Duration.ofSeconds(-5), ChronoUnit.SECONDS, DurationType.UNLIMITED),
				Arguments.of(Duration.ofSeconds(0), ChronoUnit.SECONDS, DurationType.LESS_THAN),
				Arguments.of(Duration.ofMillis(500), ChronoUnit.SECONDS, DurationType.LESS_THAN),
				Arguments.of(Duration.ofSeconds(1), ChronoUnit.SECONDS, DurationType.NORMAL),
				Arguments.of(Duration.ofMinutes(5), ChronoUnit.MINUTES, DurationType.NORMAL)
		);
	}


	// -----------------------------
	// Tests for isUnlimited(...)
	// -----------------------------

	@ParameterizedTest(name = "isUnlimited({0}) == {1}")
	@MethodSource("unlimitedCases")
	void testIsUnlimited(Duration input, boolean expected) {
		assertEquals(expected, DurationType.isUnlimited(input));
	}

	static Stream<Arguments> unlimitedCases()
	{
		return Stream.of(
				Arguments.of(null, false),
				Arguments.of(Duration.ofSeconds(0), false),
				Arguments.of(Duration.ofSeconds(-1), true),
				Arguments.of(Duration.ofMillis(-500), true),
				Arguments.of(Duration.ofSeconds(1), false)
		);
	}

	// -----------------------------
	// Tests for isLessThan(...)
	// -----------------------------

	@ParameterizedTest(name = "isLessThan({0}, {1}) == {2}")
	@MethodSource("lessThanCases")
	void testIsLessThan(Duration input, ChronoUnit precision, boolean expected) {
		assertEquals(expected, DurationType.isLessThan(input, precision));
	}

	static Stream<Arguments> lessThanCases() {
		return Stream.of(
				Arguments.of(null, ChronoUnit.SECONDS, false),
				Arguments.of(Duration.ofSeconds(-1), ChronoUnit.SECONDS, false), // Negative = UNLIMITED
				Arguments.of(Duration.ofMillis(500), ChronoUnit.SECONDS, true),
				Arguments.of(Duration.ofSeconds(1), ChronoUnit.SECONDS, false),
				Arguments.of(Duration.ofMinutes(1), ChronoUnit.MINUTES, false),
				Arguments.of(Duration.ofSeconds(59), ChronoUnit.MINUTES, true)
		);
	}

	@Test
	void testFallBack()
	{
		assertEquals("", DurationType.NORMAL.getFallback());
		assertEquals("< {DURATION}", DurationType.LESS_THAN.getFallback());
		assertEquals("unlimited", DurationType.UNLIMITED.getFallback());
	}
}
