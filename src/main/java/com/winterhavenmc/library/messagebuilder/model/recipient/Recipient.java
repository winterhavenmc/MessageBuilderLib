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

package com.winterhavenmc.library.messagebuilder.model.recipient;

import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.ProxiedCommandSender;
import org.bukkit.entity.Player;


public sealed interface Recipient permits Recipient.Valid, Recipient.Proxied, Recipient.Invalid
{
	enum InvalidReason { NULL, OTHER }
	record Valid(CommandSender sender) implements Recipient { }
	record Proxied(CommandSender sender, ProxiedCommandSender proxy) implements Recipient { }
	record Invalid(CommandSender sender, InvalidReason invalidReason) implements Recipient { }


	static Recipient of(final CommandSender sender)
	{
		return switch (sender)
		{
			case Player ignored -> new Valid(sender);
			case ConsoleCommandSender ignored -> new Valid(sender);
			case BlockCommandSender ignored -> new Valid(sender);
			case ProxiedCommandSender proxy -> new Proxied(sender, proxy);
			case null -> new Invalid(sender, InvalidReason.NULL);
			default -> new Invalid(sender, InvalidReason.OTHER);
		};
	}

}
