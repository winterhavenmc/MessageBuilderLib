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

import java.util.function.Consumer;


public sealed interface ValidationHandler<T> permits Throwing, Logging, LoggingAndThrowing, DefaultValue
{
	@SuppressWarnings("UnusedReturnValue")
	T handleInvalid(final T value);


	static <T> ValidationHandler<T> throwing(final ExceptionMessageKey messageKey, final Parameter parameter)
	{
		return new Throwing<>(() -> new ValidationException(messageKey, parameter));
	}


	static <T> ValidationHandler<T> logging(final Consumer<? super T> logger)
	{
		return new Logging<>(logger);
	}


	static <T> ValidationHandler<T> loggingAndThrowing(final Consumer<? super T> logger,
                                                       final String message,
                                                       final ExceptionMessageKey messageKey,
                                                       final Parameter parameter)
	{
		return new LoggingAndThrowing<>(logger, () -> new ValidationException(messageKey, parameter));
	}


	static <T> ValidationHandler<T> defaultValue(final T value)
	{
		return new DefaultValue<>(value);
	}

}
