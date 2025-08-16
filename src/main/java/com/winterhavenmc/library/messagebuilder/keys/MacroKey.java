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

import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Pattern;


public sealed interface MacroKey extends StandardKey permits ValidMacroKey, InvalidKey
{
	Pattern VALID_MACRO_KEY_PATTERN = Pattern.compile("(\\p{Lu}[\\p{Alnum}_]+)[\\p{Alnum}_.]*");
	Predicate<String> IS_INVALID_MACRO_KEY = string -> !VALID_KEY.matcher(string).matches();


	static MacroKey of(String key)
	{
		if (key == null) return new InvalidKey("∅", InvalidKeyReason.KEY_NULL);
		else if (key.isBlank()) return new InvalidKey("⬚", InvalidKeyReason.KEY_BLANK);
		else if (IS_INVALID_MACRO_KEY.test(key)) return new InvalidKey(key, InvalidKeyReason.KEY_INVALID);
		else return new ValidMacroKey(key);
	}


	static <E extends Enum<E>> MacroKey of(final E key)
	{
		if (key == null) return new InvalidKey("∅", InvalidKeyReason.KEY_NULL);
		else if (IS_INVALID_MACRO_KEY.test(key.name())) return new InvalidKey(key.name(), InvalidKeyReason.KEY_INVALID);
		else return new ValidMacroKey(key.name());
	}


	default Optional<ValidMacroKey> isValid()
	{
		return (this instanceof ValidMacroKey valid)
				? Optional.of(valid)
				: Optional.empty();
	}

}
