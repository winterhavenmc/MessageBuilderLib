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
import com.winterhavenmc.library.messagebuilder.pipeline.containers.MacroStringMap;
import com.winterhavenmc.library.messagebuilder.pipeline.formatters.duration.DurationFormatter;
import com.winterhavenmc.library.messagebuilder.pipeline.formatters.duration.BoundedDuration;
import com.winterhavenmc.library.messagebuilder.pipeline.formatters.number.LocaleNumberFormatter;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Optional;

import static com.winterhavenmc.library.messagebuilder.validation.ErrorMessageKey.PARAMETER_NULL;
import static com.winterhavenmc.library.messagebuilder.validation.Parameter.FORMATTER_CONTAINER;
import static com.winterhavenmc.library.messagebuilder.validation.ValidationHandler.throwing;
import static com.winterhavenmc.library.messagebuilder.validation.Validator.validate;


/**
 * Resolves simple object values that have only one value that is mapped to the root level key in the result map.
 */
public class AtomicResolver implements Resolver
{
	private final DurationFormatter durationFormatter;
	private final LocaleNumberFormatter localeNumberFormatter;


	public AtomicResolver(final FormatterContainer formatterContainer)
	{
		validate(formatterContainer, Objects::isNull, throwing(PARAMETER_NULL, FORMATTER_CONTAINER));

		this.durationFormatter = formatterContainer.durationFormatter();
		this.localeNumberFormatter = formatterContainer.localeNumberFormatter();
	}


	public MacroStringMap resolve(final MacroKey macroKey, final MacroObjectMap macroObjectMap)
	{
		MacroStringMap stringMap = new MacroStringMap();

		macroObjectMap.get(macroKey)
				.flatMap(this::resolveAtomic)
				.ifPresent(formatted -> stringMap.putIfAbsent(macroKey, formatted));

		return stringMap;
	}


	private Optional<String> resolveAtomic(final Object value)
	{
		return switch (value)
		{
			// case Boolean bool -> result.putIfAbsent(macroKey, bool.toString());
			case String string -> Optional.of(string);
			case Number number -> Optional.of(localeNumberFormatter.getFormatted(number));
			case Duration duration -> Optional.of(durationFormatter.format(duration, ChronoUnit.SECONDS));
			case BoundedDuration boundedDuration -> Optional.of(durationFormatter
					.format(boundedDuration.duration(), boundedDuration.precision()));
			default -> Optional.of(value.toString());
		};
	}

}
