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

package com.winterhavenmc.library.messagebuilder.models.validation;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.logging.Logger;

import static com.winterhavenmc.library.messagebuilder.models.validation.Validator.formatMessage;


/**
 * A {@link Validator} implementation that logs a warning or error
 * when a value fails validation, but does not throw.
 * <p>
 * This handler is useful in cases where validation failures are non-critical
 * or should be tracked without disrupting normal flow.
 * <p>
 * The message is localized using {@link Validator#formatMessage(ErrorMessageKey, Parameter)},
 * and is logged using the Java {@link java.util.logging.Logger} API.
 *
 * <p>
 * The {@link LogLevel} enum provides a clearer abstraction over
 * {@link java.util.logging.Level} for improved readability and intent.
 *
 * <h2>Typical usage:</h2>
 * {@snippet lang = "java":
 *  import com.winterhavenmc.library.messagebuilder.models.validation.Validator;validate(value, Predicate, Validator.logging(logLevel, ErrorMessageKey, Parameter));
 *}
 *
 * @param <T> the type of the value being validated
 *
 * @see Validator
 * @see Predicate
 * @see LogLevel
 * @see ErrorMessageKey
 * @see Parameter
 */
public record Logging<T>(LogLevel logLevel,
                         ErrorMessageKey messageKey,
                         Parameter parameter) implements Validator<T>
{
    private static final Logger LOGGER = Logger.getLogger("ValidationLogger");


    /**
     * Logs a validation failure using the specified log level and message,
     * but continues execution by returning the original value.
     *
     * @param value the invalid value
     * @return an {@code Optional} containing the value, despite being invalid
     */
    @Override
    public Optional<T> handleInvalid(final T value)
    {
        LOGGER.log(logLevel.toJavaUtilLevel(), formatMessage(messageKey, parameter));
        return Optional.ofNullable(value); // return the value even though it failed validation
    }

}
