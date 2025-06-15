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

package com.winterhavenmc.library.messagebuilder.pipeline.adapters.location;

import com.winterhavenmc.library.messagebuilder.keys.MacroKey;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.AdapterContextContainer;
import com.winterhavenmc.library.messagebuilder.pipeline.containers.MacroStringMap;
import com.winterhavenmc.library.messagebuilder.pipeline.formatters.number.LocaleNumberFormatter;
import org.bukkit.Location;

import java.util.Optional;

import static com.winterhavenmc.library.messagebuilder.pipeline.adapters.Adapter.BuiltIn.LOCATION;
import static com.winterhavenmc.library.messagebuilder.pipeline.adapters.Adapter.UNKNOWN_VALUE;


/**
 * An interface that describes objects that have a {@code getLocation()} method
 * that returns a {@link Location}.
 */
@FunctionalInterface
public interface Locatable
{
	Location getLocation();


	/**
	 * Enumeration of subfields for {@code Location}
	 */
	enum Field
	{
		STRING, WORLD, X, Y, Z
	}


	/**
	 * Returns a new MacroStringMap containing all fields extracted from a Durationable type
	 *
	 * @param baseKey the top level key for the fields of this object
	 * @param ctx context container with the number formatter to be used to convert coordinate numbers to a localized String
	 * @return a MacroStringMap containing the fields extracted for objects of Durationable type
	 */
	default MacroStringMap extractLocation(final MacroKey baseKey, final AdapterContextContainer ctx)
	{
		MacroStringMap resultMap = new MacroStringMap();

		if (getLocation() != null)
		{
			MacroKey locationKey = (!baseKey.toString().endsWith("LOCATION"))
					? baseKey.append(LOCATION).orElseThrow()
					: baseKey;

			resultMap.put(locationKey, formatLocation(this.getLocation(), ctx).orElse(UNKNOWN_VALUE));
			locationKey.append(Field.WORLD).ifPresent(worldKey ->
					resultMap.put(worldKey, getLocationWorldName(this.getLocation(), ctx).orElse(UNKNOWN_VALUE)));
			locationKey.append(Field.X).ifPresent(xKey ->
					resultMap.put(xKey, ctx.formatterContainer().localeNumberFormatter().getFormatted(this.getLocation().getBlockX())));
			locationKey.append(Field.Y).ifPresent(yKey ->
					resultMap.put(yKey, ctx.formatterContainer().localeNumberFormatter().getFormatted(this.getLocation().getBlockY())));
			locationKey.append(Field.Z).ifPresent(zKey ->
					resultMap.put(zKey, ctx.formatterContainer().localeNumberFormatter().getFormatted(this.getLocation().getBlockZ())));
		}

		return resultMap;
	}


	static Optional<String> getLocationWorldName(final Location location, final AdapterContextContainer ctx)
	{
		return (location != null && location.getWorld() != null && !location.getWorld().getName().isBlank())
				? Optional.of(ctx.worldNameResolver().resolveWorldName(location.getWorld()))
				: Optional.empty();
	}


	/**
	 * Returns a formatted string representing a duration of time, using the supplied DurationFormatter
	 *
	 * @param location the object that conforms to the Expirable interface by including a getExpiration() method
	 * @param ctx the number formatter to be used to convert the location coordinates to localized Strings
	 * @return a formatted String representing the location of the Locatable conforming object
	 */
	static Optional<String> formatLocation(final Location location, final AdapterContextContainer ctx)
	{
		LocaleNumberFormatter numberFormatter = ctx.formatterContainer().localeNumberFormatter();

		return (location != null)
				? Optional.of(getLocationWorldName(location, ctx).orElse(UNKNOWN_VALUE) +
					" [" + String.join(", ",
					ctx.formatterContainer().localeNumberFormatter().getFormatted(location.getBlockX()),
					ctx.formatterContainer().localeNumberFormatter().getFormatted(location.getBlockY()),
					ctx.formatterContainer().localeNumberFormatter().getFormatted(location.getBlockZ()) + "]"))
				: Optional.empty();
	}

}
