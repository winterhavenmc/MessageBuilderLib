package com.winterhavenmc.library.messagebuilder.core.ports.resources.language;

import com.winterhavenmc.library.messagebuilder.models.keys.ItemKey;
import com.winterhavenmc.library.messagebuilder.models.keys.ValidItemKey;
import com.winterhavenmc.library.messagebuilder.models.language.ItemRecord;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.Optional;


public interface ItemRepository
{
	ItemKey key(ItemStack itemStack);

	Optional<ItemRecord> record(ValidItemKey validItemKey);

	Optional<ItemStack> createItem(ValidItemKey validItemKey);

	Optional<ItemStack> createItem(ValidItemKey validItemKey, int quantity);

	Optional<ItemStack> createItem(ValidItemKey validItemKey, int quantity, Map<String, String> replacements);

	Optional<String> name(ValidItemKey validItemKey);

	Optional<String> displayName(ValidItemKey validItemKey);

	boolean isItem(ItemStack itemStack);

	@Deprecated
	ItemRecord getRecord(ValidItemKey validItemKey);
}
