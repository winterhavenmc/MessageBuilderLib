package com.winterhavenmc.library.messagebuilder.core.ports.resources.language;

import com.winterhavenmc.library.messagebuilder.models.keys.ItemKey;
import com.winterhavenmc.library.messagebuilder.models.keys.ValidItemKey;
import com.winterhavenmc.library.messagebuilder.models.language.ItemRecord;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.Optional;


public interface ItemRepository
{
	ItemRecord getRecord(ValidItemKey validItemKey);

	Optional<ItemStack> createItem(String key);

	Optional<ItemStack> createItem(String key, int quantity);

	Optional<ItemStack> createItem(String key, int quantity, Map<String, String> replacements);

	Optional<ItemStack> createItem(ValidItemKey validItemKey);

	Optional<ItemStack> createItem(ValidItemKey validItemKey, int quantity);

	Optional<ItemStack> createItem(ValidItemKey validItemKey, int quantity, Map<String, String> replacements);

	Optional<String> getItemName(String key);

	Optional<String> getItemDisplayName(String key);

	Optional<ItemRecord> getItemRecord(String key);

	Optional<ItemRecord> getItemRecord(ValidItemKey validItemKey);

	boolean isItem(ItemStack itemStack);

	ItemKey getItemKey(ItemStack itemStack);
}
