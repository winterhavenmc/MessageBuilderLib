package com.winterhavenmc.library.messagebuilder.core.ports.resolvers.itemname;

import org.bukkit.inventory.ItemStack;

public interface ItemNameResolver
{
	String resolve(ItemStack itemStack);
}
