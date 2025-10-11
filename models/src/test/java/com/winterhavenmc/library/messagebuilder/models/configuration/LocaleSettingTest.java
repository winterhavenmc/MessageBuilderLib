package com.winterhavenmc.library.messagebuilder.models.configuration;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class LocaleSettingTest
{

	@Test
	void locale_with_valid_language_tag_returns_valid_locale()
	{
		// Arrange
		LanguageTag languageTag = LanguageTag.of("es-ES").orElseThrow();

		// Act
		LocaleSetting result = new LocaleSetting(languageTag);

		// Assert
		assertEquals(languageTag.getLocale(), result.locale());
	}

}
