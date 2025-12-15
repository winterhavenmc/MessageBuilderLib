package com.winterhavenmc.library.messagebuilder.adapters.pipeline.retrievers.itemname;

import org.bukkit.inventory.ItemStack;

import java.util.Optional;


public interface NameRetriever
{
	Optional<String> retrieve(ItemStack itemStack);
}
