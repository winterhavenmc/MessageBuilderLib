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

package com.winterhavenmc.util.messagebuilder.resources.language.yaml;

import com.winterhavenmc.util.messagebuilder.validation.ValidationException;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import static com.winterhavenmc.util.messagebuilder.validation.ValidationMessageKey.PARAMETER_EMPTY;
import static com.winterhavenmc.util.messagebuilder.validation.ValidationMessageKey.PARAMETER_NULL;
import static com.winterhavenmc.util.messagebuilder.validation.Parameter.KEY;
import static com.winterhavenmc.util.messagebuilder.validation.Validator.validate;


/**
 * A type that represents a validated key for a record
 */
public class RecordKey
{
	// valid key must start with alpha only and may contain alpha, digits, underscore or period
	private static final Pattern VALID_KEY = Pattern.compile("^[a-zA-Z][a-zA-Z\\d_.]*$");
	private static final Predicate<String> IS_INVALID_KEY = s -> !VALID_KEY.matcher(s).matches();

	private final String key;


	/**
	 * Private constructor that only allows key creation from within this class
	 * @param key a String representing a record key
	 */
	private RecordKey(final String key)
	{
		validate(key, Objects::isNull, () -> new ValidationException(PARAMETER_NULL, KEY));
		validate(key, IS_INVALID_KEY, () -> new ValidationException(PARAMETER_EMPTY, KEY));

		this.key = key;
	}


	/**
	 * Static factory method for instantiating a record key from an enum constant
	 *
	 * @param key an enum constant whose name is used to create a record key
	 * @return an Optional record key
	 * @param <E> an enum constant
	 */
	public static <E extends Enum<E>> Optional<RecordKey> create(final E key)
	{
		return Optional.ofNullable(key)
				.map(Enum::name)
				.map(RecordKey::new);
	}


	/**
	 * Static factory method for instantiating a record key from a string
	 * @param key a String to be used in the creation of a record key
	 * @return An Optional record key
	 */
	public static Optional<RecordKey> create(final String key)
	{
		return Optional.ofNullable(key)
				.filter(VALID_KEY.asMatchPredicate())
				.map(RecordKey::new);
	}


	/**
	 * Returns the record key as a String
	 * @return a String representation fo the record key
	 */
	@Override
	public String toString()
	{
		return this.key;
	}


	@Override
	public final boolean equals(final Object object)
	{
		if (!(object instanceof RecordKey recordKey)) return false;

		return key.equals(recordKey.key);
	}


	@Override
	public int hashCode()
	{
		return key.hashCode();
	}

}
