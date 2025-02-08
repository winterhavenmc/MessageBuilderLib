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

package com.winterhavenmc.util.messagebuilder.util;

import java.util.function.Predicate;
import java.util.function.Supplier;

public class Validate implements Validator {

	public static <T> T validate(T value, Predicate<T> predicate, Supplier<RuntimeException> exceptionSupplier) {
		if (predicate.test(value)) {
			throw exceptionSupplier.get(); // Throws from the call site!
		}
		return value; // Pass through the valid value for functional chains.
	}

}
