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

package com.winterhavenmc.library.messagebuilder.models.recipient;

import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.ProxiedCommandSender;

import org.bukkit.entity.Player;


/**
 * A sealed interface and its related types that represent different categories
 * of message recipients in a player message context.
 * <p>
 * This model encapsulates information about the origin of a command or message
 * recipient and classifies it into one of three known types:
 *
 * <ul>
 *   <li>{@link Recipient.Valid} – A recognized sender type capable of receiving messages directly
 *       (e.g., {@link org.bukkit.entity.Player}, {@link org.bukkit.command.ConsoleCommandSender})</li>
 *   <li>{@link Recipient.Proxied} – A special wrapper for {@link org.bukkit.command.ProxiedCommandSender}
 *       that maintains both the proxied and originating sender</li>
 *   <li>{@link Recipient.Invalid} – An unrecognized or {@code null} sender, with an accompanying reason</li>
 * </ul>
 *
 * <p>This design follows a data-driven pattern: recipients are validated and categorized
 * during construction via the static factory method {@link #of(CommandSender)}, which guarantees
 * type safety and clarity at the call site.
 *
 * <p>Both {@code Valid} and {@code Proxied} subtypes implement the {@link Sendable} interface, which
 * can be used to differentiate between the {@code Invalid} subtype using the {@code instanceof} operator.
 *
 * <p>Typical usage:
 * {@snippet lang="java":
 *  Recipient recipient = Recipient.of(player);
 *
 *  if (recipient instanceof Recipient.Sendable sendable) {
 *      sendable.sender().sendMessage("Hello!");
 *  }
 * }
 *
 * @see Recipient.Sendable
 * @see Recipient.Valid
 * @see Recipient.Proxied
 * @see Recipient.Invalid
 * @see Recipient.InvalidReason
 */
public sealed interface Recipient permits Recipient.Valid, Recipient.Proxied, Recipient.Invalid
{
	/**
	 * A marker subinterface for {@link Recipient} types that are capable of
	 * receiving messages directly.
	 * <p>
	 * Includes {@link Valid} and {@link Proxied} recipients.
	 */
	sealed interface Sendable permits Recipient.Valid, Recipient.Proxied
	{
		/**
		 * The actual {@link CommandSender} that may receive messages.
		 *
		 * @return the target sender
		 */
		CommandSender sender();
	}

	/**
	 * Represents a valid, recognized message recipient such as a player, console,
	 * or command block.
	 *
	 * @param sender the original {@link CommandSender}
	 */
	record Valid(CommandSender sender) implements Recipient, Sendable { }


	/**
	 * Represents a proxied command sender (e.g., from another plugin or dispatch chain).
	 * Maintains both the original sender and the {@link ProxiedCommandSender} wrapper.
	 *
	 * @param sender the origin {@link CommandSender}
	 * @param proxy  the {@link ProxiedCommandSender} that is delegating the command
	 */
	record Proxied(CommandSender sender, ProxiedCommandSender proxy) implements Recipient, Sendable { }


	/**
	 * Represents an invalid or unrecognized message recipient.
	 * <p>
	 * This can occur if the sender is {@code null} or of a type not
	 * explicitly handled by the library.
	 *
	 * @param sender        the original {@link CommandSender}, if any
	 * @param invalidReason the reason this recipient was marked invalid
	 */
	record Invalid(CommandSender sender, InvalidReason invalidReason) implements Recipient { }


	/**
	 * Indicates the reason a {@link Recipient} was deemed invalid.
	 */
	enum InvalidReason { NULL, OTHER }


	/**
	 * Factory method that analyzes the given {@link CommandSender} and returns a categorized
	 * {@link Recipient} instance.
	 * <ul>
	 *   <li>If the sender is a {@link Player}, {@link ConsoleCommandSender}, or {@link BlockCommandSender},
	 *       a {@link Valid} recipient is returned</li>
	 *   <li>If the sender is a {@link ProxiedCommandSender}, a {@link Proxied} recipient is returned</li>
	 *   <li>If the sender is {@code null}, an {@link Invalid} recipient with reason {@code NULL} is returned</li>
	 *   <li>Otherwise, an {@link Invalid} recipient with reason {@code OTHER} is returned</li>
	 * </ul>
	 *
	 * @param sender the {@link CommandSender} to classify
	 * @return a concrete {@link Recipient} representing the sender’s classification
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
