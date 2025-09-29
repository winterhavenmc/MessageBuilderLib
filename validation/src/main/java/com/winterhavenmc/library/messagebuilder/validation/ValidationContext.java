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

import com.winterhavenmc.library.messagebuilder.configuration.LocaleProvider;

import java.util.Locale;
import java.util.Objects;

import static com.winterhavenmc.library.messagebuilder.validation.ErrorMessageKey.PARAMETER_NULL;
import static com.winterhavenmc.library.messagebuilder.validation.Parameter.LOCALE_PROVIDER;
import static com.winterhavenmc.library.messagebuilder.validation.Validator.throwing;
import static com.winterhavenmc.library.messagebuilder.validation.Validator.validate;

/**
 * Provides global access to the plugin-configured locale used
 * during validation message formatting.
 * <p>
 * This singleton must be explicitly initialized during plugin startup
 * using {@link #initialize(LocaleProvider)}. After initialization,
 * the locale is considered immutable.
 * <p>
 * Validation logic (e.g., {@link Validator}) uses this context
 * to format localized error messages.
 * <p>
 * <strong>Note:</strong> The {@link #reset()} method is intended for use
 * in unit tests only and should never be called during normal plugin execution.
 *
 * @see Validator
 * @see LocaleProvider
 */
public final class ValidationContext
{
	private static LocaleProvider localeProvider;

	private ValidationContext() { throw new AssertionError(); }


	/**
	 * Initializes the validation context with the provided locale source.
	 * <p>
	 * This method should be called exactly once during plugin startup.
	 * Subsequent calls will throw an {@link IllegalStateException}.
	 *
	 * @param provider the plugin’s configured locale provider
	 * @throws IllegalStateException if already initialized
	 */
	public static void initialize(final LocaleProvider provider)
	{
		validate(provider, Objects::isNull, throwing(PARAMETER_NULL, LOCALE_PROVIDER));

		if (localeProvider == null)
		{
			localeProvider = provider;
		}
	}


	/**
	 * Returns the current locale used for formatting validation messages.
	 * If the context has not been initialized, falls back to {@link Locale#getDefault()}.
	 *
	 * @return the active validation locale
	 */
	public static Locale getLocale()
	{
		return (localeProvider != null)
				? localeProvider.getLocale()
				: Locale.getDefault();
	}


	/**
	 * Resets the validation context, clearing the configured locale provider.
	 * <p>
	 * <strong>Intended for use in test environments only.</strong>
	 * This method allows tests to reinitialize the context with different
	 * locale providers without restarting the plugin.
	 */
	static void reset() {
		localeProvider = null;
	}

}
