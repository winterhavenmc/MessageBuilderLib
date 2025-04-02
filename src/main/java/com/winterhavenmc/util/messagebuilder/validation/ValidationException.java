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


public class ValidationException extends IllegalArgumentException
{

	private static final String ERROR_BUNDLE_NAME = "language.errors";

	private final ExceptionMessageKey exceptionMessageKey;
	private final String parameterDisplayName;
	private final Object[] params;


	public ValidationException(ExceptionMessageKey exceptionMessageKey, Parameter parameter, Object... params)
	{
		super(formatMessage(exceptionMessageKey, parameter, params));
		this.exceptionMessageKey = exceptionMessageKey;
		this.parameterDisplayName = parameter.getDisplayName();
		this.params = params;
	}


	private static String formatMessage(ExceptionMessageKey exceptionMessageKey, Parameter parameter, Object... params)
	{
		// Fetch localized message pattern from resource bundle
		String pattern = ResourceBundle.getBundle(ERROR_BUNDLE_NAME, getConfiguredLocale()).getString(exceptionMessageKey.name());

		// Insert parameter name into the pattern with additional parameters
		return MessageFormat.format(pattern, parameter.getDisplayName(), params);
	}


	//TODO: fetch locale from plugin config.yml ?
	private static Locale getConfiguredLocale()
	{
		return Locale.getDefault();
	}


	@Override
	public String getMessage()
	{
		return getLocalizedMessage(Locale.getDefault());
	}


	public String getLocalizedMessage(Locale locale)
	{
		ResourceBundle bundle = ResourceBundle.getBundle(ERROR_BUNDLE_NAME, locale);
		String pattern = bundle.getString(exceptionMessageKey.name());
		return MessageFormat.format(pattern, parameterDisplayName, params);
	}

}
