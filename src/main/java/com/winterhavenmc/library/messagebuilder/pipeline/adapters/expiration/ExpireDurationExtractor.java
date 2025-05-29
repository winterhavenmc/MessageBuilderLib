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

package com.winterhavenmc.library.messagebuilder.pipeline.adapters.expiration;

import com.winterhavenmc.library.messagebuilder.keys.MacroKey;
import com.winterhavenmc.library.messagebuilder.pipeline.containers.MacroStringMap;
import com.winterhavenmc.library.messagebuilder.pipeline.formatters.duration.DurationFormatter;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static com.winterhavenmc.library.messagebuilder.pipeline.adapters.Adapter.BuiltIn.EXPIRE;
import static com.winterhavenmc.library.messagebuilder.pipeline.adapters.Adapter.BuiltIn.DURATION;
import static com.winterhavenmc.library.messagebuilder.pipeline.adapters.Adapter.BuiltIn.INSTANT;


@Deprecated
public class ExpireDurationExtractor
{
	private final static String UNKNOWN_VALUE = "-";
	private final DurationFormatter durationFormatter;

	public ExpireDurationExtractor(DurationFormatter durationFormatter)
	{
		this.durationFormatter = durationFormatter;
	}


	MacroStringMap extract(final MacroKey baseKey, final Expirable expirable)
	{
		MacroStringMap resultMap = new MacroStringMap();
		MacroKey expireKey = baseKey.append(EXPIRE).orElseThrow();
		expireKey.append(DURATION).ifPresent(macroKey -> resultMap.put(macroKey, format(expirable).orElse(UNKNOWN_VALUE)));
		expireKey.append(INSTANT).ifPresent(macroKey -> resultMap.put(macroKey, formatInstant(expirable).orElse(UNKNOWN_VALUE)));
		return resultMap;
	}


	String getExpireDurationString(final Expirable expirable)
	{
		return (expirable.getExpiration().isBefore(Instant.now()))
				? durationFormatter.format(Duration.between(Instant.now(), expirable.getExpiration()), ChronoUnit.MINUTES)
				: UNKNOWN_VALUE;
	}

	Optional<String> format(final Expirable expirable)
	{
		return Optional.ofNullable(durationFormatter.format(Duration.between(Instant.now(), expirable.getExpiration()), ChronoUnit.MINUTES));
	}

	String getExpireInstantString(final Expirable expirable)
	{
		return (expirable.getExpiration().isBefore(Instant.now()))
				? durationFormatter.format(Duration.between(Instant.now(), expirable.getExpiration()), ChronoUnit.MINUTES)
				: UNKNOWN_VALUE;
	}


	Optional<String> formatInstant(final Expirable expirable)
	{
		return Optional.ofNullable(new SimpleDateFormat().format(expirable.getExpiration()));
	}

}
