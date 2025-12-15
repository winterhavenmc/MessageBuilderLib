package com.winterhavenmc.library.messagebuilder.adapters.pipeline.resolvers.itemname;

import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.resolvers.itemname.ItemDisplayNameResolver;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class BukkitItemDisplayNameResolverTest
{
	@Mock ItemStack itemStackMock;
	@Mock ItemMeta itemMetaMock;


	@Test
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
	void resolve_with_valid_parameter_returns_item_displayname()
	{
		// Arrange
		when(itemStackMock.hasItemMeta()).thenReturn(true);
		when(itemStackMock.getItemMeta()).thenReturn(itemMetaMock);
		when(itemMetaMock.hasDisplayName()).thenReturn(true);
		when(itemMetaMock.getDisplayName()).thenReturn("item display name");

		// Act
		ItemDisplayNameResolver resolver = new BukkitItemDisplayNameResolver();
		String result = resolver.resolve(itemStackMock);

		// Assert
		assertEquals("item display name", result);

		// Verify
		verify(itemStackMock, atLeastOnce()).hasItemMeta();
		verify(itemStackMock, atLeastOnce()).getItemMeta();
		verify(itemMetaMock, atLeastOnce()).hasDisplayName();
		verify(itemMetaMock, atLeastOnce()).getDisplayName();
	}


	@Test
	void resolve_itemstack_without_display_name_returns_item_name()
	{
		// Arrange
		when(itemStackMock.hasItemMeta()).thenReturn(true);
		when(itemStackMock.getItemMeta()).thenReturn(itemMetaMock);
		when(itemMetaMock.hasItemName()).thenReturn(true);
		when(itemMetaMock.getItemName()).thenReturn("item name");

		// Act
		ItemDisplayNameResolver resolver = new BukkitItemDisplayNameResolver();
		String result = resolver.resolve(itemStackMock);

		// Assert
		assertEquals("item name", result);

		// Verify
		verify(itemStackMock, atLeastOnce()).hasItemMeta();
		verify(itemStackMock, atLeastOnce()).getItemMeta();
		verify(itemMetaMock, atLeastOnce()).hasItemName();
		verify(itemMetaMock, atLeastOnce()).getItemName();
	}

}
