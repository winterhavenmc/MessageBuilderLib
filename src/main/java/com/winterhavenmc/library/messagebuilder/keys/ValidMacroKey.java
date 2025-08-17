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

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * A type that represents a validated string for a macro. This type guarantees a valid string that has been
 * validated upon creation. The static factory methods return an Optional of the LegacyRecordKey,
 * or an empty Optional if the parameter was invalid, as determined by regex pattern and Predicate.
 */
public final class ValidMacroKey implements MacroKey
{
	private static final Pattern MATCH_BASE_KEY_PATTERN = Pattern.compile("^(\\p{Lu}[\\p{Alnum}_]+)[\\p{Alnum}_.]*$");
	private final String string;


	ValidMacroKey(String string)
	{
		this.string = string;
	}


	/**
	 * Create a new string by suffixing a dot-separated subkey to this existing string
	 *
	 * @param subKey a string to be used for the appended subkey
	 * @return a new string with the subkey appended, or an empty Optional if the string was not a valid string
	 */
	public MacroKey append(final String subKey)
	{
		if (subKey == null) return new InvalidKey("∅", InvalidKeyReason.KEY_NULL);
		if (subKey.isBlank()) return new InvalidKey("⬚", InvalidKeyReason.KEY_BLANK);
		else if (IS_INVALID_KEY.test(subKey)) return new InvalidKey(subKey, InvalidKeyReason.KEY_INVALID);
		else return MacroKey.of(dotJoin(this, subKey));
	}


	/**
	 * Create a new string by suffixing a dot-separated subkey to this existing string
	 *
	 * @param subKey an enu constant whose name() is to be used for the appended subkey
	 * @param <E>    an enum constant
	 * @return a new string with the subkey appended, or an empty Optional if the string was not a valid string
	 */
	public <E extends Enum<E>> MacroKey append(final E subKey)
	{
		if (subKey == null) return new InvalidKey("∅", InvalidKeyReason.KEY_NULL);
		else if (IS_INVALID_KEY.test(subKey.name())) return new InvalidKey(subKey.name(), InvalidKeyReason.KEY_INVALID);
		else return MacroKey.of(dotJoin(this, subKey.name()));
	}


	public ValidMacroKey getBase()
	{
		Matcher matcher = MATCH_BASE_KEY_PATTERN.matcher(string);

		return (matcher.find())
				? (ValidMacroKey) MacroKey.of(matcher.group(1))
				: this;
	}


	public String asPlaceholder()
	{
		return Delimiter.OPEN + string + Delimiter.CLOSE;
	}


	static String dotJoin(final MacroKey baseKey, final String subKey)
	{
		return String.join(".", baseKey.toString(), subKey);
	}


	@Override
	public String toString()
	{
		return this.string;
	}


	@Override
	public boolean equals(Object obj)
	{
		if (obj == this) return true;
		if (obj == null || obj.getClass() != this.getClass()) return false;
		var that = (ValidMacroKey) obj;
		return Objects.equals(this.string, that.string);
	}


	@Override
	public int hashCode()
	{
		return Objects.hash(string);
	}

}
