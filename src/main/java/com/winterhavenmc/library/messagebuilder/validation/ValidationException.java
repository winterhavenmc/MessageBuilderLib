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

import static com.winterhavenmc.library.messagebuilder.validation.Validator.formatMessage;


/**
 * Thrown to indicate that a parameter validation has failed.
 * <p>
 * This exception supports localized error messages using the provided
 * {@link ErrorMessageKey} and {@link Parameter}, and is typically thrown
 * via static methods in {@link Validator}.
 * <p>
 * Unlike {@link IllegalArgumentException}, this class is designed specifically
 * to support structured, translatable messages in a plugin context.
 *
 * <p>
 * The message string is generated using {@link Validator#formatMessage(ErrorMessageKey, Parameter)},
 * and may vary depending on the configured locale.
 *
 * @see ErrorMessageKey
 * @see Parameter
 * @see Validator
 */
public class ValidationException extends IllegalArgumentException
{
	private final ErrorMessageKey errorMessageKey;
	private final Parameter parameter;


	/**
	 * Constructs a new {@code ValidationException} with the specified
	 * {@link ErrorMessageKey} and {@link Parameter} describing the failed validation.
	 * <p>
	 * The exception message is immediately formatted using the current
	 * locale returned by {@link ValidationContext#getLocale()}.
	 *
	 * @param errorMessageKey a structured key identifying the error message template
	 * @param parameter the parameter that failed validation
	 */
	public ValidationException(final ErrorMessageKey errorMessageKey, final Parameter parameter)
	{
		super(formatMessage(errorMessageKey, parameter));
		this.errorMessageKey = errorMessageKey;
		this.parameter = parameter;
	}


	/**
	 * Returns the localized message for this validation exception.
	 * The message is re-formatted on each call to reflect any changes
	 * in the active locale.
	 *
	 * @return the localized validation error message
	 */
	@Override
	public String getMessage()
	{
		return formatMessage(errorMessageKey, parameter);
	}

}
