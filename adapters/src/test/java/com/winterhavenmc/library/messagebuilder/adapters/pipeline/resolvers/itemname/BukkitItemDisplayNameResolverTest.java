package com.winterhavenmc.library.messagebuilder.adapters.pipeline.resolvers.itemname;

import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.resolvers.itemname.ItemDisplayNameResolver;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class BukkitItemDisplayNameResolverTest
{
	@Test
	@Disabled("needs static mock server")
	void resolve_with_null_parameter_returns_empty_string()
	{
		// Arrange
		ItemDisplayNameResolver resolver = new BukkitItemDisplayNameResolver();

		// Act
		String result = resolver.resolve(null);

		// Assert
		assertEquals("", result);
	}


	@Test
	@Disabled("needs static mock server")
	void resolve_with_valid_parameter_returns_displayname()
	{
		// Arrange
		ItemStack itemStack = new ItemStack(Material.GOLDEN_PICKAXE);
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.setDisplayName("display name");
		itemStack.setItemMeta(itemMeta);

		ItemDisplayNameResolver resolver = new BukkitItemDisplayNameResolver();

		// Act
		String result = resolver.resolve(itemStack);

		// Assert
		assertEquals("display name", result);
	}


	@Test
	@Disabled("needs static mock server")
	void resolve_with_valid_parameter_without_displayname_returns_item_name()
	{
		// Arrange
		ItemStack itemStack = new ItemStack(Material.GOLDEN_PICKAXE);
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.setItemName("item name");
		itemMeta.setDisplayName("display name");
		itemStack.setItemMeta(itemMeta);

		ItemDisplayNameResolver resolver = new BukkitItemDisplayNameResolver();

		// Act
		String result = resolver.resolve(itemStack);

		// Assert
		assertEquals("item name", result);

		// Verify
		verify(itemMeta, atLeastOnce()).hasDisplayName();
		verify(itemMeta, atLeastOnce()).hasItemName();
		verify(itemMeta, atLeastOnce()).getItemName();
	}

}
