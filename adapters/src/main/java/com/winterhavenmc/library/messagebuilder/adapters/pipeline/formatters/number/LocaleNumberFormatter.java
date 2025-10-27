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

package com.winterhavenmc.library.messagebuilder.adapters.pipeline.formatters.number;

import com.winterhavenmc.library.messagebuilder.models.configuration.ConfigRepository;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.formatters.number.NumberFormatter;

import java.text.NumberFormat;


/**
 * A locale-aware implementation of the {@link NumberFormatter} interface that formats
 * numeric values using the default number format for the locale provided by a {@link ConfigRepository}.
 *
 * <p>This class is used throughout the message formatting pipeline to produce
 * human-readable, properly localized number strings (e.g., for coordinates, quantities, etc.).
 *
 * <p>For example, the number {@code 12345.67} might be formatted as {@code "12,345.67"} in
 * {@code Locale.US}, or as {@code "12 345,67"} in {@code Locale.FRANCE}, depending on the locale.
 *
 * @see java.text.NumberFormat
 * @see ConfigRepository
 */
public class LocaleNumberFormatter implements NumberFormatter
{
	private final ConfigRepository configRepository;


	/**
	 * Constructs a new {@code LocaleNumberFormatter} with the given locale provider.
	 *
	 * @param configRepository a provider of {@link java.util.Locale} instances used for formatting
	 */
	public LocaleNumberFormatter(final ConfigRepository configRepository)
	{
		this.configRepository = configRepository;
	}


	/**
	 * Formats the specified number using the {@link java.text.NumberFormat}
	 * instance associated with the locale provided by the {@link ConfigRepository}.
	 *
	 * @param number the number to format
	 * @return a localized string representation of the number
	 */
	@Override
	public String format(final Number number)
	{
		return NumberFormat.getInstance(configRepository.locale()).format(number);
	}

}
