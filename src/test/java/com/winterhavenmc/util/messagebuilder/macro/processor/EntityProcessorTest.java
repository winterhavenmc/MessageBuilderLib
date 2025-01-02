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

import com.winterhavenmc.util.messagebuilder.query.LanguageFileQueryHandler;
import org.junit.jupiter.api.*;

import static org.mockito.Mockito.mock;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EntityProcessorTest {

	private LanguageFileQueryHandler mockLanguageFileQueryHandler;
	private MacroProcessor macroProcessor;


	@BeforeAll
	public void setUp() {
		LanguageFileQueryHandler mockLanguageFileQueryHandler = mock(LanguageFileQueryHandler.class, "MockQueryHandler");

		macroProcessor = new EntityProcessor(mockLanguageFileQueryHandler);
	}

	@AfterAll
	public void tearDown() {
		mockLanguageFileQueryHandler = null;
		macroProcessor = null;
	}

	@Test
	void resolveContext() { }

}
