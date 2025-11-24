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
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.senders.Sender;
import com.winterhavenmc.library.messagebuilder.models.recipient.Recipient;
import com.winterhavenmc.library.messagebuilder.models.language.message.FinalMessageRecord;

import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;

import org.bukkit.entity.Player;


/**
 * Sends a title and optional subtitle to a {@link org.bukkit.entity.Player}
 * using values from a {@link FinalMessageRecord}.
 *
 * <p>This implementation ensures that:
 * <ul>
 *   <li>Only players receive title messages (non-player senders are ignored)</li>
 *   <li>Title and subtitle strings are validated for presence before sending</li>
 *   <li>Colors are translated using {@code '&'} codes</li>
 *   <li>Cooldowns are applied using the provided {@link MessageCooldownMap}</li>
 * </ul>
 *
 * @see Sender
 * @see FinalMessageRecord
 * @see MessageCooldownMap
 */
public final class KyoriTitleSender implements Sender
{
	private final MessageCooldownMap messageCooldownMap;
	private final MiniMessage miniMessage;
	private final BukkitAudiences audiences;


	/**
	 * Constructs a {@code KyoriTitleSender} using the specified cooldown map.
	 *
	 * @param messageCooldownMap an instance of the message cooldown map used to prevent redundant delivery
	 */
	public KyoriTitleSender(final MessageCooldownMap messageCooldownMap, final MiniMessage miniMessage, final BukkitAudiences audiences)
	{
		this.messageCooldownMap = messageCooldownMap;
		this.miniMessage = miniMessage;
		this.audiences = audiences;
	}


	/**
	 * Sends a title and subtitle to a player if applicable, using values from the given message record.
	 *
	 * <p>This method checks whether the sender is a {@link org.bukkit.entity.Player}, whether the
	 * message is enabled, and whether at least one of the final title or subtitle strings is non-empty.
	 * CooldownMap state is recorded upon successful delivery.
	 *
	 * @param recipient the message recipient
	 * @param messageRecord the message record containing the title, subtitle, and timing data
	 */
	@Override
	public void send(final Recipient.Sendable recipient, final FinalMessageRecord messageRecord)
	{
		// if sender is player and at least one title/subtitle string is non-null and non-blank, send title to player
		if (recipient.sender() instanceof Player player
				&& messageRecord.enabled()
				&& (messageRecord.finalTitleString().isPresent() || messageRecord.finalSubtitleString().isPresent()))
		{
			final Component mainTitle = miniMessage.deserialize(messageRecord.finalTitleString().orElse(""));
			final Component subTitle = miniMessage.deserialize(messageRecord.finalSubtitleString().orElse(""));
			final Title.Times times = Title.Times.times(messageRecord.titleFadeIn(), messageRecord.titleStay(), messageRecord.titleFadeOut());
			final Title title = Title.title(mainTitle, subTitle, times);

			audiences.sender(recipient.sender()).showTitle(title);

			messageCooldownMap.putExpirationTime(recipient, messageRecord);
		}
	}

}
