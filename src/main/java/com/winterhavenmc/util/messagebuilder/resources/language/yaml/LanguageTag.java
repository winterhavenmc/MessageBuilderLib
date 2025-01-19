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

import java.io.File;
import java.util.Locale;

import static com.winterhavenmc.util.messagebuilder.resources.language.yaml.YamlLanguageResourceInstaller.SUBDIRECTORY;

public class LanguageTag
{
	private final Locale locale;
	private final String languageTag;


	/**
	 * Class constructor that takes a String language tag as a parameter
	 *
	 * @param languageTag the language tag representing a potential language resource
	 */
	public LanguageTag(final String languageTag)
	{
		this.languageTag = languageTag;
		this.locale = Locale.forLanguageTag(languageTag);
	}


	/**
	 * Class constructor that takes a Locale as a parameter to indicate a potential language resource
	 *
	 * @param locale the {@link Locale} representing a potential language resource
	 */
	public LanguageTag(final Locale locale)
	{
		this.locale = locale;
		this.languageTag = locale.toLanguageTag();
	}


	/**
	 * Retrieve the {@link Locale} associated with this language tag
	 *
	 * @return the {@code Locale} associated with this language tag
	 */
	public Locale getLocale()
	{
		return locale;
	}


	/**
	 * Retrieve the IETF language tag associated with a potential language resource, as a String
	 *
	 * @return {@code String} representing the IETF language tag associated with a potential language resource
	 */
	public String getLanguageTag()
	{
		return languageTag;
	}


	/**
	 * Retrieve the name of the potential language resource associated with this language tag, as a String
	 *
 	 * @return {@code String} representation of the potential language resource associated with this language tag
	 */
	public String getResourceName()
	{
		return String.join("/", SUBDIRECTORY, languageTag).concat(".yml");
	}


	/**
	 * Retrieve the name of the potential language resource file as installed in the plugin data directory, as a String.
	 *
	 * @return {@code String} representation of the potential language resource file installed in the plugin data directory
	 */
	public String getFileName()
	{
		return String.join(File.separator, SUBDIRECTORY, languageTag).concat(".yml");
	}

}
