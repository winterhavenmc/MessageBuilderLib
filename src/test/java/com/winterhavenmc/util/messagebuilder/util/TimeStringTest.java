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

package com.winterhavenmc.util.messagebuilder.util;

import com.winterhavenmc.util.TimeUnit;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.MemoryConfiguration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TimeStringTest {

	private Configuration configuration;
	private TimeString timeString;

	@BeforeEach
	void setUp() {
		configuration = new MemoryConfiguration();
		configuration.set("TIME_STRINGS.UNLIMITED", "unlimited");
		configuration.set("TIME_STRINGS.LESS_THAN_ONE", "< 1");
		configuration.set("TIME_STRINGS.DAY", "day");
		configuration.set("TIME_STRINGS.DAY_PLURAL", "days");
		configuration.set("TIME_STRINGS.HOUR", "hour");
		configuration.set("TIME_STRINGS.HOUR_PLURAL", "hours");
		configuration.set("TIME_STRINGS.MINUTE", "minute");
		configuration.set("TIME_STRINGS.MINUTE_PLURAL", "minutes");
		configuration.set("TIME_STRINGS.SECOND", "second");
		configuration.set("TIME_STRINGS.SECOND_PLURAL", "seconds");



		//		timeString = new TimeString(configuration);
	}



	@Test
	void testNegativeDuration() {
		assertEquals("unlimited", timeString.getTimeString(-1, TimeUnit.SECONDS));
	}

	@Test
	void testLessThanOneDay() {
		assertEquals("< 1 day", timeString.getTimeString(86399_000L, TimeUnit.DAYS)); // Just under one day
	}

	@Test
	void testLessThanOneHour() {
		assertEquals("< 1 hour", timeString.getTimeString(3599_000L, TimeUnit.HOURS)); // Just under one hour
	}

	@Test
	void testLessThanOneMinute() {
		assertEquals("< 1 minute", timeString.getTimeString(59_000L, TimeUnit.MINUTES)); // Just under one minute
	}

	@Test
	void testLessThanOneSecond() {
		assertEquals("< 1 second", timeString.getTimeString(999L, TimeUnit.SECONDS)); // Just under one second
	}

	@Test
	void testExactDurations() {
		assertEquals("1 day", timeString.getTimeString(86400_000L, TimeUnit.DAYS)); // Exactly one day
		assertEquals("1 hour", timeString.getTimeString(3600_000L, TimeUnit.HOURS)); // Exactly one hour
		assertEquals("1 minute", timeString.getTimeString(60_000L, TimeUnit.MINUTES)); // Exactly one minute
		assertEquals("1 second", timeString.getTimeString(1_000L, TimeUnit.SECONDS)); // Exactly one second
	}


	@Test
	void testCompositeDuration() {
		// 1 day, 2 hours, 3 minutes, 4 seconds
		long duration = (1 * 86_400_000L) + (2 * 3_600_000L) + (3 * 60_000L) + (4 * 1_000L);
		assertEquals("1 day 2 hours 3 minutes 4 seconds", timeString.getTimeString(duration, TimeUnit.SECONDS));
	}

	@Test
	void testGranularity() {
		// Same as above, but only up to hours
		long duration = (1 * 86_400_000L) + (2 * 3_600_000L) + (3 * 60_000L) + (4 * 1_000L);
		assertEquals("1 day 2 hours", timeString.getTimeString(duration, TimeUnit.HOURS));
	}


	@Test
	void testMissingConfigurationValues() {
		configuration.set("TIME_STRINGS.HOUR", null); // Simulate missing configuration
		assertEquals("1 hours", timeString.getTimeString(3600_000L, TimeUnit.HOURS)); // Falls back to default
	}

	@Test
	void testZeroDuration() {
		assertEquals("< 1 second", timeString.getTimeString(0L, TimeUnit.SECONDS));
	}

	@Test
	void testHighDuration() {
		// Example: 10 days, 5 hours, 30 minutes, 45 seconds
		long duration = (10 * 86_400_000L) + (5 * 3_600_000L) + (30 * 60_000L) + (45 * 1_000L);
		assertEquals("10 days 5 hours 30 minutes 45 seconds", timeString.getTimeString(duration, TimeUnit.SECONDS));
	}


}