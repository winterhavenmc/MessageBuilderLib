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

package com.winterhavenmc.library.messagebuilder.pipeline.extractor;

import com.winterhavenmc.library.messagebuilder.keys.MacroKey;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.Adapter;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.AdapterContextContainer;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.displayname.DisplayNameAdapter;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.displayname.DisplayNameable;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.duration.DurationAdapter;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.duration.Durationable;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.expiration.Expirable;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.expiration.ExpirationAdapter;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.instant.InstantAdapter;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.instant.Instantable;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.killer.Killable;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.location.Locatable;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.location.LocationAdapter;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.killer.KillerAdapter;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.looter.Lootable;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.looter.LooterAdapter;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.name.NameAdapter;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.name.Nameable;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.owner.Ownable;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.owner.OwnerAdapter;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.protection.Protectable;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.protection.ProtectionAdapter;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.quantity.Quantifiable;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.quantity.QuantityAdapter;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.uuid.Identifiable;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.uuid.UniqueIdAdapter;
import com.winterhavenmc.library.messagebuilder.pipeline.maps.MacroStringMap;

import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;


/**
 * Default implementation of the {@link Extractor} interface that delegates
 * field extraction to the appropriate functional interface based on the adapter type.
 * <p>
 * The {@code FieldExtractor} is responsible for mapping adapted objects (e.g., {@link Nameable},
 * {@link Locatable}, {@link Quantifiable}, etc.) to their extracted macro string values.
 * These values are inserted into a {@link MacroStringMap}, keyed by the supplied {@link MacroKey}.
 * <p>
 * This implementation supports all built-in {@link Adapter} types and ensures
 * that the correct extraction method is called based on both the adapter class
 * and the runtime type of the adapted object.
 */
public class FieldExtractor implements Extractor
{
	private final AdapterContextContainer ctx;


	/**
	 * Constructs a new {@code FieldExtractor} with the provided context container.
	 *
	 * @param ctx the context container holding formatter and configuration dependencies
	 */
	public FieldExtractor(final AdapterContextContainer ctx)
	{
		this.ctx = ctx;
	}


	/**
	 * Extracts macro string values from the adapted object using the adapter's
	 * corresponding interface, such as {@code Nameable} or {@code Locatable}.
	 * <p>
	 * If the adapter is recognized and the adapted object is an instance of the
	 * expected functional interface, the appropriate extractor method is invoked.
	 * <p>
	 * If no valid match is found for the adapter/object combination, an empty
	 * {@link MacroStringMap} is returned.
	 *
	 * @param baseKey the base {@link MacroKey} representing the placeholder prefix
	 * @param adapter the adapter that was used to adapt the original object
	 * @param adapted the adapted object implementing one of the field interfaces
	 * @param <T>     the type of the adapted object
	 * @return a {@code MacroStringMap} containing extracted macro values
	 */
	@Override
	public <T> MacroStringMap extract(final MacroKey baseKey, final Adapter adapter, final T adapted)
	{
		MacroStringMap resultMap = new MacroStringMap();

		switch (adapter)
		{
			// Extract name field as string
			case NameAdapter __ when adapted instanceof Nameable nameable ->
			{
				resultMap.putAll(nameable.extractName(baseKey, ctx));
				Nameable.formatName(nameable.getName()).ifPresent(string ->
						resultMap.putIfAbsent(baseKey, string));
			}

			// Extract displayName field as string
			case DisplayNameAdapter __ when adapted instanceof DisplayNameable displayNameable ->
			{
				resultMap.putAll(displayNameable.extractDisplayName(baseKey, ctx));
				DisplayNameable.formatDisplayName(displayNameable.getDisplayName()).ifPresent(string ->
						resultMap.putIfAbsent(baseKey, string));
			}

			// Extract owner field as string
			case OwnerAdapter __ when adapted instanceof Ownable ownable ->
			{
				resultMap.putAll(ownable.extractOwner(baseKey, ctx));
				Ownable.formatOwner(ownable.getOwner()).ifPresent(string ->
						resultMap.putIfAbsent(baseKey, string));
			}

			// Extract killer field as string
			case KillerAdapter __ when adapted instanceof Killable killable ->
			{
				resultMap.putAll(killable.extractKiller(baseKey, ctx));
				Killable.formatKiller(killable.getKiller()).ifPresent(string ->
						resultMap.putIfAbsent(baseKey, string));
			}

			// Extract looter field as string
			case LooterAdapter __ when adapted instanceof Lootable lootable ->
			{
				resultMap.putAll(lootable.extractLooter(baseKey, ctx));
				Lootable.formatLooter(lootable.getLooter()).ifPresent(string ->
						resultMap.putIfAbsent(baseKey, string));
			}

			// Extract location fields as strings
			case LocationAdapter __ when adapted instanceof Locatable locatable ->
				resultMap.putAll(locatable.extractLocation(baseKey, ctx));

			// Extract uniqueId field as string
			case UniqueIdAdapter __ when adapted instanceof Identifiable identifiable ->
					resultMap.putAll(identifiable.extractUid(baseKey, ctx));

			// Extract quantity field as string
			case QuantityAdapter __ when adapted instanceof Quantifiable quantifiable ->
					resultMap.putAll(quantifiable.extractQuantity(baseKey, ctx));

			// Extract duration field as string
			case DurationAdapter __ when adapted instanceof Durationable durationable ->
					resultMap.putAll(durationable.extractDuration(baseKey, ChronoUnit.MINUTES, ctx));

			// Extract instant field as string
			case InstantAdapter __ when adapted instanceof Instantable instantable ->
					resultMap.putAll(instantable.extractInstant(baseKey, FormatStyle.MEDIUM, ctx));

			// Extract expiration fields as strings
			case ExpirationAdapter __ when adapted instanceof Expirable expirable ->
				resultMap.putAll(expirable.extractExpiration(baseKey, ChronoUnit.MINUTES, FormatStyle.MEDIUM, ctx));

			// Extract protection fields as strings
			case ProtectionAdapter __ when adapted instanceof Protectable protectable ->
					resultMap.putAll(protectable.extractProtection(baseKey, ChronoUnit.MINUTES, FormatStyle.MEDIUM, ctx));

			default -> {} // no-op
		}

		return resultMap;
	}

}
