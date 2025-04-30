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

package com.winterhavenmc.library.messagebuilder.pipeline.resolvers;

import com.winterhavenmc.library.messagebuilder.keys.MacroKey;
import com.winterhavenmc.library.messagebuilder.pipeline.containers.MacroObjectMap;
import com.winterhavenmc.library.messagebuilder.pipeline.result.ResultMap;
import com.winterhavenmc.library.messagebuilder.pipeline.formatters.duration.DurationFormatter;
import com.winterhavenmc.library.messagebuilder.pipeline.formatters.duration.BoundedDuration;
import com.winterhavenmc.library.messagebuilder.pipeline.formatters.number.LocaleNumberFormatter;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Optional;

import static com.winterhavenmc.library.messagebuilder.validation.ErrorMessageKey.PARAMETER_NULL;
import static com.winterhavenmc.library.messagebuilder.validation.Parameter.RESOLVER_CONTEXT_CONTAINER;
import static com.winterhavenmc.library.messagebuilder.validation.ValidationHandler.throwing;
import static com.winterhavenmc.library.messagebuilder.validation.Validator.validate;


public class AtomicResolver implements Resolver
{
	private final DurationFormatter durationFormatter;
	private final LocaleNumberFormatter localeNumberFormatter;


	public AtomicResolver(final FormatterContainer formatterContainer)
	{
		validate(formatterContainer, Objects::isNull, throwing(PARAMETER_NULL, RESOLVER_CONTEXT_CONTAINER));

		this.durationFormatter = formatterContainer.durationFormatter();
		this.localeNumberFormatter = formatterContainer.localeNumberFormatter();
	}


	public ResultMap resolve(final MacroKey macroKey, final MacroObjectMap macroObjectMap)
	{
		ResultMap result = new ResultMap();

		macroObjectMap.get(macroKey)
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
