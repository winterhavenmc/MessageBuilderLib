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

import com.winterhavenmc.library.messagebuilder.keys.ConstantKey;
import com.winterhavenmc.library.messagebuilder.keys.ValidConstantKey;
import com.winterhavenmc.library.messagebuilder.query.QueryHandler;
import com.winterhavenmc.library.messagebuilder.query.QueryHandlerFactory;
import com.winterhavenmc.library.messagebuilder.model.language.ConstantRecord;
import com.winterhavenmc.library.messagebuilder.model.language.Section;
import com.winterhavenmc.library.messagebuilder.model.language.ValidConstantRecord;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Objects;


/**
 * A {@link DurationFormatter} implementation that localizes duration strings using configurable
 * constants from the language file, while delegating formatting of standard durations to another formatter.
 *
 * <p>This class wraps a delegate {@code DurationFormatter}, enhancing it with support for
 * localized representations of special duration types such as {@code UNLIMITED} and {@code LESS_THAN}.
 *
 * <p>The localized constants are fetched from the {@code CONSTANTS} section of the language file using
 * the following keys:
 * <ul>
 *   <li>{@code TIME.UNLIMITED} – defines the display string for unlimited (negative) durations</li>
 *   <li>{@code TIME.LESS_THAN} – a template string for durations shorter than the given precision</li>
 * </ul>
 *
 * <p>If the constants are not found or improperly configured, fallback values from the associated
 * {@link DurationType} enum are used.
 *
 * @see DurationFormatter
 * @see DurationType
 * @see Time4jDurationFormatter
 * @see com.winterhavenmc.library.messagebuilder.model.language.Section#CONSTANTS
 */
public final class LocalizedDurationFormatter implements DurationFormatter
{
	private final DurationFormatter delegate;
	private final QueryHandlerFactory queryHandlerFactory;
	private static final ValidConstantKey UNLIMITED_KEY = ConstantKey.of("TIME.UNLIMITED").isValid().orElseThrow();
	private static final ValidConstantKey LESS_THAN_KEY = ConstantKey.of("TIME.LESS_THAN").isValid().orElseThrow();


	/**
	 * Constructs a {@code LocalizedDurationFormatter} with a backing delegate and query handler factory.
	 *
	 * @param delegate the base formatter to handle standard durations
	 * @param queryHandlerFactory factory for retrieving language constants from the configuration
	 */
	public LocalizedDurationFormatter(final DurationFormatter delegate, final QueryHandlerFactory queryHandlerFactory)
	{
		this.delegate = Objects.requireNonNull(delegate);
		this.queryHandlerFactory = queryHandlerFactory;
	}


	/**
	 * Formats a {@link Duration} into a localized string, applying special handling for unlimited or
	 * too-small durations.
	 *
	 * @param duration the duration to format
	 * @param lowerBound the smallest time unit to represent (e.g., SECONDS)
	 * @return a formatted string representation of the duration, localized as needed
	 */
	@Override
	public String format(final Duration duration, final ChronoUnit lowerBound)
	{
		DurationType type = DurationType.classify(duration, lowerBound);

		return switch (type)
		{
			case NORMAL    -> formatNormal(duration, lowerBound);
			case UNLIMITED -> formatUnlimited();
			case LESS_THAN -> formatLessThan(lowerBound);
		};
	}


	/**
	 * Formats a standard duration using the delegate formatter.
	 *
	 * @param duration the duration to format
	 * @param lowerBound the smallest time unit to include
	 * @return a formatted string for a normal duration
	 */
	String formatNormal(final Duration duration, final ChronoUnit lowerBound)
	{
		return delegate.format(duration, lowerBound);
	}


	/**
	 * Retrieves the configured string constant for unlimited durations.
	 *
	 * @return a localized string representing an unlimited time
	 */
	String formatUnlimited()
	{
		return getTimeConstant(UNLIMITED_KEY, DurationType.UNLIMITED);
	}


	/**
	 * Formats a "less than" string for durations smaller than the lower bound.
	 * Uses the template string from the configuration and fills in a sample value.
	 *
	 * @param lowerBound the smallest unit of time to represent
	 * @return a localized "less than" duration string
	 */
	String formatLessThan(final ChronoUnit lowerBound)
	{
		Duration duration = Duration.of(1, lowerBound);
		String formattedDuration = delegate.format(duration, lowerBound);
		String lessThanTemplate = getTimeConstant(LESS_THAN_KEY, DurationType.LESS_THAN);

		return lessThanTemplate.replace("{DURATION}", formattedDuration);
	}


	/**
	 * Retrieves a string constant from the language file, falling back to a default
	 * if the constant is missing or invalid.
	 *
	 * @param constantKey the configuration string to fetch
	 * @param durationType the fallback duration type if the constant is unavailable
	 * @return a localized or fallback string
	 */
	String getTimeConstant(final ValidConstantKey constantKey, final DurationType durationType)
	{
		QueryHandler<ConstantRecord> constantQueryHandler = queryHandlerFactory.getQueryHandler(Section.CONSTANTS);
		ConstantRecord timeConstant = constantQueryHandler.getRecord(constantKey);

		return (timeConstant instanceof ValidConstantRecord validRecord && validRecord.value() instanceof String string)
				? string
				: durationType.getFallback();
	}

}
