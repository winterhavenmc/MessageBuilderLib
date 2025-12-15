package com.winterhavenmc.library.messagebuilder.adapters.pipeline.retrievers.itemname;

import com.winterhavenmc.library.messagebuilder.adapters.pipeline.retrievers.Retriever;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Optional;


public class ItemNameRetriever implements Retriever
{
	public Optional<String> retrieve(final ItemStack itemStack)
	{
		return Optional.ofNullable(itemStack.getItemMeta())
				.filter(ItemMeta::hasItemName)
				.map(ItemMeta::getItemName)
				.map(ChatColor::stripColor);
	}

}
