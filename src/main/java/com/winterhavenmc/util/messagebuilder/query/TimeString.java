/*
 * Copyright (c) 2024 Tim Savage.
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

package com.winterhavenmc.util.messagebuilder.query;

import com.winterhavenmc.util.TimeUnit;
import org.bukkit.configuration.Configuration;

public class TimeString {

	private final Configuration configuration;


	public TimeString(final Configuration configuration) {
		this.configuration = configuration;
	}

	/**
	 * Format the time string with days, hours, minutes and seconds as necessary, to the granularity passed
	 *
	 * @param duration a time duration in milliseconds
	 * @param timeUnit the time granularity to display (days | hours | minutes | seconds)
	 * @return formatted time string
	 */
	public String getTimeString(final long duration, final TimeUnit timeUnit) {
		//TODO: Refactor this entire method, using Java Duration or other date/time handling packages

		TimeUnit finalTimeUnit = timeUnit;

		// check for null parameter
		if (finalTimeUnit == null) {
			finalTimeUnit = TimeUnit.SECONDS;
		}

		// if duration is negative, return unlimited time string
		if (duration < 0) {
			String string = configuration.getString("TIME_STRINGS.UNLIMITED");
			if (string == null) {
				string = "unlimited";
			}
			return string;
		}

		// return string if less than 1 of passed timeUnit remains
		String lessString = configuration.getString("TIME_STRINGS.LESS_THAN_ONE");
		if (lessString == null) {
			lessString = "< 1";
		}
		if (finalTimeUnit.equals(TimeUnit.DAYS)
				&& TimeUnit.MILLISECONDS.toDays(duration) < 1) {
			return lessString + " " + configuration.getString("TIME_STRINGS.DAY");
		}
		if (finalTimeUnit.equals(TimeUnit.HOURS)
				&& TimeUnit.MILLISECONDS.toHours(duration) < 1) {
			return lessString + " " + configuration.getString("TIME_STRINGS.HOUR");
		}
		if (finalTimeUnit.equals(TimeUnit.MINUTES)
				&& TimeUnit.MILLISECONDS.toMinutes(duration) < 1) {
			return lessString + " " + configuration.getString("TIME_STRINGS.MINUTE");
		}
		if (finalTimeUnit.equals(TimeUnit.SECONDS)
				&& TimeUnit.MILLISECONDS.toSeconds(duration) < 1) {
			return lessString + " " + configuration.getString("TIME_STRINGS.SECOND");
		}


		StringBuilder timeString = new StringBuilder();

		int days = (int) TimeUnit.MILLISECONDS.toDays(duration);
		int hours = (int) TimeUnit.MILLISECONDS.toHours(duration % TimeUnit.DAYS.toMillis(1));
		int minutes = (int) TimeUnit.MILLISECONDS.toMinutes(duration % TimeUnit.HOURS.toMillis(1));
		int seconds = (int) TimeUnit.MILLISECONDS.toSeconds(duration % TimeUnit.MINUTES.toMillis(1));

		String dayString = configuration.getString("TIME_STRINGS.DAY");
		if (dayString == null) {
			dayString = "day";
		}
		String dayPluralString = configuration.getString("TIME_STRINGS.DAY_PLURAL");
		if (dayPluralString == null) {
			dayPluralString = "days";
		}
		String hourString = configuration.getString("TIME_STRINGS.HOUR");
		if (hourString == null) {
			hourString = "hour";
		}
		String hourPluralString = configuration.getString("TIME_STRINGS.HOUR_PLURAL");
		if (hourPluralString == null) {
			hourPluralString = "hours";
		}
		String minuteString = configuration.getString("TIME_STRINGS.MINUTE");
		if (minuteString == null) {
			minuteString = "minute";
		}
		String minutePluralString = configuration.getString("TIME_STRINGS.MINUTE_PLURAL");
		if (minutePluralString == null) {
			minutePluralString = "minutes";
		}
		String secondString = configuration.getString("TIME_STRINGS.SECOND");
		if (secondString == null) {
			secondString = "second";
		}
		String secondPluralString = configuration.getString("TIME_STRINGS.SECOND_PLURAL");
		if (secondPluralString == null) {
			secondPluralString = "seconds";
		}

		if (days > 1) {
			timeString.append(days);
			timeString.append(' ');
			timeString.append(dayPluralString);
			timeString.append(' ');
		}
		else if (days == 1) {
			timeString.append(days);
			timeString.append(' ');
			timeString.append(dayString);
			timeString.append(' ');
		}

		if (finalTimeUnit.equals(TimeUnit.HOURS)
				|| finalTimeUnit.equals(TimeUnit.MINUTES)
				|| finalTimeUnit.equals(TimeUnit.SECONDS)) {
			if (hours > 1) {
				timeString.append(hours);
				timeString.append(' ');
				timeString.append(hourPluralString);
				timeString.append(' ');
			}
			else if (hours == 1) {
				timeString.append(hours);
				timeString.append(' ');
				timeString.append(hourString);
				timeString.append(' ');
			}
		}

		if (finalTimeUnit.equals(TimeUnit.MINUTES) || finalTimeUnit.equals(TimeUnit.SECONDS)) {
			if (minutes > 1) {
				timeString.append(minutes);
				timeString.append(' ');
				timeString.append(minutePluralString);
				timeString.append(' ');
			}
			else if (minutes == 1) {
				timeString.append(minutes);
				timeString.append(' ');
				timeString.append(minuteString);
				timeString.append(' ');
			}
		}

		if (finalTimeUnit.equals(TimeUnit.SECONDS)) {
			if (seconds > 1) {
				timeString.append(seconds);
				timeString.append(' ');
				timeString.append(secondPluralString);
			}
			else if (seconds == 1) {
				timeString.append(seconds);
				timeString.append(' ');
				timeString.append(secondString);
			}
		}

		return timeString.toString().strip();
	}

}
