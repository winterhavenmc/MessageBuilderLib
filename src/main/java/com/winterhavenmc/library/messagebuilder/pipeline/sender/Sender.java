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

package com.winterhavenmc.library.messagebuilder.pipeline.sender;

import com.winterhavenmc.library.messagebuilder.model.recipient.Recipient;
import com.winterhavenmc.library.messagebuilder.model.language.FinalMessageRecord;


/**
 * A functional interface representing a message dispatch strategy responsible for
 * delivering a fully rendered {@link com.winterhavenmc.library.messagebuilder.model.language.FinalMessageRecord}
 * to a {@link com.winterhavenmc.library.messagebuilder.model.recipient.Recipient.Sendable}.
 *
 * <p>Implementations of this interface define how a message is transmitted, such as through
 * standard chat output, titles, or other in-game message channels.
 *
 * <p>This is typically the final step of the message delivery lifecycle.
 *
 * @see com.winterhavenmc.library.messagebuilder.model.recipient.Recipient.Sendable
 * @see com.winterhavenmc.library.messagebuilder.model.language.FinalMessageRecord
 * @see com.winterhavenmc.library.messagebuilder.pipeline.sender.MessageSender
 * @see com.winterhavenmc.library.messagebuilder.pipeline.sender.TitleSender
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
