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

package com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.time;

import com.winterhavenmc.util.time.TimeUnit;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.YamlConfigurationSupplier;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.Section;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.SectionQueryHandler;
import com.winterhavenmc.util.messagebuilder.util.Error;

import org.bukkit.configuration.ConfigurationSection;

import java.time.Duration;
import java.util.List;

import static com.winterhavenmc.util.messagebuilder.util.Error.Parameter.NULL_DURATION;
import static com.winterhavenmc.util.messagebuilder.util.Error.Parameter.NULL_TIME_UNIT;


/**
 * A ItemRecord handler that is solely responsible for the {@code TIME} section of the language file. Provides methods
 * for retrieving String translations related to time, and to produce a time string from a {@code long} millisecond
 * duration, listing the appropriate time units and their singular or plural names down to the granularity
 * specified by the provided TimeUnit constant.
 */
public class TimeSectionQueryHandler implements SectionQueryHandler {

	public static final String LESS_THAN_ONE_KEY = "OTHER.LESS_THAN_ONE";
	public static final String LESS_THAN_KEY = "OTHER.LESS_THAN";
	public static final String UNLIMITED_KEY = "OTHER.UNLIMITED";
	public static final String UNIT_SECTION = "UNITS";
	public static final String PATH_DELIMITER = ".";

	private final ConfigurationSection timeSection;


	/**
	 * Class constructor
	 *
	 * @param configurationSupplier the supplier for the configuration object for the language file
	 */
	public TimeSectionQueryHandler(final YamlConfigurationSupplier configurationSupplier) {
		if (configurationSupplier == null) { throw new IllegalArgumentException(Error.Parameter.NULL_CONFIGURATION_SECTION.getMessage()); }

		// check that 'TIME' section returned by the configuration supplier is not null
		if (configurationSupplier.getSection(Section.TIME) == null) {
			throw new IllegalArgumentException(Error.Parameter.INVALID_SECTION_TIME.getMessage());
		}

		this.timeSection = configurationSupplier.getSection(Section.TIME);
	}


	/**
	 * Return the Section constant for this query handler type
	 *
	 * @return the TIME Section constant, establishing this query handler type
	 */
	@Override
	public Section getSectionType() {
		return Section.TIME;
	}


	/**
	 * The primary type returned by this query handler. A query handler may provide methods that return
	 * values of other types.
	 *
	 * @return String.class as the primary type returned by this query handler
	 */
	@Override
	public Class<?> getHandledType() {
		return String.class;
	}


	/**
	 * A list of the types returned by this query handler. A query handler should not provide methods that return
	 * values of other types.
	 *
	 * @return {@code List} of class types that are handled by this query handler
	 */
	@Override
	public List<Class<?>> listHandledTypes() {
		return List.of(String.class);
	}


	/**
	 * Get the 'less than one' string from the current language file
	 *
	 * @return String containing 'less than one' in the currently configured language
	 */
	public String getLessThanOneString() {
		return timeSection.getString(LESS_THAN_ONE_KEY);
	}


	/**
	 * Get the 'less than' string from the current language file
	 *
	 * @return String containing 'less than' in the currently configured language
	 */
	public String getLessThan() {
		return timeSection.getString(LESS_THAN_KEY);
	}


	/**
	 * Get the 'unlimited time' string from the current language file
	 *
	 * @return String containing 'unlimited time' in the currently configured language
	 */
	public String getUnlimited() {
		return timeSection.getString(UNLIMITED_KEY);
	}


	/**
	 * Get the singular name for the provided TimeUnit.
	 *
	 * @param timeUnit The TimeUnit for which to retrieve the singular name
	 * @return String containing the singular name of the provided TimeUnit in the currently configured language.
	 */
	public String getSingular(final TimeUnit timeUnit) {
		return timeSection.getString(String.join(PATH_DELIMITER,UNIT_SECTION, timeUnit.name(), "SINGULAR"));
	}


	/**
	 * Get the plural name for the provided TimeUnit.
	 *
	 * @param timeUnit The TimeUnit for which to retrieve the singular name
	 * @return String containing the singular name of the provided TimeUnit in the currently configured language.
	 */
	
	public String getPlural(final TimeUnit timeUnit) {
		return timeSection.getString(String.join(PATH_DELIMITER, UNIT_SECTION, timeUnit.name(), "PLURAL"));
	}


	/**
	 * Get the plural or singular version of a {@code TimeUnit} appropriate for the provided duration.
	 *
	 * @param duration the duration for which a pluralized TimeUnit name will be returned
	 * @param timeUnit the TimeUnit for which to retrieve a pluralized name
	 * @return the singular or plural version of a TimeUnit name, appropriate for the provided duration.
	 */
	
	public String getPluralized(final long duration, final TimeUnit timeUnit) {
		// if duration is one, return singular name for timeUnit
		if (duration == timeUnit.one()) {
			return getSingular(timeUnit);
		}
		return getPlural(timeUnit);
	}


	/**
	 * Retrieve the localized String from the language file for the phrase 'less than one {timeUnit}'
	 * for the provided {@code TimeUnit} in the currently configured language.
	 *
	 * @param timeUnit the time unit name to append to the phrase
	 * @return a localized String containing the phrase 'less than one {timeUnit}' equivalent in the
	 * currently configured language.
	 */
	
	public String getLessThanOneString(TimeUnit timeUnit) {
		if (timeUnit == null) { throw new IllegalArgumentException(NULL_TIME_UNIT.getMessage()); }

		// get 'less than one' string from TIME section language file
		String lessThanOne = getLessThanOneString();

		// if no string could be found for LESS_THAN_ONE key, return mathematical expression for less than one
		if (lessThanOne == null) {
			lessThanOne = "< 1";
		}

		// get singular name for timeUnit from TIME section of language file
		String timeUnitString = getSingular(timeUnit);

		// if no string could be found for the singular name of the timeUnit, use the timeUnit name (which is plural)
		if (timeUnitString == null) {
			timeUnitString = timeUnit.name().toLowerCase();
		}

		// return joined string 'less than one' + singular timeUnit name
		return String.join(" ", lessThanOne, timeUnitString).strip();
	}


	/**
	 * Get a formatted time string for a {@code long} duration to the granularity
	 * of a constant in the {@link TimeUnit} enum.
	 *
	 * @param duration the duration in milliseconds to be used to calculate the time string
	 * @param timeUnit a {@code TimeUnit} constant representing the minimum granularity to include in the returned time string
	 * @return {@code String} representation of the duration, using time units returned from the
	 * language file, to the specified granularity
	 */
	
	public String getTimeString(final Long duration, final TimeUnit timeUnit) {
		if (duration == null) { throw new IllegalArgumentException(NULL_DURATION.getMessage()); }
		if (timeUnit == null) { throw new IllegalArgumentException(NULL_TIME_UNIT.getMessage()); }

		// if duration is negative, return string for unlimited time
		if (duration < 0) {
			return getUnlimited();
		}

		// if duration is less than duration of timeUnit, return string 'less than one {timeUnit.SINGULAR}'
		if (duration < timeUnit.getMillis()) {
			return getLessThanOneString(timeUnit);
		}

		// I think we're good to here
		return getFormattedTimeString(duration, timeUnit);
	}


	/**
	 * This method produces the formatted time string, and backs the getTimeString method after any
	 * tests for alternatives to the formatted time string, such as 'less than one' or 'unlimited',
	 * have been exasperated.
	 *
	 * @param durationMillis the duration in milliseconds for which to produce a time string
	 * @param timeUnit the minimum granularity of the time string. No TimeUnits will be appended
	 *                 after this TimeUnit has been reached.
	 * @return the formatted time string
	 */
	private String getFormattedTimeString(final long durationMillis, final TimeUnit timeUnit) {

		// convert long millis to duration
		Duration duration = Duration.ofMillis(durationMillis);

		// start string builder
		StringBuilder timeString = new StringBuilder();

		// Append all time unit values and labels above the granularity of the provided TimeUnit that are not equal to zero
		if (timeUnit.getMillis() <= TimeUnit.DAYS.getMillis()) {
			long days = duration.toDaysPart();
			if (days > 0) {
				appendUnit(timeString, days, TimeUnit.DAYS);
			}
		}

		// append hours to timeString
		if (timeUnit.getMillis() <= TimeUnit.HOURS.getMillis()) {
			long hours = duration.toHoursPart();
			if (hours > 0) {
				appendUnit(timeString, hours, TimeUnit.HOURS);
			}
		}

		// append minutes to timeString
		if (timeUnit.getMillis() <= TimeUnit.MINUTES.getMillis()) {
			long minutes = duration.toMinutesPart();
			if (minutes > 0) {
				appendUnit(timeString, minutes, TimeUnit.MINUTES);
			}
		}

		// append seconds to timeString
		if (timeUnit.getMillis() <= (TimeUnit.SECONDS.getMillis())) {
			long seconds = duration.toSecondsPart();
			if (seconds > 0) {
				appendUnit(timeString, seconds, TimeUnit.SECONDS);
			}
		}

		// Debug statement to verify intermediate outputs
		System.out.println("Constructed time string: " + timeString);

		return timeString.toString().strip();
	}


	/**
	 * Append a time unit and its appropriate singular or plural name from the language file for the
	 * currently configured language. If the value representing the duration of TimeUnits to be displayed
	 * is one (1), the singular name for the TimeUnit is used. If the value is any other number, the plural
	 * name for the TimeUnit will be used.
	 *
	 * @param sb a StringBuilder that holds the time string as constructed thus far
	 * @param value the duration of TimeUnits to be displayed for this
	 * @param timeUnit the TimeUnit and its duration to be appended to the StringBuilder
	 */
	private void appendUnit(StringBuilder sb, long value, TimeUnit timeUnit) {

		// add delimiter before adding this unit unless this is the first unit to be added
		if (!sb.isEmpty()) {
			sb.append(", "); // Add delimiter between units
		}

		// append value to string
		sb.append(value);

		// append space to string
		sb.append(' ');

		// if value equals one (1), append singular version of unit name (hour, minute)
		if (value == 1) {
			sb.append(getSingular(timeUnit));
		}
		// otherwise, append plural version of unit name (hours, minutes)
		else {
			sb.append(getPlural(timeUnit));
		}
	}

}
