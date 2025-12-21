package com.winterhavenmc.library.messagebuilder.adapters.pipeline.senders;

import com.winterhavenmc.library.messagebuilder.adapters.pipeline.cooldown.MessageCooldownMap;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.senders.Sender;
import com.winterhavenmc.library.messagebuilder.core.ports.resources.sound.SoundRepository;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class SenderFactory
{
	public static List<Sender> createSenders(final Plugin plugin, final MessageCooldownMap messageCooldownMap, final SoundRepository sounds)
	{
		final MiniMessage miniMessage = MiniMessage.miniMessage();
		final BukkitAudiences bukkitAudiences = BukkitAudiences.create(plugin);
		final KyoriMessageSender messageSender = new KyoriMessageSender(messageCooldownMap, miniMessage, bukkitAudiences, sounds);
		final KyoriTitleSender titleSender = new KyoriTitleSender(messageCooldownMap, miniMessage, bukkitAudiences);

		return List.of(messageSender, titleSender);
	}
}
