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

import com.winterhavenmc.library.messagebuilder.model.language.FinalMessageRecord;
import com.winterhavenmc.library.messagebuilder.model.recipient.Recipient;
import com.winterhavenmc.library.messagebuilder.pipeline.cooldown.CooldownMap;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;

import org.bukkit.entity.Player;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;


public final class PaperTitleSender implements Sender
{
	private final CooldownMap cooldownMap;
	private final MethodHandle sendTitlePartHandle;
	private final Object titlePartTitle;
	private final Object titlePartSubtitle;
	private final Object titlePartTimes;


	public PaperTitleSender(final CooldownMap cooldownMap,
							final MiniMessage miniMessage,
							final BukkitAudiences audiences) throws ReflectiveOperationException
	{
		this.cooldownMap = cooldownMap;

		// Resolve once at startup
		Class<?> titlePartClass = Class.forName("net.kyori.adventure.title.TitlePart");
		Class<?> playerClass = Class.forName("org.bukkit.entity.Player");

		// Grab the enum constants TITLE, SUBTITLE, TIMES
		Object[] constants = titlePartClass.getEnumConstants();
		Object titleConst = null, subtitleConst = null, timesConst = null;
		for (Object c : constants)
		{
			String name = c.toString().toUpperCase();
			switch (name)
			{
				case "TITLE" -> titleConst = c;
				case "SUBTITLE" -> subtitleConst = c;
				case "TIMES" -> timesConst = c;
			}
		}
		this.titlePartTitle = titleConst;
		this.titlePartSubtitle = subtitleConst;
		this.titlePartTimes = timesConst;

		// Prepare method handle: void Player.sendTitlePart(TitlePart, Component)
//		MethodType sig = MethodType.methodType(void.class, titlePartClass, Component.class);
		MethodType sig = MethodType.methodType(void.class, titlePartClass, Object.class);

		this.sendTitlePartHandle = MethodHandles.lookup().findVirtual(playerClass, "sendTitlePart", sig);
	}


	@Override
	public void send(Recipient.Sendable recipient, FinalMessageRecord messageRecord)
	{
		try {
			Component title = MiniMessage.miniMessage().deserialize(messageRecord.message());
			Component subtitle = MiniMessage.miniMessage().deserialize(messageRecord.subtitle());
			Title.Times times = Title.Times.times(
					messageRecord.titleFadeIn(),
					messageRecord.titleStay(),
					messageRecord.titleFadeOut()
			);

			if (recipient.sender() instanceof Player player)
			{
				// Invoke reflectively
				sendTitlePartHandle.invoke(player, titlePartTitle, title);
				sendTitlePartHandle.invoke(player, titlePartSubtitle, subtitle);
				sendTitlePartHandle.invoke(player, titlePartTimes, times);
			}

			cooldownMap.putExpirationTime(recipient, messageRecord);

		}
		catch (Throwable t)
		{
			// If Paper breaks API, fallback silently (or log once)
			t.printStackTrace();
			// Could call Spigot strategy here if you inject one
		}
	}

}
