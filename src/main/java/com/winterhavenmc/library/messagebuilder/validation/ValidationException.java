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

import static com.winterhavenmc.library.messagebuilder.validation.ValidationUtility.formatMessage;


public class ValidationException extends IllegalArgumentException
{
	private final ErrorMessageKey errorMessageKey;
	private final Parameter parameter;


	public ValidationException(final ErrorMessageKey errorMessageKey, final Parameter parameter)
	{
		super(formatMessage(errorMessageKey, parameter));
		this.errorMessageKey = errorMessageKey;
		this.parameter = parameter;
	}


	@Override
	public String getMessage()
	{
		return formatMessage(errorMessageKey, parameter);
	}

}
