package com.winterhavenmc.library.messagebuilder.core.ports.pipeline.resolvers.itemname;

import org.bukkit.inventory.ItemStack;


/**
 * Declares an interface for ItemStacks that have a display name metadata field
 */
public interface ItemDisplayNameResolver
{
	String resolve(ItemStack itemStack);
}
