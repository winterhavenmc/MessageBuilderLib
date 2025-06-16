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


/**
 * A sealed interface and records that define a message recipient
 */
public sealed interface Recipient permits Recipient.Valid, Recipient.Proxied, Recipient.Invalid
{
	/**
	 * A marker interface that denotes a Recipient capable of receiving messages
	 */
	sealed interface Sendable permits Recipient.Valid, Recipient.Proxied
	{
		CommandSender sender();
	}

	record Valid(CommandSender sender) implements Recipient, Sendable { }
	record Proxied(CommandSender sender, ProxiedCommandSender proxy) implements Recipient, Sendable { }
	record Invalid(CommandSender sender, InvalidReason invalidReason) implements Recipient { }


	/**
	 * Enum defining the reason an InvalidRecipient was returned
	 */
	enum InvalidReason { NULL, OTHER }


	/**
	 * Create a Recipient object from a CommandSender
	 *
	 * @param sender the CommandSender used in the creation of the Recipient
	 * @return the newly created recipient of the appropriate subtype
	 */
	static Recipient of(final CommandSender sender)
	{
		return switch (sender)
		{
			case Player __ -> new Valid(sender);
			case ConsoleCommandSender __ -> new Valid(sender);
			case BlockCommandSender __ -> new Valid(sender);
			case ProxiedCommandSender proxy -> new Proxied(sender, proxy);
			case null -> new Invalid(null, InvalidReason.NULL);
			default -> new Invalid(sender, InvalidReason.OTHER);
		};
	}

}
