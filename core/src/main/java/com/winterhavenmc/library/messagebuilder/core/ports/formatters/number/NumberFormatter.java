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

package com.winterhavenmc.library.messagebuilder.core.ports.formatters.number;

/**
 * A functional interface for formatting numeric values into localized {@link String} representations.
 *
 * <p>Implementations of this interface convert {@link Number} instances into strings
 * based on localization rules, such as digit grouping, decimal separators, and numeric styles
 * defined by a {@link java.util.Locale}.
 *
 * <p>This interface is used throughout the message building pipeline to ensure
 * numeric values (e.g., coordinates, quantities) are presented in a locale-aware manner
 * within macro-replaced messages.
 *
 * <p>To provide a concrete implementation, see
 * {@code LocaleNumberFormatter}.
 *
 * @see java.text.NumberFormat
 */
@FunctionalInterface
public interface NumberFormatter
{
	/**
	 * Formats the provided {@link Number} using a locale-specific format.
	 *
	 * @param number the numeric value to format
	 * @return a localized string representation of the number
	 */
	String format(Number number);
}
