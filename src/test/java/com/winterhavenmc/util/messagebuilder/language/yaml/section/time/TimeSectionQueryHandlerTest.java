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

package com.winterhavenmc.util.messagebuilder.language.yaml.section.time;

import com.winterhavenmc.util.TimeUnit;
import com.winterhavenmc.util.messagebuilder.language.yaml.YamlConfigurationSupplier;
import com.winterhavenmc.util.messagebuilder.util.Namespace;
import com.winterhavenmc.util.messagebuilder.util.MockUtility;

import org.bukkit.configuration.Configuration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;


class TimeSectionQueryHandlerTest {

	YamlConfigurationSupplier configurationSupplier;
	TimeSectionQueryHandler queryHandler;

	@BeforeEach
	void setUp() {
		Configuration configuration = MockUtility.loadConfigurationFromResource("language/en-US.yml");
		configurationSupplier = new YamlConfigurationSupplier(configuration);
		queryHandler = new TimeSectionQueryHandler(configurationSupplier);
	}

	@AfterEach
	void tearDown() {
		queryHandler = null;
	}


	@Test
	void testConstructor_parameter_null() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> new TimeSectionQueryHandler(null));
		assertEquals("The configurationSection parameter was null.", exception.getMessage());
	}

	@Test
	void testConstructor_parameter_invalid() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> new TimeSectionQueryHandler(configurationSupplier));
		assertEquals("The timeSection parameter was an invalid 'TIME' section.", exception.getMessage());
	}

	@Test
	void testTimeQueryHandler() {
		assertNotNull(queryHandler);
	}

	@Test
	void testGetSectionType() {
		assertEquals("TIME", queryHandler.getSectionType().name());
	}

	@Test
	void getHandledType() {
		assertEquals(String.class, queryHandler.getHandledType());
	}

	@Test
	void listHandledTypes() {
		assertEquals(List.of(String.class), queryHandler.listHandledTypes());
	}

	@Test
	void testGetLessThanOneString_minute() {
		assertEquals("less than one minute", queryHandler.getLessThanOneString(TimeUnit.MINUTES));
	}

	@Test
	void testGetLessThanOneString_no_parameter() {
		assertEquals("less than one", queryHandler.getLessThanOneString());
		assertNotEquals("not less than one", queryHandler.getLessThanOneString());
	}

	@Test
	void testGetLessThan() {
		assertEquals("less than", queryHandler.getLessThan());
		assertNotEquals("not less than", queryHandler.getLessThan());
	}

	@Test
	void testGetUnlimited() {
		assertEquals("unlimited time", queryHandler.getUnlimited());
		assertNotEquals("not unlimited time", queryHandler.getUnlimited());
	}

	@ParameterizedTest
	@EnumSource
	void testGetSingular(Namespace.Time.Unit namespaceTimeUnit) {
		// Arrange
		TimeUnit match = match(namespaceTimeUnit);

		assertEquals(namespaceTimeUnit.getSingular(), queryHandler.getSingular(match));
		assertNotEquals(namespaceTimeUnit.getPlural(), queryHandler.getSingular(match));
	}

	@ParameterizedTest
	@EnumSource
	void testGetPlural(Namespace.Time.Unit namespaceTimeUnit) {
		// Arrange
		TimeUnit match = match(namespaceTimeUnit);

		// Act & Assert
		assertEquals(namespaceTimeUnit.getPlural(), queryHandler.getPlural(match));
		assertNotEquals(namespaceTimeUnit.getSingular(), queryHandler.getPlural(match));
	}

	@ParameterizedTest
	@EnumSource
	void testGetPluralized_one(Namespace.Time.Unit namespaceTimeUnit) {
		// Arrange
		TimeUnit match = match(namespaceTimeUnit);

		// Act & Assert
		assertEquals(namespaceTimeUnit.getSingular(), queryHandler.getPluralized(match.one(), match));
		assertNotEquals(namespaceTimeUnit.getPlural(), queryHandler.getPluralized(match.one(), match));
	}

	@ParameterizedTest
	@EnumSource
	void tstGetPluralized_DAY_just_shy_of(Namespace.Time.Unit namespaceTimeUnit) {
		// Arrange
		TimeUnit match = match(namespaceTimeUnit);

		// Act & Assert
		assertEquals(namespaceTimeUnit.getPlural(), queryHandler.getPluralized(match.justShyOf(0), match));
		assertEquals(namespaceTimeUnit.getPlural(), queryHandler.getPluralized(match.justShyOf(1), match));
		//TODO: Just shy of two (2) gives undetermined output depending on TimeUnit; 2 will always fail for millis
		assertEquals(namespaceTimeUnit.getPlural(), queryHandler.getPluralized(match.justShyOf(3), match));
		assertNotEquals(namespaceTimeUnit.getPlural(), queryHandler.getPluralized(match.one(), match));
	}

	@ParameterizedTest
	@EnumSource
	void testGetPluralized_DAY_times_x(Namespace.Time.Unit namespaceTimeUnit) {
		// Arrange
		TimeUnit match = match(namespaceTimeUnit);

		// Act & Assert
		assertEquals(namespaceTimeUnit.getPlural(), queryHandler.getPluralized(match.times(0), match));
		assertNotEquals(namespaceTimeUnit.getPlural(), queryHandler.getPluralized(match.times(1), match));
		assertEquals(namespaceTimeUnit.getPlural(), queryHandler.getPluralized(match.times(2), match));
		assertEquals(namespaceTimeUnit.getPlural(), queryHandler.getPluralized(match.times(3), match));
	}

	@Test
	void testGetTimeString() {
		// Arrange
		long duration = TimeUnit.DAYS.times(2) + TimeUnit.HOURS.times(4) + TimeUnit.MINUTES.times(18);

		// Act
		String resultString = queryHandler.getTimeString(duration, TimeUnit.MINUTES);

		// Assert
		assertEquals("2 days, 4 hours, 18 minutes", resultString);
	}


	@Test
	void testGetTimeString2() {
		// Arrange
		long duration = TimeUnit.DAYS.times(1);

		// Act
		String resultString = queryHandler.getTimeString(duration, TimeUnit.MINUTES);

		// Assert
		assertEquals("1 day", resultString);
	}


	@Test
	void testGetTimeString3() {
		// Arrange
		long duration = TimeUnit.DAYS.justShyOf(1);

		// Act
		String resultString = queryHandler.getTimeString(duration, TimeUnit.SECONDS);

		// Assert
		assertEquals("23 hours, 59 minutes, 59 seconds", resultString);
	}


	@Test
	void testGetTimeString4() {
		// Arrange
		long duration = TimeUnit.DAYS.justShyOf(1);

		// Act
		String resultString = queryHandler.getTimeString(duration, TimeUnit.MINUTES);

		// Assert
		assertEquals("23 hours, 59 minutes", resultString);
	}


	@Test
	void testGetTimeString5() {
		// Arrange
		long duration = TimeUnit.MINUTES.justShyOf(1);

		// Act
		String resultString = queryHandler.getTimeString(duration, TimeUnit.MINUTES);

		// Assert
		assertEquals("less than one minute", resultString);
	}


	@ParameterizedTest
	@EnumSource
	void testGetTimeString_unlimited(TimeUnit timeUnit) {
		// Arrange
		long duration = -1;

		// Act
		String resultString = queryHandler.getTimeString(duration, timeUnit);

		// Assert
		assertEquals("unlimited time", resultString);
	}


	@ParameterizedTest
	@EnumSource
	void testGetLessThanOneString(TimeUnit timeUnit) {
		// Arrange & Act
		String resultString = queryHandler.getLessThanOneString(timeUnit);

		// Assert
		assertEquals("less than one " + queryHandler.getSingular(timeUnit), resultString);
	}


	@Test
	void testGetLessThanOne_String_null_parameter() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> queryHandler.getLessThanOneString(null));
		assertEquals("The timeUnit parameter cannot be null.", exception.getMessage());
	}


	@Test
	void testGetTimeString_null_duration() {
		// Arrange & Act
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> queryHandler.getTimeString(null, TimeUnit.MINUTES) );

		// Assert
		assertEquals("The duration parameter cannot be null.", exception.getMessage());
	}


	@Test
	void testGetTimeString_null_timeUnit() {
		// Arrange & Act
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> queryHandler.getTimeString(TimeUnit.MINUTES.one(), null) );

		// Assert
		assertEquals("The timeUnit parameter cannot be null.", exception.getMessage());
	}


	private static TimeUnit match(Namespace.Time.Unit unit) {
		return TimeUnit.valueOf(unit.name());
	}

}