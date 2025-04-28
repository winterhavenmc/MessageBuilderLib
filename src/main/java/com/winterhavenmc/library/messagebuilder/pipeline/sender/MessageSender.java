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

package com.winterhavenmc.library.messagebuilder.pipeline.sender;

import com.winterhavenmc.library.messagebuilder.model.recipient.Recipient;
import com.winterhavenmc.library.messagebuilder.pipeline.cooldown.CooldownMap;
import com.winterhavenmc.library.messagebuilder.model.language.message.FinalMessageRecord;

import org.bukkit.ChatColor;


public final class MessageSender implements Sender
{
	private final CooldownMap cooldownMap;


	/**
	 * Class constructor
	 *
	 * @param cooldownMap map of recipients/messages and their cooldown expiration times
	 */
	public MessageSender(final CooldownMap cooldownMap)
	{
		this.cooldownMap = cooldownMap;
	}


	@Override
	public void send(final Recipient.Valid recipient, final FinalMessageRecord messageRecord)
	{
		recipient.sender().sendMessage(ChatColor.translateAlternateColorCodes('&', messageRecord.finalMessageString()));
		cooldownMap.putExpirationTime(recipient, messageRecord);
	}

}
