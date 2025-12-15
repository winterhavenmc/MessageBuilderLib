package com.winterhavenmc.library.messagebuilder.core.ports.pipeline.retrievers;

import org.bukkit.inventory.ItemStack;

import java.util.Optional;


public interface ItemStackNameRetriever
{
	Optional<String> retrieve(ItemStack itemStack);
}
