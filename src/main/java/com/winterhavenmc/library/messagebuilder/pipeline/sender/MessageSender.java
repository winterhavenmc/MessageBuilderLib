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
import com.winterhavenmc.library.messagebuilder.pipeline.cooldown.CooldownMap;
import com.winterhavenmc.library.messagebuilder.model.language.FinalMessageRecord;

import org.bukkit.ChatColor;


/**
 * Sends chat messages to a {@link com.winterhavenmc.library.messagebuilder.model.recipient.Recipient.Sendable}
 * based on the contents of a {@link com.winterhavenmc.library.messagebuilder.model.language.FinalMessageRecord}.
 *
 * <p>This implementation handles:
 * <ul>
 *   <li>Chat color translation using {@code '&'} codes</li>
 *   <li>Empty or disabled messages gracefully</li>
 *   <li>Cooldown tracking via a {@link com.winterhavenmc.library.messagebuilder.pipeline.cooldown.CooldownMap}</li>
 * </ul>
 *
 * <p>It is typically used to deliver the main message body, rather than titles or subtitles.
 *
 * @see Sender
 * @see com.winterhavenmc.library.messagebuilder.model.language.FinalMessageRecord
 * @see com.winterhavenmc.library.messagebuilder.pipeline.cooldown.CooldownMap
 */
public final class MessageSender implements Sender
{
	private final CooldownMap cooldownMap;


	/**
	 * Constructs a {@code MessageSender} with the specified cooldown map for managing
	 * message repeat suppression.
	 *
	 * @param cooldownMap the cooldown map used to track and store message cooldowns
	 */
	public MessageSender(final CooldownMap cooldownMap)
	{
		this.cooldownMap = cooldownMap;
	}


	/**
	 * Sends a chat message to the specified recipient if the message is enabled and contains content.
	 * Cooldowns are recorded after sending.
	 *
	 * <p>The message text is color-translated using {@code '&'} codes.
	 *
	 * @param recipient the message recipient
	 * @param messageRecord the final message record with resolved string content
	 */
	@Override
	public void send(final Recipient.Sendable recipient, final FinalMessageRecord messageRecord)
	{
		if (messageRecord.enabled()
				&& messageRecord.finalMessageString().isPresent()
				&& !messageRecord.finalMessageString().get().isBlank())
		{
			recipient.sender().sendMessage(ChatColor.translateAlternateColorCodes('&',
					messageRecord.finalMessageString().orElse("")));
			cooldownMap.putExpirationTime(recipient, messageRecord);
		}
	}

}
