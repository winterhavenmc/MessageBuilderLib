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

import com.winterhavenmc.util.messagebuilder.util.Error;
import net.time4j.CalendarUnit;
import net.time4j.ClockUnit;
import net.time4j.Duration;
import net.time4j.PrettyTime;
import net.time4j.format.TextWidth;

import java.util.Locale;

import static com.winterhavenmc.util.messagebuilder.MessageBuilder.bundle;


public final class TimeString {

private TimeString() { /* private constructor to prevent instantiation */ }

	/**
	 * Retrieve a {@link PrettyTime} string for the given amount of milliseconds, with the US Locale
	 *
	 * @param millis a time duration represented in milliseconds
	 * @return the {@code PrettyTime} formated string
	 */
	public static String getTimeString(final long millis) {
		return getTimeString(Locale.US, millis);
	}


	/**
	 * Retrieve a {@link PrettyTime} string for the given amount of milliseconds, translated for the locale
	 * represented by the provided IETF language tag string. If no Locale can be found from the provided language tag,
	 * the US locale will be used.
	 *
	 * @param languageTag the IETF language tag that represents the locale for which the string should be translated
	 * @param millis a time duration in milliseconds
	 * @return the {@code PrettyTime} formatted string
	 */
	public static String getTimeString(final String languageTag, final long millis) {
		if (languageTag == null) { throw new IllegalArgumentException(bundle.getString(Error.Parameter.NULL_LANGUAGE_TAG.name())); }

		Locale locale = Locale.forLanguageTag(languageTag);
		if (locale == null) { locale = Locale.US; }

		return getTimeString(locale, millis);
	}


	/**
	 * Retrieve a {@link PrettyTime} string for the given amount of milliseconds, translated for the locale provided.
	 *
	 * @param locale the Locale to use for {@code PrettyTime} translation and pluralization
	 * @param millis a time duration in milliseconds
	 * @return the {@code PrettyTime} formatted string
	 */
	public static String getTimeString(final Locale locale, final long millis) {
		if (locale == null) { throw new IllegalArgumentException(bundle.getString(Error.Parameter.NULL_LOCALE.name())); }

		PrettyTime prettyTime = PrettyTime.of(locale);

		Duration<?> duration = Duration.of(millis, ClockUnit.MILLIS).toClockPeriod();

		Duration<CalendarUnit> calendarPart = duration.toCalendarPeriod();
		Duration<ClockUnit> clockPart = duration.toClockPeriod();

		clockPart = clockPart.with(ClockUnit.SECONDS.rounded());

		// time unit is less than one day and more than one hour, display hours, minutes
		return prettyTime.print(Duration.compose(calendarPart, clockPart), TextWidth.WIDE);
	}

}
