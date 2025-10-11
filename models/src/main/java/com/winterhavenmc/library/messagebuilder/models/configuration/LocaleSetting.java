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

import java.util.Locale;


/**
 * Represents a locale setting derived from a valid {@link LanguageTag}.
 * <p>
 * This record is used as a wrapper around a {@code LanguageTag} to provide access
 * to its associated {@link Locale}, typically for localization and formatting logic.
 * </p>
 *
 * @param languageTag the {@link LanguageTag} representing a specific locale
 *
 * @see LanguageTag
 * @see Locale
 */
public record LocaleSetting(LanguageTag languageTag)
{
	/**
	 * Returns the {@link Locale} corresponding to the encapsulated {@link LanguageTag}.
	 *
	 * @return a valid {@code Locale} derived from the language tag
	 */
	public Locale locale()
	{
		return languageTag.getLocale();
	}
}
