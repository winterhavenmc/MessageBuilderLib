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

package com.winterhavenmc.library.messagebuilder.adapters.pipeline.senders;

import com.winterhavenmc.library.messagebuilder.adapters.pipeline.cooldown.MessageCooldownMap;
import com.winterhavenmc.library.messagebuilder.core.ports.senders.Sender;
import com.winterhavenmc.library.messagebuilder.models.language.FinalMessageRecord;
import com.winterhavenmc.library.messagebuilder.models.recipient.Recipient;

import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;


/**
 * Sends chat messages to a {@link com.winterhavenmc.library.messagebuilder.models.recipient.Recipient.Sendable}
 * based on the contents of a {@link com.winterhavenmc.library.messagebuilder.models.language.FinalMessageRecord}.
 *
 * <p>This implementation handles:
 * <ul>
 *   <li>Kyori Adventure style formatting tags</li>
 *   <li>Empty or disabled messages gracefully</li>
 *   <li>CooldownMap tracking via a {@link MessageCooldownMap}</li>
 * </ul>
 *
 * <p>It is typically used to deliver the main message body, rather than titles or subtitles.
 *
 * @see Sender
 * @see com.winterhavenmc.library.messagebuilder.models.language.FinalMessageRecord
 * @see MessageCooldownMap
 */
public final class KyoriMessageSender implements Sender
{
	private final MessageCooldownMap messageCooldownMap;
	private final MiniMessage miniMessage;
	private final BukkitAudiences audiences;


	/**
	 * Constructs a {@code KyoriMessageSender} with the specified cooldown map for managing
	 * message repeat suppression.
	 *
	 * @param messageCooldownMap the cooldown map used to track and store message cooldowns
	 */
	public KyoriMessageSender(final MessageCooldownMap messageCooldownMap, final MiniMessage miniMessage, final BukkitAudiences audiences)
	{
		this.messageCooldownMap = messageCooldownMap;
		this.miniMessage = miniMessage;
		this.audiences = audiences;
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
			Component component = miniMessage.deserialize(messageRecord.finalMessageString().get());
			audiences.sender(recipient.sender()).sendMessage(component);
			messageCooldownMap.putExpirationTime(recipient, messageRecord);
		}
	}

}
