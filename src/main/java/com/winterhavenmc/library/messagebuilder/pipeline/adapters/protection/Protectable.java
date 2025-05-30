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
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.duration.Durationable;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.instant.Instantable;
import com.winterhavenmc.library.messagebuilder.pipeline.containers.MacroStringMap;
import com.winterhavenmc.library.messagebuilder.pipeline.resolvers.FormatterContainer;

import java.time.format.FormatStyle;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static com.winterhavenmc.library.messagebuilder.pipeline.adapters.Adapter.BuiltIn.*;
import static com.winterhavenmc.library.messagebuilder.pipeline.adapters.Adapter.UNKNOWN_VALUE;
import static com.winterhavenmc.library.messagebuilder.pipeline.adapters.Formatter.durationUntil;


@FunctionalInterface
public interface Protectable
{
	Instant getProtection();


	/**
	 * Returns a new MacroStringMap containing all fields extracted from a Protectable type
	 *
	 * @param baseKey the top level key for the fields of this object
	 * @param protectable an object that conforms to the Protectable interface
	 * @param formatterContainer the duration formatter to be used to convert the duration to a String
	 * @return a MacroStringMap containing the fields extracted for objects of Protectable type
	 */
	default MacroStringMap extract(final MacroKey baseKey,
								   final Protectable protectable,
								   final ChronoUnit lowerBound,
								   final FormatStyle formatStyle,
								   final FormatterContainer formatterContainer)
	{
		MacroKey protectionKey = baseKey.append(PROTECTION).orElseThrow();
		MacroStringMap resultMap = new MacroStringMap();

		// formatted date/time from Instant
		protectionKey.append(INSTANT).ifPresent(macroKey ->
				resultMap.put(macroKey, Instantable
						.format(this.getProtection(), formatStyle, formatterContainer.localeProvider())
						.orElse(UNKNOWN_VALUE)));

		// formatted duration
		protectionKey.append(DURATION).ifPresent(macroKey ->
				resultMap.put(macroKey, Durationable
						.format(durationUntil(this.getProtection()), lowerBound, formatterContainer.durationFormatter())
						.orElse(UNKNOWN_VALUE)));

		return resultMap;
	}

}
