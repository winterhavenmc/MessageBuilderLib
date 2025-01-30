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

import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.messages.MessageRecord;
import com.winterhavenmc.util.messagebuilder.util.LocalizedException;
import org.bukkit.command.CommandSender;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.winterhavenmc.util.messagebuilder.util.LocalizedException.MessageKey.PARAMETER_NULL;
import static com.winterhavenmc.util.messagebuilder.util.LocalizedException.Parameter.*;


public final class CooldownMap {

	// cooldown backing map
	private final Map<CooldownKey, Instant> COOLDOWN_MAP = new ConcurrentHashMap<>();


	/**
	 * Add entry to message cooldown map
	 *
	 * @param recipient the entity whose uuid will be added as a key to the cooldown map
	 * @param messageRecord the message to be placed in the cooldown map for recipient
	 * @throws LocalizedException if any parameter is null
	 */
	public
	<MessageId extends Enum<MessageId>>
	void putExpirationTime(final CommandSender recipient, final MessageRecord<MessageId> messageRecord) {
		if (recipient == null) { throw new LocalizedException(PARAMETER_NULL, RECIPIENT); }
		if (messageRecord == null) { throw new LocalizedException(PARAMETER_NULL, MESSAGE_RECORD); }

		if (!isCooling(recipient, messageRecord.messageId())) {
			CooldownKey key = new CooldownKey(recipient, messageRecord.messageId());
			Instant expirationTime = Instant.now().plus(messageRecord.repeatDelay());
			COOLDOWN_MAP.put(key, expirationTime);
		}
	}


	/**
	 * check if player message is in cooldown map
	 *
	 * @param recipient player being sent message
	 * @param messageId message id of message being sent
	 * @return true if player message is in cooldown map and has not reached its expiration time, false if it is not
	 * @throws LocalizedException if any parameter is null
	 */
	public
	<MessageId extends Enum<MessageId>>
	boolean isCooling(final CommandSender recipient, final MessageId messageId) {
		if (recipient == null) { throw new LocalizedException(PARAMETER_NULL, RECIPIENT); }
		if (messageId == null) { throw new LocalizedException(PARAMETER_NULL, MESSAGE_ID); }

		CooldownKey key = new CooldownKey(recipient, messageId);
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
