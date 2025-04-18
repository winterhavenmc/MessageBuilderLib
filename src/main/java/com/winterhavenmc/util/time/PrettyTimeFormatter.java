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
import net.time4j.CalendarUnit;
import net.time4j.ClockUnit;
import net.time4j.Duration;
import net.time4j.PrettyTime;
import net.time4j.format.TextWidth;

import java.util.Locale;
import java.util.Objects;

import static com.winterhavenmc.util.messagebuilder.validation.ErrorMessageKey.PARAMETER_NULL;
import static com.winterhavenmc.util.messagebuilder.validation.Parameter.DURATION;
import static com.winterhavenmc.util.messagebuilder.validation.Parameter.LOCALE;
import static com.winterhavenmc.util.messagebuilder.validation.ValidationHandler.throwing;
import static com.winterhavenmc.util.messagebuilder.validation.Validator.validate;


/**
 * Format a time string using the Time4J PrettyTime formatter. When given a locale and a duration, a formatted string
 * with pluralized time units appropriate for the duration is returned as a string.
 */
public final class PrettyTimeFormatter implements TimeFormatter
{
	private final LocaleSupplier localeSupplier;


	public PrettyTimeFormatter(LocaleSupplier localeSupplier)
	{
		this.localeSupplier = localeSupplier;
	}


	/**
	 * Return a {@link PrettyTime} string for the given amount of milliseconds, translated for the locale
	 * set in the config.yml file, or Locale.getDefault() if a valid config setting is not found.<br>
	 * <b><i>Note:</i></b> Duration type used in this method are of the net.time4j.Duration type, except for
	 * the java.time.Duration type that is passed in as a parameter
	 *
	 * @param duration a time duration
	 * @return the {@code PrettyTime} formatted string
	 */
	public String getFormatted(final java.time.Duration duration)
	{
		Locale locale = localeSupplier.get();

		validate(locale, Objects::isNull, throwing(PARAMETER_NULL, LOCALE));
		validate(duration, Objects::isNull, throwing(PARAMETER_NULL, DURATION));

		// get instance of PrettyTime
		PrettyTime prettyTime = PrettyTime.of(locale);

		// convert java.time.Duration to net.time4j.Duration
		Duration<?> t4jDuration = Duration.from(duration).toClockPeriod();

		Duration<CalendarUnit> calendarPart = t4jDuration.toCalendarPeriod();
		Duration<ClockUnit> clockPart = t4jDuration.toClockPeriod();

		clockPart = clockPart.with(ClockUnit.SECONDS.rounded());

		// time unit is less than one day and more than one hour, display hours, minutes
		return prettyTime.print(Duration.compose(calendarPart, clockPart), TextWidth.WIDE);
	}


	/**
	 * Return a {@link PrettyTime} string for the given amount of milliseconds, translated for the locale provided.<br>
	 * <b><i>Note:</i></b> Duration type used in this method are of the net.time4j.Duration type, except for
	 * the java.time.Duration type that is accepted as a parameter
	 *
	 * @param locale the Locale to use for {@code PrettyTime} translation and pluralization
	 * @param duration a time duration
	 * @return the {@code PrettyTime} formatted string
	 */
	public String getFormatted(final Locale locale, final java.time.Duration duration)
	{
		validate(locale, Objects::isNull, throwing(PARAMETER_NULL, LOCALE));
		validate(duration, Objects::isNull, throwing(PARAMETER_NULL, DURATION));

		// get instance of PrettyTime
		PrettyTime prettyTime = PrettyTime.of(locale);

		// convert java.time.Duration to net.time4j.Duration
		Duration<?> t4jDuration = Duration.from(duration).toClockPeriod();

		Duration<CalendarUnit> calendarPart = t4jDuration.toCalendarPeriod();
		Duration<ClockUnit> clockPart = t4jDuration.toClockPeriod();

		clockPart = clockPart.with(ClockUnit.SECONDS.rounded());

		// time unit is less than one day and more than one hour, display hours, minutes
		return prettyTime.print(Duration.compose(calendarPart, clockPart), TextWidth.WIDE);
	}

}
