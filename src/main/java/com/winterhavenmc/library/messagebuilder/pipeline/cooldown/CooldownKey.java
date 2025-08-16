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

package com.winterhavenmc.library.messagebuilder.pipeline.cooldown;

import com.winterhavenmc.library.messagebuilder.keys.ValidMessageKey;
import com.winterhavenmc.library.messagebuilder.model.recipient.Recipient;
import org.bukkit.entity.Entity;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;


/**
 * An implementation of a string for use in the cooldown map. It is composed of the recipient uuid and
 * the unique MessageId. If a recipient does not have an uuid, such as the console, the declared constant
 * default uuid is used. This means that the console is subject to message repeat delays, which are
 * shared by all non-uuid message recipients. (Are there more than console? Players and Entities both have uuids.)
 * <p>
 * This behavior could be adapted by generating random uuids, or specific uuids for various non-uuid
 * message recipient types.
 */
public class CooldownKey
{
	final static UUID DEFAULT_UUID = new UUID(0, 0);

	private final UUID uuid;
	private final ValidMessageKey messageKey;


	/**
	 * Class constructor
	 *
	 * @param recipient the message recipient
	 * @param messageKey the unique message id
	 */
	private CooldownKey(final Recipient.Sendable recipient, final ValidMessageKey messageKey)
	{
		this.messageKey = messageKey;
		this.uuid = (recipient.sender() instanceof Entity entity)
				? entity.getUniqueId()
				: DEFAULT_UUID;
	}


	/**
	 * Static factory method with Message parameter
	 *
	 * @param recipient the recipient used for CooldownKey creation
	 * @param messageKey the message string used for CooldownKey creation
	 * @return {@code Optional} CooldownKey, or empty Optional if either parameter is invalid
	 */
	public static Optional<CooldownKey> of(final Recipient.Sendable recipient, final ValidMessageKey messageKey)
	{
		return Optional.of(new CooldownKey(recipient, messageKey));
	}


	/**
	 * Retrieve the message string used for this cooldown string
	 *
	 * @return the message string for this cooldown string
	 */
	public ValidMessageKey getMessageKey()
	{
		return this.messageKey;
	}


	@Override
	public String toString()
	{
		return messageKey + "|" + this.uuid;
	}


	@Override
	public final boolean equals(final Object object)
	{
		return object instanceof CooldownKey that
				&& Objects.equals(uuid, that.uuid)
				&& Objects.equals(messageKey, that.messageKey);
	}


	@Override
	public int hashCode()
	{
		int result = uuid.hashCode();
		result = 31 * result + messageKey.hashCode();
		return result;
	}

}
