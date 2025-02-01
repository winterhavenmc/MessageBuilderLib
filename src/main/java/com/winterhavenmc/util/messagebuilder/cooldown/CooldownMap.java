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

package com.winterhavenmc.util.messagebuilder.cooldown;

import com.winterhavenmc.util.messagebuilder.pipeline.Cooldown;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.messages.MessageRecord;
import com.winterhavenmc.util.messagebuilder.util.LocalizedException;
import org.bukkit.command.CommandSender;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.winterhavenmc.util.messagebuilder.util.LocalizedException.MessageKey.PARAMETER_NULL;
import static com.winterhavenmc.util.messagebuilder.util.LocalizedException.Parameter.*;


public final class CooldownMap implements Cooldown {

	// cooldown backing map
	private final Map<CooldownKey, Instant> COOLDOWN_MAP = new ConcurrentHashMap<>();


	/**
	 * Add entry to message cooldown map
	 *
	 * @param recipient the entity whose uuid will be added as a key to the cooldown map
	 * @param messageRecord the message to be placed in the cooldown map for recipient
	 * @throws LocalizedException if any parameter is null
	 */
	public void putExpirationTime(final CommandSender recipient, final MessageRecord messageRecord) {
		if (recipient == null) { throw new LocalizedException(PARAMETER_NULL, RECIPIENT); }
		if (messageRecord == null) { throw new LocalizedException(PARAMETER_NULL, MESSAGE_RECORD); }

		CooldownKey key = new CooldownKey(recipient, messageRecord.messageId());
		if (!isCooling(key)) {
			Instant expirationTime = Instant.now().plus(messageRecord.repeatDelay());
			COOLDOWN_MAP.put(key, expirationTime);
		}
	}


	/**
	 * check if player message is in cooldown map
	 *
	 * @param key the cooldown map key for the recipient/message
	 * @return true if player message is in cooldown map and has not reached its expiration time, false if it is not
	 * @throws LocalizedException if any parameter is null
	 */
	public boolean isCooling(final CooldownKey key) {
		if (key == null) { throw new LocalizedException(PARAMETER_NULL, KEY); }

		return COOLDOWN_MAP.containsKey(key) && Instant.now().isBefore(COOLDOWN_MAP.get(key));
	}


	/**
	 * Iterate the cooldown map and remove any entries whose expire time has passed.
	 */
	public int removeExpired() {
		int count = 0;
		for (Map.Entry<CooldownKey, Instant> entry : COOLDOWN_MAP.entrySet()) {
			if (Instant.now().isAfter(entry.getValue())) {
				COOLDOWN_MAP.remove(entry.getKey());
				count++;
			}
		}
		return count;
	}

}
