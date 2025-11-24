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

package com.winterhavenmc.library.messagebuilder.core.ports.pipeline.cooldown;


import com.winterhavenmc.library.messagebuilder.models.keys.CooldownKey;
import com.winterhavenmc.library.messagebuilder.models.language.message.FinalMessageRecord;
import com.winterhavenmc.library.messagebuilder.models.recipient.Recipient;

/**
 * A functional interface representing a cooldown check for a given string.
 * <p>
 * This is used to determine whether a message should be allowed to send,
 * based on the last time it was shown and the configured delay.
 */
public interface CooldownMap
{
	/**
	 * Determines if the given cooldown string is not currently cooling down.
	 * <p>
	 * Returns {@code true} if either no cooldown exists for the string, or
	 * the cooldown has expired.
	 *
	 * @param cooldownKey the cooldown string composed of recipient and message identifier
	 * @return {@code true} if the message is not cooling down and may be shown
	 */
	boolean notCooling(CooldownKey cooldownKey);
	void putExpirationTime(Recipient.Sendable recipient, FinalMessageRecord messageRecord);
	int removeExpired();
}
