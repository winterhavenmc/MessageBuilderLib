package com.winterhavenmc.library.messagebuilder.adapters.pipeline.resolvers.itemname;

import com.winterhavenmc.library.messagebuilder.core.ports.resolvers.itemname.ItemNameResolver;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class BukkitItemNameResolverTest
{
	@Mock ItemStack itemStackMock;
	@Mock ItemMeta itemMetaMock;


	@Test
	void resolve_with_null_parameter_returns_empty_string()
	{
		// Arrange
		ItemNameResolver resolver = new BukkitItemNameResolver();

		// Act
		String result = resolver.resolve(null);

		// Assert
		assertEquals("", result);
	}


	@Test
	void resolve_with_valid_parameter_returns_item_name()
	{
		// Arrange
		ItemNameResolver resolver = new BukkitItemNameResolver();
		when(itemStackMock.hasItemMeta()).thenReturn(true);
		when(itemStackMock.getItemMeta()).thenReturn(itemMetaMock);
		when(itemMetaMock.hasItemName()).thenReturn(true);
		when(itemMetaMock.getItemName()).thenReturn("item name");

		// Act
		String result = resolver.resolve(itemStackMock);

		// Assert
		assertEquals("item name", result);

		// Verify
		verify(itemStackMock, atLeastOnce()).hasItemMeta();
		verify(itemStackMock, atLeastOnce()).getItemMeta();
		verify(itemMetaMock, atLeastOnce()).hasItemName();
		verify(itemMetaMock, atLeastOnce()).getItemName();
	}


	@Test
	void resolve_with_valid_parameter_without_displayname_returns_item_name()
	{
		// Arrange
		ItemNameResolver resolver = new BukkitItemNameResolver();
		when(itemStackMock.hasItemMeta()).thenReturn(true);
		when(itemStackMock.getItemMeta()).thenReturn(itemMetaMock);
		when(itemMetaMock.hasItemName()).thenReturn(false);
		when(itemMetaMock.hasDisplayName()).thenReturn(true);
		when(itemMetaMock.getDisplayName()).thenReturn("display name");

		// Act
		String result = resolver.resolve(itemStackMock);

		// Assert
		assertEquals("display name", result);

		// Verify
		verify(itemStackMock, atLeastOnce()).hasItemMeta();
		verify(itemStackMock, atLeastOnce()).getItemMeta();
		verify(itemMetaMock, atLeastOnce()).hasItemName();
		verify(itemMetaMock, atLeastOnce()).hasDisplayName();
		verify(itemMetaMock, atLeastOnce()).getDisplayName();
	}

}
