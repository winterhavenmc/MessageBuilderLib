/*
 * Copyright (c) 2022 Tim Savage.
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

import com.winterhavenmc.util.messagebuilder.query.YamlLanguageQueryHandler;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;


@ExtendWith(MockitoExtension.class)
class ProcessorRegistryTest {

	@Mock
	YamlLanguageQueryHandler queryHandler;
	ProcessorRegistry processorRegistry;

	@BeforeEach
	public void setUp() {
		processorRegistry = new ProcessorRegistry();
	}

	@AfterEach
	public void tearDown() {
		processorRegistry = null;
	}


	@Test
	void testPut() {
		// Arrange & Act
		processorRegistry.put(ProcessorType.STRING, ProcessorType.STRING.create(queryHandler));

		// Assert
		assertNotNull(processorRegistry.get(ProcessorType.STRING));
	}

	@Test
	void tstGet() {
		// Arrange & Act
		processorRegistry.put(ProcessorType.STRING, ProcessorType.STRING.create(queryHandler));

		// Assert
		assertNotNull(processorRegistry.get(ProcessorType.STRING));
	}

}
