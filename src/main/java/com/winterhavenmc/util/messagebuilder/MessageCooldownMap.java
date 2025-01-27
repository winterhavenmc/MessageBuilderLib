/*
 * Copyright (c) 2022 Tim Savage.
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

package com.winterhavenmc.util.messagebuilder;

import com.winterhavenmc.util.messagebuilder.util.LocalizedException;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.winterhavenmc.util.messagebuilder.util.LocalizedException.MessageKey.PARAMETER_NULL;
import static com.winterhavenmc.util.messagebuilder.util.LocalizedException.Parameter.*;


/**
 * A singleton class that wraps a map of player message cooldown expiration times.
 *
 * @param <MessageId> An enum representing message identifiers defined in the client
 */
final class MessageCooldownMap<MessageId extends Enum<MessageId>> {


	private static MessageCooldownMap<? extends Enum<?>> INSTANCE;

	// cooldown backing map
	private final Map<CooldownKey, Instant> backingMap = new ConcurrentHashMap<>();


	/**
	 * Static getter for instance
	 *
	 * @return instance of this class
	 */
	@SuppressWarnings("unchecked")
	static synchronized <MessageId extends Enum<MessageId>> MessageCooldownMap<MessageId> getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new MessageCooldownMap<>();
		}
		return (MessageCooldownMap<MessageId>) INSTANCE;
	}


	/**
	 * Add entry to message cooldown map
	 *
	 * @param recipient the entity whose uuid will be added as a key to the cooldown map
	 * @param messageId the message id to use as a key in the cooldown map
	 * @param duration the cooldown duration
	 * @throws NullPointerException if parameter is null
	 */
	void put(final CommandSender recipient, final MessageId messageId, Duration duration) {
		if (recipient == null) { throw new LocalizedException(PARAMETER_NULL, RECIPIENT); }
		if (messageId == null) { throw new LocalizedException(PARAMETER_NULL, MESSAGE_ID); }
		if (duration == null) { throw new LocalizedException(PARAMETER_NULL, DURATION); }

		CooldownKey key = new CooldownKey(recipient, messageId);
		backingMap.putIfAbsent(key, Instant.now().plus(duration));
	}


	/**
	 * check if player message is in cooldown map
	 *
	 * @param recipient player being sent message
	 * @param messageId message id of message being sent
	 * @return true if player message is in cooldown map and has not reached its expiration time, false if it is not
	 * @throws NullPointerException if parameter is null
	 */
	boolean isCooling(final CommandSender recipient, final MessageId messageId) {
		if (recipient == null) { throw new LocalizedException(PARAMETER_NULL, RECIPIENT); }
		if (messageId == null) { throw new LocalizedException(PARAMETER_NULL, MESSAGE_ID); }

		CooldownKey key = new CooldownKey(recipient, messageId);

		if (backingMap.containsKey(key)) {
			return backingMap.get(key).isBefore(Instant.now());
		}
		return false;
	}

}
