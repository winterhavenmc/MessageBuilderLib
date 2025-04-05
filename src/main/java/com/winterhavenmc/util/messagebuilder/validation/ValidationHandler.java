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
import java.util.function.Supplier;


public sealed interface ValidationHandler<T>
		permits ValidationHandler.Throwing, ValidationHandler.Logging, ValidationHandler.LoggingAndThrowing, ValidationHandler.DefaultValue
{
	@SuppressWarnings("UnusedReturnValue")
	T handleInvalid(T value);


	record Throwing<T>(Supplier<ValidationException> exceptionSupplier) implements ValidationHandler<T>
	{
		@Override
		public T handleInvalid(T value)
		{
			ValidationException exception = exceptionSupplier.get();
			exception.fillInStackTrace(); // Maintain call-site accuracy
			throw exception;
		}
	}


	record Logging<T>(Consumer<? super T> logger) implements ValidationHandler<T>
	{
		@Override
		public T handleInvalid(T value)
		{
			logger.accept(value);
			return value;
		}
	}


	record LoggingAndThrowing<T>(
			Consumer<? super T> logger,
			Supplier<ValidationException> exceptionSupplier) implements ValidationHandler<T>
	{
		@Override
		public T handleInvalid(T value) {
			logger.accept(value);
			ValidationException ex = exceptionSupplier.get();
			ex.fillInStackTrace();
			throw ex;
		}
	}


	record DefaultValue<T>(T defaultValue) implements ValidationHandler<T>
	{
		@Override
		public T handleInvalid(T value) {
			return defaultValue;
		}
	}


	static <T> ValidationHandler<T> throwing(ExceptionMessageKey messageKey, Parameter parameter)
	{
		return new Throwing<>(() -> new ValidationException(messageKey, parameter));
	}


	static <T> ValidationHandler<T> logging(Consumer<? super T> logger)
	{
		return new Logging<>(logger);
	}


	static <T> ValidationHandler<T> loggingAndThrowing(Consumer<? super T> logger,
                                                       String message,
                                                       ExceptionMessageKey messageKey,
                                                       Parameter parameter)
	{
		return new LoggingAndThrowing<>(logger, () -> new ValidationException(messageKey, parameter));
	}

}
