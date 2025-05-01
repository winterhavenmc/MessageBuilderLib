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

package com.winterhavenmc.library.messagebuilder.resources.configuration;

import com.winterhavenmc.library.messagebuilder.validation.ValidationException;

import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

import static com.winterhavenmc.library.messagebuilder.validation.ErrorMessageKey.PARAMETER_EMPTY;
import static com.winterhavenmc.library.messagebuilder.validation.ErrorMessageKey.PARAMETER_NULL;
import static com.winterhavenmc.library.messagebuilder.validation.Parameter.LANGUAGE_TAG;
import static com.winterhavenmc.library.messagebuilder.validation.ValidationHandler.throwing;
import static com.winterhavenmc.library.messagebuilder.validation.Validator.validate;


public class LanguageTag
{
	private final String wrappedLanguageTag;


	/**
	 * Class constructor that takes a String language tag as a parameter
	 *
	 * @param string the language tag representing a potential language resource
	 * @throws ValidationException if parameter is null or empty
	 */
	private LanguageTag(final String string)
	{
		validate(string, Objects::isNull, throwing(PARAMETER_NULL, LANGUAGE_TAG));
		validate(string, String::isBlank, throwing(PARAMETER_EMPTY, LANGUAGE_TAG));

		this.wrappedLanguageTag = string;
	}


	/**
	 * Static factory method that creates a new LanguageTag from a String
	 *
	 * @param string the string to use in the creation of a new LanguageTag
	 * @return Optional of a LanguageTag if valid, otherwise an empty Optional
	 */
	public static Optional<LanguageTag> of(final String string)
	{
		return (string != null && Locale.forLanguageTag(string) != null)
				? Optional.of(new LanguageTag(string))
				: Optional.empty();
	}


	/**
	 * Static factory method that creates a new LanguageTag from a Locale
	 *
	 * @param locale the Locale to use in the creation of a new LanguageTag
	 * @return Optional of a LanguageTag if valid, otherwise an empty Optional
	 */
	public static Optional<LanguageTag> of(final Locale locale)
	{
		return (locale == null)
				? Optional.empty()
				: Optional.of(new LanguageTag(locale.toLanguageTag()));
	}


	/**
	 * Get the default LanguageTag (en-US)
	 *
	 * @return a new default LanguageTag
	 */
	public static LanguageTag getDefault()
	{
		return new LanguageTag("en-US");
	}


	/**
	 * Retrieve the {@link Locale} associated with this language tag
	 *
	 * @return the {@code Locale} associated with this language tag
	 */
	public Locale getLocale()
	{
		return Locale.forLanguageTag(wrappedLanguageTag);
	}


	/**
	 * Retrieve the IETF language tag associated with a potential language resource, as a String
	 *
	 * @return {@code String} representing the IETF language tag associated with a potential language resource
	 */
	@Override
	public String toString()
	{
		return this.wrappedLanguageTag;
	}

}
