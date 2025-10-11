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

/**
 * Contains the sealed {@link com.winterhavenmc.library.messagebuilder.core.message.Message}
 * interface and its implementations, which represent composed messages ready for macro
 * substitution and delivery.
 * <p>
 * A {@code Message} is constructed through the library's fluent builder API, with macro
 * values bound dynamically to placeholders. Once composed, the message can be dispatched
 * using {@link com.winterhavenmc.library.messagebuilder.core.message.Message#send()}.
 *
 * <h2>Message Implementations</h2>
 * This package defines two implementations:
 *
 * <ul>
 *   <li>{@link com.winterhavenmc.library.messagebuilder.core.message.ValidMessage} –
 *       A fully constructed and resolvable message that contains a valid recipient,
 *       a message template string, a macro object map, and a {@code MessagePipeline}</li>
 *
 *   <li>{@link com.winterhavenmc.library.messagebuilder.core.message.InvalidMessage} –
 *       A fallback or no-op message representing a failure to compose a valid message,
 *       typically due to missing or null input</li>
 * </ul>
 *
 * <h2>Usage</h2>
 * Developers do not typically instantiate message types directly. Instead, messages are created
 * via the fluent API exposed by {@link com.winterhavenmc.library.messagebuilder.MessageBuilder}:
 * {@snippet lang="java":
 * messageBuilder.compose(sender, MessageId.LOGIN_SUCCESS)
 *               .setMacro(Macro.PLAYER, player)
 *               .send();
 * }
 * <p>
 * If the recipient or message string is invalid, the library will return an
 * {@link com.winterhavenmc.library.messagebuilder.core.message.InvalidMessage InvalidMessage}, which safely performs no action when sent.
 *
 * @see com.winterhavenmc.library.messagebuilder.core.message.Message
 * @see com.winterhavenmc.library.messagebuilder.core.message.ValidMessage
 * @see com.winterhavenmc.library.messagebuilder.core.message.InvalidMessage
 * @see com.winterhavenmc.library.messagebuilder.models.recipient.Recipient Recipient
 * @see com.winterhavenmc.library.messagebuilder.pipeline.MessagePipeline MessagePipeline
 */
package com.winterhavenmc.library.messagebuilder.core.message;
