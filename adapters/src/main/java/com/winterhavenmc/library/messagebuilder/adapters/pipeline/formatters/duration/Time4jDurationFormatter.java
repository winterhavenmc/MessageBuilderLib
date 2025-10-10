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

package com.winterhavenmc.library.messagebuilder.adapters.pipeline.formatters.duration;

import com.winterhavenmc.library.messagebuilder.models.configuration.LocaleProvider;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.formatters.duration.DurationFormatter;
import com.winterhavenmc.library.messagebuilder.models.validation.LogLevel;

import net.time4j.CalendarUnit;
import net.time4j.ClockUnit;
import net.time4j.PrettyTime;
import net.time4j.format.TextWidth;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import static com.winterhavenmc.library.messagebuilder.models.validation.ErrorMessageKey.PARAMETER_NULL;
import static com.winterhavenmc.library.messagebuilder.models.validation.Parameter.*;
import static com.winterhavenmc.library.messagebuilder.models.validation.Validator.logging;
import static com.winterhavenmc.library.messagebuilder.models.validation.Validator.validate;


/**
 * A {@link DurationFormatter} implementation that uses the Time4J library's {@link PrettyTime}
 * to render {@link java.time.Duration} values as human-readable, localized strings.
 *
 * <p>This formatter decomposes the {@code Duration} into calendar and clock units and uses Time4J’s
 * formatting capabilities to generate grammatically correct, locale-aware strings with wide text style.
 * For example, a duration of 2 days, 3 hours, and 4 minutes might be rendered as:
 * {@code "2 days, 3 hours, and 4 minutes"} in English, or a culturally appropriate equivalent in other locales.
 *
 * <p>If the provided {@code Duration} or {@code ChronoUnit} is {@code null}, this class falls back
 * to a default duration of 0 and a default lower bound of {@link ChronoUnit#MINUTES}, respectively,
 * with warnings logged through the configured logging strategy.
 *
 * <p>The {@code lowerBound} determines the level of truncation applied before formatting. For example,
 * a lower bound of {@code ChronoUnit.MINUTES} would discard all precision below minutes.
 *
 * @see PrettyTime
 * @see LocaleProvider
 * @see DurationFormatter
 */
public final class Time4jDurationFormatter implements DurationFormatter
{
	private final LocaleProvider localeProvider;


	/**
	 * Constructs a {@code Time4jDurationFormatter} with the provided {@link LocaleProvider}.
	 *
	 * @param localeProvider supplies the {@link java.util.Locale} to use for formatting
	 */
	public Time4jDurationFormatter(final LocaleProvider localeProvider)
	{
		this.localeProvider = localeProvider;
	}


	/**
	 * Formats a {@link java.time.Duration} using Time4J's {@link PrettyTime} engine.
	 * The duration is truncated to the given {@code lowerBound}, and formatted into a
	 * locale-sensitive string using {@code TextWidth.WIDE}.
	 *
	 * <p>The method maps Java {@code Duration} to Time4J's {@code Duration} classes
	 * by separating calendar and clock units before composing them for formatting.
	 *
	 * @param duration the duration to format; {@code null} defaults to {@code Duration.ZERO}
	 * @param lowerBound the lowest time unit to preserve; {@code null} defaults to {@code ChronoUnit.MINUTES}
	 * @return a localized, human-friendly string representation of the duration
	 */
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
