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

import com.winterhavenmc.util.TimeUnit;
import com.winterhavenmc.util.messagebuilder.query.LanguageFileQueryHandler;

import java.time.Duration;

public class TimeString {

	private final LanguageFileQueryHandler queryHandler;

	public TimeString(final LanguageFileQueryHandler queryHandler) {
		this.queryHandler = queryHandler;
	}

	public String getTimeString(final long durationMillis, final TimeUnit timeUnit) {
		// Handle invalid time unit and unlimited durations
		if (timeUnit == null) {
			throw new IllegalArgumentException("timeUnit cannot be null");
		}

		if (durationMillis < 0) {
			return queryHandler.getString("TIME_STRINGS.UNLIMITED").orElse("unlimited");
		}

		// Convert duration and prepare formatted string
		Duration duration = Duration.ofMillis(durationMillis);

		long days = duration.toDays();
		long hours = duration.toHoursPart();
		long minutes = duration.toMinutesPart();
		long seconds = duration.toSecondsPart();

		// Handle cases where the time is less than the specified unit
		String lessString = queryHandler.getString("TIME_STRINGS.LESS_THAN_ONE").orElse("< 1");
		if (timeUnit == TimeUnit.DAYS && days < 1) {
			return lessString + " " + getUnitString("DAY");
		}
		else if (timeUnit == TimeUnit.HOURS && duration.toHours() < 1) {
			return lessString + " " + getUnitString("HOUR");
		}
		else if (timeUnit == TimeUnit.MINUTES && duration.toMinutes() < 1) {
			return lessString + " " + getUnitString("MINUTE");
		}
		else if (timeUnit == TimeUnit.SECONDS && duration.toSeconds() < 1) {
			return lessString + " " + getUnitString("SECOND");
		}

		// Build formatted time string
		StringBuilder timeString = new StringBuilder();

		appendUnit(timeString, days, "DAY");
		if (timeUnit.ordinal() >= TimeUnit.HOURS.ordinal()) {
			appendUnit(timeString, hours, "HOUR");
		}
		if (timeUnit.ordinal() >= TimeUnit.MINUTES.ordinal()) {
			appendUnit(timeString, minutes, "MINUTE");
		}
		if (timeUnit.ordinal() >= TimeUnit.SECONDS.ordinal()) {
			appendUnit(timeString, seconds, "SECOND");
		}

		return timeString.toString().strip();
	}

	private void appendUnit(StringBuilder sb, long value, String unitKey) {
		if (value > 0) {
			sb.append(value).append(' ')
					.append(getUnitString(value == 1 ? unitKey : unitKey + "_PLURAL"))
					.append(' ');
		}
	}

	private String getUnitString(String key) {
		return queryHandler.getString("TIME_STRINGS." + key)
				.orElse(key.toLowerCase().replace("_", " "));
	}
}
