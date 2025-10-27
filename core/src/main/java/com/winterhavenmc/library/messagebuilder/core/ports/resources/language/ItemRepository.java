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

	Optional<ItemStack> createItem(ValidItemKey validItemKey);

	Optional<ItemStack> createItem(ValidItemKey validItemKey, int quantity);

	Optional<ItemStack> createItem(ValidItemKey validItemKey, int quantity, Map<String, String> replacements);

	Optional<ItemRecord> getItemRecord(ValidItemKey validItemKey);

	ItemKey getItemKey(ItemStack itemStack);

	Optional<String> getItemDisplayName(String key);
//	Optional<String> getItemDisplayName(ValidItemKey validItemKey);

	boolean isItem(ItemStack itemStack);

	@Deprecated
	Optional<ItemStack> createItem(String key);

	@Deprecated
	Optional<ItemStack> createItem(String key, int quantity);

	@Deprecated
	Optional<ItemStack> createItem(String key, int quantity, Map<String, String> replacements);

	@Deprecated
	Optional<String> getItemName(String key);

	@Deprecated
	Optional<ItemRecord> getItemRecord(String key);
}
