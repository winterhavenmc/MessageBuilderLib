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

import com.winterhavenmc.library.messagebuilder.util.Delimiter;
import com.winterhavenmc.library.messagebuilder.validation.ValidationException;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * A type that represents a validated key for a record. This type guarantees a valid key that has been
 * validated upon creation. The static factory methods return an Optional of the RecordKey,
 * or an empty Optional if the parameter was invalid, as determined by regex pattern and Predicate.
 */
public final class MacroKey extends AbstractKey implements StandardKey
{
	private static final Pattern BASE_KEY_PATTERN = Pattern.compile("(\\p{Lu}[\\p{Alnum}_]+)[\\p{Alnum}_.]*");


	/**
	 * Private constructor that allows instantiation only from within this class
	 *
	 * @param key a String representing a record key
	 * @throws ValidationException if parameter passed from static factory method is null or invalid
	 */
	private MacroKey(final String key)
	{
		super(key);
	}


	/**
	 * Static factory method for instantiating a record key from a string
	 *
	 * @param key a String to be used in the creation of a record key
	 * @return an Optional RecordKey, or an empty Optional if the key parameter is null or invalid
	 */
	public static Optional<MacroKey> of(final String key)
	{
		return (key == null || IS_INVALID_KEY.test(key))
				? Optional.empty()
				: Optional.of(key)
						.filter(VALID_KEY.asMatchPredicate())
						.map(MacroKey::new);
	}


	/**
	 * Static factory method for instantiating a record key from an enum constant
	 *
	 * @param key an enum constant whose name is used to create a record key
	 * @return an Optional RecordKey, or an empty Optional if the key parameter is null or invalid
	 * @param <E> an enum constant
	 */
	public static <E extends Enum<E>> Optional<MacroKey> of(final E key)
	{
		return (key == null || IS_INVALID_KEY.test(key.name()))
				? Optional.empty()
				: Optional.of(key)
						.map(Enum::name)
						.map(MacroKey::new);
	}


	/**
	 * Create a new key by suffixing a dot-separated subkey to this existing key
	 *
	 * @param subKey an enu constant whose name() is to be used for the appended subkey
	 * @return a new key with the subkey appended, or an empty Optional if the string was not a valid key
	 * @param <E> an enum constant
	 */
	public <E extends Enum<E>> Optional<MacroKey> append(final E subKey)
	{
		return (subKey == null || IS_INVALID_KEY.test(subKey.name()))
				? Optional.empty()
				: MacroKey.of(dotJoin(subKey.name()));
	}


	/**
	 * Create a new key by suffixing a dot-separated subkey to this existing key
	 *
	 * @param subKey a string to be used for the appended subkey
	 * @return a new key with the subkey appended, or an empty Optional if the string was not a valid key
	 */
	public Optional<MacroKey> append(final String subKey)
	{
		return (subKey == null || IS_INVALID_KEY.test(subKey))
				? Optional.empty()
				: MacroKey.of(dotJoin(subKey));
	}


	public MacroKey getBase()
	{
		Matcher matcher = BASE_KEY_PATTERN.matcher(wrappedString);
		return (matcher.find())
			? MacroKey.of(matcher.group(1)).orElse(this)
			: this;
	}


	public String asPlaceholder()
	{
		return Delimiter.OPEN + wrappedString + Delimiter.CLOSE;
	}

}
