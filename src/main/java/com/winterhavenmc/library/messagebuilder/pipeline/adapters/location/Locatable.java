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
import com.winterhavenmc.library.messagebuilder.pipeline.maps.MacroStringMap;
import com.winterhavenmc.library.messagebuilder.pipeline.formatters.number.LocaleNumberFormatter;
import org.bukkit.Location;

import java.util.Optional;

import static com.winterhavenmc.library.messagebuilder.pipeline.adapters.Adapter.BuiltIn.LOCATION;
import static com.winterhavenmc.library.messagebuilder.pipeline.adapters.Adapter.UNKNOWN_VALUE;


/**
 * Represents an object that has a {@link org.bukkit.Location}, which can be extracted
 * and used for macro substitution in messages.
 *
 * <p>This interface is used by the {@link LocationAdapter}
 * to extract structured location data for use in messages. It supports both full string representations and
 * granular subfields such as world name and block coordinates.
 *
 * <p>Any plugin-defined object can implement this interface to automatically enable macro expansion of the form:
 * <ul>
 *   <li>{@code {OBJECT.LOCATION}} – full formatted location string</li>
 *   <li>{@code {OBJECT.LOCATION.WORLD}} – world name (optionally aliased by Multiverse)</li>
 *   <li>{@code {OBJECT.LOCATION.X}}, {@code {OBJECT.LOCATION.Y}, {OBJECT.LOCATION.Z}} – localized coordinate values</li>
 * </ul>
 *
 * <p>The resulting macro values are extracted via {@link #extractLocation(MacroKey, AdapterContextContainer)}.
 * This method is used internally by the MessageBuilder pipeline and should not need to be called directly.
 */
@FunctionalInterface
public interface Locatable
{
	/**
	 * Returns the Bukkit {@link Location} associated with this object.
	 *
	 * @return the location of the object, or {@code null} if none exists
	 */
	Location getLocation();


	/**
	 * Enumeration of subfields extractable from a {@link Location}.
	 * These correspond to macro keys used in message templates.
	 */
	enum Field
	{
		STRING, WORLD, X, Y, Z
	}


	/**
	 * Extracts macro key-value pairs representing location data from this object.
	 * <p>
	 * The returned {@link MacroStringMap} may contain:
	 * <ul>
	 *   <li>{@code LOCATION} – formatted string of world name and coordinates</li>
	 *   <li>{@code LOCATION.WORLD} – resolved world name (Multiverse alias if available)</li>
	 *   <li>{@code LOCATION.X}, {@code Y}, {@code Z} – localized block coordinates</li>
	 * </ul>
	 *
	 * @param baseKey the top-level macro key associated with this object
	 * @param ctx a container providing access to formatters and world name resolution
	 * @return a {@code MacroStringMap} containing extracted location-related macro keys
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


	/**
	 * Resolves the name of the world for the given {@link Location}.
	 * <p>
	 * If Multiverse is installed and enabled, this method may return an alias
	 * rather than the raw world name.
	 *
	 * @param location the Bukkit location
	 * @param ctx the context container with the
	 * {@link com.winterhavenmc.library.messagebuilder.pipeline.resolvers.worldname.WorldNameResolver WorldNameResolver}
	 * @return an optional world name, or empty if it could not be resolved
	 */
	static Optional<String> getLocationWorldName(final Location location, final AdapterContextContainer ctx)
	{
		return (location != null && location.getWorld() != null && !location.getWorld().getName().isBlank())
				? Optional.of(ctx.worldNameResolver().resolve(location.getWorld()))
				: Optional.empty();
	}


	/**
	 * Produces a formatted string representation of the given location,
	 * consisting of the world name followed by localized coordinates.
	 *
	 * <p>Example output: {@code world [123, 64, -512]}
	 *
	 * @param location the location to format
	 * @param ctx the context container with the number formatter and world name resolver
	 * @return an {@code Optional<String>} containing the formatted location, or empty if the location is {@code null}
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
