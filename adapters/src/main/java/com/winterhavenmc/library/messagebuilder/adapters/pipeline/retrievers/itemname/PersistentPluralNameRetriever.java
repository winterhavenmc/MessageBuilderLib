package com.winterhavenmc.library.messagebuilder.adapters.pipeline.retrievers.itemname;

import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.retrievers.ItemStackNameRetriever;
import com.winterhavenmc.library.messagebuilder.adapters.resources.language.YamlItemRepository;
import com.winterhavenmc.library.messagebuilder.models.Delimiter;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.Optional;
import java.util.regex.Pattern;


public class PersistentPluralNameRetriever implements ItemStackNameRetriever
{
	private final static String PLURAL_KEY_STRING = "PLURAL_NAME";
	private final Plugin plugin;
	private final MiniMessage miniMessage;


	public PersistentPluralNameRetriever(final Plugin plugin, final MiniMessage miniMessage)
	{
		this.plugin = plugin;
		this.miniMessage = miniMessage;
	}


	public Optional<String> retrieve(final ItemStack itemStack)
	{
		if (itemStack.getItemMeta() instanceof ItemMeta itemMeta)
		{
			NamespacedKey pluralKey = new NamespacedKey(plugin, PLURAL_KEY_STRING);
			if (itemMeta.getPersistentDataContainer().has(pluralKey, PersistentDataType.STRING))
			{
				String persistedPluralName = itemMeta.getPersistentDataContainer().get(pluralKey, PersistentDataType.STRING);

				if (persistedPluralName != null && !persistedPluralName.isBlank())
				{
					String result = deserializePluralName(persistedPluralName, itemStack.getAmount());
					return Optional.ofNullable(itemMeta.getPersistentDataContainer().get(pluralKey, PersistentDataType.STRING));
				}
			}
		}
		return Optional.empty();
	}


	String deserializePluralName(final String string, final int amount)
	{
		String pluralString = string.replaceAll(
				Pattern.quote(Delimiter.OPEN + "QUANTITY" + Delimiter.CLOSE),
				String.valueOf(amount)
		);

		Component component = miniMessage.deserialize(
				pluralString,
				Formatter.choice("choice", amount)
		);

		return YamlItemRepository.LEGACY_SERIALIZER.serializeOr(component, "");
	}
}
