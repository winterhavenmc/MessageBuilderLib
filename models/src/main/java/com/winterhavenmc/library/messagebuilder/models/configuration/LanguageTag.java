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

package com.winterhavenmc.library.messagebuilder.models.configuration;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Predicate;

import static java.util.Locale.forLanguageTag;


/**
 * A class that wraps a string-based IETF BCP 47 language tag, allowing for validation against locales that
 * are known to the system at runtime.
 *
 * <h2>Typical usage:</h2>
 * From string:
 * {@snippet lang="java":
 *  LanguageTag tag = LanguageTag.of("en-US");
 * }
 * From Locale:
 * {@snippet lang="java":
 *  LanguageTag tag = LanguageTag.of(Locale.US);
 * }
 *
 * @see Locale
 */
public final class LanguageTag
{
	private final String wrappedLanguageTag;


	/**
	 * Class constructor that takes a Section language tag as a parameter
	 *
	 * @param languageTag a {@code Section} representing a potential language resource
	 */
	private LanguageTag(@NotNull final String languageTag)
	{
		this.wrappedLanguageTag = languageTag;
	}


	/**
	 * Static factory method that creates a new LanguageTag from a Locale
	 *
	 * @param locale the Locale to use in the creation of a new LanguageTag
	 * @return Optional of a LanguageTag if valid, otherwise an empty Optional
	 */
	@Contract("_->!null")
	public static Optional<LanguageTag> of(final Locale locale)
	{
		return (locale != null)
				? Optional.of(new LanguageTag(locale.toLanguageTag()))
				: Optional.empty();
	}


	/**
	 * Static factory method that creates a new validated LanguageTag from a Section
	 *
	 * @param string the string to use in the creation of a new LanguageTag
	 * @return Optional of a LanguageTag if valid, otherwise an empty Optional
	 */
	@Contract("_->!null")
	public static Optional<LanguageTag> of(final String string)
	{
		return (VALID_LOCALE.test(string))
				? Optional.of(new LanguageTag(Locale.forLanguageTag(string).toLanguageTag()))
				: Optional.empty();
	}


	/**
	 * Predicate that evaluates to {@code true} if the input string resolves to a valid ISO 639 language
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
	 * Get the system default locale expressed as a LanguageTag
	 *
	 * @return a new default LanguageTag
	 */
	@Contract(" -> new")
	public static @NotNull LanguageTag getSystemDefault()
	{
		return new LanguageTag(Locale.getDefault().toLanguageTag());
	}


	/**
	 * Retrieve the {@link Locale} associated with this language tag
	 *
	 * @return the {@code Locale} associated with this language tag
	 */
	@Contract(pure = true)
	public Locale getLocale()
	{
		return forLanguageTag(wrappedLanguageTag);
	}


	/**
	 * Retrieve the IETF language tag associated with a potential language resource, as a Section
	 *
	 * @return {@code Section} representing the IETF language tag associated with a potential language resource
	 */
	@Override @NotNull @Contract(pure = true)
	public String toString()
	{
		return this.wrappedLanguageTag;
	}

}
