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

import com.winterhavenmc.util.messagebuilder.keys.RecordKey;
import com.winterhavenmc.util.messagebuilder.resources.QueryHandler;
import com.winterhavenmc.util.messagebuilder.resources.QueryHandlerFactory;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.ConstantRecord;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.Section;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.ValidConstantRecord;

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
	public String format(final Duration duration, final ChronoUnit precision)
	{
		DurationType type = DurationType.classify(duration, precision);

		return switch (type)
		{
			case UNLIMITED -> formatUnlimited();
			case LESS_THAN -> formatLessThan(precision);
			case NORMAL    -> formatNormal(duration, precision);
		};
	}


	String formatUnlimited()
	{
		return getTimeConstant(UNLIMITED_KEY, DurationType.UNLIMITED);
	}


	String formatLessThan(ChronoUnit precision)
	{
		Duration duration = Duration.of(1, precision);
		String formattedDuration = delegate.format(duration, precision);
		String lessThanTemplate = getTimeConstant(LESS_THAN_KEY, DurationType.LESS_THAN);

		return lessThanTemplate.replace("{DURATION}", formattedDuration);
	}


	String formatNormal(Duration duration, ChronoUnit precision)
	{
		return delegate.format(duration, precision);
	}


	private String getTimeConstant(RecordKey constantKey, DurationType durationType)
	{
		QueryHandler<ConstantRecord> constantQueryHandler = queryHandlerFactory.getQueryHandler(Section.CONSTANTS);
		ConstantRecord timeConstant = constantQueryHandler.getRecord(constantKey);

		return (timeConstant instanceof ValidConstantRecord validRecord && validRecord.value() instanceof String string)
				? string
				: durationType.getFallback();
	}

}
