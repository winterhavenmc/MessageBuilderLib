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

package com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.protection;

import com.winterhavenmc.library.messagebuilder.models.keys.ValidMacroKey;
import com.winterhavenmc.library.messagebuilder.core.context.AdapterCtx;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.duration.Durationable;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.instant.Instantable;
import com.winterhavenmc.library.messagebuilder.core.maps.MacroStringMap;

import java.time.format.FormatStyle;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.Accessor.BuiltIn.*;
import static com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.Accessor.UNKNOWN_VALUE;


/**
 * An interface representing objects that have a limited protection period,
 * retrievable via an {@link Instant} timestamp.
 *
 * <p>Objects implementing this interface allow protection-related time values
 * to be extracted and formatted for use in macro placeholder replacement,
 * such as:
 *
 * <ul>
 *   <li>{@code [OBJECT.PROTECTION.DURATION}} – time remaining until protection expires</li>
 *   <li>{@code [OBJECT.PROTECTION.INSTANT}} – the exact localized date/time when protection ends</li>
 * </ul>
 *
 * <p>These macros are automatically populated using this interface's
 * {@code extractProtection(...)} method when objects are processed by
 * the {@code ProtectionAdapter}.
 */
@FunctionalInterface
public interface Protectable
{
	/**
	 * Returns the protection {@link Instant}, representing the moment at which
	 * protection ends or expires.
	 *
	 * @return the protection expiration timestamp
	 */
	Instant getProtection();


	/**
	 * Extracts protection-related fields from this object into a {@link MacroStringMap}.
	 * This includes both a localized duration until protection ends and a formatted
	 * date/time string.
	 *
	 * @param baseKey the macro base string this object is mapped to
	 * @param lowerBound the lowest time unit to include in the duration formatting
	 * @param formatStyle the desired style of date/time formatting (e.g. SHORT, MEDIUM)
	 * @param ctx container for formatter dependencies and resolvers
	 * @return a {@link MacroStringMap} with entries for both duration and instant representations
	 */
	default MacroStringMap extractProtection(final ValidMacroKey baseKey,
											 final ChronoUnit lowerBound,
											 final FormatStyle formatStyle,
											 final AdapterCtx ctx)
	{
		MacroStringMap resultMap = new MacroStringMap();

		baseKey.append(PROTECTION).isValid().ifPresent(protectionKey ->
		{
			// formatted duration from current time
			protectionKey.append(DURATION).isValid().ifPresent(macroKey ->
					resultMap.put(macroKey, Durationable.formatDuration(Durationable
							.durationUntil(this.getProtection()), lowerBound, ctx.formatterCtx()
							.durationFormatter())
							.orElse(UNKNOWN_VALUE)));

			// formatted date/time from Instant
			protectionKey.append(INSTANT).isValid().ifPresent(macroKey ->
					resultMap.put(macroKey, Instantable.formatInstant(this.getProtection(), formatStyle, ctx.formatterCtx()
							.localeProvider())
							.orElse(UNKNOWN_VALUE)));
		});

		return resultMap;
	}

}
