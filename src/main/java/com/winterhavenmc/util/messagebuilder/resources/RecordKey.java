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

package com.winterhavenmc.util.messagebuilder.resources;

import com.winterhavenmc.util.messagebuilder.validation.ValidationException;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import static com.winterhavenmc.util.messagebuilder.validation.Parameter.KEY;
import static com.winterhavenmc.util.messagebuilder.validation.ExceptionMessageKey.*;
import static com.winterhavenmc.util.messagebuilder.validation.Validator.validate;


/**
 * A type that represents a validated key for a record. This type guarantees a valid key that has been
 * validated upon creation. The static factory methods return an Optional of the RecordKey,
 * or an empty Optional if the parameter was invalid, as determined by regex pattern and Predicate.
 */
public final class RecordKey
{
	// valid key must begin with alpha only and may contain alpha, digits, underscore or period
	private static final Pattern VALID_KEY = Pattern.compile("^[a-zA-Z][a-zA-Z\\d_.]*$");
	private static final Predicate<String> IS_INVALID_KEY = s -> !VALID_KEY.matcher(s).matches();

	private final String wrappedString;


	/**
	 * Private constructor that allows instantiation only from within this class
	 *
	 * @param key a String representing a record key
	 * @throws ValidationException if parameter passed from static factory method is null or invalid
	 */
	private RecordKey(final String key)
	{
		validate(key, Objects::isNull, () -> new ValidationException(PARAMETER_NULL, KEY));
		validate(key, IS_INVALID_KEY, () -> new ValidationException(PARAMETER_INVALID, KEY));

		this.wrappedString = key;
	}


	/**
	 * Static factory method for instantiating a record key from a string
	 *
	 * @param key a String to be used in the creation of a record key
	 * @return an Optional record key, or an empty Optional if the key parameter is null or invalid
	 */
	public static Optional<RecordKey> of(final String key)
	{
		return Optional.ofNullable(key)
				.filter(VALID_KEY.asMatchPredicate())
				.map(RecordKey::new);
	}


	/**
	 * Static factory method for instantiating a record key from an enum constant
	 *
	 * @param key an enum constant whose name is used to create a record key
	 * @return an Optional record key, or an empty Optional if the key parameter is null or invalid
	 * @param <E> an enum constant
	 */
	public static <E extends Enum<E>> Optional<RecordKey> of(final E key)
	{
		return Optional.ofNullable(key)
				.map(Enum::name)
				.map(RecordKey::new);
	}


	/**
	 * Return the record key as a String
	 *
	 * @return a String representation fo the record key
	 */
	@Override
	public String toString()
	{
		return this.wrappedString;
	}


	@Override
	public final boolean equals(final Object object)
	{
		if (!(object instanceof RecordKey recordKey)) { return false; }

		return wrappedString.equals(recordKey.wrappedString);
	}


	@Override
	public int hashCode()
	{
		return wrappedString.hashCode();
	}

}
