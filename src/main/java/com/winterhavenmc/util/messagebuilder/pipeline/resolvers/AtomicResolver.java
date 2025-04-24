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

package com.winterhavenmc.util.messagebuilder.pipeline.resolvers;

import com.winterhavenmc.util.messagebuilder.keys.MacroKey;
import com.winterhavenmc.util.messagebuilder.pipeline.context.ContextMap;
import com.winterhavenmc.util.messagebuilder.pipeline.result.ResultMap;
import com.winterhavenmc.util.messagebuilder.pipeline.formatters.duration.DurationFormatter;
import com.winterhavenmc.util.messagebuilder.pipeline.formatters.duration.BoundedDuration;
import com.winterhavenmc.util.messagebuilder.pipeline.formatters.number.LocaleNumberFormatter;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Optional;


public class AtomicResolver implements Resolver
{
	private final DurationFormatter durationFormatter;
	private final LocaleNumberFormatter localeNumberFormatter;


	public AtomicResolver(final ResolverContextContainer resolverContextContainer)
	{
		this.durationFormatter = resolverContextContainer.durationFormatter();
		localeNumberFormatter = resolverContextContainer.localeNumberFormatter();
	}


	public ResultMap resolve(final MacroKey macroKey, final ContextMap contextMap)
	{
		ResultMap result = new ResultMap();

		contextMap.get(macroKey)
				.flatMap(this::resolveAtomic)
				.ifPresent(formatted -> result.putIfAbsent(macroKey, formatted));

		return result;
	}


	private Optional<String> resolveAtomic(final Object value) {
		return switch (value) {
			// TODO: Replace with record pattern match when Java 22+ is standard in Bukkit
			case BoundedDuration boundedDuration -> Optional.of(durationFormatter
					.format(boundedDuration.duration(), boundedDuration.precision()));
			case Duration duration -> Optional.of(durationFormatter.format(duration, ChronoUnit.SECONDS));
			case Number number -> Optional.of(localeNumberFormatter.getFormatted(number));
//			case Boolean bool -> result.putIfAbsent(macroKey, bool.toString());
			case String string -> Optional.of(string);
			default -> Optional.of(value.toString());
		};
	}

}
