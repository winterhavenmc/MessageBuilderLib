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

package com.winterhavenmc.library.messagebuilder.pipeline.formatters.duration;

import com.winterhavenmc.library.messagebuilder.resources.configuration.LocaleProvider;
import com.winterhavenmc.library.messagebuilder.validation.LogLevel;

import net.time4j.CalendarUnit;
import net.time4j.ClockUnit;
import net.time4j.PrettyTime;
import net.time4j.format.TextWidth;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import static com.winterhavenmc.library.messagebuilder.validation.ErrorMessageKey.PARAMETER_NULL;
import static com.winterhavenmc.library.messagebuilder.validation.Parameter.*;
import static com.winterhavenmc.library.messagebuilder.validation.ValidationHandler.logging;
import static com.winterhavenmc.library.messagebuilder.validation.Validator.validate;


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
	private final LocaleProvider localeProvider;


	public Time4jDurationFormatter(final LocaleProvider localeProvider)
	{
		this.localeProvider = localeProvider;
	}


	@Override
	public String format(final Duration duration, final ChronoUnit lowerBound)
	{
		Duration validDuration = validate(duration, Objects::isNull, logging(LogLevel.WARN, PARAMETER_NULL, DURATION)).orElse(Duration.ZERO);
		ChronoUnit validLowerBound = validate(lowerBound, Objects::isNull, logging(LogLevel.WARN, PARAMETER_NULL, LOWER_BOUND)).orElse(ChronoUnit.MINUTES);

		validDuration = validDuration.truncatedTo(validLowerBound);

		// Extract total seconds for conversion
		long totalSeconds = validDuration.getSeconds();
		long days = totalSeconds / (24 * 3600);
		long remainderSeconds = totalSeconds % (24 * 3600);

		// extract hours, minutes and seconds
		long hours = remainderSeconds / 3600;
		long minutes = (remainderSeconds % 3600) / 60;
		long seconds = remainderSeconds % 60;

		// Build calendar part with days, clock part with remainder
		net.time4j.Duration<CalendarUnit> calendarPart = net.time4j.Duration.of(days, CalendarUnit.DAYS);
		net.time4j.Duration<ClockUnit> clockPart = net.time4j.Duration.of(hours, ClockUnit.HOURS)
				.with(minutes, ClockUnit.MINUTES)
				.with(seconds, ClockUnit.SECONDS);

		return PrettyTime.of(localeProvider.getLocale()).print(net.time4j.Duration.compose(calendarPart, clockPart), TextWidth.WIDE);
	}

}
