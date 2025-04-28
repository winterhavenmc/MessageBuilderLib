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

package com.winterhavenmc.library.messagebuilder.keys;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import static com.winterhavenmc.library.messagebuilder.validation.ErrorMessageKey.PARAMETER_INVALID;
import static com.winterhavenmc.library.messagebuilder.validation.ErrorMessageKey.PARAMETER_NULL;
import static com.winterhavenmc.library.messagebuilder.validation.Parameter.KEY;
import static com.winterhavenmc.library.messagebuilder.validation.ValidationHandler.throwing;
import static com.winterhavenmc.library.messagebuilder.validation.Validator.validate;


/**
 * An abstract class that provides common methods for implementations of the StandardKey interface
 */
public abstract class AbstractKey
{
	// valid key must begin with alpha only and may contain alpha, digits, underscore or period
	protected static final Pattern VALID_KEY = Pattern.compile("^[a-zA-Z][a-zA-Z\\d_.]*$");
	protected static final Predicate<String> IS_INVALID_KEY = string -> !VALID_KEY.matcher(string).matches();

	protected final String wrappedString;


	protected AbstractKey(String key)
	{
		validate(key, Objects::isNull, throwing(PARAMETER_NULL, KEY));
		validate(key, IS_INVALID_KEY, throwing(PARAMETER_INVALID, KEY));

		this.wrappedString = key;
	}


	protected String dotJoin(String subKey)
	{
		return dotJoin(this, subKey);
	}


	static protected String dotJoin(AbstractKey baseKey, String subKey)
	{
		return String.join(".", baseKey.toString(), subKey);
	}


	@Override
	public String toString()
	{
		return this.wrappedString;
	}


	/**
	 * Test for equality. Keys must be of same type and have matching string value.
	 * @param object the object to test for equality against this instance
	 * @return true if the object is equal to this instance, false if not
	 */
	@Override
	public boolean equals(final Object object)
	{
		return object instanceof StandardKey standardKey && switch (standardKey)
		{
			case MacroKey macroKey -> this instanceof MacroKey && this.wrappedString.equals(macroKey.wrappedString);
			case RecordKey recordKey -> this instanceof RecordKey && this.wrappedString.equals(recordKey.wrappedString);
		};
	}


	@Override
	public int hashCode()
	{
		return wrappedString.hashCode();
	}

}
