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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


class LanguageTagTest
{
	@Nested @DisplayName("Test static factory methods")
	class ConstructorTests
	{
		@Test @DisplayName("with valid parameter locale")
		void testConstructor_locale()
		{
			// Arrange
			LanguageTag languageTag = LanguageTag.of(Locale.US).orElseThrow();

			// Act
			String result = languageTag.toString();

			// Assert
			assertEquals("en-US", result);
		}


		@Test @DisplayName("with null parameter locale")
		void testConstructor_null_parameter_locale()
		{
			// Arrange & Act
			Optional<LanguageTag> result = LanguageTag.of((Locale) null);

			// Assert
			assertTrue(result.isEmpty());
		}
	}


	@Test
	void testToString()
	{
		// Arrange
		LanguageTag languageTag = LanguageTag.of(Locale.US).orElseThrow();

		// Act & Assert
		assertEquals("en-US", languageTag.toString());
		assertNotEquals("fr-FR", languageTag.toString());
	}


	@Test
	void testGetLocale() {
		// Arrange
		LanguageTag languageTag = LanguageTag.of(Locale.US).orElseThrow();

		// Act
		Locale locale = languageTag.getLocale();

		// Assert
		assertEquals(Locale.US, locale);
	}

}
