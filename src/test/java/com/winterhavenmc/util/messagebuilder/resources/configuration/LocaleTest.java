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

package com.winterhavenmc.util.messagebuilder.resources.configuration;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LocaleTest
{
	@Test
	void testMockLocaleForLanguageTag()
	{
		try (MockedStatic<Locale> mockedLocale = mockStatic(Locale.class))
		{
			// Define the behavior for the static method
			mockedLocale.when(() -> Locale.forLanguageTag("en-US"))
					.thenReturn(Locale.US);

			// Test the mocked behavior
			Locale result = Locale.forLanguageTag("en-US");
			assertEquals(Locale.US, result);

			// Verify that the static method was called
			mockedLocale.verify(() -> Locale.forLanguageTag("en-US"));
		}
	}
}
