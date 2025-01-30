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

import com.winterhavenmc.util.messagebuilder.util.LocalizedException;
import net.time4j.CalendarUnit;
import net.time4j.ClockUnit;
import net.time4j.Duration;
import net.time4j.PrettyTime;
import net.time4j.format.TextWidth;

import java.util.Locale;

import static com.winterhavenmc.util.messagebuilder.util.LocalizedException.MessageKey.PARAMETER_NULL;
import static com.winterhavenmc.util.messagebuilder.util.LocalizedException.Parameter.DURATION;
import static com.winterhavenmc.util.messagebuilder.util.LocalizedException.Parameter.LOCALE;


public final class PrettyTimeFormatter implements TimeFormatter {

	/**
	 * Retrieve a {@link PrettyTime} string for the given amount of milliseconds, translated for the locale provided.
	 *
	 * @param locale the Locale to use for {@code PrettyTime} translation and pluralization
	 * @param duration a time duration
	 * @return the {@code PrettyTime} formatted string
	 */
	public String getFormatted(final Locale locale, final java.time.Duration duration) {
		if (locale == null) { throw new LocalizedException(PARAMETER_NULL, LOCALE); }
		if (duration == null) { throw new LocalizedException(PARAMETER_NULL, DURATION); }

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
