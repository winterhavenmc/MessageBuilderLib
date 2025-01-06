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

package com.winterhavenmc.util.messagebuilder.query;

import com.winterhavenmc.util.TimeUnit;
import com.winterhavenmc.util.messagebuilder.namespace.Namespace;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.*;

class TimeQueryHandlerTest {

	TimeQueryHandler timeQueryHandler;

	@BeforeEach
	void setUp() {

		ConfigurationSection timeSection = new MemoryConfiguration();

		timeSection.set("UNLIMITED", "unlimited time");
		timeSection.set("OTHER.LESS_THAN_ONE", "less than one");
		timeSection.set("OTHER.LESS_THAN", "less than");
		timeSection.set("TICKS.SINGULAR", "tick");
		timeSection.set("TICKS.PLURAL", "ticks");
		timeSection.set("MILLISECONDS.SINGULAR", "millisecond");
		timeSection.set("MILLISECONDS.PLURAL", "milliseconds");
		timeSection.set("SECONDS.SINGULAR", "second");
		timeSection.set("SECONDS.PLURAL", "seconds");
		timeSection.set("MINUTES.SINGULAR", "minute");
		timeSection.set("MINUTES.PLURAL", "minutes");
		timeSection.set("HOURS.SINGULAR", "hour");
		timeSection.set("HOURS.PLURAL", "hours");
		timeSection.set("DAYS.SINGULAR", "day");
		timeSection.set("DAYS.PLURAL", "days");
		timeSection.set("WEEKS.SINGULAR", "week");
		timeSection.set("WEEKS.PLURAL", "weeks");
		timeSection.set("MONTHS.SINGULAR", "month");
		timeSection.set("MONTHS.PLURAL", "months");
		timeSection.set("YEARS.SINGULAR", "year");
		timeSection.set("YEARS.PLURAL", "years");

		timeQueryHandler = new TimeQueryHandler(timeSection);
	}

	@AfterEach
	void tearDown() {
		timeQueryHandler = null;
	}

	@Test
	void testTimeQueryHandler() {
		assertNotNull(timeQueryHandler);
	}

	@Test
	void testGetLessThanOne() {
		assertEquals("less than one", timeQueryHandler.getLessThanOne());
		assertNotEquals("not less than one", timeQueryHandler.getLessThanOne());
	}

	@Test
	void testGetLessThan() {
		assertEquals("less than", timeQueryHandler.getLessThan());
		assertNotEquals("not less than", timeQueryHandler.getLessThan());
	}

	@Test
	void testGetUnlimited() {
		assertEquals("unlimited time", timeQueryHandler.getUnlimited());
		assertNotEquals("not unlimited time", timeQueryHandler.getUnlimited());
	}

	@ParameterizedTest
	@EnumSource
	void testGetSingular(Namespace.Time.Unit namespaceTimeUnit) {
		// Arrange
		TimeUnit match = match(namespaceTimeUnit);

		assertEquals(namespaceTimeUnit.getSingular(), timeQueryHandler.getSingular(match));
		assertNotEquals(namespaceTimeUnit.getPlural(), timeQueryHandler.getSingular(match));
	}

	@ParameterizedTest
	@EnumSource
	void testGetPlural(Namespace.Time.Unit namespaceTimeUnit) {
		// Arrange
		TimeUnit match = match(namespaceTimeUnit);

		// Act & Assert
		assertEquals(namespaceTimeUnit.getPlural(), timeQueryHandler.getPlural(match));
		assertNotEquals(namespaceTimeUnit.getSingular(), timeQueryHandler.getPlural(match));
	}

	@ParameterizedTest
	@EnumSource
	void testGetPluralized_one(Namespace.Time.Unit namespaceTimeUnit) {
		// Arrange
		TimeUnit match = match(namespaceTimeUnit);

		// Act & Assert
		assertEquals(namespaceTimeUnit.getSingular(), timeQueryHandler.getPluralized(match.one(), match));
		assertNotEquals(namespaceTimeUnit.getPlural(), timeQueryHandler.getPluralized(match.one(), match));
	}

	@ParameterizedTest
	@EnumSource
	void tstGetPluralized_DAY_just_shy_of(Namespace.Time.Unit namespaceTimeUnit) {
		// Arrange
		TimeUnit match = match(namespaceTimeUnit);

		// Act & Assert
		assertEquals(namespaceTimeUnit.getPlural(), timeQueryHandler.getPluralized(match.justShyOf(0), match));
		assertEquals(namespaceTimeUnit.getPlural(), timeQueryHandler.getPluralized(match.justShyOf(1), match));
		//TODO: Just shy of two (2) gives undetermined output depending on TimeUnit; 2 will always fail for millis
		assertEquals(namespaceTimeUnit.getPlural(), timeQueryHandler.getPluralized(match.justShyOf(3), match));
		assertNotEquals(namespaceTimeUnit.getPlural(), timeQueryHandler.getPluralized(match.one(), match));
	}

	@ParameterizedTest
	@EnumSource
	void testGetPluralized_DAY_times_x(Namespace.Time.Unit namespaceTimeUnit) {
		// Arrange
		TimeUnit match = match(namespaceTimeUnit);

		// Act & Assert
		assertEquals(namespaceTimeUnit.getPlural(), timeQueryHandler.getPluralized(match.times(0), match));
		assertNotEquals(namespaceTimeUnit.getPlural(), timeQueryHandler.getPluralized(match.times(1), match));
		assertEquals(namespaceTimeUnit.getPlural(), timeQueryHandler.getPluralized(match.times(2), match));
		assertEquals(namespaceTimeUnit.getPlural(), timeQueryHandler.getPluralized(match.times(3), match));
	}

	@Test
	void testGetTimeString() {
		// Arrange
		long duration = TimeUnit.DAYS.times(2) + TimeUnit.HOURS.times(4) + TimeUnit.MINUTES.times(18);

		// Act
		String resultString = timeQueryHandler.getTimeString(duration, TimeUnit.MINUTES);

		// Assert
		assertEquals("2 days, 4 hours, 18 minutes", resultString);
	}


	@Test
	void testGetTimeString2() {
		// Arrange
		long duration = TimeUnit.DAYS.times(1);

		// Act
		String resultString = timeQueryHandler.getTimeString(duration, TimeUnit.MINUTES);

		// Assert
		assertEquals("1 day", resultString);
	}


	@Test
	void testGetTimeString3() {
		// Arrange
		long duration = TimeUnit.DAYS.justShyOf(1);

		// Act
		String resultString = timeQueryHandler.getTimeString(duration, TimeUnit.SECONDS);

		// Assert
		assertEquals("23 hours, 59 minutes, 59 seconds", resultString);
	}


	@Test
	void testGetTimeString4() {
		// Arrange
		long duration = TimeUnit.DAYS.justShyOf(1);

		// Act
		String resultString = timeQueryHandler.getTimeString(duration, TimeUnit.MINUTES);

		// Assert
		assertEquals("23 hours, 59 minutes", resultString);
	}


	@Test
	void testGetTimeString5() {
		// Arrange
		long duration = TimeUnit.MINUTES.justShyOf(1);

		// Act
		String resultString = timeQueryHandler.getTimeString(duration, TimeUnit.MINUTES);

		// Assert
		assertEquals("less than one minute", resultString);
	}


	@ParameterizedTest
	@EnumSource
	void testGetTimeString_unlimited(TimeUnit timeUnit) {
		// Arrange
		long duration = -1;

		// Act
		String resultString = timeQueryHandler.getTimeString(duration, timeUnit);

		// Assert
		assertEquals("unlimited time", resultString);
	}


	@ParameterizedTest
	@EnumSource
	void testGetLessThanOne(TimeUnit timeUnit) {
		// Arrange
		long duration = timeUnit.justShyOf(1);

		// Act
		String resultString = timeQueryHandler.getLessThanOne(timeUnit);

		// Assert
		assertEquals("less than one " + timeQueryHandler.getSingular(timeUnit), resultString);
	}


	@Test
	void testGetLessThanOne_null_parameter() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> timeQueryHandler.getLessThanOne(null));
		assertEquals("The timeUnit parameter cannot be null.", exception.getMessage());
	}


	@Test
	void testGetTimeString_null_duration() {
		// Arrange & Act
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> timeQueryHandler.getTimeString(null, TimeUnit.MINUTES) );

		// Assert
		assertEquals("The duration parameter cannot be null.", exception.getMessage());
	}


	@Test
	void testGetTimeString_null_timeUnit() {
		// Arrange & Act
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> timeQueryHandler.getTimeString(TimeUnit.MINUTES.one(), null) );

		// Assert
		assertEquals("The timeUnit parameter cannot be null.", exception.getMessage());
	}


	private static TimeUnit match(Namespace.Time.Unit unit) {
		return TimeUnit.valueOf(unit.name());
	}
}
