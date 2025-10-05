package com.winterhavenmc.library.messagebuilder.core.ports.resolvers.itemname;

import org.bukkit.inventory.ItemStack;

public interface ItemPluralNameResolver
{
	String resolve(ItemStack itemStack);
}
