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

import static com.winterhavenmc.util.messagebuilder.util.LocalizedException.MessageKey.PARAMETER_NULL;
import static com.winterhavenmc.util.messagebuilder.util.LocalizedException.Parameter.MESSAGE_RECORD;
import static com.winterhavenmc.util.messagebuilder.util.LocalizedException.Parameter.RECIPIENT;

public class MessageSender implements Sender {
	@Override
	public void send(final CommandSender recipient, final MessageRecord messageRecord) {
		if (recipient == null) { throw new LocalizedException(PARAMETER_NULL, RECIPIENT); }
		if (messageRecord == null) { throw new LocalizedException(PARAMETER_NULL, MESSAGE_RECORD); }

		recipient.sendMessage(ChatColor.translateAlternateColorCodes('&', messageRecord.finalMessageString()));
	}
}
