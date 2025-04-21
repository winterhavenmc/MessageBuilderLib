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

import com.winterhavenmc.util.messagebuilder.formatters.LocaleNumberFormatter;
import com.winterhavenmc.util.time.DurationFormatter;
import java.util.Objects;


public final class ResolverContext
{

	private final DurationFormatter durationFormatter;
	private final LocaleNumberFormatter localeNumberFormatter;


	public ResolverContext(final DurationFormatter durationFormatter,
						   final LocaleNumberFormatter localeNumberFormatter)
	{
		this.durationFormatter = Objects.requireNonNull(durationFormatter);
		this.localeNumberFormatter = localeNumberFormatter;
	}


	public DurationFormatter durationFormatter()
	{
		return durationFormatter;
	}


	public LocaleNumberFormatter localeNumberFormatter()
	{
		return localeNumberFormatter;
	}

}
