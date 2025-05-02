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

package com.winterhavenmc.library.messagebuilder.validation;

import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Predicate;


/**
 * Provides utilities for validating and parsing language tags into supported JVM {@link Locale} instances.
 * <p>
 * This class ensures that the provided language tag:
 * <ul>
 *     <li>Is not null or blank</li>
 *     <li>Does not resolve to the special "und" (undefined) tag</li>
 *     <li>Has a recognized ISO 639 language code</li>
 * </ul>
 * Tags that fail any of these conditions are considered invalid.
 */
public final class LocaleValidator
{
	private LocaleValidator() { /* private constructor to prevent instantiation of utility class */ }


	/**
	 * Predicate that returns {@code true} if the input string resolves to a valid ISO 639 language
	 * code recognized by the JVM. Rejects null, blank, "und", or non-existent tags.
	 */
	public static final Predicate<String> VALID_LOCALE = string -> {
		if (string == null || string.isBlank()) return false;

		Locale locale = Locale.forLanguageTag(string);
		String language = locale.getLanguage();

		if (language.isEmpty() || "und".equalsIgnoreCase(language)) return false;

		// Ensure the language code is one of the official ISO 639 codes
		return Arrays.asList(Locale.getISOLanguages()).contains(language);
	};


	/**
	 * Attempts to parse a supported {@link Locale} from a language tag string.
	 * <p>
	 * Returns {@link Optional#empty()} if the input is null, blank, invalid,
	 * or corresponds to an unrecognized or undefined language.
	 *
	 * @param languageTag the BCP 47 language tag to validate and parse
	 * @return an Optional containing a valid Locale, or empty if invalid
	 */
	public static Optional<Locale> parseSupportedLocale(final String languageTag) {
		return VALID_LOCALE.test(languageTag)
				? Optional.of(Locale.forLanguageTag(languageTag))
				: Optional.empty();
	}

}
