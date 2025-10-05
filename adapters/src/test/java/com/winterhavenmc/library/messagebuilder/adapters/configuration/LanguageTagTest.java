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

package com.winterhavenmc.library.messagebuilder.adapters.configuration;

import com.winterhavenmc.library.messagebuilder.configuration.LanguageTag;
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

		@Test
		void testConstructor_string()
		{
			// Arrange
			LanguageTag languageTag = LanguageTag.of("en-US").orElseThrow();

			// Act
			String result = languageTag.toString();

			// Assert
			assertEquals("en-US", result);
		}

		@Test
		void testConstructor_string_invalid()
		{
			// Arrange && Act
			Optional<LanguageTag> languageTag = LanguageTag.of("NOT_A_VALID_LANGUAGE_TAG");

			// Assert
			assertTrue(languageTag.isEmpty());
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
	void testGetLocale()
	{
		// Arrange
		LanguageTag languageTag = LanguageTag.of(Locale.US).orElseThrow();

		// Act
		Locale locale = languageTag.getLocale();

		// Assert
		assertEquals(Locale.US, locale);
	}



	@Test
	void systemDefault_returnsNonNullNonBlankTag() {
		LanguageTag tag = LanguageTag.getSystemDefault();

		assertNotNull(tag, "getSystemDefault() should not return null");
		assertFalse(tag.toString().isBlank(), "LanguageTag should not be blank");
	}

	@Test
	void systemDefault_matchesActualLocaleToLanguageTag() {
		Locale systemLocale = Locale.getDefault();
		String expectedTag = systemLocale.toLanguageTag();

		LanguageTag actual = LanguageTag.getSystemDefault();

		assertEquals(expectedTag, actual.toString(), "LanguageTag should match system Locale");
	}

	@Test
	void systemDefault_respectsOverriddenLocale() {
		Locale originalDefault = Locale.getDefault();
		Locale testLocale = Locale.forLanguageTag("es-MX");

		try {
			Locale.setDefault(testLocale);

			LanguageTag tag = LanguageTag.getSystemDefault();
			assertEquals("es-MX", tag.toString(), "Overridden Locale should be reflected in LanguageTag");

		} finally {
			Locale.setDefault(originalDefault);
		}
	}

}
