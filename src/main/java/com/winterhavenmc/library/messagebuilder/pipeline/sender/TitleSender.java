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
import org.bukkit.entity.Player;


/**
 * Sends a title and optional subtitle to a {@link org.bukkit.entity.Player}
 * using values from a {@link com.winterhavenmc.library.messagebuilder.model.language.FinalMessageRecord}.
 *
 * <p>This implementation ensures that:
 * <ul>
 *   <li>Only players receive title messages (non-player senders are ignored)</li>
 *   <li>Title and subtitle strings are validated for presence before sending</li>
 *   <li>Colors are translated using {@code '&'} codes</li>
 *   <li>Cooldowns are applied using the provided {@link com.winterhavenmc.library.messagebuilder.pipeline.cooldown.CooldownMap}</li>
 * </ul>
 *
 * @see Sender
 * @see com.winterhavenmc.library.messagebuilder.model.language.FinalMessageRecord
 * @see com.winterhavenmc.library.messagebuilder.pipeline.cooldown.CooldownMap
 */
public final class TitleSender implements Sender
{
	private final CooldownMap cooldownMap;


	/**
	 * Constructs a {@code TitleSender} using the specified cooldown map.
	 *
	 * @param cooldownMap an instance of the message cooldown map used to prevent redundant delivery
	 */
	public TitleSender(final CooldownMap cooldownMap)
	{
		this.cooldownMap = cooldownMap;
	}


	/**
	 * Sends a title and subtitle to a player if applicable, using values from the given message record.
	 *
	 * <p>This method checks whether the sender is a {@link org.bukkit.entity.Player}, whether the
	 * message is enabled, and whether at least one of the final title or subtitle strings is non-empty.
	 * Cooldown state is recorded upon successful delivery.
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
			player.sendTitle(
					ChatColor.translateAlternateColorCodes('&', messageRecord.finalTitleString().orElse("")),
					ChatColor.translateAlternateColorCodes('&', messageRecord.finalSubtitleString().orElse("")),
					messageRecord.titleFadeIn(),
					messageRecord.titleStay(),
					messageRecord.titleFadeOut());

			cooldownMap.putExpirationTime(recipient, messageRecord);
		}
	}

}
