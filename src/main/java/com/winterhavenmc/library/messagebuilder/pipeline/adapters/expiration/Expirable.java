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
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.AdapterContextContainer;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.duration.Durationable;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.instant.Instantable;
import com.winterhavenmc.library.messagebuilder.pipeline.containers.MacroStringMap;

import java.time.Instant;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;

import static com.winterhavenmc.library.messagebuilder.pipeline.adapters.Adapter.UNKNOWN_VALUE;
import static com.winterhavenmc.library.messagebuilder.pipeline.adapters.Adapter.BuiltIn.*;


/**
 * An interface that describes objects with an {@code Instant} expiration field
 * and a corresponding getExpiration() accessor method
 */
@FunctionalInterface
public interface Expirable
{
	Instant getExpiration();


	/**
	 * Returns a new MacroStringMap containing all fields extracted from an Expirable type
	 *
	 * @param baseKey the top level key for the fields of this object
	 * @param lowerBound a Temporal unit for use as a lower bound of units displayed
	 * @param ctx containing the duration formatter to be used to convert the duration to a String
	 * @return a MacroStringMap containing the fields extracted for objects of Expirable type
	 */
	default MacroStringMap extractExpiration(final MacroKey baseKey,
											 final ChronoUnit lowerBound,
											 final FormatStyle formatStyle,
											 final AdapterContextContainer ctx)
	{
		MacroStringMap resultMap = new MacroStringMap();

		baseKey.append(PROTECTION).ifPresent(protectionKey ->
		{
			// formatted duration
			protectionKey.append(DURATION).ifPresent(macroKey ->
					resultMap.put(macroKey, Durationable.formatDuration(Durationable
							.durationUntil(this.getExpiration()), lowerBound, ctx.formatterContainer()
							.durationFormatter())
							.orElse(UNKNOWN_VALUE)));

			// formatted date/time from Instant
			protectionKey.append(INSTANT).ifPresent(macroKey ->
					resultMap.put(macroKey, Instantable.formatInstant(this.getExpiration(), formatStyle, ctx.formatterContainer()
							.localeProvider())
							.orElse(UNKNOWN_VALUE)));
		});

		return resultMap;
	}

}
