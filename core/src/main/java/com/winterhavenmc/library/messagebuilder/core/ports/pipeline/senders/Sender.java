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

package com.winterhavenmc.library.messagebuilder.core.ports.pipeline.senders;

import com.winterhavenmc.library.messagebuilder.models.language.message.FinalMessageRecord;
import com.winterhavenmc.library.messagebuilder.models.recipient.Recipient;


/**
 * A functional interface representing a message dispatch strategy responsible for
 * delivering a fully rendered {@link FinalMessageRecord}
 * to a {@link Recipient.Sendable}.
 *
 * <p>Implementations of this interface define how a message is transmitted, such as through
 * standard chat output, titles, or other in-game message channels.
 *
 * <p>This is typically the final step of the message delivery lifecycle.
 *
 * @see Recipient.Sendable
 * @see FinalMessageRecord
 */
@FunctionalInterface
public interface Sender
{
	/**
	 * Sends a processed message to the given recipient using the implementation’s delivery strategy.
	 *
	 * @param recipient the recipient of the message
	 * @param messageRecord the final message record with all macros resolved and fields populated
	 */
	void send(Recipient.Sendable recipient, FinalMessageRecord messageRecord);
}
