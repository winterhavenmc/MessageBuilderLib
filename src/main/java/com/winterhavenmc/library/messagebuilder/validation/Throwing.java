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
import java.util.function.Supplier;


/**
 * A {@link Validator} implementation that throws a {@link ValidationException}
 * when a value fails validation.
 * <p>
 * The exception is supplied via a {@link java.util.function.Supplier}, allowing
 * lazy instantiation and support for dynamic message construction.
 * <p>
 * The exception's stack trace is reset via {@link Throwable#fillInStackTrace()}
 * to ensure that the reported call site accurately reflects the point of failure,
 * rather than the supplier's origin.
 *
 * <p>
 * Typically used via {@link Validator#throwing(ErrorMessageKey, Parameter)}.
 *
 * @param <T> the type of the value being validated
 *
 * @see Validator
 * @see ValidationException
 */
public record Throwing<T>(Supplier<ValidationException> exceptionSupplier) implements Validator<T>
{
    /**
     * Throws the supplied exception when the value is invalid.
     *
     * @param value the invalid value
     * @return never returns normally
     * @throws ValidationException always thrown when this handler is invoked
     */
    @Override
    public Optional<T> handleInvalid(final T value)
    {
        ValidationException exception = exceptionSupplier.get();
        exception.fillInStackTrace(); // Maintain call-site accuracy
        throw exception;
    }

}
