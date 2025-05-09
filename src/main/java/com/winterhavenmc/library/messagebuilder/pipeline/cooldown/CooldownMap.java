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

package com.winterhavenmc.library.messagebuilder.pipeline.cooldown;

import com.winterhavenmc.library.messagebuilder.model.recipient.Recipient;
import com.winterhavenmc.library.messagebuilder.model.language.FinalMessageRecord;
import com.winterhavenmc.library.messagebuilder.validation.ValidationException;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public final class CooldownMap implements Cooldown
{
	// cooldown backing map
	private final Map<CooldownKey, Instant> COOLDOWN_MAP = new ConcurrentHashMap<>();


	/**
	 * Add entry to message cooldown map
	 *
	 * @param recipient the entity whose uuid will be added as a key to the cooldown map
	 * @param messageRecord the message to be placed in the cooldown map for protoRecipient
	 * @throws ValidationException if any parameter is null
	 */
	public void putExpirationTime(final Recipient.Valid recipient, final FinalMessageRecord messageRecord)
	{
		CooldownKey.of(recipient, messageRecord.key())
				.ifPresent(key -> COOLDOWN_MAP.put(key, Instant.now().plus(messageRecord.repeatDelay())));
	}


	/**
	 * check if player message is in cooldown map
	 *
	 * @param key the cooldown map key for the recipient/message
	 * @return true if player message is in cooldown map and has not reached its expiration time, false if it is not
	 * @throws ValidationException if any parameter is null
	 */
	public boolean notCooling(final CooldownKey key)
	{
		return !(COOLDOWN_MAP.containsKey(key) && Instant.now().isBefore(COOLDOWN_MAP.get(key)));
	}


	/**
	 * Iterate the cooldown map and remove any entries whose expire time has passed.
	 */
	public int removeExpired()
	{
		int count = 0;
		for (Map.Entry<CooldownKey, Instant> entry : COOLDOWN_MAP.entrySet())
		{
			if (Instant.now().isAfter(entry.getValue()))
			{
				COOLDOWN_MAP.remove(entry.getKey());
				count++;
			}
		}
		return count;
	}

}
