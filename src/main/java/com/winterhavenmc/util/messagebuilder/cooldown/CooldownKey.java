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

package com.winterhavenmc.util.messagebuilder.cooldown;

import com.winterhavenmc.util.messagebuilder.util.LocalizedException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;

import java.util.UUID;

import static com.winterhavenmc.util.messagebuilder.util.LocalizedException.MessageKey.PARAMETER_NULL;
import static com.winterhavenmc.util.messagebuilder.util.LocalizedException.Parameter.*;


/**
 * An implementation of a key for use in the cooldown map. It is composed of the recipient uuid and
 * the unique MessageId. If a recipient does not have a uuid, such as the console, the declared constant
 * default uuid is used. This means that the console is subject to message repeat delays, which are
 * shared by all non-uuid message recipients. (Are there more than console? Players and Entities both have uuids.)
 * <p>
 * This behavior could be adapted by generating random uuids, or specific uuids for various non-uuid
 * message recipient types.
 */
class CooldownKey {

	final static UUID DEFAULT_UUID = new UUID(0, 0);

	private final UUID uuid;
	private final String messageIdString;


	/**
	 * Class constructor
	 *
	 * @param recipient the message recipient
	 * @param messageId the unique message id
	 * @param <MessageId> the type for message id
	 */
	<MessageId extends Enum<MessageId>> CooldownKey(final CommandSender recipient, MessageId messageId) {
		if (recipient == null) { throw new LocalizedException(PARAMETER_NULL, RECIPIENT); }
		if (messageId == null) { throw new LocalizedException(PARAMETER_NULL, MESSAGE_ID); }

		this.messageIdString = messageId.name();

		if (recipient instanceof Entity entity) {
			this.uuid = entity.getUniqueId();
		}
		else {
			this.uuid = DEFAULT_UUID;
		}
	}

	@Override
	public String toString() {
		return messageIdString + "|" + this.uuid;
	}

	@Override
	public final boolean equals(Object object) {
		if (!(object instanceof CooldownKey that)) {
			return false;
		}
		return uuid.equals(that.uuid) && messageIdString.equals(that.messageIdString);
	}

	@Override
	public int hashCode() {
		int result = uuid.hashCode();
		result = 31 * result + messageIdString.hashCode();
		return result;
	}

}
