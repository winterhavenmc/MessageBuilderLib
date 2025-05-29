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

package com.winterhavenmc.library.messagebuilder.pipeline.adapters.protection;

import com.winterhavenmc.library.messagebuilder.keys.MacroKey;
import com.winterhavenmc.library.messagebuilder.pipeline.containers.MacroStringMap;
import com.winterhavenmc.library.messagebuilder.pipeline.formatters.duration.DurationFormatter;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static com.winterhavenmc.library.messagebuilder.pipeline.adapters.Adapter.BuiltIn.PROTECTION_DURATION;


@Deprecated
public class ProtectDurationExtractor
{
	private final static String UNKNOWN_STRING = "-";
	private final DurationFormatter durationFormatter;

	public ProtectDurationExtractor(DurationFormatter durationFormatter)
	{
		this.durationFormatter = durationFormatter;
	}


	MacroStringMap extract(final MacroKey baseKey,
				 final Protectable protectable)
	{
		MacroStringMap resultMap = new MacroStringMap();
		baseKey.append(PROTECTION_DURATION).ifPresent(macroKey -> resultMap.put(macroKey, getProtectDurationString(protectable)));
		return resultMap;
	}


	String getProtectDurationString(final Protectable protectable)
	{
		return (protectable.getProtection().isBefore(Instant.now()))
				? durationFormatter.format(Duration.between(Instant.now(), protectable.getProtection()), ChronoUnit.MINUTES)
				: UNKNOWN_STRING;
	}

}
