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
import java.util.logging.Logger;

import static com.winterhavenmc.library.messagebuilder.validation.ValidationUtility.formatMessage;


public record Logging<T>(LogLevel logLevel,
                         ErrorMessageKey messageKey,
                         Parameter parameter) implements ValidationHandler<T>
{
    private static final Logger LOGGER = Logger.getLogger("ValidationLogger");


    @Override
    public Optional<T> handleInvalid(final T value)
    {
        LOGGER.log(logLevel.toJavaUtilLevel(), formatMessage(messageKey, parameter));

        return Optional.ofNullable(value); // return the value even though it failed validation
    }

}
