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

import java.util.Optional;

@FunctionalInterface
public interface ValidationHandler<T>
{
	Optional<T> handleInvalid(final T value);


	static <T> ValidationHandler<T> throwing(final ErrorMessageKey messageKey, final Parameter parameter)
	{
		return new Throwing<>(() -> new ValidationException(messageKey, parameter));
	}


	static <T> ValidationHandler<T> logging(final LogLevel level,
											final ErrorMessageKey messageKey,
											final Parameter parameter)
	{
		return new Logging<>(level, messageKey, parameter);
	}

}
