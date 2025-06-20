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

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;


/**
 * A utility class providing static methods to support parameter validation
 * and localized exception formatting within the MessageBuilder library.
 * <p>
 * The utility supports both exception-based and logging-based validation
 * through the {@link #validate(Object, Predicate, ValidationHandler)} method,
 * which delegates to {@link ValidationHandler} implementations.
 *
 * <p>
 * Localized error messages are resolved using {@link java.util.ResourceBundle}
 * from a bundle named {@code exception.messages}. These messages are formatted
 * using a combination of {@link ErrorMessageKey} and {@link Parameter}.
 * <p>
 * By default, the locale used for formatting is the system default; this can
 * be overridden in future revisions by integrating a plugin-configured locale
 * via a singleton {@code ValidationContext}.
 *
 * @see ValidationHandler
 * @see ValidationException
 * @see ErrorMessageKey
 * @see Parameter
 */
public class ValidationUtility
{
	static final String BUNDLE_NAME = "exception.messages";


	private ValidationUtility() { /* private constructor to prevent instantiation */ }


	/**
	 * Formats a localized error message using the specified key and parameter.
	 * <p>
	 * Message templates are retrieved from the {@code exception.messages}
	 * resource bundle, and parameter placeholders are replaced using
	 * {@link java.text.MessageFormat}.
	 *
	 * @param errorMessageKey the structured error key
	 * @param parameter the parameter involved in the validation
	 * @return the formatted, localized message
	 */
	static String formatMessage(final ErrorMessageKey errorMessageKey,
								final Parameter parameter)
	{
		// Fetch localized message pattern from resource bundle
		String pattern = ResourceBundle.getBundle(BUNDLE_NAME, getConfiguredLocale()).getString(errorMessageKey.name());

		// Insert parameter name into the pattern
		return MessageFormat.format(pattern, parameter.getDisplayName());
	}


	/**
	 * Retrieves the locale to be used for message formatting.
	 * <p>
	 * This method currently returns the system default locale,
	 * but may be replaced by a plugin-configured value in the future.
	 *
	 * @return the locale for formatting messages
	 */
	static Locale getConfiguredLocale()
	{
		return ValidationContext.getLocale();
	}


	/**
	 * Validates a value using the given predicate and handler.
	 * <p>
	 * If the predicate returns {@code true}, the handler is invoked
	 * to process the invalid value (e.g., throw or log). Otherwise,
	 * the valid value is returned as an {@link Optional}.
	 *
	 * @param value the value to validate
	 * @param predicate the condition indicating invalidity
	 * @param handler the strategy for handling invalid values
	 * @return an {@code Optional} containing the valid value, or empty if handled
	 * @param <T> the type of the value
	 */
	public static <T> Optional<T> validate(T value, Predicate<T> predicate, ValidationHandler<T> handler)
	{
		return predicate.test(value)
				? handler.handleInvalid(value)
				: Optional.ofNullable(value);
	}

}
