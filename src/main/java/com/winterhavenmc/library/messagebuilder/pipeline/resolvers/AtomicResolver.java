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
import com.winterhavenmc.library.messagebuilder.pipeline.formatters.FormatterContainer;
import com.winterhavenmc.library.messagebuilder.pipeline.maps.MacroObjectMap;
import com.winterhavenmc.library.messagebuilder.pipeline.maps.MacroStringMap;
import com.winterhavenmc.library.messagebuilder.pipeline.formatters.duration.DurationFormatter;
import com.winterhavenmc.library.messagebuilder.pipeline.formatters.duration.BoundedDuration;
import com.winterhavenmc.library.messagebuilder.pipeline.formatters.number.LocaleNumberFormatter;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Optional;

import static com.winterhavenmc.library.messagebuilder.validation.ErrorMessageKey.PARAMETER_NULL;
import static com.winterhavenmc.library.messagebuilder.validation.Parameter.FORMATTER_CONTAINER;
import static com.winterhavenmc.library.messagebuilder.validation.Validator.throwing;
import static com.winterhavenmc.library.messagebuilder.validation.Validator.validate;


/**
 * A {@link Resolver} implementation that handles simple or atomic values,
 * converting them into formatted strings mapped directly to the base {@link MacroKey}.
 * <p>
 * The {@code AtomicResolver} is typically used as the final step in a resolution chain,
 * after more complex resolvers (such as {@link CompositeResolver}) have had a chance
 * to handle structured objects. It processes values such as {@link Boolean}, {@link String}, {@link Number},
 * {@link Duration}, and other primitive or directly representable types.
 * <p>
 * Formatting is applied for supported types:
 * <ul>
 *   <li>{@link Number} values are formatted using a {@link LocaleNumberFormatter}</li>
 *   <li>{@link Duration} and {@link BoundedDuration} values are formatted using a {@link DurationFormatter}</li>
 *   <li>All other values fall back to {@code toString()}</li>
 * </ul>
 * The resolved value is added under the original macro key only if no value already exists for it.
 *
 * @see Resolver
 * @see FieldResolver
 * @see CompositeResolver
 * @see LocaleNumberFormatter
 * @see DurationFormatter
 */
public class AtomicResolver implements Resolver
{
	private final DurationFormatter durationFormatter;
	private final LocaleNumberFormatter localeNumberFormatter;


	/**
	 * Constructs an {@code AtomicResolver} using the provided formatter container.
	 *
	 * @param formatterContainer a container providing formatters for durations and numbers
	 * @throws IllegalArgumentException if the formatter container is {@code null}
	 */
	public AtomicResolver(final FormatterContainer formatterContainer)
	{
		validate(formatterContainer, Objects::isNull, throwing(PARAMETER_NULL, FORMATTER_CONTAINER));

		this.durationFormatter = formatterContainer.durationFormatter();
		this.localeNumberFormatter = formatterContainer.localeNumberFormatter();
	}


	/**
	 * Resolves a single value mapped to the provided {@link MacroKey} into a
	 * string representation, if the object exists and can be formatted.
	 * <p>
	 * Only one entry is produced per resolution: a single mapping from the
	 * {@code macroKey} to the formatted string value.
	 *
	 * @param macroKey the key used to retrieve the value from the macro object map
	 * @param macroObjectMap the object map containing macro values to be resolved
	 * @return a {@link MacroStringMap} containing at most one resolved key-value pair
	 */
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
			case Boolean bool -> Optional.of(bool.toString());
			case String string -> Optional.of(string);
			case Number number -> Optional.of(localeNumberFormatter.getFormatted(number));
			case Duration duration -> Optional.of(durationFormatter.format(duration, ChronoUnit.SECONDS));
			case BoundedDuration boundedDuration -> Optional.of(durationFormatter
					.format(boundedDuration.duration(), boundedDuration.precision()));
			default -> Optional.of(value.toString());
		};
	}

}
