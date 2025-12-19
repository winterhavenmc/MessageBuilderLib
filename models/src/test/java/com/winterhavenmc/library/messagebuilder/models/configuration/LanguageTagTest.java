package com.winterhavenmc.library.messagebuilder.models.configuration;

import org.junit.jupiter.api.Test;

import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


class LanguageTagTest
{
	@Test
	void of_returns_optional_LangageTag_given_valid_locale()
	{
		// Act
		Optional<LanguageTag> result = LanguageTag.of(Locale.US);

		// Assert
		assertTrue(result.isPresent());
		assertEquals("en-US", result.get().toString());
	}


	@Test
	void of_returns_empty_optional_given_null_locale()
	{
		// Act
		Optional<LanguageTag> result = LanguageTag.of((Locale) null);

		// Assert
		assertTrue(result.isEmpty());
	}


	@Test
	void of_returns_optional_LanguageTag_given_valid_locale_string()
	{
		// Act
		Optional<LanguageTag> result = LanguageTag.of("en-US");

		// Assert
		assertTrue(result.isPresent());
		assertEquals(Locale.US, result.get().getLocale());
	}


	@Test
	void of_returns_empty_optional_given_null_locale_string()
	{
		// Act
		Optional<LanguageTag> result = LanguageTag.of((String) null);

		// Assert
		assertTrue(result.isEmpty());
	}


	@Test
	void of_returns_empty_optional_given_invalid_locale_string()
	{
		// Act
		Optional<LanguageTag> result = LanguageTag.of("not-a-valid-locale-string");

		// Assert
		assertTrue(result.isEmpty());
	}


	@Test
	void isValid_returns_true_given_valid_LanguageTag_string()
	{
		// Act
		boolean result = LanguageTag.isValid("en-US");

		// Assert
		assertTrue(result);
	}


	@Test
	void isValid_returns_false_given_null_LanguageTag_string()
	{
		// Act
		boolean result = LanguageTag.isValid(null);

		// Assert
		assertFalse(result);
	}


	@Test
	void isValid_returns_false_given_empty_LanguageTag_string()
	{
		// Act
		boolean result = LanguageTag.isValid("");

		// Assert
		assertFalse(result);
	}


	@Test
	void isValid_returns_true_given_language_string_in_hardcoded_exceptions_list()
	{
		// Act
		boolean result = LanguageTag.isValid("haw");

		// Assert
		assertTrue(result);
	}


	@Test
	void isValid_returns_false_given_invalid_LanguageTag_string()
	{
		// Act
		boolean result = LanguageTag.isValid("not-a-valid-locale-streing");

		// Assert
		assertFalse(result);
	}


	@Test
	void isValid_returns_false_given_language_string_in_hardcoded_rejections_list()
	{
		// Act
		boolean result = LanguageTag.isValid("und");

		// Assert
		assertFalse(result);
	}


	@Test
	void getSystemDefault_returns_system_default_locale()
	{
		// Act
		LanguageTag result = LanguageTag.getSystemDefault();

		// Assert
		assertEquals(LanguageTag.of(Locale.getDefault()).get(), result);
	}


	@Test
	void getLocale_returns_valid_locale()
	{
		// Arrange
		LanguageTag languageTag = LanguageTag.of(Locale.US).orElseThrow();

		// Act
		Locale result = languageTag.getLocale();

		// Assert
		assertEquals(Locale.US, result);
	}


	@Test
	void toString_returns_string_representation()
	{
		// Arrange
		LanguageTag languageTag = LanguageTag.of(Locale.US).orElseThrow();

		// Act
		String result = languageTag.toString();

		// Assert
		assertEquals("en-US", result);
	}


	@Test
	void equals_returns_true_given_equal_language_tags()
	{
		// Arrange
		LanguageTag languageTag1 = LanguageTag.of(Locale.US).orElseThrow();
		LanguageTag languageTag2 = LanguageTag.of("en-US").orElseThrow();

		// Act
		boolean result = languageTag1.equals(languageTag2);

		// Assert
		assertTrue(result);
	}


	@Test
	void equals_returns_false_given_unequal_language_tags()
	{
		// Arrange
		LanguageTag languageTag1 = LanguageTag.of(Locale.US).orElseThrow();
		LanguageTag languageTag2 = LanguageTag.of(Locale.FRANCE).orElseThrow();

		// Act
		boolean result = languageTag1.equals(languageTag2);

		// Assert
		assertFalse(result);
	}


	@Test
	void hashcode_returns_correct_hashcode()
	{
		// Arrange
		LanguageTag languageTag = LanguageTag.of(Locale.US).orElseThrow();

		// Act
		var result = languageTag.hashCode();

		assertEquals("en-US".hashCode(), result);
	}

}
