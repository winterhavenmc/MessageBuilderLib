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

package com.winterhavenmc.util.messagebuilder.macro.processor;

import com.winterhavenmc.util.messagebuilder.context.ContextContainer;
import com.winterhavenmc.util.messagebuilder.context.ContextMap;
import com.winterhavenmc.util.messagebuilder.resources.language.LanguageQueryHandler;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class NullProcessorTest {

	@Mock LanguageQueryHandler queryHandler;
	@Mock Player playerMock;

	@BeforeEach
	void setUp() {
	}

	@AfterEach
	void tearDown() {
	}

	@Test
	void testConstructor_parameter_valid() {
		// Arrange & Act
		NullProcessor processor = new NullProcessor(queryHandler);

		// Assert
		assertNotNull(processor);
	}

	@Test
	void testConstructor_parameter_null() {
		// Arrange & Act
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> new NullProcessor(null));

		// Assert
		assertEquals("The queryHandler parameter was null.", exception.getMessage());
	}

	@Test
	void resolveContext() {
		// Arrange
		NullProcessor nullProcessor = new NullProcessor(queryHandler);
		ContextMap contextMap = new ContextMap(playerMock);
		contextMap.put("KEY", new ContextContainer<>(null, ProcessorType.NULL));

		// Act
		ResultMap resultMap = nullProcessor.resolveContext("KEY", contextMap, null);

		// Assert
		assertEquals("NULL", resultMap.get("KEY"));
	}

	@Test
	void resolveContext_not_null() {
		// Arrange
		NullProcessor nullProcessor = new NullProcessor(queryHandler);
		ContextMap contextMap = new ContextMap(playerMock);
		contextMap.put("KEY", new ContextContainer<>("not_null", ProcessorType.NULL));

		// Act
		ResultMap resultMap = nullProcessor.resolveContext("KEY", contextMap, "not_null");

		// Assert
		assertTrue(resultMap.isEmpty());
	}

}
