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

import com.winterhavenmc.util.messagebuilder.pipeline.cooldown.CooldownMap;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.MessageRecord;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

import static com.winterhavenmc.util.messagebuilder.validation.ExceptionMessageKey.PARAMETER_NULL;
import static com.winterhavenmc.util.messagebuilder.validation.Parameter.MESSAGE_RECORD;
import static com.winterhavenmc.util.messagebuilder.validation.Parameter.RECIPIENT;
import static com.winterhavenmc.util.messagebuilder.validation.ValidationHandler.throwing;
import static com.winterhavenmc.util.messagebuilder.validation.Validator.validate;


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
	 * Display a title for recipient, if recipient is a player, using the relevant fields from a MessageRecord.
	 *
	 * @param recipient a message recipient
	 * @param messageRecord a message record containing the fields necessary for sending a title to a player
	 */
	@Override
	public void send(final CommandSender recipient, final MessageRecord messageRecord)
	{
		validate(recipient, Objects::isNull, throwing(PARAMETER_NULL, RECIPIENT));
		validate(messageRecord, Objects::isNull, throwing(PARAMETER_NULL, MESSAGE_RECORD));

		if (recipient instanceof Player player)
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
