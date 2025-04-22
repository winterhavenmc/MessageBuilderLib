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

package com.winterhavenmc.util.time;

import com.winterhavenmc.util.messagebuilder.util.LocaleSupplier;
import com.winterhavenmc.util.messagebuilder.validation.LogLevel;
import net.time4j.CalendarUnit;
import net.time4j.ClockUnit;
import net.time4j.PrettyTime;
import net.time4j.format.TextWidth;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.Objects;

import static com.winterhavenmc.util.messagebuilder.validation.ErrorMessageKey.PARAMETER_NULL;
import static com.winterhavenmc.util.messagebuilder.validation.Parameter.*;
import static com.winterhavenmc.util.messagebuilder.validation.ValidationHandler.logging;
import static com.winterhavenmc.util.messagebuilder.validation.Validator.validate;


/**
 * Formats a duration using the Time4J {@link PrettyTime} formatter.
 * <p>
 * Given a {@code Duration} and a {@code ChronoUnit} precision, this formatter returns
 * a localized string with appropriate pluralized time units.
 * If the duration is less than one unit of the specified precision, the output is
 * rendered as if it were exactly one unit of that precision.
 */
public final class Time4jDurationFormatter implements DurationFormatter
{
	private final LocaleSupplier localeSupplier;


	public Time4jDurationFormatter(final LocaleSupplier localeSupplier)
	{
		this.localeSupplier = localeSupplier;
	}


	@Override
	public String format(final Duration duration, final ChronoUnit lowerBound)
	{
		Duration validDuration = validate(duration, Objects::isNull, logging(LogLevel.WARN, PARAMETER_NULL, DURATION)).orElse(Duration.ZERO);
		ChronoUnit validLowerBound = validate(lowerBound, Objects::isNull, logging(LogLevel.WARN, PARAMETER_NULL, PRECISION)).orElse(ChronoUnit.MINUTES);

		Locale locale = localeSupplier.get();

		// clamp duration to lowerBound threshold for formatting if needed
		Duration toFormat = validDuration.compareTo(Duration.of(1, validLowerBound)) < 0
				? Duration.of(1, validLowerBound)
				: validDuration;

		PrettyTime prettyTime = PrettyTime.of(locale);

		net.time4j.Duration<?> t4jDuration = net.time4j.Duration.from(toFormat).toClockPeriod();

		net.time4j.Duration<CalendarUnit> calendarPart = t4jDuration.toCalendarPeriod();
		net.time4j.Duration<ClockUnit> clockPart = t4jDuration.toClockPeriod().with(ClockUnit.SECONDS.rounded());

		return prettyTime.print(net.time4j.Duration.compose(calendarPart, clockPart), TextWidth.WIDE);
	}

}
