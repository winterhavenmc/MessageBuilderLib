/**
 * Contains a sealed model hierarchy representing classified message recipients
 * in a Bukkit-based command or messaging context.
 * <p>
 * The {@link com.winterhavenmc.library.messagebuilder.model.recipient.Recipient} interface
 * and its nested types allow plugin developers to reason about the type of a {@link org.bukkit.command.CommandSender}
 * at runtime using a type-safe, data-driven approach.
 *
 * <h2>Recipient Categories</h2>
 * <ul>
 *   <li>{@link com.winterhavenmc.library.messagebuilder.model.recipient.Recipient.Valid} –
 *       A standard, recognized sender such as a player, console, or command block</li>
 *   <li>{@link com.winterhavenmc.library.messagebuilder.model.recipient.Recipient.Proxied} –
 *       A {@link org.bukkit.command.ProxiedCommandSender ProxiedCommandSender} that represents a delegated command flow</li>
 *   <li>{@link com.winterhavenmc.library.messagebuilder.model.recipient.Recipient.Invalid} –
 *       A null or unsupported sender, accompanied by a reason code</li>
 * </ul>
 *
 * <h2>Usage</h2>
 * The factory method {@link com.winterhavenmc.library.messagebuilder.model.recipient.Recipient#of(CommandSender)}
 * is used to classify any incoming {@link org.bukkit.command.CommandSender}, and returns a
 * sealed {@code Recipient} subtype appropriate to its role.
 *
 * <pre>{@code
 * Recipient recipient = Recipient.of(sender);
 * if (recipient instanceof Recipient.Sendable sendable) {
 *     sendable.sender().sendMessage("Welcome!");
 * }
 * }</pre>
 *
 * This model is typically used by the message dispatch layer of the library to safely and clearly
 * manage how messages are sent based on the sender's context.
 *
 * @see com.winterhavenmc.library.messagebuilder.model.recipient.Recipient
 * @see com.winterhavenmc.library.messagebuilder.model.recipient.Recipient.Valid
 * @see com.winterhavenmc.library.messagebuilder.model.recipient.Recipient.Proxied
 * @see com.winterhavenmc.library.messagebuilder.model.recipient.Recipient.Invalid
 * @see org.bukkit.command.CommandSender CommandSender
 * @see org.bukkit.command.ProxiedCommandSender ProxiedCommandSender
 */
package com.winterhavenmc.library.messagebuilder.model.recipient;

import org.bukkit.command.CommandSender;