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

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;


/**
 * A singleton class that wraps a map of player message cooldown times.
 * The map is keyed by MessageId, with values containing a nested map keyed by player uuid and
 * with value a long representing the last displayed time of the message in milliseconds since epoch.
 * On subsequent calls to display the same message for the same user, this value will be used to check
 * if the configured message cooldown time has elapsed.
 *
 * @param <MessageId> An enum representing message identifiers defined in the client
 */
final class MessageCooldownMap<MessageId extends Enum<MessageId>> implements Listener {

	private static MessageCooldownMap<? extends Enum<?>> INSTANCE;

	// cooldown enum map
	private final Map<MessageId, Map<UUID, Long>> messageCooldownMap = new ConcurrentHashMap<>();


	/**
	 * private class constructor prevents instantiation of singleton
	 * registers class as bukkit event listener
	 */
	private MessageCooldownMap(Plugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}


	/**
	 * Static getter for instance
	 *
	 * @return instance of this class
	 */
	@SuppressWarnings("unchecked")
	static synchronized <MessageId extends Enum<MessageId>> MessageCooldownMap<MessageId> getInstance(final Plugin plugin) {
		if (INSTANCE == null) {
			INSTANCE = new MessageCooldownMap<>(plugin);
		}
		return (MessageCooldownMap<MessageId>) INSTANCE;
	}


	@SuppressWarnings("unused")
	public Map<MessageId, Map<UUID, Long>> getMessageCooldownMap() {
		return this.messageCooldownMap;
	}


	/**
	 * Add entry to message cooldown map
	 *
	 * @param messageId the message id to use as a key in the cooldown map
	 * @param entity    the entity whose uuid will be added as a key to the cooldown map
	 * @throws NullPointerException if parameter is null
	 */
	void put(final MessageId messageId, final Entity entity) {

		// check for null parameters
		Objects.requireNonNull(messageId);
		Objects.requireNonNull(entity);

		// create new HashMap with entity UUID as key
		Map<UUID, Long> tempMap = new ConcurrentHashMap<>();

		// put current time in HashMap with entity UUID as key
		long currentTime = System.currentTimeMillis(); // NOTE: Don't inline this, it can cause a deadlock
		tempMap.put(entity.getUniqueId(), currentTime);

		// put HashMap in cooldown map with messageId as key
		messageCooldownMap.put(messageId, tempMap);
	}


	/**
	 * get entry from message cooldown map
	 *
	 * @param messageId the message identifier for which retrieve cooldown time
	 * @param entity    the entity for whom to retrieve cooldown time
	 * @return cooldown expire time
	 * @throws NullPointerException if parameter is null
	 */
	long get(final MessageId messageId, final Entity entity) {

		// check for null parameters
		Objects.requireNonNull(messageId);
		Objects.requireNonNull(entity);

		// check if messageId is in message cooldown hashmap
		if (messageCooldownMap.containsKey(messageId)) {

			// check if messageID is in entity's cooldown hashmap
			if (messageCooldownMap.get(messageId).containsKey(entity.getUniqueId())) {

				// return cooldown time
				return messageCooldownMap.get(messageId).get(entity.getUniqueId());
			}
		}
		return 0L;
	}


	/**
	 * Remove player from message cooldown map
	 *
	 * @param entity the entity (player) to be removed from message cooldown map
	 */
	int remove(final Entity entity) {

		// if entity is null, do nothing and return
		if (entity == null) {
			return 0;
		}

		int count = 0;

		// iterate through all cooldown map keys
		for (MessageId messageId : messageCooldownMap.keySet()) {

			// remove entity UUID from cooldown map
			Long ignored = messageCooldownMap.get(messageId).remove(entity.getUniqueId());
			if (ignored != null) {
				count++;
			}
		}
		return count;
	}


	/**
	 * check if player message is in cooldown map
	 *
	 * @param recipient   player being sent message
	 * @param messageId   message id of message being sent
	 * @param repeatDelay time (in seconds) before message should be redisplayed
	 * @return true if player message is in cooldown map, false if it is not
	 * @throws NullPointerException if parameter is null
	 */
	boolean isCooling(final CommandSender recipient, final MessageId messageId, final long repeatDelay) {

		// check for null parameters
		Objects.requireNonNull(recipient);
		Objects.requireNonNull(messageId);

		// if recipient is entity...
		if (recipient instanceof Entity entity) {

			// get message cooldown time remaining
			long lastDisplayed = get(messageId, entity);

			// if message has repeat delay value and was displayed to player more recently, do nothing and return
			//noinspection RedundantIfStatement
			if (lastDisplayed > System.currentTimeMillis() - TimeUnit.SECONDS.toMillis(repeatDelay)) {
				return true;
			}
		}
		return false;
	}


	/**
	 * Event handler for PlayerQuitEvent;
	 * removes player from message cooldown map on logout
	 *
	 * @param event the event handled by this method
	 */
	@EventHandler
	public void onPlayerQuit(final PlayerQuitEvent event) {

		// remove player from message cooldown map
		remove(event.getPlayer());
	}

}
