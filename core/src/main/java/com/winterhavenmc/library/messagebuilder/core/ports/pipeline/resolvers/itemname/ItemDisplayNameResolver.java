package com.winterhavenmc.library.messagebuilder.core.ports.pipeline.resolvers.itemname;

import org.bukkit.inventory.ItemStack;

public interface ItemDisplayNameResolver
{
	String resolve(ItemStack itemStack);
}
