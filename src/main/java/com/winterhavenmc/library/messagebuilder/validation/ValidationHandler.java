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

import java.util.Optional;

/**
 * A functional interface representing a strategy for handling invalid values
 * detected during validation.
 * <p>
 * Implementations may choose to throw an exception, log a message, or
 * perform some other side-effect based on the invalid value.
 * <p>
 * Used in conjunction with {@code ValidationUtility.validate(Object, Predicate, ValidationHandler)}.
 *
 * @param <T> the type of the value being validated
 *
 * @see ValidationUtility
 */
@FunctionalInterface
public interface ValidationHandler<T>
{
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
	static <T> ValidationHandler<T> throwing(final ErrorMessageKey messageKey, final Parameter parameter)
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
	static <T> ValidationHandler<T> logging(final LogLevel level,
											final ErrorMessageKey messageKey,
											final Parameter parameter)
	{
		return new Logging<>(level, messageKey, parameter);
	}

}
