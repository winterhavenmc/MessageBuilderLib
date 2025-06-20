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
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;

/**
 * A functional interface representing a strategy for handling invalid values
 * detected during validation.
 * <p>
 * Implementations may choose to throw an exception, log a message, or
 * perform some other side-effect based on the invalid value.
 * <p>
 * Used in conjunction with {@code ValidationUtility.validate(Object, Predicate, Validator)}.
 *
 * @param <T> the type of the value being validated
 */
@FunctionalInterface
public interface Validator<T>
{
	String BUNDLE_NAME = "exception.messages";

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
	static <T> Optional<T> validate(T value, Predicate<T> predicate, Validator<T> handler)
	{
		return predicate.test(value)
				? handler.handleInvalid(value)
				: Optional.ofNullable(value);
	}


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
		String pattern = ResourceBundle.getBundle(BUNDLE_NAME, ValidationContext.getLocale()).getString(errorMessageKey.name());

		// Insert parameter name into the pattern
		return MessageFormat.format(pattern, parameter.getDisplayName());
	}


	/**
	 * Handles an invalid value detected during validation.
	 *
	 * @param value the invalid value
	 * @return an empty optional if handled, or a fallback value (if applicable)
	 */
	Optional<T> handleInvalid(final T value);


	/**
	 * Returns a handler that throws a {@link ValidationException}
	 * using the given error message key and parameter.
	 *
	 * @param messageKey the structured message key
	 * @param parameter the parameter that failed validation
	 * @return a throwing validation handler
	 * @param <T> the type of the value
	 */
	static <T> Validator<T> throwing(final ErrorMessageKey messageKey, final Parameter parameter)
	{
		return new Throwing<>(() -> new ValidationException(messageKey, parameter));
	}


	/**
	 * Returns a handler that logs a validation warning or error using
	 * the specified log level, message key, and parameter.
	 *
	 * @param level the severity level to log
	 * @param messageKey the structured message key
	 * @param parameter the parameter that failed validation
	 * @return a logging validation handler
	 * @param <T> the type of the value
	 */
	static <T> Validator<T> logging(final LogLevel level,
									final ErrorMessageKey messageKey,
									final Parameter parameter)
	{
		return new Logging<>(level, messageKey, parameter);
	}

}
