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

import com.winterhavenmc.util.messagebuilder.resources.language.yaml.YamlLanguageQueryHandler;
import com.winterhavenmc.util.messagebuilder.util.LocalizedException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class ProcessorRegistryTest {

	@Mock
	YamlLanguageQueryHandler queryHandler;
	ProcessorRegistry processorRegistry;

	@BeforeEach
	public void setUp() {
		processorRegistry = new ProcessorRegistry(new DependencyContext());
	}

	@AfterEach
	public void tearDown() {
		processorRegistry = null;
	}


	@Nested
	class ConstructorTests {
		@Test
		void testConstructor_parameter_valid() {
			ProcessorRegistry registry = new ProcessorRegistry(new DependencyContext());

			assertNotNull(registry);
		}

		@Test
		void testConstructor_parameter_null() {
			LocalizedException exception = assertThrows(LocalizedException.class,
					() -> new ProcessorRegistry(null));

			assertEquals("The parameter 'context' cannot be null.", exception.getMessage());
		}
	}


	@Test
	void tstGet() {
		// Assert
		assertNotNull(processorRegistry.get(ProcessorType.STRING));
	}

}
