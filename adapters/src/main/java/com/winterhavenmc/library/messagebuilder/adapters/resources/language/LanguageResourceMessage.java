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

package com.winterhavenmc.library.messagebuilder.adapters.resources.language;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public enum LanguageResourceMessage
{
	LANGUAGE_RESOURCE_LOAD_SUCCESS("Language file ''{0}'' was successfully loaded."),

	LANGUAGE_RESOURCE_TAG_MISSING("No valid language tag could be resolved from config or default."),
	LANGUAGE_RESOURCE_NOT_FOUND("Language file ''{0}'' not found. Falling back to default."),
	LANGUAGE_RESOURCE_UNREADABLE("Language file ''{0}'' could not be read. Falling back to default."),
	LANGUAGE_RESOURCE_INVALID_YAML("Language file ''{0}'' is not valid YAML. Falling back to default."),
	LANGUAGE_RESOURCE_INVALID_FILE("Language file ''{0}'' is invalid: {1}"),
	LANGUAGE_RESOURCE_FALLBACK_SUCCESS("Loaded fallback language resource ''{0}'' from plugin JAR."),
	LANGUAGE_RESOURCE_FALLBACK_MISSING("Fallback language resource ''{0}'' is missing from plugin JAR."),
	LANGUAGE_RESOURCE_FALLBACK_FAILED("Failed to load fallback language resource ''{0}'' from JAR."),

	LANGUAGE_RESOURCE_EXCEPTION("An exception occurred while attempting to load language file ''{0}''."),

	LANGUAGE_RESOURCE_INSTALL_SUCCESS("Installed resource: {0}"),
	LANGUAGE_RESOURCE_INSTALL_MISSING("Installation failed. File missing after save: {0}"),
	LANGUAGE_RESOURCE_INSTALL_EXCEPTION("An exception occurred while attempting to install language file ''{0}'': {1}")
	;

	private final String defaultMessage;


	LanguageResourceMessage(final String defaultMessage)
	{
		this.defaultMessage = defaultMessage;
	}


	public String getDefaultMessage()
	{
		return defaultMessage;
	}


	public String getLocalizedMessage(final Locale locale)
	{
		try
		{
			ResourceBundle bundle = ResourceBundle.getBundle(getClass().getSimpleName(), locale);
			return bundle.getString(name());
		}
		catch (MissingResourceException exception)
		{
			return this.defaultMessage;
		}
	}


	public String getLocalizedMessage(final Locale locale, final Object... objects)
	{
		try
		{
			final ResourceBundle bundle = ResourceBundle.getBundle(getClass().getSimpleName(), locale);
			String pattern = bundle.getString(name());
			return MessageFormat.format(pattern, objects);
		}
		catch (MissingResourceException exception)
		{
			return MessageFormat.format(this.defaultMessage, objects);
		}
	}


	@Override
	public String toString()
	{
		return this.defaultMessage;
	}

}
