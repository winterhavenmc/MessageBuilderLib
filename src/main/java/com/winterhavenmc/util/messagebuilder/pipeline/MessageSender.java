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

import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.messages.MessageRecord;

import com.winterhavenmc.util.messagebuilder.util.LocalizedException;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Objects;

import static com.winterhavenmc.util.messagebuilder.util.MessageKey.PARAMETER_NULL;
import static com.winterhavenmc.util.messagebuilder.util.Parameter.MESSAGE_RECORD;
import static com.winterhavenmc.util.messagebuilder.util.Parameter.RECIPIENT;
import static com.winterhavenmc.util.messagebuilder.util.Validate.validate;

public class MessageSender implements Sender {

	private final CooldownMap cooldownMap;


	public MessageSender(final CooldownMap cooldownMap) {
		this.cooldownMap = cooldownMap;
	}

	@Override
	public void send(final CommandSender recipient, final MessageRecord messageRecord) {
		validate(recipient, Objects::isNull, () -> new LocalizedException(PARAMETER_NULL, RECIPIENT));
		validate(messageRecord, Objects::isNull, () -> new LocalizedException(PARAMETER_NULL, MESSAGE_RECORD));

		recipient.sendMessage(ChatColor.translateAlternateColorCodes('&', messageRecord.finalMessageString()));

		cooldownMap.putExpirationTime(recipient, messageRecord);
	}

}
