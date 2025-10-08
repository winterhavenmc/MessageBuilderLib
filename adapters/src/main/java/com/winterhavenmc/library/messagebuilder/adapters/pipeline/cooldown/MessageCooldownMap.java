/*
 * Copyright (c) 2022-2025 Tim Savage.
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

package com.winterhavenmc.library.messagebuilder.adapters.pipeline.cooldown;

import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.cooldown.CooldownMap;
import com.winterhavenmc.library.messagebuilder.models.keys.CooldownKey;

import com.winterhavenmc.library.messagebuilder.models.recipient.Recipient;
import com.winterhavenmc.library.messagebuilder.models.language.FinalMessageRecord;

import java.time.Instant;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


/**
 * An implementation of {@link CooldownMap} that maintains a per-message cooldown map.
 * <p>
 * Each cooldown entry is keyed by a {@link CooldownKey}, which typically includes
 * the {@code UUID} of a recipient and the {@code MessageId} of a message.
 */
public final class MessageCooldownMap implements CooldownMap
{
	private final Map<CooldownKey, Instant> COOLDOWN_MAP = new ConcurrentHashMap<>();


	/**
	 * Registers a new cooldown expiration time for the given message and recipient.
	 * <p>
	 * The expiration time is determined by adding the {@code repeatDelay}
	 * from the message record to the current time.
	 * <p>
	 * This method is also responsible for removing stale entries from the map, to prevent memory leakage.
	 *
	 * @param recipient      the message recipient
	 * @param messageRecord  the message record containing the delay configuration
	 */
	public void putExpirationTime(final Recipient.Sendable recipient, final FinalMessageRecord messageRecord)
	{
		removeExpired(); // as good a time as any to clear out stale map entries
		CooldownKey.of(recipient, messageRecord.key())
				.ifPresent(key -> COOLDOWN_MAP.put(key, Instant.now().plus(messageRecord.repeatDelay())));
	}


	/**
	 * Checks whether the message corresponding to the given cooldown string
	 * is currently cooling down for a recipient.
	 * <p>
	 * Returns {@code true} if the string has no cooldown entry or the cooldown has expired.
	 *
	 * @param key the composed cooldown string
	 * @return {@code true} if the cooldown has expired or doesn't exist
	 */
	public boolean notCooling(final CooldownKey key)
	{
		return !(COOLDOWN_MAP.containsKey(key) && Instant.now().isBefore(COOLDOWN_MAP.get(key)));
	}


	/**
	 * Removes all expired cooldown entries from the internal map.
	 *
	 * @return the number of entries removed
	 */
	public int removeExpired()
	{
		final Instant now = Instant.now();
		final Set<CooldownKey> expiredKeys = COOLDOWN_MAP.entrySet().stream()
				.filter(entry -> now.isAfter(entry.getValue()))
				.map(Map.Entry::getKey)
				.collect(Collectors.toSet());

		expiredKeys.forEach(COOLDOWN_MAP::remove);

		return expiredKeys.size();
	}

}
