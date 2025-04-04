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

package com.winterhavenmc.util.messagebuilder.resources.language.yaml;

import com.winterhavenmc.util.messagebuilder.validation.ValidationException;

import java.io.File;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

import static com.winterhavenmc.util.messagebuilder.resources.language.yaml.YamlLanguageSetting.RESOURCE_SUBDIRECTORY;
import static com.winterhavenmc.util.messagebuilder.validation.ExceptionMessageKey.PARAMETER_EMPTY;
import static com.winterhavenmc.util.messagebuilder.validation.ExceptionMessageKey.PARAMETER_NULL;
import static com.winterhavenmc.util.messagebuilder.validation.Parameter.LANGUAGE_TAG;
import static com.winterhavenmc.util.messagebuilder.validation.Validator.validate;


public class LanguageTag
{
	private final String wrappedLanguageTag;


	/**
	 * Class constructor that takes a String language tag as a parameter
	 *
	 * @param languageTag the language tag representing a potential language resource
	 * @throws ValidationException if parameter is null or empty
	 */
	private LanguageTag(final String languageTag)
	{
		validate(languageTag, Objects::isNull, () -> new ValidationException(PARAMETER_NULL, LANGUAGE_TAG));
		validate(languageTag, String::isBlank, () -> new ValidationException(PARAMETER_EMPTY, LANGUAGE_TAG));

		this.wrappedLanguageTag = languageTag;
	}


	public static Optional<LanguageTag> of(final Locale locale)
	{
		return locale == null
				? Optional.empty()
				: Optional.of(new LanguageTag(locale.toLanguageTag()));
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


	/**
	 * Retrieve the name of the potential language resource associated with this language tag, as a String
	 *
 	 * @return {@code String} representation of the potential language resource associated with this language tag
	 */
	public String getResourceName()
	{
		return String.join("/", RESOURCE_SUBDIRECTORY.toString(), wrappedLanguageTag).concat(".yml");
	}


	/**
	 * Retrieve the name of the potential language resource file as installed in the plugin data directory, as a String.
	 *
	 * @return {@code String} representation of the potential language resource file installed in the plugin data directory
	 */
	public String getFileName()
	{
		return String.join(File.separator, RESOURCE_SUBDIRECTORY.toString(), wrappedLanguageTag).concat(".yml");
	}

}
