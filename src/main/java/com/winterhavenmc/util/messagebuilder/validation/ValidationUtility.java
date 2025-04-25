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

package com.winterhavenmc.util.messagebuilder.validation;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class ValidationUtility
{
	static final String BUNDLE_NAME = "language.errors";


	private ValidationUtility() { /* private constructor to prevent instantiation */ }


	static String formatMessage(final ErrorMessageKey errorMessageKey,
								final Parameter parameter)
	{
		// Fetch localized message pattern from resource bundle
		String pattern = ResourceBundle.getBundle(BUNDLE_NAME, getConfiguredLocale()).getString(errorMessageKey.name());

		// Insert parameter name into the pattern
		return MessageFormat.format(pattern, parameter.getDisplayName());
	}


	//TODO: fetch locale from plugin config.yml ?
	static Locale getConfiguredLocale()
	{
		return Locale.getDefault();
	}

}
