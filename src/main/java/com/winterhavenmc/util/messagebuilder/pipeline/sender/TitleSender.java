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

package com.winterhavenmc.util.messagebuilder.pipeline.sender;

import com.winterhavenmc.util.messagebuilder.model.recipient.ValidRecipient;
import com.winterhavenmc.util.messagebuilder.pipeline.cooldown.CooldownMap;
import com.winterhavenmc.util.messagebuilder.model.language.message.FinalMessageRecord;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;


/**
 * This class is an implementation of the Sender interface, and is used to display a title to a player.
 */
public final class TitleSender implements Sender
{
	private final CooldownMap cooldownMap;


	/**
	 * Class constructor
	 *
	 * @param cooldownMap an instance of the message cooldown map
	 */
	public TitleSender(final CooldownMap cooldownMap)
	{
		this.cooldownMap = cooldownMap;
	}


	/**
	 * Display a title for protoRecipient, if protoRecipient is a player, using the relevant fields from a ValidMessageRecord.
	 *
	 * @param recipient a message protoRecipient
	 * @param messageRecord a message record containing the fields necessary for sending a title to a player
	 */
	@Override
	public void send(final ValidRecipient recipient, final FinalMessageRecord messageRecord)
	{
		if (recipient.sender() instanceof Player player)
		{
			player.sendTitle(
					ChatColor.translateAlternateColorCodes('&', messageRecord.title()),
					ChatColor.translateAlternateColorCodes('&', messageRecord.subtitle()),
					messageRecord.titleFadeIn(),
					messageRecord.titleStay(),
					messageRecord.titleFadeOut());

			cooldownMap.putExpirationTime(recipient, messageRecord);
		}
	}

}
