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

import java.util.function.Predicate;
import java.util.regex.Pattern;


/**
 * An interface that defines a standard string type.
 */
public sealed interface StandardKey permits MacroKey, RecordKey
{
	// valid key string must begin with uppercase alpha and then may contain upper/lower alpha, digits, underscores or periods
	Pattern VALID_KEY_PATTERN = Pattern.compile("^[A-Z][A-Za-z\\d_.]*$");
	Predicate<String> IS_INVALID_KEY = string -> !VALID_KEY_PATTERN.matcher(string).matches();
}
