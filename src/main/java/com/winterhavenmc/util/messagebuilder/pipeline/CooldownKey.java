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

package com.winterhavenmc.util.messagebuilder.pipeline;

import com.winterhavenmc.util.messagebuilder.resources.language.yaml.RecordKey;
import com.winterhavenmc.util.messagebuilder.validation.ValidationException;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static com.winterhavenmc.util.messagebuilder.validation.ExceptionMessageKey.PARAMETER_NULL;
import static com.winterhavenmc.util.messagebuilder.validation.Parameter.MESSAGE_ID;
import static com.winterhavenmc.util.messagebuilder.validation.Parameter.RECIPIENT;
import static com.winterhavenmc.util.messagebuilder.validation.Validator.validate;


/**
 * An implementation of a key for use in the cooldown map. It is composed of the recipient uuid and
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
	private final RecordKey recordKey;


	/**
	 * Class constructor
	 *
	 * @param recipient the message recipient
	 * @param recordKey the unique message id
	 */
	public CooldownKey(final CommandSender recipient, final RecordKey recordKey)
	{
		validate(recipient, Objects::isNull, () -> new ValidationException(PARAMETER_NULL, RECIPIENT));
		validate(recordKey, Objects::isNull, () -> new ValidationException(PARAMETER_NULL, MESSAGE_ID));

		this.recordKey = recordKey;

		if (recipient instanceof Entity entity) {
			this.uuid = entity.getUniqueId();
		}
		else {
			this.uuid = DEFAULT_UUID;
		}
	}


	public static Optional<CooldownKey> optional(final CommandSender recipient, final RecordKey messageId)
	{
		return Optional.of(new CooldownKey(recipient, messageId));
	}


	public RecordKey getRecordKey()
	{
		return this.recordKey;
	}


	@Override
	public String toString()
	{
		return recordKey + "|" + this.uuid;
	}


	@Override
	public final boolean equals(Object object)
	{
		if (!(object instanceof CooldownKey that)) {
			return false;
		}
		return uuid.equals(that.uuid) && recordKey.equals(that.recordKey);
	}


	@Override
	public int hashCode()
	{
		int result = uuid.hashCode();
		result = 31 * result + recordKey.hashCode();
		return result;
	}

}
