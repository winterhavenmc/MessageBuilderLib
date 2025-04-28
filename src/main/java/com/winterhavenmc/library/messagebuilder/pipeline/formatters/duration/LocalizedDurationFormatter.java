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

import com.winterhavenmc.library.messagebuilder.keys.RecordKey;
import com.winterhavenmc.library.messagebuilder.query.QueryHandler;
import com.winterhavenmc.library.messagebuilder.query.QueryHandlerFactory;
import com.winterhavenmc.library.messagebuilder.model.language.constant.ConstantRecord;
import com.winterhavenmc.library.messagebuilder.model.language.Section;
import com.winterhavenmc.library.messagebuilder.model.language.constant.ValidConstantRecord;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Objects;


public final class LocalizedDurationFormatter implements DurationFormatter
{
	private final DurationFormatter delegate;
	private final QueryHandlerFactory queryHandlerFactory;
	private static final RecordKey UNLIMITED_KEY = RecordKey.of("TIME.UNLIMITED").orElseThrow();
	private static final RecordKey LESS_THAN_KEY = RecordKey.of("TIME.LESS_THAN").orElseThrow();


	public LocalizedDurationFormatter(final DurationFormatter delegate, final QueryHandlerFactory queryHandlerFactory)
	{
		this.delegate = Objects.requireNonNull(delegate);
		this.queryHandlerFactory = queryHandlerFactory;
	}


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
	 * Transform a duration into a formatted string.
	 *
	 * @param duration the duration to be formatted
	 * @param lowerBound the lowest time unit to use in the formatted string
	 * @return a localized String representing a normal duration.
	 */
	String formatNormal(final Duration duration, final ChronoUnit lowerBound)
	{
		return delegate.format(duration, lowerBound);
	}


	/**
	 * Format duration string as configured UNLIMITED constant string in language file
	 *
	 * @return a localized String representing unlimited time.
	 */
	String formatUnlimited()
	{
		return getTimeConstant(UNLIMITED_KEY, DurationType.UNLIMITED);
	}


	/**
	 * Format a duration into a string using the configured LESS_THAN String constant in the language file.
	 *
	 * @param lowerBound the time unit to use in the formatted string
	 * @return a localized String representing less than a unit of time.
	 */
	String formatLessThan(final ChronoUnit lowerBound)
	{
		Duration duration = Duration.of(1, lowerBound);
		String formattedDuration = delegate.format(duration, lowerBound);
		String lessThanTemplate = getTimeConstant(LESS_THAN_KEY, DurationType.LESS_THAN);

		return lessThanTemplate.replace("{DURATION}", formattedDuration);
	}


	/**
	 * Retrieve the time string constant for a language file constant key
	 *
	 * @param constantKey the key in the language file CONSTANTS section
	 * @param durationType the duration type (NORMAL, UNLIMITED, LESS_THAN)
	 * @return a String value from the language file for the key
	 */
	String getTimeConstant(final RecordKey constantKey, final DurationType durationType)
	{
		QueryHandler<ConstantRecord> constantQueryHandler = queryHandlerFactory.getQueryHandler(Section.CONSTANTS);
		ConstantRecord timeConstant = constantQueryHandler.getRecord(constantKey);

		return (timeConstant instanceof ValidConstantRecord validRecord && validRecord.value() instanceof String string)
				? string
				: durationType.getFallback();
	}

}
