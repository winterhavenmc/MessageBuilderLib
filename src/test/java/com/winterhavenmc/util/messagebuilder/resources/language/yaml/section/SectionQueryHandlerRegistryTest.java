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

package com.winterhavenmc.util.messagebuilder.resources.language.yaml.section;

import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.constants.ConstantSectionQueryHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;

class SectionQueryHandlerRegistryTest {

	@Mock ConstantSectionQueryHandler queryHandlerMock;

	SectionQueryHandlerRegistry registry;

	@BeforeEach
	void setUp() {
		registry = new SectionQueryHandlerRegistry();
	}

	@AfterEach
	void tearDown() {
		registry = null;
	}

	@Test
	void registerQueryHandler() {
		// Arrange
		registry.registerQueryHandler(Section.CONSTANTS, queryHandlerMock);

		// Act & Assert
		assertTrue(registry.hasQueryHandler(Section.CONSTANTS));
	}

	@Test
	void registerQueryHandler_already_registered() {
		// Arrange
		registry.registerQueryHandler(Section.CONSTANTS, queryHandlerMock);

		// Act
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> registry.registerQueryHandler(Section.CONSTANTS, queryHandlerMock));

		// Assert
		assertEquals("Handler already registered for section: CONSTANTS", exception.getMessage());
	}

	@Test
	void getQueryHandler() {
		// Arrange
		registry.registerQueryHandler(Section.CONSTANTS, queryHandlerMock);

		// Act
		var queryHandler = registry.getQueryHandler(Section.CONSTANTS);

		// Assert
		assertEquals(queryHandlerMock, queryHandler);
	}

	@Test
	void getQueryHandler_parameter_null() {
		// Arrange & Act
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				()-> registry.getQueryHandler(null));

		// Assert
		assertEquals("The section parameter cannot be null.", exception.getMessage());
	}

	@Test
	void hasQueryHandler() {
		// Arrange
		registry.registerQueryHandler(Section.CONSTANTS, queryHandlerMock);

		// Act & Assert
		assertTrue(registry.hasQueryHandler(Section.CONSTANTS));
	}

	@Test
	void clearQueryHandlers() {
		// Arrange
		registry.registerQueryHandler(Section.CONSTANTS, queryHandlerMock);
		assertTrue(registry.hasQueryHandler(Section.CONSTANTS));

		// Act & Assert
		registry.clearQueryHandlers();

		assertFalse(registry.hasQueryHandler(Section.CONSTANTS));
	}

}
