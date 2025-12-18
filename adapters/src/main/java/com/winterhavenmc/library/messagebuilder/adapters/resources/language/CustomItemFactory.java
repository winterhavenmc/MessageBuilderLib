package com.winterhavenmc.library.messagebuilder.adapters.resources.language;

import com.winterhavenmc.library.messagebuilder.models.Delimiter;
import com.winterhavenmc.library.messagebuilder.models.language.item.ValidItemRecord;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.winterhavenmc.library.messagebuilder.adapters.resources.language.YamlItemRepository.LEGACY_SERIALIZER;


/**
 * Responsible for the creation of custom items as declared in the language file
 */
public final class CustomItemFactory
{
	static final String ITEM_KEY_STRING = "ITEM_KEY";
	private static final Material DEFAULT_MATERIAL = Material.STICK;
	private static final String PLURAL_KEY_STRING = "PLURAL_NAME";

	private final Plugin plugin;
	private final NamespacedKey namespacedKey;
	private final MiniMessage miniMessage;


	/**
	 * Class constructor
	 *
	 * @param plugin instance of plugin
	 * @param miniMessage instance of MiniMessage
	 */
	public CustomItemFactory(final Plugin plugin, final MiniMessage miniMessage)
	{
		this.plugin = plugin;
		this.miniMessage = miniMessage;
		this.namespacedKey = new NamespacedKey(plugin, ITEM_KEY_STRING);
	}


	Optional<ItemStack> createItem(final ValidItemRecord validItemRecord,
								   final int quantity,
								   final Map<String, String> replacements)
	{
		Material material = Material.matchMaterial(validItemRecord.material());
		if (material == null || !material.isItem())
		{
			material = DEFAULT_MATERIAL;
		}
		ItemStack itemStack = new ItemStack(material, quantity);
		ItemMeta itemMeta = itemStack.getItemMeta();
		if (itemMeta != null)
		{
			setItemName(validItemRecord, itemMeta, replacements);
			setItemDisplayName(validItemRecord, itemMeta, replacements);
			setItemLore(validItemRecord, itemMeta, replacements);
			setItemPersistentData(validItemRecord, itemMeta);
			setItemFlags(itemMeta);
		}
		itemStack.setItemMeta(itemMeta);
		return Optional.of(itemStack);
	}


	private void setItemName(final ValidItemRecord validItemRecord, final ItemMeta itemMeta)
	{
		setItemName(validItemRecord, itemMeta, Map.of());
	}


	private void setItemName(final ValidItemRecord validItemRecord,
							 final ItemMeta itemMeta,
							 final Map<String, String> replacements)
	{
		String itemName = validItemRecord.name();

		if (itemName != null && !itemName.isEmpty())
		{
			for (Map.Entry<String, String> entry : replacements.entrySet())
			{
				itemName = itemName.replace(Delimiter.OPEN + entry.getKey() + Delimiter.CLOSE, entry.getValue());
			}

			Component nameComponent = miniMessage.deserialize(itemName);
			String formattedName = LEGACY_SERIALIZER.serialize(nameComponent);
			itemMeta.setItemName(formattedName);
		}
	}


	private void setItemDisplayName(final ValidItemRecord validItemRecord, final ItemMeta itemMeta)
	{
		setItemDisplayName(validItemRecord, itemMeta, Map.of());
	}


	private void setItemDisplayName(final ValidItemRecord validItemRecord,
									final ItemMeta itemMeta,
									final Map<String, String> replacements)
	{
		String displayName = validItemRecord.displayName();

		if (displayName != null && !displayName.isEmpty())
		{
			for (Map.Entry<String, String> entry : replacements.entrySet())
			{
				displayName = displayName.replace(Delimiter.OPEN + entry.getKey() + Delimiter.CLOSE, entry.getValue());
			}

			Component displayNameComponent = miniMessage.deserialize(displayName);
			String formattedDisplayName = LEGACY_SERIALIZER.serialize(displayNameComponent);
			itemMeta.setDisplayName(formattedDisplayName);
		}
	}


	private void setItemLore(final ValidItemRecord validItemRecord, final ItemMeta itemMeta)
	{
		setItemLore(validItemRecord, itemMeta, Map.of());
	}


	private void setItemLore(final ValidItemRecord validItemRecord,
							 final ItemMeta itemMeta,
							 final Map<String, String> replacements)
	{
		if (validItemRecord.lore() != null && !validItemRecord.lore().isEmpty())
		{
			List<String> formattedLore = new ArrayList<>();
			for (String line : validItemRecord.lore())
			{
				for (Map.Entry<String, String> entry : replacements.entrySet())
				{
					line = line.replace(Delimiter.OPEN + entry.getKey() + Delimiter.CLOSE, entry.getValue());
				}
				Component loreLineComponent = miniMessage.deserialize(line);
				formattedLore.add(LEGACY_SERIALIZER.serialize(loreLineComponent));
			}
			itemMeta.setLore(formattedLore);
		}
	}


	private void setItemPersistentData(final ValidItemRecord itemRecord, final ItemMeta itemMeta)
	{
		itemMeta.getPersistentDataContainer().set(namespacedKey, PersistentDataType.STRING, itemRecord.key().toString());

		if (itemRecord.pluralName() != null && !itemRecord.pluralName().isBlank())
		{
			NamespacedKey pluralKey = new NamespacedKey(plugin, PLURAL_KEY_STRING);
			itemMeta.getPersistentDataContainer().set(pluralKey, PersistentDataType.STRING, itemRecord.pluralName());
		}
	}

	private void setItemFlags(final ItemMeta itemMeta)
	{
		itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
	}

}
